const LifeRecordsComponent = {
    template: `
        <div>
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <h1 class="h2"><i class="fas fa-heart me-2"></i>Life Records</h1>
                <div class="btn-toolbar mb-2 mb-md-0">
                    <button class="btn btn-primary" @click="showAddModal">
                        <i class="fas fa-plus me-1"></i>Add Life Record
                    </button>
                </div>
            </div>

            <!-- Filters -->
            <div class="card mb-4">
                <div class="card-body">
                    <div class="row g-3">
                        <div class="col-md-4">
                            <label class="form-label">Title</label>
                            <input type="text" class="form-control" v-model="filters.title" placeholder="Search by title">
                        </div>
                        <div class="col-md-4">
                            <label class="form-label">Content</label>
                            <input type="text" class="form-control" v-model="filters.content" placeholder="Search in content">
                        </div>
                        <div class="col-md-4">
                            <label class="form-label">Date</label>
                            <input type="date" class="form-control" v-model="filters.createTime">
                        </div>
                    </div>
                    <div class="row mt-3">
                        <div class="col-12">
                            <button class="btn btn-outline-primary me-2" @click="applyFilters">
                                <i class="fas fa-search me-1"></i>Search
                            </button>
                            <button class="btn btn-outline-secondary" @click="clearFilters">
                                <i class="fas fa-times me-1"></i>Clear
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Loading state -->
            <div v-if="loading" class="loading">
                <div class="spinner-border" role="status">
                    <span class="visually-hidden">Loading...</span>
                </div>
                <p class="mt-2">Loading life records...</p>
            </div>

            <!-- Life Records Grid -->
            <div v-else>
                <div v-if="lifeRecords.length === 0" class="text-center py-5">
                    <i class="fas fa-heart fa-3x text-muted mb-3"></i>
                    <h5 class="text-muted">No life records found</h5>
                    <p class="text-muted">Start documenting your life moments and memories.</p>
                </div>
                <div v-else class="row">
                    <div v-for="record in lifeRecords" :key="record.id" class="col-md-6 col-lg-4 mb-4">
                        <div class="card h-100 life-record-card">
                            <div class="card-body d-flex flex-column">
                                <div class="d-flex justify-content-between align-items-start mb-2">
                                    <h5 class="card-title text-truncate" :title="record.title">{{ record.title }}</h5>
                                    <div class="dropdown">
                                        <button class="btn btn-sm btn-outline-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown">
                                            <i class="fas fa-ellipsis-v"></i>
                                        </button>
                                        <ul class="dropdown-menu">
                                            <li><a class="dropdown-item" href="#" @click="editRecord(record)"><i class="fas fa-edit me-2"></i>Edit</a></li>
                                            <li><a class="dropdown-item text-danger" href="#" @click="deleteRecord(record.id)"><i class="fas fa-trash me-2"></i>Delete</a></li>
                                        </ul>
                                    </div>
                                </div>
                                <p class="card-text flex-grow-1" :class="{'text-truncate-3': !record.expanded}">
                                    {{ record.content }}
                                </p>
                                <div v-if="record.content && record.content.length > 150" class="mb-2">
                                    <button class="btn btn-sm btn-link p-0" @click="toggleExpand(record)">
                                        {{ record.expanded ? 'Show less' : 'Show more' }}
                                    </button>
                                </div>
                                <div class="mt-auto">
                                    <small class="text-muted">
                                        <i class="fas fa-calendar me-1"></i>
                                        {{ formatDateTime(record.createTime) }}
                                    </small>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Pagination -->
                <nav v-if="totalPages > 1" class="mt-4">
                    <ul class="pagination justify-content-center">
                        <li class="page-item" :class="{disabled: currentPage === 1}">
                            <button class="page-link" @click="changePage(currentPage - 1)" :disabled="currentPage === 1">
                                Previous
                            </button>
                        </li>
                        <li v-for="page in visiblePages" :key="page" class="page-item" :class="{active: page === currentPage}">
                            <button class="page-link" @click="changePage(page)">{{ page }}</button>
                        </li>
                        <li class="page-item" :class="{disabled: currentPage === totalPages}">
                            <button class="page-link" @click="changePage(currentPage + 1)" :disabled="currentPage === totalPages">
                                Next
                            </button>
                        </li>
                    </ul>
                </nav>
            </div>

            <!-- Add/Edit Modal -->
            <div class="modal fade" id="lifeRecordModal" tabindex="-1">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">{{ isEditing ? 'Edit' : 'Add' }} Life Record</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <form @submit.prevent="saveRecord">
                                <div class="mb-3">
                                    <label class="form-label">Title *</label>
                                    <input type="text" class="form-control" v-model="currentRecord.title" required maxlength="100" placeholder="Enter a memorable title">
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Content *</label>
                                    <textarea class="form-control" v-model="currentRecord.content" rows="6" required placeholder="Share your thoughts, experiences, or memories..."></textarea>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Date & Time *</label>
                                    <input type="datetime-local" class="form-control" v-model="currentRecord.createTime" required>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                            <button type="button" class="btn btn-primary" @click="saveRecord" :disabled="saving">
                                <span v-if="saving" class="spinner-border spinner-border-sm me-1"></span>
                                {{ isEditing ? 'Update' : 'Add' }} Record
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `,
    data() {
        return {
            loading: true,
            saving: false,
            lifeRecords: [],
            currentRecord: {
                id: null,
                title: '',
                content: '',
                createTime: ''
            },
            isEditing: false,
            filters: {
                title: '',
                content: '',
                createTime: ''
            },
            currentPage: 1,
            pageSize: 9,
            total: 0
        };
    },
    computed: {
        totalPages() {
            return Math.ceil(this.total / this.pageSize);
        },
        visiblePages() {
            const pages = [];
            const start = Math.max(1, this.currentPage - 2);
            const end = Math.min(this.totalPages, this.currentPage + 2);
            
            for (let i = start; i <= end; i++) {
                pages.push(i);
            }
            return pages;
        }
    },
    methods: {
        async loadLifeRecords() {
            this.loading = true;
            try {
                const params = {
                    page: this.currentPage,
                    pageSize: this.pageSize
                };
                
                // Add filters if they exist
                if (this.filters.title) params.title = this.filters.title;
                if (this.filters.content) params.content = this.filters.content;
                if (this.filters.createTime) params.createTime = this.filters.createTime;
                
                const response = await axios.get('/life/list', { params });
                
                if (response.data.code === 1) {
                    this.lifeRecords = (response.data.data.rows || []).map(record => ({
                        ...record,
                        expanded: false
                    }));
                    this.total = response.data.data.total || 0;
                } else {
                    utils.showError(response.data.message || 'Failed to load life records');
                }
            } catch (error) {
                console.error('Error loading life records:', error);
                utils.showError('Failed to load life records');
            } finally {
                this.loading = false;
            }
        },
        
        showAddModal() {
            this.isEditing = false;
            this.currentRecord = {
                id: null,
                title: '',
                content: '',
                createTime: new Date().toISOString().slice(0, 16)
            };
            const modal = new bootstrap.Modal(document.getElementById('lifeRecordModal'));
            modal.show();
        },
        
        editRecord(record) {
            this.isEditing = true;
            this.currentRecord = {
                id: record.id,
                title: record.title,
                content: record.content,
                createTime: record.createTime ? record.createTime.slice(0, 16) : ''
            };
            const modal = new bootstrap.Modal(document.getElementById('lifeRecordModal'));
            modal.show();
        },
        
        async saveRecord() {
            if (!this.currentRecord.title || !this.currentRecord.content || !this.currentRecord.createTime) {
                utils.showError('Please fill in all required fields');
                return;
            }
            
            this.saving = true;
            try {
                let response;
                if (this.isEditing) {
                    response = await axios.put('/life', this.currentRecord);
                } else {
                    response = await axios.post('/life', this.currentRecord);
                }
                
                if (response.data.code === 1) {
                    utils.showSuccess(`Life record ${this.isEditing ? 'updated' : 'added'} successfully`);
                    const modal = bootstrap.Modal.getInstance(document.getElementById('lifeRecordModal'));
                    modal.hide();
                    this.loadLifeRecords();
                } else {
                    utils.showError(response.data.message || `Failed to ${this.isEditing ? 'update' : 'add'} life record`);
                }
            } catch (error) {
                console.error('Error saving life record:', error);
                utils.showError(`Failed to ${this.isEditing ? 'update' : 'add'} life record`);
            } finally {
                this.saving = false;
            }
        },
        
        async deleteRecord(id) {
            if (!utils.confirmDelete('Are you sure you want to delete this life record?')) {
                return;
            }
            
            try {
                const response = await axios.delete(`/life/${id}`);
                
                if (response.data.code === 1) {
                    utils.showSuccess('Life record deleted successfully');
                    this.loadLifeRecords();
                } else {
                    utils.showError(response.data.message || 'Failed to delete life record');
                }
            } catch (error) {
                console.error('Error deleting life record:', error);
                utils.showError('Failed to delete life record');
            }
        },
        
        toggleExpand(record) {
            record.expanded = !record.expanded;
        },
        
        applyFilters() {
            this.currentPage = 1;
            this.loadLifeRecords();
        },
        
        clearFilters() {
            this.filters = {
                title: '',
                content: '',
                createTime: ''
            };
            this.currentPage = 1;
            this.loadLifeRecords();
        },
        
        changePage(page) {
            if (page >= 1 && page <= this.totalPages) {
                this.currentPage = page;
                this.loadLifeRecords();
            }
        },
        
        formatDateTime(dateTime) {
            return utils.formatDateTime(dateTime);
        }
    },
    
    mounted() {
        this.loadLifeRecords();
    }
};