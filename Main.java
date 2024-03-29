public class Main {
	

    public static void main(String[] args) 
    {
	//General tree
	BPTree<Integer, Integer> tree = new BPTree<Integer, Integer>(4); // A B+ Tree with order 4
   
	tree.insert(29, 5000);
	tree.insert(57, 1000);
	tree.insert(41, 9000);
	tree.insert(20, 8000);
	tree.insert(21, 2000);
	tree.insert(55, 3000);
	tree.insert(70, 6000);
	tree.insert(80, 4000);
	tree.insert(50, 81230);
	tree.insert(661, 20421400);
	tree.insert(15, 3021400);
	tree.insert(40, 6001240);
	tree.insert(410, 4045100);
	tree.insert(510, 8035100);
	tree.insert(6651, 200140);
	tree.insert(151, 30041240);
	tree.insert(450, 605500);
	tree.insert(45, 40100); 

	tree.printInorder();

	System.out.println("Structure of the constucted tree is : ");
	tree.print();

	Integer value = 70; // Delete leaf also internal
	tree.delete(value);
	System.out.println("Structure of the tree after delete of : " + value);
	tree.print();

	value = 60; // Delete leaf
	tree.delete(value);
	System.out.println("Structure of the tree after delete of : " + value);
	tree.print();

	value = 50; // Delete leaf, underflow, borrow left
	tree.delete(value);
	System.out.println("Structure of the tree after delete of : " + value);
	tree.print();

	value = 40; // Delete leaf, underflow, borrow right
	tree.delete(value);
	System.out.println("Structure of the tree after delete of : " + value);
	tree.print();

	System.out.println("Search the tree for 80: ");
    	Integer result = (Integer)tree.search(80);
	if (result != null)
		System.out.println("Found key with value " + result);
	else
		System.out.println("Key not found!");

	System.out.println("Search the tree for 100: ");
    	result = (Integer)tree.search(100);
	if (result != null)
		System.out.println("Found key with value " + result);
	else
		System.out.println("Key not found!");

	System.out.println("Search the tree for 40: ");
	result = (Integer)tree.search(40);
	if (result != null)
		System.out.println("Found key with value " + result);
	else
		System.out.println("Key not found!");


	/*
	// DB student table indexes
	BPTree<Integer, Integer> pktree = new BPTree<Integer, Integer>(4); // A B+ Tree with order 4
	pktree.insert(16094340, 100);
	pktree.insert(16230943, 200);
	pktree.insert(17012340, 300);
	pktree.insert(17248830, 400);
	System.out.println();
	System.out.println("Structure of the constucted index is : ");
	pktree.print();
	
	Integer studentid = 17248830;
	System.out.println("Search the index tree for student: " + studentid);
	result = (Integer)pktree.search(studentid);
	if (result != null)
		System.out.println("Found key with value " + result);
	else
		System.out.println("Key not found!");

	BPTree<String, Integer> sktree = new BPTree<String, Integer>(4); // A B+ Tree with order 4
	sktree.insert("Botha", 100);
	sktree.insert("Molefe", 200);
	sktree.insert("Evans", 300);
	sktree.insert("Muller", 400);
	System.out.println();
	System.out.println("Structure of the constucted index is : ");
	sktree.print();
	
	String surname = "Botha";
	System.out.println("Search the index tree for student: " + surname);
	result = (Integer)sktree.search(surname);
	if (result != null)
		System.out.println("Found key with value " + result);
	else
		System.out.println("Key not found!");
*/
	/* Expected Output:
	Structure of the constucted tree is :
	Level 1 [ 30 50 70]
	Level 2 [ 10 20]
	Level 2 [ 30 40]
	Level 2 [ 50 60]
	Level 2 [ 70 80 90]

	Structure of the tree after delete of : 70
	Level 1 [ 30 50 70]
	Level 2 [ 10 20]
	Level 2 [ 30 40]
	Level 2 [ 50 60]
	Level 2 [ 80 90]

	Structure of the tree after delete of : 60
	Level 1 [ 30 50 70]
	Level 2 [ 10 20]
	Level 2 [ 30 40]
	Level 2 [ 50]
	Level 2 [ 80 90]

	Structure of the tree after delete of : 50
	Level 1 [ 30 40 70]
	Level 2 [ 10 20]
	Level 2 [ 30]
	Level 2 [ 40]
	Level 2 [ 80 90]

	Structure of the tree after delete of : 40
	Level 1 [ 30 40 90]
	Level 2 [ 10 20]
	Level 2 [ 30]
	Level 2 [ 80]
	Level 2 [ 90]

	Search the tree for 80:
	Found key with value 4000
	Search the tree for 100:
	Key not found!
	Search the tree for 40:
	Key not found!

	Structure of the constucted index is :
	Level 1 [ 17012340]
	Level 2 [ 16094340 16230943]
	Level 2 [ 17012340 17248830]

	Search the index tree for student: 17248830
	Found key with value 400

	Structure of the constucted index is :
	Level 1 [ Molefe]
	Level 2 [ Botha Evans]
	Level 2 [ Molefe Muller]

	Search the index tree for student: Botha
	Found key with value 100
	*/
    }


    
}