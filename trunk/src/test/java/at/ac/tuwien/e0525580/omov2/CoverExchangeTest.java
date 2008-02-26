package at.ac.tuwien.e0525580.omov2;

import junit.framework.TestCase;

public class CoverExchangeTest extends TestCase {
    
    public void xtestSimpleExchange() throws Exception {
        
        // insert movie (no cover set)
        // update movie (set cover)
        // - check if file is existing
        // update movie (unset cover)
        // - check if file was deleted
        
    }

    public void xtestScanAndEnhanceExchange() throws Exception {
        // create new folder
        // scan it
        // import it
        // check if proper file is existing
    }
    
    public void xtestEnhanceExisting() throws Exception {
        // create movie with set cover
        // afterwards enhance that movie
        // - coverfile should be replaced (?)
    }
}
