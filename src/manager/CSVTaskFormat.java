package manager;

import task.*;

import java.util.List;

public class CSVTaskFormat {

    public static String header() {
        return "id,type,name,status,description,epic";
    }

    public static String toString(Task task) {
        String type = "";
        String epicField = "";

        if (task instanceof Epic) {
            type = "Epic";
        } else if (task instanceof Subtask) {
            type = "Subtask";
            Subtask subtask = (Subtask) task;
            epicField = String.valueOf(subtask.getEpicId());
        } else {
            type = "Task";
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
        final String type = values[1];
        final String name = values[2];
        final Status status = Status.valueOf(values[3]);
        final String description = values[4];

        switch (type) {
            case "Epic":
                return new Epic(id, name, description, status);
            case "Subtask":
                final int epicId = Integer.parseInt(values[5]);
                return new Subtask(id, name, description, status, epicId);
            case "Task":
                return new Task(id, name, description, status);
            default:
                throw new IllegalArgumentException("Неизвестный вид таски: " + type);
        }
    }
}

