package com.ryaltech.util.archive;

public abstract class BaseComparator implements Comparator {

    public boolean compare(Comparable c1, Comparable c2) {
        return compare(c1, c2, true);
    }

    protected void reportMatch(String comment, String f1, String f2, boolean reportResults) {
        if (reportResults) {
            System.out.println(String.format("Matched successfully: %s and %s %s", f1, f2, comment));
        }

    }

    protected void reportMatch(String f1, String f2, boolean reportResults) {
        reportMatch("", f1, f2, reportResults);

    }

    protected void reportMisMatch(String f1, String f2, boolean reportResults) {
        if (reportResults) {
            System.out.println(String.format("Match failed: %s and %s", f1, f2));
        }

    }

}
