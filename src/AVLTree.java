import java.util.Arrays;
import java.util.Optional;

/**
 * AVLTree
 * <p>
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 */

public class AVLTree {

	protected IAVLNode root;
	private Rotations rotations = new Rotations();
	private InsertionBalancer insertionBalancer = new InsertionBalancer();
	private DeletionBalancer deletionBalancer = new DeletionBalancer();
	protected IAVLNode min;
	protected IAVLNode max;

	//region private methods

	/**
	 * Find the leftmost or rightmost subtree with a maximum height
	 * <p>
	 * precondition: a node with height smaller or equal to maxHeight exists
	 * </p>
	 *complexity: O(log n)
	 *
	 * @param left      the side of the tree to go down to
	 * @param maxHeight the maximum height of the subtree
	 * @return the root of the subtree
	 */
	private IAVLNode findSubtreeByHeight(boolean left, int maxHeight) {
		IAVLNode node = getRoot();
		while (node.getHeight() > maxHeight) {
			//find the first node with height<=maxHeight
			if (left) {
				node = node.getLeft();
			} else {
				node = node.getRight();
			}
		}
		return node;
	}

	/**
	 * Inserts a node to the AVL tree
	 *O(log n)
	 *
	 * @param node the node to insert
	 * @return the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
	 * returns -1 if an item with key k already exists in the tree.
	 */
	private int insertNode(IAVLNode node) {
		node.setFakeLeft();
		node.setFakeRight();
		int k = node.getKey();
		int rebalances = 0;
		if (getRoot() == null) {
			root = node;
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
			updateSizeToRoot(node.getParent());
			rebalances = insertionBalancer.rebalance(node);
		}
		//update min and max
		if (min == null || node.getKey() < min.getKey()) {
			min = node;
		}
		if (max == null || node.getKey() > max.getKey()) {
			max = node;
		}

		return rebalances;
	}

	/**
	 * Recursively fill an array with an in-order scan of the tree's nodes
	 * complexity: O(n)
	 *
	 * @param node  the current node (should be the root for the first call)
	 * @param arr   the array to fill with in-order nodes (should be empty for the first call)
	 * @param index the current index in the array (should be 0 for the first call)
	 */
	private void inorderScan(IAVLNode node, IAVLNode[] arr, int index) {
		if (node != null && node.isRealNode()) {
			inorderScan(node.getLeft(), arr, index);
			index += node.getLeft().getSize();
			arr[index] = node;
			inorderScan(node.getRight(), arr, index + 1);
		}
	}

	/**
	 * Find the successor of a node with a right child
	 *complexity: O(log n)
	 *
	 * @param node AVL node
	 * @return the successor
	 */
	private IAVLNode findChildSuccessor(IAVLNode node) {
		IAVLNode successor = node.getRight();
		while (successor.getLeft().isRealNode()) {
			successor = successor.getLeft();
		}
		return successor;
	}

	/**
	 * Delete a node by replacing it with its successor and deleting the successor instead
	 *complexity: O(log n)
	 *
	 * @param node
	 * @return the parent of the successor
	 */
	private IAVLNode deleteSuccessor(IAVLNode node) {
		IAVLNode successor = findChildSuccessor(node);
		IAVLNode successorParent = successor.getParent();
		if (successorParent == node) {
			//if the successor is the direct child of node, it will be similar to direct deletion
			successorParent = successor;
		}
		//delete the successor
		deleteDirectly(successor);
		//replace node with its successor
		successor.setLeft(node.getLeft());
		node.getLeft().setParent(successor);
		successor.setRight(node.getRight());
		node.getRight().setParent(successor);
		successor.setHeight(node.getHeight());
		successor.setSize(node.getSize());

		IAVLNode parent = node.getParent();
		if (parent != null) {
			if (parent.getRight() == node) {
				parent.setRight(successor);
			} else {
				parent.setLeft(successor);
			}
		} else {
			//the root node was deleted
			root = successor;
		}
		successor.setParent(parent);

		return successorParent;
	}

	/**
	 * Delete a node with one or zero children
	 *complexity: O(1)
	 *
	 * @param node
	 */
	private void deleteDirectly(IAVLNode node) {
		//the child to connect to the deleted node's parent
		IAVLNode replacementNode = node.getLeft().isRealNode() ? node.getLeft() : node.getRight();
		IAVLNode parent = node.getParent();
		if (parent != null) {
			if (parent.getRight() == node) {
				parent.setRight(replacementNode);
			} else {
				parent.setLeft(replacementNode);
			}
		} else {
			//the root node was deleted
			root = replacementNode.isRealNode() ? replacementNode : null;
		}
		replacementNode.setParent(parent);
	}

	/**
	 * Find a node in the tree by its key
	 *complexity: O(log n)
	 *
	 * @param key the key of the searched node
	 * @return AVL node
	 */
	protected Optional<IAVLNode> findNodeByKey(int key) {
		return findNodeByKey(key, this.root);
	}

	/**
	 * Find a node in a subtree by its key
	 *complexity: O(log n)
	 *
	 * @param key         the key of the searched node
	 * @param currentNode the parent of the subtree to search
	 * @return AVL node
	 */
	protected Optional<IAVLNode> findNodeByKey(int key, IAVLNode currentNode) {
		if (currentNode == null)
			return Optional.empty();
		if (!currentNode.isRealNode())
			return Optional.empty();
		final int currentNodeKey = currentNode.getKey();
		if (currentNodeKey == key)
			return Optional.of(currentNode);
		else if (currentNodeKey > key)
			return findNodeByKey(key, currentNode.getLeft());
		else
			return findNodeByKey(key, currentNode.getRight());
	}

	/**
	 * Update the minimum field
	 *complexity: O(log n)
	 */
	protected void updateMin() {
		if (empty()) {
			min = null;
		} else {
			IAVLNode node = root;
			while (node.getLeft().isRealNode()) {
				node = node.getLeft();
			}
			min = node;
		}
	}

	/**
	 * Update the maximum field
 	 *complexity: O(log n)
	 */
	protected void updateMax() {
		if (empty()) {
			max = null;
		} else {
			IAVLNode node = root;
			while (node.getRight().isRealNode()) {
				node = node.getRight();
			}
			max = node;
		}
	}

	/**
	 * Update the size of this node and its descendents after a change in size happened to a child
	 *complexity: O(log n)
	 *
	 * @param node AVL node
	 */
	private void updateSizeToRoot(IAVLNode node) {
		if (node != null) {
			node.setSize(node.getLeft().getSize() + node.getRight().getSize() + 1);
			updateSizeToRoot(node.getParent());
		}
	}

	//endregion

	/**
	 * public boolean empty()
	 * <p>
	 *complexity: O(1)
	 *
	 * returns true if and only if the tree is empty
	 */
	public boolean empty() {
		return getRoot() == null;
	}

	/**
	 * public String search(int k)
	 * <p>
	 *complexity: O(log n)
	 *
	 * returns the info of an item with key k if it exists in the tree
	 * otherwise, returns null
	 */
	public String search(int k) {
		IAVLNode node = root;
		while (node != null && node.isRealNode()) {
			if (node.getKey() == k) {
				return node.getValue();
			} else if (node.getKey() > k) {
				node = node.getLeft();
			} else {
				node = node.getRight();
			}
		}

		return null;
	}

	/**
	 * public int insert(int k, String i)
	 * <p>
	 *complexity: O(log n)
	 *
	 * inserts an item with key k and info i to the AVL tree.
	 * the tree must remain valid (keep its invariants).
	 * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
	 * returns -1 if an item with key k already exists in the tree.
	 */
	public int insert(int k, String i) {
		IAVLNode node = new AVLNode(k, i);

		return insertNode(node);
	}

	/**
	 * public int delete(int k)
	 * <p>
	 *complexity: O(log n)
	 *
	 * deletes an item with key k from the binary tree, if it is there;
	 * the tree must remain valid (keep its invariants).
	 * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
	 * returns -1 if an item with key k was not found in the tree.
	 */
	public int delete(int k) {
		final Optional<IAVLNode> placeToDelete = findNodeByKey(k);
		//return -1 if the key was not found
		if (!placeToDelete.isPresent())
			return -1;

		final IAVLNode deletedNode = placeToDelete.get();
		final boolean hasTwoChildren = deletedNode.getLeft().isRealNode() && deletedNode.getRight().isRealNode();

		//the parent of the node that was actually deleted
		//in the case of deletion of a successor, it will be the successor's parent
		IAVLNode deletedNodeParent;
		if (!hasTwoChildren) {
			deletedNodeParent = deletedNode.getParent();
			deleteDirectly(deletedNode);
		} else {
			deletedNodeParent = deleteSuccessor(deletedNode);
		}
		updateSizeToRoot(deletedNodeParent);
		int amount = deletionBalancer.rebalance(deletedNodeParent);
		if (deletedNode == min) {
			updateMin();
		}
		if (deletedNode == max) {
			updateMax();
		}
		return amount;
	}

	/**
	 * public String min()
	 * <p>
	 * Returns the info of the item with the smallest key in the tree,
	 * or null if the tree is empty
	 **complexity: O(1)
	 */
	public String min() {
		return min != null ? this.min.getValue() : null;
	}

	/**
	 * public String max()
	 *complexity: O(1)
	 *
	 * <p>
	 * Returns the info of the item with the largest key in the tree,
	 * or null if the tree is empty
	 */
	public String max() {
		return max != null ? this.max.getValue() : null;
	}

	/**
	 * public int[] keysToArray()
	 * <p>
	 **complexity: O(n)
	 * Returns a sorted array which contains all keys in the tree,
	 * or an empty array if the tree is empty.
	 */
	public int[] keysToArray() {
		IAVLNode[] arr = new IAVLNode[size()];
		inorderScan(getRoot(), arr, 0);
		return Arrays.stream(arr).mapToInt(n -> n.getKey()).toArray();
	}

	/**
	 * public String[] infoToArray()
	 * <p>
	 *complexity: O(n)
	 * Returns an array which contains all info in the tree,
	 * sorted by their respective keys,
	 * or an empty array if the tree is empty.
	 */
	public String[] infoToArray() {
		IAVLNode[] arr = new IAVLNode[size()];
		inorderScan(getRoot(), arr, 0);
		return Arrays.stream(arr).map(n -> n.getValue()).toArray(String[]::new);
	}

	/**
	 * public int size()
	 * <p>
	 **complexity: O(1)
	 * Returns the number of nodes in the tree.
	 * <p>
	 * precondition: none
	 * postcondition: none
	 */
	public int size() {
		return empty() ? 0 : root.getSize();
	}

	/**
	 * public int getRoot()
	 * <p>
	 *complexity: O(1)
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
	 *complexity: O(log n)
	 * splits the tree into 2 trees according to the key x.
	 * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
	 * precondition: search(x) != null
	 * postcondition: none
	 */
	public AVLTree[] split(int x) {
		AVLTree greaterTree = new AVLTree();
		AVLTree smallerTree = new AVLTree();

		IAVLNode node = findNodeByKey(x).get();
		greaterTree.root = node.getRight().isRealNode() ? node.getRight() : null;
		node.getRight().setParent(null);

		smallerTree.root = node.getLeft().isRealNode() ? node.getLeft() : null;
		node.getLeft().setParent(null);

		node = node.getParent();
		// travel until we get to the root, while joining the left or right subtrees to the split trees
		while (node != null) {
			IAVLNode parent = node.getParent();
			//disconnect node from tree
			node.setParent(null);
			node.setHeight(0);
			node.setSize(1);

			AVLTree tree = new AVLTree();
			if (x > node.getKey()) {
				tree.root = node.getLeft().isRealNode() ? node.getLeft() : null;
				node.getLeft().setParent(null);
				node.setFakeLeft();
				node.setFakeRight();
				smallerTree.join(node, tree);
			} else {
				tree.root = node.getRight().isRealNode() ? node.getRight() : null;
				node.getRight().setParent(null);
				node.setFakeLeft();
				node.setFakeRight();
				greaterTree.join(node, tree);
			}
			node = parent;
		}

		//set min and max
		if (x != min.getKey()) {
			smallerTree.min = min;
		}
		if (x != max.getKey()) {
			greaterTree.max = max;
		}
		smallerTree.updateMax();
		greaterTree.updateMin();

		return new AVLTree[]{smallerTree, greaterTree};
	}

	/**
	 * public join(IAVLNode x, AVLTree t)
	 * <p>
	 * joins t and x with the tree.
	 *complexity:O(delta-h) or O(n) (whereas delta-h is the height difference between the 2 trees)
	 * Returns the complexity of the operation (rank difference between the tree and t)
	 * precondition: keys(x,t) < keys() or keys(x,t) > keys()
	 * postcondition: none
	 */
	public int join(IAVLNode x, AVLTree t) {
		int complexity = Math.abs(getHeight() - t.getHeight()) + 1;
		if (empty() || t.empty()) {
			if (empty()) {
				root = t.getRoot();
				//update min and max
				min = t.min;
				max = t.max;
			}
			insertNode(x);
		} else {
			//both trees have a root node
			boolean isLarger = getRoot().getHeight() > t.getRoot().getHeight();
			AVLTree largerTree = isLarger ? this : t;
			AVLTree smallerTree = isLarger ? t : this;
			IAVLNode joinNode; //the first large tree's node with height<=small tree's height
			if (x.getKey() < largerTree.getRoot().getKey()) {
				//the key of x is smaller than the large tree's keys
				joinNode = largerTree.findSubtreeByHeight(true, smallerTree.getHeight());
				x.setLeft(smallerTree.getRoot());
				x.setRight(joinNode);
				if (joinNode.getParent() != null) {
					joinNode.getParent().setLeft(x);
				}
				//update min and max
				min = smallerTree.min;
				max = largerTree.max;
			} else {
				//the key of x is bigger than the large tree's keys
				joinNode = largerTree.findSubtreeByHeight(false, smallerTree.getHeight());
				x.setRight(smallerTree.getRoot());
				x.setLeft(joinNode);
				if (joinNode.getParent() != null) {
					joinNode.getParent().setRight(x);
				}
				//update min and max
				min = largerTree.min;
				max = smallerTree.max;
			}
			x.setParent(joinNode.getParent());
			updateSizeToRoot(x);
			joinNode.setParent(x);
			smallerTree.root.setParent(x);
			x.setHeight(smallerTree.getHeight() + 1);
			if (joinNode == largerTree.getRoot()) {
				//if the heights of both tree are identical, x will become their new root
				root = x;
			} else if (!isLarger) {
				//if this tree's height is smaller than t's height, its root should change to be t's root
				root = largerTree.root;
			}
			insertionBalancer.rebalance(x);
		}
		return complexity;
	}

	/**
	 * Get the height of the tree
	 *complexity: O(1)
	 * @return height
	 */
	public int getHeight() {
		if (getRoot() != null) {
			return getRoot().getHeight();
		} else {
			return -1;
		}
	}

	/**
	 * public interface IAVLNode
	 * ! Do not delete or modify this - otherwise all tests will fail !
	 */
	public interface IAVLNode {
		public int getKey(); //returns node's key (for virtuval node return -1)

		public String getValue(); //returns node's value [info] (for virtuval node return null)

		public void setLeft(IAVLNode node); //sets left child

		public void setFakeLeft(); //creates a none-real node to be the left child

		public IAVLNode getLeft(); //returns left child (if there is no left child return null)

		public void setRight(IAVLNode node); //sets right child

		public void setFakeRight(); //creates a none-real node to be the right child

		public IAVLNode getRight(); //returns right child (if there is no right child return null)

		public void setParent(IAVLNode node); //sets parent

		public IAVLNode getParent(); //returns the parent (if there is no parent return null)

		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node

		public void setHeight(int height); // sets the height of the node

		public int getHeight(); // Returns the height of the node (-1 for virtual nodes)

		/**
		 * Get the number of nodes in this node's subtree
		 *
		 * @return size
		 */
		int getSize();

		/**
		 * Set the number of nodes in this node's subtree
		 *
		 * @param size amount of nodes in subtree
		 */
		void setSize(int size);

		/**
		 * Increases height by 1
		 *
		 * @return the time complexity of the action
		 */
		int promote();

		/**
		 * Decreases height by 1
		 *
		 * @return the time complexity of the action
		 */
		int demote();
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
		@Override
		public String toString() {
			return key + " " + value;
		}

		private int key;
		private String value;
		private IAVLNode left;
		private IAVLNode right;
		private IAVLNode parent;
		private boolean realNode;
		private int height;
		private int size = 1;

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
				size = 0;
				this.key = -1;
			}
		}

		private IAVLNode createFakeChild() {
			return new AVLNode(0, "", this, false);
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

		public void setFakeLeft() {
			setLeft(createFakeChild());
		}

		public IAVLNode getLeft() {
			return left;
		}

		public void setRight(IAVLNode node) {
			right = node;
		}

		public void setFakeRight() {
			setRight(createFakeChild());
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

		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}

		public int promote() {
			setHeight(getHeight() + 1);
			return 1;
		}

		public int demote() {
			setHeight(getHeight() - 1);
			return 1;
		}
	}

	class Rotations {

		/**
		 * Perform a left rotation
		 *complexity: O(1)
		 * @param node the node to rotate
		 * @return time complexity
		 */
		public int rotateLeft(IAVLNode node) {
			IAVLNode parent = node.getParent();
			IAVLNode leftChild = node.getLeft();
			//update children
			if (parent == getRoot()) {
				root = node;
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
			updateParents(parent, node, leftChild);
			//updates sizes
			updateSizes(parent, node);
			return 1;
		}

		/**
		 * Perform a right rotation
		 *complexity: O(1)
		 *
		 * @param node the node to rotate
		 * @return time complexity
		 */
		public int rotateRight(IAVLNode node) {
			IAVLNode parent = node.getParent();
			IAVLNode rightChild = node.getRight();
			//update children
			if (parent == getRoot()) {
				root = node;
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
			updateParents(parent, node, rightChild);
			//updates sizes
			updateSizes(parent, node);
			return 1;
		}

		/**
		 * Perform a double rotation of left and right
		 *complexity: O(1)
		 *
		 * @param node the node to double rotate
		 * @return time complexity
		 */
		public int rotateLeftNRight(IAVLNode node) {
			int actions = 0;
			actions += rotateLeft(node);
			actions += rotateRight(node);
			return actions;
		}

		/**
		 * Perform a double rotation of right and left
		 *complexity: O(1)
		 *
		 * @param node the node to double rotate
		 * @return time complexity
		 */
		public int rotateRightNLeft(IAVLNode node) {
			int actions = 0;
			actions += rotateRight(node);
			actions += rotateLeft(node);
			return actions;
		}

		/**
		 * Update the parent field of nodes involved in a rotation
		 *complexity: O(1)
		 *
		 * @param parent     the parent of the rotated node
		 * @param node       the rotated node
		 * @param nodesChild the rotated node's child (left child for left rotation, right child for right rotation)
		 */
		private void updateParents(IAVLNode parent, IAVLNode node, IAVLNode nodesChild) {
			node.setParent(parent.getParent());
			parent.setParent(node);
			if (nodesChild != null)
				nodesChild.setParent(parent);
		}

		/**
		 * Update the sizes of nodes involved in a rotation
		 *complexity: O(1)
		 * @param parent the parent of the rotated node
		 * @param node   the rotated node
		 */
		private void updateSizes(IAVLNode parent, IAVLNode node) {
			node.setSize(parent.getSize());
			parent.setSize(parent.getLeft().getSize() + parent.getRight().getSize() + 1);
		}
	}

	class InsertionBalancer {
		/**
		 * Rebalances a node after insertion to the tree.
		 *complexity: O(log n)
		 * @param node the node to rebalance
		 * @return the number of rebalances that occurred
		 */
		public int rebalance(IAVLNode node) {
			int amount = 0;
			IAVLNode parent = node.getParent();
			if (parent != null) {
				IAVLNode otherChild = parent.getLeft() == node ? parent.getRight() : parent.getLeft();
				int parentDif = parent.getHeight() - node.getHeight(); //height difference from parent
				int parentOtherDif = parent.getHeight() - otherChild.getHeight(); //height difference of other child from parent
				int leftDif = node.getHeight() - node.getLeft().getHeight(); //height difference from left child
				int rightDif = node.getHeight() - node.getRight().getHeight(); //height difference from right child
				if (parentDif == 0) {
					if (parentOtherDif == 1) {
						//case 1: promote
						amount += handleInsertionCase1(parent);
					} else if (parentOtherDif == 2) {
						if ((parent.getLeft() == node && leftDif == 1 && rightDif == 2) ||
							(parent.getRight() == node && leftDif == 2 && rightDif == 1) ||
							(leftDif == 1 && rightDif == 1)) {
							//case 2
							amount += handleInsertionCase2(parent, node, leftDif, rightDif);
						} else {
							//case 3
							amount += handleInsertionCase3(parent, node, leftDif, rightDif);
						}
					} else {
						throw new IllegalStateException("Unsupported rebalance state");
					}
				} else if (parentDif != 1 || parentOtherDif != 1) {
					throw new IllegalStateException("Unsupported rebalance state");
				}
				//otherwise, parent was not a leaf and no rebalancing is needed
			}
			return amount;
		}

		/**
		 * Handle case 1 of insertion, which requires promotion and additional rebalancing
		 *complexity: O(log n)
		 * @param parent the rebalanced node's parent
		 * @return time complexity of the operation
		 */
		private int handleInsertionCase1(IAVLNode parent) {
			int amount = 0;
			amount += parent.promote();
			amount += rebalance(parent);
			return amount;
		}

		/**
		 * Handle case 2 of insertion, which requires rotation
		 *complexity: O(log n)
		 * @param parent   the rebalanced node's parent
		 * @param node     the rebalanced node
		 * @param leftDif  the height difference between the rebalanced node and its left child
		 * @param rightDif the height difference between the rebalanced node and its right child
		 * @return time complexity of the operation
		 */
		private int handleInsertionCase2(IAVLNode parent, IAVLNode node, int leftDif, int rightDif) {
			int amount = 0;
			if (parent.getLeft() == node) {
				//case 2 of left child: rotate right
				amount += rotations.rotateRight(node);
			} else {
				//case 2 of right child: rotate left
				amount += rotations.rotateLeft(node);
			}
			if (leftDif != rightDif) {
				amount += parent.demote();
			} else {
				//this is a case that may happen in join where leftDif=rightDif=1
				//after the rotation, it becomes case 1 and more rebalancing is needed
				amount += node.promote();
				amount += rebalance(node);
			}
			return amount;
		}

		/**
		 * Handle case 3 of insertion, which requires double rotation
		 *complexity: O(1)
		 * @param parent   the rebalanced node's parent
		 * @param node     the rebalanced node
		 * @param leftDif  the height difference between the rebalanced node and its left child
		 * @param rightDif the height difference between the rebalanced node and its right child
		 * @return time complexity of the operation
		 */
		private int handleInsertionCase3(IAVLNode parent, IAVLNode node, int leftDif, int rightDif) {
			int amount = 0;
			IAVLNode rotatedChild;
			if (parent.getLeft() == node && leftDif == 2 && rightDif == 1) {
				//case 3 of left child: rotate left then right
				rotatedChild = node.getRight();
				amount += rotations.rotateLeftNRight(rotatedChild);
			} else if (parent.getRight() == node && leftDif == 1 && rightDif == 2) {
				//case 3 of right child: rotate right then left
				rotatedChild = node.getLeft();
				amount += rotations.rotateRightNLeft(rotatedChild);
			} else {
				throw new IllegalStateException("Unsupported rebalance state");
			}
			amount += rotatedChild.promote();
			amount += rotatedChild.getLeft().demote();
			amount += rotatedChild.getRight().demote();
			return amount;
		}
	}

	class DeletionBalancer {
		/**
		 * Rebalances a node after deletion from the tree.
		 *complexity: O(log n)
		 * @param node the parent of the deleted node
		 * @return the number of rebalances that occurred
		 */
		public int rebalance(IAVLNode node) {
			if (node == null) {
				return 0;
			}
			int amount = 0;
			IAVLNode rightChild = node.getRight();
			IAVLNode leftChild = node.getLeft();

			int nodeHeight = node.getHeight();
			int leftDif = nodeHeight - leftChild.getHeight();
			int rightDif = nodeHeight - rightChild.getHeight();

			if ((leftDif == 2 && rightDif == 1) || (leftDif == 1 && rightDif == 2) || (leftDif == 1 && rightDif == 1)) {
				//no rebalancing is needed
				amount = 0;
			} else if (leftDif == 2 && rightDif == 2) {
				//case 1: demote and rebalance parent
				amount += handleDeletionCase1(node);
			} else if ((rightDif == 3 && leftDif == 1) || (rightDif == 1 && leftDif == 3)) {
				//decides the side of the symmetric cases
				boolean leftDifLarger = leftDif == 3;
				//height difference of grandchildren from their parent
				IAVLNode grandchildA = leftDifLarger ? rightChild.getLeft() : leftChild.getRight();
				IAVLNode grandchildB = leftDifLarger ? rightChild.getRight() : leftChild.getLeft();
				IAVLNode grandchildrenParent = grandchildA.getParent();
				int grandchildDifA = grandchildrenParent.getHeight() - grandchildA.getHeight();
				int grandchildDifB = grandchildrenParent.getHeight() - grandchildB.getHeight();

				if (grandchildDifA == 1 && grandchildDifB == 1) {
					//case 2: single rotation
					amount += handleDeletionCase2(leftDifLarger, node, grandchildrenParent);
				} else if (grandchildDifA == 2 && grandchildDifB == 1) {
					//case 3: single rotation and rebalance parent
					amount += handleDeletionCase3(leftDifLarger, node, grandchildrenParent);
				} else if (grandchildDifA == 1 && grandchildDifB == 2) {
					//case 4: double rotation and rebalance parent
					amount += handleDeletionCase4(leftDifLarger, node, grandchildrenParent, grandchildA);
				} else {
					throw new IllegalStateException("Unsupported rebalance state");
				}
			} else {
				throw new IllegalStateException("Unsupported rebalance state");
			}

			return amount;
		}

		/**
		 * Handle case 1 of deletion, which requires demotion and more rebalancing
		 *complexity: O(log n)
		 * @param node the rebalanced node
		 * @return time complexity
		 */
		private int handleDeletionCase1(IAVLNode node) {
			int amount = 0;
			amount += node.demote();
			amount += rebalance(node.getParent());
			return amount;
		}

		/**
		 * Handle case 2 of deletion, which requires rotation
		 *complexity: O(1)
		 * @param leftDifLarger       is the left height difference larger (decides the symmetric case side)
		 * @param node                the rebalanced node
		 * @param grandchildrenParent the node's child with grandchildren to rebalance
		 * @return time complexity
		 */
		private int handleDeletionCase2(boolean leftDifLarger, IAVLNode node, IAVLNode grandchildrenParent) {
			int amount = 0;
			if (leftDifLarger) {
				amount += rotations.rotateLeft(grandchildrenParent);
			} else {
				amount += rotations.rotateRight(grandchildrenParent);
			}
			amount += node.demote();
			amount += grandchildrenParent.promote();
			return amount;
		}

		/**
		 * Handle case 3 of deletion, which requires rotation and more rebalancing
		 *complexity: O(log n)
		 *
		 * @param leftDifLarger       is the left height difference larger (decides the symmetric case side)
		 * @param node                the rebalanced node
		 * @param grandchildrenParent the node's child with grandchildren to rebalance
		 * @return time complexity
		 */
		private int handleDeletionCase3(boolean leftDifLarger, IAVLNode node, IAVLNode grandchildrenParent) {
			int amount = 0;
			if (leftDifLarger) {
				amount += rotations.rotateLeft(grandchildrenParent);
			} else {
				amount += rotations.rotateRight(grandchildrenParent);
			}
			amount += node.demote();
			amount += node.demote();
			amount += rebalance(grandchildrenParent.getParent());
			return amount;
		}

		/**
		 * Handle case 4 of deletion, which requires double rotation and more rebalancing
		 *complexity: O(log n)
		 *
		 * @param leftDifLarger       is the left height difference larger (decides the symmetric case side)
		 * @param node                the rebalanced node
		 * @param grandchildrenParent the node's child with grandchildren to rebalance
		 * @param grandchild          the grandchild to rotate
		 * @return time complexity
		 */
		private int handleDeletionCase4(boolean leftDifLarger, IAVLNode node, IAVLNode grandchildrenParent, IAVLNode grandchild) {
			int amount = 0;
			if (leftDifLarger) {
				amount += rotations.rotateRightNLeft(grandchild);
			} else {
				amount += rotations.rotateLeftNRight(grandchild);
			}
			amount += node.demote();
			amount += node.demote();
			amount += grandchildrenParent.demote();
			amount += grandchild.promote();
			amount += rebalance(grandchild.getParent());
			return amount;
		}
	}
}


