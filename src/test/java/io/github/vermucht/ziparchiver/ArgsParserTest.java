package io.github.vermucht.ziparchiver;

import org.junit.Test;

import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ArgsParserTest {

    @Test
    public void whenAllArgsGoodThenReturnsValues() {
        String argString = "-a archive -s c:\\input -e pdf,xml,html -o c:\\output";
        String[] args = argString.split(" ");
        ArgsParser parser = new ArgsParser.Builder(args).build();
        assertThat(parser.getInputPath(), is("c:\\input"));
        assertThat(parser.getOutputPath(), is("c:\\output"));
        assertThat(parser.getAction(), is("archive"));
        assertThat(parser.getExtensions(), is(Set.of("pdf", "xml", "html")));
    }

    @Test
    public void whenArgumentValueSkippedThenRuntimeException() {
        // -a value skipped
        String argString = "-a -s c:\\input -e pdf,xml,html -o c:\\output";
        String[] args = argString.split(" ");
        boolean wasException = false;
        try {
            new ArgsParser.Builder(args).build();
        } catch (RuntimeException e) {
            wasException = true;
        }
        assertThat(wasException, is(true));
    }

    @Test
    public void whenUnknownArgumentThenRuntimeException() {
        // unknown parameter '-action'
        String argString = "-action archive -s c:\\input -e pdf,xml,html -o c:\\output";
        String[] args = argString.split(" ");
        boolean wasException = false;
        try {
            new ArgsParser.Builder(args).build();
        } catch (RuntimeException e) {
            wasException = true;
        }
        assertThat(wasException, is(true));
    }
}