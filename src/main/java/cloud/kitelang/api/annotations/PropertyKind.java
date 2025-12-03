package cloud.kitelang.api.annotations;

/**
 * Defines the kind of a schema property.
 */
public enum PropertyKind {
    /**
     * Regular property - no special meaning
     */
    REGULAR,

    /**
     * Input property - user must provide this value
     */
    INPUT,

    /**
     * Output property - cloud-provided value after provisioning
     */
    OUTPUT
}