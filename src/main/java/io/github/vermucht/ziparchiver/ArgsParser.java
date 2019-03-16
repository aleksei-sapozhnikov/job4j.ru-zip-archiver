package io.github.vermucht.ziparchiver;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * Parses and holds given command-line application arguments.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class ArgsParser {
    /**
     * Input path.
     */
    private final String inputPath;
    /**
     * Output path.
     */
    private final String outputPath;
    /**
     * Set of extensions needed.
     * Empty set means no need to check exceptions.
     */
    private final Set<String> extensions;
    /**
     * Action to do: 'archive' or 'unarchive'.
     */
    private final String action;

    /**
     * Constructs new instance.
     *
     * @param builder Builder object to take parameters from.
     */
    private ArgsParser(Builder builder) {
        this.inputPath = builder.inputPath;
        this.outputPath = builder.outputPath;
        this.extensions = builder.extensions;
        this.action = builder.action;
    }

    /**
     * Returns inputPath.
     *
     * @return Value of inputPath field.
     */
    public String getInputPath() {
        return this.inputPath;
    }

    /**
     * Returns extensions.
     *
     * @return Value of extensions field.
     */
    public Set<String> getExtensions() {
        return this.extensions;
    }

    /**
     * Returns outputPath.
     *
     * @return Value of outputPath field.
     */
    public String getOutputPath() {
        return this.outputPath;
    }

    /**
     * Returns action.
     *
     * @return Value of action field.
     */
    public String getAction() {
        return this.action;
    }

    /**
     * Builder class. Needed to construct
     * ArgsParser object with final fields.
     */
    public static class Builder {
        /**
         * Dispatcher of methods to use in case of different arguments.
         */
        private final Map<String, BiConsumer<String, String>> argDispatch = new HashMap<>();

        /**
         * Input path.
         */
        private String inputPath = "";
        /**
         * Output path.
         */
        private String outputPath = "";
        /**
         * Set of extensions needed.
         * Empty set means no need to check exceptions.
         */
        private Set<String> extensions = new HashSet<>();
        /**
         * Action to do: 'archive' or 'unarchive'.
         */
        private String action = "";

        /**
         * Constructs new instance.
         *
         * @param args Command-line arguments.
         */
        public Builder(String[] args) {
            this.initArgDispatch();
            this.validateArgsLength(args);
            this.parseArgs(args);
        }

        /**
         * Puts parameter keys to argument dispatcher.
         */
        private void initArgDispatch() {
            this.argDispatch.put("-a", (param, value) -> setAction(value));
            this.argDispatch.put("-s", (param, value) -> setInputPath(value));
            this.argDispatch.put("-e", (param, value) -> setExtensions(value));
            this.argDispatch.put("-o", (param, value) -> setOutputPath(value));
        }

        /**
         * Validates length of parameters.
         *
         * @param args Command-line arguments.
         */
        private void validateArgsLength(String[] args) {
            if (args.length % 2 != 0) {
                throw new RuntimeException("Each parameter must have value");
            }
        }

        /**
         * Reads all arguments and redirects them to needed action.
         *
         * @param args Command-line arguments.
         */
        private void parseArgs(String[] args) {
            for (int i = 0; i < args.length - 1; i += 2) {
                String param = args[i];
                String value = args[i + 1];
                this.argDispatch.getOrDefault(param, (par, val) -> unknownParam(par)).accept(param, value);
            }
        }

        /**
         * Sets 'inputPath' field value and returns this builder.
         *
         * @param value Value to set.
         */
        private void setInputPath(String value) {
            this.inputPath = value;
        }

        /**
         * Sets 'outputPath' field value and returns this builder.
         *
         * @param value Value to set.
         */
        private void setOutputPath(String value) {
            this.outputPath = value;
        }

        /**
         * Sets 'extensions' field value and returns this builder.
         *
         * @param values Value to set.
         */
        private void setExtensions(String values) {
            this.extensions.addAll(Arrays.asList(
                    values.split(",")
            ));
        }

        /**
         * Sets 'action' field value and returns this builder.
         *
         * @param value Value to set.
         */
        private void setAction(String value) {
            this.action = value;
        }

        /**
         * The method is called in case of unknown parameter key found.
         *
         * @param param Parameter key.
         */
        private void unknownParam(String param) {
            throw new RuntimeException(String.format("Unknown parameter: %s", param));
        }

        /**
         * Returns new ArgsDispatcher object using this builder.
         *
         * @return New ArgsDispatcher object.
         */
        public ArgsParser build() {
            return new ArgsParser(this);
        }
    }
}
