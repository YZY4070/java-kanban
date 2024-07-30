package manager;

import package_task.Task;

import java.util.List;

public interface HistoryManager {
    <T extends Task> void add(Task task);
    List<Task> getHistory();
}
