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

package net.sourceforge.omov.core.smartfolderx;

import net.sourceforge.omov.core.bo.Movie.MovieField;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class TextCriterion extends AbstractColumnCriterion<TextMatch> {

    public static TextCriterion newTitle(TextMatch match) {
        return new TextCriterion(match, MovieField.TITLE);
    }
    public static TextCriterion newStyle(TextMatch match) {
        return new TextCriterion(match, MovieField.STYLE);
    }
    public static TextCriterion newDirector(TextMatch match) {
        return new TextCriterion(match, MovieField.DIRECTOR);
    }
    public static TextCriterion newFormat(TextMatch match) {
        return new TextCriterion(match, MovieField.FORMAT);
    }
    public static TextCriterion newComment(TextMatch match) {
        return new TextCriterion(match, MovieField.COMMENT);
    }
    public static TextCriterion newFolderPath(TextMatch match) {
        return new TextCriterion(match, MovieField.FOLDER_PATH);
    }
    
    private TextCriterion(TextMatch match, MovieField field) {
        super(match, field);
    }
}
