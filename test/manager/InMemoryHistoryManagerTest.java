package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class InMemoryHistoryManagerTest {

    @Test
    public void deleteIfOneInHistoryManager() {

        Task task1 = new Task(1, "task1", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 1, 9, 0), Duration.ofMinutes(40));
        Task task2 = new Task(2, "task2", "testTask", Status.NEW, LocalDateTime.of(2020, 1,2, 9, 0  ), Duration.ofMinutes(10));
        Task task3 = new Task(3, "task3", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 3, 9, 0), Duration.ofMinutes(30));
        Task task4 = new Task(4, "task4", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 4, 9, 0 ), Duration.ofMinutes(52));
        Task task5 = new Task(5, "task5", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 5, 9, 0), Duration.ofMinutes(3));
        Task task6 = new Task(6, "task6", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 6, 9, 0), Duration.ofMinutes(53));

        Epic epicFirst = new Epic(1, "epicFirst", "testEpic", Status.NEW);
        Epic epicMiddle = new Epic(2, "epicMiddle", "testEpic", Status.NEW);
        Epic epicLast = new Epic(3, "epicLast", "testEpic", Status.NEW);

        Subtask subtask1 = new Subtask(1, "subtask1", "testSubtask", Status.NEW, epicFirst.getId(), LocalDateTime.of(2020, 1, 1, 9, 0), Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask(2, "subtask2", "testSubtask", Status.NEW, epicFirst.getId(), LocalDateTime.of(2020, 1, 1, 10, 0), Duration.ofMinutes(40));
        Subtask subtask3 = new Subtask(3, "subtask3", "testSubtask", Status.NEW, epicMiddle.getId(), LocalDateTime.of(2020, 1, 1, 11, 0), Duration.ofMinutes(30));
        Subtask subtask4 = new Subtask(4, "subtask4", "testSubtask", Status.NEW, epicMiddle.getId(), LocalDateTime.of(2020, 1, 1, 12, 0), Duration.ofMinutes(50));
        Subtask subtask5 = new Subtask(5, "subtask5", "testSubtask", Status.NEW, epicLast.getId(), LocalDateTime.of(2020, 1, 1, 13, 0), Duration.ofMinutes(30));
        Subtask subtask6 = new Subtask(6, "subtask6", "testSubtask", Status.NEW, epicLast.getId(), LocalDateTime.of(2020, 1, 1 , 14, 0), Duration.ofMinutes(49));

        HistoryManager historyManager = Managers.getDefaultHistoryManager();
        TaskManager taskManager = new InMemoryTaskManager(historyManager);

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createTask(task4);
        taskManager.createTask(task5);
        taskManager.createTask(task6);

        taskManager.createEpic(epicFirst);
        taskManager.createEpic(epicMiddle);
        taskManager.createEpic(epicLast);


        historyManager.add(epicFirst);
        historyManager.add(epicMiddle);
        historyManager.add(epicLast);

        //Сам тест
        taskManager.deleteEpicById(epicFirst.getId());
        Assertions.assertFalse(historyManager.getHistory().contains(epicFirst));

    }

    @Test
    public void deleteItemInTheMiddle() {
        Task task1 = new Task(1, "task1", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 1, 9, 0), Duration.ofMinutes(40));
        Task task2 = new Task(2, "task2", "testTask", Status.NEW, LocalDateTime.of(2020, 1,2, 9, 0  ), Duration.ofMinutes(10));
        Task task3 = new Task(3, "task3", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 3, 9, 0), Duration.ofMinutes(30));
        Task task4 = new Task(4, "task4", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 4, 9, 0 ), Duration.ofMinutes(52));
        Task task5 = new Task(5, "task5", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 5, 9, 0), Duration.ofMinutes(3));
        Task task6 = new Task(6, "task6", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 6, 9, 0), Duration.ofMinutes(53));

        Epic epicFirst = new Epic(1, "epicFirst", "testEpic", Status.NEW);
        Epic epicMiddle = new Epic(2, "epicMiddle", "testEpic", Status.NEW);
        Epic epicLast = new Epic(3, "epicLast", "testEpic", Status.NEW);

        Subtask subtask1 = new Subtask(1, "subtask1", "testSubtask", Status.NEW, epicFirst.getId(), LocalDateTime.of(2020, 1, 1, 9, 0), Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask(2, "subtask2", "testSubtask", Status.NEW, epicFirst.getId(), LocalDateTime.of(2020, 1, 1, 10, 0), Duration.ofMinutes(40));
        Subtask subtask3 = new Subtask(3, "subtask3", "testSubtask", Status.NEW, epicMiddle.getId(), LocalDateTime.of(2020, 1, 1, 11, 0), Duration.ofMinutes(30));
        Subtask subtask4 = new Subtask(4, "subtask4", "testSubtask", Status.NEW, epicMiddle.getId(), LocalDateTime.of(2020, 1, 1, 12, 0), Duration.ofMinutes(50));
        Subtask subtask5 = new Subtask(5, "subtask5", "testSubtask", Status.NEW, epicLast.getId(), LocalDateTime.of(2020, 1, 1, 13, 0), Duration.ofMinutes(30));
        Subtask subtask6 = new Subtask(6, "subtask6", "testSubtask", Status.NEW, epicLast.getId(), LocalDateTime.of(2020, 1, 1 , 14, 0), Duration.ofMinutes(49));

        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        InMemoryTaskManager taskManager = new InMemoryTaskManager(historyManager);

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createTask(task4);
        taskManager.createTask(task5);
        taskManager.createTask(task6);

        taskManager.createEpic(epicFirst);
        taskManager.createEpic(epicMiddle);
        taskManager.createEpic(epicLast);


        historyManager.add(epicFirst);
        historyManager.add(epicMiddle);
        historyManager.add(epicLast);

        //Сам тест
        taskManager.deleteEpicById(epicMiddle.getId());
        Assertions.assertFalse(historyManager.getHistory().contains(epicMiddle));
    }

    @Test
    public void deleteItemLast() {

        Task task1 = new Task(1, "task1", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 1, 9, 0), Duration.ofMinutes(40));
        Task task2 = new Task(2, "task2", "testTask", Status.NEW, LocalDateTime.of(2020, 1,2, 9, 0  ), Duration.ofMinutes(10));
        Task task3 = new Task(3, "task3", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 3, 9, 0), Duration.ofMinutes(30));
        Task task4 = new Task(4, "task4", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 4, 9, 0 ), Duration.ofMinutes(52));
        Task task5 = new Task(5, "task5", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 5, 9, 0), Duration.ofMinutes(3));
        Task task6 = new Task(6, "task6", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 6, 9, 0), Duration.ofMinutes(53));

        Epic epicFirst = new Epic(1, "epicFirst", "testEpic", Status.NEW);
        Epic epicMiddle = new Epic(2, "epicMiddle", "testEpic", Status.NEW);
        Epic epicLast = new Epic(3, "epicLast", "testEpic", Status.NEW);

        Subtask subtask1 = new Subtask(1, "subtask1", "testSubtask", Status.NEW, epicFirst.getId(), LocalDateTime.of(2020, 1, 1, 9, 0), Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask(2, "subtask2", "testSubtask", Status.NEW, epicFirst.getId(), LocalDateTime.of(2020, 1, 1, 10, 0), Duration.ofMinutes(40));
        Subtask subtask3 = new Subtask(3, "subtask3", "testSubtask", Status.NEW, epicMiddle.getId(), LocalDateTime.of(2020, 1, 1, 11, 0), Duration.ofMinutes(30));
        Subtask subtask4 = new Subtask(4, "subtask4", "testSubtask", Status.NEW, epicMiddle.getId(), LocalDateTime.of(2020, 1, 1, 12, 0), Duration.ofMinutes(50));
        Subtask subtask5 = new Subtask(5, "subtask5", "testSubtask", Status.NEW, epicLast.getId(), LocalDateTime.of(2020, 1, 1, 13, 0), Duration.ofMinutes(30));
        Subtask subtask6 = new Subtask(6, "subtask6", "testSubtask", Status.NEW, epicLast.getId(), LocalDateTime.of(2020, 1, 1 , 14, 0), Duration.ofMinutes(49));

        HistoryManager historyManager = Managers.getDefaultHistoryManager();
        TaskManager taskManager = new InMemoryTaskManager(historyManager);

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createTask(task4);
        taskManager.createTask(task5);
        taskManager.createTask(task6);

        taskManager.createEpic(epicFirst);
        taskManager.createEpic(epicMiddle);
        taskManager.createEpic(epicLast);

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        taskManager.createSubtask(subtask4);
        taskManager.createSubtask(subtask5);
        taskManager.createSubtask(subtask6);


        historyManager.add(epicFirst);
        historyManager.add(epicMiddle);
        historyManager.add(epicLast);

        //Сам тест
        taskManager.deleteEpicById(epicLast.getId());
        Assertions.assertFalse(historyManager.getHistory().contains(epicLast));
    }

    @Test
    public void shouldReturnNullAfterClearingHistory() {
        Task task1 = new Task(1, "task1", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 1, 9, 0), Duration.ofMinutes(40));
        Task task2 = new Task(2, "task2", "testTask", Status.NEW, LocalDateTime.of(2020, 1,2, 9, 0  ), Duration.ofMinutes(10));
        Task task3 = new Task(3, "task3", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 3, 9, 0), Duration.ofMinutes(30));
        Task task4 = new Task(4, "task4", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 4, 9, 0 ), Duration.ofMinutes(52));
        Task task5 = new Task(5, "task5", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 5, 9, 0), Duration.ofMinutes(3));
        Task task6 = new Task(6, "task6", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 6, 9, 0), Duration.ofMinutes(53));

        Epic epicFirst = new Epic(1, "epicFirst", "testEpic", Status.NEW);
        Epic epicMiddle = new Epic(2, "epicMiddle", "testEpic", Status.NEW);
        Epic epicLast = new Epic(3, "epicLast", "testEpic", Status.NEW);

        Subtask subtask1 = new Subtask(1, "subtask1", "testSubtask", Status.NEW, epicFirst.getId(), LocalDateTime.of(2020, 1, 1, 9, 0), Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask(2, "subtask2", "testSubtask", Status.NEW, epicFirst.getId(), LocalDateTime.of(2020, 1, 1, 10, 0), Duration.ofMinutes(40));
        Subtask subtask3 = new Subtask(3, "subtask3", "testSubtask", Status.NEW, epicMiddle.getId(), LocalDateTime.of(2020, 1, 1, 11, 0), Duration.ofMinutes(30));
        Subtask subtask4 = new Subtask(4, "subtask4", "testSubtask", Status.NEW, epicMiddle.getId(), LocalDateTime.of(2020, 1, 1, 12, 0), Duration.ofMinutes(50));
        Subtask subtask5 = new Subtask(5, "subtask5", "testSubtask", Status.NEW, epicLast.getId(), LocalDateTime.of(2020, 1, 1, 13, 0), Duration.ofMinutes(30));
        Subtask subtask6 = new Subtask(6, "subtask6", "testSubtask", Status.NEW, epicLast.getId(), LocalDateTime.of(2020, 1, 1 , 14, 0), Duration.ofMinutes(49));

        HistoryManager historyManager = Managers.getDefaultHistoryManager();
        TaskManager taskManager = new InMemoryTaskManager(historyManager);

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createTask(task4);
        taskManager.createTask(task5);
        taskManager.createTask(task6);

        taskManager.createEpic(epicFirst);
        taskManager.createEpic(epicMiddle);
        taskManager.createEpic(epicLast);


        historyManager.add(epicFirst);

        Assertions.assertNotNull(historyManager.getHistory());
        historyManager.remove(epicFirst.getId());
        Assertions.assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    public void shouldReturnTRUEAfterUseMethodDeleteAllTasks() {
        Task task1 = new Task(1, "task1", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 1, 9, 0), Duration.ofMinutes(40));
        Task task2 = new Task(2, "task2", "testTask", Status.NEW, LocalDateTime.of(2020, 1,2, 9, 0  ), Duration.ofMinutes(10));
        Task task3 = new Task(3, "task3", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 3, 9, 0), Duration.ofMinutes(30));
        Task task4 = new Task(4, "task4", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 4, 9, 0 ), Duration.ofMinutes(52));
        Task task5 = new Task(5, "task5", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 5, 9, 0), Duration.ofMinutes(3));
        Task task6 = new Task(6, "task6", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 6, 9, 0), Duration.ofMinutes(53));

        Epic epicFirst = new Epic(1, "epicFirst", "testEpic", Status.NEW);
        Epic epicMiddle = new Epic(2, "epicMiddle", "testEpic", Status.NEW);
        Epic epicLast = new Epic(3, "epicLast", "testEpic", Status.NEW);

        Subtask subtask1 = new Subtask(1, "subtask1", "testSubtask", Status.NEW, epicFirst.getId(), LocalDateTime.of(2020, 1, 1, 9, 0), Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask(2, "subtask2", "testSubtask", Status.NEW, epicFirst.getId(), LocalDateTime.of(2020, 1, 1, 10, 0), Duration.ofMinutes(40));
        Subtask subtask3 = new Subtask(3, "subtask3", "testSubtask", Status.NEW, epicMiddle.getId(), LocalDateTime.of(2020, 1, 1, 11, 0), Duration.ofMinutes(30));
        Subtask subtask4 = new Subtask(4, "subtask4", "testSubtask", Status.NEW, epicMiddle.getId(), LocalDateTime.of(2020, 1, 1, 12, 0), Duration.ofMinutes(50));
        Subtask subtask5 = new Subtask(5, "subtask5", "testSubtask", Status.NEW, epicLast.getId(), LocalDateTime.of(2020, 1, 1, 13, 0), Duration.ofMinutes(30));
        Subtask subtask6 = new Subtask(6, "subtask6", "testSubtask", Status.NEW, epicLast.getId(), LocalDateTime.of(2020, 1, 1 , 14, 0), Duration.ofMinutes(49));

        HistoryManager historyManager = Managers.getDefaultHistoryManager();
        TaskManager taskManager = new InMemoryTaskManager(historyManager);

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createTask(task4);
        taskManager.createTask(task5);
        taskManager.createTask(task6);

        taskManager.createEpic(epicFirst);
        taskManager.createEpic(epicMiddle);
        taskManager.createEpic(epicLast);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);
        historyManager.add(task5);
        historyManager.add(task6);

        taskManager.deleteAllTasks();
        Assertions.assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    public void shouldRetturnTrueWhenAllEpicsClear() {
        Task task1 = new Task(1, "task1", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 1, 9, 0), Duration.ofMinutes(40));
        Task task2 = new Task(2, "task2", "testTask", Status.NEW, LocalDateTime.of(2020, 1,2, 9, 0  ), Duration.ofMinutes(10));
        Task task3 = new Task(3, "task3", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 3, 9, 0), Duration.ofMinutes(30));
        Task task4 = new Task(4, "task4", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 4, 9, 0 ), Duration.ofMinutes(52));
        Task task5 = new Task(5, "task5", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 5, 9, 0), Duration.ofMinutes(3));
        Task task6 = new Task(6, "task6", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 6, 9, 0), Duration.ofMinutes(53));

        Epic epicFirst = new Epic(1, "epicFirst", "testEpic", Status.NEW);
        Epic epicMiddle = new Epic(2, "epicMiddle", "testEpic", Status.NEW);
        Epic epicLast = new Epic(3, "epicLast", "testEpic", Status.NEW);

        Subtask subtask1 = new Subtask(1, "subtask1", "testSubtask", Status.NEW, epicFirst.getId(), LocalDateTime.of(2020, 1, 1, 9, 0), Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask(2, "subtask2", "testSubtask", Status.NEW, epicFirst.getId(), LocalDateTime.of(2020, 1, 1, 10, 0), Duration.ofMinutes(40));
        Subtask subtask3 = new Subtask(3, "subtask3", "testSubtask", Status.NEW, epicMiddle.getId(), LocalDateTime.of(2020, 1, 1, 11, 0), Duration.ofMinutes(30));
        Subtask subtask4 = new Subtask(4, "subtask4", "testSubtask", Status.NEW, epicMiddle.getId(), LocalDateTime.of(2020, 1, 1, 12, 0), Duration.ofMinutes(50));
        Subtask subtask5 = new Subtask(5, "subtask5", "testSubtask", Status.NEW, epicLast.getId(), LocalDateTime.of(2020, 1, 1, 13, 0), Duration.ofMinutes(30));
        Subtask subtask6 = new Subtask(6, "subtask6", "testSubtask", Status.NEW, epicLast.getId(), LocalDateTime.of(2020, 1, 1 , 14, 0), Duration.ofMinutes(49));

        HistoryManager historyManager = Managers.getDefaultHistoryManager();
        TaskManager taskManager = new InMemoryTaskManager(historyManager);

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createTask(task4);
        taskManager.createTask(task5);
        taskManager.createTask(task6);

        taskManager.createEpic(epicFirst);
        taskManager.createEpic(epicMiddle);
        taskManager.createEpic(epicLast);


        historyManager.add(epicFirst);
        taskManager.deleteAllEpics();

        Assertions.assertTrue(historyManager.getHistory().isEmpty());

    }

    @Test
    public void shouldReturnTrueAfterMethodDeleteAllSubtasks() {

        Task task1 = new Task(1, "task1", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 1, 9, 0), Duration.ofMinutes(40));
        Task task2 = new Task(2, "task2", "testTask", Status.NEW, LocalDateTime.of(2020, 1,2, 9, 0  ), Duration.ofMinutes(10));
        Task task3 = new Task(3, "task3", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 3, 9, 0), Duration.ofMinutes(30));
        Task task4 = new Task(4, "task4", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 4, 9, 0 ), Duration.ofMinutes(52));
        Task task5 = new Task(5, "task5", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 5, 9, 0), Duration.ofMinutes(3));
        Task task6 = new Task(6, "task6", "testTask", Status.NEW, LocalDateTime.of(2020, 1, 6, 9, 0), Duration.ofMinutes(53));

        Epic epicFirst = new Epic(1, "epicFirst", "testEpic", Status.NEW);
        Epic epicMiddle = new Epic(2, "epicMiddle", "testEpic", Status.NEW);
        Epic epicLast = new Epic(3, "epicLast", "testEpic", Status.NEW);

        Subtask subtask1 = new Subtask(1, "subtask1", "testSubtask", Status.NEW, epicFirst.getId(), LocalDateTime.of(2020, 1, 1, 9, 0), Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask(2, "subtask2", "testSubtask", Status.NEW, epicFirst.getId(), LocalDateTime.of(2020, 1, 1, 10, 0), Duration.ofMinutes(40));
        Subtask subtask3 = new Subtask(3, "subtask3", "testSubtask", Status.NEW, epicMiddle.getId(), LocalDateTime.of(2020, 1, 1, 11, 0), Duration.ofMinutes(30));
        Subtask subtask4 = new Subtask(4, "subtask4", "testSubtask", Status.NEW, epicMiddle.getId(), LocalDateTime.of(2020, 1, 1, 12, 0), Duration.ofMinutes(50));
        Subtask subtask5 = new Subtask(5, "subtask5", "testSubtask", Status.NEW, epicLast.getId(), LocalDateTime.of(2020, 1, 1, 13, 0), Duration.ofMinutes(30));
        Subtask subtask6 = new Subtask(6, "subtask6", "testSubtask", Status.NEW, epicLast.getId(), LocalDateTime.of(2020, 1, 1 , 14, 0), Duration.ofMinutes(49));

        HistoryManager historyManager = Managers.getDefaultHistoryManager();
        TaskManager taskManager = new InMemoryTaskManager(historyManager);

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createTask(task4);
        taskManager.createTask(task5);
        taskManager.createTask(task6);

        taskManager.createEpic(epicFirst);
        taskManager.createEpic(epicMiddle);
        taskManager.createEpic(epicLast);


        historyManager.add(epicFirst);
        taskManager.deleteAllSubtasks();
        List<Integer> subtaskIds = new ArrayList<>(epicFirst.getSubtaskIds());
        Assertions.assertTrue(subtaskIds.isEmpty());

    }
}
