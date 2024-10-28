package http.handlers.specific;

import com.google.gson.stream.JsonReader;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.handlers.BaseHandler;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import task.Epic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class EpicHttpHandler extends BaseHandler implements HttpHandler {

    public EpicHttpHandler(InMemoryTaskManager taskManager) {
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

    private void handlePost(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            Epic epic = gson.fromJson(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8), Epic.class);

            if (epic == null) {
                throw new IllegalArgumentException("Bad request");
            }

            if ("/epics".equals(path)) {
                taskManager.createEpic(epic);
                String response = gson.toJson(epic);
                sendText(exchange, response, 200);
            } else if (path.startsWith("/epics/")) {
                String pathId = path.substring("/epics/".length());
                int id = -1;
                try {
                    id = Integer.parseInt(pathId);
                } catch (NumberFormatException e) {
                    sendNotFound(exchange);
                    return;
                }

                epic.setId(id);
                taskManager.updateEpic(epic);
                String response = gson.toJson(epic);
                sendText(exchange, response, 201);
            }
        } catch (IllegalArgumentException e) {
            sendText(exchange, "Bad Request", 400);
        } catch (IOException e) {
            sendHasInteractions(exchange);
        } catch (Exception e) {
            e.printStackTrace();
            sendText(exchange, "Internal Server Error", 500);
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();

            if ("/epics".equals(path)) {
                String response = gson.toJson(taskManager.getAllEpics());
                sendText(exchange, response, 200);
            } else if (path.startsWith("/epics/") && path.indexOf('/', 7) == -1) {
                String pathId = path.substring(7);
                int id = Integer.parseInt(pathId);
                if (id != -1) {
                    Object epic = taskManager.getEpicById(id);
                    if (epic != null) {
                        String response = gson.toJson(epic);
                        sendText(exchange, response, 200);
                    } else {
                        sendNotFound(exchange);
                    }
                } else {
                    sendNotFound(exchange);
                }
            } else if (path.startsWith("/epics/") && path.endsWith("/subtasks")) {
                String pathId = path.substring(7, path.lastIndexOf("/subtasks"));
                int id = Integer.parseInt(pathId);
                if (id != -1) {
                    if (taskManager.getSubtasksByEpicId(id) != null) {
                        String response = gson.toJson(taskManager.getSubtasksByEpicId(id));
                        sendText(exchange, response, 200);
                    } else {
                        sendNotFound(exchange);
                    }
                } else {
                    sendNotFound(exchange);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendText(exchange, "Internal Server Error", 500);
        }
    }

    private void handleDEL(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();

            if (path.startsWith("/epics/") && path.indexOf("/", 7) == -1) {
                String pathId = path.substring(7);
                int id = Integer.parseInt(pathId);
                Object epic = taskManager.getEpicById(id);
                if (epic != null) {
                    taskManager.deleteEpicById(id);
                    sendText(exchange, "Эпик с id " + id + " удален", 200);
                } else sendNotFound(exchange);
            } else sendNotFound(exchange);
        } catch (Exception e) {
            e.printStackTrace();
            sendText(exchange, "Server error", 500);
        }
    }
}

