package main;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import controller.ExpenseController;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ExpenseHttpServer {
    private ExpenseController controller;

    public ExpenseHttpServer(ExpenseController controller) {
        this.controller = controller;
    }

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new RootHandler());
        server.createContext("/addExpense", new AddExpenseHandler(controller));
        server.setExecutor(null);
        server.start();
        System.out.println("Server started at http://localhost:8080/");
    }

    class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                Map<String, Double> totals = controller.getSubWidgetTotals();

                String html = """
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Expense Tracking System</title>
                        <style>
                            body {
                                font-family: Arial, sans-serif;
                                display: flex;
                                justify-content: center;
                                align-items: center;
                                flex-direction: column;
                                background-color: #f4f4f9;
                                margin: 0;
                                padding: 0;
                            }
                            h1 {
                                color: #333;
                                margin-top: 20px;
                            }
                            .widget-container {
                                display: flex;
                                justify-content: space-around;
                                width: 80%;
                                margin-top: 20px;
                            }
                            .widget {
                                background-color: #fff;
                                border: 1px solid #ddd;
                                border-radius: 8px;
                                width: 30%;
                                padding: 20px;
                                box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
                                display: flex;
                                flex-direction: column;
                                align-items: center;
                            }
                            .widget h2 {
                                text-align: center;
                                color: #555;
                            }
                            .sub-widgets {
                                margin-top: 15px;
                                color: #777;
                                font-size: 14px;
                                padding: 10px;
                                border-top: 1px solid #ddd;
                                display: flex;
                                flex-direction: column;
                                align-items: center;
                                gap: 8px;
                            }
                            .sub-widgets p {
                                margin: 0;
                                text-align: center;
                            }
                            .add-button {
                                display: block;
                                margin: 20px auto;
                                padding: 10px 20px;
                                font-size: 16px;
                                cursor: pointer;
                                background-color: #4CAF50;
                                color: white;
                                border: none;
                                border-radius: 5px;
                            }
                            .form-popup {
                                display: none;
                                position: fixed;
                                top: 50%;
                                left: 50%;
                                transform: translate(-50%, -50%);
                                border: 2px solid #ddd;
                                background-color: #fff;
                                padding: 30px;
                                width: 400px;
                                max-width: 90%;
                                box-shadow: 0px 4px 16px rgba(0, 0, 0, 0.2);
                                z-index: 10;
                                border-radius: 10px;
                            }
                            .form-popup h3 {
                                margin-top: 0;
                                font-size: 20px;
                                color: #333;
                                text-align: center;
                            }
                            .form-popup label {
                                display: block;
                                font-size: 14px;
                                margin-top: 10px;
                                color: #555;
                            }
                            .category-buttons, .sub-widget-buttons {
                                display: flex;
                                gap: 8px;
                                margin-top: 10px;
                                justify-content: center;
                                flex-wrap: wrap;
                            }
                            .category-buttons button, .sub-widget-buttons button {
                                padding: 8px 12px;
                                border: none;
                                border-radius: 5px;
                                cursor: pointer;
                                background-color: #4CAF50;
                                color: white;
                                font-size: 14px;
                            }
                            .form-popup input[type="number"] {
                                width: calc(100% - 20px);
                                padding: 10px;
                                margin: 10px 0;
                                box-sizing: border-box;
                                border: 1px solid #ddd;
                                border-radius: 4px;
                                font-size: 14px;
                            }
                            .form-popup .submit-btn {
                                background-color: #4CAF50;
                                color: white;
                                margin-top: 15px;
                                padding: 10px 15px;
                                font-size: 16px;
                                border: none;
                                border-radius: 5px;
                                width: 100px;
                            }
                            .form-popup .cancel-btn {
                                background-color: #f44336;
                                color: white;
                                margin-top: 15px;
                                padding: 10px 15px;
                                font-size: 16px;
                                border: none;
                                border-radius: 5px;
                                width: 100px;
                                margin-left: 10px;
                            }
                            .form-popup .button-group {
                                display: flex;
                                justify-content: space-between;
                                margin-top: 10px;
                            }
                        </style>
                        <script>
                            const subWidgets = {
                                "Transport": ["Taxi", "Bus", "Scooter"],
                                "Food": ["Restaurant", "Cafe", "Home Products", "Orders"],
                                "Personal Spending": ["Rent Payment", "Utility Fee", "Gym", "Self Care"]
                            };

                            function openForm() {
                                document.getElementById("expenseForm").style.display = "block";
                            }
                            
                            function closeForm() {
                                document.getElementById("expenseForm").style.display = "none";
                            }

                            function setCategory(category) {
                                document.getElementById("selectedCategory").value = category;
                                const subWidgetContainer = document.getElementById("subWidgetContainer");
                                subWidgetContainer.innerHTML = "";
                                subWidgets[category].forEach(subWidget => {
                                    const button = document.createElement("button");
                                    button.innerText = subWidget;
                                    button.onclick = () => setDetail(subWidget);
                                    subWidgetContainer.appendChild(button);
                                });
                            }

                            function setDetail(detail) {
                                document.getElementById("selectedDetail").value = detail;
                            }
                            
                            function submitForm() {
                                const category = document.getElementById("selectedCategory").value;
                                const amount = document.getElementById("amount").value;
                                const detail = document.getElementById("selectedDetail").value;

                                fetch('/addExpense', {
                                    method: 'POST',
                                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                                    body: `category=${category}&amount=${amount}&detail=${detail}`
                                }).then(response => {
                                    if (response.ok) {
                                        alert("Expense added successfully!");
                                        closeForm();
                                        location.reload(); // Reload to update amounts
                                    } else {
                                        alert("Failed to add expense.");
                                    }
                                });
                                return false;
                            }
                        </script>
                    </head>
                    <body>
                        <h1>Expense Tracking System</h1>
                        <div class="widget-container">
                            <div class="widget">
                                <h2>Transport</h2>
                                <div class="sub-widgets">
                    """ +
                        "<p>Taxi - $" + totals.getOrDefault("Taxi", 0.0) + "</p>" +
                        "<p>Bus - $" + totals.getOrDefault("Bus", 0.0) + "</p>" +
                        "<p>Scooter - $" + totals.getOrDefault("Scooter", 0.0) + "</p>" +
                        """
                                    </div>
                                </div>
                                <div class="widget">
                                    <h2>Food</h2>
                                    <div class="sub-widgets">
                        """ +
                        "<p>Restaurant - $" + totals.getOrDefault("Restaurant", 0.0) + "</p>" +
                        "<p>Cafe - $" + totals.getOrDefault("Cafe", 0.0) + "</p>" +
                        "<p>Home Products - $" + totals.getOrDefault("Home Products", 0.0) + "</p>" +
                        "<p>Orders - $" + totals.getOrDefault("Orders", 0.0) + "</p>" +
                        """
                                    </div>
                                </div>
                                <div class="widget">
                                    <h2>Personal Spending</h2>
                                    <div class="sub-widgets">
                        """ +
                        "<p>Rent Payment - $" + totals.getOrDefault("Rent Payment", 0.0) + "</p>" +
                        "<p>Utility Fee - $" + totals.getOrDefault("Utility Fee", 0.0) + "</p>" +
                        "<p>Gym - $" + totals.getOrDefault("Gym", 0.0) + "</p>" +
                        "<p>Self Care - $" + totals.getOrDefault("Self Care", 0.0) + "</p>" +
                        """
                                    </div>
                                </div>
                            </div>
                            <button class="add-button" onclick="openForm()">Add Data</button>
                            <div class="form-popup" id="expenseForm">
                                <h3>Add New Expense</h3>
                                <form onsubmit="return submitForm()">
                                    <div class="category-buttons">
                                        <button type="button" onclick="setCategory('Transport')">Transport</button>
                                        <button type="button" onclick="setCategory('Food')">Food</button>
                                        <button type="button" onclick="setCategory('Personal Spending')">Personal Spending</button>
                                    </div>
                                    <input type="hidden" id="selectedCategory" name="category" required>
                                    
                                    <label for="amount">Amount:</label>
                                    <input type="number" id="amount" name="amount" required>
                                    
                                    <label for="detail">Detail:</label>
                                    <input type="hidden" id="selectedDetail" name="detail" required>
                                    <div class="sub-widget-buttons" id="subWidgetContainer"></div>
                                    
                                    <div class="button-group">
                                        <button type="submit" class="submit-btn">Submit</button>
                                        <button type="button" class="cancel-btn" onclick="closeForm()">Cancel</button>
                                    </div>
                                </form>
                            </div>
                        </body>
                        </html>
                        """;

                exchange.sendResponseHeaders(200, html.length());
                OutputStream os = exchange.getResponseBody();
                os.write(html.getBytes(StandardCharsets.UTF_8));
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        }
    }

    static class AddExpenseHandler implements HttpHandler {
        private ExpenseController controller;

        public AddExpenseHandler(ExpenseController controller) {
            this.controller = controller;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                String[] params = requestBody.split("&");
                String category = "", detail = "";
                double amount = 0;

                for (String param : params) {
                    String[] keyValue = param.split("=");
                    switch (keyValue[0]) {
                        case "category" -> category = keyValue[1];
                        case "amount" -> amount = Double.parseDouble(keyValue[1]);
                        case "detail" -> detail = keyValue[1];
                    }
                }

                controller.addExpense(category, amount, detail);
                exchange.sendResponseHeaders(200, 0);
                OutputStream os = exchange.getResponseBody();
                os.write("Expense added successfully!".getBytes(StandardCharsets.UTF_8));
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        }
    }
}
