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
            //(едиснтвенный элемент) - нода является и головой и хвостом
            if (optionalExistedNode == head && optionalExistedNode == tail) {
                head = null;
                tail = null;
            } else if (optionalExistedNode == head) { // если удаляемая нода является сейчас головой
                removeHead(optionalExistedNode);

            } else if (optionalExistedNode == tail) { // если удаляемая нода сейчас является хвостом
                removeTail(optionalExistedNode);
            } else {
                removeFromMiddle(optionalExistedNode); // если удаление происходит из середины
            }
        }
    }

    @Override
    public void remove(int id) {
        removeById(id);
    }

    private void removeById(Integer id) {
        Node optionalExistedNode = idNodeMap.remove(id);
        if (optionalExistedNode != null) {
            // Если такая нода с такой таской вообще существует
            if (optionalExistedNode.task != null) {
                if (optionalExistedNode == head && optionalExistedNode == tail) {
                    head = null;
                    tail = null;
                } else if (optionalExistedNode == head) { // если удаляемая нода является сейчас головой
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
    }

    private void removeTail(Node optionalExistedNode) {
        Node nextTail = optionalExistedNode.previous;
        tail = nextTail;
        if (tail != null) {
            tail.next = null;
        } else {
            System.out.println("Удалять нечего! Добавьте таску в историю!");
        }
    }

    private void removeHead(Node optionalExistedNode) {
        Node nextHead = optionalExistedNode.next;
        head = nextHead;
        if (head != null) {
            head.previous = null;
        }
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
        if (task != null) {
            removeIfExists(task);
            generateNewNode(task);
        } else {
            System.out.println("Вы пытаетесь добавить пустую таску!");
        }
    }

    private List<Task> getAllTasksFromNodes() {
        List<Task> tasksFromNodes = new ArrayList<>();

        if (head != null) {
            Node node = head;
            while (node != null) {
                tasksFromNodes.add(node.task);
                node = node.next;
            }
            return tasksFromNodes;
        } else {
            System.out.println("Возвращать нечего, список пуст! Добавьте в историю таску!");
            return tasksFromNodes;
        }
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
