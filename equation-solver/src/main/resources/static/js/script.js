// DOM Elements
const equationInput = document.getElementById('equationInput');
const storeBtn = document.getElementById('storeBtn');
const loadingSpinner = document.getElementById('loadingSpinner');
const messageDiv = document.getElementById('message');
const equationsSection = document.getElementById('equationsSection');
const equationsList = document.getElementById('equationsList');
const refreshBtn = document.getElementById('refreshBtn');
const evaluationModal = document.getElementById('evaluationModal');
const modalOverlay = document.getElementById('modalOverlay');
const closeModalBtn = document.getElementById('closeModalBtn');
const modalEquationText = document.getElementById('modalEquationText');
const variableInputsContainer = document.getElementById('variableInputs');
const evaluateModalBtn = document.getElementById('evaluateModalBtn');
const resultDisplay = document.getElementById('resultDisplay');
const resultValue = document.getElementById('resultValue');

// Example buttons
const exampleBtns = document.querySelectorAll('.example-btn');

// Current equation being evaluated
let currentEquationId = '';
let currentEquation = '';

// Event Listeners
storeBtn.addEventListener('click', storeEquation);
equationInput.addEventListener('keypress', (e) => {
    if (e.key === 'Enter') {
        storeEquation();
    }
});

refreshBtn.addEventListener('click', loadEquations);
closeModalBtn.addEventListener('click', closeModal);
modalOverlay.addEventListener('click', (e) => {
    if (e.target === modalOverlay) {
        closeModal();
    }
});
evaluateModalBtn.addEventListener('click', evaluateEquation);

// Example buttons
exampleBtns.forEach(btn => {
    btn.addEventListener('click', () => {
        const equation = btn.getAttribute('data-equation');
        equationInput.value = equation;
    });
});

// Store equation function
async function storeEquation() {
    const equation = equationInput.value.trim();
    
    if (!equation) {
        showMessage('Please enter an equation', 'error');
        return;
    }
    
    hideMessage();
    showLoading();
    
    try {
        const response = await fetch('/api/equations/store', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ equation: equation })
        });
        
        const data = await response.json();
        hideLoading();
        
        if (response.ok) {
            showMessage(`Equation stored successfully! ID: ${data.equationId}`, 'success');
            equationInput.value = '';
            loadEquations();
        } else {
            showMessage(data.error || 'Failed to store equation', 'error');
        }
    } catch (error) {
        hideLoading();
        showMessage('Network error. Please try again.', 'error');
        console.error('Error:', error);
    }
}

// Load equations function
async function loadEquations() {
    try {
        const response = await fetch('/api/equations');
        const data = await response.json();
        
        if (response.ok) {
            displayEquations(data.equations);
            equationsSection.classList.remove('hidden');
        } else {
            showMessage('Failed to load equations', 'error');
        }
    } catch (error) {
        showMessage('Network error while loading equations', 'error');
        console.error('Error:', error);
    }
}

// Display equations function
function displayEquations(equations) {
    equationsList.innerHTML = '';
    
    if (equations.length === 0) {
        equationsList.innerHTML = '<p style="text-align: center; color: #666; padding: 40px;">No equations stored yet. Add some equations above!</p>';
        return;
    }
    
    equations.forEach(eq => {
        const equationItem = document.createElement('div');
        equationItem.className = 'equation-item';
        
        equationItem.innerHTML = `
            <div class="equation-info">
                <div class="equation-id">ID: ${eq.equationId}</div>
                <div class="equation-text">${eq.equation}</div>
            </div>
            <button class="evaluate-btn" onclick="openEvaluationModal('${eq.equationId}', '${eq.equation}')">
                <i class="fas fa-calculator"></i>
                Evaluate
            </button>
        `;
        
        equationsList.appendChild(equationItem);
    });
}

// Open evaluation modal
function openEvaluationModal(equationId, equation) {
    currentEquationId = equationId;
    currentEquation = equation;
    
    modalEquationText.textContent = equation;
    
    // Extract variables from equation
    const variables = extractVariables(equation);
    createVariableInputs(variables);
    
    resultDisplay.classList.add('hidden');
    modalOverlay.classList.remove('hidden');
}

// Extract variables from equation
function extractVariables(equation) {
    const variableRegex = /[a-zA-Z]+/g;
    const matches = equation.match(variableRegex) || [];
    return [...new Set(matches)]; // Remove duplicates
}

// Create variable input fields
function createVariableInputs(variables) {
    variableInputsContainer.innerHTML = '';
    
    variables.forEach(variable => {
        const inputDiv = document.createElement('div');
        inputDiv.className = 'variable-input';
        
        inputDiv.innerHTML = `
            <label for="var_${variable}">${variable} =</label>
            <input type="number" id="var_${variable}" step="any" placeholder="Enter value for ${variable}">
        `;
        
        variableInputsContainer.appendChild(inputDiv);
    });
}

// Evaluate equation
async function evaluateEquation() {
    const variables = {};
    const inputs = variableInputsContainer.querySelectorAll('input');
    
    // Collect variable values
    for (let input of inputs) {
        const variable = input.id.replace('var_', '');
        const value = parseFloat(input.value);
        
        if (isNaN(value)) {
            showMessage(`Please enter a valid number for variable ${variable}`, 'error');
            return;
        }
        
        variables[variable] = value;
    }
    
    try {
        const response = await fetch(`/api/equations/${currentEquationId}/evaluate`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ variables: variables })
        });
        
        const data = await response.json();
        
        if (response.ok) {
            resultValue.textContent = data.result;
            resultDisplay.classList.remove('hidden');
        } else {
            showMessage(data.error || 'Failed to evaluate equation', 'error');
        }
    } catch (error) {
        showMessage('Network error during evaluation', 'error');
        console.error('Error:', error);
    }
}

// Close modal
function closeModal() {
    modalOverlay.classList.add('hidden');
    hideMessage();
}

// Utility functions
function showLoading() {
    loadingSpinner.classList.remove('hidden');
}

function hideLoading() {
    loadingSpinner.classList.add('hidden');
}

function showMessage(text, type) {
    messageDiv.textContent = text;
    messageDiv.className = `message ${type}`;
    messageDiv.classList.remove('hidden');
    
    // Auto-hide success messages after 3 seconds
    if (type === 'success') {
        setTimeout(() => {
            hideMessage();
        }, 3000);
    }
}

function hideMessage() {
    messageDiv.classList.add('hidden');
}

// Initialize app
document.addEventListener('DOMContentLoaded', () => {
    equationInput.focus();
    loadEquations();
    console.log('Equation Solver App initialized successfully!');
});