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
		tree.insert(5, "a");
		tree.insert(10, "a");
		tree.insert(12, "a");
		tree.insert(4, "a");
		tree.insert(15, "a");
		tree.insert(1, "a");
		tree.insert(3, "a");
		tree.insert(17, "a");
		tree.insert(8, "a");
		tree.insert(2, "a");
		tree.insert(14, "a");
		tree.insert(16, "a");

		TreePrinter.print(tree, false);
	}
}