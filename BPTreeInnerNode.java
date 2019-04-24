/**
 * A B+ tree internal node
 * @param <TKey> the data type of the key
 * @param <TValue> the data type of the value
 */
class BPTreeInnerNode<TKey extends Comparable<TKey>, TValue> extends BPTreeNode<TKey, TValue> {
	
	protected Object[] references; 
	
	public BPTreeInnerNode(int order) {
		this.m = order;
		// The strategy used here first inserts and then checks for overflow. 
		// Thus an extra space is required i.e. m instead of m-1/m+1 instead of m.
		// You can change this if needed. 
		this.keys = new Object[m];
		this.references = new Object[m + 1];
	}
	
	@SuppressWarnings("unchecked")
	public BPTreeNode<TKey, TValue> getChild(int index) {
		return (BPTreeNode<TKey, TValue>)this.references[index];
	}

	public void setChild(int index, BPTreeNode<TKey, TValue> child) {
		this.references[index] = child;
		if (child != null)
			child.setParent(this);
	}
	
	@Override
	public boolean isLeaf() {
		return false;
	}

	////// You should not change any code above this line //////

	////// Implement functions below this line //////

	public int promote(TKey key){
		for(int i = 0; i < this.getKeyCount(); i++){
			if (this.getKey(i).compareTo(key) > 0){
				for(int j = this.getKeyCount(); j > i; j--){ //MOVE UP TO MAKE SPACE FOR NEW KEYPAIR
					this.setKey(j, this.getKey(j-1));
					this.setChild(j, this.getChild(j-1));

				}
				this.setKey(i, key);
				this.keyTally++;
				return i;
			}
			if(i+1 == this.getKeyCount()){ //NEW KEYPAIR IS LARGER THAN ALL PREVIOUS ONES
				this.setKey(i+1, key);
				this.keyTally++;
				return i+1;
			}
		}
		return 0;
	}

	public BPTreeNode<TKey, TValue> insert(TKey key, TValue value) 
	{
		int trav = 0;
		BPTreeInnerNode<TKey, TValue> Traverse = (BPTreeInnerNode<TKey, TValue>)this;
		while(! Traverse.isLeaf()){
			for(int i = 0; i < Traverse.getKeyCount(); i++){
				if(key.compareTo(Traverse.getKey(i)) < 0){
					if (Traverse.getChild(i).isLeaf()){
						BPTreeNode<TKey, TValue> I = Traverse.getChild(i).insert(key, value); 
						if (I == Traverse.getChild(i)){
							return this;
						} else {
							return I;
						}

					}
				}
				if(i+1 == Traverse.getKeyCount()){
					if (Traverse.getChild(i+1).isLeaf()){
						BPTreeNode<TKey, TValue> I = Traverse.getChild(i+1).insert(key, value); 
						if (I == Traverse.getChild(i+1)){
							return this;
						} else {
							return I;
						}
					}
				}		
				trav = i;
			}
			Traverse = (BPTreeInnerNode<TKey, TValue>)Traverse.getChild(trav);
				
		}


		return null;
	}


}