package org.saucistophe.patterns;

/**
 A refreshable can be refreshed when its refresh() method is called.
 The refreshable itself will gather the updated data.
 */
public interface Refreshable
{
	/**
	 Triggers a refresh of this element.
	 */
	public void refresh();
}
