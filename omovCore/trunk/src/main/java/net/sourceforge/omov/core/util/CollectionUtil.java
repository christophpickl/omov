/*
 * OurMovies - Yet another movie manager
 * Copyright (C) 2008 Christoph Pickl (christoph_pickl@users.sourceforge.net)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.sourceforge.omov.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
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
    	return toString(set, ", ");
    }
    
    public static String toString(Set<String> set, String separator) {
        if(set.size() == 0) return "";
        
        boolean first = true;
        final StringBuilder sb = new StringBuilder();
        for (String s : set) {
        	if(first == true) first = false;
        	else sb.append(separator);
            sb.append(s);
        }
        return sb.toString();
    }
}
