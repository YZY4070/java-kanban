package package_task;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    TaskManager taskManager = Managers.getDefault();

    @Test
    public void checkTask(){
        Task task = new Task(1, "name", "description", Status.NEW);
        taskManager.createTask(task);
        Task copyTask = taskManager.getTaskById(1);

        Assertions.assertEquals(task, copyTask);
    }
}