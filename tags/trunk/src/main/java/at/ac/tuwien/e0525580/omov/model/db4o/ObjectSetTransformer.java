package at.ac.tuwien.e0525580.omov.model.db4o;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.db4o.ObjectSet;

public class ObjectSetTransformer<T> {

    public Set<T> transformSet(ObjectSet<T> os) {
        final Set<T> result = new HashSet<T>();
        while(os.hasNext()) {
            result.add(os.next());
        }
        return Collections.unmodifiableSet(result);
    }

    public List<T> transformList(ObjectSet<T> os) {
        final List<T> result = new LinkedList<T>();
        while(os.hasNext()) {
            result.add(os.next());
        }
        return Collections.unmodifiableList(result);
    }

    public List<T> transformMutableList(ObjectSet<T> os) {
        final List<T> result = new LinkedList<T>();
        while(os.hasNext()) {
            result.add(os.next());
        }
        return result;
    }
}