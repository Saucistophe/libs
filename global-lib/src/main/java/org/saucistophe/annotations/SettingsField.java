package org.saucistophe.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 An annotation allowing the class to be handled by a SettingsViewer.
 */
@Target(value =
{
	ElementType.FIELD
})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface SettingsField
{
	/**
	 @return The setting's human readable name. If not set, defaults to the field's name.
	 */
	public String name() default "";

	/**
	 @return The setting's description.
	 */
	public String description() default "";

	/**
	 @return The settings category.
	 */
	public String category() default "General";

	/**
	@return The possible string value, for example for a dropdown list.
	*/
	public String[] possibleValues() default {};
}
