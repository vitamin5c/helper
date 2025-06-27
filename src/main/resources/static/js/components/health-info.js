const HealthInfoComponent = {
    template: `
        <div>
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <h1 class="h2"><i class="fas fa-heartbeat me-2"></i>Health Information</h1>
                <div class="btn-toolbar mb-2 mb-md-0">
                    <button class="btn btn-primary" @click="showAddModal">
                        <i class="fas fa-plus me-1"></i>Add Health Record
                    </button>
                </div>
            </div>

            <!-- Filters -->
            <div class="card mb-4">
                <div class="card-body">
                    <div class="row g-3">
                        <div class="col-md-3">
                            <label class="form-label">Description</label>
                            <input type="text" class="form-control" v-model="filters.description" placeholder="Search by description">
                        </div>
                        <div class="col-md-3">
                            <label class="form-label">Status</label>
                            <select class="form-select" v-model="filters.status">
                                <option value="">All Status</option>
                                <option value="Normal">Normal</option>
                                <option value="Warning">Warning</option>
                                <option value="Critical">Critical</option>
                                <option value="Under Treatment">Under Treatment</option>
                                <option value="Recovered">Recovered</option>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label">From Date</label>
                            <input type="date" class="form-control" v-model="filters.startDate">
                        </div>
                        <div class="col-md-3">
                            <label class="form-label">To Date</label>
                            <input type="date" class="form-control" v-model="filters.endDate">
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
                <p class="mt-2">Loading health records...</p>
            </div>

            <!-- Health Records Table -->
            <div v-else class="card">
                <div class="card-body">
                    <div v-if="healthRecords.length === 0" class="text-center py-4">
                        <i class="fas fa-heartbeat fa-3x text-muted mb-3"></i>
                        <h5 class="text-muted">No health records found</h5>
                        <p class="text-muted">Start tracking your health information and medical records.</p>
                    </div>
                    <div v-else>
                        <div class="table-responsive">
                            <table class="table table-hover">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Description</th>
                                        <th>Record Date</th>
                                        <th>Status</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-for="record in healthRecords" :key="record.id">
                                        <td>{{ record.id }}</td>
                                        <td>
                                            <div class="health-description">
                                                <span :title="record.description">{{ record.description }}</span>
                                            </div>
                                        </td>
                                        <td>{{ formatDate(record.recordDate) }}</td>
                                        <td>
                                            <span class="badge" :class="getStatusBadgeClass(getStatusString(record.status))">
                                                <i :class="getStatusIcon(getStatusString(record.status))" class="me-1"></i>
                                                {{ getStatusString(record.status) }}
                                            </span>
                                        </td>
                                        <td>
                                            <button class="btn btn-sm btn-outline-primary me-1" @click="editRecord(record)">
                                                <i class="fas fa-edit"></i>
                                            </button>
                                            <button class="btn btn-sm btn-outline-danger" @click="deleteRecord(record.id)">
                                                <i class="fas fa-trash"></i>
                                            </button>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>

                        <!-- Pagination -->
                        <nav v-if="totalPages > 1" class="mt-3">
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
                </div>
            </div>

            <!-- Add/Edit Modal -->
            <div class="modal fade" id="healthModal" tabindex="-1">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">{{ isEditing ? 'Edit' : 'Add' }} Health Record</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <form @submit.prevent="saveRecord">
                                <div class="mb-3">
                                    <label class="form-label">Description *</label>
                                    <textarea class="form-control" v-model="currentRecord.description" rows="4" required placeholder="Describe your health condition, symptoms, or medical information..."></textarea>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Record Date *</label>
                                    <input type="date" class="form-control" v-model="currentRecord.recordDate" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Status *</label>
                                    <select class="form-select" v-model="currentRecord.status" required>
                                        <option value="">Select Status</option>
                                        <option value="Normal">Normal</option>
                                        <option value="Warning">Warning</option>
                                        <option value="Critical">Critical</option>
                                        <option value="Under Treatment">Under Treatment</option>
                                        <option value="Recovered">Recovered</option>
                                    </select>
                                </div>
                                <div class="alert alert-info">
                                    <i class="fas fa-info-circle me-2"></i>
                                    <strong>Status Guide:</strong>
                                    <ul class="mb-0 mt-2">
                                        <li><strong>Normal:</strong> Regular health status, no concerns</li>
                                        <li><strong>Warning:</strong> Minor issues that need attention</li>
                                        <li><strong>Critical:</strong> Serious conditions requiring immediate care</li>
                                        <li><strong>Under Treatment:</strong> Currently receiving medical treatment</li>
                                        <li><strong>Recovered:</strong> Successfully recovered from previous condition</li>
                                    </ul>
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
            healthRecords: [],
            currentRecord: {
                id: null,
                description: '',
                recordDate: '',
                status: ''
            },
            isEditing: false,
            filters: {
                description: '',
                status: '',
                startDate: '',
                endDate: ''
            },
            currentPage: 1,
            pageSize: 10,
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
        async loadHealthRecords() {
            this.loading = true;
            try {
                const params = {
                    page: this.currentPage,
                    pageSize: this.pageSize
                };
                
                // Add filters if they exist
                if (this.filters.status) params.status = this.getStatusValue(this.filters.status);
                if (this.filters.startDate) params.recordDate = this.filters.startDate;
                
                const response = await axios.get('/health/list', { params });
                
                if (response.data.code === 1) {
                    this.healthRecords = response.data.data.rows || [];
                    this.total = response.data.data.total || 0;
                } else {
                    utils.showError(response.data.message || 'Failed to load health records');
                }
            } catch (error) {
                console.error('Error loading health records:', error);
                utils.showError('Failed to load health records');
            } finally {
                this.loading = false;
            }
        },
        
        showAddModal() {
            this.isEditing = false;
            this.currentRecord = {
                id: null,
                description: '',
                recordDate: new Date().toISOString().split('T')[0],
                status: ''
            };
            const modal = new bootstrap.Modal(document.getElementById('healthModal'));
            modal.show();
        },
        
        editRecord(record) {
            this.isEditing = true;
            this.currentRecord = {
                id: record.id,
                description: record.description,
                recordDate: record.recordDate,
                status: this.getStatusString(record.status)
            };
            const modal = new bootstrap.Modal(document.getElementById('healthModal'));
            modal.show();
        },
        
        async saveRecord() {
            if (!this.currentRecord.description || !this.currentRecord.recordDate || !this.currentRecord.status) {
                utils.showError('Please fill in all required fields');
                return;
            }
            
            this.saving = true;
            try {
                // Convert status string to integer for backend
                const recordData = {
                    ...this.currentRecord,
                    status: this.getStatusValue(this.currentRecord.status)
                };
                
                let response;
                if (this.isEditing) {
                    response = await axios.put('/health', recordData);
                } else {
                    response = await axios.post('/health', recordData);
                }
                
                if (response.data.code === 1) {
                    utils.showSuccess(`Health record ${this.isEditing ? 'updated' : 'added'} successfully`);
                    const modal = bootstrap.Modal.getInstance(document.getElementById('healthModal'));
                    modal.hide();
                    this.loadHealthRecords();
                } else {
                    utils.showError(response.data.message || `Failed to ${this.isEditing ? 'update' : 'add'} health record`);
                }
            } catch (error) {
                console.error('Error saving health record:', error);
                utils.showError(`Failed to ${this.isEditing ? 'update' : 'add'} health record`);
            } finally {
                this.saving = false;
            }
        },
        
        async deleteRecord(id) {
            if (!utils.confirmDelete('Are you sure you want to delete this health record?')) {
                return;
            }
            
            try {
                const response = await axios.delete(`/health/${id}`);
                
                if (response.data.code === 1) {
                    utils.showSuccess('Health record deleted successfully');
                    this.loadHealthRecords();
                } else {
                    utils.showError(response.data.message || 'Failed to delete health record');
                }
            } catch (error) {
                console.error('Error deleting health record:', error);
                utils.showError('Failed to delete health record');
            }
        },
        
        getStatusBadgeClass(status) {
            const statusClasses = {
                'Normal': 'bg-success',
                'Warning': 'bg-warning text-dark',
                'Critical': 'bg-danger',
                'Under Treatment': 'bg-info',
                'Recovered': 'bg-primary'
            };
            return statusClasses[status] || 'bg-secondary';
        },
        
        getStatusIcon(status) {
            const statusIcons = {
                'Normal': 'fas fa-check-circle',
                'Warning': 'fas fa-exclamation-triangle',
                'Critical': 'fas fa-exclamation-circle',
                'Under Treatment': 'fas fa-pills',
                'Recovered': 'fas fa-heart'
            };
            return statusIcons[status] || 'fas fa-question-circle';
        },
        
        getStatusValue(statusString) {
            const statusMapping = {
                'Normal': 1,
                'Warning': 2,
                'Critical': 3,
                'Under Treatment': 4,
                'Recovered': 5
            };
            return statusMapping[statusString] || 1;
        },
        
        getStatusString(statusValue) {
            const statusMapping = {
                1: 'Normal',
                2: 'Warning',
                3: 'Critical',
                4: 'Under Treatment',
                5: 'Recovered'
            };
            return statusMapping[statusValue] || 'Normal';
        },
        
        getStatusBadgeClass(statusString) {
            const badgeClasses = {
                'Normal': 'bg-success',
                'Warning': 'bg-warning',
                'Critical': 'bg-danger',
                'Under Treatment': 'bg-info',
                'Recovered': 'bg-primary'
            };
            return badgeClasses[statusString] || 'bg-secondary';
        },
        
        applyFilters() {
            this.currentPage = 1;
            this.loadHealthRecords();
        },
        
        clearFilters() {
            this.filters = {
                description: '',
                status: '',
                startDate: '',
                endDate: ''
            };
            this.currentPage = 1;
            this.loadHealthRecords();
        },
        
        changePage(page) {
            if (page >= 1 && page <= this.totalPages) {
                this.currentPage = page;
                this.loadHealthRecords();
            }
        },
        
        formatDate(date) {
            return utils.formatDate(date);
        }
    },
    
    mounted() {
        this.loadHealthRecords();
    }
};