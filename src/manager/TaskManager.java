package manager;
import package_task.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private int nextId = 1;

    private int generateUniqueId() {
        return nextId++;
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public void createTask(Task task) {
        int id = generateUniqueId();
        task.setId(id);
        tasks.put(id, task);
    }

    public void updateTask(Task task) {
        if(tasks.containsKey(task.getId())){
            tasks.put(task.getId(), task);
        }else{
            System.out.println("Таски под таким id нету, воспользуйтесь добавлением");
        }

    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void deleteAllSubtasks() {
        for(Epic epic : epics.values()){
            epic.getSubtaskIds().clear();
            updateEpicStatus(epic);
        }
        subtasks.clear();

    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public void createSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null){
            int id = generateUniqueId();
            subtask.setId(id);
            subtasks.put(id, subtask);
            epic.addSubtaskId(subtask.getId());
            updateEpicStatus(epic);
        }else{
            System.out.println("У сабтаски неверно задан epicId к которому она должна принадлежать");
        }
    }

    public void updateSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            System.out.println("Эпик с ID " + subtask.getEpicId() + " не найден");
            return;
        }
        subtasks.put(subtask.getId(), subtask);
    }


    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.removeSubtaskId(subtask.getId());
            updateEpicStatus(epic);
        }
    }

    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public void createEpic(Epic epic) {
        int id = generateUniqueId();
        epic.setId(id);
        epics.put(id, epic);
    }

    public void updateEpic(Epic newEpic) {
        Epic exisEpic = epics.get(newEpic.getId());
        if (exisEpic != null){
            exisEpic.setName(newEpic.getName());/*у меня вылетело из головы что мы получаем ссылку на объект,
             а не копируем полностью весь объект в новый. Я чуть не сошел с ума на этот моменте, иногда такое вылетает
             из головы и это очень печально, ведь останавливает тебя очень надолго*/
            exisEpic.setDescription(newEpic.getDescription());
            updateEpicStatus(exisEpic);
        }
    }

    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (int subtaskId : epic.getSubtaskIds()) {
                subtasks.remove(subtaskId);
            }
        }
    }

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

    private void updateEpicStatus(Epic epic) {
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
}
