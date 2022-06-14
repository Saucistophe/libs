package org.saucistophe.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MachineGunTest
{
	@Test
	public void machineGunTest()
	{
		Map<Integer, Integer> results = new HashMap<>();
		for (int i = 0; i < 600; i++)
		{
			List<Integer> rep = MachineGunAlgorithm.bestRepartition(i);

			Assertions.assertNotNull(rep);
			Assertions.assertNotEquals(rep.size(), 0);
		}
	}
}
