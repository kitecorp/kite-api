package cloud.kitelang.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a property as cloud-managed (read-only, set by the cloud provider).
 * In Kite schema syntax, this generates the @cloud decorator.
 * These properties cannot be set by the user when initializing the resource.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cloud {
}
