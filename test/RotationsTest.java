import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RotationsTest {
	@Test
	void rotateRightTest() {
		AVLTree tree = TestUtils.generateTree(Arrays.asList(38, 25, 10, 8, 7, 9, 13, 15, 14, 29, 27, 31, 33));
		AVLTree.IAVLNode node = TestUtils.getNodeByKey(tree, 10);
		AVLTree.Rotations rotations = tree.new Rotations();

		rotations.rotateRight(node);

		List<Integer> newPreorder = TestUtils.preOrderScan(tree);
		List<Integer> expectedPreorder = Arrays.asList(38, 10, 8, 7, 9, 25, 13, 15, 14, 29, 27, 31, 33);
		assertEquals(expectedPreorder, newPreorder);
		TestUtils.testBST(tree, false);
	}

	@Test
	void rotateLeftTest() {
		AVLTree tree = TestUtils.generateTree(Arrays.asList(38, 25, 10, 8, 7, 9, 13, 15, 14, 29, 27, 31, 33));
		AVLTree.IAVLNode node = TestUtils.getNodeByKey(tree, 31);
		AVLTree.Rotations rotations = tree.new Rotations();

		rotations.rotateLeft(node);

		List<Integer> newPreorder = TestUtils.preOrderScan(tree);
		List<Integer> expectedPreorder = Arrays.asList(38, 25, 10, 8, 7, 9, 13, 15, 14, 31, 29, 27, 33);
		assertEquals(expectedPreorder, newPreorder);
		TestUtils.testBST(tree, false);
	}
}
