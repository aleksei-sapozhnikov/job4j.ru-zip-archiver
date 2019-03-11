package console;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Un-archives zip archive into given directory.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class ZipUnarchiver {

    /**
     * Un-archives given zip file to given directory.
     *
     * @param zipFilePath Path to the zip file.
     * @param outputPath  Directory where to put result into.
     * @throws IOException In case of I/O problems.
     */
    public void unarchive(String zipFilePath, String outputPath) throws IOException {
        try (FileInputStream fIn = new FileInputStream(zipFilePath);
             ZipInputStream zIn = new ZipInputStream(fIn, StandardCharsets.UTF_8)
        ) {
            new File(outputPath).mkdirs();
            boolean process = this.writeNextEntry(zIn, outputPath);
            while (process) {
                process = this.writeNextEntry(zIn, outputPath);
            }
        }
    }

    /**
     * Gets next zip entry and un-archives it.
     *
     * @param zInput         ZipInputStream object (to read data from).
     * @param resultRootPath Result main directory path.
     * @return <tt>true</tt> if there can be more entries,
     * <tt>false</tt> if there is no.
     * @throws IOException In case of I/O problems.
     */
    private boolean writeNextEntry(ZipInputStream zInput, String resultRootPath) throws IOException {
        boolean process = false;
        ZipEntry entry = zInput.getNextEntry();
        if (entry != null) {
            if (entry.isDirectory()) {
                createDirectory(resultRootPath, entry);
            } else {
                createAndWriteFile(zInput, resultRootPath, entry);
            }
            process = true;
        }
        return process;
    }

    /**
     * Creates new directory using information from zip entry.
     *
     * @param resultRootPath Root path where to un-archive.
     * @param entry          Zip entry to get info from.
     */
    private void createDirectory(String resultRootPath, ZipEntry entry) {
        String path = String.format("%s/%s", resultRootPath, this.deleteLastSlash(entry.getName()));
        new File(path).mkdirs();
    }

    /**
     * Creates new file and writes there contents from archive file.
     *
     * @param zInput         ZipInputStream object (to read data from).
     * @param resultRootPath Result main directory path.
     * @param entry          Zip entry to get info from.
     * @throws IOException In case of I/O problems.
     */
    private void createAndWriteFile(ZipInputStream zInput, String resultRootPath, ZipEntry entry) throws IOException {
        File output = new File(resultRootPath, entry.getName());
        new File(output.getParent()).mkdirs();
        this.writeContent(zInput, output);
    }

    /**
     * Removes last '/' symbol from the directory name.
     * The slash is used in zip archive to write any directory (even empty).
     * But to create directory we need path without ending '/' symbol.
     * <p>
     * E.g.: if directory name in entry is 'mydir/', the method returns 'mydir'.
     *
     * @param dirName Directory name in zip entry with ending slash symbol.
     * @return Name without last slash symbol ('mydir/' --> 'mydir').
     */
    private String deleteLastSlash(String dirName) {
        return dirName.endsWith("/") ? dirName.substring(0, dirName.length() - 1) : dirName;
    }

    /**
     * Writes content from given stream to file.
     *
     * @param zInput ZipInputStream object (to read data from).
     * @param output Output file (to write data into).
     * @throws IOException In case of I/O problems.
     */
    private void writeContent(ZipInputStream zInput, File output) throws IOException {
        try (FileOutputStream fOut = new FileOutputStream(output)) {
            Utils.writeContents(zInput, fOut);
        }
    }


}
