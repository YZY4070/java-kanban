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
        System.out.println(manager.getPrioritizedTasks());
    }
}
