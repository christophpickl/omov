package net.sourceforge.omov.core.smartfolder;

public class SmartFolderTextTest extends AbstractSmartFolderTest {
    
    /*
     * comment attribute values:
     * - "Comment"
     * - "COMMENT"
     * - "NO COMMENT"
     * - "COMMENTOS"
     * - "ramramram"
     */

    public void testCaseSensitivity() throws Exception {
        this.checkSomeExisting(TextCriterion.newComment(TextMatch.newEquals("Comment")), 1); // case sensitive
        this.checkSomeExisting(TextCriterion.newComment(TextMatch.newEquals("ram")), 0);
        this.checkSomeExisting(TextCriterion.newComment(TextMatch.newContains("COMMENT")), 4);
        
    }

    /*
     * director attribute values:
     * - "John Doe"
     * other four are empty
     */
    public void testContains() throws Exception {
        this.checkSomeExisting(TextCriterion.newDirector(TextMatch.newContains("john doe")), 1); // -> is case insensitive
    }
}
