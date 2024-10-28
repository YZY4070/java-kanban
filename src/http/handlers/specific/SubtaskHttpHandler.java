package http.handlers.specific;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.handlers.BaseHandler;
import manager.InMemoryTaskManager;
import task.Subtask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SubtaskHttpHandler extends BaseHandler implements HttpHandler {

    public SubtaskHttpHandler(InMemoryTaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            String method = exchange.getRequestMethod();

            switch (method) {
                case "POST" -> handlePost(exchange);
                case "GET" -> handleGet(exchange);
                case "DELETE" -> handleDEL(exchange);
                default -> sendText(exchange, "Неверный метод - " + method, 405);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleGet(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();

            if ("/subtasks".equals(path)) {
                String response = gson.toJson(taskManager.getAllSubtasks());
                sendText(exchange, response, 200);
            } else if (path.startsWith("/subtasks/")) {
                String pathId = path.substring("/subtasks/".length());
                Integer id = Integer.parseInt(pathId);
                if (id > 0 && taskManager.getSubtaskById(id) != null) {
                    String response = gson.toJson(taskManager.getSubtaskById(id));
                    sendText(exchange, response, 200);
                } else sendNotFound(exchange);
            } else sendNotFound(exchange);
        } catch (Exception e) {
            e.printStackTrace();
            sendText(exchange, "server error", 500);
        }
    }

    public void handlePost(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            Subtask subtask = gson.fromJson(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8), Subtask.class);
            if (subtask == null) {
                sendText(exchange, "Bad request", 400);
                return;
            }
            if ("/subtasks".equals(path)) {
                taskManager.createSubtask(subtask);
                String response = gson.toJson(subtask);
                sendText(exchange, response, 201);
            }
            if (path.startsWith("/subtasks/")) {
                String pathID = path.substring("/subtasks/".length());
                int id = Integer.parseInt(pathID);
                if (id <= 0) {
                    subtask.setId(id);
                    taskManager.updateSubtask(subtask);
                    String response = gson.toJson(subtask);
                    sendText(exchange, response, 201);
                } else sendNotFound(exchange);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendText(exchange, "Server error", 500);
        }
    }

    public void handleDEL(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            if (path.startsWith("/subtasks/")) {
                String pathID = path.substring("/subtasks/".length());
                int id = Integer.parseInt(pathID);
                if (id <= 0) {
                    taskManager.deleteSubtaskById(id);
                    sendText(exchange, "Подзадача с id - " + id + " удалена", 200);
                } else {
                    sendNotFound(exchange);
                }
            } else {
                sendNotFound(exchange);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendText(exchange, "server error", 500);
        }
    }
}