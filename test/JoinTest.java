import org.junit.jupiter.api.Test;
import sun.reflect.generics.tree.Tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
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

		assertEquals(1, complexity);
		assertEquals(13, tree.size());
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

		assertEquals(2, complexity);
		assertEquals(9, tree.size());
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
		tree.insert(13, "a");
		tree.insert(10, "a");
		tree.insert(15, "a");
		AVLTree tree2 = new AVLTree();
		tree2.insert(20, "a");
		tree2.insert(19, "a");
		tree2.insert(35, "a");
		AVLTree.IAVLNode x = tree2.new AVLNode(17, "a");

		int complexity = tree.join(x, tree2);

		assertEquals(3, complexity);
		assertEquals(13, tree.size());
		TestUtils.testAVL(tree);
	}

	@Test
	void joinTreesTest_IntoEmpty() {
		AVLTree tree = new AVLTree();
		AVLTree tree2 = new AVLTree();
		tree2.insert(3, "a");
		tree2.insert(5, "a");
		tree2.insert(8, "a");
		tree2.insert(1, "a");
		tree2.insert(13, "a");
		tree2.insert(10, "a");
		tree2.insert(15, "a");
		AVLTree.IAVLNode x = tree2.new AVLNode(17, "a");

		int complexity = tree.join(x, tree2);

		assertEquals(5, complexity);
		assertEquals(8, tree.size());
		TestUtils.testAVL(tree);
	}

	@Test
	void joinTreesTest_Empty() {
		AVLTree tree = new AVLTree();
		tree.insert(25, "a");
		tree.insert(5, "a");
		tree.insert(8, "a");
		tree.insert(68, "a");
		tree.insert(13, "a");
		tree.insert(10, "a");
		tree.insert(70, "a");
		tree.insert(100, "a");
		AVLTree tree2 = new AVLTree();
		AVLTree.IAVLNode x = tree.new AVLNode(3, "a");

		int complexity = tree.join(x, tree2);

		assertEquals(5, complexity);
		assertEquals(9, tree.size());
		TestUtils.testAVL(tree);
	}

	@Test
	void joinTreesTest_BothEmpty() {
		AVLTree tree = new AVLTree();
		AVLTree tree2 = new AVLTree();
		AVLTree.IAVLNode x = tree.new AVLNode(15, "a");

		int complexity = tree.join(x, tree2);

		assertEquals(1, complexity);
		assertEquals(1, tree.size());
		TestUtils.testAVL(tree);
	}

	@Test
	void joinTreesTest_ManyRandJoins() {
		int n = 1000;
		int maxTreeSize = 100;
		Random rand = new Random();
		for (int i = 0; i < n; i++) {
			boolean below = rand.nextBoolean();
			int xKey = 500 + rand.nextInt(50);
			HashSet<Integer> selectedValues = new HashSet<>();
			AVLTree[] trees = new AVLTree[2];
			int[] size = new int[2];
			for (int j = 0; j < 2; j++) {
				size[j] = rand.nextInt(maxTreeSize);
				int rangeStart, rangeEnd;
				if (below) {
					rangeStart = 0;
					rangeEnd = xKey;

				} else {
					rangeStart = xKey + 1;
					rangeEnd = xKey + size[j] * 5;
				}
				trees[j] = new AVLTree();
				for (int k = 0; k < size[j]; k++) {
					int randKey;
					do {
						randKey = rangeStart + rand.nextInt(rangeEnd - rangeStart);
					} while (selectedValues.contains(randKey));
					selectedValues.add(randKey);
					trees[j].insert(randKey, "a");
				}
				below = !below;
			}
			AVLTree.IAVLNode x = trees[0].new AVLNode(xKey, "a");
			trees[0].join(x, trees[1]);

			assertEquals(size[0] + size[1] + 1, trees[0].size());
			TestUtils.testAVL(trees[0]);
		}
	}
}
