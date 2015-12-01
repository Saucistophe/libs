package org.saucistophe.patterns;

/**
 An updatable can be refreshed when its update() method is called.
 The updatable will refresh itself from the given data.
 */
public interface Updatable<T>
{
	/**
	 Triggers an update of this element.

	 @param newValue The value to take data from.
	 */
	public void update(T newValue);
}
