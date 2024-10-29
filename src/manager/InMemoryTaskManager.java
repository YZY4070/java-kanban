package manager;

import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final HistoryManager historyManager;
    private final Set<Task> prioritizedTasks = new TreeSet<>((task1, task2) -> {

        if (task1.getStartTime() == null && task2.getStartTime() == null) {
            return 0;
        } else if (task1.equals(task2)) {
            return 0;
        } else if (task1.getStartTime() == null) {
            return 1;  // task1 уходит в конец списка
        } else if (task2.getStartTime() == null) {
            return 1; // task2 уходит в конец списка
        }

        return task1.getStartTime().compareTo(task2.getStartTime());
    });
    protected int nextId = 1;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    private static boolean isTimeOverLap(Task task1, Task task2) {
        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = task1.getEndTime();
        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = task2.getEndTime();

        if (start1 == null || end1 == null || start2 == null || end2 == null) {
            return false;
        }
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    private boolean prioritizedTaskOverlapCkeck(Task task) {
        return prioritizedTasks.stream()
                .filter(task1 -> task1.getId() != task.getId())
                .anyMatch(task1 -> isTimeOverLap(task1, task));
    }

    private void addPrioritizedTaskWithCheck(Task task) {
        if (task.getStartTime() == null && task.getDuration() == null) {
            System.out.println("Задача не была добавлена в список приоритетов, так как не содержит времени начала.");
            return;
        }
        if (prioritizedTaskOverlapCkeck(task)) {
            throw new RuntimeException("Найдено пересечение задач");
        } else {
            prioritizedTasks.add(task);
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    public void setNextId(Integer value) {
        nextId = value;
    }


    private int generateUniqueId() {
        return nextId++;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteAllTasks() {
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
            prioritizedTasks.remove(task);
        }
        tasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public void createTask(Task task) {
        addPrioritizedTaskWithCheck(task);
        int id = generateUniqueId();
        task.setId(id);
        tasks.put(id, task);

    }

    @Override
    public void updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            System.out.println("Таски под таким id нету, воспользуйтесь добавлением");
            return;
        }

        if (prioritizedTaskOverlapCkeck(tasks.get(task.getId()))) {
            throw new RuntimeException("Найдено пересечение задач");
        }

        prioritizedTasks.remove(tasks.get(task.getId()));
        addPrioritizedTaskWithCheck(task);
        tasks.put(task.getId(), task);
    }

    @Override
    public void deleteTaskById(int id) {
        Task removedTask = tasks.remove(id);
        if (removedTask != null) {
            historyManager.remove(id);
            prioritizedTasks.remove(removedTask);
        }
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }


    @Override
    public void deleteAllSubtasks() {
        epics.values().forEach(epic -> {
            epic.getSubtaskIds().forEach(historyManager::remove);
            epic.getSubtaskIds().clear();
            updateEpicStatus(epic);
            calculateEpicFields(epic);
        });
        subtasks.clear();
        prioritizedTasks.removeIf(task -> task instanceof Subtask);
    }


    @Override
    public Subtask getSubtaskById(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            System.out.println("Нет епика которому принадлежит сабтаска!");
            return;
        }
        addPrioritizedTaskWithCheck(subtask);
        int id = generateUniqueId();
        subtask.setId(id);
        subtasks.put(id, subtask);
        epic.addSubtaskId(subtask.getId());
        updateEpicStatus(epic);
        calculateEpicFields(epic); //2

    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            System.out.println("Эпик с ID " + subtask.getEpicId() + " не найден");
            return;
        }

        if (prioritizedTaskOverlapCkeck(subtask)) {
            throw new RuntimeException("Найдено пересечение задачи");
        }
        prioritizedTasks.remove(subtasks.get(subtask.getId()));
        addPrioritizedTaskWithCheck(subtask);
        subtasks.put(subtask.getId(), subtask);
        calculateEpicFields(epic); //3
        updateEpicStatus(epic);
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.removeSubtaskId(subtask.getId());
            updateEpicStatus(epic);
            calculateEpicFields(epic); //4
            historyManager.remove(id);
            prioritizedTasks.remove(subtask);
        }
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllEpics() {
        epics.values().forEach(epic -> {
            historyManager.remove(epic.getId());
            epic.getSubtaskIds().forEach(historyManager::remove);
            epic.getSubtaskIds().forEach(subtaskId -> prioritizedTasks.remove(subtasks.get(subtaskId)));
        });
        epics.clear();
        subtasks.clear();
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void createEpic(Epic epic) {
        int id = generateUniqueId();
        epic.setId(id);
        epics.put(id, epic);

    }

    @Override
    public void updateEpic(Epic newEpic) {
        Epic exisEpic = epics.get(newEpic.getId());
        if (exisEpic != null) {
            exisEpic.setName(newEpic.getName());
            exisEpic.setDescription(newEpic.getDescription());
        }
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (int subtaskId : epic.getSubtaskIds()) {
                historyManager.remove(id); // удаление
                prioritizedTasks.remove(subtasks.get(subtaskId));
                subtasks.remove(subtaskId);
            }
        }
        historyManager.remove(id);
    }

    @Override
    public List<Subtask> getSubtasksByEpicId(int epicId) {
        List<Subtask> result = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic != null) {
            for (int subtaskId : epic.getSubtaskIds()) {
                result.add(subtasks.get(subtaskId));
            }
        }
        return result;
    }

    protected void updateEpicStatus(Epic epic) {
        List<Integer> subtaskIds = epic.getSubtaskIds();
        if (subtaskIds.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (int id : subtaskIds) {
            Status status = subtasks.get(id).getStatus();
            if (status != Status.NEW) {
                allNew = false;
            }
            if (status != Status.DONE) {
                allDone = false;
            }
        }

        if (allNew) {
            epic.setStatus(Status.NEW);
        } else if (allDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    private void calculateEpicFields(Epic epic) {

        List<Subtask> subtasks = getSubtasksByEpicId(epic.getId());

        if (subtasks.isEmpty()) {
            epic.setStartTime(null);
            epic.setEndTime(null);
            epic.setDuration(Duration.ZERO);
            return;
        }

        LocalDateTime startTime = subtasks.stream()
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);
        LocalDateTime endTime = subtasks.stream()
                .map(Subtask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);
        Duration duration = subtasks.stream()
                .map(Subtask::getDuration)
                .reduce(Duration::plus)
                .orElse(null);

        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
        epic.setDuration(duration);
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}

