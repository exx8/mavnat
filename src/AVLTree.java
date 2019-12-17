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
	protected int treeSize = 0;
	private Rotations rotations = new Rotations();
	private Balancer balancer = new Balancer();

	//region private methods

	/**
	 * Find the leftmost or rightmost subtree with a maximum height
	 * <p>
	 * precondition: a node with height smaller or equal to maxHeight exists
	 * </p>
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
	 * Inserts a node to the AVL tree.
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
			rebalances = balancer.rebalanceInsertion(node);
		}
		treeSize++;
		return rebalances;
	}

	private int inorderScan(IAVLNode node, IAVLNode[] arr, int index) {
		if (node != null && node.isRealNode()) {
			int amount = inorderScan(node.getLeft(), arr, index);
			index += amount;
			arr[index] = node;
			amount += 1 + inorderScan(node.getRight(), arr, index + 1);
			return amount;
		}
		return 0;
	}

	/**
	 * Find the successor of a node with a right child
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

	//endregion

	/**
	 * public boolean empty()
	 * <p>
	 * returns true if and only if the tree is empty
	 */
	public boolean empty() {
		return getRoot() == null;
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

		return insertNode(node);
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
		final Optional<IAVLNode> placeToDelete = findPlace(k);
		//return -1 if the key was not found
		if (!placeToDelete.isPresent())
			return -1;

		final IAVLNode deletedNode = placeToDelete.get();
		final boolean hasTwoChildren = deletedNode.getLeft().isRealNode() && deletedNode.getRight().isRealNode();

		if (!hasTwoChildren) {
			deleteDirectly(deletedNode);
		} else {
			deleteSuccessor(deletedNode);
		}
		treeSize--;
		return 0;
	}

	/**
	 * Delete a node by replacing it with its successor and deleting the successor instead
	 *
	 * @param node
	 * @return the parent of the successor
	 */
	private IAVLNode deleteSuccessor(IAVLNode node) {
		IAVLNode successor = findChildSuccessor(node);
		IAVLNode successorParent = successor.getParent();
		//delete the successor
		deleteDirectly(successor);
		//replace node with its successor
		successor.setLeft(node.getLeft());
		node.getLeft().setParent(successor);
		successor.setRight(node.getRight());
		node.getRight().setParent(successor);
		IAVLNode parent = node.getParent();
		if (parent != null) {
			if (parent.getRight() == node) {
				parent.setRight(successor);
			} else {
				parent.setLeft(successor);
			}
			successor.setParent(parent);
		} else {
			//the root node was deleted
			root = successor;
		}

		return successorParent;
	}

	/**
	 * Delete a node with one or zero children
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
			replacementNode.setParent(parent);
		} else {
			//the root node was deleted
			root = replacementNode.isRealNode() ? replacementNode : null;
		}
	}

	protected Optional<IAVLNode> findPlace(int key) {
		return findPlace(key, this.root);
	}

	protected static Optional<IAVLNode> findPlace(int key, IAVLNode currentNode) {
		if (currentNode == null)
			return Optional.empty();
		if (!currentNode.isRealNode())
			return Optional.empty();
		final int currentNodeKey = currentNode.getKey();
		if (currentNodeKey == key)
			return Optional.of(currentNode);
		else if (currentNodeKey > key)
			return findPlace(key, currentNode.getLeft());
		else
			return findPlace(key, currentNode.getRight());

	}

	protected int deletionRebalance(IAVLNode node) {
		if (node == null)
			return 0;
		if (!node.isRealNode())
			return 0;
		IAVLNode nextRootPreviousParentRight = node.getRight();
		IAVLNode nextRootPreviousParentLeft = node.getLeft();

		boolean bothChildrenHaveSameHeight = nextRootPreviousParentRight.getHeight() == node.getLeft().getHeight();
		int nextRootPreviousParentHeight = node.getHeight();
		int leftToParentHeightGap = nextRootPreviousParentHeight - node.getLeft().getHeight();
		int rightToParentHeightGap = nextRootPreviousParentHeight - nextRootPreviousParentRight.getHeight();

		boolean gapBetweenLeftAndRootIs2 = leftToParentHeightGap == 2;
		if (bothChildrenHaveSameHeight && leftToParentHeightGap == 1)
			return 0; //everything fine do nothing
		else if ((leftToParentHeightGap == 2 && rightToParentHeightGap == 1) || (leftToParentHeightGap == 1 && rightToParentHeightGap == 2))
			return 0; //everything fine do nothing
		else if (bothChildrenHaveSameHeight && gapBetweenLeftAndRootIs2) {
			//case 1
			node.demote();
			return deletionRebalance(node.getParent());

		} else if (nextRootPreviousParentLeft.getRight() == null) {
			rotations.rotateRight(nextRootPreviousParentLeft);
			return deletionRebalance(nextRootPreviousParentLeft) + 1;
		} else if (nextRootPreviousParentLeft.getLeft() == null) {
			rotations.rotateLeft(nextRootPreviousParentLeft);
			return deletionRebalance(nextRootPreviousParentLeft) + 1;
		} else if (nextRootPreviousParentRight.getRight() == null) {

			rotations.rotateRight(nextRootPreviousParentLeft);
			return deletionRebalance(nextRootPreviousParentLeft) + 1;
		} else if (nextRootPreviousParentRight.getLeft() == null) {
			rotations.rotateLeft(nextRootPreviousParentLeft);
			return deletionRebalance(nextRootPreviousParentLeft) + 1;
		} else if (rightToParentHeightGap == 3 && leftToParentHeightGap == 1) {
			//case 2 left
			if (nextRootPreviousParentRight.getRight().getHeight() - nextRootPreviousParentRight.getHeight() == 1 && nextRootPreviousParentRight.getLeft().getHeight() - nextRootPreviousParentRight.getHeight() == 1) {
				rotations.rotateLeft((node));
				node.demote();
				node.promote();

				return deletionRebalance(node.getParent()) + 1;
			}
			//case 3 left
			if (nextRootPreviousParentRight.getRight().getHeight() - nextRootPreviousParentRight.getHeight() == 1 && nextRootPreviousParentRight.getLeft().getHeight() - nextRootPreviousParentRight.getHeight() == 2) {
				rotations.rotateLeft((node));
				node.demote();
				node.demote();

				return deletionRebalance(node.getParent()) + 1;
			}
			//case 4 left
			else if (nextRootPreviousParentRight.getLeft().getHeight() - nextRootPreviousParentRight.getHeight() == 1 && nextRootPreviousParentRight.getRight().getHeight() - nextRootPreviousParentRight.getHeight() == 2) {
				rotations.rotateLeft((node));
				rotations.rotateLeft((node));//twice

				return deletionRebalance(node.getParent()) + 2;
			}

		} else if (rightToParentHeightGap == 1 && leftToParentHeightGap == 3) {
			//case 2 right

			if (nextRootPreviousParentLeft.getRight().getHeight() - nextRootPreviousParentLeft.getHeight() == 1 && nextRootPreviousParentLeft.getLeft().getHeight() - nextRootPreviousParentLeft.getHeight() == 1) {
				rotations.rotateRight((node));
				node.demote();

				return deletionRebalance(node.getParent()) + 1;
			}
			//case 3 right
			if (nextRootPreviousParentLeft.getRight().getHeight() - nextRootPreviousParentLeft.getHeight() == 1 && nextRootPreviousParentLeft.getLeft().getHeight() - nextRootPreviousParentLeft.getHeight() == 2) {
				rotations.rotateRight((node));
				node.demote();
				node.demote();

				return deletionRebalance(node.getParent()) + 1;
			}
			//case 4 right
			else if (nextRootPreviousParentLeft.getLeft().getHeight() - nextRootPreviousParentLeft.getHeight() == 1 && nextRootPreviousParentLeft.getRight().getHeight() - nextRootPreviousParentLeft.getHeight() == 2) {
				rotations.rotateRight((node));
				rotations.rotateRight((node));//twice

				return deletionRebalance(node.getParent()) + 2;
			}
		}

		throw new RuntimeException("shouldn't have reached here");
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
		IAVLNode[] arr = new IAVLNode[size()];
		inorderScan(getRoot(), arr, 0);
		return Arrays.stream(arr).mapToInt(n -> n.getKey()).toArray();
	}

	/**
	 * public String[] infoToArray()
	 * <p>
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
	 * Returns the number of nodes in the tree.
	 * <p>
	 * precondition: none
	 * postcondition: none
	 */
	public int size() {
		return treeSize;
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
		int complexity = Math.abs(getHeight() - t.getHeight()) + 1;
		int newSize = size() + t.size() + 1;
		if (empty() || t.empty()) {
			if (empty()) {
				root = t.getRoot();
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
			} else {
				//the key of x is bigger than the large tree's keys
				joinNode = largerTree.findSubtreeByHeight(false, smallerTree.getHeight());
				x.setRight(smallerTree.getRoot());
				x.setLeft(joinNode);
				if (joinNode.getParent() != null) {
					joinNode.getParent().setRight(x);
				}
			}
			x.setParent(joinNode.getParent());
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
			balancer.rebalanceInsertion(x);
		}
		treeSize = newSize;
		return complexity;
	}

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
		 *
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

			return 1;
		}

		/**
		 * Perform a right rotation
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

			return 1;
		}

		/**
		 * Perform a double rotation of left and right
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
	}

	class Balancer {
		/**
		 * Rebalances a node after insertion to the tree.
		 *
		 * @param node the node to rebalance
		 * @return the number of rebalances that occurred
		 */
		public int rebalanceInsertion(IAVLNode node) {
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
		 *
		 * @param parent the rebalanced node's parent
		 * @return time complexity of the operation
		 */
		private int handleInsertionCase1(IAVLNode parent) {
			int amount = 0;
			amount += parent.promote();
			amount += rebalanceInsertion(parent);
			return amount;
		}

		/**
		 * Handle case 2 of insertion, which requires rotation
		 *
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
				amount += rebalanceInsertion(node);
			}
			return amount;
		}

		/**
		 * Handle case 3 of insertion, which requires double rotation
		 *
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
}
  

