package console;

import console.Main;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.net.URL;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ComplexMainTest {

    private final String sourcePath;
    private final String zipFilePath;
    private final String resultPath;

    public ComplexMainTest() {
        String sourceFolder = String.format("%s/%s",
                this.getClass().getPackageName().replaceAll("\\.", "/"),
                "complexMainTest"
        );
        ClassLoader loader = this.getClass().getClassLoader();
        String rootPath = loader.getResource(sourceFolder).getPath();
        this.sourcePath = String.format("%s/%s", rootPath, "source");
        this.zipFilePath = String.format("%s/%s", rootPath, "archive.zip");
        this.resultPath = String.format("%s/%s", rootPath, "result");
    }

    @Before
    public void deleteExistingResultFiles() {
        File zipFile = new File(this.zipFilePath);
        if (zipFile.exists()) {
            zipFile.delete();
        }
        //
        File[] resultHierarchy = this.getAllFilesHierarchy(this.resultPath);
        this.deleteAllIfExist(resultHierarchy);
    }

    @Test
    public void makeArchiveThenUnarchive() {
        File[] sourceHierarchy = this.getAllFilesHierarchy(this.sourcePath);
        File zipFile = new File(this.zipFilePath);
        File[] resultHierarchy = this.getAllFilesHierarchy(this.resultPath);
        assertThat(this.allExist(sourceHierarchy), is(true));
        assertThat(this.allExist(resultHierarchy), is(false));
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
        assertThat(this.allExist(resultHierarchy), is(true));
    }

    @Test
    public void makeArchiveHtmlPdfThenUnarchive() {
        File[] sourceHierarchy = this.getAllFilesHierarchy(this.sourcePath);
        File zipFile = new File(this.zipFilePath);
        File[] resultHierarchyAll = this.getAllFilesHierarchy(this.resultPath);
        File[] resultHierarchyHtmlPdf = this.getHtmlPdfFilesHierarchy(this.resultPath);
        assertThat(this.allExist(sourceHierarchy), is(true));
        assertThat(this.allExist(resultHierarchyAll), is(false));
        assertThat(this.allExist(resultHierarchyHtmlPdf), is(false));
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
        assertThat(this.allExist(resultHierarchyAll), is(false));
        assertThat(this.allExist(resultHierarchyHtmlPdf), is(true));
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
                innerDir,
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