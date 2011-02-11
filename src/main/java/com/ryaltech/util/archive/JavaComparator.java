package com.ryaltech.util.archive;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class JavaComparator {
    private static ComparatorRegistry registry = new ComparatorRegistry();

    public static String getCurrentClassName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        return stackTrace[stackTrace.length - 1].getClassName();
    }

    public static void main(String args[]) {
        if (args.length != 2)
            help();
        String fileName1 = args[0];
        String fileName2 = args[1];
        try {
            if(!new JavaComparator().compareFiles(fileName1, fileName2))System.exit(-1);
        } catch (Exception ex) {
            help();
            ex.printStackTrace();
        }
    }

    public boolean compareFiles(String fileName1, String fileName2) throws FileNotFoundException {
        File ear1 = new File(fileName1);
        File ear2 = new File(fileName2);
        if (ear1.exists() && ear2.exists() && !ear1.isDirectory() && !ear2.isDirectory()) {
            if (registry.getComparator(ear1.getName()) == registry.getComparator(ear2.getName())) {
                FileInputStream fis1 = null;
                FileInputStream fis2 = null;
                try {
                    fis1 = new FileInputStream(ear1);
                    fis2 = new FileInputStream(ear2);
                    return registry.getComparator(ear2.getName()).compare(new Comparable(fileName1, fis1), new Comparable(fileName2, fis2));
                } finally {
                    IOUtils.close(fis1);
                    IOUtils.close(fis2);
                }
            } else {
                throw new RuntimeException(String.format("Files %s, %s appear to be of different types.", fileName1, fileName2));

            }
        } else {
            throw new RuntimeException(String.format("Files %s, %s appear not to exist or are directories.", fileName1, fileName2));

        }
    }

    private static void help() {
        System.out.println(String.format("Usage: java -classpath <jdk_home>/lib/tools.jar%s<comparejava_location>/comparejava.jar %s <full path to ear1> <full path to ear2>", File.pathSeparator, getCurrentClassName()));
        System.exit(-1);
    }

}
