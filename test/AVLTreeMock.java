import java.util.ArrayList;
import java.util.List;

public class AVLTreeMock extends AVLTree {
	private int totalJoinCost = 0;
	private int joinCount = 0;
	private int maxJoinCost = 0;
	private List<AVLTreeMock> createdTrees;

	@Override
	public int join(IAVLNode x, AVLTree t) {
		int cost = super.join(x, t);
		totalJoinCost += cost;
		joinCount++;
		maxJoinCost = Math.max(maxJoinCost, cost);
		return cost;
	}

	@Override
	public AVLTree[] split(int x) {
		createdTrees = new ArrayList<>();
		return super.split(x);
	}

	@Override
	protected AVLTree createTree() {
		AVLTreeMock tree = new AVLTreeMock();
		createdTrees.add(tree);
		return tree;
	}

	public double getAvgJoinCost() {
		int cost = 0;
		int amount = 0;
		for (AVLTreeMock tree : createdTrees) {
			cost += tree.totalJoinCost;
			amount += tree.joinCount;
		}
		double avg = (double) cost / amount;
		return avg;
	}

	public int getMaxJoinCost() {
		int maxCost = 0;
		for (AVLTreeMock tree : createdTrees) {
			maxCost = Math.max(maxCost, tree.maxJoinCost);
		}
		return maxCost;
	}

	public int getMiddleKey() {
		IAVLNode node = root.getLeft();
		while (node.getRight().isRealNode()) {
			node = node.getRight();
		}
		return node.getKey();
	}
}
