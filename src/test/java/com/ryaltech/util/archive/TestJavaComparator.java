package com.ryaltech.util.archive;

import java.io.OutputStream;
import java.io.PrintStream;

import junit.framework.TestCase;

public class TestJavaComparator extends TestCase {
    PrintStreamInterceptor ps = new PrintStreamInterceptor(System.out);
    PrintStream errOriginal = System.err;
    PrintStream outOriginal = System.out;
    private String workingDir;

    public TestJavaComparator() {
        String url = getClass().getClassLoader().getResource(".").toString();
        if (url.startsWith("file:/")) {
            workingDir = url.substring(6);

        } else {
            throw new RuntimeException("Cannot test when loaded from " + url);
        }
    }

    public void setUp() {
        System.setErr(ps);
        System.setOut(ps);
        outOriginal.println("============" + getName() + "============");
    }

    public String changeTestResourcePath(String templatePath) {
        return templatePath.replace("${testresourcepath}", workingDir);
    }

    public void testIdentical() throws Exception {
        assertTrue(new JavaComparator().compareFiles(changeTestResourcePath("${testresourcepath}/v1.untampered/me.ear"), changeTestResourcePath("${testresourcepath}/v1.untampered/me.ear")));
        assertEquals(changeTestResourcePath("Matched successfully: ${testresourcepath}/v1.untampered/me.ear:lib/MyJar.jar and ${testresourcepath}/v1.untampered/me.ear:lib/MyJar.jar (based on CRC)\n" +
                "Matched successfully: ${testresourcepath}/v1.untampered/me.ear:META-INF/weblogic-application.xml and ${testresourcepath}/v1.untampered/me.ear:META-INF/weblogic-application.xml (based on CRC)\n" +
                "Matched successfully: ${testresourcepath}/v1.untampered/me.ear:MyWar.war and ${testresourcepath}/v1.untampered/me.ear:MyWar.war (based on CRC)\n" +
                "Matched successfully: ${testresourcepath}/v1.untampered/me.ear:META-INF/MANIFEST.MF and ${testresourcepath}/v1.untampered/me.ear:META-INF/MANIFEST.MF (based on CRC)\n")
                , ps.getContents());
    }

    public void testWarPresenseFail() throws Exception {
        assertFalse(new JavaComparator().compareFiles(changeTestResourcePath("${testresourcepath}/v1.untampered/me.ear"), changeTestResourcePath("${testresourcepath}/v1.compiled1/me.ear")));
        assertEquals(changeTestResourcePath("Matched successfully: ${testresourcepath}/v1.untampered/me.ear:lib/MyJar.jar and ${testresourcepath}/v1.compiled1/me.ear:lib/MyJar.jar (based on CRC)\n" +
                "Matched successfully: ${testresourcepath}/v1.untampered/me.ear:META-INF/weblogic-application.xml and ${testresourcepath}/v1.compiled1/me.ear:META-INF/weblogic-application.xml (based on CRC)\n" +
                "Matched successfully: ${testresourcepath}/v1.untampered/me.ear:META-INF/MANIFEST.MF and ${testresourcepath}/v1.compiled1/me.ear:META-INF/MANIFEST.MF (based on CRC)\n" +
                "Matched successfully: ${testresourcepath}/v1.untampered/me.ear:MyWar.war:index.jsp and ${testresourcepath}/v1.compiled1/me.ear:MyWar.war:index.jsp (based on CRC)\n" +
                "Matched successfully: ${testresourcepath}/v1.untampered/me.ear:MyWar.war:index1.jsp and ${testresourcepath}/v1.compiled1/me.ear:MyWar.war:index1.jsp (based on CRC)\n" +
                "Matched successfully: ${testresourcepath}/v1.untampered/me.ear:MyWar.war:WEB-INF/web.xml and ${testresourcepath}/v1.compiled1/me.ear:MyWar.war:WEB-INF/web.xml (based on CRC)\n" +
                "Matched successfully: ${testresourcepath}/v1.untampered/me.ear:MyWar.war:WEB-INF/weblogic.xml and ${testresourcepath}/v1.compiled1/me.ear:MyWar.war:WEB-INF/weblogic.xml (based on CRC)\n" +
                "Matched successfully: ${testresourcepath}/v1.untampered/me.ear:MyWar.war:META-INF/MANIFEST.MF and ${testresourcepath}/v1.compiled1/me.ear:MyWar.war:META-INF/MANIFEST.MF (based on CRC)\n" +
                "Archive ${testresourcepath}/v1.compiled1/me.ear:MyWar.war contains file WEB-INF/classes/jsp_servlet/__index1.class not present in archive ${testresourcepath}/v1.untampered/me.ear:MyWar.war\n" +
                "Archive ${testresourcepath}/v1.compiled1/me.ear:MyWar.war contains file WEB-INF/classes/jsp_servlet/__index.class not present in archive ${testresourcepath}/v1.untampered/me.ear:MyWar.war\n"), ps.getContents());
    }

    public void testWarPresenseFail2() throws Exception {
        assertFalse(new JavaComparator().compareFiles(changeTestResourcePath("${testresourcepath}/v1.compiled1/me.ear"), changeTestResourcePath("${testresourcepath}/v1.untampered/me.ear")));
        assertEquals(changeTestResourcePath("Matched successfully: ${testresourcepath}/v1.compiled1/me.ear:lib/MyJar.jar and ${testresourcepath}/v1.untampered/me.ear:lib/MyJar.jar (based on CRC)\n" +
                "Matched successfully: ${testresourcepath}/v1.compiled1/me.ear:META-INF/weblogic-application.xml and ${testresourcepath}/v1.untampered/me.ear:META-INF/weblogic-application.xml (based on CRC)\n" +
                "Matched successfully: ${testresourcepath}/v1.compiled1/me.ear:META-INF/MANIFEST.MF and ${testresourcepath}/v1.untampered/me.ear:META-INF/MANIFEST.MF (based on CRC)\n" +
                "Matched successfully: ${testresourcepath}/v1.compiled1/me.ear:MyWar.war:index.jsp and ${testresourcepath}/v1.untampered/me.ear:MyWar.war:index.jsp (based on CRC)\n" +
                "Archive ${testresourcepath}/v1.compiled1/me.ear:MyWar.war contains file WEB-INF/classes/jsp_servlet/__index1.class not present in archive ${testresourcepath}/v1.untampered/me.ear:MyWar.war\n" +
                "Matched successfully: ${testresourcepath}/v1.compiled1/me.ear:MyWar.war:WEB-INF/web.xml and ${testresourcepath}/v1.untampered/me.ear:MyWar.war:WEB-INF/web.xml (based on CRC)\n" +
                "Matched successfully: ${testresourcepath}/v1.compiled1/me.ear:MyWar.war:index1.jsp and ${testresourcepath}/v1.untampered/me.ear:MyWar.war:index1.jsp (based on CRC)\n" +
                "Matched successfully: ${testresourcepath}/v1.compiled1/me.ear:MyWar.war:WEB-INF/weblogic.xml and ${testresourcepath}/v1.untampered/me.ear:MyWar.war:WEB-INF/weblogic.xml (based on CRC)\n" +
                "Archive ${testresourcepath}/v1.compiled1/me.ear:MyWar.war contains file WEB-INF/classes/jsp_servlet/__index.class not present in archive ${testresourcepath}/v1.untampered/me.ear:MyWar.war\n" +
                "Matched successfully: ${testresourcepath}/v1.compiled1/me.ear:MyWar.war:META-INF/MANIFEST.MF and ${testresourcepath}/v1.untampered/me.ear:MyWar.war:META-INF/MANIFEST.MF (based on CRC)\n"), ps.getContents());
    }

    public void testJarClassDifference() throws Exception {
        assertFalse(new JavaComparator().compareFiles(changeTestResourcePath("${testresourcepath}/v1.untampered/me.ear"), changeTestResourcePath("${testresourcepath}/v2.untampered/me.ear")));
        assertEquals(changeTestResourcePath("Matched successfully: ${testresourcepath}/v1.untampered/me.ear:META-INF/weblogic-application.xml and ${testresourcepath}/v2.untampered/me.ear:META-INF/weblogic-application.xml (based on CRC)\n" +
                "Matched successfully: ${testresourcepath}/v1.untampered/me.ear:MyWar.war and ${testresourcepath}/v2.untampered/me.ear:MyWar.war (based on CRC)\n" +
                "Matched successfully: ${testresourcepath}/v1.untampered/me.ear:META-INF/MANIFEST.MF and ${testresourcepath}/v2.untampered/me.ear:META-INF/MANIFEST.MF (based on CRC)\n" +
                "Matched successfully: ${testresourcepath}/v1.untampered/me.ear:lib/MyJar.jar:Class2.class and ${testresourcepath}/v2.untampered/me.ear:lib/MyJar.jar:Class2.class (based on CRC)\n" +
                "Matched successfully: ${testresourcepath}/v1.untampered/me.ear:lib/MyJar.jar:Class1.java and ${testresourcepath}/v2.untampered/me.ear:lib/MyJar.jar:Class1.java (based on CRC)\n" +
                "Matched successfully: ${testresourcepath}/v1.untampered/me.ear:lib/MyJar.jar:Class2.java and ${testresourcepath}/v2.untampered/me.ear:lib/MyJar.jar:Class2.java (based on CRC)\n" +
                "Matched successfully: ${testresourcepath}/v1.untampered/me.ear:lib/MyJar.jar:Class1.class and ${testresourcepath}/v2.untampered/me.ear:lib/MyJar.jar:Class1.class (based on CRC)\n" +
                "Matched successfully: ${testresourcepath}/v1.untampered/me.ear:lib/MyJar.jar:META-INF/MANIFEST.MF and ${testresourcepath}/v2.untampered/me.ear:lib/MyJar.jar:META-INF/MANIFEST.MF (based on CRC)\n" +
                "Match failed: ${testresourcepath}/v1.untampered/me.ear:lib/MyJar.jar:Class3.class and ${testresourcepath}/v2.untampered/me.ear:lib/MyJar.jar:Class3.class\n" +
                "Match failed: ${testresourcepath}/v1.untampered/me.ear:lib/MyJar.jar:Class3.java and ${testresourcepath}/v2.untampered/me.ear:lib/MyJar.jar:Class3.java\n"), ps.getContents());
    }


    public void tearDown() {
        ps.reset();
    }

}

class PrintStreamInterceptor extends PrintStream {
    private PrintStream delegate;
    public StringBuffer sb = new StringBuffer();

    public void reset() {
        sb = new StringBuffer();
    }

    public String getContents() {
        return sb.toString();
    }

    public PrintStreamInterceptor(PrintStream delegate) {
        super(new OutputStream() {
            @Override
            public void write(int b) {
            }
        });
        this.delegate = delegate;
    }

    @Override
    public void print(String s) {
        sb.append(s);
        delegate.print(s);
    }

    @Override
    public void println(String s) {
        sb.append(s + "\n");
        delegate.println(s);
    }

}