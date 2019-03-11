package console;

import console.testutils.FileStructureUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ComplexMainTest {

    private final FileStructureUtils utils = new FileStructureUtils();

    private final String sourcePath;
    private final String zipFilePath;
    private final String resultPath;

    public ComplexMainTest() throws IOException {
        File root = this.utils.createTempDirectory("ComplexMainTest");
        this.sourcePath = String.format("%s/%s", root, "source");
        this.zipFilePath = String.format("%s/%s", root, "archive.zip");
        this.resultPath = String.format("%s/%s", root, "result");
        //
        var sourceHierarchy = this.utils.getAllFilesHierarchy(this.sourcePath);
        this.utils.createAllFiles(sourceHierarchy);
    }

    @Before
    public void deleteExistingResultFiles() {
        new File(this.zipFilePath).delete();
        var resultHierarchy = this.utils.getAllFilesHierarchy(this.resultPath);
        this.utils.deleteAllIfExist(resultHierarchy);
    }

    @Test
    public void makeArchiveThenUnarchive() {
        var sourceHierarchy = this.utils.getAllFilesHierarchy(this.sourcePath);
        File zipFile = new File(this.zipFilePath);
        var resultHierarchy = this.utils.getAllFilesHierarchy(this.resultPath);
        assertThat(this.utils.allExist(sourceHierarchy), is(true));
        assertThat(this.utils.allExist(resultHierarchy), is(false));
        assertThat(zipFile.exists(), is(false));
        //
        String argsArchiveStr = String.format("-a %s -s %s -o %s",
                "archive", this.sourcePath, this.zipFilePath);
        String argsUnarchiveStr = String.format("-a %s -s %s -o %s",
                "unarchive", this.zipFilePath, this.resultPath);
        Main.main(argsArchiveStr.split(" "));
        Main.main(argsUnarchiveStr.split(" "));
        //
        assertThat(zipFile.exists(), is(true));
        assertThat(this.utils.allExist(resultHierarchy), is(true));
    }

    @Test
    public void makeArchiveHtmlPdfThenUnarchive() {
        var sourceHierarchy = this.utils.getAllFilesHierarchy(this.sourcePath);
        File zipFile = new File(this.zipFilePath);
        var resultHierarchyAll = this.utils.getAllFilesHierarchy(this.resultPath);
        var resultHierarchyHtmlPdf = this.utils.getHtmlPdfFilesHierarchy(this.resultPath);
        assertThat(this.utils.allExist(sourceHierarchy), is(true));
        assertThat(this.utils.allExist(resultHierarchyAll), is(false));
        assertThat(this.utils.allExist(resultHierarchyHtmlPdf), is(false));
        assertThat(zipFile.exists(), is(false));
        //
        String argsArchiveStr = String.format("-a %s -s %s -o %s -e %s",
                "archive", this.sourcePath, this.zipFilePath, "html,pdf");
        String argsUnarchiveStr = String.format("-a %s -s %s -o %s",
                "unarchive", this.zipFilePath, this.resultPath);
        Main.main(argsArchiveStr.split(" "));
        Main.main(argsUnarchiveStr.split(" "));
        //
        assertThat(zipFile.exists(), is(true));
        assertThat(this.utils.allExist(resultHierarchyAll), is(false));
        assertThat(this.utils.allExist(resultHierarchyHtmlPdf), is(true));
    }

}