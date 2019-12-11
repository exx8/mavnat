import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class InsertionTest {

	@Test
	void createTreeTest1() {
		AVLTree tree = new AVLTree();
		TestUtils.testAVL(tree);

		tree.insert(15, "a");
		TestUtils.testAVL(tree);

		tree.insert(11, "a");
		TestUtils.testAVL(tree);

		tree.insert(17, "a");
		TestUtils.testAVL(tree);

		tree.insert(18, "a");
		TestUtils.testAVL(tree);

		tree.insert(12, "a");
		TestUtils.testAVL(tree);

		tree.insert(13, "a");
		TestUtils.testAVL(tree);

		TreePrinter.print(tree, false);
		TestUtils.testAVL(tree);
	}

	@Test
	void createTreeTest2() {
		AVLTree tree = new AVLTree();
		TestUtils.testAVL(tree);

		int rebalances = tree.insert(5, "a");
		TestUtils.testAVL(tree);

		rebalances = tree.insert(10, "a");
		TestUtils.testAVL(tree);

		rebalances = tree.insert(12, "a");
		TestUtils.testAVL(tree);

		rebalances = tree.insert(4, "a");
		TestUtils.testAVL(tree);

		rebalances = tree.insert(15, "a");
		TestUtils.testAVL(tree);

		rebalances = tree.insert(1, "a");
		TestUtils.testAVL(tree);

		rebalances = tree.insert(3, "a");
		TestUtils.testAVL(tree);

		rebalances = tree.insert(17, "a");
		TestUtils.testAVL(tree);

		rebalances = tree.insert(8, "a");
		TestUtils.testAVL(tree);

		rebalances = tree.insert(2, "a");
		TestUtils.testAVL(tree);

		rebalances = tree.insert(14, "a");
		TestUtils.testAVL(tree);

		rebalances = tree.insert(16, "a");
		TestUtils.testAVL(tree);

		TreePrinter.print(tree, true);
		TestUtils.testAVL(tree);
	}

	@Test
	void manyInsertionsTest() {
		AVLTree tree = new AVLTree();
		Random rand = new Random();
		for (int i = 0; i < 1000; i++) {
			tree.insert(rand.nextInt(5000), "a");
			TestUtils.testAVL(tree);
		}
	}
}
