public class DynamicArray {
    int size;
    int capacity = 8;
    Object[] array;

    public DynamicArray(){
        this.array = new Object[capacity];
    }
    public DynamicArray(int capacity){
        if (capacity < 1) {
            capacity = 1;
        }
        this.capacity = capacity;
        this.array = new Object[capacity];
    }
    public void add(Object data){
        if(size>= capacity)
        {
            grow();
        }
        array[size] = data;
        size++;

    }
    public void add(int index, Object data){
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        if(size>= capacity)
        {
            grow();
        }
        for(int i = size; i>index;i--){
            array[i] = array[i-1];
        }
        array[index] = data;
        size++;
    }
    public void remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        for (int j = index; j < size - 1; j++) {
            array[j] = array[j + 1];
        }
        array[size - 1] = null;
        size--;
        if (size <= capacity / 3) shrink();
    }
    public boolean contains(Object data) {
        for (int i = 0; i < size; i++) {
            if (array[i] != null && array[i].equals(data))
                return true;
        }
        return false;
    }
    private void grow(){
        int newCapacity =capacity * 2;
        Object[] newArray = new Object[newCapacity];

        for(int i = 0; i < size; i++){
            newArray[i] = array[i];
        }
        capacity = newCapacity;
        array = newArray;
    }
    private void shrink(){
        int newCapacity =capacity / 2;
        if (newCapacity < 1) {
            newCapacity = 1;
        }
        Object[] newArray = new Object[newCapacity];
        for(int i = 0; i < size; i++){
            newArray[i] = array[i];
        }
        capacity = newCapacity;
        array = newArray;
    }
    public boolean isEmpty(){
        return size == 0;
    }
    public Object get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return array[index];
    }
}
