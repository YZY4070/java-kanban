package http;

import com.sun.net.httpserver.HttpServer;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    public static final int PORT = 8080;
    private HttpServer server;

    public Server (TaskManager manager) throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
    }

    public void start(){
        System.out.println("Сервер стартовал. Порт: " + PORT);
        server.start();
    }

    public void stop(){
        System.out.println("Сервер выключен");
        server.stop(0);
    }
}
