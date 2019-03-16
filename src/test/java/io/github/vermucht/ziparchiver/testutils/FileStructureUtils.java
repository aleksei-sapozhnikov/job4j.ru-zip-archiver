package io.github.vermucht.ziparchiver.testutils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Creates
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class FileStructureUtils {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(FileStructureUtils.class);

    public File createTempDirectory(String prefix) throws IOException {
        File temp = File.createTempFile(prefix, "");
        temp.delete();
        temp.mkdir();
        return temp;
    }

    public Map<String, List<File>> getAllFilesHierarchy(String root) {
        File innerDir = new File(root, "inner_dir");
        File emptyDir = new File(root, "empty_dir");
        List<File> dirs = Arrays.asList(
                innerDir,
                emptyDir
        );
        List<File> files = Arrays.asList(
                new File(root, "test_1.html"),
                new File(root, "test2.xml"),
                new File(innerDir, "test_inner_1.pdf"),
                new File(innerDir, "test_inner_2.xml"),
                innerDir,
                emptyDir
        );
        Map<String, List<File>> hierarchy = new HashMap<>();
        hierarchy.put("dirs", dirs);
        hierarchy.put("files", files);
        return hierarchy;
    }

    public Map<String, List<File>> getHtmlPdfFilesHierarchy(String root) {
        File innerDir = new File(root, "inner_dir");
        List<File> dirs = Arrays.asList(
                innerDir
        );
        List<File> files = Arrays.asList(
                new File(root, "test_1.html"),
                new File(innerDir, "test_inner_1.pdf"),
                innerDir
        );
        Map<String, List<File>> hierarchy = new HashMap<>();
        hierarchy.put("dirs", dirs);
        hierarchy.put("files", files);
        return hierarchy;
    }

    public void createAllFiles(Map<String, List<File>> hierarchy) throws IOException {
        hierarchy.get("dirs").forEach(File::mkdirs);
        for (File file : hierarchy.get("files")) {
            file.createNewFile();
        }
    }

    public boolean allExist(Map<String, List<File>> hierarchy) {
        var result = true;
        for (var file : hierarchy.values().stream().flatMap(Collection::stream).collect(Collectors.toList())) {
            if (!file.exists()) {
                result = false;
                break;
            }
        }
        return result;
    }

    public void deleteAllIfExist(Map<String, List<File>> hierarchy) {
        hierarchy.values().stream()
                .flatMap(Collection::stream)
                .forEach(File::delete);
    }
}
