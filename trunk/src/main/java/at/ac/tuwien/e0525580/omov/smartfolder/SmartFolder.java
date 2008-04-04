package at.ac.tuwien.e0525580.omov.smartfolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.bo.Movie;

import com.db4o.query.Constraint;
import com.db4o.query.Query;

public class SmartFolder {

    private static final Log LOG = LogFactory.getLog(SmartFolder.class);

    public static final int DATA_VERSION = 1;
    
    public static final Comparator<SmartFolder> COMPARATOR_NAME = new Comparator<SmartFolder>() {
        public int compare(final SmartFolder o1, final SmartFolder o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };
    
    private final long id;
    private final String name;
    private final boolean matchAll;
    private final List<AbstractColumnCriterion> criteria;

    /**
     * used to get from db4o by prototype
     */
    public SmartFolder(long id) {
        this.id = id;
        this.name = null;
        this.matchAll = false;
        this.criteria = null;
    }
    public SmartFolder(long id, String name, boolean matchAll, List<AbstractColumnCriterion> criteria) {
        this.id = id;
        this.name = name;
        this.matchAll = matchAll;
        this.criteria = new ArrayList<AbstractColumnCriterion>(criteria);
    }
    
    public static SmartFolder byOther(long id, SmartFolder that) {
        return new SmartFolder(id, that.name, that.matchAll, new ArrayList<AbstractColumnCriterion>(that.criteria));
    }
    
    public void pepareQuery(Query query) {
        assert(this.criteria.size() > 0);
        LOG.info("Preparing query with smartfolder '"+this.getName()+"' ("+this.criteria.size()+" criteria).");
        
        query.constrain(Movie.class);
        
        List<Constraint> constraints = new ArrayList<Constraint>(this.criteria.size());
        
        for (AbstractColumnCriterion criterion: this.criteria) {
            constraints.add(criterion.getDb4oConstraint(query));
        }
        
        for (int i = 1; i < constraints.size(); i++) {
            Constraint c1 = constraints.get(i-1);
            Constraint c2 = constraints.get(i);
            if(this.matchAll) c1.and(c2);
            else c1.or(c2);
        }
    }
    
    public List<AbstractColumnCriterion> getCriteria() {
        return Collections.unmodifiableList(this.criteria);
    }
    
    public long getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean isMatchAll() {
        return this.matchAll;
    }
    
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName()).append("[");
        sb.append("id=").append(id).append(";");
        sb.append("name=").append(name).append(";");
        sb.append("matchAll=").append(matchAll).append(";");
        sb.append("criteria.length=").append(criteria.size()).append(";");
        boolean first = true;
        for (AbstractColumnCriterion criterion : this.criteria) {
            if(first == true) first = false;
            else sb.append(";");
            sb.append(criterion.toString());
        }
        sb.append("]");
        return sb.toString();
    }
}
