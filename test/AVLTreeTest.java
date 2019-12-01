import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AVLTreeTest {

	@Test
	void createTreeTest() {
		AVLTree tree = new AVLTree();
		tree.insert(15, "a");
		tree.insert(11, "a");
		tree.insert(17, "a");
		tree.insert(18, "a");
		tree.insert(12, "a");
		tree.insert(13, "a");
		TreePrinter.print(tree, false);
	}

	@Test
	void createTreeAVLTest() {
		AVLTree tree = new AVLTree();
		int rebalances = tree.insert(5, "a");
		rebalances = tree.insert(10, "a");
		rebalances = tree.insert(12, "a");
		rebalances = tree.insert(4, "a");
		rebalances = tree.insert(15, "a");
		rebalances = tree.insert(1, "a");
		rebalances = tree.insert(3, "a");
		rebalances = tree.insert(17, "a");
		rebalances = tree.insert(8, "a");
		rebalances = tree.insert(2, "a");
		rebalances = tree.insert(14, "a");
		rebalances = tree.insert(16, "a");

		TreePrinter.print(tree, false);
	}

	@Test
	void joinTreesExp() {
		AVLTree tree = new AVLTree();
		tree.insert(3, "a");
		tree.insert(5, "a");
		tree.insert(8, "a");
		tree.insert(1, "a");
		tree.insert(2, "a");
		tree.insert(4, "a");

		AVLTree tree2 = new AVLTree();
		tree2.insert(20, "a");
		tree2.insert(19, "a");
		tree2.insert(12, "a");
		tree2.insert(10, "a");
		tree2.insert(13, "a");
		tree2.insert(35, "a");

		AVLTree.IAVLNode x = tree2.new AVLNode(9, "a");
		int complexity = tree.join(x, tree2);
		TreePrinter.print(tree, false);
	}

	@Test
	void joinTreesExp2() {
		AVLTree tree = new AVLTree();
		tree.insert(3, "a");
		tree.insert(5, "a");

		AVLTree tree2 = new AVLTree();
		tree2.insert(20, "a");
		tree2.insert(19, "a");
		tree2.insert(12, "a");
		tree2.insert(10, "a");
		tree2.insert(13, "a");
		tree2.insert(35, "a");

		AVLTree.IAVLNode x = tree2.new AVLNode(9, "a");
		int complexity = tree.join(x, tree2);
		TreePrinter.print(tree, false);
	}

	@Test
	void joinTreesExp3() {
		AVLTree tree = new AVLTree();
		tree.insert(3, "a");
		tree.insert(5, "a");
		tree.insert(8, "a");
		tree.insert(1, "a");
		tree.insert(2, "a");
		tree.insert(4, "a");

		AVLTree tree2 = new AVLTree();
		tree2.insert(20, "a");
		tree2.insert(19, "a");
		tree2.insert(35, "a");

		AVLTree.IAVLNode x = tree2.new AVLNode(9, "a");
		int complexity = tree.join(x, tree2);
		TreePrinter.print(tree, false);
	}
}