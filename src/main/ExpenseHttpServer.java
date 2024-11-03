package main;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import controller.ExpenseController;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class ExpenseHttpServer {
    private ExpenseController controller;

    public ExpenseHttpServer(ExpenseController controller) {
        this.controller = controller;
    }

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", new StaticFileHandler("public/index.html", "text/html"));
        server.createContext("/styles.css", new StaticFileHandler("public/styles.css", "text/css"));
        server.createContext("/script.js", new StaticFileHandler("public/script.js", "application/javascript"));

        server.createContext("/addExpense", new AddExpenseHandler(controller));

        server.setExecutor(null);
        server.start();
        System.out.println("Server started at http://localhost:8080/");
    }

    static class StaticFileHandler implements HttpHandler {
        private String filePath;
        private String contentType;

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
