package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    protected abstract T createTaskManager();

    @BeforeEach
    public void setUp() {
        taskManager = createTaskManager();
    }

    // Тест создания задачи
    @Test
    public void testCreateTask() {
        Task task = new Task(1, "task1", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 1, 9, 0), Duration.ofMinutes(40));
        taskManager.createTask(task);
        assertNotNull(taskManager.getTaskById(task.getId()), "Задача должна быть создана и доступна для получения");
    }

    // Тест обновления задачи
    @Test
    public void testUpdateTask() {
        Task task = new Task(1, "task1", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 1, 9, 0), Duration.ofMinutes(40));
        taskManager.createTask(task);

        task.setName("Обновленная задача");
        task.setDescription("Обновленное описание");
        taskManager.updateTask(task);

        Task updatedTask = taskManager.getTaskById(task.getId());
        assertEquals("Обновленная задача", updatedTask.getName(), "Имя задачи должно обновиться");
        assertEquals("Обновленное описание", updatedTask.getDescription(), "Описание задачи должно обновиться");
    }

    // Тест удаления задачи
    @Test
    public void testDeleteTask() {
        Task task = new Task(1, "task1", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 1, 9, 0), Duration.ofMinutes(40));
        taskManager.createTask(task);
        taskManager.deleteTaskById(task.getId());

        assertNull(taskManager.getTaskById(task.getId()), "Задача должна быть удалена");
    }

    // Тест удаления всех задач
    @Test
    public void testDeleteAllTasks() {
        Task task1 = new Task(1, "task1", "testTask1", Status.NEW, LocalDateTime.of(2020, 1, 1, 9, 0), Duration.ofMinutes(40));
        Task task2 = new Task(2, "task2", "testTask2", Status.NEW, LocalDateTime.of(2020, 1, 2, 9, 0), Duration.ofMinutes(40));
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        taskManager.deleteAllTasks();
        assertTrue(taskManager.getAllTasks().isEmpty(), "Все задачи должны быть удалены");
    }

    // Тест получения задачи по ID
    @Test
    public void testGetTaskById() {
        Task task = new Task(1, "task1", "testTask1", Status.NEW, LocalDateTime.of(2020, 1, 1, 9, 0), Duration.ofMinutes(40));
        taskManager.createTask(task);

        Task fetchedTask = taskManager.getTaskById(task.getId());
        assertEquals(task, fetchedTask, "Задача должна быть доступна по ID");
    }

    // Тест получения всех задач
    @Test
    public void testGetAllTasks() {
        Task task1 = new Task(1, "task1", "testTask1", Status.NEW, LocalDateTime.of(2020, 1, 1, 9, 0), Duration.ofMinutes(40));
        Task task2 = new Task(2, "task2", "testTask2", Status.NEW, LocalDateTime.of(2020, 1, 2, 9, 0), Duration.ofMinutes(40));
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        List<Task> tasks = taskManager.getAllTasks();
        assertTrue(tasks.contains(task1) && tasks.contains(task2), "Все созданные задачи должны быть доступны для получения");
    }

    // Тест создания эпика
    @Test
    public void testCreateEpic() {
        Epic epic = new Epic(1, "Epic", "Описание", Status.NEW);
        taskManager.createEpic(epic);
        assertNotNull(taskManager.getEpicById(epic.getId()), "Эпик должен быть создан");
    }

    // Тест обновления эпика
    @Test
    public void testUpdateEpic() {
        Epic epic = new Epic(1, "Epic", "Описание эпика", Status.NEW);
        taskManager.createEpic(epic);

        epic.setName("Обновленный эпик");
        taskManager.updateEpic(epic);

        Epic updatedEpic = taskManager.getEpicById(epic.getId());
        assertEquals("Обновленный эпик", updatedEpic.getName(), "Имя эпика должно быть обновлено");
    }

    // Тест удаления эпика
    @Test
    public void testDeleteEpic() {
        Epic epic = new Epic(1, "Epic", "Описание", Status.NEW);
        taskManager.createEpic(epic);
        taskManager.deleteEpicById(epic.getId());

        assertNull(taskManager.getEpicById(epic.getId()), "Эпик должен быть удален");
    }

    // Тест удаления всех эпиков
    @Test
    public void testDeleteAllEpics() {
        Epic epic1 = new Epic(1, "Epic 1", "Описание эпика 1", Status.NEW);
        Epic epic2 = new Epic(2, "Epic 2", "Описание эпика 2", Status.NEW);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        taskManager.deleteAllEpics();
        assertTrue(taskManager.getAllEpics().isEmpty(), "Все эпики должны быть удалены");
    }

    // Тест получения эпика по ID
    @Test
    public void testGetEpicById() {
        Epic epic = new Epic(1, "Epic", "Описание эпика", Status.NEW);
        taskManager.createEpic(epic);

        Epic fetchedEpic = taskManager.getEpicById(epic.getId());
        assertEquals(epic, fetchedEpic, "Эпик должен быть доступен по ID");
    }

    // Тест получения всех эпиков
    @Test
    public void testGetAllEpics() {
        Epic epic1 = new Epic(1, "Epic 1", "Описание эпика 1", Status.NEW);
        Epic epic2 = new Epic(2, "Epic 2", "Описание эпика 2", Status.NEW);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        List<Epic> epics = taskManager.getAllEpics();
        assertTrue(epics.contains(epic1) && epics.contains(epic2), "Все созданные эпики должны быть доступны для получения");
    }

    // Тест создания сабтаска
    @Test
    public void testCreateSubtask() {
        Epic epic = new Epic(1, "Epic", "Description Epic", Status.NEW);
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask(2, "Subtask", "SubtaskDescription", Status.NEW, epic.getId(), LocalDateTime.of(2022, 3, 4, 10, 30), Duration.ofMinutes(30));
        taskManager.createSubtask(subtask);

        assertNotNull(taskManager.getSubtaskById(subtask.getId()), "Сабтаск должен быть создан и доступен для получения");
        assertTrue(taskManager.getSubtasksByEpicId(epic.getId()).contains(subtask), "Сабтаск должен быть привязан к эпику");
    }

    // Тест обновления сабтаска
    @Test
    public void testUpdateSubtask() {
        Epic epic = new Epic(1, "Epic", "Описание эпика", Status.NEW);
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask(2, "Subtask", "Описание сабтаска", Status.NEW, epic.getId(), LocalDateTime.of(2020, 1, 2, 9, 0), Duration.ofMinutes(30));
        taskManager.createSubtask(subtask);

        subtask.setName("Обновленный сабтаск");
        taskManager.updateSubtask(subtask);

        Subtask updatedSubtask = taskManager.getSubtaskById(subtask.getId());
        assertEquals("Обновленный сабтаск", updatedSubtask.getName(), "Имя сабтаска должно быть обновлено");
    }

    // Тест удаления сабтаска
    @Test
    public void testDeleteSubtask() {
        Epic epic = new Epic(1, "Epic", "Описание", Status.NEW);
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask(2, "Subtask", "SubtaskDescription", Status.NEW, epic.getId(), LocalDateTime.of(2022, 3, 4, 10, 30), Duration.ofMinutes(30));
        taskManager.createSubtask(subtask);

        taskManager.deleteSubtaskById(subtask.getId());
        assertNull(taskManager.getSubtaskById(subtask.getId()), "Сабтаск должен быть удален");
        assertFalse(taskManager.getSubtasksByEpicId(epic.getId()).contains(subtask), "Сабтаск должен быть удален из эпика");
    }

    // Тест удаления всех сабтасков
    @Test
    public void testDeleteAllSubtasks() {
        Epic epic = new Epic(1, "Epic", "Описание эпика", Status.NEW);
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask(2, "Subtask 1", "Описание", Status.NEW, epic.getId(), LocalDateTime.of(2020, 1, 2, 9, 0), Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask(3, "Subtask 2", "Описание", Status.NEW, epic.getId(), LocalDateTime.of(2020, 1, 3, 10, 0), Duration.ofMinutes(45));
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        taskManager.deleteAllSubtasks();
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Все сабтаски должны быть удалены");
    }

    // Тест получения сабтаска по ID
    @Test
    public void testGetSubtaskById() {
        Epic epic = new Epic(1, "Epic", "Описание эпика", Status.NEW);
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask(2, "Subtask", "Описание сабтаска", Status.NEW, epic.getId(), LocalDateTime.of(2020, 1, 2, 9, 0), Duration.ofMinutes(30));
        taskManager.createSubtask(subtask);

        Subtask fetchedSubtask = taskManager.getSubtaskById(subtask.getId());
        assertEquals(subtask, fetchedSubtask, "Сабтаск должен быть доступен по ID");
    }

    // Тест получения всех сабтасков
    @Test
    public void testGetAllSubtasks() {
        Epic epic = new Epic(1, "Epic", "Описание эпика", Status.NEW);
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask(2, "Subtask 1", "Описание", Status.NEW, epic.getId(), LocalDateTime.of(2020, 1, 2, 9, 0), Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask(3, "Subtask 2", "Описание", Status.NEW, epic.getId(), LocalDateTime.of(2020, 1, 3, 10, 0), Duration.ofMinutes(45));
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        List<Subtask> subtasks = taskManager.getAllSubtasks();
        assertTrue(subtasks.contains(subtask1) && subtasks.contains(subtask2), "Все сабтаски должны быть доступны для получения");
    }

    // Тест получения всех сабтасков по ID эпика
    @Test
    public void testGetSubtasksByEpicId() {
        Epic epic = new Epic(1, "Epic", "Описание эпика", Status.NEW);
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask(2, "Subtask 1", "Описание сабтаска 1", Status.NEW, epic.getId(), LocalDateTime.of(2020, 1, 2, 9, 0), Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask(3, "Subtask 2", "Описание сабтаска 2", Status.NEW, epic.getId(), LocalDateTime.of(2020, 1, 3, 10, 0), Duration.ofMinutes(45));
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        List<Subtask> subtasks = taskManager.getSubtasksByEpicId(epic.getId());
        assertTrue(subtasks.contains(subtask1) && subtasks.contains(subtask2), "Сабтаски должны быть доступны для получения по ID эпика");
    }
}
