package org.saucistophe.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 An annotation allowing the class to be aliased, for example for XML or JSON.
 */
@Target(value =
{
	ElementType.TYPE
})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface AliasedType
{
	public String alias();
}
