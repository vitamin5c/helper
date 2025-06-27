const { createApp } = Vue;

// API base URL
const API_BASE_URL = '';

// Axios configuration
axios.defaults.baseURL = API_BASE_URL;
axios.defaults.headers.common['Content-Type'] = 'application/json';

// Global error handler
axios.interceptors.response.use(
    response => response,
    error => {
        console.error('API Error:', error);
        if (error.response) {
            const message = error.response.data?.message || 'An error occurred';
            alert(`Error: ${message}`);
        } else {
            alert('Network error. Please check your connection.');
        }
        return Promise.reject(error);
    }
);

// Main Vue application
const app = createApp({
    data() {
        return {
            activeTab: 'dashboard'
        };
    },
    methods: {
        setActiveTab(tab) {
            this.activeTab = tab;
        }
    },
    mounted() {
        console.log('Personal Life Management System loaded successfully!');
    }
});

// Register components
app.component('dashboard-component', DashboardComponent);
app.component('expense-records-component', ExpenseRecordsComponent);
app.component('life-records-component', LifeRecordsComponent);
app.component('health-info-component', HealthInfoComponent);
app.component('calorie-records-component', CalorieRecordsComponent);

// Mount the app
app.mount('#app');

// Utility functions
window.utils = {
    formatDate(date) {
        if (!date) return '';
        return new Date(date).toLocaleDateString();
    },
    
    formatDateTime(dateTime) {
        if (!dateTime) return '';
        return new Date(dateTime).toLocaleString();
    },
    
    formatCurrency(amount) {
        if (amount === null || amount === undefined) return '$0.00';
        return new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency: 'USD'
        }).format(amount);
    },
    
    formatNumber(number, decimals = 2) {
        if (number === null || number === undefined) return '0';
        return Number(number).toFixed(decimals);
    },
    
    showSuccess(message) {
        // Simple success notification
        const alert = document.createElement('div');
        alert.className = 'alert alert-success alert-dismissible fade show position-fixed';
        alert.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px;';
        alert.innerHTML = `
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;
        document.body.appendChild(alert);
        
        setTimeout(() => {
            if (alert.parentNode) {
                alert.parentNode.removeChild(alert);
            }
        }, 3000);
    },
    
    showError(message) {
        // Simple error notification
        const alert = document.createElement('div');
        alert.className = 'alert alert-danger alert-dismissible fade show position-fixed';
        alert.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px;';
        alert.innerHTML = `
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;
        document.body.appendChild(alert);
        
        setTimeout(() => {
            if (alert.parentNode) {
                alert.parentNode.removeChild(alert);
            }
        }, 5000);
    },
    
    confirmDelete(message = 'Are you sure you want to delete this item?') {
        return confirm(message);
    }
};