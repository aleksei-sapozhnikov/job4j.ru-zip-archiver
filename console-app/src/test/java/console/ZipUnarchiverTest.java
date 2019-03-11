package console;

import console.ZipUnarchiver;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ZipUnarchiverTest {

    private final ZipUnarchiver test = new ZipUnarchiver();

    private final String rootPath;
    private final String zipFilePath;
    private final String resultPath;

    public ZipUnarchiverTest() {
        String sourceFolder = String.format("%s/%s",
                this.getClass().getPackageName().replaceAll("\\.", "/"),
                "zipUnarchiverTest"
        );
        ClassLoader loader = this.getClass().getClassLoader();
        this.rootPath = loader.getResource(sourceFolder).getPath();
        this.zipFilePath = String.format("%s/%s", this.rootPath, "source.zipfile");
        this.resultPath = String.format("%s/%s", this.rootPath, "result");
    }

    @Before
    public void deleteAllResults() {
        File[] resultFiles = this.getFilesHierarchy(this.resultPath);
        this.deleteAllIfExist(resultFiles);
        new File(this.resultPath).delete();
    }

    @Test
    public void whenUnarchiveThenResultFolderCreated() throws IOException {
        File result = new File(this.resultPath);
        assertThat(result.exists(), is(false));
        this.test.unarchive(this.zipFilePath, this.resultPath);
        assertThat(result.exists(), is(true));
        assertThat(result.isDirectory(), is(true));
    }

    @Test
    public void whenUnarchiveThenFilesOnPlace() throws IOException {
        File[] resultHierarchy = this.getFilesHierarchy(this.resultPath);
        assertThat(this.allExist(resultHierarchy), is(false));
        this.test.unarchive(this.zipFilePath, this.resultPath);
        assertThat(this.allExist(resultHierarchy), is(true));
    }

    private void deleteAllIfExist(File[] files) {
        for (File file : files) {
            file.delete();
        }
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

    private File[] getFilesHierarchy(String root) {
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


}