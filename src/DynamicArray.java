public class DynamicArray {
    int size;
    int capacity = 8;
    Object[] array;

    public DynamicArray(){
        this.array = new Object[capacity];
    }
    public DynamicArray(int capacity){
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
    public void insert(int index, Object data){
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
    public int search(Object data) {
        for (int i = 0; i < size; i++) {
            if (array[i] == data) {
                return i;
            }
        }
        return -1;
    }
    private void grow(){
        int newCapacity =(int)(capacity * 2);
        Object[] newArray = new Object[newCapacity];

        for(int i = 0; i < size; i++){
            newArray[i] = array[i];
        }
        capacity = newCapacity;
        array = newArray;
    }
    private void shrink(){
        int newCapacity =(int)(capacity / 2);
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
