package com.ryaltech.util.archive;

import java.io.InputStream;
import java.util.Arrays;

public class BinaryComparator extends BaseComparator{


    public boolean compare(Comparable c1, Comparable c2, boolean reportResults) {
        if (isSame(c1.getInputStream(), c2.getInputStream())) {
            reportMatch(c1.getFullName(), c2.getFullName(), reportResults);            
            return true;
        } else {
            reportMisMatch(c1.getFullName(), c2.getFullName(), reportResults);            
            return false;
        }
    }

    private boolean isSame(InputStream input1, InputStream input2) {
        try {
            byte[] buffer1 = new byte[1024];
            byte[] buffer2 = new byte[1024];

            int numRead1 = 0;
            int numRead2 = 0;
            while (true) {
                numRead1 = input1.read(buffer1);
                numRead2 = input2.read(buffer2);
                if (numRead1 > -1) {
                    if (numRead2 != numRead1)
                        return false;
                    // Otherwise same number of bytes read
                    if (!Arrays.equals(buffer1, buffer2))
                        return false;
                    // Otherwise same bytes read, so continue ...
                } else {
                    // Nothing more in stream 1 ...
                    return numRead2 < 0;
                }
            }
        } catch (RuntimeException ex) {
            throw ex;

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
