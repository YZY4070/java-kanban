package manager;

import task.*;

public class CSVTaskFormat {

    public enum TaskType {
        EPIC,
        TASK,
        SUBTASK
    }

    public static String header() {
        return "id,type,name,status,description,epic";
    }

    public static String toString(Task task) {
        TaskType type = TaskType.TASK;
        String epicField = "";

        if (task instanceof Epic) {
            type = TaskType.EPIC;
        } else if (task instanceof Subtask) {
            type = TaskType.SUBTASK;
            Subtask subtask = (Subtask) task;
            epicField = String.valueOf(subtask.getEpicId());
        }

        return task.getId() + "," +
                type + "," +
                task.getName() + "," +
                task.getStatus() + "," +
                task.getDescription() + "," +
                epicField;
    }

    public static Task taskFromString(String value) {
        final String[] values = value.split(",");
        final int id = Integer.parseInt(values[0]);
        final TaskType type = TaskType.valueOf(values[1]);
        final String name = values[2];
        final Status status = Status.valueOf(values[3]);
        final String description = values[4];

        switch (type) {
            case TaskType.EPIC:
                return new Epic(id, name, description, status);
            case TaskType.SUBTASK:
                final int epicId = Integer.parseInt(values[5]);
                return new Subtask(id, name, description, status, epicId);
            case TaskType.TASK:
                return new Task(id, name, description, status);
            default:
                throw new IllegalArgumentException("Неизвестный вид таски: " + type);
        }
    }
}

