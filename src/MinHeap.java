public class MinHeap {
    int size;
    int capacity = 8;
    Object[] array;

    public long comparisons = 0;
    public long movements = 0;

    public void resetCounters(){
        comparisons = 0;
        movements = 0;
    }

    public MinHeap(){
        this.array = new Object[capacity];
    }
    public MinHeap(int capacity){
        if (capacity < 1) {
            capacity = 1;
        }
        this.capacity = capacity;
        this.array = new Object[capacity];
    }

    private void grow(){
        int newCapacity = capacity * 2;
        Object[] newArray = new Object[newCapacity];

        for(int i = 0; i < size; i++){
            newArray[i] = array[i];
        }
        capacity = newCapacity;
        array = newArray;
    }

    public void insert(Object data){
        if(size>= capacity)
        {
            grow();
        }
        array[size] = data;
        size++;
        siftUp(size - 1);
    }

    private void siftUp(int index){
        while (index > 0) {
            int parent = (index - 1) / 2;
            comparisons++;
            if (compare(array[index], array[parent]) < 0) {
                swap(index, parent);
                index = parent;
            } else {
                break;
            }
        }
    }
    private void swap(int i, int j){
        Object temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        movements++;
    }

    private int compare(Object a, Object b){
        return ((Comparable) a).compareTo(b);
    }

    public Object peekMin(){
        if (size == 0) throw new IndexOutOfBoundsException();
        return array[0];
    }
    public Object extractMin(){
        if (size == 0) throw new IndexOutOfBoundsException();
        Object min = array[0];
        array[0] = array[size - 1];
        array[size - 1] = null;
        size--;
        if (size > 0) siftDown(0);
        return min;
    }

    private void siftDown(int index){
        while (true) {
            int left = index * 2 + 1;
            int right = index * 2 + 2;
            int smallest = index;
            if (left < size) {
                comparisons++;
                if (compare(array[left], array[smallest]) < 0) {
                    smallest = left;
                }
            }
            if (right < size) {
                comparisons++;
                if (compare(array[right], array[smallest]) < 0) {
                    smallest = right;
                }
            }
            if (smallest == index) break;
            swap(index, smallest);
            index = smallest;
        }
    }
    public boolean isEmpty(){
        return size == 0;
    }
}