package org.saucistophe.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import static java.awt.FlowLayout.TRAILING;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.saucistophe.annotations.SettingsCallback;
import org.saucistophe.annotations.SettingsField;

/**
 A viewer that scans and displays the available settings, ready for edition.
 */
public class SettingsHandler
{
	private static final Logger LOGGER = Logger.getLogger(SettingsHandler.class.getName());

	/**
	 @return A list of the settable fields.
	 */
	public static List<Field> getSettableFields()
	{
		// Init the search in the root package.
		Reflections reflections = new Reflections("org.saucistophe", new FieldAnnotationsScanner());
		Set<Field> annotatedFields = reflections.getFieldsAnnotatedWith(SettingsField.class);

		// Turn the set to a list to sort it.
		List<Field> fieldsList = new ArrayList<>(annotatedFields);
		fieldsList.sort((Field field1, Field field2) ->
		{
			// Retrieve the fields info.
			SettingsField fieldInfo1 = field1.getAnnotation(SettingsField.class);
			SettingsField fieldInfo2 = field2.getAnnotation(SettingsField.class);

			// If the name wasn't set, get the field's declared name.
			String actualName1 = fieldInfo1.name().isEmpty() ? field1.getName() : fieldInfo1.name();
			String actualName2 = fieldInfo2.name().isEmpty() ? field2.getName() : fieldInfo2.name();

			// Elaborate a sortable string representation.
			String sortableString1 = fieldInfo1.category() + "." + actualName1;
			String sortableString2 = fieldInfo2.category() + "." + actualName2;

			return sortableString1.compareTo(sortableString2);
		});

		return fieldsList;
	}

	/**
	 @return A list of the settings callbacks.
	 */
	public static Set<Method> getSettingsCallbacks()
	{
		// Init the search in the root package.
		Reflections reflections = new Reflections("org.saucistophe", new MethodAnnotationsScanner());
		Set<Method> annotatedMethods = reflections.getMethodsAnnotatedWith(SettingsCallback.class);

		return annotatedMethods;
	}

	/**
	 Shows a swing dialog to eit the available settings.
	 */
	public static void showSettingsDialog()
	{
		// Create a tabbed panel, one tab per category.
		JTabbedPane categoriesPane = new JTabbedPane();

		// A list of actions taken when validating, i.e. when setting all settings to the input values.
		List<Supplier<Boolean>> actions = new ArrayList<>();

		// Create a panel.
		// For each available setting:
		for (Field settingField : getSettableFields())
		{
			// Retrieve the annotation.
			SettingsField fieldInfo = settingField.getAnnotation(SettingsField.class);

			// Search for an existing category panel.
			Integer categoryIndex = null;
			for (int candidateCategoryIndex = 0; candidateCategoryIndex < categoriesPane.getTabCount(); candidateCategoryIndex++)
			{
				if (categoriesPane.getTitleAt(candidateCategoryIndex).equals(fieldInfo.category()))
				{
					categoryIndex = candidateCategoryIndex;
					break;
				}
			}

			// If the category exists, retrieve its pane, otherwise create it.
			JPanel categoryPanel = null;
			if (categoryIndex != null)
			{
				categoryPanel = (JPanel) categoriesPane.getComponentAt(categoryIndex);
			}
			else
			{
				categoryPanel = new JPanel();
				categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.Y_AXIS));
				categoriesPane.add(categoryPanel, fieldInfo.category());
			}

			// If the name wasn't set, get the field's declared name.
			String actualName = fieldInfo.name().isEmpty() ? settingField.getName() : fieldInfo.name();

			// Prepare a label text.
			String labelText = fieldInfo.description().isEmpty() ? actualName : fieldInfo.description();
			// Create an optional label for component that need it.
			if(fieldInfo.requiresRestart())
				labelText += "*";
			JLabel label = new JLabel(labelText);

			JComponent editionComponent;

			// Handle enums.
			if (settingField.getType().isEnum())
			{
				JComboBox<?> comboBox = new JComboBox<>(settingField.getType().getEnumConstants());
				comboBox.setSelectedItem(get(settingField));
				actions.add(() ->
				{
					set(settingField, comboBox.getSelectedItem());
					return true;
				});
				// Add a label then the component.
				editionComponent = new JPanel(new FlowLayout(FlowLayout.LEFT));
				editionComponent.add(label);
				editionComponent.add(comboBox);
			}
			else
			{
				// Handle primitive types
				switch (settingField.getType().getSimpleName().toLowerCase())
				{
					case "boolean":
						JCheckBox checkbox = new JCheckBox(labelText, (boolean) get(settingField));
						editionComponent = checkbox;
						checkbox.setHorizontalTextPosition(SwingConstants.LEFT);
						actions.add(() ->
						{
							set(settingField, checkbox.isSelected());
							return true;
						});
						break;
					case "string":
						// If there are possible values, use a combobox.
						if (fieldInfo.possibleValues().length != 0)
						{
							JComboBox<String> comboBox = new JComboBox<>(fieldInfo.possibleValues());
							comboBox.setSelectedItem(get(settingField));
							actions.add(() ->
							{
								set(settingField, comboBox.getSelectedItem());
								return true;
							});
							// Add a label then the component.
							editionComponent = new JPanel(new FlowLayout(FlowLayout.LEFT));
							editionComponent.add(label);
							editionComponent.add(comboBox);
						}
						else
						{
							// Otherwise, use a simple text field.
							JTextField textField = new JTextField((String) get(settingField));
							actions.add(() ->
							{
								set(settingField, textField.getText());
								return true;
							});
							// Add a label then the component.
							editionComponent = new JPanel(new FlowLayout(FlowLayout.LEFT));
							editionComponent.add(label);
							editionComponent.add(textField);
						}
						break;
					case "int":
					case "integer":
						int currentIntValue = (int) get(settingField);
						SpinnerModel spinnerModel = new SpinnerNumberModel(currentIntValue, fieldInfo.minValue(), fieldInfo.maxValue(), 1);
						JSpinner spinner = new JSpinner(spinnerModel);

						spinner.setValue(currentIntValue);
						actions.add(() ->
						{
							set(settingField, spinner.getValue());
							return true;
						});
						// Add a label then the component.
						editionComponent = new JPanel(new FlowLayout(FlowLayout.LEFT));
						editionComponent.add(label);
						editionComponent.add(spinner);
						break;
					default:
						editionComponent = new JTextField("Unknown setting type");
						editionComponent.setEnabled(false);
						break;
				}
			}

			editionComponent.setAlignmentX( Component.LEFT_ALIGNMENT );
			categoryPanel.add(editionComponent);

			// Add a fancy tooltip.
			if (fieldInfo.description() != null && !fieldInfo.description().isEmpty())
			{
				editionComponent.setToolTipText(fieldInfo.description());
			}
		}

		// Put the panel in a modal frame.
		JDialog dialog = new JDialog((Frame) null, "Settings", true);
		dialog.add(categoriesPane);

		// Add a validation and cancel button.
		JPanel bottomButtons = new JPanel(new FlowLayout(TRAILING));
		dialog.add(bottomButtons, BorderLayout.SOUTH);
		JButton okButton = new JButton("OK");
		{
			okButton.addActionListener(e ->
			{
				for (Supplier<Boolean> action : actions)
				{
					action.get();
				}
				saveToFile();
				// Call all callbacks on all categories.
				Set<Method> callbacks = getSettingsCallbacks();
				callbacks.stream().forEach(m ->
				{
					try
					{
						m.invoke(null);
					}
					catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
					{
						Logger.getLogger(SettingsHandler.class.getName()).log(Level.SEVERE, null, ex);
					}
				});

				dialog.dispose();
			});
		}
		bottomButtons.add(okButton);
		JButton cancelButton = new JButton("Cancel");
		{
			cancelButton.addActionListener(e ->
			{
				dialog.dispose();
			});
		}
		bottomButtons.add(cancelButton);

		dialog.pack();
		dialog.setVisible(true);
	}

	/**
	 Saves the state of the current settings to a file.
	 */
	public static void saveToFile()
	{
		// Create a map of fields, indexed on categories then names.
		// Those are sorted maps, to preserve alphabetical order.
		SortedMap<String, SortedMap<String, Object>> categories = new TreeMap<>();

		for (Field field : getSettableFields())
		{
			// Retrieve the field info.
			SettingsField fieldInfo = field.getAnnotation(SettingsField.class);

			// Create a category map, if it doesn't exist.
			SortedMap<String, Object> category;
			if (categories.containsKey(fieldInfo.category()))
			{
				category = categories.get(fieldInfo.category());
			}
			else
			{
				category = new TreeMap<>();
				categories.put(fieldInfo.category(), category);
			}

			// If the name wasn't set, get the field's declared name.
			String actualName = fieldInfo.name().isEmpty() ? field.getName() : fieldInfo.name();

			// Put the field and its value, as a string.
			category.put(actualName, get(field).toString());
		}

		// Export the result to a json file.
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setPrettyPrinting();
		Gson gson = gsonBuilder.create();
		String jsonString = gson.toJson(categories);

		// Write to file.
		try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("settings.json")))
		{
			bufferedWriter.write(jsonString);
		}
		catch (IOException ex)
		{
			// If the file can't be written, it's an error.
			Logger.getLogger(SettingsHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 Reads the settings from the settings JSON file, and call all callbacks.
	 Typically used during the application init.
	 */
	public static void readFromFile()
	{
		Map<String, Map<String, Object>> categories;

		Gson gson = new Gson();

		// Read from file
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader("settings.json")))
		{
			@SuppressWarnings("unchecked")
			Map<String, Map<String, Object>> dummyCategories = gson.fromJson(bufferedReader, Map.class);
			categories = dummyCategories;

			// Then, for each setting, try to find its value.
			for (Field settableField : getSettableFields())
			{
				SettingsField fieldInfo = settableField.getAnnotation(SettingsField.class);
				if (categories.containsKey(fieldInfo.category()))
				{
					Map<String, Object> category = categories.get(fieldInfo.category());

					// If the name wasn't set, get the field's declared name.
					String actualName = fieldInfo.name().isEmpty() ? settableField.getName() : fieldInfo.name();
					if (category.containsKey(actualName))
						// Deserialize from String.
						set(settableField, toObject(settableField.getType(), (String) category.get(actualName)));
					else
						LOGGER.log(Level.CONFIG, "Field {0} was found in settings file but does not relate to any field.", actualName);
				}
				else
					LOGGER.log(Level.CONFIG, "Category {0} was found in settings file but does not relate to any field.", fieldInfo.category());
			}
		}
		catch (JsonSyntaxException exception)
		{
			// On incorrect properties, log a warning.
			LOGGER.log(Level.WARNING, "Syntax error in JSON settings file.");
			LOGGER.log(Level.WARNING, null, exception);
		}
		catch (IOException exception)
		{
			// If the file is not readable, or does not exist.
			LOGGER.log(Level.WARNING, "Could not read JSON settings file.");
		}
		finally
		{
			triggerCallbacks();
		}
	}

	/**
	 Call all callbacks on all categories.
	 */
	private static void triggerCallbacks()
	{
		Set<Method> callbacks = getSettingsCallbacks();
		callbacks.stream().forEach(m ->
		{
			try
			{
				m.invoke(null);
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
			{
				Logger.getLogger(SettingsHandler.class.getName()).log(Level.SEVERE, null, ex);
			}
		});
	}

	/**
	 Sets the value of the given static field.

	 @param field The field to set.
	 @param value The value to set.
	 */
	public static void set(Field field, Object value)
	{
		try
		{
			field.set(null, value);
		}
		catch (IllegalArgumentException | IllegalAccessException ex)
		{
			Logger.getLogger(SettingsHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 Gets the value of the given static field.

	 @param field The static field to get the values from.
	 @return Its value, or null if not found.
	 */
	public static Object get(Field field)
	{
		try
		{
			return field.get(null);
		}
		catch (IllegalArgumentException | IllegalAccessException ex)
		{
			Logger.getLogger(SettingsHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	/**
	 Gets the object value from its string serialisation.

	 @param clazz       The class of the value to get.
	 @param stringValue The string represnetation of the value.
	 @return The corresponding deserialized value.
	 */
	@SuppressWarnings({ "unchecked" })
	public static Object toObject(Class clazz, String stringValue)
	{
		// Ignore null values.
		if (stringValue == null)
		{
			return null;
		}

		// Handle enums.
		if (clazz.isEnum())
		{
			Class<? extends Enum> enumType = clazz;
			return Enum.valueOf(enumType, stringValue);
		}

		// Handle primitive types.
		switch (clazz.getSimpleName().toLowerCase())
		{
			case "boolean":
				return Boolean.parseBoolean(stringValue);
			case "byte":
				return Byte.parseByte(stringValue);
			case "short":
				return Short.parseShort(stringValue);
			case "integer":
			case "int":
				return Integer.parseInt(stringValue);
			case "long":
				return Long.parseLong(stringValue);
			case "float":
				return Float.parseFloat(stringValue);
			case "double":
				return Double.parseDouble(stringValue);
			default:
				return stringValue;
		}
	}
}
