package at.ac.tuwien.e0525580.omov.bo;

import java.util.Comparator;

import at.ac.tuwien.e0525580.omov.FatalException;


public final class Quality implements Comparable<Quality> {

    public static final Quality UNRATED = new Quality(0, "Unrated");
    public static final Quality UGLY    = new Quality(1, "Ugly");
    public static final Quality NORMAL  = new Quality(2, "Normal");
    public static final Quality GOOD    = new Quality(3, "Good");
    public static final Quality BEST    = new Quality(4, "DVD Best");
    
    
    private final int id;
    private final String label;
    
    private Quality(int id, String label) {
        this.id = id;
        this.label = label;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String label() {
        return this.label;
    }
    
    public static Quality getQualityById(int id) {
        if(id < 0 || id > 4) {
            throw new IllegalArgumentException("quality id " + id);
        }
        switch (id) {
            case 0: return UNRATED;
            case 1: return UGLY;
            case 2: return NORMAL;
            case 3: return GOOD;
            case 4: return BEST;
            default: throw new FatalException("Unhandled id: " + id);
        }
    }
    
    public static Comparator<Quality> COMPARATOR = new Comparator<Quality>() {
        public int compare(Quality o1, Quality o2) {
            return o1.id - o2.id;
        }
    };
    
    public String toString() {
        return "Quality[id="+this.id+";label="+label+"]";
    }
    
    /**
     * db4o creates other instances of quality singletons :(
     */
    @Override
    public boolean equals(Object object) {
    	if( (object instanceof Quality) == false) return false;
    	Quality that = (Quality) object;
    	return this.id == that.id;
    }

    @Override
    public int hashCode() {
    	return this.id;
    }
    
    /*
     * 
    
    private static final Map<Integer, String> QUALITY_MAPPING;
    static {
        Map<Integer, String> tmp = new HashMap<Integer, String>();
        tmp.put(0, "Unrated");
        tmp.put(1, "Ugly");
        tmp.put(2, "Normal");
        tmp.put(3, "Good");
        tmp.put(4, "DVD best");
        QUALITY_MAPPING = Collections.unmodifiableMap(tmp);
    }
    
    public static String mapQuality(int i) {
        if(i < 0 || i > 4) throw new IllegalArgumentException("quality must be between 0-4 but was: " + i);
        return QUALITY_MAPPING.get(i);
    }
     */

    public int compareTo(Quality that) {
        return this.id - that.id;
    }
}
