package io.github.vermucht.ziparchiver;

import io.github.vermucht.ziparchiver.testutils.FileStructureUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ZipUnarchiverTest {

    public static final String SOURCE_ZIPFILE = "io/github/vermucht/ziparchiver/source.zipfile";

    private final FileStructureUtils utils = new FileStructureUtils();
    private final ZipUnarchiver unarchiver = new ZipUnarchiver();

    private final String zipFilePath;
    private final String resultPath;

    public ZipUnarchiverTest() throws IOException {
        File root = this.utils.createTempDirectory("ComplexMainTest");
        this.resultPath = String.format("%s/%s", root, "result");
        //
        this.zipFilePath = this.getClass().getClassLoader()
                .getResource(SOURCE_ZIPFILE).getPath();
    }

    @Before
    public void deleteAllResults() {
        var resultFiles = this.utils.getAllFilesHierarchy(this.resultPath);
        this.utils.deleteAllIfExist(resultFiles);
        new File(this.resultPath).delete();
    }

    @Test
    public void whenUnarchiveThenResultFolderCreated() throws IOException {
        File result = new File(this.resultPath);
        assertThat(result.exists(), is(false));
        this.unarchiver.unarchive(this.zipFilePath, this.resultPath);
        assertThat(result.exists(), is(true));
        assertThat(result.isDirectory(), is(true));
    }

    @Test
    public void whenUnarchiveThenFilesOnPlace() throws IOException {
        var resultHierarchy = this.utils.getAllFilesHierarchy(this.resultPath);
        assertThat(this.utils.allExist(resultHierarchy), is(false));
        this.unarchiver.unarchive(this.zipFilePath, this.resultPath);
        assertThat(this.utils.allExist(resultHierarchy), is(true));
    }
}