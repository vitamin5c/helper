const ExpenseRecordsComponent = {
    template: `
        <div>
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <h1 class="h2"><i class="fas fa-wallet me-2"></i>Expense Records</h1>
                <div class="btn-toolbar mb-2 mb-md-0">
                    <button class="btn btn-primary" @click="showAddModal">
                        <i class="fas fa-plus me-1"></i>Add Expense
                    </button>
                </div>
            </div>

            <!-- Filters -->
            <div class="card mb-4">
                <div class="card-body">
                    <div class="row g-3">
                        <div class="col-md-3">
                            <label class="form-label">Category</label>
                            <input type="text" class="form-control" v-model="filters.category" placeholder="Filter by category">
                        </div>
                        <div class="col-md-3">
                            <label class="form-label">Min Amount</label>
                            <input type="number" class="form-control" v-model="filters.minAmount" placeholder="0.00" step="0.01">
                        </div>
                        <div class="col-md-3">
                            <label class="form-label">Max Amount</label>
                            <input type="number" class="form-control" v-model="filters.maxAmount" placeholder="1000.00" step="0.01">
                        </div>
                        <div class="col-md-3">
                            <label class="form-label">Date</label>
                            <input type="date" class="form-control" v-model="filters.dateTime">
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
                <p class="mt-2">Loading expense records...</p>
            </div>

            <!-- Expense Records Table -->
            <div v-else class="card">
                <div class="card-body">
                    <div v-if="expenses.length === 0" class="text-center py-4">
                        <i class="fas fa-wallet fa-3x text-muted mb-3"></i>
                        <h5 class="text-muted">No expense records found</h5>
                        <p class="text-muted">Start by adding your first expense record.</p>
                    </div>
                    <div v-else>
                        <div class="table-responsive">
                            <table class="table table-hover">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Category</th>
                                        <th>Description</th>
                                        <th>Amount</th>
                                        <th>Date</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-for="expense in expenses" :key="expense.id">
                                        <td>{{ expense.id }}</td>
                                        <td>
                                            <span class="badge bg-secondary">{{ expense.category }}</span>
                                        </td>
                                        <td>{{ expense.description }}</td>
                                        <td class="fw-bold text-danger">{{ formatCurrency(expense.amount) }}</td>
                                        <td>{{ formatDate(expense.createDate) }}</td>
                                        <td>
                                            <button class="btn btn-sm btn-outline-primary me-1" @click="editExpense(expense)">
                                                <i class="fas fa-edit"></i>
                                            </button>
                                            <button class="btn btn-sm btn-outline-danger" @click="deleteExpense(expense.id)">
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
            <div class="modal fade" id="expenseModal" tabindex="-1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">{{ isEditing ? 'Edit' : 'Add' }} Expense Record</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <form @submit.prevent="saveExpense">
                                <div class="mb-3">
                                    <label class="form-label">Category *</label>
                                    <input type="text" class="form-control" v-model="currentExpense.category" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Description *</label>
                                    <textarea class="form-control" v-model="currentExpense.description" rows="3" required></textarea>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Amount *</label>
                                    <input type="number" class="form-control" v-model="currentExpense.amount" step="0.01" min="0" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Date *</label>
                                    <input type="date" class="form-control" v-model="currentExpense.createDate" required>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                            <button type="button" class="btn btn-primary" @click="saveExpense" :disabled="saving">
                                <span v-if="saving" class="spinner-border spinner-border-sm me-1"></span>
                                {{ isEditing ? 'Update' : 'Add' }} Expense
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
            expenses: [],
            currentExpense: {
                id: null,
                category: '',
                description: '',
                amount: '',
                createDate: ''
            },
            isEditing: false,
            filters: {
                category: '',
                minAmount: '',
                maxAmount: '',
                dateTime: ''
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
        async loadExpenses() {
            this.loading = true;
            try {
                const params = {
                    pageNum: this.currentPage,
                    pageSize: this.pageSize
                };
                
                // Add filters if they exist
                if (this.filters.category) params.category = this.filters.category;
                if (this.filters.minAmount) params.minAmount = this.filters.minAmount;
                if (this.filters.maxAmount) params.maxAmount = this.filters.maxAmount;
                if (this.filters.dateTime) params.dateTime = this.filters.dateTime;
                
                const response = await axios.get('/expense/list', { params });
                
                if (response.data.code === 1) {
                    this.expenses = response.data.data.rows || [];
                    this.total = response.data.data.total || 0;
                } else {
                    utils.showError(response.data.message || 'Failed to load expenses');
                }
            } catch (error) {
                console.error('Error loading expenses:', error);
                utils.showError('Failed to load expense records');
            } finally {
                this.loading = false;
            }
        },
        
        showAddModal() {
            this.isEditing = false;
            this.currentExpense = {
                id: null,
                category: '',
                description: '',
                amount: '',
                createDate: new Date().toISOString().split('T')[0]
            };
            const modal = new bootstrap.Modal(document.getElementById('expenseModal'));
            modal.show();
        },
        
        editExpense(expense) {
            this.isEditing = true;
            this.currentExpense = {
                id: expense.id,
                category: expense.category,
                description: expense.description,
                amount: expense.amount,
                createDate: expense.createDate
            };
            const modal = new bootstrap.Modal(document.getElementById('expenseModal'));
            modal.show();
        },
        
        async saveExpense() {
            if (!this.currentExpense.category || !this.currentExpense.description || !this.currentExpense.amount || !this.currentExpense.createDate) {
                utils.showError('Please fill in all required fields');
                return;
            }
            
            this.saving = true;
            try {
                let response;
                if (this.isEditing) {
                    response = await axios.put('/expense', this.currentExpense);
                } else {
                    response = await axios.post('/expense', this.currentExpense);
                }
                
                if (response.data.code === 1) {
                    utils.showSuccess(`Expense ${this.isEditing ? 'updated' : 'added'} successfully`);
                    const modal = bootstrap.Modal.getInstance(document.getElementById('expenseModal'));
                    modal.hide();
                    this.loadExpenses();
                } else {
                    utils.showError(response.data.message || `Failed to ${this.isEditing ? 'update' : 'add'} expense`);
                }
            } catch (error) {
                console.error('Error saving expense:', error);
                utils.showError(`Failed to ${this.isEditing ? 'update' : 'add'} expense record`);
            } finally {
                this.saving = false;
            }
        },
        
        async deleteExpense(id) {
            if (!utils.confirmDelete('Are you sure you want to delete this expense record?')) {
                return;
            }
            
            try {
                const response = await axios.delete(`/expense/${id}`);
                
                if (response.data.code === 1) {
                    utils.showSuccess('Expense deleted successfully');
                    this.loadExpenses();
                } else {
                    utils.showError(response.data.message || 'Failed to delete expense');
                }
            } catch (error) {
                console.error('Error deleting expense:', error);
                utils.showError('Failed to delete expense record');
            }
        },
        
        applyFilters() {
            this.currentPage = 1;
            this.loadExpenses();
        },
        
        clearFilters() {
            this.filters = {
                category: '',
                minAmount: '',
                maxAmount: '',
                dateTime: ''
            };
            this.currentPage = 1;
            this.loadExpenses();
        },
        
        changePage(page) {
            if (page >= 1 && page <= this.totalPages) {
                this.currentPage = page;
                this.loadExpenses();
            }
        },
        
        formatCurrency(amount) {
            return utils.formatCurrency(amount);
        },
        
        formatDate(date) {
            return utils.formatDate(date);
        }
    },
    
    mounted() {
        this.loadExpenses();
    }
};