package cloud.kitelang.api;

import java.util.logging.Logger;

/**
 * Base class for Kite plugins.
 * Plugins can override start() and stop() for lifecycle hooks.
 */
public class Plugin {
    private static final Logger logger = Logger.getLogger(Plugin.class.getName());

    public void start() {
        logger.info(this.getClass().getName() + " started");
    }

    public void stop() {
        logger.info(this.getClass().getName() + " stopped");
    }

    public void delete() {
        // Override if cleanup is needed
    }
}
