package io.github.vermucht.ziparchiver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main application launcher.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class Main {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(Main.class);

    /**
     * Starts archiver application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        try {
            new ActionPerformer(new InstancesFactory()).perform(args);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
