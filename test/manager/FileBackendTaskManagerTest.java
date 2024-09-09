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
    void testSaveMultipleTasks() {
        // Создаем несколько задач
        Task task1 = new Task(0, "Task 1", "Description 1", Status.NEW);
        Task task2 = new Task(2, "Task 2", "Description 2", Status.IN_PROGRESS);
        Epic epic1 = new Epic(3, "Epic1", "Description1", Status.NEW);
        Subtask subtask1 = new Subtask(0, "Subtask1", "descripton", Status.NEW, epic1.getId());

        // Сохраняем их в менеджере задач
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);

        // Проверяем, что задачи корректно добавлены
        List<Task> tasks = taskManager.getAllTasks();
        assertEquals(2, tasks.size());
        assertEquals("Task 1", tasks.get(0).getName());
        assertEquals("Task 2", tasks.get(1).getName());
        assertEquals(subtask1.getId(), epic1.getSubtaskIds().get(0));
    }

    @Test
    void testLoadMultipleTasks() {
        // Создаем несколько задач и сохраняем их
        Task task1 = new Task(0, "Task 1", "Description 1", Status.NEW);
        Task task2 = new Task(0, "Task 2", "Description 2", Status.IN_PROGRESS);
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
