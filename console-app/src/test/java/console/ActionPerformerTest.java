package console;

import org.junit.Test;
import org.mockito.Mock;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class ActionPerformerTest {

    @Mock
    private ZipArchiver zipArchiver;

    @Mock
    private ZipUnarchiver zipUnarchiver;

    @Mock
    private ArgsParser argsParser;

    @Mock
    private InstancesFactory factory;

    public ActionPerformerTest() {
        this.initMocks();
    }

    private void initMocks() {
        this.argsParser = mock(ArgsParser.class);
        this.zipArchiver = mock(ZipArchiver.class);
        this.zipUnarchiver = mock(ZipUnarchiver.class);
        this.factory = mock(InstancesFactory.class);
        //
        when(this.factory.getZipArchiver(anySet())).thenReturn(this.zipArchiver);
        when(this.factory.getZipUnarchiver()).thenReturn(this.zipUnarchiver);
        //
        when(this.factory.getArgsParser(any(String[].class))).thenReturn(this.argsParser);
        when(this.argsParser.getAction()).thenReturn("archive");
        when(this.argsParser.getInputPath()).thenReturn("input");
        when(this.argsParser.getOutputPath()).thenReturn("output");
    }

    @Test
    public void whenPerformArchiveThenZipArchiverCalled() throws Exception {
        when(this.argsParser.getAction()).thenReturn("archive");
        new ActionPerformer(this.factory).perform(new String[]{"args"});
        verify(this.zipArchiver).archive("input", "output");
    }

    @Test
    public void whenPerformUnarchiveThenZipUnarchiverCalled() throws Exception {
        when(this.argsParser.getAction()).thenReturn("unarchive");
        new ActionPerformer(this.factory).perform(new String[]{"args"});
        verify(this.zipUnarchiver).unarchive("input", "output");
    }

    @Test
    public void whenUnknownActionThenRuntimeException() throws Exception {
        new ActionPerformer(this.factory).perform(new String[]{"args"}); // check no exception
        when(this.argsParser.getAction()).thenReturn("other");
        boolean wasException = false;
        try {
            new ActionPerformer(this.factory).perform(new String[]{"args"});
        } catch (RuntimeException e) {
            wasException = true;
        }
        assertThat(wasException, is(true));
    }

    @Test
    public void whenOneOfNeededArgumentsEmptyThenRuntimeException() throws Exception {
        new ActionPerformer(this.factory).perform(new String[]{"args"}); // check no exception
        when(this.argsParser.getOutputPath()).thenReturn("");
        boolean wasException = false;
        try {
            new ActionPerformer(this.factory).perform(new String[]{"args"});
        } catch (RuntimeException e) {
            wasException = true;
        }
        assertThat(wasException, is(true));
    }

    @Test
    public void whenEmptyArgumentsThenHelpMessage() throws Exception {
        PrintStream out = System.out;
        ByteArrayOutputStream myOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(myOut));
        String[] args = new String[0];
        InstancesFactory factory = new InstancesFactory();
        new ActionPerformer(factory).perform(args);
        System.setOut(out);
        String result = new String(myOut.toByteArray());
        assertThat(result.startsWith("=== Simple zip archiver/unarchiver ==="), is(true));
    }

    @Test
    public void whenArgumentOfHelpThenHelpMessage() throws Exception {
        PrintStream out = System.out;
        ByteArrayOutputStream myOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(myOut));
        String args = "-h";
        InstancesFactory factory = new InstancesFactory();
        new ActionPerformer(factory).perform(args.split(" "));
        System.setOut(out);
        String result = new String(myOut.toByteArray());
        assertThat(result.startsWith("=== Simple zip archiver/unarchiver ==="), is(true));
    }

}