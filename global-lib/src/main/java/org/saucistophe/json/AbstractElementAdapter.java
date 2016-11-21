package org.saucistophe.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.saucistophe.annotations.AliasedType;

/**
 An adapter to help Gson sort abstract classes out.

 @param <T> The type of object to serialize/deserialize.
 */
public class AbstractElementAdapter<T extends Object> implements JsonSerializer<T>, JsonDeserializer<T>
{
	/**
	 A map to get a class from its String representation.
	 */
	public Map<String, Class> aliasToClassMap = new HashMap<>();

	/**
	 A map to get a String representation from a class.
	 */
	public Map<Class, String> classToAliasMap = new HashMap<>();

	/**
	 Constructs an adapter from a mapping.

	 @param clazz The root class to handle. The implementing classes MUST be in the same package or subpackage as the
	 root class.
	 */
	public AbstractElementAdapter(Class<T> clazz)
	{
		// List the subclasses.
		Reflections reflections = new Reflections(clazz.getPackage().getName());
		Set<Class<? extends T>> subClasses = reflections.getSubTypesOf(clazz);

		// Register each subclass.
		for (Class<? extends T> subClass : subClasses)
		{
			// Skip abstract classes.
			if (Modifier.isAbstract(subClass.getModifiers()))
			{
				continue;
			}

			// If the subclass has an alias, use it; otherwise, use the name of the class.
			String alias;
			if (subClass.isAnnotationPresent(AliasedType.class))
			{
				alias = subClass.getAnnotation(AliasedType.class).alias();
			} else
			{
				alias = subClass.getSimpleName();
			}
			aliasToClassMap.put(alias, subClass);
			classToAliasMap.put(subClass, alias);
		}
	}

	@Override
	public JsonElement serialize(T object, Type type, JsonSerializationContext context)
	{
		JsonObject result = new JsonObject();

		// If the type is not found, raise an error.
		if (!classToAliasMap.containsKey(object.getClass()))
		{
			throw new JsonParseException(new ClassNotFoundException(object.getClass().toString()));
		}

		// Store the type in a tag, and the object in another.
		result.add("type", new JsonPrimitive(classToAliasMap.get(object.getClass())));

		result.add("properties", context.serialize(object, object.getClass()));

		return result;
	}

	@Override
	public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException
	{
		JsonObject jsonObject = json.getAsJsonObject();
		
		// Retrieve the type.
		JsonElement typeObject = jsonObject.get("type");
		if( typeObject == null)
			throw new JsonParseException("Missing type for deserialization.");
		String type = jsonObject.get("type").getAsString();

		// If the type is not found, raise an error.
		if (!aliasToClassMap.containsKey(type))
		{
			throw new JsonParseException(new ClassNotFoundException(type));
		}
		JsonElement element = jsonObject.get("properties");
		if(element == null)
			element = new JsonObject();

		return context.deserialize(element, aliasToClassMap.get(type));
	}
}
