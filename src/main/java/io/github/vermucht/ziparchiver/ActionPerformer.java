package io.github.vermucht.ziparchiver;

import io.github.vermucht.util.BiConsumerEx;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Consumer;

/**
 * Class to launch operations according to given action (archive, unarchive).
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class ActionPerformer {
    /**
     * Instances factory - to produce new objects of needed classes.
     * Is used to make using mocks in testing possible.
     */
    private final InstancesFactory factory;
    /**
     * Dispatch to link action type and operations to perform.
     */
    private Map<String, BiConsumerEx<String, ArgsParser>> actionDispatch = new HashMap<>();

    /**
     * Constructs new instance.
     *
     * @param factory Instances factory object.
     */
    public ActionPerformer(InstancesFactory factory) {
        this.factory = factory;
        this.initDispatch();
    }

    /**
     * Finds action type and performs it.
     *
     * @param args Command-line arguments.
     * @throws Exception In case of problems during operations.
     */
    public void perform(String[] args) throws Exception {
        if (args.length == 0 || "-h".equals(args[0])) {
            this.printHelp(System.out::println);
        } else {
            ArgsParser parser = this.factory.getArgsParser(args);
            this.validateHasNeededArguments(parser);
            String action = parser.getAction();
            this.actionDispatch
                    .getOrDefault(action, (act, pars) -> unknownAction(act))
                    .accept(action, parser);
        }
    }

    /**
     * Prints help message to consumer.
     */
    private void printHelp(Consumer<String> consumer) {
        consumer.accept(new StringJoiner(System.lineSeparator())
                .add("=== Simple zip archiver/unarchiver ===")
                .add("Usage: 'zip-archiver -a {action} -s {source} -o {output} -e {extensions}'")
                .add("Example: 'zip-archiver -a archive -s /home/john/project -o /home/john/my_project.zip -e xml,pdf,txt'")
                .add("Parameters:")
                .add("  -a {action}: action to do. Values:")
                .add("       archive: make zip archive from {source} folder to {output} file")
                .add("       unarchive: unzip files from {source} archive to {output} folder")
                .add("  -s {source}: folder with files to pack or zip file to unpack")
                .add("  -o {output}: new zip file path or output folder to create and write archive contents to")
                .add("  -e {extensions}, <optional>: extensions of files to take into archive,")
                .add(("      can work only on 'archive' action, ignored in 'unarchive action"))
                .toString()
        );
    }

    /**
     * Links action names and methods to call.
     */
    private void initDispatch() {
        this.actionDispatch.put("archive", (action, parser) -> archive(parser));
        this.actionDispatch.put("unarchive", (action, parser) -> unarchive(parser));
    }

    /**
     * Performs 'archive' operations.
     *
     * @param parser ArgsParser object with needed parameters.
     * @throws IOException In case of I/O problems.
     */
    private void archive(ArgsParser parser) throws IOException {
        ZipArchiver archiver = this.factory.getZipArchiver(parser.getExtensions());
        archiver.archive(parser.getInputPath(), parser.getOutputPath());
    }

    /**
     * Performs 'unarchive' operations.
     *
     * @param parser ArgsParser object with needed parameters.
     * @throws IOException In case of I/O problems.
     */
    private void unarchive(ArgsParser parser) throws IOException {
        ZipUnarchiver unarchiver = this.factory.getZipUnarchiver();
        unarchiver.unarchive(parser.getInputPath(), parser.getOutputPath());
    }

    /**
     * Is called if action key is unknown.
     *
     * @param action Action key.
     */
    private void unknownAction(String action) {
        throw new RuntimeException(String.format("Unknown action: %s", action));
    }

    /**
     * Checks if parser has all needed arguments to perform action.
     *
     * @param parser ArgsParser object with parameters.
     */
    private void validateHasNeededArguments(ArgsParser parser) {
        Map<String, String> params = Map.of(
                "sourceDir", parser.getInputPath(),
                "outputDir", parser.getOutputPath(),
                "action", parser.getAction()
        );
        params.forEach((key, value) -> {
            if ("".equals(value)) {
                throw new RuntimeException(String.format("Argument '%s' has empty value", key));
            }
        });
    }
}
