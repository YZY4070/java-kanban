package http.handlers.specific;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.handlers.BaseHandler;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import task.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

public class TaskHttpHandler extends BaseHandler implements HttpHandler {

    public TaskHttpHandler(InMemoryTaskManager taskManager) {
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

    private void handleGet(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();


            if ("/tasks".equals(path)) { // Проверка пути "/tasks"
                String response = gson.toJson(taskManager.getAllTasks());
                sendText(exchange, response, 200);
                return;
            }

            if (path.startsWith("/tasks/")) { // Проверка пути "/tasks/{id}"
                String pathId = path.substring("/tasks/".length());
                int id = Integer.parseInt(pathId);
                if (id != -1) {
                    if (taskManager.getTaskById(id) != null) {
                        String response = gson.toJson(taskManager.getTaskById(id));
                        sendText(exchange, response, 200);
                    } else {
                        sendNotFound(exchange);
                    }
                } else {
                    sendNotFound(exchange);
                }
                return;
            }
            sendNotFound(exchange); // Если путь не соответствует ни одному из условий, возвращаем 404

        } catch (Exception e) {
            e.printStackTrace();
            sendText(exchange, "Internal Server Error", 500);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            Task task = gson.fromJson(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8), Task.class);
            if (task == null) {
                throw new IllegalArgumentException("Bad request");
            }
            if ("/tasks".equals(path)) {
                taskManager.createTask(task);
                String response = gson.toJson(task);
                sendText(exchange, response, 201);
            }
            if (path.startsWith("/tasks/")) {
                String pathId = path.substring("/tasks/".length());
                int id = Integer.parseInt(pathId);
                if (id <= 0) {
                    task.setId(id);
                    taskManager.updateTask(task);
                    String response = gson.toJson(task);
                    sendText(exchange, response, 200);
                } else {
                    sendNotFound(exchange);
                }
            }
        } catch (IllegalArgumentException e) {
            sendText(exchange, "Bad Request", 400);
        } catch (NoSuchElementException e) {
            sendNotFound(exchange);
        }
    }

    private void handleDEL(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();

            if (path.startsWith("/tasks/")) {
                String pathId = path.substring("/tasks/".length());
                int id = Integer.parseInt(pathId);
                if (id > 0) {
                    taskManager.deleteTaskById(id);
                    sendText(exchange, "Task с id: " + id + " - удалена", 204);
                } else {
                    sendNotFound(exchange);
                }
            } else {
                sendNotFound(exchange);
            }
        } catch (IllegalArgumentException e) {
            sendText(exchange, "Bad request", 400);
        } catch (NoSuchElementException e) {
            sendNotFound(exchange);
        } catch (Exception e) {
            e.printStackTrace();
            sendText(exchange, "Internal Server Error", 500);
        }
    }
}
