There are two utilities here:
- com.ryaltech.util.archive.JavaComparator to compare ears/wars/jars directories with subdirectories and archives
Usage: java -classpath <jdk_home>/lib/tools.jar:<comparejava_location>/comparejava.jar com.ryaltech.util.archive.JavaComparator <full path to ear1> <full path to ear2>

- com.ryaltech.util.JavaPJava to disassemble classes recursively
Usage: java com.ryaltech.util.JavaPJava <filename - class, ear, war, jar, rar>