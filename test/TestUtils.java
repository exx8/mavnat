import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

public class TestUtils {
	/**
	 * Tests all the basic attributes of an AVL tree, as well as attributes of a regular tree:
	 * binary search tree, heights, unreal leaves, children pointing to parents
	 */
	public static void testAVL(AVLTree tree) {
		testOrder(tree);
		testUnrealLeaves(tree);
		testHeights(tree, true);
		testParents(tree);
	}

	/**
	 * Tests all the basic attributes of a binary search tree
	 */
	public static void testBST(AVLTree tree, boolean testHeights) {
		testOrder(tree);
		testUnrealLeaves(tree);
		if (testHeights) {
			testHeights(tree, false);
		}
		testParents(tree);
	}

	public static void testHeights(AVLTree tree, boolean testAVLdif) {
		if (tree.getRoot() != null) {
			testHeightRec(tree.getRoot(), testAVLdif);
		}
	}

	private static int testHeightRec(AVLTree.IAVLNode node, boolean testAVLdif) {
		if (node.isRealNode()) {
			int heightLeft = testHeightRec(node.getLeft(), testAVLdif);
			int heightRight = testHeightRec(node.getRight(), testAVLdif);
			int height = Math.max(heightLeft, heightRight) + 1;
			int difLeft = height - heightLeft;
			int difRight = height - heightRight;
			if (testAVLdif) {
				Assertions.assertTrue((difLeft == 1 && difRight == 1) || (difLeft == 1 && difRight == 2) || (difLeft == 2 && difRight == 1), String.format("Wrong rank difference in node %d", node.getKey()));
			}
			Assertions.assertEquals(height, node.getHeight(), String.format("Node %d has a wrong height", node.getKey()));
			return height;
		} else {
			Assertions.assertEquals(-1, node.getHeight(), "An unreal node has a wrong height");
			return node.getHeight();
		}
	}

	public static void testParents(AVLTree tree) {
		testParentsRec(tree.getRoot(), null);
	}

	public static void testParentsRec(AVLTree.IAVLNode node, AVLTree.IAVLNode parent) {
		if (node != null) {
			Assertions.assertSame(parent, node.getParent(), String.format("Node %d has incorrect parent field", node.getKey()));
			testParentsRec(node.getLeft(), node);
			testParentsRec(node.getRight(), node);
		}
	}

	public static void testOrder(AVLTree tree) {
		testOrderRec(tree.getRoot(), new ArrayList<>());
	}

	public static void testOrderRec(AVLTree.IAVLNode node, List<Integer> inOrder) {
		if (node != null && node.isRealNode()) {
			testOrderRec(node.getLeft(), inOrder);
			if (inOrder.size() > 0) {
				Assertions.assertTrue(node.getKey() > inOrder.get(inOrder.size() - 1));
			}
			inOrder.add(node.getKey());
			testOrderRec(node.getRight(), inOrder);
		}
	}

	public static List<Integer> preOrderScan(AVLTree tree) {
		List<Integer> preorder = new ArrayList<>();
		recPreOrder(tree.getRoot(), preorder);
		return preorder;
	}

	private static void recPreOrder(AVLTree.IAVLNode node, List<Integer> preorder) {
		if (node != null && node.isRealNode()) {
			preorder.add(node.getKey());
			recPreOrder(node.getLeft(), preorder);
			recPreOrder(node.getRight(), preorder);
		}
	}

	public static AVLTree generateTree(List<Integer> preorder) {
		AVLTree tree = new AVLTree();
		AVLTree.IAVLNode root = tree.new AVLNode(preorder.get(0), "a");
		tree.root = root;
		generateTreeNode(tree, root, preorder, 1, preorder.size());
		updateHeightRec(root);
		return tree;
	}

	public static int updateHeightRec(AVLTree.IAVLNode node) {
		if (node.isRealNode()) {
			int height = Math.max(updateHeightRec(node.getLeft()), updateHeightRec(node.getRight())) + 1;
			node.setHeight(height);
			return height;
		} else {
			return node.getHeight();
		}
	}

	private static void generateTreeNode(AVLTree tree, AVLTree.IAVLNode node, List<Integer> preorder, int startRange, int endRange) {
		int firstRightIndex = endRange;
		for (int i = startRange; i < endRange; i++) {
			if (preorder.get(i) > node.getKey()) {
				firstRightIndex = i;
				break;
			}
		}
		if (firstRightIndex > startRange) {
			AVLTree.IAVLNode leftChild = tree.new AVLNode(preorder.get(startRange), "a", node, true);
			node.setLeft(leftChild);
			generateTreeNode(tree, leftChild, preorder, startRange + 1, firstRightIndex);
		} else {
			node.setFakeLeft();
		}
		if (firstRightIndex < endRange) {
			AVLTree.IAVLNode rightChild = tree.new AVLNode(preorder.get(firstRightIndex), "a", node, true);
			node.setRight(rightChild);
			generateTreeNode(tree, rightChild, preorder, firstRightIndex + 1, endRange);
		} else {
			node.setFakeRight();
		}
	}

	public static void testUnrealLeaves(AVLTree tree) {
		testUnrealLeavesRec(tree.getRoot());
	}

	private static void testUnrealLeavesRec(AVLTree.IAVLNode node) {
		if (node != null) {
			Assertions.assertTrue((node.getLeft() != null && node.getRight() != null) || !node.isRealNode(), String.format("Node %d is missing unreal leaves", node.getKey()));
			testUnrealLeavesRec(node.getLeft());
			testUnrealLeavesRec(node.getRight());
		}
	}

	public static AVLTree.IAVLNode getNodeByKey(AVLTree tree, int k) {
		AVLTree.IAVLNode node = tree.getRoot();
		while (node != null && node.isRealNode()) {
			if (k == node.getKey()) {
				return node;
			} else if (k < node.getKey()) {
				node = node.getLeft();
			} else {
				node = node.getRight();
			}
		}
		return null;
	}
}
