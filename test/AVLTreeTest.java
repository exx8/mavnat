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
		TreePrinter.print(tree);
	}
}