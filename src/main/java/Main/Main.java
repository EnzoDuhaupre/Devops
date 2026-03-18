package Main;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.io.OutputStream;

public class Main {
    public static void main(String[] args) throws IOException {
        // Créer un serveur HTTP sur le port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Route /
        server.createContext("/", exchange -> {
            String response = "Hello and welcome!";
            exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        });

        // Route /health
        server.createContext("/health", exchange -> {
            String response = "OK";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        });

        server.setExecutor(null);
        server.start();
        System.out.println("✅ Serveur démarré sur http://localhost:8080");

        for (int i = 1; i <= 5; i++) {
            System.out.println("i = " + i);
        }
    }
}
