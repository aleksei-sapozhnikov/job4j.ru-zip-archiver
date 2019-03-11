package console;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Creates zip archive from given directory.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class ZipArchiver {
    /**
     * Set of extensions needed.
     * Empty set means no need to check exceptions.
     */
    private final Set<String> extensions;

    /**
     * Constructs new instance.
     * Without checking extensions.
     */
    public ZipArchiver() {
        this(new HashSet<>());
    }

    /**
     * Constructs new instance.
     * With checking file extensions.
     *
     * @param extensions Set of extensions. Files having such extensions
     *                   must be taken into the archive.
     */
    public ZipArchiver(Set<String> extensions) {
        this.extensions = extensions;
    }

    /**
     * Creates archive from given directory.
     *
     * @param sourceDirPath Directory where to take contents from.
     * @param zipFilePath   Path where to create resulting zip file.
     * @throws IOException In case of I/O problems.
     */
    public void archive(String sourceDirPath, String zipFilePath) throws IOException {
        File source = new File(sourceDirPath);
        File output = new File(zipFilePath);
        int rootLength = source.getPath().length() + File.separator.length();
        try (FileOutputStream fOut = new FileOutputStream(output);
             ZipOutputStream zOut = new ZipOutputStream(fOut, StandardCharsets.UTF_8)
        ) {
            this.processDirectory(zOut, source, rootLength);
        }
    }

    /**
     * Adds all files in given directory to the archive.
     * Recursively adds inner directories to the archive.
     *
     * @param zOut       ZipOutputStream object (to write contents).
     * @param dir        Current directory object.
     * @param rootLength Length of path from system root to the main source
     *                   directory (to cut off substring).
     * @throws IOException In case of I/O problems.
     */
    private void processDirectory(ZipOutputStream zOut, File dir, int rootLength) throws IOException {
        File[] elements = dir.listFiles();
        elements = elements != null ? elements : new File[0];
        for (File element : elements) {
            if (element.isDirectory()) {
                this.addDirectory(zOut, element, rootLength);
                this.processDirectory(zOut, element, rootLength);
            } else {
                if (this.extensionMatches(element, this.extensions)) {
                    this.addFile(zOut, element, rootLength);
                }
            }
        }
    }

    /**
     * Adds directory to archive.
     *
     * @param zOut       ZipOutputStream object (to write contents).
     * @param directory  Directory to add.
     * @param rootLength Length of path from system root to the main source
     *                   directory (to cut off substring).
     * @throws IOException In case of I/O problems.
     */
    private void addDirectory(ZipOutputStream zOut, File directory, int rootLength) throws IOException {
        String path = this.normalize(
                String.format("%s/", directory.getPath().substring(rootLength)));
        zOut.putNextEntry(new ZipEntry(path));
        zOut.closeEntry();
    }

    /**
     * Adds file to archive.
     *
     * @param zOut       ZipOutputStream object (to write contents).
     * @param file       File to add.
     * @param rootLength Length of path from system root to the main source
     *                   directory (to cut off substring).
     * @throws IOException In case of I/O problems.
     */
    private void addFile(ZipOutputStream zOut, File file, int rootLength) throws IOException {
        String path = this.normalize(
                file.getPath().substring(rootLength));
        zOut.putNextEntry(new ZipEntry(path));
        try (FileInputStream fis = new FileInputStream(file)) {
            writeContents(fis, zOut);
            zOut.closeEntry();
        }
    }

    private String normalize(String path) {
        return path.replaceAll("\\\\", "/");
    }

    /**
     * Writes data from file to archive.
     *
     * @param fis  FileInputStream object (to read file).
     * @param zOut ZipOutputStream object (to write contents).
     * @throws IOException In case of I/O problems.
     */
    private void writeContents(FileInputStream fis, ZipOutputStream zOut) throws IOException {
        Utils.writeContents(fis, zOut);
    }

    /**
     * Checks if file extension matches needed.
     * If file has no extension and list of extensions is empty - file passes.
     * If file has no extension and list of extensions is NOT empty - file is discarded
     *
     * @param file   File object.
     * @param needed Set of needed extensions.
     * @return <tt>true</tt> if matches, <tt>false</tt> if not.
     */
    private boolean extensionMatches(File file, Set<String> needed) {
        boolean result = this.extensions.isEmpty();
        if (!result) {
            String name = file.getName();
            int extIndex = name.lastIndexOf(".") + 1;
            String extension = extIndex > 0 ? name.substring(extIndex) : "";
            if (needed.contains(extension)) {
                result = true;
            }
        }
        return result;
    }
}
