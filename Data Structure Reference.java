public class Main
{
	public static void main(String[] args) {
		 DSArrayUnorderedList<String> list = new DSArrayUnorderedList<String>(); //O(1)
		 list.add("Database");
		 list.add("Medical");
		 list.add("Image Processing");
		 list.add("buildings");
		 list.add("clustering");
		 list.add("image-management");
		 printArray(list);
		 list.removeLazy(3);
		 list.removeLazy(1);
		 printArray(list);
	}
	
	public static void printArray(DSArrayUnorderedList<String> list) {
	    System.out.println();
	    for (int i = 0; i < list.size(); i++)  {
	        System.out.printf("[%d] and Length: %d\n", list.size(), list.length());
	    }
	    System.out.printf("Size: %d and Length: %d\n", list.size(), list.length());
	}
}

class DSArrayUnorderedList {
    private transient E[] data;   
    private static final int DEFAULT_CAPACITY = 5;
    private int size;
    
    public DSArrayUnorderedList() {
        this(DEFAULT_CAPACITY);
        
    }
    
    @SuppressWarnings("unchecked")
    
    public DSArrayUnorderedList(int capacity) { // O(1)
        if(capacity < 0)
            throw new IllegalArgumentException();
            data = (E[]) new Object[capacity];
    }
    
    public boolean add(E e) { // *O(1) -- amortized constant 
        if(size == data.length) {
            ensureCapacity(size + 1);
        }
        data[size++] = e;
        return true;
    }
    
    @SuppressWarnings("unchecked")
    public void ensureCapacity(int minCapacity) { //O(N)
        int current = data.length;
        if (minCapacity > current) {
            E[] newData = (E[]) new Object[Math.max(current * 2, minCapacity)];
            System.arraycopy(data, 0, newData, 0, size); //O(N) and copy array
            data = newData;
        }
    }
    
    public int size() {return size;} // O(1)
    public int length() {return data.length;}
    public boolean contains(Object e) {return indexOf(e) != -1; }
    public int indexOf(Object e) { // O(N)
        for (int i = 0; i < size; i++)
        {
            if (e.equals(data[i]))
            return i;
        }
        return -1;
    }
    
    public E removeLazy(int index) { //O(1)
        checkBoundExclusive(index);
        E temp = data[index];
        data[size] = null;
        return temp;
}

private void checkBoundExclusive(int index) { //O(1)
    if (index >= size) throw new IndexOutOfBoundsException("Index: " + index + "Size: " + size);
}


public E get (int index) { //O(1) access to the kth element 
    rangeCheck(index);
    return data[index];
}

private void rangeCheck(int index) { //O(1)
    if (index >= size) throw new IndexOutOfBoundsException("Index: " + index + "Size: " + size);
}

private String outOfBoundMsg(int index) { //O(1)
    return "Index: " + index + ", Size: " + this.size;
}
}
    
