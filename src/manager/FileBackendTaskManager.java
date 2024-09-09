package manager;

import task.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

public class FileBackendTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackendTaskManager(File file) {
        super(Managers.getDefaultHistoryManager());
        this.file = file;
    }

    protected void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.write(CSVTaskFormat.header());
            writer.newLine();

            for (Task task : epics.values()) {
                writer.write(CSVTaskFormat.toString(task));
                writer.newLine();
            }

            for (Task task : subtasks.values()) {
                writer.write(CSVTaskFormat.toString(task));
                writer.newLine();
            }

            for (Task task : tasks.values()) {
                writer.write(CSVTaskFormat.toString(task));
                writer.newLine();
            }


        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении файла" + file.getName());
        }
    }

    public static FileBackendTaskManager loadFromFile(File file) {
        final FileBackendTaskManager taskManager = new FileBackendTaskManager(file);
        int maxId = 0;
        try {
            final String csv = Files.readString(file.toPath());
            final String[] lines = csv.split(System.lineSeparator());
            for (String line : lines) {
                if (!line.equals(CSVTaskFormat.header())) {
                    Task task = CSVTaskFormat.taskFromString(line);
                    if (task.getId() > maxId) {
                        maxId = task.getId();
                    }
                    if (task instanceof Epic) {
                        taskManager.epics.put(task.getId(), (Epic) task);
                    } else if (task instanceof Subtask) {
                        taskManager.subtasks.put(task.getId(), (Subtask) task);
                        Epic epic = taskManager.epics.get(((Subtask) task).getEpicId());
                        if (epic != null) {
                            epic.addSubtaskId(task.getId());
                        }
                    } else {
                        taskManager.tasks.put(task.getId(), task);
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении файла" + e.getMessage());
        }
        taskManager.setNextId(maxId + 1);
        return taskManager;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }
}
