package at.ac.tuwien.e0525580.omov.smartfolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.db4o.query.Constraint;
import com.db4o.query.Query;

public abstract class RatingMatch extends AbstractMatch<Integer> {

    private static final Log LOG = LogFactory.getLog(RatingMatch.class);

    public static final String LABEL_EQUALS = "equals";
    public static final String LABEL_NOT_EQUALS = "not equals";
    public static final String LABEL_GREATER = "greater";
    public static final String LABEL_LESS = "less";
    public static final String LABEL_IN_THE_RANGE = "in the range";

    static final List<String> ALL_MATCH_LABELS;
    static {
        List<String> tmp = new ArrayList<String>();
        tmp.add(LABEL_EQUALS);
        tmp.add(LABEL_NOT_EQUALS);
        tmp.add(LABEL_GREATER);
        tmp.add(LABEL_LESS);
        tmp.add(LABEL_IN_THE_RANGE);
        ALL_MATCH_LABELS = Collections.unmodifiableList(tmp);
    }
    
    public static RatingMatch newEquals(Integer value) {
        return new RatingMatch(LABEL_EQUALS, new Integer[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing equals for value '"+this.getValueAt(0)+"'.");
                return query.constrain(this.getValueAt(0));
            }
        };
    }
    public static RatingMatch newNotEquals(Integer value) {
        return new RatingMatch(LABEL_NOT_EQUALS, new Integer[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing not equals for value '"+this.getValueAt(0)+"'.");
                return query.constrain(this.getValueAt(0)).not();
            }
        };
    }
    public static RatingMatch newGreater(Integer value) {
        return new RatingMatch(LABEL_GREATER, new Integer[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing not equals for value '"+this.getValueAt(0)+"'.");
                return query.constrain(this.getValueAt(0)).greater();
            }
        };
    }
    public static RatingMatch newLess(Integer value) {
        return new RatingMatch(LABEL_LESS, new Integer[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing less for value '"+this.getValueAt(0)+"'.");
                return query.constrain(this.getValueAt(0)).smaller();
            }
        };
    }
    public static RatingMatch newInRange(Integer lowerBound, Integer upperBound) {
        return new RatingMatch(LABEL_IN_THE_RANGE, new Integer[] { lowerBound, upperBound } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing in range of '"+this.getValueAt(0)+"' to '"+this.getValueAt(1)+"'.");
                return query.constrain(this.getValueAt(1)).smaller().equal().and(
                       query.constrain(this.getValueAt(0)).greater().equal());
            }
        };
    }
    
    private RatingMatch(String label, Integer[] values) {
        super(label, values);
    }

}
