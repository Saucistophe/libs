package org.saucistophe.settings;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.saucistophe.annotations.SettingsField;
import org.saucistophe.stats.ImprovedRandom;

import java.io.File;

public class SettingsTest
{
	public enum testEnum
	{
		enum1, enum2
	}

	// A few settings for test purpose.
	@SettingsField(name = "intValue")
	public static int intValue;
	@SettingsField(name = "longValue")
	public static long longValue;
	@SettingsField(name = "doubleValue")
	public static double doubleValue;
	@SettingsField(name = "enum1")
	public static testEnum enumValue;
	@SettingsField(category = "category1", name = "booleanSettings1")
	public static boolean booleanSettings1;
	@SettingsField(category = "category2", name = "stringSettings1")
	public static String stringSettings2;

	public static void clear()
	{
		File settingsFile = new File("settings.json");
		settingsFile.delete();
	}

	@Test
	public void settingsStoreAndLoadTest()
	{
		// First delete the settings file, if any.
		clear();

		// Then set arbitrary values.
		ImprovedRandom random = new ImprovedRandom(0L);
		intValue = random.nextInt();
		longValue = random.nextLong();
		doubleValue = random.nextDouble();
		enumValue = testEnum.enum1;
		booleanSettings1 = random.nextBoolean();
		stringSettings2 = random.nextString(10);

		// Create a copy of the values.
		int originalIntValue = intValue;
		long originalLongValue = longValue;
		double originalDoubleValue = doubleValue;
		testEnum originalEnumValue = enumValue;
		boolean originalBooleanSettings1 = booleanSettings1;
		String originalStringSettings2 = stringSettings2;

		// Save to a file.
		SettingsHandler.saveToFile();

		// Erase the values.
		intValue = random.nextInt();
		longValue = random.nextLong();
		doubleValue = random.nextDouble();
		enumValue = testEnum.enum2;
		booleanSettings1 = !booleanSettings1;
		stringSettings2 = "";

		// Reload, and ensure they are the same.
		SettingsHandler.readFromFile();
		Assertions.assertEquals(originalIntValue, intValue);
		Assertions.assertEquals(originalLongValue, longValue);
		Assertions.assertEquals(originalEnumValue, enumValue);
		Assertions.assertEquals((double) originalDoubleValue, doubleValue, 0.01);
		Assertions.assertEquals(originalBooleanSettings1, booleanSettings1);
		Assertions.assertEquals(originalStringSettings2, stringSettings2);

		clear();
	}
}
