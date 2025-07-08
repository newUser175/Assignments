// DOM Elements
const pincodeInput = document.getElementById('pincodeInput');
const searchBtn = document.getElementById('searchBtn');
const loadingSpinner = document.getElementById('loadingSpinner');
const errorMessage = document.getElementById('errorMessage');
const errorText = document.getElementById('errorText');
const weatherCard = document.getElementById('weatherCard');
const historySection = document.getElementById('historySection');
const historyBtn = document.getElementById('historyBtn');
const closeHistoryBtn = document.getElementById('closeHistoryBtn');
const historyContent = document.getElementById('historyContent');

// Weather display elements
const cityName = document.getElementById('cityName');
const stateInfo = document.getElementById('stateInfo');
const pincodeInfo = document.getElementById('pincodeInfo');
const weatherIcon = document.getElementById('weatherIcon');
const tempValue = document.getElementById('tempValue');
const weatherDescription = document.getElementById('weatherDescription');
const humidityValue = document.getElementById('humidityValue');
const windSpeedValue = document.getElementById('windSpeedValue');
const timestampValue = document.getElementById('timestampValue');

// Quick search buttons
const quickBtns = document.querySelectorAll('.quick-btn');

// Current pincode for history
let currentPincode = '';

// Event Listeners
searchBtn.addEventListener('click', handleSearch);
pincodeInput.addEventListener('keypress', (e) => {
    if (e.key === 'Enter') {
        handleSearch();
    }
});

// Input validation - only allow numbers
pincodeInput.addEventListener('input', (e) => {
    e.target.value = e.target.value.replace(/[^0-9]/g, '');
});

historyBtn.addEventListener('click', showHistory);
closeHistoryBtn.addEventListener('click', hideHistory);

// Quick search buttons
quickBtns.forEach(btn => {
    btn.addEventListener('click', () => {
        const pincode = btn.getAttribute('data-pincode');
        pincodeInput.value = pincode;
        handleSearch();
    });
});

// Main search function
async function handleSearch() {
    const pincode = pincodeInput.value.trim();
    
    if (!validatePincode(pincode)) {
        showError('Please enter a valid 6-digit pincode');
        return;
    }
    
    hideAllSections();
    showLoading();
    
    try {
        const response = await fetch(`/api/weather/${pincode}`);
        const data = await response.json();
        
        hideLoading();
        
        if (data.status === 'error') {
            showError(data.message);
        } else {
            currentPincode = pincode;
            displayWeather(data);
        }
    } catch (error) {
        hideLoading();
        showError('Failed to fetch weather data. Please try again.');
        console.error('Error:', error);
    }
}

// Validate pincode format
function validatePincode(pincode) {
    return /^\d{6}$/.test(pincode);
}

// Display weather data
function displayWeather(data) {
    cityName.textContent = data.city;
    stateInfo.textContent = data.state;
    pincodeInfo.textContent = data.pincode;
    
    tempValue.textContent = Math.round(data.temperature);
    weatherDescription.textContent = data.description;
    humidityValue.textContent = `${data.humidity}%`;
    windSpeedValue.textContent = `${data.windSpeed} km/h`;
    
    // Format timestamp
    const timestamp = new Date(data.timestamp);
    timestampValue.textContent = formatDateTime(timestamp);
    
    // Set weather icon based on description
    setWeatherIcon(data.description);
    
    weatherCard.classList.remove('hidden');
}

// Set appropriate weather icon
function setWeatherIcon(description) {
    const desc = description.toLowerCase();
    let iconClass = 'fas fa-sun'; // default
    
    if (desc.includes('rain')) {
        iconClass = 'fas fa-cloud-rain';
    } else if (desc.includes('cloud')) {
        iconClass = 'fas fa-cloud';
    } else if (desc.includes('thunder')) {
        iconClass = 'fas fa-bolt';
    } else if (desc.includes('mist') || desc.includes('haze')) {
        iconClass = 'fas fa-smog';
    } else if (desc.includes('clear')) {
        iconClass = 'fas fa-sun';
    }
    
    weatherIcon.className = iconClass;
}

// Show weather history
async function showHistory() {
    if (!currentPincode) return;
    
    try {
        const response = await fetch(`/api/weather/${currentPincode}/history`);
        const historyData = await response.json();
        
        displayHistory(historyData);
        historySection.classList.remove('hidden');
    } catch (error) {
        showError('Failed to fetch weather history');
        console.error('Error:', error);
    }
}

// Display history data
function displayHistory(historyData) {
    historyContent.innerHTML = '';
    
    if (historyData.length === 0) {
        historyContent.innerHTML = '<p style="text-align: center; color: #666; padding: 20px;">No history available</p>';
        return;
    }
    
    historyData.forEach(item => {
        const historyItem = document.createElement('div');
        historyItem.className = 'history-item';
        
        const timestamp = new Date(item.timestamp);
        
        historyItem.innerHTML = `
            <div class="history-weather">
                <div class="history-temp">${Math.round(item.temperature)}°C</div>
                <div class="history-desc">${item.description}</div>
            </div>
            <div class="history-time">${formatDateTime(timestamp)}</div>
        `;
        
        historyContent.appendChild(historyItem);
    });
}

// Hide history section
function hideHistory() {
    historySection.classList.add('hidden');
}

// Utility functions
function showLoading() {
    loadingSpinner.classList.remove('hidden');
}

function hideLoading() {
    loadingSpinner.classList.add('hidden');
}

function showError(message) {
    errorText.textContent = message;
    errorMessage.classList.remove('hidden');
}

function hideError() {
    errorMessage.classList.add('hidden');
}

function hideAllSections() {
    hideLoading();
    hideError();
    weatherCard.classList.add('hidden');
    historySection.classList.add('hidden');
}

function formatDateTime(date) {
    const options = {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        hour12: true
    };
    return date.toLocaleDateString('en-IN', options);
}

// Initialize app
document.addEventListener('DOMContentLoaded', () => {
    pincodeInput.focus();
    console.log('Weather App initialized successfully!');
});