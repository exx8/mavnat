public class AVLSanitizer {
	public static int calcHeight(AVLTree.IAVLNode node)
	  {
		  if (node == null || !node.isRealNode())
			  return -1;
		  return Math.max(calcHeight(node.getLeft()), calcHeight(node.getRight())) + 1;
	  }
	  
	  public static int calcSize(AVLTree.IAVLNode node)
	  {
		  if (node == null || !node.isRealNode())
			  return 0;
		  int sizeLeft = calcSize(node.getLeft());
		  int sizeRight = calcSize(node.getRight());
		  int size = sizeLeft + sizeRight + 1;
		  if (size != node.getSubtreeSize())
		  {
			  assert false;
		  }
		  return size;
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
		  
		  if (calcSize(root) != root.getSubtreeSize())
		  {
			  System.out.format("Size should be %d but is %d%n", calcSize(root), root.getSubtreeSize());
			  calcSize(root);
			  return false;
		  }
			  
		  if (calcSum(root) != root.getSubtreeSum())
		  {
			  System.out.format("Sum should be %d but is %d%n", calcSum(root), root.getSubtreeSum());
			  return false;
		  }
		  
		  int bf = calcHeight(root.getLeft()) - calcHeight(root.getRight());
		  if (Math.abs(bf) >= 2)
		  {
			  System.out.format("Balance factor should be out of range %d%n", bf, root.getSubtreeSize());
			  return false;
		  }
		  
		  return sanitizeTree(root.getLeft()) && sanitizeTree(root.getRight());
	  }
	  
	  
	  public static boolean sanitizeTree(AVLTree tree)
	  {
		  return sanitizeTree(tree.getRoot());
	  }
}
