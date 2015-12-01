package org.saucistophe.math;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class MachineGunTest
{
	@Test
	public void machineGunTest()
	{
		Map<Integer, Integer> results = new HashMap<>();
		for (int i = 0; i < 600; i++)
		{
			List<Integer> rep = MachineGunAlgorithm.bestRepartition(i);

			Assert.assertNotNull(rep);
			Assert.assertNotEquals(rep.size(), 0);

		}
	}
}
