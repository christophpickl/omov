package at.ac.tuwien.e0525580.omov.spielwiese;


public class Db4oSpiel {
    public static void main(String[] args) {
//        if(new File("spielerei.db4").exists()) new File("spielerei.db4").delete();
//        ObjectContainer db = Db4o.openFile("spielerei.db4");
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
