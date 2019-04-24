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
				for(int j = this.getKeyCount(); j > i; j--){ //MOVE UP TO MAKE SPACE FOR NEW KEY
					this.setKey(j, this.getKey(j-1));
				}
				for(int j = this.getKeyCount(); j > i; j--){ //MOVE UP TO MAKE SPACE FOR NEW REFERENCES
					this.setChild(j+1, this.getChild(j));
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

	int insertPos = 0;
	public BPTreeInnerNode<TKey, TValue> splitInner(BPTreeInnerNode<TKey, TValue> oldInner){

		BPTreeInnerNode<TKey, TValue> newInnerLeft = new BPTreeInnerNode<TKey, TValue>(oldInner.m);
		BPTreeInnerNode<TKey, TValue> newInnerRight= new BPTreeInnerNode<TKey, TValue>(oldInner.m);
		BPTreeInnerNode<TKey, TValue> newInner = new BPTreeInnerNode<TKey, TValue>(oldInner.m);

			if (oldInner.parentNode == null){
				newInner.setKey(0, oldInner.getKey(oldInner.getKeyCount()/2));
				newInner.setChild(0, oldInner.getChild(oldInner.getKeyCount()/2));
				newInner.keyTally = 1;
				insertPos = 0;
			} else {
				BPTreeInnerNode<TKey, TValue> parent = (BPTreeInnerNode<TKey, TValue>)oldInner.parentNode;
				insertPos = parent.promote(oldInner.getKey(oldInner.getKeyCount()/2));
				newInner = parent;
			}

			for(int i = 0; i < oldInner.getKeyCount()/2; i++){
				newInnerLeft.setKey(i, oldInner.getKey(i));
				newInnerLeft.setChild(i, oldInner.getChild(i));
				newInnerLeft.keyTally++;
			}
			newInnerLeft.setChild(newInnerLeft.getKeyCount(), oldInner.getChild(oldInner.getKeyCount()/2));


			for(int i = oldInner.getKeyCount()/2+1; i < oldInner.getKeyCount(); i++){
				newInnerRight.setKey(newInnerRight.getKeyCount(),oldInner.getKey(i));
				newInnerRight.setChild(newInnerRight.getKeyCount(), oldInner.getChild(i));
				newInnerRight.keyTally++;
			}
			newInnerRight.setChild(newInnerRight.getKeyCount(), oldInner.getChild(oldInner.getKeyCount()-1).rightSibling);

			newInnerLeft.rightSibling = newInnerRight;
			newInnerRight.leftSibling = newInnerLeft;
			newInnerLeft.setParent(newInner);
			newInnerRight.setParent(newInner);

			if(insertPos-1 >= 0){
				newInner.getChild(insertPos-1).rightSibling = newInnerLeft;
				newInnerLeft.leftSibling = newInner.getChild(insertPos-1);
			}
			if(insertPos+2 < newInner.getKeyCount()){
				newInner.getChild(insertPos+2).leftSibling = newInnerRight;
				newInnerRight.rightSibling = newInner.getChild(insertPos+2);
			}

			if(oldInner.parentNode == null){
				newInner.setChild(0, newInnerLeft);
				newInner.setChild(1, newInnerRight);
			} else {
				newInner.setChild(insertPos, newInnerLeft);
				newInner.setChild(insertPos+1, newInnerRight);
			}

			if(newInner.getKeyCount() == m){
				newInner = splitInner(newInner);
			}
		
		
			return newInner;
		
	}

	public BPTreeNode<TKey, TValue> insert(TKey key, TValue value) 
	{
		int trav = 0;
		BPTreeInnerNode<TKey, TValue> Traverse = (BPTreeInnerNode<TKey, TValue>)this;
		Boolean complete = false;
		int pos = 0;
		while(! complete){
			for(int i = 0; i < Traverse.getKeyCount(); i++){
				if(key.compareTo(Traverse.getKey(i)) < 0){
					if(Traverse.getChild(i).isLeaf()){
						pos = i;
						complete = true;
						break;
					} else {
						Traverse = (BPTreeInnerNode<TKey, TValue>) Traverse.getChild(i);
						break;
					}
				}
				if(i+1 == Traverse.getKeyCount()){
					if(Traverse.getChild(i+1).isLeaf()){
						pos = i+1;
						complete = true;
						break;
					} else {
						Traverse = (BPTreeInnerNode<TKey, TValue>) Traverse.getChild(i+1);
						break;
					}
					
					
				}
			}
		}

		BPTreeLeafNode<TKey, TValue> InsertHere = (BPTreeLeafNode<TKey, TValue>)Traverse.getChild(pos);
		BPTreeNode<TKey, TValue> S = InsertHere.insert(key, value);
		if (S == InsertHere){
			return this;
		} else {
			if(S.getKeyCount() == m){
				S = splitInner((BPTreeInnerNode<TKey, TValue>)S);
			}
			while(S.getParent() != null){
				S = S.getParent();
			}
			return S;
		}

	}

	public BPTreeNode<TKey, TValue> delete(TKey key) 
	{
		BPTreeLeafNode<TKey, TValue> deleteMe = (BPTreeLeafNode<TKey, TValue>)getNode(key);
		if(deleteMe == null){ //key not in tree (at least not in a leaf)
			return this;
		}

		//Remove from the leaf
		for(int i = 0; i < deleteMe.getKeyCount(); i++){
			if(deleteMe.getKey(i).equals(key)){
				for(int j = i; j < deleteMe.getKeyCount(); j++){
					if(j+1 == deleteMe.getKeyCount())
					deleteMe.setKey(j, deleteMe.getKey(j+1));
					deleteMe.setValue(j, deleteMe.getValue(j+1));
				}
				deleteMe.keyTally--;
				break;
			}
		}

		//Check underflow
		if(deleteMe.getKeyCount() < m/2-1){
			BPTreeLeafNode<TKey, TValue> Left = (BPTreeLeafNode<TKey, TValue>)deleteMe.leftSibling;
			BPTreeInnerNode<TKey, TValue> Parent = (BPTreeInnerNode<TKey, TValue>)deleteMe.parentNode;
			BPTreeLeafNode<TKey, TValue> Right = (BPTreeLeafNode<TKey, TValue>)deleteMe.rightSibling;
			
			//1st check if left sibling can share
			if(deleteMe.leftSibling != null)
			if(deleteMe.leftSibling.getKeyCount() > m/2-1){
				for(int i = deleteMe.getKeyCount(); i >0 ; i--){ //Make space
					deleteMe.setKey(i, deleteMe.getKey(i-1));
					deleteMe.setValue(i, deleteMe.getValue(i-1));
				} //Copy key
				deleteMe.setKey(0, deleteMe.leftSibling.getKey(deleteMe.leftSibling.getKeyCount()-1));
				deleteMe.setValue(0, Left.getValue(deleteMe.leftSibling.getKeyCount()-1));
				deleteMe.keyTally++;

				//Add to parent
				for(int k = 0; k < Parent.getKeyCount(); k++){
					if(Parent.getKey(k).equals(key)){
						Parent.setKey(k, Left.getKey(deleteMe.leftSibling.getKeyCount()-1));
						break;
					}
				}
				//remove from left
				deleteMe.leftSibling.keyTally--;
				return this;
			}

			//2nd check if right sibling can share
			if(deleteMe.rightSibling != null)
			if(deleteMe.rightSibling.getKeyCount() > m/2-1){
				//Copy key
				deleteMe.setKey(deleteMe.getKeyCount(), deleteMe.rightSibling.getKey(0));
				deleteMe.setValue(deleteMe.getKeyCount(), Right.getValue(0));
				deleteMe.keyTally++;

				//remove from right
				for(int i = 0; i < deleteMe.getKeyCount() ; i++){ 
					deleteMe.rightSibling.setKey(i, deleteMe.rightSibling.getKey(i+1));
				}
				deleteMe.rightSibling.keyTally--;

				//Add to parent
				for(int k = 0; k < Parent.getKeyCount(); k++){
					if(Parent.getKey(k).equals(key)){
						Parent.setKey(k+1, deleteMe.rightSibling.getKey(0));
						break;
					}
				}
				return this;
			}

			//Merge left
			if(deleteMe.leftSibling != null){
				//Add right to left
				for(int i = 0; i < Right.getKeyCount(); i++){
					Left.setKey(Left.getKeyCount(), Right.getKey(i));
					Left.keyTally++;
				}
	
				//Remove from parent
				for(int i = 0; i < Parent.getKeyCount(); i++){
					if(Parent.getKey(i).equals(key)){
						for(int j = i; j < Parent.getKeyCount(); j++){
							Parent.setKey(j, Parent.getKey(j+1));
							Parent.setChild(j, Parent.getChild(j+1));
						}
						Parent.keyTally--;
					}
				}
				return Parent;

			} else {//Merge right

				if(deleteMe.rightSibling != null){
					//Make space
					for(int i = 0; i < deleteMe.getKeyCount(); i++){
						Right.setKey(deleteMe.getKeyCount()+i, Right.getKey(i));
					}

					//Add to right
					for (int i = 0; i < deleteMe.getKeyCount(); i++){
						Right.setKey(i, deleteMe.getKey(i));
						Right.keyTally++;
					}
		
					//Remove from parent
					for(int i = 0; i < Parent.getKeyCount(); i++){
						if(Parent.getKey(i).equals(key)){
							for(int j = i; j < Parent.getKeyCount(); j++){
								Parent.setKey(j, Parent.getKey(j+1));
								Parent.setChild(j, Parent.getChild(j+1));
							}
							Parent.keyTally--;
						}
					}
					return Parent;

			} 
		
		}

		
	}
	return this;
}



			
	

	

	public BPTreeNode<TKey, TValue> getNode(TKey key) 
	{
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
					return TraverseLeaf;
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



}