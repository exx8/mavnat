public class AVLSanitizer {
	public static int calcHeight(AVLTree.IAVLNode node)
	  {
		  if (node == null || !node.isRealNode())
			  return -1;
		  return Math.max(calcHeight(node.getLeft()), calcHeight(node.getRight())) + 1;
	  }
	  

	  
	  public static int calcSum(AVLTree.IAVLNode node)
	  {
		  if (node == null || !node.isRealNode())
			  return 0;
		  return calcSum(node.getLeft()) + calcSum(node.getRight()) + node.getKey();
	  }
	  
	  public static boolean sanitizeTree(AVLTree.IAVLNode root)
	  {
		  if (root == null)
			  return true;
		  
		  if (!root.isRealNode())
			  return true;
		  
		  if (calcHeight(root) != root.getHeight())
		  {
			  System.out.format("Height should be %d but is %d%n", calcHeight(root), root.getHeight());
			  return false;
		  }

		  
		  return sanitizeTree(root.getLeft()) && sanitizeTree(root.getRight());
	  }
	  
	  
	  public static boolean sanitizeTree(AVLTree tree)
	  {
		  return sanitizeTree(tree.getRoot());
	  }
}
