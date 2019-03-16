package io.github.vermucht.ziparchiver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Util class for common-used methods.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class Utils {

    /**
     * Writes all data from InputStream to OutputStream.
     *
     * @param in  InnputStream object.
     * @param out OutputStream object.
     * @throws IOException In case of I/O problems.
     */
    public static void writeContents(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[4048];
        int length;
        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }
    }
}
