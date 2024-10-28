package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import http.Server;
import manager.InMemoryTaskManager;
import manager.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Status;
import task.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskTest {

    private static final String BASE_URL = "http://localhost:8080/tasks";
    private Server server;
    private InMemoryTaskManager taskManager;
    private HttpClient client;
    private Task task1;
    private Task task2;
    Gson gson;

    @BeforeEach
    void setUp() throws IOException {
        taskManager = new InMemoryTaskManager(Managers.getDefaultHistoryManager());
        gson = Managers.getGson();

        // Создание задач
        task1 = new Task(1, "task1", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 1, 9, 0), Duration.ofMinutes(40));
        task2 = new Task(2, "task2", "testTask2", Status.NEW, LocalDateTime.of(2020, 1, 2, 9, 0), Duration.ofMinutes(40));

        // Добавление задач в менеджер
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        server = new Server(taskManager);
        client = HttpClient.newHttpClient();
        server.start(); // Запускаем сервер
    }

    @AfterEach
    void tearDown() {
        server.stop(); // Останавливаем сервер
    }

    @Test
    void getAllTasks_ShouldReturn200() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> tasks = gson.fromJson(response.body(), taskType);

        assertEquals(200, response.statusCode(), "Неверный статус");
        assertEquals(2, tasks.size(), "Неверное количество задач");
        assertEquals(task1, tasks.get(0), "Неверная задача 1");
        assertEquals(task2, tasks.get(1), "Неверная задача 2");
    }

    @Test
    void createTask_ShouldReturn201() throws IOException, InterruptedException {
        Task newTask = new Task(3, "task3", "testTask3", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(30));
        String jsonTask = gson.toJson(newTask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Неверный статус при создании задачи");
        assertEquals(gson.toJson(newTask), response.body(), "Неверное сообщение при создании задачи");
    }

    @Test
    void getTaskById_ShouldReturn200() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/1")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Task receivedTask = gson.fromJson(response.body(), Task.class);

        assertEquals(200, response.statusCode(), "Неверный статус");
        assertEquals(task1, receivedTask, "Неверная задача по ID");
    }


    @Test
    void deleteTask_ShouldReturn204() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/1"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(204, response.statusCode(), "Неверный статус при удалении задачи");
    }
}
