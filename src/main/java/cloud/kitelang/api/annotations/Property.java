package cloud.kitelang.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field as a resource property in Kite.
 *
 * <p>Properties are the configurable attributes of a resource that users can set
 * in their Kite configuration files. Each property becomes part of the resource's
 * schema and can be validated, documented, and processed by the Kite engine.</p>
 *
 * <h2>Basic Usage</h2>
 * <pre>{@code
 * @TypeName("File")
 * public class FileResource {
 *     @Property
 *     private String path;
 *
 *     @Property
 *     private String content;
 *
 *     @Property(optional = false)
 *     private String name;
 * }
 * }</pre>
 *
 * <h2>With Cloud-Managed Properties</h2>
 * <p>Use {@link Cloud} annotation alongside {@code @Property} for read-only
 * properties that are set by the cloud provider after resource creation:</p>
 * <pre>{@code
 * @Property
 * private String cidrBlock;  // User-configurable
 *
 * @Cloud
 * @Property
 * private String vpcId;      // Set by AWS after creation
 * }</pre>
 *
 * <h2>Custom Property Names</h2>
 * <p>By default, the field name is used as the property name. Use {@code name}
 * to specify a different name (e.g., for snake_case in config files):</p>
 * <pre>{@code
 * @Property(name = "cidr_block")
 * private String cidrBlock;
 * }</pre>
 *
 * <h2>Resulting Kite Schema</h2>
 * <p>Properties are exposed in the Kite DSL as:</p>
 * <pre>
 * resource File main {
 *     path = "/tmp/file.txt"
 *     content = "Hello World"
 *     name = "main"
 * }
 * </pre>
 *
 * @see Cloud
 * @see TypeName
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {

    /**
     * Custom name for the property in Kite configuration.
     *
     * <p>If not specified, the Java field name is used. Use this to specify
     * snake_case names or other naming conventions:</p>
     * <pre>{@code
     * @Property(name = "enable_dns_support")
     * private Boolean enableDnsSupport;
     * }</pre>
     *
     * @return the property name, or empty string to use field name
     */
    String name() default "";

    /**
     * Human-readable description of the property.
     *
     * <p>Used for documentation generation and IDE support:</p>
     * <pre>{@code
     * @Property(description = "The IPv4 CIDR block for the VPC (e.g., 10.0.0.0/16)")
     * private String cidrBlock;
     * }</pre>
     *
     * @return the description text
     */
    String description() default "";

    /**
     * Deprecation message if this property is deprecated.
     *
     * <p>When set, Kite will emit a warning when this property is used:</p>
     * <pre>{@code
     * @Property(deprecationMessage = "Use 'instance_type' instead")
     * private String instanceSize;
     * }</pre>
     *
     * @return the deprecation message, or empty if not deprecated
     */
    String deprecationMessage() default "";

    /**
     * Whether this property is optional.
     *
     * <p>Optional properties don't need to be specified in configuration.
     * Required properties (optional=false) will cause validation errors if missing:</p>
     * <pre>{@code
     * @Property(optional = false)  // Required - must be specified
     * private String name;
     *
     * @Property  // Optional by default
     * private String description;
     * }</pre>
     *
     * @return true if optional (default), false if required
     */
    boolean optional() default true;

    /**
     * Whether this property can be used for resource import.
     *
     * <p>Importable properties are used to identify existing resources when
     * importing them into Kite state. Typically identifiers like IDs or ARNs:</p>
     * <pre>{@code
     * @Property(importable = true)
     * private String vpcId;  // Can import: kite import Vpc.main vpc-12345
     * }</pre>
     *
     * @return true if the property can be used for import
     */
    boolean importable() default false;

    /**
     * Whether this property is hidden from schema output.
     *
     * <p>Hidden properties are not shown in documentation or schema introspection.
     * Useful for internal implementation details:</p>
     * <pre>{@code
     * @Property(hidden = true)
     * private String internalState;
     * }</pre>
     *
     * @return true if hidden from schema
     */
    boolean hidden() default false;
}
