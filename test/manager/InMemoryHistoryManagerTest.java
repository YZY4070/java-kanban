package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import package_task.Epic;
import package_task.Status;
import package_task.Subtask;
import package_task.Task;

import java.util.ArrayList;
import java.util.List;


public class InMemoryHistoryManagerTest {

    @Test
    public void deleteIfOneInHistoryManager(){

        Task task1 = new Task(1, "task1", "testTask", Status.NEW);
        Task task2 = new Task(2, "task2", "testTask", Status.NEW);
        Task task3 = new Task(3, "task3", "testTask", Status.NEW);
        Task task4 = new Task(4, "task4", "testTask", Status.NEW);
        Task task5 = new Task(5, "task5", "testTask", Status.NEW);
        Task task6 = new Task(6, "task6", "testTask", Status.NEW);

        Epic epicFirst = new Epic(1 , "epicFirst", "testEpic", Status.NEW);
        Epic epicMiddle = new Epic(2, "epicMiddle", "testEpic", Status.NEW);
        Epic epicLast = new Epic(3, "epicLast", "testEpic", Status.NEW);

        Subtask subtask1 = new Subtask(1 , "subtask1", "testSubtask", Status.NEW, epicFirst.getId());
        Subtask subtask2 = new Subtask(2 , "subtask2", "testSubtask", Status.NEW, epicFirst.getId());
        Subtask subtask3 = new Subtask(3 , "subtask3", "testSubtask", Status.NEW, epicMiddle.getId());
        Subtask subtask4 = new Subtask(4 , "subtask4", "testSubtask", Status.NEW, epicMiddle.getId());
        Subtask subtask5 = new Subtask(5 , "subtask5", "testSubtask", Status.NEW, epicLast.getId());
        Subtask subtask6 = new Subtask(6 , "subtask6", "testSubtask", Status.NEW, epicLast.getId());

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
        historyManager.remove(epicFirst.getId());
        Assertions.assertFalse(historyManager.getHistory().contains(epicFirst));

    }

    @Test
    public void deleteItemInTheMiddle(){
        Task task1 = new Task(1, "task1", "testTask", Status.NEW);
        Task task2 = new Task(2, "task2", "testTask", Status.NEW);
        Task task3 = new Task(3, "task3", "testTask", Status.NEW);
        Task task4 = new Task(4, "task4", "testTask", Status.NEW);
        Task task5 = new Task(5, "task5", "testTask", Status.NEW);
        Task task6 = new Task(6, "task6", "testTask", Status.NEW);

        Epic epicFirst = new Epic(1 , "epicFirst", "testEpic", Status.NEW);
        Epic epicMiddle = new Epic(2, "epicMiddle", "testEpic", Status.NEW);
        Epic epicLast = new Epic(3, "epicLast", "testEpic", Status.NEW);

        Subtask subtask1 = new Subtask(1 , "subtask1", "testSubtask", Status.NEW, epicFirst.getId());
        Subtask subtask2 = new Subtask(2 , "subtask2", "testSubtask", Status.NEW, epicFirst.getId());
        Subtask subtask3 = new Subtask(3 , "subtask3", "testSubtask", Status.NEW, epicMiddle.getId());
        Subtask subtask4 = new Subtask(4 , "subtask4", "testSubtask", Status.NEW, epicMiddle.getId());
        Subtask subtask5 = new Subtask(5 , "subtask5", "testSubtask", Status.NEW, epicLast.getId());
        Subtask subtask6 = new Subtask(6 , "subtask6", "testSubtask", Status.NEW, epicLast.getId());

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
        historyManager.remove(epicMiddle.getId());
        historyManager.getHistory();
        Assertions.assertFalse(historyManager.getHistory().contains(epicMiddle));
    }

    @Test
    public void deleteItemLast(){

        Task task1 = new Task(1, "task1", "testTask", Status.NEW);
        Task task2 = new Task(2, "task2", "testTask", Status.NEW);
        Task task3 = new Task(3, "task3", "testTask", Status.NEW);
        Task task4 = new Task(4, "task4", "testTask", Status.NEW);
        Task task5 = new Task(5, "task5", "testTask", Status.NEW);
        Task task6 = new Task(6, "task6", "testTask", Status.NEW);

        Epic epicFirst = new Epic(1 , "epicFirst", "testEpic", Status.NEW);
        Epic epicMiddle = new Epic(2, "epicMiddle", "testEpic", Status.NEW);
        Epic epicLast = new Epic(3, "epicLast", "testEpic", Status.NEW);

        Subtask subtask1 = new Subtask(1 , "subtask1", "testSubtask", Status.NEW, epicFirst.getId());
        Subtask subtask2 = new Subtask(2 , "subtask2", "testSubtask", Status.NEW, epicFirst.getId());
        Subtask subtask3 = new Subtask(3 , "subtask3", "testSubtask", Status.NEW, epicMiddle.getId());
        Subtask subtask4 = new Subtask(4 , "subtask4", "testSubtask", Status.NEW, epicMiddle.getId());
        Subtask subtask5 = new Subtask(5 , "subtask5", "testSubtask", Status.NEW, epicLast.getId());
        Subtask subtask6 = new Subtask(6 , "subtask6", "testSubtask", Status.NEW, epicLast.getId());

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
        historyManager.remove(epicLast.getId());
        Assertions.assertFalse(historyManager.getHistory().contains(epicLast));
    }

    @Test
    public void shouldReturnNullAfterClearingHistory(){
        Task task1 = new Task(1, "task1", "testTask", Status.NEW);
        Task task2 = new Task(2, "task2", "testTask", Status.NEW);
        Task task3 = new Task(3, "task3", "testTask", Status.NEW);
        Task task4 = new Task(4, "task4", "testTask", Status.NEW);
        Task task5 = new Task(5, "task5", "testTask", Status.NEW);
        Task task6 = new Task(6, "task6", "testTask", Status.NEW);

        Epic epicFirst = new Epic(1 , "epicFirst", "testEpic", Status.NEW);
        Epic epicMiddle = new Epic(2, "epicMiddle", "testEpic", Status.NEW);
        Epic epicLast = new Epic(3, "epicLast", "testEpic", Status.NEW);

        Subtask subtask1 = new Subtask(1 , "subtask1", "testSubtask", Status.NEW, epicFirst.getId());
        Subtask subtask2 = new Subtask(2 , "subtask2", "testSubtask", Status.NEW, epicFirst.getId());
        Subtask subtask3 = new Subtask(3 , "subtask3", "testSubtask", Status.NEW, epicMiddle.getId());
        Subtask subtask4 = new Subtask(4 , "subtask4", "testSubtask", Status.NEW, epicMiddle.getId());
        Subtask subtask5 = new Subtask(5 , "subtask5", "testSubtask", Status.NEW, epicLast.getId());
        Subtask subtask6 = new Subtask(6 , "subtask6", "testSubtask", Status.NEW, epicLast.getId());

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
    public void shouldReturnTRUEAfterUseMethodDeleteAllTasks(){
        Task task1 = new Task(1, "task1", "testTask", Status.NEW);
        Task task2 = new Task(2, "task2", "testTask", Status.NEW);
        Task task3 = new Task(3, "task3", "testTask", Status.NEW);
        Task task4 = new Task(4, "task4", "testTask", Status.NEW);
        Task task5 = new Task(5, "task5", "testTask", Status.NEW);
        Task task6 = new Task(6, "task6", "testTask", Status.NEW);

        Epic epicFirst = new Epic(1 , "epicFirst", "testEpic", Status.NEW);
        Epic epicMiddle = new Epic(2, "epicMiddle", "testEpic", Status.NEW);
        Epic epicLast = new Epic(3, "epicLast", "testEpic", Status.NEW);

        Subtask subtask1 = new Subtask(1 , "subtask1", "testSubtask", Status.NEW, epicFirst.getId());
        Subtask subtask2 = new Subtask(2 , "subtask2", "testSubtask", Status.NEW, epicFirst.getId());
        Subtask subtask3 = new Subtask(3 , "subtask3", "testSubtask", Status.NEW, epicMiddle.getId());
        Subtask subtask4 = new Subtask(4 , "subtask4", "testSubtask", Status.NEW, epicMiddle.getId());
        Subtask subtask5 = new Subtask(5 , "subtask5", "testSubtask", Status.NEW, epicLast.getId());
        Subtask subtask6 = new Subtask(6 , "subtask6", "testSubtask", Status.NEW, epicLast.getId());

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
    public void shouldRetturnTrueWhenAllEpicsClear(){
        Task task1 = new Task(1, "task1", "testTask", Status.NEW);
        Task task2 = new Task(2, "task2", "testTask", Status.NEW);
        Task task3 = new Task(3, "task3", "testTask", Status.NEW);
        Task task4 = new Task(4, "task4", "testTask", Status.NEW);
        Task task5 = new Task(5, "task5", "testTask", Status.NEW);
        Task task6 = new Task(6, "task6", "testTask", Status.NEW);

        Epic epicFirst = new Epic(1 , "epicFirst", "testEpic", Status.NEW);
        Epic epicMiddle = new Epic(2, "epicMiddle", "testEpic", Status.NEW);
        Epic epicLast = new Epic(3, "epicLast", "testEpic", Status.NEW);

        Subtask subtask1 = new Subtask(1 , "subtask1", "testSubtask", Status.NEW, epicFirst.getId());
        Subtask subtask2 = new Subtask(2 , "subtask2", "testSubtask", Status.NEW, epicFirst.getId());
        Subtask subtask3 = new Subtask(3 , "subtask3", "testSubtask", Status.NEW, epicMiddle.getId());
        Subtask subtask4 = new Subtask(4 , "subtask4", "testSubtask", Status.NEW, epicMiddle.getId());
        Subtask subtask5 = new Subtask(5 , "subtask5", "testSubtask", Status.NEW, epicLast.getId());
        Subtask subtask6 = new Subtask(6 , "subtask6", "testSubtask", Status.NEW, epicLast.getId());

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
    public void shouldReturnTrueAfterMethodDeleteAllSubtasks(){
        Task task1 = new Task(1, "task1", "testTask", Status.NEW);
        Task task2 = new Task(2, "task2", "testTask", Status.NEW);
        Task task3 = new Task(3, "task3", "testTask", Status.NEW);
        Task task4 = new Task(4, "task4", "testTask", Status.NEW);
        Task task5 = new Task(5, "task5", "testTask", Status.NEW);
        Task task6 = new Task(6, "task6", "testTask", Status.NEW);

        Epic epicFirst = new Epic(1 , "epicFirst", "testEpic", Status.NEW);
        Epic epicMiddle = new Epic(2, "epicMiddle", "testEpic", Status.NEW);
        Epic epicLast = new Epic(3, "epicLast", "testEpic", Status.NEW);

        Subtask subtask1 = new Subtask(1 , "subtask1", "testSubtask", Status.NEW, epicFirst.getId());
        Subtask subtask2 = new Subtask(2 , "subtask2", "testSubtask", Status.NEW, epicFirst.getId());
        Subtask subtask3 = new Subtask(3 , "subtask3", "testSubtask", Status.NEW, epicMiddle.getId());
        Subtask subtask4 = new Subtask(4 , "subtask4", "testSubtask", Status.NEW, epicMiddle.getId());
        Subtask subtask5 = new Subtask(5 , "subtask5", "testSubtask", Status.NEW, epicLast.getId());
        Subtask subtask6 = new Subtask(6 , "subtask6", "testSubtask", Status.NEW, epicLast.getId());

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
