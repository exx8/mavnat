import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.CollectionUtils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

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
		AVLTree tree = new AVLTree();
		List<Integer> keys = new ArrayList<>();
		int n = 1000;
		for (int i = 0; i < n; i++) {
			int key;
			do {
				key = rand.nextInt(n * 10);
			} while (TestUtils.getNodeByKey(tree, key) != null);
			tree.insert(key, "a");
			keys.add(key);
		}
		Collections.sort(keys);

		for (int i = 0; i < n; i++) {
			int index = rand.nextInt(keys.size());
			int key = keys.get(index);
			keys.remove(index);

			tree.delete(key);

			int[] currentKeys = tree.keysToArray();
			assertArrayEquals(keys.stream().mapToInt(k -> k).toArray(), currentKeys);
			assertEquals(keys.size(), tree.size());
			if (i < n - 1) {
				int min = keys.get(0);
				int max = keys.get(keys.size() - 1);
				assertEquals(min, tree.min.getKey());
				assertEquals(max, tree.max.getKey());
			} else {
				assertSame(null, tree.min);
				assertSame(null, tree.max);
			}
			TestUtils.testAVL(tree);
		}

		assertSame(null, tree.getRoot());
	}

	@Test
	void deletionTest_SpecificCase() {
		AVLTree tree = TestUtils.generateTree(Arrays.asList(57, 24, 5, 13, 37, 81, 78, 71, 79, 92));
		tree.delete(81);
		TestUtils.testAVL(tree);
	}

	@Test
	void deletionTest_SpecificCase2() {
		AVLTree tree = TestUtils.generateTree(Arrays.asList(84, 49, 20, 8, 45, 73, 82, 93, 91, 89, 106, 102, 136, 108, 145));
		tree.delete(106);
		tree.delete(73);
		tree.delete(49);

		tree.delete(89);
		TestUtils.testAVL(tree);
	}

	@Test
	void deletionTest_SpecificCase3() {
		AVLTree tree = TestUtils.generateTree(Arrays.asList(86, 54, 16, 9, 14, 31, 36, 70, 63, 77, 74, 133, 100, 132, 140));
		tree.delete(100);
		tree.delete(9);
		tree.delete(36);
		TestUtils.testAVL(tree);
	}
}
