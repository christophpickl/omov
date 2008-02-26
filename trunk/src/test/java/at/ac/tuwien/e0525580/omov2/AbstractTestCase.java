package at.ac.tuwien.e0525580.omov2;

import junit.framework.TestCase;

public abstract class AbstractTestCase extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
        
        System.out.println("running test '"+this.getName()+"' ...");
    }
    
}
