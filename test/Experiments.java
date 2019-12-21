import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

	@Test
	void exp2() {
		Random rand = new Random();
		for (int i = 1; i <= 10; i++) {
			System.out.println("i: " + i);
			int n = i * 10000;
			for (int exp = 0; exp < 2; exp++) {
				AVLTreeMock tree = new AVLTreeMock();
				List<Integer> keys = new ArrayList<>();
				for (int j = 0; j < n; j++) {
					int key;
					do {
						key = rand.nextInt(10000000);
					} while (keys.contains(key));
					keys.add(key);
					tree.insert(key, "a");
				}
				double avgJoinCost;
				int maxJoinCost;
				int key;
				if (exp == 0) {
					System.out.println("Random key:");
					int randInd = rand.nextInt(keys.size());
					key = keys.get(randInd);
				} else {
					System.out.println("Middle key:");
					key = tree.getMiddleKey();
				}
				tree.split(key);
				avgJoinCost = tree.getAvgJoinCost();
				maxJoinCost = tree.getMaxJoinCost();
				System.out.println(String.format("Avg: %.3f", avgJoinCost));
				System.out.println(String.format("Max: %d", maxJoinCost));
			}
		}
	}

	@Test
	void getMiddleKeyTest() {
		AVLTreeMock tree = new AVLTreeMock();
		tree.insert(9, "a");
		tree.insert(4, "a");
		tree.insert(12, "a");
		tree.insert(24, "a");
		tree.insert(98, "a");
		tree.insert(4, "a");
		tree.insert(56, "a");
		tree.insert(5, "a");
		tree.insert(17, "a");
		tree.insert(28, "a");
		tree.insert(19, "a");
		tree.insert(11, "a");
		tree.insert(14, "a");
		tree.insert(15, "a");
		tree.insert(7, "a");
		tree.insert(8, "a");
		tree.insert(100, "a");
		tree.insert(200, "a");
		tree.insert(300, "a");
		tree.insert(400, "a");
		tree.insert(500, "a");
		tree.insert(600, "a");

		int middleKey = tree.getMiddleKey();

		assertEquals(15, middleKey);
	}
}
