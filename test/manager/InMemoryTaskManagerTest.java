package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import package_task.Epic;
import package_task.Status;
import package_task.Task;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    @Test
    public void createTaskShouldExisInTaskManager() {
        Task task = new Task(1, "name", "description", Status.NEW);
        TaskManager taskManager = Managers.getDefault();
        taskManager.createTask(task);
         Assertions.assertEquals(task, taskManager.getTaskById(1));
    }

    @Test
    public void shouldAddAndSearch(){
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task(1, "name", "description", Status.NEW);
        Epic epic = new Epic(2, "name", "description", Status.NEW);

        taskManager.createTask(task1);
        taskManager.createEpic(epic);

        Assertions.assertNotNull(taskManager.getTaskById(1));
        Assertions.assertNotNull(taskManager.getEpicById(2));
    }
}
