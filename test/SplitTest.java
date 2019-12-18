import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class SplitTest {
	@Test
	void splitTest() {
		AVLTree tree=new AVLTree();
		tree.insert(6, "a");
		tree.insert(7, "a");
		tree.insert(20, "a");
		tree.insert(1, "a");
		tree.insert(3, "a");
		TreePrinter.print(tree);
		AVLTree[] trees=tree.split(7);
		TreePrinter.print(trees[0]);
		TreePrinter.print(trees[1]);
	}

	@Test
	void sanity() {
		Random rand = new Random();
		int k = 0, splitter = 0;
		AVLTree a = new AVLTree(), b, c;
		for (int i = 0; i < 100; i++) {
			k += rand.nextInt(6);
			a.insert(k, Integer.toString(k));
			if (Math.random() > 0.9) ;
			splitter = k;


		}
		int original_size=a.size();
		AVLTree[] arr = a.split(splitter);
		b = arr[0];
		c = arr[1];
		if (Integer.parseInt(b.max()) >= splitter)
			fail();
		if (Integer.parseInt(c.max()) <= splitter)
			fail();
		assertEquals(b.size()+c.size(),original_size-1);


	}
}
