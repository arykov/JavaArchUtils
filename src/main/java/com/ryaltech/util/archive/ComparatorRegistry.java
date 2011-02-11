package com.ryaltech.util.archive;

import java.util.HashMap;
import java.util.Map;

public class ComparatorRegistry {
    private enum ComparatorType {
        ZIP, CLASS, BINARY
    };

    Map<ComparatorType, Comparator> registry = new HashMap<ComparatorType, Comparator>() {        
        private static final long serialVersionUID = 1L;
        {
            put(ComparatorType.ZIP, new ZipComparator(ComparatorRegistry.this));
            put(ComparatorType.CLASS, new ClassComparator(new BinaryComparator()));
            put(ComparatorType.BINARY, new BinaryComparator());
        }
    };

    public Comparator getComparator(String name) {
        return registry.get(getType(name));
    }

    private ComparatorType getType(String name) {
        name = name.toLowerCase();
        if(name.endsWith(".ear")||name.endsWith(".zip")||name.endsWith(".jar")||name.endsWith(".war")||name.endsWith(".rar"))return ComparatorType.ZIP;
        if(name.endsWith(".class"))return ComparatorType.CLASS;
        return ComparatorType.BINARY;
    }

}
