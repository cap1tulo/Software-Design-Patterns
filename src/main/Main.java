package main;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import controller.ExpenseController;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        ExpenseController controller = new ExpenseController();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Set up handlers
        server.createContext("/", new StaticFileHandler("public/index.html", "text/html"));
        server.createContext("/styles.css", new StaticFileHandler("public/styles.css", "text/css"));
        server.createContext("/script.js", new StaticFileHandler("public/script.js", "application/javascript"));

        server.setExecutor(null);
        server.start();
        System.out.println("Server started at http://localhost:8080/");
    }

    // StaticFileHandler to serve CSS, JS, and HTML files
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
}

