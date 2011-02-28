package com.ryaltech.util.archive;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ClassComparator extends BaseComparator {

    private Comparator coarseGrainComparator;

    public ClassComparator(Comparator coarseGrainComparator) {
        this.coarseGrainComparator = coarseGrainComparator;
    }


    public boolean compare(Comparable c1, Comparable c2, boolean reportResults) {
        try {
            Comparable clonec1 = cloneComparable(c1);
            Comparable clonec2 = cloneComparable(c2);

            if (!coarseGrainComparator.compare(clonec1, clonec2, false)) {
                clonec1.getInputStream().reset();
                clonec2.getInputStream().reset();
                String s1 = JavaPJavaArchive.javapClass(JavaPJavaArchive.createNonCloseableStream(clonec1.getInputStream()));
                String s2 = JavaPJavaArchive.javapClass(JavaPJavaArchive.createNonCloseableStream(clonec2.getInputStream()));
                if (s1.equals(s2)) {
                    reportMatch("Binary different, yet JavaP matched them up.", c1.getFullName(), c2.getFullName(), reportResults);
                    return true;
                } else {
                    reportMisMatch(c1.getFullName(), c2.getFullName(), reportResults);
                    return false;
                }
            } else {
                reportMisMatch(c1.getFullName(), c2.getFullName(), reportResults);
                return true;
            }
        } catch (Exception ioex) {
            throw new RuntimeException(ioex);
        }
    }

    private Comparable cloneComparable(Comparable c1) throws IOException {
        byte[] buffer = readFully(c1.getInputStream());
        return new Comparable(c1.getParentName(), c1.getComparableName(), new ByteArrayInputStream(buffer));
    }

    public byte[] readFully(InputStream is) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(is, output);
        return output.toByteArray();
    }

    public long copy(InputStream input, OutputStream output) throws IOException {

        byte[] buffer = new byte[1024];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;

    }

}
