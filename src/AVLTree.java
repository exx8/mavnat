import java.util.Optional;

/**
 * AVLTree
 * <p>
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 */

public class AVLTree {
	private IAVLNode root;

	//region private methods

	/**
	 * Rebalances a node after insertion to the tree.
	 *
	 * @param node the node to rebalance
	 * @return the number of rebalances that occurred
	 */
	private int rebalance(IAVLNode node) {

		Rotations rotations = new Rotations();

		int amount = 0;
		IAVLNode parent = node.getParent();
		if (parent != null) {
			final boolean nodeOnLeftOfParent = parent.getLeft() == node;
			IAVLNode otherChild = nodeOnLeftOfParent ? parent.getRight() : parent.getLeft();
			final boolean nodeNParentSameHeight = parent.getHeight() - node.getHeight() == 0;
			if (nodeNParentSameHeight) {
				amount++;
				if (parent.getHeight() - otherChild.getHeight() == 1) {
					//case 1: promote
					amount = promoteParent(amount, parent);
				} else if (nodeOnLeftOfParent && node.getHeight() - node.getLeft().getHeight() == 1) {
					//case 2 of left child: rotate right
					rotations.rotateRight(node, this.getRoot());
				} else if (parent.getRight() == node && node.getHeight() - node.getRight().getHeight() == 1) {
					//case 2 of right child: rotate left
					rotations.rotateLeft(node, this.getRoot());
				} else if (nodeOnLeftOfParent && node.getHeight() - node.getLeft().getHeight() == 2) {
					//case 3 of left child: rotate left then right
					amount = rotations.rotateLeftNRight(node, amount, this.getRoot());
				} else {
					//case 3 of right child: rotate right then left
					amount = rotations.rotateRightNLeft(node, amount, this.getRoot());
				}
			}
			//otherwise, parent is not a leaf and no rebalancing is needed
		}
		return amount;
	}


	protected int promoteParent(int amount, IAVLNode parent) {
		parent.setHeight(parent.getHeight() + 1);
		amount += rebalance(parent);
		return amount;
	}


	void setRoot(IAVLNode node) {
		root = node;
	}

	protected void updateParents(IAVLNode node, IAVLNode parent, IAVLNode leftChild) {
		node.setParent(parent.getParent());
		parent.setParent(node);
		leftChild.setParent(parent);
	}

	protected class Rotations {
		/**
		 * Perform a left rotation
		 *
		 * @param node the node to rotate
		 */
		private void rotateLeft(IAVLNode node, IAVLNode tRoot) {
			IAVLNode parent = node.getParent();
			IAVLNode leftChild = node.getLeft();
			//update children
			if (parent == tRoot) {
				setRoot(node);
			} else {
				IAVLNode parentParent = parent.getParent();
				if (parentParent.getLeft() == parent) {
					parentParent.setLeft(node);
				} else {
					parentParent.setRight(node);
				}
			}
			node.setLeft(parent);
			parent.setRight(leftChild);

			//update parents
			updateParents(node, parent, leftChild);

			//update heights
			node.setHeight(parent.getHeight());
			parent.setHeight(Math.max(parent.getLeft().getHeight(), parent.getRight().getHeight()) + 1);
		}

		/**
		 * Perform a right rotation
		 *
		 * @param node the node to rotate
		 */
		void rotateRight(IAVLNode node, IAVLNode tRoot) {
			IAVLNode parent = node.getParent();
			IAVLNode rightChild = node.getRight();
			//update children
			if (parent == tRoot) {
				setRoot(node);
			} else {
				IAVLNode parentParent = parent.getParent();
				if (parentParent.getLeft() == parent) {
					parentParent.setLeft(node);
				} else {
					parentParent.setRight(node);
				}
			}
			node.setRight(parent);
			parent.setLeft(rightChild);

			//update parents
			updateParents(node, parent, rightChild);

			//update heights
			node.setHeight(parent.getHeight());
			parent.setHeight(Math.max(parent.getLeft().getHeight(), parent.getRight().getHeight()) + 1);
		}

		protected int rotateRightNLeft(IAVLNode node, int amount, IAVLNode tRoot) {
			IAVLNode leftChild = node.getLeft();
			rotateRight(leftChild, tRoot);
			rotateLeft(leftChild, tRoot);
			amount++;
			return amount;
		}

		protected int rotateLeftNRight(IAVLNode node, int amount, IAVLNode tRoot) {
			IAVLNode rightChild = node.getRight();
			rotateLeft(rightChild, tRoot);
			rotateRight(rightChild, tRoot);
			amount++;
			return amount;
		}
	}
	//endregion

	/**
	 * public boolean empty()
	 * <p>
	 * returns true if and only if the tree is empty
	 */
	public boolean empty() {
		return false; // to be replaced by student code
	}

	/**
	 * public String search(int k)
	 * <p>
	 * returns the info of an item with key k if it exists in the tree
	 * otherwise, returns null
	 */
	public String search(int k) {
		return "42";  // to be replaced by student code
	}

	/**
	 * public int insert(int k, String i)
	 * <p>
	 * inserts an item with key k and info i to the AVL tree.
	 * the tree must remain valid (keep its invariants).
	 * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
	 * returns -1 if an item with key k already exists in the tree.
	 */
	public int insert(int k, String i) {
		IAVLNode node = new AVLNode(k, i);
		node.setLeft(new AVLNode(0, "", node, false));
		node.setRight(new AVLNode(0, "", node, false));
		int rebalances = 0;
		if (getRoot() == null) {
			setRoot(node);
		} else {
			IAVLNode currNode = getRoot();
			while (currNode.isRealNode()) {
				if (k < currNode.getKey()) {
					currNode = currNode.getLeft();
				} else if (k > currNode.getKey()) {
					currNode = currNode.getRight();
				} else {
					return -1;
				}
			}
			if (currNode.getParent().getLeft() == currNode) {
				currNode.getParent().setLeft(node);
			} else {
				currNode.getParent().setRight(node);
			}
			node.setParent(currNode.getParent());
			//TODO: should promotions also count as rebalances?
			rebalances = rebalance(node);
		}
		return rebalances;
	}

	/**
	 * public int delete(int k)
	 * <p>
	 * deletes an item with key k from the binary tree, if it is there;
	 * the tree must remain valid (keep its invariants).
	 * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
	 * returns -1 if an item with key k was not found in the tree.
	 */
	public int delete(int k) {

		final Optional<IAVLNode> place2Delete=findPlace(k);
		if(!place2Delete.isPresent())
			return -1;
		final IAVLNode deletedRoot = place2Delete.get();
		final IAVLNode parentOfDeletedRoot = deletedRoot.getParent();
		final boolean isRootLeftChild=deletedRoot.getKey()< parentOfDeletedRoot.getKey();
		final IAVLNode rightChild = deletedRoot.getRight();
		final Optional<IAVLNode> optionalnextRoot=findMin(rightChild);
		if(!optionalnextRoot.isPresent()) {
			final IAVLNode deletedRootLeftChild = deletedRoot.getLeft();

			if(isRootLeftChild) {
				parentOfDeletedRoot.setLeft(deletedRootLeftChild);
			}
			else
			{
				parentOfDeletedRoot.setRight(deletedRootLeftChild);
			}
			deletedRootLeftChild.setParent(parentOfDeletedRoot);
				return rebalance(parentOfDeletedRoot);
		}
		else
		{
			final IAVLNode nextRoot = optionalnextRoot.get();
			final IAVLNode nextCurrentRootParent = nextRoot.getParent();
			final IAVLNode nextCurrentRootLeftChild = nextRoot.getLeft();
			nextCurrentRootParent.setLeft(nextCurrentRootLeftChild);

			nextRoot.setParent(parentOfDeletedRoot);
			nextRoot.setLeft(deletedRoot.getLeft());
			nextRoot.setRight(deletedRoot.getRight());


		}


		return 42;
	}

	protected static Optional<IAVLNode> findMin(IAVLNode node) {
		if (!node.isRealNode())
			return Optional.empty();
		if (!node.getLeft().isRealNode())
			return Optional.of(node);
		return findMin(node.getLeft());
	}
	protected Optional<IAVLNode> findPlace(int key) {
		return findPlace(key,this.root);
	}

	protected static Optional<IAVLNode> findPlace(int key, IAVLNode currentNode) {
		if (!currentNode.isRealNode())
			return Optional.empty();
		final int currentNodeKey = currentNode.getKey();
		if (currentNodeKey == key)
			return Optional.of(currentNode);
		else if (currentNodeKey < key)

			return findPlace(key, currentNode.getLeft());

		else
			return findPlace(key, currentNode.getRight());

	}


	/**
	 * public String min()
	 * <p>
	 * Returns the info of the item with the smallest key in the tree,
	 * or null if the tree is empty
	 */
	public String min() {
		return "42"; // to be replaced by student code
	}

	/**
	 * public String max()
	 * <p>
	 * Returns the info of the item with the largest key in the tree,
	 * or null if the tree is empty
	 */
	public String max() {
		return "42"; // to be replaced by student code
	}

	/**
	 * public int[] keysToArray()
	 * <p>
	 * Returns a sorted array which contains all keys in the tree,
	 * or an empty array if the tree is empty.
	 */
	public int[] keysToArray() {
		int[] arr = new int[42]; // to be replaced by student code
		return arr;              // to be replaced by student code
	}

	/**
	 * public String[] infoToArray()
	 * <p>
	 * Returns an array which contains all info in the tree,
	 * sorted by their respective keys,
	 * or an empty array if the tree is empty.
	 */
	public String[] infoToArray() {
		String[] arr = new String[42]; // to be replaced by student code
		return arr;                    // to be replaced by student code
	}

	/**
	 * public int size()
	 * <p>
	 * Returns the number of nodes in the tree.
	 * <p>
	 * precondition: none
	 * postcondition: none
	 */
	public int size() {
		return 42; // to be replaced by student code
	}

	/**
	 * public int getRoot()
	 * <p>
	 * Returns the root AVL node, or null if the tree is empty
	 * <p>
	 * precondition: none
	 * postcondition: none
	 */
	public IAVLNode getRoot() {
		return root;
	}

	/**
	 * public string split(int x)
	 * <p>
	 * splits the tree into 2 trees according to the key x.
	 * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
	 * precondition: search(x) != null
	 * postcondition: none
	 */
	public AVLTree[] split(int x) {
		return null;
	}

	/**
	 * public join(IAVLNode x, AVLTree t)
	 * <p>
	 * joins t and x with the tree.
	 * Returns the complexity of the operation (rank difference between the tree and t)
	 * precondition: keys(x,t) < keys() or keys(x,t) > keys()
	 * postcondition: none
	 */
	public int join(IAVLNode x, AVLTree t) {
		return 0;
	}

	/**
	 * public interface IAVLNode
	 * ! Do not delete or modify this - otherwise all tests will fail !
	 */
	public interface IAVLNode {
		public int getKey(); //returns node's key (for virtuval node return -1)

		public String getValue(); //returns node's value [info] (for virtuval node return null)

		public void setLeft(IAVLNode node); //sets left child

		public IAVLNode getLeft(); //returns left child (if there is no left child return null)

		public void setRight(IAVLNode node); //sets right child

		public IAVLNode getRight(); //returns right child (if there is no right child return null)

		public void setParent(IAVLNode node); //sets parent

		public IAVLNode getParent(); //returns the parent (if there is no parent return null)

		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node

		public void setHeight(int height); // sets the height of the node

		public int getHeight(); // Returns the height of the node (-1 for virtual nodes)
	}

	/**
	 * public class AVLNode
	 * <p>
	 * If you wish to implement classes other than AVLTree
	 * (for example AVLNode), do it in this file, not in
	 * another file.
	 * This class can and must be modified.
	 * (It must implement IAVLNode)
	 */
	public class AVLNode implements IAVLNode {
		private int key;
		private String value;
		private IAVLNode left;
		private IAVLNode right;
		private IAVLNode parent;
		private boolean realNode;
		private int height;

		public AVLNode(int key, String value) {
			this(key, value, null, true);
		}

		public AVLNode(int key, String value, IAVLNode parent, boolean realNode) {
			this.key = key;
			this.value = value;
			this.parent = parent;
			this.realNode = realNode;
			if (!realNode) {
				height = -1;
			}
		}

		public int getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}

		public void setLeft(IAVLNode node) {
			left = node;
		}

		public IAVLNode getLeft() {
			return left;
		}

		public void setRight(IAVLNode node) {
			right = node;
		}

		public IAVLNode getRight() {
			return right;
		}

		public void setParent(IAVLNode node) {
			parent = node;
		}

		public IAVLNode getParent() {
			return parent;
		}

		// Returns True if this is a non-virtual AVL node
		public boolean isRealNode() {
			return realNode;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public int getHeight() {
			return height;
		}
	}

}
  

