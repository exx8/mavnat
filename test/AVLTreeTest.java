import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AVLTreeTest {

	@Test
	void keysToArrayTest() {
		AVLTree tree = new AVLTree();
		tree.insert(10, "a");
		tree.insert(2, "a");
		tree.insert(17, "a");
		tree.insert(54, "a");
		tree.insert(3, "a");
		tree.insert(4, "a");
		tree.insert(8, "a");
		tree.insert(1, "a");

		int[] keys = tree.keysToArray();

		int[] expectedKeys = {1, 2, 3, 4, 8, 10, 17, 54};
		assertArrayEquals(expectedKeys, keys);
	}

	@Test
	void keysToArrayTest_EmptyTree() {
		AVLTree tree = new AVLTree();

		int[] keys = tree.keysToArray();

		assertArrayEquals(new int[]{}, keys);
	}

	@Test
	void infoToArrayTest() {
		AVLTree tree = new AVLTree();
		tree.insert(2, "b");
		tree.insert(3, "c");
		tree.insert(6, "d");
		tree.insert(22, "g");
		tree.insert(8, "e");
		tree.insert(34, "h");
		tree.insert(0, "a");
		tree.insert(9, "f");

		String[] values = tree.infoToArray();

		String[] expectedValues = {"a", "b", "c", "d", "e", "f", "g", "h"};
		assertArrayEquals(expectedValues, values);
	}

	@Test
	void infoToArrayTest_EmptyTree() {
		AVLTree tree = new AVLTree();

		String[] values = tree.infoToArray();

		assertArrayEquals(new String[]{}, values);
	}

	@Test
	void deleteTreeTestParent() {
		AVLTree tree = new AVLTree();
		tree.insert(15, "a");
		tree.insert(11, "a");
		tree.insert(17, "a");
		tree.insert(18, "a");
		tree.insert(12, "a");
		tree.insert(13, "a");

		tree.delete(15);
		TreePrinter.print(tree, false);
	}


	@Test
	void deleteTreeTest13() {
		AVLTree tree = new AVLTree();
		tree.insert(15, "a");
		tree.insert(11, "a");
		tree.insert(17, "a");
		tree.insert(18, "a");
		tree.insert(12, "a");
		tree.insert(13, "a");

		tree.delete(13);
		tree.delete(18);
		tree.delete(12);

		TreePrinter.print(tree, false);
	}
}
