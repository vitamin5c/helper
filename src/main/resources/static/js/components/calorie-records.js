const CalorieRecordsComponent = {
    template: `
        <div>
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <h1 class="h2"><i class="fas fa-utensils me-2"></i>Calorie Records</h1>
                <div class="btn-toolbar mb-2 mb-md-0">
                    <button class="btn btn-primary" @click="showAddModal">
                        <i class="fas fa-plus me-1"></i>Add Calorie Record
                    </button>
                </div>
            </div>

            <!-- Summary Cards -->
            <div class="row mb-4">
                <div class="col-md-3">
                    <div class="card bg-primary text-white">
                        <div class="card-body">
                            <div class="d-flex justify-content-between">
                                <div>
                                    <h6 class="card-title">Today's Calories</h6>
                                    <h4 class="mb-0">{{ todayCalories }}</h4>
                                </div>
                                <i class="fas fa-fire fa-2x opacity-75"></i>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card bg-success text-white">
                        <div class="card-body">
                            <div class="d-flex justify-content-between">
                                <div>
                                    <h6 class="card-title">This Week</h6>
                                    <h4 class="mb-0">{{ weekCalories }}</h4>
                                </div>
                                <i class="fas fa-calendar-week fa-2x opacity-75"></i>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card bg-info text-white">
                        <div class="card-body">
                            <div class="d-flex justify-content-between">
                                <div>
                                    <h6 class="card-title">This Month</h6>
                                    <h4 class="mb-0">{{ monthCalories }}</h4>
                                </div>
                                <i class="fas fa-calendar-alt fa-2x opacity-75"></i>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card bg-warning text-dark">
                        <div class="card-body">
                            <div class="d-flex justify-content-between">
                                <div>
                                    <h6 class="card-title">Daily Average</h6>
                                    <h4 class="mb-0">{{ dailyAverage }}</h4>
                                </div>
                                <i class="fas fa-chart-line fa-2x opacity-75"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Filters -->
            <div class="card mb-4">
                <div class="card-body">
                    <div class="row g-3">
                        <div class="col-md-2">
                            <label class="form-label">Food Name</label>
                            <input type="text" class="form-control" v-model="filters.foodName" placeholder="Search food">
                        </div>
                        <div class="col-md-2">
                            <label class="form-label">Meal Type</label>
                            <select class="form-select" v-model="filters.mealType">
                                <option value="">All Meals</option>
                                <option value="Breakfast">Breakfast</option>
                                <option value="Lunch">Lunch</option>
                                <option value="Dinner">Dinner</option>
                                <option value="Snack">Snack</option>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <label class="form-label">Min Calories</label>
                            <input type="number" class="form-control" v-model="filters.minCalories" placeholder="0">
                        </div>
                        <div class="col-md-2">
                            <label class="form-label">Max Calories</label>
                            <input type="number" class="form-control" v-model="filters.maxCalories" placeholder="1000">
                        </div>
                        <div class="col-md-2">
                            <label class="form-label">From Date</label>
                            <input type="date" class="form-control" v-model="filters.startDate">
                        </div>
                        <div class="col-md-2">
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
                <p class="mt-2">Loading calorie records...</p>
            </div>

            <!-- Calorie Records Table -->
            <div v-else class="card">
                <div class="card-body">
                    <div v-if="calorieRecords.length === 0" class="text-center py-4">
                        <i class="fas fa-utensils fa-3x text-muted mb-3"></i>
                        <h5 class="text-muted">No calorie records found</h5>
                        <p class="text-muted">Start tracking your daily calorie intake and nutrition.</p>
                    </div>
                    <div v-else>
                        <div class="table-responsive">
                            <table class="table table-hover">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Food Name</th>
                                        <th>Calories</th>
                                        <th>Serving Size</th>
                                        <th>Meal Type</th>
                                        <th>Description</th>
                                        <th>Date</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-for="record in calorieRecords" :key="record.id">
                                        <td>{{ record.id }}</td>
                                        <td class="fw-bold">{{ record.foodName }}</td>
                                        <td>
                                            <span class="badge bg-primary">{{ formatNumber(record.calories) }} cal</span>
                                        </td>
                                        <td>{{ record.servingSize }}</td>
                                        <td>
                                            <span class="badge" :class="getMealTypeBadgeClass(record.mealType)">
                                                <i :class="getMealTypeIcon(record.mealType)" class="me-1"></i>
                                                {{ record.mealType }}
                                            </span>
                                        </td>
                                        <td>
                                            <span :title="record.description" class="text-truncate d-inline-block" style="max-width: 150px;">
                                                {{ record.description || '-' }}
                                            </span>
                                        </td>
                                        <td>{{ formatDate(record.recordDate) }}</td>
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
            <div class="modal fade" id="calorieModal" tabindex="-1">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">{{ isEditing ? 'Edit' : 'Add' }} Calorie Record</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <form @submit.prevent="saveRecord">
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label class="form-label">Food Name *</label>
                                            <input type="text" class="form-control" v-model="currentRecord.foodName" required placeholder="e.g., Apple, Rice, Chicken Breast">
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label class="form-label">Calories *</label>
                                            <input type="number" class="form-control" v-model="currentRecord.calories" required min="0" placeholder="e.g., 150">
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label class="form-label">Serving Size *</label>
                                            <input type="text" class="form-control" v-model="currentRecord.servingSize" required placeholder="e.g., 1 medium, 100g, 1 cup">
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label class="form-label">Meal Type *</label>
                                            <select class="form-select" v-model="currentRecord.mealType" required>
                                                <option value="">Select Meal Type</option>
                                                <option value="Breakfast">Breakfast</option>
                                                <option value="Lunch">Lunch</option>
                                                <option value="Dinner">Dinner</option>
                                                <option value="Snack">Snack</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Description</label>
                                    <textarea class="form-control" v-model="currentRecord.description" rows="3" placeholder="Additional notes about the food or meal..."></textarea>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Record Date *</label>
                                    <input type="date" class="form-control" v-model="currentRecord.recordDate" required>
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
            calorieRecords: [],
            currentRecord: {
                id: null,
                foodName: '',
                calories: '',
                servingSize: '',
                mealType: '',
                description: '',
                recordDate: ''
            },
            isEditing: false,
            filters: {
                foodName: '',
                mealType: '',
                minCalories: '',
                maxCalories: '',
                startDate: '',
                endDate: ''
            },
            currentPage: 1,
            pageSize: 10,
            total: 0,
            todayCalories: 0,
            weekCalories: 0,
            monthCalories: 0,
            dailyAverage: 0
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
        async loadCalorieRecords() {
            this.loading = true;
            try {
                const params = {
                    pageNum: this.currentPage,
                    pageSize: this.pageSize
                };
                
                // Add filters if they exist
                if (this.filters.foodName) params.foodName = this.filters.foodName;
                if (this.filters.mealType) params.mealType = this.filters.mealType;
                if (this.filters.minCalories) params.minCalories = this.filters.minCalories;
                if (this.filters.maxCalories) params.maxCalories = this.filters.maxCalories;
                if (this.filters.startDate) params.startDate = this.filters.startDate;
                if (this.filters.endDate) params.endDate = this.filters.endDate;
                
                const response = await axios.get('/calorie/list', { params });
                
                if (response.data.code === 1) {
                    this.calorieRecords = response.data.data.rows || [];
                    this.total = response.data.data.total || 0;
                } else {
                    utils.showError(response.data.message || 'Failed to load calorie records');
                }
            } catch (error) {
                console.error('Error loading calorie records:', error);
                utils.showError('Failed to load calorie records');
            } finally {
                this.loading = false;
            }
        },
        
        async loadCalorieStats() {
            try {
                const response = await axios.get('/calorie/analytics');
                
                if (response.data.code === 1) {
                    const data = response.data.data;
                    this.todayCalories = data.todayCalories || 0;
                    this.weekCalories = data.weekCalories || 0;
                    this.monthCalories = data.monthCalories || 0;
                    this.dailyAverage = data.dailyAverage || 0;
                }
            } catch (error) {
                console.error('Error loading calorie stats:', error);
            }
        },
        
        showAddModal() {
            this.isEditing = false;
            this.currentRecord = {
                id: null,
                foodName: '',
                calories: '',
                servingSize: '',
                mealType: '',
                description: '',
                recordDate: new Date().toISOString().split('T')[0]
            };
            const modal = new bootstrap.Modal(document.getElementById('calorieModal'));
            modal.show();
        },
        
        editRecord(record) {
            this.isEditing = true;
            this.currentRecord = {
                id: record.id,
                foodName: record.foodName,
                calories: record.calories,
                servingSize: record.servingSize,
                mealType: record.mealType,
                description: record.description,
                recordDate: record.recordDate
            };
            const modal = new bootstrap.Modal(document.getElementById('calorieModal'));
            modal.show();
        },
        
        async saveRecord() {
            if (!this.currentRecord.foodName || !this.currentRecord.calories || !this.currentRecord.servingSize || !this.currentRecord.mealType || !this.currentRecord.recordDate) {
                utils.showError('Please fill in all required fields');
                return;
            }
            
            this.saving = true;
            try {
                let response;
                if (this.isEditing) {
                    response = await axios.put('/calorie', this.currentRecord);
                } else {
                    response = await axios.post('/calorie', this.currentRecord);
                }
                
                if (response.data.code === 1) {
                    utils.showSuccess(`Calorie record ${this.isEditing ? 'updated' : 'added'} successfully`);
                    const modal = bootstrap.Modal.getInstance(document.getElementById('calorieModal'));
                    modal.hide();
                    this.loadCalorieRecords();
                    this.loadCalorieStats();
                } else {
                    utils.showError(response.data.message || `Failed to ${this.isEditing ? 'update' : 'add'} calorie record`);
                }
            } catch (error) {
                console.error('Error saving calorie record:', error);
                utils.showError(`Failed to ${this.isEditing ? 'update' : 'add'} calorie record`);
            } finally {
                this.saving = false;
            }
        },
        
        async deleteRecord(id) {
            if (!utils.confirmDelete('Are you sure you want to delete this calorie record?')) {
                return;
            }
            
            try {
                const response = await axios.delete(`/calorie/${id}`);
                
                if (response.data.code === 1) {
                    utils.showSuccess('Calorie record deleted successfully');
                    this.loadCalorieRecords();
                    this.loadCalorieStats();
                } else {
                    utils.showError(response.data.message || 'Failed to delete calorie record');
                }
            } catch (error) {
                console.error('Error deleting calorie record:', error);
                utils.showError('Failed to delete calorie record');
            }
        },
        
        getMealTypeBadgeClass(mealType) {
            const mealClasses = {
                'Breakfast': 'bg-warning text-dark',
                'Lunch': 'bg-success',
                'Dinner': 'bg-primary',
                'Snack': 'bg-info'
            };
            return mealClasses[mealType] || 'bg-secondary';
        },
        
        getMealTypeIcon(mealType) {
            const mealIcons = {
                'Breakfast': 'fas fa-sun',
                'Lunch': 'fas fa-cloud-sun',
                'Dinner': 'fas fa-moon',
                'Snack': 'fas fa-cookie-bite'
            };
            return mealIcons[mealType] || 'fas fa-utensils';
        },
        
        applyFilters() {
            this.currentPage = 1;
            this.loadCalorieRecords();
        },
        
        clearFilters() {
            this.filters = {
                foodName: '',
                mealType: '',
                minCalories: '',
                maxCalories: '',
                startDate: '',
                endDate: ''
            };
            this.currentPage = 1;
            this.loadCalorieRecords();
        },
        
        changePage(page) {
            if (page >= 1 && page <= this.totalPages) {
                this.currentPage = page;
                this.loadCalorieRecords();
            }
        },
        
        formatNumber(number) {
            return utils.formatNumber(number);
        },
        
        formatDate(date) {
            return utils.formatDate(date);
        }
    },
    
    mounted() {
        this.loadCalorieRecords();
        this.loadCalorieStats();
    }
};