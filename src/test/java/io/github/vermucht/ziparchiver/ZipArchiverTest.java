package io.github.vermucht.ziparchiver;

import io.github.vermucht.ziparchiver.testutils.FileStructureUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ZipArchiverTest {

    private final FileStructureUtils utils = new FileStructureUtils();

    private final ZipArchiver test = new ZipArchiver();

    private final String sourcePath;
    private final String zipFilePath;

    public ZipArchiverTest() throws IOException {
        File root = this.utils.createTempDirectory("ZipArchiverTest");
        this.sourcePath = String.format("%s/%s", root, "source");
        this.zipFilePath = String.format("%s/%s", root, "result.zip");
        //
        var sourceHierarchy = this.utils.getAllFilesHierarchy(this.sourcePath);
        this.utils.createAllFiles(sourceHierarchy);
    }

    @Test
    public void whenArchiveThenZipFileCreated() throws IOException {
        File zipFile = new File(this.zipFilePath);
        zipFile.delete();
        assertThat(zipFile.exists(), is(false));
        this.test.archive(this.sourcePath, this.zipFilePath);
        assertThat(zipFile.exists(), is(true));
        assertThat(this.isZipArchive(zipFile), is(true));
    }

    /**
     * Checks if given file is zip archive.
     * https://stackoverflow.com/questions/33934178/how-to-identify-a-zip-file-in-java
     *
     * @param file File to check.
     * @return <tt>true</tt> if identified as zip archive, <tt>false</tt> if not.
     * @throws IOException If I/O error occurs.
     */
    private boolean isZipArchive(File file) throws IOException {
        boolean result = false;
        long[] needed = new long[]{
                0x504B0304, // normal archive
                0x504B0506, // empty archive
                0x504B0708  // spanned zip archive
        };
        try (RandomAccessFile rFile = new RandomAccessFile(file, "r")) {
            long n = rFile.readInt();
            for (long value : needed) {
                if (n == value) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }


}