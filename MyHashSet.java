import java.util.ArrayList;
import java.util.Iterator;

/** 
 * The MyHashSet API is similar to the Java Set interface. This
 * collection is backed by a hash table.
 */
public class MyHashSet<E> implements Iterable<E> {

	/** Unless otherwise specified, the table will start as
	 * an array (ArrayList) of this size.*/
	private final static int DEFAULT_INITIAL_CAPACITY = 4;

	/** When the ratio of size/capacity exceeds this
	 * value, the table will be expanded. */
	private final static double MAX_LOAD_FACTOR = 0.75;

	public ArrayList<Node<E>> hashTable;

	private int size;  // number of elements in the table

	
	
	
	//Class representing the nodes that contain the data stored 
	//In the hash table
	public static class Node<T> {
		private T data;
		public Node<T> next;  

		private Node(T data) {
			this.data = data;
			next = null;
		}
	}

	/**
	 * Initializes an empty table with the specified capacity.  
	 *
	 * @param initialCapacity initial capacity (length) of the 
	 * underlying table
	 */
	public MyHashSet(int initialCapacity) {
		//Initializes hashTable variable and adds nulls until 
		//Filled to the initialCapacity
		hashTable = new ArrayList<Node<E>>();
		while(hashTable.size() < initialCapacity) {
			hashTable.add(null);
		}
	}

	/**
	 * Initializes an empty table of length equal to 
	 * DEFAULT_INITIAL_CAPACITY
	 */
	public MyHashSet() {
		//Initializes hashTable variable and adds nulls until
		//Filled to the default initial capacity, 4
		this(DEFAULT_INITIAL_CAPACITY);
	}

	/**
	 * Returns the number of elements stored in the table.
	 * @return number of elements in the table
	 */
	public int size(){
		//Variable representing number of elements in the table
		return size;
	}

	/**
	 * Returns the length of the table (the number of buckets).
	 * @return length of the table (capacity)
	 */
	public int getCapacity(){
		return hashTable.size();
	}

	/** 
	 * Looks for the specified element in the table.
	 * 
	 * @param element to be found
	 * @return true if the element is in the table, false otherwise
	 */
	public boolean contains(Object element) {
		//Variable representing the index of the element for the hash table
		int indexWouldBe = Math.abs(element.hashCode() % hashTable.size());
		//Checks to see if the bucket is empty. If so, the table does not
		//Contain the element. If it is not empty, the bucket is traversed
		//Through to see if any of the elements match the target element
		if(hashTable.get(indexWouldBe) == null) {
			return false;
		}
		Node<E> curr = hashTable.get(indexWouldBe);
		while(curr != null) {
			if(curr.data.equals(element)) {
				return true;
			}
			curr = curr.next;
		}
		return false;
	}

	/** Adds the specified element to the collection, if it is not
	 * already present.  If the element is already in the collection, 
	 * then this method does nothing.
	 * 
	 * @param element the element to be added to the collection
	 */
	public void add(E element) {
		//Checks to see if the element is already in the table
		if(this.contains(element)) {
			return;
		}
		//If it's not in the table, then it can be added
		//Variables representing the element in node form 
		//And the index of where the element should go
		Node<E> elementNode = new Node<E>(element);
		int addingIndex = Math.abs(element.hashCode() % hashTable.size());
		//Add the element to the table
		if(hashTable.get(addingIndex) == null) {
			hashTable.set(addingIndex, elementNode);
		} else {
			Node<E> currHead = hashTable.get(addingIndex);
			elementNode.next = currHead;
			hashTable.set(addingIndex, elementNode);
		}
		size++;
		//Check to see if the table needs to be rehashed
		if(((double)size / hashTable.size()) > MAX_LOAD_FACTOR) {
			//New empty table representing the old table but twice the size
			ArrayList<Node<E>> rehashed = new ArrayList<Node<E>>();
			while(rehashed.size() < (hashTable.size() * 2)) {
				rehashed.add(null);
			}
			//Iterate through old hash table and copy values into new one
			for(E obj : this) {
				int indexToAdd = Math.abs(obj.hashCode() % rehashed.size());
				Node<E> objNode = new Node<E>(obj);
				if(rehashed.get(indexToAdd) == null) {
					rehashed.set(indexToAdd, objNode);
				} else {
					Node<E> currHead = rehashed.get(indexToAdd);
					objNode.next = currHead;
					rehashed.set(indexToAdd, objNode);
				}
			}
			//Set hashTable to be the new table, generating garbage
			hashTable = rehashed;
		}
		
		
		
	}

	/** Removes the specified element from the collection.  If the
	 * element is not present then this method should do nothing.
	 *
	 * @param element the element to be removed
	 * @return true if an element was removed, false if no element 
	 * removed
	 */
	public boolean remove(Object element) {
		//Check to see if the element is even in the table
		if(!(this.contains(element))) {
			return false;
		}
		//If it is in the table, then proceed
		size--;
		//Variable representing the bucket the element should be
		int indexAt = Math.abs(element.hashCode() % hashTable.size());
		
		//Traverse through bucket to find element
		Node<E> curr = hashTable.get(indexAt);
		Node<E> prev = null;
		while(curr != null) {
			if(curr.data.equals(element)) {
				//Case where node to remove is the head of the bucket
				if(curr == hashTable.get(indexAt)) {
					hashTable.set(indexAt, curr.next);
					return true;
				}
				//Case where node to remove is the tail of the bucket
				if(curr.next == null) {
					prev.next = null;
					return true;
				}
				
				//Other possible case where node is somewhere in the middle of the list
				prev.next = curr.next;
				return true;
			}
			
			//If target is not found in this iteration, update curr and prev
			prev = curr;
			curr = curr.next;
		}
		
		//This statement is here for eclipse to not show any errors
		//If it runs, then something went wrong
		return false;
		
	}

	/** Returns an Iterator that can be used to iterate over
	 * all of the elements in the collection.
	 * 
	 * The order of the elements is unspecified.
	 */
	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {
			//Variable representing the elementCount and the current bucket
			private int elementAt = 0;
			private int currIndex = 0;
			//Variable representing a node in the current bucket
			private Node<E> currNodeInBucket = null;
			
			//Returns true if there are still more elements to iterate 
			//Through in the table
			public boolean hasNext() {
				return elementAt < size;
			}
			
			//Returns the next element in the table
			public E next() {
				//Finds the next non-empty bucket
				if(currNodeInBucket == null) {
					while(hashTable.get(currIndex) == null) {
						currIndex++;
					}
					currNodeInBucket = hashTable.get(currIndex);
				} else {
					
				}
				elementAt++;
				Node<E> returnable = currNodeInBucket;
				//If it's the tail, it will become null. If it's not, it'll go to the next
				if(currNodeInBucket.next == null) {
					currNodeInBucket = currNodeInBucket.next;
					currIndex++;
				} else {
					currNodeInBucket = currNodeInBucket.next;
				}
				return returnable.data;
				
				
			}
		};
	}

}
