package org.saucistophe.geometry;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.saucistophe.geometry.threeDimensional.Point3F;
import org.saucistophe.geometry.twoDimensional.KdTreeNode;

public class KdTreeTest
{
	@Test
	public void touchyMedianTest()
	{
		List<Point3F> points = new ArrayList<>();
		points.add(new Point3F(2, 3, 0));
		points.add(new Point3F(2, 4, 0));
		points.add(new Point3F(4, 3, 0));

		KdTreeNode tree = KdTreeNode.buildTree(points);
		String treeToString = tree.toString();
		assert (treeToString != null && treeToString.length() > 0);
	}

}
