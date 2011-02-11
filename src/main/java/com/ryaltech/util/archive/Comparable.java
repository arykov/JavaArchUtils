package com.ryaltech.util.archive;

import java.io.InputStream;

public class Comparable {
    private String parentName;
    private String comparableName;
    private InputStream inputStream;
    

    public Comparable(String parentName, String comparableName, InputStream inputStream) {
        super();

        this.parentName = parentName;
        this.comparableName = comparableName;
        this.inputStream = inputStream;
    }

    public Comparable(String comparableName, InputStream inputStream) {
        super();

        this.comparableName = comparableName;
        this.inputStream = inputStream;
    }

    public String getParentName() {
        return parentName;
    }

    public String getComparableName() {
        return comparableName;
    }

    public String getFullName() {
        return ((parentName == null) ? comparableName : parentName + ":" + comparableName);
    }

    public InputStream getInputStream() {
        return inputStream;
    }


}
