package com.ryaltech.util.archive;

public class ClassComparator implements Comparator {

    private Comparator coarseGrainComparator;
    public ClassComparator(Comparator coarseGrainComparator){
        this.coarseGrainComparator = coarseGrainComparator;
    }
    @Override
    public boolean compare(Comparable c1, Comparable c2) {
        return coarseGrainComparator.compare(c1, c2);
    }

}
