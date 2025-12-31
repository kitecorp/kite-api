package cloud.kitelang.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a property as cloud-managed (read-only, set by the cloud provider).
 * In Kite schema syntax, this generates the @cloud decorator.
 * These properties cannot be set by the user when initializing the resource.
 *
 * Use {@code importable = true} for properties that can be used to import
 * existing cloud resources (e.g., ARN for AWS, resource ID for Azure).
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cloud {
    /**
     * Whether this property can be used to import existing resources.
     * Set to true for identifiers like ARN (AWS), resource ID (Azure).
     */
    boolean importable() default false;
}
