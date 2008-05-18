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

package net.sourceforge.omov.core.model.db4o;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.db4o.ObjectSet;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
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