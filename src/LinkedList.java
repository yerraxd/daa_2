
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
}
