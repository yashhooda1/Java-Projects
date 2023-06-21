//Name: Yash Hooda
//Date Created: 08/29/2022
//Data Structure and Algorithms
//This program is an example of data structures being implemented using binary search, linear search, lazy deletion in various fields.

//main method
public class Main {
    public static void main(String[] args) {
        // add dyanmic array list.
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

        System.out.println("\n/* code on 8/31 handling sorted array */");
        list.clear();
        printArray(list);

        // linear search
        list.add(linearSearch(list, "Database"), "Database");
        list.add(linearSearch(list, "Medical"), "Medical");
        list.add(linearSearch(list, "Image Processing"), "Image Processing");
        printArray(list);

        // add stocks of google, amazon, and netflix as examples using binary insert.
        int index = binaryInsert(list, "GOOGL");
        if (index >= 0)
            list.add(index, "GOOGL");

        index = binaryInsert(list, "AMZN");
        if (index >= 0)
            list.add(index, "AMZN");

        index = binaryInsert(list, "NFLX");
        if (index >= 0)
            list.add(index, "NFLX");
        printArray(list);

    }

    // find size and length of array list.
    public static void printArray(DSArrayList<String> list) {
        System.out.println();
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("[%d] %s\n", i, list.get(i));
        }
        System.out.printf("Size: %d and Length: %d\n", list.size(), list.length());
    }

    /*
     * this function will find a position for an ordered key word in the array to be
     * inserted; so it will iterate
     * K ~ N elements. Then it will insert using add(index, E) function which will
     * shift N-K elements; hence the
     * sum of linearSearch and add() function is O(N) â†’ O(K) + O(N-K) â†’ O(N)
     * Note: depending upon the array K or N-K will approach O(N)
     */
    public static int linearSearch(DSArrayList<String> list, String key) { // O(N)
        int insIndex = 0;
        while (insIndex < list.size() && key.compareTo(list.get(insIndex)) >= 0) {
            insIndex++;
        }
        return insIndex;
    }

    /*
     * feel free to modify or use your version of binaryInsert
     * It needs to return the index position where to insert the new key
     */
    public static int binaryInsert(DSArrayList<String> list, String key) { // O(logN)
        int lowerBound = 0;
        int upperBound = list.size() - 1;
        int numElems = list.size();
        int curIndex;

        while (true) {
            curIndex = (upperBound + lowerBound) / 2;
            // if there are no elements, index is 0
            if (numElems == 0) {
                return curIndex = 0;
            }

            // if lower bound is the index, then make sure its greater then 0 in order to be
            // returned.
            if (lowerBound == curIndex) {
                if (list.get(curIndex).compareTo(key) > 0) {
                    return curIndex;
                }
            }

            // if current index is negative, then increment the current index by 1 in order
            // to find lower bound.
            if (list.get(curIndex).compareTo(key) < 0) {
                lowerBound = curIndex + 1;

                // if lower bound is greater then upper bound, then return current index by
                // incrementing by 1.
                if (lowerBound > upperBound) {
                    return curIndex += 1;
                }
                // if lower bound is greater, then return current index.
            } else if (lowerBound > upperBound) {
                return curIndex;
            } else {
                upperBound = curIndex - 1;
            }

        }

    }

    /*
     * ADD a BinarySearch function to return the index of the element
     * which means the Search in OrderedArray using BinarySearch will become O(logN)
     */
    public static int BinarySearch(DSArrayList<String> list, String key) {
        curIndex = binaryInsert(list, key);
        return curIndex;

        return 0; // temp return
    }
}

// data structure array list class, the point of
class DSArrayList<E> {
    private transient E[] data;
    private static final int DEFAULT_CAPACITY = 5;
    private int size;

    public DSArrayList() { // O(1)
        this(DEFAULT_CAPACITY);
    }

    // throw new warning if the capacity is too small.
    @SuppressWarnings("unchecked")
    public DSArrayList(int capacity) { // O(1)
        if (capacity < 0)
            throw new IllegalArgumentException();
        data = (E[]) new Object[capacity];
    }

    // check if size is equal to length of capacity.
    public boolean add(E e) { // *O(1) -- amortized constant
        if (size == data.length) {
            ensureCapacity(size + 1);
        }
        data[size++] = e;
        return true;
    }

    // check warnings and capacity is greater then current capacity.
    @SuppressWarnings("unchecked")
    public void ensureCapacity(int minCapacity) { // O(N)
        int current = data.length;
        if (minCapacity > current) {
            E[] newData = (E[]) new Object[Math.max(current * 2, minCapacity)];
            System.arraycopy(data, 0, newData, 0, size); // O(N)
            data = newData;
        }
    }

    // return size and index.
    public int size() {
        return size;
    } // O(1)

    // return length of data
    public int length() {
        return data.length;
    }

    // if object e is found, return the index of e
    public boolean contains(Object e) {
        return indexOf(e) != -1;
    }

    // find index number
    public int indexOf(Object e) { // O(N)
        for (int i = 0; i < size; i++) {
            if (e.equals(data[i]))
                return i;
        }
        return -1;
    }

    // remove index
    public E remove(int index) { // O(N)
        checkBoundExclusive(index);
        E temp = data[index];
        if (index != --size)
            System.arraycopy(data, index + 1, data, index, size - index); // Not Lazy; O(N)

        data[size] = null;
        return temp;
    }

    // remove lazy deletion
    public E removeLazy(int index) { // O(1)
        checkBoundExclusive(index);
        E temp = data[index];
        data[index] = null;
        return temp;
    }

    // check bounds of index
    private void checkBoundExclusive(int index) { // O(1)
        if (index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ",Size: " + size);
    }

    // get index range
    public E get(int index) { // O(1) access to kth element
        rangeCheck(index);
        return data[index];
    }

    // check index range if it is in range or not.
    private void rangeCheck(int index) { // O(1)
        if (index >= size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    // if string is out of bounds, then return index size.
    private String outOfBoundsMsg(int index) { // O(1)
        return "Index: " + index + ", Size: " + this.size;
    }

    /** New code on 8/31 ************/
    @SuppressWarnings("unchecked")
    public void clear() {
        data = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    // add index
    public void add(int index, E e) { // O(N) regardless of linearSearch or binaryInsert
        checkBoundInclusive(index);
        if (size == data.length) {
            ensureCapacity(size + 1);
        }

        if (index != size) {
            System.arraycopy(data, index, data, index + 1, size - index); // shifting everthing above the insert
                                                                          // position
        }

        data[index] = e;
        size++;
    }

    // if index is too big, then throw exception.
    private void checkBoundInclusive(int index) {
        if (index > size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

}
