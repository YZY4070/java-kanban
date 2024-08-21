package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import package_Task.Epic;
import package_Task.Status;
import package_Task.Subtask;
import package_Task.Task;

import java.util.Objects;

class InMemoryTaskManagerTest {

    @Test
    public void createTaskShouldExisInTaskManager() {
        Task task = new Task(1, "name", "description", Status.NEW);
        TaskManager taskManager = Managers.getDefault();
        taskManager.createTask(task);
        Assertions.assertTrue(Objects.equals(task, taskManager.getTaskById(1)));
    }

    @Test
    public void shouldAddAndSearch() {
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task(1, "name", "description", Status.NEW);
        Epic epic = new Epic(2, "name", "description", Status.NEW);
        Subtask subtask = new Subtask(3, "name", "description", Status.NEW, 2);

        taskManager.createTask(task1);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        Assertions.assertNotNull(taskManager.getTaskById(1));
        Assertions.assertNotNull(taskManager.getEpicById(2));
        Assertions.assertNotNull(taskManager.getSubtaskById(3));
    }
}
