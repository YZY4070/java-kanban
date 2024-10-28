package manager;

import task.Epic;
import task.Subtask;
import task.Task;
import java.util.List;

public interface TaskManager {
  List<Task> getAllTasks();

  void deleteAllTasks();

  Task getTaskById(int id);

  void createTask(Task task);

  void updateTask(Task task);

  void deleteTaskById(int id);

  List<Subtask> getAllSubtasks();

  void deleteAllSubtasks();

  Subtask getSubtaskById(int id);

  void createSubtask(Subtask subtask);

  void updateSubtask(Subtask subtask);

  void deleteSubtaskById(int id);

  List<Epic> getAllEpics();

  void deleteAllEpics();

  Epic getEpicById(int id);

  void createEpic(Epic epic);

  void updateEpic(Epic newEpic);

  void deleteEpicById(int id);

  List<Subtask> getSubtasksByEpicId(int epicId);

  List<Task> getHistory();

  List<Task> getPrioritizedTasks();
}
