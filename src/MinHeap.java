public class MinHeap {
    int size;
    int capacity = 8;
    Object[] array;

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
    }
}