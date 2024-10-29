package http;

import com.sun.net.httpserver.HttpServer;
import http.handlers.specific.EpicHttpHandler;
import http.handlers.specific.HistoryHttpHandler;
import http.handlers.specific.PrioritizedHttpHandler;
import http.handlers.specific.SubtaskHttpHandler;
import http.handlers.specific.TaskHttpHandler;
import manager.InMemoryTaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    public static final int PORT = 8080;
    private HttpServer server;
    boolean isRunning;

    public Server(InMemoryTaskManager manager) throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/epics", new EpicHttpHandler(manager));
        server.createContext("/tasks", new TaskHttpHandler(manager));
        server.createContext("/subtasks", new SubtaskHttpHandler(manager));
        server.createContext("/history", new HistoryHttpHandler(manager));
        server.createContext("/prioritized", new PrioritizedHttpHandler(manager));
    }

    public void start() throws IOException {

        if (isRunning) {
            throw new IllegalStateException("Server is already running");
        }
        server.start();
        isRunning = true; // Установите флаг, что сервер запущен
        System.out.println("Сервер стартовал");
    }


    public void stop() {
        System.out.println("Сервер выключен");
        server.stop(0);
    }
}
