package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import http.Server;
import manager.InMemoryTaskManager;
import manager.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;

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

public class SubtaskHttpHandlerTest {

    private static final String BASE_URL = "http://localhost:8080/subtasks";
    private Server server;
    private InMemoryTaskManager taskManager;
    private HttpClient client;
    private Subtask subtask1;
    private Subtask subtask2;
    private Epic epic;
    Gson gson;

    @BeforeEach
    void setUp() throws IOException {
        taskManager = new InMemoryTaskManager(Managers.getDefaultHistoryManager());
        gson = Managers.getGson();

        // Создание подзадач
        epic = new Epic(1, "epic", "description", Status.NEW);
        subtask1 = new Subtask(2, "subtask1", "testSubtask1", Status.NEW, epic.getId(), LocalDateTime.of(2020, 1, 3, 10, 0), Duration.ofMinutes(30));
        subtask2 = new Subtask(3, "subtask2", "testSubtask2", Status.NEW, epic.getId(), LocalDateTime.of(2020, 1, 4, 10, 0), Duration.ofMinutes(30));

        // Добавление подзадач в менеджер
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        server = new Server(taskManager);
        client = HttpClient.newHttpClient();
        server.start(); // Запускаем сервер
    }

    @AfterEach
    void tearDown() {
        server.stop(); // Останавливаем сервер
    }

    @Test
    void getAllSubtasks_ShouldReturn200() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type subtaskType = new TypeToken<ArrayList<Subtask>>() {
        }.getType();
        List<Subtask> subtasks = gson.fromJson(response.body(), subtaskType);
        System.out.println(taskManager.getAllSubtasks());

        assertEquals(200, response.statusCode(), "Неверный статус");
        assertEquals(2, subtasks.size(), "Неверное количество подзадач");
        assertEquals(subtask1, subtasks.get(0), "Неверная подзадача 1");
        assertEquals(subtask2, subtasks.get(1), "Неверная подзадача 2");
    }

    @Test
    void createSubtask_ShouldReturn201() throws IOException, InterruptedException {
        Subtask newSubtask = new Subtask(4, "subtask3", "testSubtask3", Status.NEW, epic.getId(), LocalDateTime.now(), Duration.ofMinutes(25));
        String jsonSubtask = gson.toJson(newSubtask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonSubtask))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Неверный статус при создании подзадачи");
        assertEquals(gson.toJson(newSubtask), response.body(), "Неверное сообщение при создании подзадачи");
    }

    @Test
    void getSubtaskById_ShouldReturn200() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/2")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Subtask receivedSubtask = gson.fromJson(response.body(), Subtask.class);

        assertEquals(200, response.statusCode(), "Неверный статус");
        assertEquals(subtask1, receivedSubtask, "Неверная подзадача по ID");
    }

    @Test
    void deleteSubtask_ShouldReturn200() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/2"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Неверный статус при удалении подзадачи");
    }
}
