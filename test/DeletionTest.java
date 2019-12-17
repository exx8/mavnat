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
			TestUtils.testBST(tree, false);
		}

		assertSame(null, tree.getRoot());
	}
}
