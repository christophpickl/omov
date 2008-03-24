package at.ac.tuwien.e0525580.omov.smartfolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.db4o.query.Constraint;
import com.db4o.query.Query;

public abstract class FileSizeMatch extends AbstractMatch<Long> {

    private static final Log LOG = LogFactory.getLog(DurationMatch.class);

    public static final String LABEL_EQUALS = "equals";
    public static final String LABEL_GREATER = "greater";
    public static final String LABEL_LESS = "less";

    static final List<String> ALL_MATCH_LABELS;
    static {
        List<String> tmp = new ArrayList<String>();
        tmp.add(LABEL_EQUALS);
        tmp.add(LABEL_GREATER);
        tmp.add(LABEL_LESS);
        ALL_MATCH_LABELS = Collections.unmodifiableList(tmp);
    }
    
    public static FileSizeMatch newEquals(Long value) {
        return new FileSizeMatch(LABEL_EQUALS, new Long[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing equals for value '"+this.getValueAt(0)+"'.");
                
                return query.constrain(this.getValueAt(0)).equal();
            }
        };
    }
    
    public static FileSizeMatch newGreater(Long value) {
        return new FileSizeMatch(LABEL_EQUALS, new Long[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing equals for value '"+this.getValueAt(0)+"'.");
                
                return query.constrain(this.getValueAt(0)).greater();
            }
        };
    }
    
    public static FileSizeMatch newLess(Long value) {
        return new FileSizeMatch(LABEL_EQUALS, new Long[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing equals for value '"+this.getValueAt(0)+"'.");
                
                return query.constrain(this.getValueAt(0)).smaller();
            }
        };
    }
    
    private FileSizeMatch(String label, Long[] values) {
        super(label, values);
    }

}
