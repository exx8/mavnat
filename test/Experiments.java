import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

class Experiments {
	@Test
	void exp1() {
		Random rand = new Random();
		for (int i = 1; i <= 10; i++) {
			AVLTree tree = new AVLTree();
			int n = i * 10000;
			List<Integer> keys = new ArrayList<>();
			System.out.println("i: " + i);
			int insertionBalances = 0;
			int maxInsertionBalances = 0;
			int deletionBalances = 0;
			int maxDeletionBalances = 0;
			for (int j = 0; j < n; j++) {
				int key;
				do {
					key = rand.nextInt(10000000);
				} while (keys.contains(key));
				keys.add(key);
				int balances = tree.insert(key, "a");
				maxInsertionBalances = Math.max(maxInsertionBalances, balances);
				insertionBalances += balances;
			}
			Collections.sort(keys);
			for (int j = 0; j < n; j++) {
				int balances = tree.delete(keys.get(j));
				maxDeletionBalances = Math.max(maxDeletionBalances, balances);
				deletionBalances += balances;
			}
			double insertionAvg = (double) insertionBalances / n;
			double deletionAvg = (double) deletionBalances / n;
			System.out.println(String.format("Insertion - max: %d, avg.: %.3f", maxInsertionBalances, insertionAvg));
			System.out.println(String.format("Deletion - max: %d, avg.: %.3f", maxDeletionBalances, deletionAvg));
		}
	}
}
