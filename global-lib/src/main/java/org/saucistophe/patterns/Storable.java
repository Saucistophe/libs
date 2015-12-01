package org.saucistophe.patterns;

import org.saucistophe.exceptions.FormatException;

/**
 * A Storable can be serialized to a simple String, and loaded from such a string.
 */
public interface Storable<T>
{
	public String toStorageString();
	public T fromStorageString(String storageString) throws FormatException;
}
