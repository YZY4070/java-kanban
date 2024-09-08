package manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Status;
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
        System.out.println(content);
        assertEquals(CSVTaskFormat.header(), content);
        /*что-то без понятия как проверить, решил проверить что он выводит, но вроде то что я и ожидаю, ???*/

    }

    @Test
    void testLoadEmptyFile() {
        // Тест на загрузку пустого файла
        FileBackendTaskManager loadedTaskManager = FileBackendTaskManager.loadFromFile(file);

        // Проверяем, что задачи, эпики и сабтаски пустые
        assertTrue(loadedTaskManager.getAllTasks().isEmpty());
        assertTrue(loadedTaskManager.getAllEpics().isEmpty());
        assertTrue(loadedTaskManager.getAllSubtasks().isEmpty());
        //без понятия вообще как написать этот тест, ведь из-за пустоты все ссыпется заранее как я понимаю
    }

    @Test
    void testSaveMultipleTasks() {
        // Создаем несколько задач
        Task task1 = new Task(0, "Task 1", "Description 1", Status.NEW);
        Task task2 = new Task(0, "Task 2", "Description 2", Status.IN_PROGRESS);

        // Сохраняем их в менеджере задач
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        // Проверяем, что задачи корректно добавлены
        List<Task> tasks = taskManager.getAllTasks();
        assertEquals(2, tasks.size());
        assertEquals("Task 1", tasks.get(0).getName());
        assertEquals("Task 2", tasks.get(1).getName());
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
