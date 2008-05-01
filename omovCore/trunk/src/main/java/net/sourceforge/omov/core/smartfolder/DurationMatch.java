package net.sourceforge.omov.core.smartfolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.omov.core.util.NumberUtil.Duration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.db4o.query.Constraint;
import com.db4o.query.Query;

public abstract class DurationMatch extends AbstractMatch<Duration> {

    private static final Log LOG = LogFactory.getLog(DurationMatch.class);

    public static final String LABEL_EQUALS = "equals";
    public static final String LABEL_LONGER = "longer";
    public static final String LABEL_SHORTER = "shorter";

    static final List<String> ALL_MATCH_LABELS;
    static {
        List<String> tmp = new ArrayList<String>();
        tmp.add(LABEL_EQUALS);
        tmp.add(LABEL_LONGER);
        tmp.add(LABEL_SHORTER);
        ALL_MATCH_LABELS = Collections.unmodifiableList(tmp);
    }
    
    public static DurationMatch newEquals(Duration value) {
        return new DurationMatch(LABEL_EQUALS, new Duration[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                final Duration value = this.getValueAt(0);
                LOG.debug("Preparing equals for value '"+value+"'.");
                
                return query.constrain(value.getTotalInMinutes()).equal();
            }
        };
    }
    
    public static DurationMatch newLonger(Duration value) {
        return new DurationMatch(LABEL_LONGER, new Duration[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                final Duration value = this.getValueAt(0);
                LOG.debug("Preparing longer for value '"+value+"'.");
                
                return query.constrain(value.getTotalInMinutes()).greater();
            }
        };
    }
    
    public static DurationMatch newShorter(Duration value) {
        return new DurationMatch(LABEL_SHORTER, new Duration[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                final Duration value = this.getValueAt(0);
                LOG.debug("Preparing shorter for value '"+value+"'.");
                
                return query.constrain(value.getTotalInMinutes()).smaller();
            }
        };
    }
    
    private DurationMatch(String label, Duration[] values) {
        super(label, values);
    }

}
