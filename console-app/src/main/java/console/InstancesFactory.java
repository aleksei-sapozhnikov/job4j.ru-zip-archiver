package console;

import java.util.Set;

/**
 * Factory to create instances.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class InstancesFactory {

    /**
     * Returns new ZipArchiver object.
     *
     * @param extensions Set of extensions. Files having such extensions
     *                   must be taken into the archive.
     * @return New ZipArchiver object.
     */
    public ZipArchiver getZipArchiver(Set<String> extensions) {
        return new ZipArchiver(extensions);
    }

    /**
     * Returns new ZipUnarchiver object.
     *
     * @return New ZipUnarchiver object.
     */
    public ZipUnarchiver getZipUnarchiver() {
        return new ZipUnarchiver();
    }

    /**
     * Returns new ArgsParser object.
     *
     * @param args Command-line arguments to parse.
     * @return New ArgsParser object.
     */
    public ArgsParser getArgsParser(String[] args) {
        return new ArgsParser.Builder(args).build();
    }
}
