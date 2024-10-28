import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.Server;
import http.handlers.specific.utils.DurationTypeGsonAdapter;
import http.handlers.specific.utils.LocalDateTimeTypeGsonAdapter;
import manager.InMemoryTaskManager;
import manager.Managers;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;
import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {


  public static void main(String[] args) throws IOException {

//    LocalDateTimeTypeGsonAdapter localDateTimeTypeGsonAdapter = new LocalDateTimeTypeGsonAdapter();
//    DurationTypeGsonAdapter durationTypeGsonAdapter = new DurationTypeGsonAdapter();
//
//    Gson gson = new GsonBuilder()
//      .setPrettyPrinting()
//      .serializeNulls()
//      .registerTypeAdapter(LocalDateTime.class, localDateTimeTypeGsonAdapter)
//      .registerTypeAdapter(Duration.class, durationTypeGsonAdapter)
//      .create();
//
//      InMemoryTaskManager manager = new InMemoryTaskManager(Managers.getDefaultHistoryManager());
////    Server server = new Server(manager);
//
//    Epic epic = new Epic(1, "Epic 1", "Epic description", Status.NEW);
//    Task task = new Task(1, "task1", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 1, 9, 0), Duration.ofMinutes(40));
//    Task task2 = new Task(2, "task2", "testTask2", Status.NEW, LocalDateTime.of(2020, 1, 2, 9, 0), Duration.ofMinutes(40));
//    Subtask subtask = new Subtask(1, "Subtask 1", "Subtask description", Status.NEW, epic.getId(), LocalDateTime.of(2022, 3, 6, 13, 30), Duration.ofMinutes(30));
//    manager.createTask(task);
//    manager.createTask(task2);
//
////    System.out.println("ТАК ВЫГЛЯДИТ JSON ДЛЯ EPICа: \n" + gson.toJson(epic));
////    System.out.println("ТАК ВЫГЛЯДИТ JSON ДЛЯ TASKи: \n" + gson.toJson(task));
////    System.out.println("ТАК ВЫГЛЯДИТ JSON ДЛЯ SUBTASKи: \n" + gson.toJson(subtask));
//
//    System.out.println(gson.toJson(manager.getAllTasks()));


    Server server;
    InMemoryTaskManager taskManager;
    HttpClient client;
    Task task1;
    Task task2;
    Gson gson;

    taskManager = new InMemoryTaskManager(Managers.getDefaultHistoryManager());
    gson = Managers.getGson();

    // Создание задач
    task1 = new Task(1, "task1", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 1, 9, 0), Duration.ofMinutes(40));
    task2 = new Task(2, "task2", "testTask2", Status.NEW, LocalDateTime.of(2020, 1, 2, 9, 0), Duration.ofMinutes(40));

    // Добавление задач в менеджер
    taskManager.createTask(task1);
    taskManager.createTask(task2);

    server = new Server(taskManager);
    server.start();// Запускаем сервер



  }
}

