package org.saucistophe.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 An annotation indicating the callback associated to a category, that is processed at the settings loading and edition.
 */
@Target(value =
{
	ElementType.METHOD
})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface SettingsCallback
{
	/**
	 @return The settings category.
	 */
	public String category() default "General";
}
