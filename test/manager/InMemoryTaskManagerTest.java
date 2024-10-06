package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

class InMemoryTaskManagerTest {

    @Test
    public void createTaskShouldExisInTaskManager() {
        Task task = new Task(1, "Task 1", "Description 1", Status.NEW, LocalDateTime.of(2022, 11, 6, 13, 30), Duration.ofMinutes(30));
        TaskManager taskManager = Managers.getDefault();
        taskManager.createTask(task);
        Assertions.assertTrue(Objects.equals(task, taskManager.getTaskById(1)));
    }

    @Test
    public void shouldAddAndSearch() {
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task(1, "Task 1", "Description 1", Status.NEW, LocalDateTime.of(2022, 11, 6, 13, 30), Duration.ofMinutes(30));
        Epic epic = new Epic(2, "name", "description", Status.NEW);
        Subtask subtask = new Subtask(3, "name", "description", Status.NEW, epic.getId(), LocalDateTime.of(2024, 9, 13, 14, 30), Duration.ofMinutes(120));

        taskManager.createTask(task1);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        Assertions.assertNotNull(taskManager.getTaskById(1));
        Assertions.assertNotNull(taskManager.getEpicById(2));
        Assertions.assertNotNull(taskManager.getSubtaskById(3));
    }
}
