package com.ryaltech.util.archive;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipComparator extends BaseComparator {

    private ComparatorRegistry registry;

    public ZipComparator(ComparatorRegistry comparatorRegistry) {
        this.registry = comparatorRegistry;

    }

    /**
     * Wraps InputStream to prevent close from taking effect. This is required
     * due to JavaP closing InputStream passed in. This breaks ZipInputStream
     * 
     * @param is
     * @return
     */
    public InputStream createNonCloseableStream(final InputStream is) {
        return new InputStream() {

            @Override
            public int read() throws IOException {
                return is.read();
            }

            public void close() {

            }
        };
    }

    @Override
    public boolean compare(Comparable c1, Comparable c2) {
        File file1 = saveTempFile(c1.getInputStream());
        File file2 = saveTempFile(c2.getInputStream());

        ZipFile zif1 = null;
        ZipFile zif2 = null;
        try {
            zif1 = new ZipFile(file1);
            zif2 = new ZipFile(file2);
            Map<String, ZipEntry> zipEntryMap1 = getZipEntries(zif1);
            Map<String, ZipEntry> zipEntryMap2 = getZipEntries(zif2);
            List<ZipEntry[]> zipEntryDeepInspectionList = new ArrayList<ZipEntry[]>();
            boolean retVal = true;

            for (String fileName : zipEntryMap1.keySet()) {
                ZipEntry ze1 = zipEntryMap1.get(fileName);
                ZipEntry ze2 = zipEntryMap2.get(fileName);
                if (ze2 == null) {
                    retVal = false;
                    System.out.println(String.format("Archive %s contains file %s not present in archive %s", c1.getFullName(), fileName, c2.getFullName()));
                } else {

                    if (ze1.getCrc() != ze2.getCrc()) {
                        zipEntryDeepInspectionList.add(new ZipEntry[] { ze1, ze2 });
                    } else {
                        reportMatch("(based on CRC)", c1.getFullName() + ":" + ze1.getName(), c2.getFullName() + ":" + ze2.getName());
                    }

                }
            }

            for (String fileName : zipEntryMap2.keySet()) {
                if (zipEntryMap1.get(fileName) == null) {
                    retVal = false;
                    System.out.println(String.format("Archive %s contains file %s not present in archive %s", c2.getFullName(), fileName, c1.getFullName()));
                }
            }
            for (ZipEntry[] ze : zipEntryDeepInspectionList) {
                String name = ze[0].getName();
                Comparator comparator = registry.getComparator(name);
                InputStream ze1is = null;
                InputStream ze2is = null;
                try {
                    ze1is = zif1.getInputStream(ze[0]);
                    ze2is = zif2.getInputStream(ze[1]);
                    retVal = comparator.compare(new Comparable(c1.getFullName(), name, ze1is), new Comparable(c2.getFullName(), name, ze2is)) && retVal;
                } finally {
                    IOUtils.close(ze2is);
                    IOUtils.close(ze1is);
                }
            }
            return retVal;
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {

            file1.delete();
            file2.delete();
            IOUtils.close(zif1);
        }

    }

    private Map<String, ZipEntry> getZipEntries(ZipFile zif) {
        Enumeration<? extends ZipEntry> enumeration = zif.entries();
        Map<String, ZipEntry> map = new HashMap<String, ZipEntry>();

        while (enumeration.hasMoreElements()) {
            ZipEntry ze = enumeration.nextElement();
            if (!ze.isDirectory())
                map.put(ze.getName(), ze);
        }
        return map;
    }

    private File saveTempFile(InputStream is) {
        File file = null;
        FileOutputStream fos = null;
        try {
            file = File.createTempFile("comjava", ".zip");

            fos = new FileOutputStream(file, false);
            byte[] buffer = new byte[100000];
            int byteRead = 0;
            while ((byteRead = is.read(buffer)) != -1) {

                fos.write(buffer, 0, byteRead);

            }
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            IOUtils.close(fos);
        }
        return file;

    }

}
