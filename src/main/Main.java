package main;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import controller.ExpenseController;
import view.ExpenseViewObserver;
import command.AddExpenseCommand;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        // Create the ExpenseController
        ExpenseController controller = new ExpenseController();

        // Register the observer for the controller
        ExpenseViewObserver viewObserver = new ExpenseViewObserver();
        controller.addObserver(viewObserver);

        try {
            // Start the HTTP server with the ExpenseController
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

            // Serve static files (HTML, CSS, JavaScript)
            server.createContext("/", new StaticFileHandler("public/index.html", "text/html"));
            server.createContext("/styles.css", new StaticFileHandler("public/styles.css", "text/css"));
            server.createContext("/script.js", new StaticFileHandler("public/script.js", "application/javascript"));

            // Add a context to handle dynamic requests (e.g., adding an expense)
            server.createContext("/addExpense", new AddExpenseHandler(controller));

            server.setExecutor(null); // Default executor
            server.start();
            System.out.println("Server started at http://localhost:8080/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class StaticFileHandler implements HttpHandler {
        private final String filePath;
        private final String contentType;

        public StaticFileHandler(String filePath, String contentType) {
            this.filePath = filePath;
            this.contentType = contentType;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", contentType);
            byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
            exchange.sendResponseHeaders(200, fileContent.length);
            OutputStream os = exchange.getResponseBody();
            os.write(fileContent);
            os.close();
        }
    }

    static class AddExpenseHandler implements HttpHandler {
        private final ExpenseController controller;

        public AddExpenseHandler(ExpenseController controller) {
            this.controller = controller;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                String requestBody = new String(exchange.getRequestBody().readAllBytes());
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

                AddExpenseCommand addExpenseCommand = new AddExpenseCommand(controller, category, amount, detail);
                addExpenseCommand.execute();

                exchange.sendResponseHeaders(200, 0);
                OutputStream os = exchange.getResponseBody();
                os.write("Expense added successfully!".getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }
}
