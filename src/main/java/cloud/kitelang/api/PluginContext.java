package cloud.kitelang.api;

/**
 * Context information for plugins.
 */
public class PluginContext {

    public enum RuntimeMode {
        DEVELOPMENT, DEPLOYMENT
    }

    private final RuntimeMode runtimeMode;

    public PluginContext(RuntimeMode runtimeMode) {
        this.runtimeMode = runtimeMode;
    }

    public RuntimeMode getRuntimeMode() {
        return runtimeMode;
    }
}
