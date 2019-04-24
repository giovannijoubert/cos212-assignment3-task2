/**
 * A B+ tree leaf node
 * @param <TKey> the data type of the key
 * @param <TValue> the data type of the value
 */
class BPTreeLeafNode<TKey extends Comparable<TKey>, TValue> extends BPTreeNode<TKey, TValue> {
	
	private Object[] values;
	
	public BPTreeLeafNode(int order) {
		this.m = order;
		// The strategy used here first inserts and then checks for overflow. 
		// Thus an extra space is required i.e. m instead of m-1.
		// You can change this if needed.
		this.keys = new Object[m];
		this.values = new Object[m];
	}

	@SuppressWarnings("unchecked")
	public TValue getValue(int index) {
		return (TValue)this.values[index];
	}

	public void setValue(int index, TValue value) {
		this.values[index] = value;
	}
	
	@Override
	public boolean isLeaf() {
		return true;
	}

	////// You should not change any code above this line //////

	////// Implement functions below this line //////

	

	public BPTreeNode<TKey, TValue> insert(TKey key, TValue value) 
	{
		if(this.getKeyCount() < m){ // THIS LEAF has space
			if (this.getKeyCount() == 0){ //THIS LEAF IS EMPTY
				this.setKey(0, key);
				this.setValue(0, value);
				this.keyTally++;
				return this;
			}
			
			for(int i = 0; i < this.getKeyCount(); i++){
				if (this.getKey(i).compareTo(key) > 0){
					for(int j = this.getKeyCount(); j > i; j--){ //MOVE UP TO MAKE SPACE FOR NEW KEYPAIR
						this.setKey(j, this.getKey(j-1));
						this.setValue(j, this.getValue(j-1));
					}
					this.setKey(i, key);
					this.setValue(i, value);
					this.keyTally++;
					break;
				}
				if(i+1 == this.getKeyCount()){ //NEW KEYPAIR IS LARGER THAN ALL PREVIOUS ONES
					this.setKey(i+1, key);
					this.setValue(i+1, value);
					this.keyTally++;
					break;
				}
			}
		} 

		if (this.getKeyCount() == m) { //THIS WAS leaf but is full now needs to split
			BPTreeInnerNode<TKey, TValue> s = splitLeaf(this);
			return s;
		}
		return this;
	}

	public BPTreeInnerNode<TKey, TValue> splitLeaf(BPTreeLeafNode<TKey, TValue> oldLeaf){
		int insertPos = 0;	
		BPTreeLeafNode<TKey, TValue> newLeafLeft = new BPTreeLeafNode<TKey, TValue>(oldLeaf.m);
			BPTreeLeafNode<TKey, TValue> newLeafRight= new BPTreeLeafNode<TKey, TValue>(oldLeaf.m);
			BPTreeInnerNode<TKey, TValue> newInner = new BPTreeInnerNode<TKey, TValue>(oldLeaf.m);
			if (oldLeaf.parentNode == null){
				newInner.setKey(0, oldLeaf.getKey(oldLeaf.getKeyCount()/2));
				newInner.keyTally = 1;
				insertPos = 0;
			} else {
				BPTreeInnerNode<TKey, TValue> parent = (BPTreeInnerNode<TKey, TValue>)oldLeaf.parentNode;
				insertPos = parent.promote(oldLeaf.getKey(oldLeaf.getKeyCount()/2));
				newInner = parent;
			}

			for(int i = 0; i < oldLeaf.getKeyCount()/2; i++){
				newLeafLeft.setKey(i, oldLeaf.getKey(i));
				newLeafLeft.setValue(i, oldLeaf.getValue(i));
				newLeafLeft.keyTally++;
			}

			for(int i = oldLeaf.getKeyCount()/2; i < oldLeaf.getKeyCount(); i++){
				newLeafRight.setKey(newLeafRight.getKeyCount(),oldLeaf.getKey(i));
				newLeafRight.setValue(newLeafRight.getKeyCount(), oldLeaf.getValue(i));
				newLeafRight.keyTally++;
			}

			newLeafLeft.rightSibling = newLeafRight;
			newLeafRight.leftSibling = newLeafLeft;
			newLeafLeft.setParent(newInner);
			newLeafRight.setParent(newInner);

			newLeafLeft.leftSibling = oldLeaf.leftSibling;
			newLeafRight.rightSibling = oldLeaf.rightSibling;

			if(newLeafLeft.leftSibling != null)
			newLeafLeft.leftSibling.rightSibling = newLeafLeft;

			if(newLeafRight.rightSibling != null)
			newLeafRight.rightSibling.leftSibling = newLeafRight;

			if(oldLeaf.parentNode == null){
				newInner.setChild(0, newLeafLeft);
				newInner.setChild(1, newLeafRight);
			} else {
				newInner.setChild(insertPos, newLeafLeft);
				newInner.setChild(insertPos+1, newLeafRight);
			}
			
		
		
			return newInner;
		
	}
	
	public BPTreeNode<TKey, TValue> delete(TKey key) 
	{
		for(int i = 0; i < this.getKeyCount(); i++){
			if(this.getKey(i).equals(key)){
				for(int j = i; j < this.getKeyCount(); j++){
					this.setKey(j, this.getKey(j+1));
					this.setValue(j, this.getValue(j+1));
				}
				this.keyTally--;
				break;
			}
		}
		return this; 
	}
}
