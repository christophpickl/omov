package net.sourceforge.omov.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import junit.framework.TestCase;

public abstract class AbstractTestCase extends TestCase {

    private static final Log LOG = LogFactory.getLog(AbstractTestCase.class);
    
    
    protected void setUp() throws Exception {
        super.setUp();
        
        LOG.info("### " + this.getName() + " START");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        LOG.info("### " + this.getName() + " END");
    }
    
}
