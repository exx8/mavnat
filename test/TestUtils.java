import org.junit.jupiter.api.Assertions;

public class TestUtils {
	public static String preOrder(AVLTree tree) {
		StringBuilder sb = new StringBuilder();
		recPreOrder(tree.getRoot(), sb);
		return sb.toString();
	}

	private static void recPreOrder(AVLTree.IAVLNode node, StringBuilder sb) {
		if (node != null) {
			sb.append(node.getKey());
			recPreOrder(node.getLeft(), sb);
			recPreOrder(node.getRight(), sb);
		}
	}

	public static AVLTree generateTree(int[] preorder) {
		AVLTree tree = new AVLTree();
		AVLTree.IAVLNode root = tree.new AVLNode(preorder[0], "a");
		tree.root = root;
		generateTreeNode(tree, root, preorder, 1, preorder.length);
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

	private static void generateTreeNode(AVLTree tree, AVLTree.IAVLNode node, int[] preorder, int startRange, int endRange) {
		int firstRightIndex = endRange;
		for (int i = startRange; i < endRange; i++) {
			if (preorder[i] > node.getKey()) {
				firstRightIndex = i;
				break;
			}
		}
		if (firstRightIndex > startRange) {
			AVLTree.IAVLNode leftChild = tree.new AVLNode(preorder[startRange], "a", node, true);
			node.setLeft(leftChild);
			generateTreeNode(tree, leftChild, preorder, startRange + 1, firstRightIndex);
		} else {
			node.setFakeLeft();
		}
		if (firstRightIndex < endRange) {
			AVLTree.IAVLNode rightChild = tree.new AVLNode(preorder[firstRightIndex], "a", node, true);
			node.setRight(rightChild);
			generateTreeNode(tree, rightChild, preorder, firstRightIndex + 1, endRange);
		} else {
			node.setFakeRight();
		}
	}

	public static void verifyUnrealLeaves(AVLTree tree) {
		verifyUnrealLeavesRec(tree.getRoot());
	}

	private static void verifyUnrealLeavesRec(AVLTree.IAVLNode node) {
		if (node != null) {
			Assertions.assertTrue((node.getLeft() != null && node.getRight() != null) || !node.isRealNode());
			verifyUnrealLeavesRec(node.getLeft());
			verifyUnrealLeavesRec(node.getRight());
		}
	}
}
