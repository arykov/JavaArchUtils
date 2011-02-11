package com.ryaltech.util.archive;

public abstract class BaseComparator implements Comparator {

    protected void reportMatch(String comment, String f1, String f2) {
        System.out.println(String.format("Matched successfully: %s and %s %s",  f1, f2, comment));
        
    }
    protected void reportMatch(String f1, String f2) {
        reportMatch("", f1, f2);

    }

    protected void reportMisMatch(String f1, String f2) {
        System.out.println(String.format("Match failed: %s and %s", f1, f2));

    }

}
