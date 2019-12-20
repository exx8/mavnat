import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

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
		AVLTree tree = new AVLTree();
		int n = 1000;
		int maxTreeSize = 1000;
		for (int i = 0; i < n; i++) {
			int size = rand.nextInt(maxTreeSize);
			List<Integer> keysList = new ArrayList<>();
			for (int j = 0; j < size; j++) {
				int key;
				do {
					key = rand.nextInt(maxTreeSize * 100);
				} while (keysList.contains(key));
				keysList.add(key);
				tree.insert(key, Integer.toString(key));
			}
			int splitKeyIndex = rand.nextInt(size + 1);
			int splitKey = keysList.get(splitKeyIndex);
			int[] lowerKeys = keysList.stream().filter(k -> k < splitKey).sorted().mapToInt(k -> k).toArray();
			int[] higherKeys = keysList.stream().filter(k -> k > splitKey).sorted().mapToInt(k -> k).toArray();

			AVLTree[] splitTrees = tree.split(splitKey);
			assertArrayEquals(lowerKeys, splitTrees[0].keysToArray());
			assertArrayEquals(higherKeys, splitTrees[1].keysToArray());
			TestUtils.testAVL(splitTrees[0]);
			TestUtils.testAVL(splitTrees[1]);
		}
	}
}
