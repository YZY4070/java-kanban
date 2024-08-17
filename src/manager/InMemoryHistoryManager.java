package manager;

import package_task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> idNodeMap = new HashMap<>();
    private Node head;
    private Node tail;

    private static void removeFromMiddle(Node optionalExistedNode) {
        // запоминаем у удаляемого элемента слудующий за ним
        Node beforeRemoveNext = optionalExistedNode.next;

        // запоминаем у удаляемого элемента его предыдущий элемент
        Node beforeRemovePrevious = optionalExistedNode.previous;

        // у следующего за удаляемым переносим ссылку на предыдущий предыдущий
        beforeRemoveNext.previous = beforeRemovePrevious;
        // у предыдущего за удаляемым переносим ссылку на слудующий следующий
        beforeRemovePrevious.next = beforeRemoveNext;

        // обнуляем у удаляемого все ссылки чтоб он голову не ебал
        optionalExistedNode.next = null;
        optionalExistedNode.previous = null;
    }

    private void removeIfExists(Task task) {
        Node optionalExistedNode = idNodeMap.remove(task.getId());

        // Если такая нода с такой таской вообще существует
        if (optionalExistedNode != null) {

            // если удаляемая нода является сейчас головой
            if (optionalExistedNode == head) {
                removeHead(optionalExistedNode);
                // если удаляемая нода сейчас является хвостом
            } else if (optionalExistedNode == tail) {
                removeTail(optionalExistedNode);
                // если удаление происходит из середины
            } else {
                removeFromMiddle(optionalExistedNode);
            }

        }
    }

    @Override
    public void remove(int id) {
        removeById(id);
    }

    private void removeById(Integer id) {
        Node optionalExistedNode = idNodeMap.remove(id);

        // Если такая нода с такой таской вообще существует
        if (optionalExistedNode != null) {

            // если удаляемая нода является сейчас головой
            if (optionalExistedNode == head) {
                removeHead(optionalExistedNode);
                // если удаляемая нода сейчас является хвостом
            } else if (optionalExistedNode == tail) {
                removeTail(optionalExistedNode);
                // если удаление происходит из середины
            } else {
                removeFromMiddle(optionalExistedNode);
            }

        }
    }

    private void removeTail(Node optionalExistedNode) {
        // запоминаем новый хвост (берем у текущего хвоста предыдущий элемент)
        Node nextTail = optionalExistedNode.previous;
        // у ранее существующего хвоста обнуляем ссылку на предыдущий элемент
        tail.previous = null;
        // у запомненного нового хвоста обнуляем ссылку на следующий элемент
        // (так как до этого он был не хвостом и ссылка на предыдущий элемент у него не была пустой)
        nextTail.next = null;

        // ранее вычисленный новый хвост запоминаем что это теперь хвост
        tail = nextTail;
    }

    private void removeHead(Node optionalExistedNode) {
        // запоминаем слудующую голову (берем из текущей головы следующий элемент)
        Node nextHead = optionalExistedNode.next;

        // берем следующий элмент за головой и обнуляем у него ссылку на предыдущий элемент
        optionalExistedNode.next.previous = null;
        // у текущей головы обнуляем ссылку на следующий элемент (help GC)
        optionalExistedNode.next = null;

        // ранее вычисленную новую голову запоминаем что это теперь голова
        head = nextHead;
    }

    private void generateNewNode(Task task) {

        Node newNode = new Node(task, null, null);

        if (head == null && tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.previous = tail;
            tail.next = newNode;

            tail = newNode;
        }
        idNodeMap.put(newNode.task.getId(), newNode);
    }

    @Override
    public List<Task> getHistory() {
        return getAllTasksFromNodes();
    }

    @Override
    public void add(Task task) {
        removeIfExists(task);
        generateNewNode(task);
    }

    private List<Task> getAllTasksFromNodes() {
        List<Task> tasksFromNodes = new ArrayList<>();

        tasksFromNodes.add(head.task);
        Node nextNode = head.next;

        while (nextNode != null) {

            tasksFromNodes.add(nextNode.task);

            nextNode = nextNode.next;
        }

        return tasksFromNodes;
    }

    public static class Node {

        private final Task task;
        private Node next;
        private Node previous;

        public Node(Task task, Node next, Node previous) {
            this.task = task;
            this.next = next;
            this.previous = previous;
        }
    }


}
