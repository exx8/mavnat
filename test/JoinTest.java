import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class JoinTest {

	@Test
	void joinTreesTest_EqualSize() {
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
		TestUtils.testAVL(tree);
	}

	@Test
	void joinTreesTest_JoinedIntoSmaller() {
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
		TestUtils.testAVL(tree);
	}

	@Test
	void joinTreesTest_JoinedIntoLarger() {
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
		TestUtils.testAVL(tree);
	}
}
