package manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackendTaskManagerTest {
    private FileBackendTaskManager taskManager;
    private File file;

    @BeforeEach
    void setUp() throws IOException {
        // Создаем временный файл для каждого теста
        file = File.createTempFile("task_manager_test", ".csv");
        taskManager = new FileBackendTaskManager(file);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Удаляем временный файл после каждого теста
        Files.deleteIfExists(file.toPath());
    }

    @Test
    void testSaveEmptyFile() throws IOException {
        taskManager.save();

        String content = Files.readString(file.toPath());
        assertEquals(CSVTaskFormat.header() + System.lineSeparator(), content);

    }

    @Test
    void testLoadEmptyFile() throws IOException {
        Files.writeString(file.toPath(), CSVTaskFormat.header() + System.lineSeparator());

        FileBackendTaskManager loadedTaskManager = FileBackendTaskManager.loadFromFile(file);

        assertTrue(loadedTaskManager.getAllTasks().isEmpty());
        assertTrue(loadedTaskManager.getAllEpics().isEmpty());
        assertTrue(loadedTaskManager.getAllSubtasks().isEmpty());
    }


    @Test
    void testSaveMultipleTasks() throws IOException {
        // Создаем несколько задач
        Task task1 = new Task(1, "Task 1", "Description 1", Status.NEW, LocalDateTime.of(2022, 11, 6, 13, 30), Duration.ofMinutes(30));
        Task task2 = new Task(2, "Task 2", "Description 2", Status.IN_PROGRESS, LocalDateTime.of(2022, 10, 3, 12, 10), Duration.ofMinutes(54));
        Epic epic1 = new Epic(3, "Epic 1", "Epic Description", Status.NEW);
        Subtask subtask1 = new Subtask(4, "Subtask 1", "Subtask Description", Status.NEW, epic1.getId(), LocalDateTime.of(2021, 3, 10, 13, 30), Duration.ofMinutes(30));

        // Сохраняем их в менеджере задач
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);

        // Сохраняем данные в файл
        taskManager.save();

        // Загружаем задачи из файла
        FileBackendTaskManager loadedTaskManager = FileBackendTaskManager.loadFromFile(file);

        // Проверяем задачи
        List<Task> loadedTasks = loadedTaskManager.getAllTasks();
        assertEquals(2, loadedTasks.size());

        Task loadedTask1 = loadedTasks.get(0);
        Task loadedTask2 = loadedTasks.get(1);

        // Проверка всех полей для task1
        assertEquals(task1.getId(), loadedTask1.getId());
        assertEquals(task1.getName(), loadedTask1.getName());
        assertEquals(task1.getDescription(), loadedTask1.getDescription());
        assertEquals(task1.getStatus(), loadedTask1.getStatus());

        // Проверка всех полей для task2
        assertEquals(task2.getId(), loadedTask2.getId());
        assertEquals(task2.getName(), loadedTask2.getName());
        assertEquals(task2.getDescription(), loadedTask2.getDescription());
        assertEquals(task2.getStatus(), loadedTask2.getStatus());

        // Проверяем эпики
        List<Epic> loadedEpics = loadedTaskManager.getAllEpics();
        assertEquals(1, loadedEpics.size());

        Epic loadedEpic1 = loadedEpics.get(0);

        // Проверка всех полей для epic1
        assertEquals(epic1.getId(), loadedEpic1.getId());
        assertEquals(epic1.getName(), loadedEpic1.getName());
        assertEquals(epic1.getDescription(), loadedEpic1.getDescription());
        assertEquals(epic1.getStatus(), loadedEpic1.getStatus());

        // Проверяем подзадачи
        List<Subtask> loadedSubtasks = loadedTaskManager.getAllSubtasks();
        assertEquals(1, loadedSubtasks.size());

        Subtask loadedSubtask1 = loadedSubtasks.get(0);

        // Проверка всех полей для subtask1
        assertEquals(subtask1.getId(), loadedSubtask1.getId());
        assertEquals(subtask1.getName(), loadedSubtask1.getName());
        assertEquals(subtask1.getDescription(), loadedSubtask1.getDescription());
        assertEquals(subtask1.getStatus(), loadedSubtask1.getStatus());
        assertEquals(subtask1.getEpicId(), loadedSubtask1.getEpicId());

        // Проверка связи подзадачи с эпиком
        assertEquals(1, loadedEpic1.getSubtaskIds().size());
        assertEquals(subtask1.getId(), loadedEpic1.getSubtaskIds().get(0));
    }


    @Test
    void testLoadMultipleTasks() {
        // Создаем несколько задач и сохраняем их
        Task task1 = new Task(1, "Task 1", "Description 1", Status.NEW, LocalDateTime.of(2022, 9, 6, 13, 30), Duration.ofMinutes(30));
        Task task2 = new Task(2, "Task 2", "Description 2", Status.IN_PROGRESS, LocalDateTime.of(2022, 10, 3, 12, 10), Duration.ofMinutes(54));
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        // Загружаем менеджер задач из файла
        FileBackendTaskManager loadedTaskManager = FileBackendTaskManager.loadFromFile(file);

        // Проверяем, что загружены 2 задачи
        List<Task> tasks = loadedTaskManager.getAllTasks();
        assertEquals(2, tasks.size());
        assertEquals("Task 1", tasks.get(0).getName());
        assertEquals("Task 2", tasks.get(1).getName());
    }
}
