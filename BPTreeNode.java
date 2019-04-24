/**
 * A B+ tree generic node
 * Abstract class with common methods and data. Each kind of node implements this class.
 * @param <TKey> the data type of the key
 * @param <TValue> the data type of the value
 */
abstract class BPTreeNode<TKey extends Comparable<TKey>, TValue> {
	
	protected Object[] keys;
	protected int keyTally;
	protected int m;
	protected BPTreeNode<TKey, TValue> parentNode;
	protected BPTreeNode<TKey, TValue> leftSibling;
	protected BPTreeNode<TKey, TValue> rightSibling;
	protected static int level = 0;
	

	protected BPTreeNode() 
	{
		this.keyTally = 0;
		this.parentNode = null;
		this.leftSibling = null;
		this.rightSibling = null;
	}

	public int getKeyCount() 
	{
		return this.keyTally;
	}
	
	@SuppressWarnings("unchecked")
	public TKey getKey(int index) 
	{
		return (TKey)this.keys[index];
	}

	public void setKey(int index, TKey key) 
	{
		this.keys[index] = key;
	}

	public BPTreeNode<TKey, TValue> getParent() 
	{
		return this.parentNode;
	}

	public void setParent(BPTreeNode<TKey, TValue> parent) 
	{
		this.parentNode = parent;
	}	
	
	public abstract boolean isLeaf();
	
	/**
	 * Print all nodes in a subtree rooted with this node
	 */
	@SuppressWarnings("unchecked")
	public void print(BPTreeNode<TKey, TValue> node)
	{
		level++;
		if (node != null) {
			System.out.print("Level " + level + " ");
			node.printKeys();
			System.out.println();

			// If this node is not a leaf, then 
        		// print all the subtrees rooted with this node.
        		if (!node.isLeaf())
			{	BPTreeInnerNode inner = (BPTreeInnerNode<TKey, TValue>)node;
				for (int j = 0; j < (node.m); j++)
    				{
        				this.print((BPTreeNode<TKey, TValue>)inner.references[j]);
    				}
			}
		}
		level--;
	}

	/**
	 * Print all the keys in this node
	 */
	protected void printKeys()
	{
		System.out.print("[");
    		for (int i = 0; i < this.getKeyCount(); i++)
    		{
        		System.out.print(" " + this.keys[i]);
    		}
 		System.out.print("]");
	}


	////// You may not change any code above this line //////

	////// Implement the functions below this line //////
	
/*	public abstract void setValue(int index, TValue value);
	public abstract TValue getValue(int index);
	public abstract void setChild(int index, BPTreeNode<TKey, TValue> child); */

	/**
	 * Search a key on the B+ tree and return its associated value. If the given key 
	 * is not found, null should be returned.
	 */
	public TValue search(TKey key) 
	{
		//always ran on root
		if (this.isLeaf()){ // if root is leaf just do search
			for(int i = 0; i< this.getKeyCount(); i++){
				if (this.getKey(i).equals(key)){
					BPTreeLeafNode<TKey, TValue> golden = (BPTreeLeafNode<TKey, TValue>)this;
					return golden.getValue(i);
				}
			}
			return null;
		}

		Boolean complete = false;

		BPTreeInnerNode<TKey, TValue> Traverse = (BPTreeInnerNode<TKey, TValue> )this;

		while(! complete){
			if (Traverse.getChild(0).isLeaf()){
				complete = true;
			} else {
				Traverse = (BPTreeInnerNode<TKey, TValue>)Traverse.getChild(0);
			}
		}

		complete = false;
		BPTreeLeafNode<TKey, TValue> TraverseLeaf = (BPTreeLeafNode<TKey, TValue>)Traverse.getChild(0);
		while(! complete){
			for(int i = 0; i< TraverseLeaf.getKeyCount(); i++){
				if (TraverseLeaf.getKey(i).equals(key)){
					complete = true;
					return TraverseLeaf.getValue(i);
				}
			}
			if(TraverseLeaf.rightSibling == null){
				complete = true;
				break;
			}
			TraverseLeaf = (BPTreeLeafNode<TKey, TValue>)TraverseLeaf.rightSibling;
		}
	
		

		return null;
	}



	/**
	 * Insert a new key and its associated value into the B+ tree. The root node of the
	 * changed tree should be returned.
	 */
	public BPTreeNode<TKey, TValue> insert(TKey key, TValue value) 
	{
		return this; // no instance will ever be a pure BPTreeNode
	}

	


	/**
	 * Delete a key and its associated value from the B+ tree. The root node of the
	 * changed tree should be returned.
	 */
	public BPTreeNode<TKey, TValue> delete(TKey key) 
	{
		// Your code goes here
		return this; // no instance will ever be a pure BPTreeNode
	}
	
}