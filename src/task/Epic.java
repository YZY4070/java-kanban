package task;

import manager.InMemoryTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List<Integer> subtaskIds;

    private LocalDateTime endTime;

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status, LocalDateTime.MIN, Duration.ZERO);
        this.subtaskIds = new ArrayList<>();
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtaskId(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public void removeSubtaskId(int subtaskId) {
        subtaskIds.remove(Integer.valueOf(subtaskId));
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", subtaskIds=" + subtaskIds +
                '}';
    }

    public void calculateEpicFields(InMemoryTaskManager taskManager) {

        List<Subtask> subtasks = taskManager.getSubtasksByEpicId(this.id);

        if (subtasks.isEmpty()) {
            this.startTime = null;
            this.endTime = null;
            this.duration = Duration.ZERO;
            return;
        }

        this.startTime = subtasks.stream()
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);
        this.endTime = subtasks.stream()
                .map(Subtask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        if (startTime != null && endTime != null) {
            this.duration = Duration.between(startTime, endTime);
        } else {
            this.duration = Duration.ZERO;
        }
    }
}
