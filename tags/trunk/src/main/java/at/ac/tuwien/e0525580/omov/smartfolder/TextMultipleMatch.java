package at.ac.tuwien.e0525580.omov.smartfolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.db4o.query.Constraint;
import com.db4o.query.Query;

public abstract class TextMultipleMatch extends AbstractMatch<String> {

    private static final Log LOG = LogFactory.getLog(DurationMatch.class);

    public static final String LABEL_CONTAINS = "contains";
    public static final String LABEL_NOT_CONTAINS = "not contains";

    static final List<String> ALL_MATCH_LABELS;
    static {
        List<String> tmp = new ArrayList<String>();
        tmp.add(LABEL_CONTAINS);
        tmp.add(LABEL_NOT_CONTAINS);
        ALL_MATCH_LABELS = Collections.unmodifiableList(tmp);
    }
    
    public static TextMultipleMatch newContains(String value) {
        return new TextMultipleMatch(LABEL_CONTAINS, new String[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                final String value = this.getValueAt(0);
                LOG.debug("Preparing contains for value '"+value+"'.");

                if(value.length() == 0) {
                    return query.constrain(this.getValueAt(0)).equal();
                }
                return query.constrain(this.getValueAt(0)).like();
                
            }
        };
    }
    
    public static TextMultipleMatch newNotContains(String value) {
        return new TextMultipleMatch(LABEL_NOT_CONTAINS, new String[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                final String value = this.getValueAt(0);
                LOG.debug("Preparing contains for value '"+value+"'.");

                if(value.length() == 0) {
                    return query.constrain(this.getValueAt(0)).not().equal();
                }
                return query.constrain(this.getValueAt(0)).not().like();
                
            }
        };
    }
    
    private TextMultipleMatch(String label, String[] values) {
        super(label, values);
    }

}
