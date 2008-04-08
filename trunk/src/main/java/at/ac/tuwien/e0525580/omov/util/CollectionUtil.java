package at.ac.tuwien.e0525580.omov.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class CollectionUtil<T> {

    public CollectionUtil() {
        // no instantiation
    }
    
    public Collection<T> asCollection(T... values) {
        final Collection<T> result = new HashSet<T>(values.length);
        
        for (T T : values) {
            result.add(T);
        }
        
        return result;
    }
    
    public Set<T> asSet(T... values) {
        final Set<T> result = new HashSet<T>(values.length);
        
        for (T T : values) {
            result.add(T);
        }
        
        return result;
    }
    
    public List<T> asList(T... values) {
        final List<T> result = new ArrayList<T>(values.length);
        
        for (T T : values) {
            result.add(T);
        }
        
        return result;
    }
    
    public List<T> asImmutableList(T... values) {
        return Collections.unmodifiableList(this.asList(values));
    }
    


    public static Collection<String> asStringCollection(String... values) {
        return new CollectionUtil<String>().asCollection(values);
    }
    public static Set<String> asStringSet(String... values) {
        return new CollectionUtil<String>().asSet(values);
    }
    
    public static String[] asArray(String... values) {
        return values;
    }
    
    public static String toString(Collection<?> collection) {
        if(collection.size() == 0) return "";
        
        StringBuilder sb = new StringBuilder();
        for (Object object : collection) {
            sb.append(", ").append(object.toString());
        }
        return sb.substring(2);
    }
    
    

    public static Set<String> immutableSet(Set<String> original) {
        return Collections.unmodifiableSet(new HashSet<String>(original));
    }

    public static Set<String> immutableSet(String... values) {
        return Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(values)));
    }

    public static List<String> immutableList(String... values) {
        return Collections.unmodifiableList(new ArrayList<String>(Arrays.asList(values)));
    }
    
    public static String toString(Set<String> set) {
        if(set.size() == 0) return "";
        
        final StringBuilder sb = new StringBuilder();
        for (String s : set) {
            sb.append(", ").append(s);
        }
        return sb.substring(2);
    }
}
