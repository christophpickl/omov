package net.sourceforge.omov.app.util;

import java.io.File;
import java.io.IOException;

import net.sourceforge.omov.app.AbstractTestCase;
import net.sourceforge.omov.core.util.FileUtil;

public class FileUtilTest extends AbstractTestCase {

    public void testClearingDotsForFiles() throws Exception {
        doTestFileEquals("tut.mpg", "tut.mpg");
        doTestFileEquals("tut.tat.bumbap...mpg", "tut tat bumbap.mpg");
        doTestFileEquals("...tut.tat.bumbap.mpg", "tut tat bumbap.mpg");
        doTestFileEquals("tut...tat.bumbap.mpg", "tut   tat bumbap.mpg");
        doTestFileEquals("Das ist Goodfella in Alt.avi", "Das ist Goodfella in Alt.avi");
        doTestFileEquals("Das.ist.Dr.Goodfella.in.Neu.avi", "Das ist Dr. Goodfella in Neu.avi");
        doTestFileEquals("mr.Levinski.mov", "mr. Levinski.mov");
        doTestFileEquals("Mr.Levinski.mov", "Mr. Levinski.mov");
        doTestFileEquals("MR.Levinski.mov", "MR. Levinski.mov");
        doTestFileEquals("Mr. Levinski.mov", "Mr.  Levinski.mov");
        doTestFileEquals("Mr..Levinski.mov", "Mr.  Levinski.mov");
        doTestFileEquals("the.dr.mr.rowy.mov", "the dr. mr. rowy.mov");
        doTestFileEquals("the.dr..mr..rowy.mov", "the dr.  mr.  rowy.mov");
    }
    
    public void testClearingDotsForFolders() throws Exception {
        doTestFolderEquals("tutFolder", "tutFolder");
        doTestFolderEquals("tutFolder.avi", "tutFolder avi");
        doTestFolderEquals("tutFolder...avi", "tutFolder   avi");
        doTestFolderEquals("tut.tat.tet.", "tut tat tet");
        doTestFolderEquals("Das ist Dr. Folder", "Das ist Dr.  Folder");
        doTestFolderEquals("Das.ist.Dr.Folder", "Das ist Dr. Folder");
        doTestFolderEquals("Das.ist.Dr..Folder", "Das ist Dr.  Folder");
    }
    
    private static void doTestFileEquals(String fileName, String expected) throws IOException {
        final File file = new File(fileName);
        file.createNewFile();
        try {
            assertEquals(expected, FileUtil.clearFileNameDots(file));
        } finally {
            if(file.delete() == false) {
                System.err.println("Could not delete file '"+file.getAbsolutePath()+"'!");
            }
        }
    }
    
    private static void doTestFolderEquals(String folderName, String expected) throws IOException {
        final File folder = new File(folderName);
        if(folder.mkdir() == false) {
            throw new IOException("Could not create folder '"+folder.getAbsolutePath()+"'!");
        }
        try {
            assertEquals(expected, FileUtil.clearFileNameDots(folder));
        } finally {
            if(folder.delete() == false) {
                System.err.println("Could not delete folder '"+folder.getAbsolutePath()+"'!");
            }
        }
    }

}
