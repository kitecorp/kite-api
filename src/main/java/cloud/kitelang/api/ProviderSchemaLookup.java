package cloud.kitelang.api;

import java.util.List;

/**
 * Interface for looking up provider schemas during import resolution.
 * This allows the language module to resolve provider imports without
 * depending on the engine module.
 *
 * <p>Provider imports use syntax like:
 * <pre>
 * import Vpc, Subnet from "aws/networking"
 * import S3Bucket from "aws/storage"
 * import * from "aws"
 * </pre>
 */
public interface ProviderSchemaLookup {

    /**
     * Schema definition containing type name, domain, provider name, and schema string.
     */
    record SchemaInfo(
            String typeName,
            String domain,
            String providerName,
            String schemaString
    ) {}

    /**
     * Gets all schemas for a specific provider and domain.
     *
     * @param provider The provider name (e.g., "aws", "gcp")
     * @param domain The domain name (e.g., "networking", "storage")
     * @return List of schema definitions, or empty list if not found
     */
    List<SchemaInfo> getSchemas(String provider, String domain);

    /**
     * Gets all schemas for a provider (across all domains).
     *
     * @param provider The provider name
     * @return List of all schema definitions for the provider
     */
    List<SchemaInfo> getAllSchemas(String provider);

    /**
     * Checks if a provider is known/registered.
     *
     * @param provider The provider name to check
     * @return true if the provider has registered schemas
     */
    boolean isKnownProvider(String provider);

    /**
     * Gets a specific schema by type name.
     *
     * @param typeName The resource type name
     * @return The schema info, or null if not found
     */
    SchemaInfo getSchema(String typeName);
}
