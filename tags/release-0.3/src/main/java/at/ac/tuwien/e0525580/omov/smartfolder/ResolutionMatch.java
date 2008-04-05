package at.ac.tuwien.e0525580.omov.smartfolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.bo.Resolution;

import com.db4o.query.Constraint;
import com.db4o.query.Query;

public abstract class ResolutionMatch extends AbstractMatch<Resolution> {

    private static final Log LOG = LogFactory.getLog(ResolutionMatch.class);

    public static final String LABEL_EQUALS = "equals";
    public static final String LABEL_GREATER = "greater";

    static final List<String> ALL_MATCH_LABELS;
    static {
        List<String> tmp = new ArrayList<String>();
        tmp.add(LABEL_EQUALS);
        tmp.add(LABEL_GREATER);
        ALL_MATCH_LABELS = Collections.unmodifiableList(tmp);
    }
    
    public static ResolutionMatch newEquals(Resolution value) {
        return new ResolutionMatch(LABEL_EQUALS, new Resolution[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing equals for value '"+this.getValueAt(0)+"'.");
                Resolution value = this.getValueAt(0);
                return query.constrain(value);
            }
        };
    }

    /**
     * @param value inclusive
     */
    public static ResolutionMatch newGreater(Resolution value) {
        return new ResolutionMatch(LABEL_GREATER, new Resolution[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing greater for value '"+this.getValueAt(0)+"'.");
                Resolution value = this.getValueAt(0);
                return query.descend("width").constrain(value.getWidth()).greater().and(
                        query.descend("height").constrain(value.getHeight()).greater());
            }
        };
    }
    
    // in range?!
    
    private ResolutionMatch(String label, Resolution[] values) {
        super(label, values);
    }
}
