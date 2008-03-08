package at.ac.tuwien.e0525580.omov.bo;

import java.util.Comparator;

import at.ac.tuwien.e0525580.omov.FatalException;


public enum Quality {
    
    UNRATED(0, "Unrated"),
    UGLY(1, "Ugly"),
    NORMAL(2, "Normal"),
    GOOD(3, "Good"),
    BEST(4, "DVD Best");
    
    private final int id;
    private final String label;
    
    private Quality(int id, String label) {
        this.id = id;
        this.label = label;
    }
    
    public int id() {
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
}
