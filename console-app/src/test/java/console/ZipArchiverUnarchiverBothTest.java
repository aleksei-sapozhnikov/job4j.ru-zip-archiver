package console;

import console.ZipArchiver;
import console.ZipUnarchiver;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ZipArchiverUnarchiverBothTest {

    private final String rootPath;
    private final String sourcePath;
    private final String zipFilePath;
    private final String resultPath;

    public ZipArchiverUnarchiverBothTest() {
        String sourceFolder = String.format("%s/%s",
                this.getClass().getPackageName().replaceAll("\\.", "/"),
                "zipArchiverUnarchiverBothTest"
        );
        ClassLoader loader = this.getClass().getClassLoader();
        this.rootPath = loader.getResource(sourceFolder).getPath();
        this.sourcePath = String.format("%s/%s", this.rootPath, "source");
        this.zipFilePath = String.format("%s/%s", this.rootPath, "archive.zip");
        this.resultPath = String.format("%s/%s", this.rootPath, "result");
    }

    @Before
    public void deleteExistingResultFiles() {
        File[] hierarchy = this.getAllFilesHierarchy(this.resultPath);
        this.deleteAllIfExist(hierarchy);
    }

    @Test
    public void whenArchiveUnarchiveThenProjectTheSame() throws IOException {
        ZipArchiver archiver = new ZipArchiver();
        ZipUnarchiver unarchiver = new ZipUnarchiver();
        //
        File[] hierarchyBefore = this.getAllFilesHierarchy(this.sourcePath);
        File[] hierarchyAfter = this.getAllFilesHierarchy(this.resultPath);
        //
        assertThat(this.allExist(hierarchyBefore), is(true));
        assertThat(this.allExist(hierarchyAfter), is(false));
        //
        archiver.archive(this.sourcePath, this.zipFilePath);
        unarchiver.unarchive(this.zipFilePath, this.resultPath);
        //
        assertThat(this.allExist(hierarchyAfter), is(true));
    }

    @Test
    public void whenArchiveSpecificExtensionsAndUnarchiveThenOnlySpecifiedExtensionsLeft() throws IOException {
        ZipArchiver archiver = new ZipArchiver(Set.of("html", "pdf"));
        ZipUnarchiver unarchiver = new ZipUnarchiver();
        //
        File[] hierarchyBefore = this.getAllFilesHierarchy(this.sourcePath);
        File[] hierarchyAfterAllFiles = this.getAllFilesHierarchy(this.resultPath);
        File[] hierarchyAfterSpecific = this.getHtmlPdfFilesHierarchy(this.resultPath);
        //
        assertThat(this.allExist(hierarchyBefore), is(true));
        assertThat(this.allExist(hierarchyAfterAllFiles), is(false));
        assertThat(this.allExist(hierarchyAfterSpecific), is(false));
        //
        archiver.archive(this.sourcePath, this.zipFilePath);
        unarchiver.unarchive(this.zipFilePath, this.resultPath);
        //
        assertThat(this.allExist(hierarchyAfterAllFiles), is(false));
        assertThat(this.allExist(hierarchyAfterSpecific), is(true));
    }

    private File[] getAllFilesHierarchy(String root) {
        File innerDir = new File(root, "inner_dir");
        File emptyDir = new File(root, "empty_dir");
        return new File[]{
                new File(root, "test_1.html"),
                new File(root, "test2.xml"),
                new File(innerDir, "test_inner_1.pdf"),
                new File(innerDir, "test_inner_2.xml"),
                innerDir,
                emptyDir
        };
    }

    private File[] getHtmlPdfFilesHierarchy(String root) {
        File innerDir = new File(root, "inner_dir");
        return new File[]{
                new File(root, "test_1.html"),
                new File(innerDir, "test_inner_1.pdf"),
                innerDir
        };
    }

    private boolean allExist(File[] files) {
        boolean result = true;
        for (File file : files) {
            if (!file.exists()) {
                result = false;
                break;
            }
        }
        return result;
    }

    private void deleteAllIfExist(File[] files) {
        for (File file : files) {
            file.delete();
        }
    }
}
