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

package net.sourceforge.omov.core.bo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
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
