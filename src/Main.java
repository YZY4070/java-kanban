import task.*;
import manager.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager(Managers.getDefaultHistoryManager());

        Task task1 = new Task(0, "Задача 1", "Описание 1", Status.NEW, LocalDateTime.of(2021, 3, 6, 11, 30), Duration.ofMinutes(30));
        manager.createTask(task1);
        System.out.println("Создана задача: " + task1.getId());

        Task task2 = new Task(1, "Task2", "desc2", Status.NEW, LocalDateTime.of(2021, 3, 6, 12, 10), Duration.ofMinutes(54));
        manager.createTask(task2);

        Task task3 = new Task(2, "Task3", "desc", Status.NEW, LocalDateTime.of(2021, 3, 6, 8, 00), Duration.ofMinutes(52));
        manager.createTask(task3);

        Epic epic1 = new Epic(0, "Эпик 1", "Описание Эпика 1", Status.NEW);
        manager.createEpic(epic1);
        System.out.println("Создан эпик: " + epic1.getId());

        Subtask subtask1 = new Subtask(0, "Подзадача 1", "Описание Подзадачи 1", Status.NEW, epic1.getId(), LocalDateTime.of(2022, 3, 4, 13, 40), Duration.ofMinutes(20));
        manager.createSubtask(subtask1);
        System.out.println("Создана подзадача: " + subtask1.getId());

        Subtask subtask2 = new Subtask(0, "Подзадача 2", "Описание Подзадачи 2", Status.IN_PROGRESS, epic1.getId(), LocalDateTime.of(2022, 3, 5, 10, 10), Duration.ofMinutes(20));
        manager.createSubtask(subtask2);
        System.out.println("Создана подзадача: " + subtask2.getId());

        System.out.println("Все задачи: " + manager.getAllTasks());

        System.out.println("Все эпики: " + manager.getAllEpics());

        System.out.println("Подзадачи Эпика 1: " + manager.getSubtasksByEpicId(epic1.getId()));

        subtask1.setStatus(Status.DONE);
        manager.updateSubtask(subtask1);
        System.out.println("Обновленный статус Подзадачи 1: " + subtask1.getStatus());
        System.out.println("Обновленный статус Эпика 1: " + epic1.getStatus());

        manager.deleteTaskById(task1.getId());
        System.out.println("Все задачи после удаления: " + manager.getAllTasks());

        System.out.println(CSVTaskFormat.toString(task1));
        System.out.println(manager.getPrioritizedTasks());

    }
}
