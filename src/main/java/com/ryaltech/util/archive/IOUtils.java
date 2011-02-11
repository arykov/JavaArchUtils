package com.ryaltech.util.archive;

import java.io.Closeable;
import java.util.zip.ZipFile;

public class IOUtils {
    public static void close(Closeable c) {
        try {
            c.close();
        } catch (Exception ex) {

        }
    }
    
    public static void close(ZipFile c) {
        try {
            c.close();
        } catch (Exception ex) {

        }
    }


}
