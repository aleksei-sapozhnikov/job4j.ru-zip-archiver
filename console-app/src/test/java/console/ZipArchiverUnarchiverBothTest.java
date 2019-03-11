package console;

import console.ZipArchiver;
import console.ZipUnarchiver;
import console.testutils.FileStructureUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ZipArchiverUnarchiverBothTest {

    private final FileStructureUtils utils = new FileStructureUtils();

    private final String sourcePath;
    private final String zipFilePath;
    private final String resultPath;

    public ZipArchiverUnarchiverBothTest() throws IOException {
        File root = this.utils.createTempDirectory("ZipArchiverUnarchiverBothTest");
        this.sourcePath = String.format("%s/%s", root, "source");
        this.zipFilePath = String.format("%s/%s", root, "archive.zip");
        this.resultPath = String.format("%s/%s", root, "result");
        //
        var sourceHierarchy = this.utils.getAllFilesHierarchy(this.sourcePath);
        this.utils.createAllFiles(sourceHierarchy);
    }

    @Before
    public void deleteExistingResultFiles() {
        var resultHierarchy = this.utils.getAllFilesHierarchy(this.resultPath);
        this.utils.deleteAllIfExist(resultHierarchy);
        new File(this.resultPath).delete();
    }

    @Test
    public void whenArchiveUnarchiveThenProjectTheSame() throws IOException {
        ZipArchiver archiver = new ZipArchiver();
        ZipUnarchiver unarchiver = new ZipUnarchiver();
        //
        var hierarchyBefore = this.utils.getAllFilesHierarchy(this.sourcePath);
        var hierarchyAfter = this.utils.getAllFilesHierarchy(this.resultPath);
        //
        assertThat(this.utils.allExist(hierarchyBefore), is(true));
        assertThat(this.utils.allExist(hierarchyAfter), is(false));
        //
        archiver.archive(this.sourcePath, this.zipFilePath);
        assertThat(new File(this.resultPath).exists(), is(false));
        unarchiver.unarchive(this.zipFilePath, this.resultPath);
        //
        assertThat(this.utils.allExist(hierarchyAfter), is(true));
    }

    @Test
    public void whenArchiveSpecificExtensionsAndUnarchiveThenOnlySpecifiedExtensionsLeft() throws IOException {
        ZipArchiver archiver = new ZipArchiver(Set.of("html", "pdf"));
        ZipUnarchiver unarchiver = new ZipUnarchiver();
        //
        var hierarchyBefore = this.utils.getAllFilesHierarchy(this.sourcePath);
        var hierarchyAfterAllFiles = this.utils.getAllFilesHierarchy(this.resultPath);
        var hierarchyAfterSpecific = this.utils.getHtmlPdfFilesHierarchy(this.resultPath);
        //
        assertThat(this.utils.allExist(hierarchyBefore), is(true));
        assertThat(this.utils.allExist(hierarchyAfterAllFiles), is(false));
        assertThat(this.utils.allExist(hierarchyAfterSpecific), is(false));
        //
        archiver.archive(this.sourcePath, this.zipFilePath);
        unarchiver.unarchive(this.zipFilePath, this.resultPath);
        //
        assertThat(this.utils.allExist(hierarchyAfterAllFiles), is(false));
        assertThat(this.utils.allExist(hierarchyAfterSpecific), is(true));
    }
}
