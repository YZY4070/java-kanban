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

public class EpicHttpHandlerTest {
    private static final String BASE_URL = "http://localhost:8080/epics";
    private Server server;
    private InMemoryTaskManager taskManager;
    private HttpClient client;
    private Epic epic1;
    private Epic epic2;
    private Subtask subtask1;
    private Subtask subtask2;
    Gson gson = Managers.getGson();

    @BeforeEach
    void setUp() throws IOException {
        taskManager = new InMemoryTaskManager(Managers.getDefaultHistoryManager());
        gson = Managers.getGson();

        // Создание задач
        epic1 = new Epic(1, "Epic 1", "Epic Description", Status.NEW);
        epic2 = new Epic(2, "Epic 2", "Epic Description", Status.NEW);
//        subtask1 = new Subtask(3, "Subtask 1", "Subtask Description", Status.NEW, epic1.getId(), LocalDateTime.of(2021, 3, 10, 13, 30), Duration.ofMinutes(30));
//        subtask2 = new Subtask(4, "Subtask 1", "Subtask Description", Status.NEW, epic2.getId(), LocalDateTime.of(2021, 3, 10, 15, 30), Duration.ofMinutes(30));

        // Добавление задач в менеджер
        epic1.setStartTime(LocalDateTime.now());
        epic1.setEndTime(LocalDateTime.now());
        epic2.setStartTime(LocalDateTime.now());
        epic2.setEndTime(LocalDateTime.now());
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

//        taskManager.createSubtask(subtask1);
//        taskManager.createSubtask(subtask2);

        System.out.println(taskManager.getAllEpics());

        server = new Server(taskManager);
        client = HttpClient.newHttpClient();
        server.start(); // Запускаем сервер
    }

    @AfterEach
    void tearDown() {
        server.stop(); // Останавливаем сервер
    }

    @Test
    void getAllEpics_ShouldReturn200() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type epicType = new TypeToken<ArrayList<Epic>>() {
        }.getType();
        List<Epic> epics = gson.fromJson(response.body(), epicType);
        System.out.println(taskManager.getAllEpics());

        assertEquals(200, response.statusCode(), "Неверный статус");
        assertEquals(2, epics.size(), "Неверное количество эпиков");
        assertEquals(epic1, epics.get(0), "Неверный эпик 1");
        assertEquals(epic2, epics.get(1), "Неверный эпик 2");
    }

    @Test
    void createEpic_ShouldReturn200() throws IOException, InterruptedException {
        Epic newEpic = new Epic(3, "Epic 3", "New Epic Description", Status.NEW);
        newEpic.setStartTime(LocalDateTime.now());
        newEpic.setEndTime(LocalDateTime.now());
        String jsonEpic = gson.toJson(newEpic);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpic))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epic54 = gson.fromJson(response.body(), Epic.class);

        assertEquals(200, response.statusCode(), "Неверный статус при создании эпика");
        assertEquals(newEpic, epic54, "Неверное сообщение при создании эпика");
    }

    @Test
    void getEpicById_ShouldReturn200() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/1")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Epic receivedEpic = gson.fromJson(response.body(), Epic.class);

        assertEquals(200, response.statusCode(), "Неверный статус");
        assertEquals(epic1, receivedEpic, "Неверный эпик по ID");
    }

    @Test
    void deleteEpic_ShouldReturn204() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/1"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Неверный статус при удалении эпика");
    }

    @Test
    void canGetEpicSubtasks() throws IOException, InterruptedException {
        Subtask subtask1 = new Subtask(3, "subtasks1", "descriptio1", Status.NEW, epic1.getId(), LocalDateTime.of(2002, 3, 4, 13, 30), Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask(4, "asd", "wefwe", Status.NEW, epic1.getId(), LocalDateTime.of(2002, 3, 5, 13, 40), Duration.ofMinutes(40));
        Subtask subtask3 = new Subtask(5, "wepofi", "eroigj", Status.NEW, epic1.getId(), LocalDateTime.of(2002, 3, 6, 13, 50), Duration.ofMinutes(50));
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/1/subtasks")).GET().build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        Type subtaskType = new TypeToken<ArrayList<Subtask>>() {
        }.getType();
        List<Subtask> subtasks = gson.fromJson(response.body(), subtaskType);

        assertEquals(200, response.statusCode(), "Неверный статус");
        assertEquals(3, subtasks.size(), "Неверное количество подзадач");
        assertEquals(subtask1, subtasks.get(0), "Неверная подзадача 1");
    }
}

