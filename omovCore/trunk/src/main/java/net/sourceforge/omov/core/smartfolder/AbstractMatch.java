package net.sourceforge.omov.core.smartfolder;

import java.util.Arrays;

import com.db4o.query.Constraint;
import com.db4o.query.Query;

public abstract class AbstractMatch<V> {

    /** eg: equals, not equals, lower, greater, in range, ... */
    private final String label;
    
    private final V[] values;
    
    
    AbstractMatch(String label, V[] values) {
        this.label = label;
        this.values = values;
    }
    
    abstract Constraint prepareDb4oQuery(Query query);
    
    final V getValueAt(int index) {
        return this.values[index];
    }
    
    final int getValueCount() {
        return this.values.length;
    }
    
    public final String getLabel() {
        return this.label;
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName()).append("[");
        sb.append("label=").append(label).append(";");
        sb.append("values=").append(Arrays.toString(values)).append(";");
        sb.append("]");
        return sb.toString();
    }
}
