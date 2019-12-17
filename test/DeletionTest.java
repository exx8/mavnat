import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DeletionTest {
	@Test
	void deletionTest_BST_Successor() {
		AVLTree tree = TestUtils.generateTree(Arrays.asList(10, 7, 5, 8, 20, 16, 25, 22));
		TreePrinter.print(tree);

		tree.delete(20);
		TreePrinter.print(tree);

		List<Integer> preOrder = TestUtils.preOrderScan(tree);
		List<Integer> expectedPreOrder = Arrays.asList(10, 7, 5, 8, 22, 16, 25);
		assertEquals(expectedPreOrder, preOrder);
		TestUtils.testBST(tree, false);
	}

	@Test
	void deletionTest_BST_Direct() {
		AVLTree tree = TestUtils.generateTree(Arrays.asList(10, 7, 5, 8, 9, 20, 16, 25, 22));
		TreePrinter.print(tree);

		tree.delete(8);
		TreePrinter.print(tree);

		List<Integer> preOrder = TestUtils.preOrderScan(tree);
		List<Integer> expectedPreOrder = Arrays.asList(10, 7, 5, 9, 20, 16, 25, 22);
		assertEquals(expectedPreOrder, preOrder);
		TestUtils.testBST(tree, false);
	}

	@Test
	void deletionTest_ManyDeletions() {
		Random rand = new Random();
	}

	void manyDeletions(double ratio) {

	}
}
