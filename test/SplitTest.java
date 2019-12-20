import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SplitTest {
	@Test
	void splitTest() {
		AVLTree tree = new AVLTree();
		tree.insert(6, "a");
		tree.insert(7, "a");
		tree.insert(20, "a");
		tree.insert(1, "a");
		tree.insert(3, "a");
		TreePrinter.print(tree);
		AVLTree[] trees = tree.split(3);
		TreePrinter.print(trees[0]);
		TreePrinter.print(trees[1]);
	}

	@Test
	void splitTest_ManyRandomSplits() {
		Random rand = new Random();
		int n = 1000;
		int maxTreeSize = 1000;
		for (int i = 0; i < n; i++) {
			AVLTree tree = new AVLTree();
			int size = 1 + rand.nextInt(maxTreeSize);
			List<Integer> keysList = new ArrayList<>();
			for (int j = 0; j < size; j++) {
				int key;
				do {
					key = rand.nextInt(maxTreeSize * 100);
				} while (keysList.contains(key));
				keysList.add(key);
				tree.insert(key, Integer.toString(key));
			}
			int splitKeyIndex = rand.nextInt(size);
			int splitKey = keysList.get(splitKeyIndex);
			int[] lowerKeys = keysList.stream().filter(k -> k < splitKey).sorted().mapToInt(k -> k).toArray();
			int[] higherKeys = keysList.stream().filter(k -> k > splitKey).sorted().mapToInt(k -> k).toArray();

			//System.out.println(TestUtils.preOrderScan(tree));
			//System.out.println("Split on: " + splitKey);
			AVLTree[] splitTrees = tree.split(splitKey);
			/*assertArrayEquals(lowerKeys, splitTrees[0].keysToArray());
			assertArrayEquals(higherKeys, splitTrees[1].keysToArray());
			TestUtils.testAVL(splitTrees[0]);
			TestUtils.testAVL(splitTrees[1]);*/
		}
	}

	@Test
	void splitTest_SpecificCase() {
		AVLTree tree = TestUtils.generateTree(Arrays.asList(193, 66, 69, 490));
		TreePrinter.print(tree);
		tree.split(69);
	}

	@Test
	void splitTest_SpecificCase2() {
		AVLTree tree = TestUtils.generateTree(Arrays.asList(134, 64, 435));
		TreePrinter.print(tree);
		tree.split(435);
	}

	@Test
	void splitTest_SpecificCase3() {
		AVLTree tree = TestUtils.generateTree(Arrays.asList(133, 1, 73, 369));
		TreePrinter.print(tree);
		tree.split(369);
	}
}
