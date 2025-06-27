const DashboardComponent = {
    template: `
        <div>
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <h1 class="h2"><i class="fas fa-tachometer-alt me-2"></i>Dashboard</h1>
                <div class="btn-toolbar mb-2 mb-md-0">
                    <button class="btn btn-outline-secondary btn-sm" @click="refreshData">
                        <i class="fas fa-sync-alt me-1"></i>Refresh
                    </button>
                </div>
            </div>

            <!-- Loading state -->
            <div v-if="loading" class="loading">
                <div class="spinner-border" role="status">
                    <span class="visually-hidden">Loading...</span>
                </div>
                <p class="mt-2">Loading dashboard data...</p>
            </div>

            <!-- Dashboard content -->
            <div v-else>
                <!-- Summary Cards -->
                <div class="row mb-4">
                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card border-left-primary shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">
                                            Total Expenses This Month
                                        </div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">
                                            {{ formatCurrency(stats.totalExpenses) }}
                                        </div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-wallet fa-2x text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card border-left-success shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-success text-uppercase mb-1">
                                            Life Records
                                        </div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">
                                            {{ stats.totalLifeRecords }}
                                        </div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-book fa-2x text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card border-left-info shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-info text-uppercase mb-1">
                                            Health Records
                                        </div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">
                                            {{ stats.totalHealthRecords }}
                                        </div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-heartbeat fa-2x text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card border-left-warning shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-warning text-uppercase mb-1">
                                            Today's Calories
                                        </div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">
                                            {{ stats.todayCalories }} kcal
                                        </div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-apple-alt fa-2x text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Recent Activities -->
                <div class="row">
                    <!-- Recent Expenses -->
                    <div class="col-lg-6 mb-4">
                        <div class="card shadow">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">
                                    <i class="fas fa-wallet me-2"></i>Recent Expenses
                                </h6>
                            </div>
                            <div class="card-body">
                                <div v-if="recentExpenses.length === 0" class="text-center text-muted py-3">
                                    No recent expenses
                                </div>
                                <div v-else>
                                    <div v-for="expense in recentExpenses" :key="expense.id" class="d-flex justify-content-between align-items-center mb-2">
                                        <div>
                                            <strong>{{ expense.description }}</strong><br>
                                            <small class="text-muted">{{ expense.category }} • {{ formatDate(expense.createDate) }}</small>
                                        </div>
                                        <span class="badge bg-primary">{{ formatCurrency(expense.amount) }}</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Recent Life Records -->
                    <div class="col-lg-6 mb-4">
                        <div class="card shadow">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-success">
                                    <i class="fas fa-book me-2"></i>Recent Life Records
                                </h6>
                            </div>
                            <div class="card-body">
                                <div v-if="recentLifeRecords.length === 0" class="text-center text-muted py-3">
                                    No recent life records
                                </div>
                                <div v-else>
                                    <div v-for="record in recentLifeRecords" :key="record.id" class="mb-3">
                                        <strong>{{ record.title }}</strong><br>
                                        <p class="mb-1 text-muted">{{ record.content.substring(0, 100) }}{{ record.content.length > 100 ? '...' : '' }}</p>
                                        <small class="text-muted">{{ formatDateTime(record.createTime) }}</small>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Health & Calorie Overview -->
                <div class="row">
                    <!-- Recent Health Info -->
                    <div class="col-lg-6 mb-4">
                        <div class="card shadow">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-info">
                                    <i class="fas fa-heartbeat me-2"></i>Recent Health Info
                                </h6>
                            </div>
                            <div class="card-body">
                                <div v-if="recentHealthInfo.length === 0" class="text-center text-muted py-3">
                                    No recent health records
                                </div>
                                <div v-else>
                                    <div v-for="health in recentHealthInfo" :key="health.id" class="d-flex justify-content-between align-items-center mb-2">
                                        <div>
                                            <strong>{{ health.description }}</strong><br>
                                            <small class="text-muted">{{ formatDate(health.recordDate) }}</small>
                                        </div>
                                        <span class="badge" :class="getHealthStatusBadgeClass(health.status)">{{ getHealthStatusString(health.status) }}</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Recent Calorie Records -->
                    <div class="col-lg-6 mb-4">
                        <div class="card shadow">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-warning">
                                    <i class="fas fa-apple-alt me-2"></i>Recent Meals
                                </h6>
                            </div>
                            <div class="card-body">
                                <div v-if="recentCalorieRecords.length === 0" class="text-center text-muted py-3">
                                    No recent calorie records
                                </div>
                                <div v-else>
                                    <div v-for="calorie in recentCalorieRecords" :key="calorie.id" class="d-flex justify-content-between align-items-center mb-2">
                                        <div>
                                            <strong>{{ calorie.foodName }}</strong><br>
                                            <small class="text-muted">{{ calorie.mealType }} • {{ formatDate(calorie.recordDate) }}</small>
                                        </div>
                                        <span class="badge bg-warning text-dark">{{ calorie.calories }} kcal</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `,
    data() {
        return {
            loading: true,
            stats: {
                totalExpenses: 0,
                totalLifeRecords: 0,
                totalHealthRecords: 0,
                todayCalories: 0
            },
            recentExpenses: [],
            recentLifeRecords: [],
            recentHealthInfo: [],
            recentCalorieRecords: []
        };
    },
    methods: {
        async loadDashboardData() {
            this.loading = true;
            try {
                // Load all dashboard data in parallel
                const [expensesRes, lifeRes, healthRes, caloriesRes] = await Promise.allSettled([
                    this.loadExpenseStats(),
                    this.loadLifeRecords(),
                    this.loadHealthInfo(),
                    this.loadCalorieStats()
                ]);
                
                console.log('Dashboard data loaded successfully');
            } catch (error) {
                console.error('Error loading dashboard data:', error);
                utils.showError('Failed to load dashboard data');
            } finally {
                this.loading = false;
            }
        },
        
        async loadExpenseStats() {
            try {
                // Get current month expenses
                const currentDate = new Date();
                const firstDay = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1);
                const lastDay = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 0);
                
                const response = await axios.get('/expense/list', {
                    params: {
                        pageNum: 1,
                        pageSize: 5,
                        minAmount: 0
                    }
                });
                
                if (response.data.code === 1) {
                    this.recentExpenses = response.data.data.rows || [];
                    // Calculate total from recent expenses (simplified)
                    this.stats.totalExpenses = this.recentExpenses.reduce((sum, expense) => sum + (expense.amount || 0), 0);
                }
            } catch (error) {
                console.error('Error loading expense stats:', error);
            }
        },
        
        async loadLifeRecords() {
            try {
                const response = await axios.get('/life/list', {
                    params: {
                        page: 1,
                        pageSize: 3
                    }
                });
                
                if (response.data.code === 1) {
                    this.recentLifeRecords = response.data.data.rows || [];
                    this.stats.totalLifeRecords = response.data.data.total || 0;
                }
            } catch (error) {
                console.error('Error loading life records:', error);
            }
        },
        
        async loadHealthInfo() {
            try {
                const response = await axios.get('/health/list', {
                    params: {
                        page: 1,
                        pageSize: 3
                    }
                });
                
                if (response.data.code === 1) {
                    this.recentHealthInfo = response.data.data.rows || [];
                    this.stats.totalHealthRecords = response.data.data.total || 0;
                }
            } catch (error) {
                console.error('Error loading health info:', error);
            }
        },
        
        async loadCalorieStats() {
            try {
                const today = new Date().toISOString().split('T')[0];
                
                // Get recent calorie records
                const response = await axios.get('/calorie/list', {
                    params: {
                        pageNum: 1,
                        pageSize: 3
                    }
                });
                
                if (response.data.code === 1) {
                    this.recentCalorieRecords = response.data.data.rows || [];
                }
                
                // Get today's total calories
                try {
                    const caloriesResponse = await axios.get('/calorie/total', {
                        params: {
                            startDate: today,
                            endDate: today
                        }
                    });
                    
                    if (caloriesResponse.data.code === 1) {
                        this.stats.todayCalories = caloriesResponse.data.data || 0;
                    }
                } catch (calorieError) {
                    console.log('Could not load today\'s calories, using default');
                    this.stats.todayCalories = 0;
                }
            } catch (error) {
                console.error('Error loading calorie stats:', error);
            }
        },
        
        refreshData() {
            this.loadDashboardData();
        },
        
        formatCurrency(amount) {
            return utils.formatCurrency(amount);
        },
        
        formatDate(date) {
            return utils.formatDate(date);
        },
        
        formatDateTime(dateTime) {
            return utils.formatDateTime(dateTime);
        },
        
        getHealthStatusString(statusValue) {
            const statusMapping = {
                1: 'Normal',
                2: 'Warning',
                3: 'Critical',
                4: 'Under Treatment',
                5: 'Recovered'
            };
            return statusMapping[statusValue] || 'Normal';
        },
        
        getHealthStatusBadgeClass(statusValue) {
            const badgeClasses = {
                1: 'bg-success',
                2: 'bg-warning',
                3: 'bg-danger',
                4: 'bg-info',
                5: 'bg-primary'
            };
            return badgeClasses[statusValue] || 'bg-secondary';
        }
    },
    
    mounted() {
        this.loadDashboardData();
    }
};