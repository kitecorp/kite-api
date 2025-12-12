package cloud.kitelang.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines the resource type name as it appears in Kite configuration.
 *
 * <p>Every resource class must have this annotation to be discoverable by Kite.
 * The value becomes the type name used in {@code resource TypeName name { }} blocks.</p>
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * @TypeName("Vpc")
 * @Data
 * public class VpcResource {
 *     @Property
 *     private String cidrBlock;
 * }
 * }</pre>
 *
 * <h2>Resulting Kite Syntax</h2>
 * <pre>
 * resource Vpc main {
 *     cidr_block = "10.0.0.0/16"
 * }
 * </pre>
 *
 * <h2>Naming Conventions</h2>
 * <ul>
 *   <li>Use <b>PascalCase</b> for type names (e.g., "Vpc", "S3Bucket", "SecurityGroup")</li>
 *   <li>Keep names concise but descriptive</li>
 *   <li>Match AWS/cloud provider naming where applicable</li>
 *   <li>The Java class name doesn't need to match (VpcResource class can have @TypeName("Vpc"))</li>
 * </ul>
 *
 * <h2>Auto-Discovery</h2>
 * <p>Classes with {@code @TypeName} are automatically discovered by
 * {@link cloud.kitelang.provider.KiteProvider#discoverResources()}:</p>
 * <pre>{@code
 * public class AwsProvider extends KiteProvider {
 *     public AwsProvider() {
 *         super("aws", "1.0.0");
 *         discoverResources();  // Finds all @TypeName annotated classes
 *     }
 * }
 * }</pre>
 *
 * <h2>GraalVM Native Image</h2>
 * <p>For GraalVM compatibility, an annotation processor generates
 * {@code META-INF/kite/resource-types.txt} at compile time listing all
 * {@code @TypeName} classes, eliminating the need for runtime reflection.</p>
 *
 * @see Property
 * @see Cloud
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TypeName {

    /**
     * The resource type name as it appears in Kite configuration.
     *
     * <p>Examples:</p>
     * <ul>
     *   <li>{@code @TypeName("Vpc")} - AWS VPC</li>
     *   <li>{@code @TypeName("S3Bucket")} - AWS S3 bucket</li>
     *   <li>{@code @TypeName("File")} - Local file resource</li>
     *   <li>{@code @TypeName("SecurityGroup")} - AWS security group</li>
     * </ul>
     *
     * @return the type name in PascalCase
     */
    String value();
}
