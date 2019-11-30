import java.util.ArrayList;
import java.util.List;

public class TreePrinter {
	/**
	 * Prints a tree object to the console
	 *
	 * @param tree an AVL tree
	 */
	public static void print(AVLTree tree) {
		if (!tree.empty()) {
			int treeHeight = tree.getRoot().getHeight();
			List<AVLTree.IAVLNode> nodes = new ArrayList<>();
			nodes.add(tree.getRoot());
			int depth = 0;
			int index = 0;
			while (depth <= treeHeight) {
				int beforeSpaces = (int) Math.pow(2, treeHeight - depth) - 1;
				int betweenSpaces = (int) Math.pow(2, treeHeight - depth + 1) - 1;
				printWhitespaces(beforeSpaces);
				int nodesAmount = (int) Math.pow(2, depth);
				int carry = 0;
				for (int i = 0; i < nodesAmount; i++) {
					AVLTree.IAVLNode node = nodes.get(index + i);
					carry = 0;
					if (node != null) {
						System.out.print(node.getKey());
						if (node.getKey() >= 10) {
							carry = -1;
						}
						nodes.add(node.getLeft());
						nodes.add(node.getRight());
					} else {
						System.out.print(' ');
						nodes.add(null);
						nodes.add(null);
					}
					printWhitespaces(betweenSpaces + carry);
				}
				System.out.print("\n");
				if (depth < treeHeight) {
					int branchesAmount = (int) Math.pow(2, treeHeight - depth - 1);
					for (int i = 0; i < branchesAmount; i++) {
						printWhitespaces(beforeSpaces - 1 - i);
						for (int j = 0; j < nodesAmount; j++) {
							AVLTree.IAVLNode node = nodes.get(index + j);
							if (node != null && node.getLeft() != null) {
								System.out.print("/");
							} else {
								System.out.print(' ');
							}
							printWhitespaces(1 + 2 * i);
							if (node != null && node.getRight() != null) {
								System.out.print("\\");
							} else {
								System.out.print(' ');
							}
							printWhitespaces(betweenSpaces - 2 * (i + 1));
						}
						System.out.print("\n");
					}
				}
				depth++;
				index += nodesAmount;
			}
		}
	}

	private static void printWhitespaces(int n) {
		for (int i = 0; i < n; i++) {
			System.out.print(' ');
		}
	}
}
