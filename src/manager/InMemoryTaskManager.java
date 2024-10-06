package manager;

import task.*;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();


    protected int nextId = 1;

    protected final HistoryManager historyManager;

    private final Set<Task> prioritizedTasks = new TreeSet<>((task1, task2) -> {
//        if (task1.equals(task2)) return 0;

        if (task1.getStartTime() == null && task2.getStartTime() == null) {
            return 0;
        } else if (task1.equals(task2)){
            return 0;
        } else if (task1.getStartTime() == null) {
            return 1;  // task1 уходит в конец списка
        } else if (task2.getStartTime() == null) {
            return 1; // task2 уходит в конец списка
        }

        // Проверяем пересечение времени выполнения
        if (isTimeOverLap(task1, task2)) {
            throw new IllegalArgumentException("Задачи пересекаются по времени выполнения");
        }// После того как проверилось на пересечения идет добавление

        // Сортировка по времени начала
        return task1.getStartTime().compareTo(task2.getStartTime());
    });

    private static boolean isTimeOverLap(Task task1, Task task2) {
        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = task1.getEndTime();
        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = task2.getEndTime();

        // Проверка на null для времени окончания
        if (start1 == null || end1 == null || start2 == null || end2 == null) {
            return false;  // Невозможно проверить пересечение, если время неизвестно
        }

        // Проверка пересечения интервалов
        return start1.isBefore(end2) && start2.isBefore(end1);
    }


    private void addPrioritizedTaskWithCheck(Task task) {
        if (task.getStartTime() != null && task.getDuration() != null) {
            prioritizedTasks.add(task);
        } else {
            System.out.println("Задача не была добавлена в список приоритетов, так как не содержит времени начала.");
        }
    }

    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
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
        }
        tasks.clear();
        prioritizedTasks.removeIf(task -> task instanceof Task);
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public void createTask(Task task) {
        int id = generateUniqueId();
        task.setId(id);
        addPrioritizedTaskWithCheck(task);
        tasks.put(id, task);

    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Таски под таким id нету, воспользуйтесь добавлением");
        }

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
        for (Epic epic : epics.values()) {
            for (Integer subtaskId : epic.getSubtaskIds()) {
                historyManager.remove(subtaskId);
            }
            epic.getSubtaskIds().clear();
            updateEpicStatus(epic);
            epic.calculateEpicFields(this); //1
        }
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
        int id = generateUniqueId();
        subtask.setId(id);
        subtasks.put(id, subtask);
        epic.addSubtaskId(subtask.getId());
        updateEpicStatus(epic);
        epic.calculateEpicFields(this); //2
        addPrioritizedTaskWithCheck(epic);

    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            System.out.println("Эпик с ID " + subtask.getEpicId() + " не найден");
            return;
        }
        subtasks.put(subtask.getId(), subtask);
        epic.calculateEpicFields(this); //3
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.removeSubtaskId(subtask.getId());
            updateEpicStatus(epic);
            epic.calculateEpicFields(this); //4
            historyManager.remove(id);
        }
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllEpics() {
        for (Epic epic : epics.values()) {
            historyManager.remove(epic.getId());
            for (Integer id : epic.getSubtaskIds()) {
                historyManager.remove(id);
            }
        }
        epics.clear();
        subtasks.clear();
        prioritizedTasks.removeIf(task -> task instanceof Epic);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void createEpic(Epic epic) {
        epic.calculateEpicFields(this);
        int id = generateUniqueId();
        epic.setId(id);
        addPrioritizedTaskWithCheck(epic);
        epics.put(id, epic);

    }

    @Override
    public void updateEpic(Epic newEpic) {
        Epic exisEpic = epics.get(newEpic.getId());
        if (exisEpic != null) {
            exisEpic.setName(newEpic.getName());
            exisEpic.setDescription(newEpic.getDescription());
            exisEpic.calculateEpicFields(this);
        }
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (int subtaskId : epic.getSubtaskIds()) {
                subtasks.remove(subtaskId);
                historyManager.remove(id); // удаление
            }
        }
        historyManager.remove(id);
        prioritizedTasks.remove(epic);
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
        epic.calculateEpicFields(this); //5
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}

