package task;

import manager.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {

    private TaskManager taskManager;
    private Epic epic;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager(Managers.getDefaultHistoryManager());
        epic = new Epic(1,"Epic 1", "Epic description", Status.NEW);
        taskManager.createEpic(epic);
    }

    @Test
    void shouldReturnNewWhenAllSubtasksAreNew() {
        Subtask subtask1 = new Subtask(1, "Subtask 1", "Subtask description",Status.NEW, epic.getId(), LocalDateTime.of(2022, 3, 6, 13, 30), Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask(2, "Subtask 2", "Subtask description", Status.NEW, epic.getId(), LocalDateTime.of(2022, 3, 7, 13, 30), Duration.ofMinutes(30));
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertEquals(Status.NEW, epic.getStatus(), "Эпик должен иметь статус NEW, если все подзадачи имеют статус NEW.");
    }

    @Test
    void shouldReturnDoneWhenAllSubtasksAreDone() {
        Subtask subtask1 = new Subtask(1, "Subtask 1", "Subtask description",Status.DONE, epic.getId(), LocalDateTime.of(2022, 3, 6, 13, 30), Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask(2, "Subtask 2", "Subtask description", Status.DONE, epic.getId(), LocalDateTime.of(2022, 3, 7, 13, 30), Duration.ofMinutes(30));
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertEquals(Status.DONE, epic.getStatus(), "Эпик должен иметь статус DONE, если все подзадачи имеют статус DONE.");
    }

    @Test
    void shouldReturnInProgressWhenSubtasksAreNewAndDone() {
        Subtask subtask1 = new Subtask(1, "Subtask 1", "Subtask description",Status.NEW, epic.getId(), LocalDateTime.of(2022, 3, 6, 13, 30), Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask(2, "Subtask 2", "Subtask description", Status.DONE, epic.getId(), LocalDateTime.of(2022, 3, 7, 13, 30), Duration.ofMinutes(30));
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Эпик должен иметь статус IN_PROGRESS, если подзадачи имеют статусы NEW и DONE.");
    }

    @Test
    void shouldReturnInProgressWhenAllSubtasksAreInProgress() {
        Subtask subtask1 = new Subtask(1, "Subtask 1", "Subtask description",Status.IN_PROGRESS, epic.getId(), LocalDateTime.of(2022, 3, 6, 13, 30), Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask(2, "Subtask 2", "Subtask description", Status.IN_PROGRESS, epic.getId(), LocalDateTime.of(2022, 3, 7, 13, 30), Duration.ofMinutes(30));
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Эпик должен иметь статус IN_PROGRESS, если все подзадачи имеют статус IN_PROGRESS.");
    }
}
