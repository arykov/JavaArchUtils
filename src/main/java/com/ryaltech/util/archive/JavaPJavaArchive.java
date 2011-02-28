package com.ryaltech.util.archive;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import sun.tools.javap.JavapEnvironment;
import sun.tools.javap.JavapPrinter;

public class JavaPJavaArchive {

    /**
     * Sets invisible field with fieldName name on the obj with value
     * 
     * @param obj
     * @param fieldName
     * @param value
     * @throws Exception
     */
    public static void setField(Object obj, String fieldName, Object value) throws Exception {
        Class<?> clz = obj.getClass();
        Field field = clz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    public static void close(Closeable c) {
        try {
            c.close();
        } catch (Exception ex) {

        }
    }

    /**
     * Wraps InputStream to prevent close from taking effect. This is required
     * due to JavaP closing InputStream passed in. This breaks ZipInputStream
     * 
     * @param is
     * @return
     */
    public static InputStream createNonCloseableStream(final InputStream is) {
        return new InputStream() {

            @Override
            public int read() throws IOException {
                return is.read();
            }

            public void close() {

            }
        };

    }

    /**
     * Runs javap against the class passed as inputstream Note that javap closes
     * this inputstream
     * 
     * @param is
     * @throws Exception
     */
    public static String javapClass(final InputStream is) throws Exception {
        JavapEnvironment env = new JavapEnvironment();
        setField(env, "showDisassembled", true);
        OutputStream output = new OutputStream()
        {
            private StringBuilder string = new StringBuilder();

            @Override
            public void write(int b) throws IOException {
                this.string.append((char) b);
            }

            public String toString() {
                return this.string.toString();
            }
        };

        PrintWriter pw = new PrintWriter(output);

        JavapPrinter printer = new JavapPrinter(is, pw, env);
        printer.print();
        pw.close();
        return output.toString();
        

    }

    public static void printJavaPClass(InputStream is)throws Exception{
        System.out.println(javapClass(is));
    }
    
    public static void main(String... args) throws Exception {

        if (args.length != 1) {
            System.err.println("Usage: java com.ryaltech.util.JavaPJava <filename - class, ear, war, jar, rar>");
            System.exit(-1);
        }
        String fileName = args[0];
        File file = new File(fileName);
        FileInputStream fis = null;
        if (file.isFile() && file.exists()) {
            try {
                processFile(file.getCanonicalPath(), fis = new FileInputStream(file));
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(-1);

            } finally {
                close(fis);
            }

        } else {
            System.err.println(fileName + " is not a file or does not exist.");
            System.exit(-1);
        }
    }

    /**
     * Processes input stream that points to a class or a java archive of some
     * sort. All other ones are ignored. name identifies the name entry.
     * 
     * @param name
     * @param fileInputStream
     * @throws Exception
     */
    public static void processFile(String name, InputStream fileInputStream) throws Exception {
        if (name.endsWith(".class")) {
            System.out.println("Processing class: " + name);
            printJavaPClass(fileInputStream);

        } else if (name.endsWith(".jar") || name.endsWith(".ear") || name.endsWith(".war") || name.endsWith(".rar")) {
            System.out.println("Processing archive: " + name);
            ZipInputStream zis = null;
            try {
                zis = new ZipInputStream(fileInputStream);
                ZipEntry ze;
                while ((ze = zis.getNextEntry()) != null) {
                    processFile(ze.getName(), createNonCloseableStream(zis));
                    zis.closeEntry();

                }
            } finally {
                close(zis);
            }

        }
    }
}
