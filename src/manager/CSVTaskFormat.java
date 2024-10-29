package manager;

import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;

public class CSVTaskFormat {

    public static String header() {
        return "id,type,name,status,description,startTime,duration,endTime,epic";
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
                task.getStartTime() + "," +
                task.getDuration().toMinutes() + "," +
                task.getEndTime() + "," +
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
                LocalDateTime startTimeEpic = LocalDateTime.parse(values[5]);
                Duration durationEpic = Duration.ofMinutes(Long.parseLong(values[6]));
                LocalDateTime endTimeEpic = LocalDateTime.parse(values[7]);
                return new Epic(id, name, description, status, startTimeEpic, durationEpic, endTimeEpic);
            case TaskType.SUBTASK:
                final int epicId = Integer.parseInt(values[8]);
                LocalDateTime startTime = LocalDateTime.parse(values[5]);
                Duration duration = Duration.ofMinutes(Long.parseLong(values[6]));
                return new Subtask(id, name, description, status, epicId, startTime, duration);
            case TaskType.TASK:
                LocalDateTime startTimeTask = LocalDateTime.parse(values[5]);
                Duration durationTask = Duration.ofMinutes(Long.parseLong(values[6]));
                return new Task(id, name, description, status, startTimeTask, durationTask);
            default:
                throw new IllegalArgumentException("Неизвестный вид таски: " + type);
        }
    }
}

