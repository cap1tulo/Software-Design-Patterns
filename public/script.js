const subWidgets = {
    "Transport": ["Taxi", "Bus", "Scooter"],
    "Food": ["Restaurant", "Cafe", "Home Products", "Orders"],
    "Personal Spending": ["Rent Payment", "Utility Fee", "Gym", "Self Care"]
};

// Load existing expenses from local storage when the page loads
window.onload = loadExpensesFromLocalStorage;

function openForm() {
    document.getElementById("expenseForm").style.display = "block";
}

function closeForm() {
    document.getElementById("expenseForm").style.display = "none";
    // Clear selected category and sub-widget to prevent unintended submissions
    document.getElementById("selectedCategory").value = "";
    document.getElementById("selectedDetail").value = "";
    document.getElementById("amount").value = "";
    document.getElementById("subWidgetContainer").innerHTML = ""; // Clear sub-widget buttons
}

function setCategory(category) {
    document.getElementById("selectedCategory").value = category;
    document.getElementById("selectedDetail").value = ""; // Clear previous sub-widget selection
    const subWidgetContainer = document.getElementById("subWidgetContainer");
    subWidgetContainer.innerHTML = "";

    // Create sub-widget buttons based on selected category
    subWidgets[category].forEach(subWidget => {
        const button = document.createElement("button");
        button.innerText = subWidget;
        button.onclick = () => setDetail(subWidget);
        subWidgetContainer.appendChild(button);
    });
}

function setDetail(detail) {
    document.getElementById("selectedDetail").value = detail;

    // Automatically trigger submission if the amount is valid
    const amount = parseFloat(document.getElementById("amount").value);
    if (amount > 0) {
        submitForm();
    }
}

function submitForm() {
    const category = document.getElementById("selectedCategory").value;
    const amount = parseFloat(document.getElementById("amount").value);
    const detail = document.getElementById("selectedDetail").value;

    // Validation: Ensure category, detail, and amount are selected/entered
    if (!category) {
        alert("Please select a category.");
        return false;
    }
    if (!detail) {
        alert("Please select a sub-widget.");
        return false;
    }
    if (isNaN(amount) || amount <= 0) {
        alert("Please enter a valid amount.");
        return false;
    }

    // Process the form submission and update local storage
    updateAmount(detail, amount);
    alert("Expense added successfully!");
    closeForm();
    return false;
}

// Load amounts from local storage and update the display
function loadExpensesFromLocalStorage() {
    Object.keys(subWidgets).forEach(category => {
        subWidgets[category].forEach(detail => {
            const storedAmount = localStorage.getItem(detail);
            const amount = storedAmount ? parseFloat(storedAmount) : 0;
            updateDisplay(detail, amount);
        });
    });
}

// Update sub-widget amount in the display and save to local storage
function updateAmount(detail, amount) {
    const currentAmount = parseFloat(localStorage.getItem(detail) || "0");
    const newAmount = currentAmount + amount;

    // Save the new amount to local storage
    localStorage.setItem(detail, newAmount);

    // Update the display with the new amount
    updateDisplay(detail, newAmount);
}

// Update the display for a specific sub-widget
function updateDisplay(detail, amount) {
    const elementIdMap = {
        "Taxi": "taxiAmount",
        "Bus": "busAmount",
        "Scooter": "scooterAmount",
        "Restaurant": "restaurantAmount",
        "Cafe": "cafeAmount",
        "Home Products": "homeProductsAmount",
        "Orders": "ordersAmount",
        "Rent Payment": "rentPaymentAmount",
        "Utility Fee": "utilityFeeAmount",
        "Gym": "gymAmount",
        "Self Care": "selfCareAmount"
    };

    const elementId = elementIdMap[detail];
    document.getElementById(elementId).innerText = amount.toFixed(2);
}
