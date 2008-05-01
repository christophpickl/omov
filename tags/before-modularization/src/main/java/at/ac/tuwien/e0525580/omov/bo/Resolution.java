package at.ac.tuwien.e0525580.omov.bo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Resolution implements Comparable<Resolution> {

    public static final Resolution R0x0 = new Resolution(0, 0);
    public static final Resolution R800x600 = new Resolution(800, 600);
    
    
    private final int width;
    
    private final int height;
    
    private final String formattedString;
    
    
    public Resolution(int width, int height) {
        if(width < 0) throw new IllegalArgumentException("width < 0: " + width);
        if(height < 0) throw new IllegalArgumentException("height < 0: " + height);
        this.width = width;
        this.height = height;
        this.formattedString = width + "x" + height;
    }

    public String getFormattedString() {
        return this.formattedString;
    }
    
    @Override
    public String toString() {
        return this.formattedString;
    }

    @Override
    public boolean equals(Object object) {
        if((object instanceof Resolution) == false) return false;
        Resolution that = (Resolution) object;
        return this.width == that.width && this.height == that.height;
    }

    @Override
    public int hashCode() {
        return width + height + 73;
    }
    
    
    public int getHeight() {
        return this.height;
    }
    
    public int getWidth() {
        return this.width;
    }

    public int compareTo(Resolution that) {
        final int areaThis = this.width * this.height;
        final int areaThat = that.width * that.height;
        return areaThis - areaThat;
    }
    
    public static void main(String[] args) {
        List<Resolution> list = new ArrayList<Resolution>();
        list.add(new Resolution(1024, 768));
        list.add(R0x0);
        list.add(new Resolution(300, 300));
        list.add(new Resolution(80, 300));
        Collections.sort(list);
        
        System.out.println(Arrays.toString(list.toArray())); // -> [0x0, 80x300, 300x300, 1024x768]
    }
}
