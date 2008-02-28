package at.ac.tuwien.e0525580.omov.smartfolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.db4o.query.Constraint;
import com.db4o.query.Query;

public abstract class BoolMatch extends AbstractMatch<Boolean> {

    private static final Log LOG = LogFactory.getLog(BoolMatch.class);

    public static final String LABEL_IS = "is";
    public static final String LABEL_IS_NOT = "is not";
    
    static final List<String> ALL_MATCH_LABELS;
    static {
        List<String> tmp = new ArrayList<String>();
        tmp.add(LABEL_IS);
        tmp.add(LABEL_IS_NOT);
        ALL_MATCH_LABELS = Collections.unmodifiableList(tmp);
    }
    
    public static BoolMatch newEquals(Boolean value) {
        return new BoolMatch(LABEL_IS, new Boolean[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing equals for value '"+this.getValueAt(0)+"'.");
                Boolean value = this.getValueAt(0);
                return query.constrain(value);
            }
        };
    }
    public static BoolMatch newNotEquals(Boolean value) {
        return new BoolMatch(LABEL_IS_NOT, new Boolean[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing equals for value '"+this.getValueAt(0)+"'.");
                Boolean value = this.getValueAt(0);
                return query.constrain(value).not();
            }
        };
    }
    
    private BoolMatch(String label, Boolean[] values) {
        super(label, values);
    }
}
