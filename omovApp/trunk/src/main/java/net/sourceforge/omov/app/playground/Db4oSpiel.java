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

package net.sourceforge.omov.app.playground;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class Db4oSpiel {
    public static void main(String[] args) {
//        if(new File("spielerei.yap").exists()) new File("spielerei.yap").delete();
//        ObjectContainer db = Db4o.openFile("spielerei.yap");
//
//        
//        db.set(new Person("aaa --X"));
//        db.set(new Person("bbxc"));
//        db.set(new Person("ccc"));
//        
//        Query q = db.query();
//        q.constrain(Person.class);
//        
//        Constraint c = q.descend("name").constrain("x").contains();
//        // q.descend("other attribut").constrain(123).and(c); // or(c)
//        ObjectSet<Person> os = q.execute();
//        while(os.hasNext()) {
//            System.out.println(os.next().getName());
//        }
    }
    
    public static class Person {
        private String name;
        public Person(String name) {
            this.name = name;
        }
        public String getName() {
            return this.name;
        }
    }
}
