
public class LinkedList {
    private static class Node {
        Object data;
        Node next;

        Node(Object data) {
            this.data = data;
        }
    }
    private Node head;
    private Node tail;
    private int size;

    public LinkedList() {
    }

    public void add(Object data) {
        Node node = new Node(data);
        if (head == null) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
    }
    private Node getNode(int index) {
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current;
    }
    public Object get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return getNode(index).data;
    }
    public boolean contains(Object data) {
        Node current = head;
        while (current != null) {
            if (current.data != null && current.data.equals(data))
                return true;
            current = current.next;
        }
        return false;
    }
}
