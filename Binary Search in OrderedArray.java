//Homework 08/31 -  //Add a binary search function to return the index of the element
	 //which means the Search in OrderedArray using BinarySearch will become O(LogN)


public class Main {
	public static void main(String[] args) {
		DSArrayList<String> list = new DSArrayList<String>(); // O(1)
		System.out.printf("Size: %d and Length: %d\n", list.size(), list.length());
		list.add("Database");
		list.add("Medical");
		list.add("Image Processing");
		list.add("buildings");
		list.add("clustering");
		list.add("image-management");
		printArray(list);
		list.remove(3);
		printArray(list);
		list.add("buildings");
		printArray(list);
		list.removeLazy(3);
		list.removeLazy(1);
		printArray(list);
		
		System.out.println(" ");
		System.out.println("Code on 08/31");
		list.clear();
		printArray(list);
		
		list.add(linearSearch(list, "Database"), "Database");
		list.add(linearSearch(list, "Medical"), "Medical");
		list.add(linearSearch(list, "Image Processing"), "Image Processing");
		printArray(list);
		
		int index = binaryInsert(list, "GOOGL");
		if (index >= 0) list.add(index, "GOOGL");
		
		index = binaryInsert(list, "AMZN");
		if (index >= 0) list.add(index, "AMZN");
		
		index = binaryInsert(list, "NTFLX");
		if (index >= 0) list.add(index, "NTFLX");
	}
	
	public static void printArray(DSArrayList<String> list) {
	    System.out.println();
	    for (int i=0; i <list.size(); i++) {
	        System.out.printf("[%d] %s\n", i, list.get(i));
	    }
	    System.out.printf("Size: %d and Length: %d\n", list.size(), list.length());
	}
	
	
	public static int linearSearch(DSArrayList<String> list, String key) //O(N)
	{
	    int insIndex = 0;
	    while (insIndex < list.size() && key.compareTo(list.get(insIndex)) >= 0)
	    {
	        insIndex++;
	    }
	        return insIndex;
	}
	
	
	// feel free to modify or use your version of binaryInsert 
	// it needs to return the index position where to insert the new key
	public static int binaryInsert(DSArrayList<String> list, String key)  {
	    int lowerBound = 0;
	    int upperBound = list.size() - 1;
	    int numElems = list.size();
	    int currentIndex;
	    
	    while (true)
	    {
	        currentIndex = (upperBound + lowerBound) / 2;
	        if(numElems == 0) {
	            return currentIndex = 0;
	        }
	        
	        if (lowerBound == currentIndex) {
	            if (list.get(currentIndex).compareTo(key) > 0) {
	                return currentIndex;
	            }
	        }
	        
	        if(list.get(currentIndex).compareTo(key) < 0) {
	            lowerBound = currentIndex + 1;
	            if (lowerBound > upperBound) {
	                return currentIndex += 1;
	            }
	        }
	        else if (lowerBound > upperBound) {
	        return currentIndex;
	        }
	        else {
	            upperBound = currentIndex - 1;
	        }
	        
	        
	        
	        
	    }
	}
	 //Add a binary search function to return the index of the element
	 //which means the Search in OrderedArray using BinarySearch will become O(LogN)
	 
	 public static int binarySearch(DSArrayList<String> list, String key) {
	     
	     return 0;
	 }
}

class DSArrayList<E> {
    private transient E[] data;
    private static final int DEFAULT_CAPACITY = 5;
    private int size;
    
    public DSArrayList() { // O(1)
        this(DEFAULT_CAPACITY);
    }
    
    @SuppressWarnings("unchecked")
    public DSArrayList(int capacity) { // O(1)
        if (capacity < 0)
            throw new IllegalArgumentException();
        data = (E[]) new Object[capacity];
    }
    
    public boolean add(E e) { // *O(1) -- amortized constant 
        if (size == data.length) {
            ensureCapacity(size + 1);
        }
        data[size++] = e;
        return true;
    }
    
    @SuppressWarnings("unchecked")
    public void ensureCapacity(int minCapacity) { // O(N)
        int current = data.length;
        if (minCapacity > current) {
            E[] newData = (E[]) new Object[Math.max(current * 2, minCapacity)];
            System.arraycopy(data, 0, newData, 0, size); // O(N)
            data = newData;
        }
    }
    
    public int size() {return size;} // O(1)
    public int length(){ return data.length;}
    public boolean contains(Object e) {return indexOf(e) != -1; }
    
    public int indexOf(Object e) { // O(N)
        for (int i = 0; i < size; i++) {
            if (e.equals(data[i]))
                return i;
        }
        return -1;
    }
    
    public E remove(int index) { // O(N)
        checkBoundExclusive(index);
        E temp = data[index];
        if (index != --size)
            System.arraycopy(data, index + 1, data, index, size - index); // Not Lazy; O(N)
        
        data[size] = null;
        return temp;
    }
    
    public E removeLazy(int index) { // O(1)
        checkBoundExclusive(index);
        E temp = data[index];
        data[index] = null;
        return temp;
    }
    
    private void checkBoundExclusive(int index) { //O(1)
        if (index >= size) throw new IndexOutOfBoundsException("Index: " + index + ",Size: " + size);
    }
    
    public E get(int index) { // O(1) access to kth element 
        rangeCheck(index);
        return data[index];
    }
    
    private void rangeCheck(int index) { // O(1)
        if (index >= size) throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }
    
    private String outOfBoundsMsg(int index) { // O(1)
        return "Index: " + index + ", Size: " + this.size;
    }

    //New code on 08/31
    @SuppressWarnings("unchecked")
    public void clear() {
        data = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
        
    }

    public void add(int index, E e) { // O(N)
        checkBoundInclusive(index);
        if (size == data.length) {
            ensureCapacity(size + 1);
        }
        
        if (index != size) {
            System.arraycopy(data, index, data, index+1, size-index); //Create a gap and shifting everything above the insert position
        }
        
        data[index] = e;
        size++;
    }

    private void checkBoundInclusive (int index) {
        if (index > size)
        throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }




}




