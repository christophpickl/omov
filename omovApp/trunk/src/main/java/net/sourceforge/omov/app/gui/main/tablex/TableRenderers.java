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

package net.sourceforge.omov.app.gui.main.tablex;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import net.sourceforge.jpotpourri.util.PtDurationUtil;
import net.sourceforge.omov.app.gui.comp.rating.RatingSlider;
import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.bo.Quality;
import net.sourceforge.omov.core.bo.Resolution;
import net.sourceforge.omov.core.bo.Movie.MovieField;
import net.sourceforge.omov.logic.ColumnsCoverFactory;
import net.sourceforge.omov.logic.MovieTableColumns;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
class TableRenderers {

    private TableRenderers() {
        /* no instantiation */
    }

    public static void updateRenderers(MovieTableX table) {
        table.getColumnByLabel(MovieTableColumns.COVER_COLUMN_LABEL).setCellRenderer(new CoverRenderer());
        table.getColumnByField(MovieField.TITLE).setCellRenderer(new TitleRenderer());
        table.getColumnByField(MovieField.RATING).setCellRenderer(new RatingRenderer());
        table.getColumnByField(MovieField.QUALITY).setCellRenderer(new QualityRenderer());
        table.getColumnByField(MovieField.DURATION).setCellRenderer(new DurationRenderer());

        table.getColumnByField(MovieField.GENRES).setCellRenderer(new DefaultRenderer());
        table.getColumnByField(MovieField.DATE_ADDED).setCellRenderer(new DateAddedRenderer());
        table.getColumnByField(MovieField.FILE_SIZE_KB).setCellRenderer(new DefaultRenderer());
        table.getColumnByField(MovieField.FOLDER_PATH).setCellRenderer(new DefaultRenderer());
        table.getColumnByField(MovieField.FORMAT).setCellRenderer(new DefaultRenderer());
        table.getColumnByField(MovieField.RESOLUTION).setCellRenderer(new ResolutionRenderer());
        table.getColumnByField(MovieField.YEAR).setCellRenderer(new DefaultRenderer());
        table.getColumnByField(MovieField.STYLE).setCellRenderer(new DefaultRenderer());
        table.getColumnByField(MovieField.LANGUAGES).setCellRenderer(new DefaultRenderer());
        table.getColumnByField(MovieField.FILES).setCellRenderer(new DefaultRenderer());
        table.getColumnByField(MovieField.ACTORS).setCellRenderer(new DefaultRenderer());
        table.getColumnByField(MovieField.SUBTITLES).setCellRenderer(new DefaultRenderer());
        table.getColumnByField(MovieField.DIRECTOR).setCellRenderer(new DefaultRenderer());
    }

    private static Movie getMovie(JTable table, final int row) {
        final MovieTableModel model = (MovieTableModel) table.getModel();
        final Movie movie = model.getMovieAt(((MovieTableX) table).convertRowIndexToModel(row));
        return movie;
    }


    private static class DefaultRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 783309666699748436L;

        @Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//            System.out.println("isSelected="+isSelected+"; value="+value);

            final JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if(isSelected) {
                lbl.setBackground(Constants.getColorSelectedBackground());
                lbl.setForeground(Constants.getColorSelectedForeground());
            } else
            	lbl.setForeground(Color.BLACK);
            return lbl;
        }
    }

    private static class CoverRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 2926410557857576765L;
        @Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//            final JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            final Movie movie = getMovie(table, row);
            final JLabel lbl = new JLabel(ColumnsCoverFactory.getInstance().getImage(movie));
            if(isSelected) {
                lbl.setBackground(Constants.getColorSelectedBackground());
                lbl.setForeground(Constants.getColorSelectedForeground());
            }
            lbl.setOpaque(true);
            return lbl;
        }
    }


    private static class TitleRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 3071418230735107442L;
        private static final Font FONT_TITLE_SEEN = new Font("Default", Font.PLAIN, 12);
        private static final Font FONT_TITLE_UNSEEN = new Font("Default", Font.BOLD, 12);
        @Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            final JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            final Movie movie = getMovie(table, row);

            lbl.setFont(movie.isSeen() ? FONT_TITLE_SEEN : FONT_TITLE_UNSEEN);
            if(isSelected) {
                lbl.setBackground(Constants.getColorSelectedBackground());
                lbl.setForeground(Constants.getColorSelectedForeground());
            }

            return lbl;
        }
    }

    private static class RatingRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = -4946524787250006442L;

        @Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            final Movie movie = getMovie(table, row);

            if(movie.getRating() == 0 && isSelected == false) {
                final JLabel lbl = new JLabel(); // display nothing if not selected and not yet rated
                if(isSelected) lbl.setBackground(Constants.getColorSelectedBackground());
                lbl.setOpaque(true);
                return lbl;
            }

            final Color primaryColor;
            if(isSelected == false) {
            	primaryColor = null; // null == default (dark gray)
            } else if(table.hasFocus() == true) {
            	primaryColor = Constants.getColorSelectedForeground();
            } else {
            	primaryColor = null;
            }
            
            final RatingSlider ratingPanel = new RatingSlider(movie.getRating(), primaryColor);
            ratingPanel.setOpaque(false);


            final JPanel panel = new JPanel();
            panel.setOpaque(true);
            if(isSelected) panel.setBackground(Constants.getColorSelectedBackground());
            final GridBagLayout layout = new GridBagLayout();
            final GridBagConstraints c = new GridBagConstraints();
            layout.setConstraints(panel, c);
            panel.setLayout(layout);

            c.gridx = 0;
            c.gridy = 0;
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.NONE;
            panel.add(ratingPanel);
            return panel;
        }
    }
    private static class QualityRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1331841353063350382L;

        @Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            final Quality quality = (Quality) value;
            final JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            lbl.setText(quality.label());
            if(isSelected) {
                lbl.setBackground(Constants.getColorSelectedBackground());
                lbl.setForeground(Constants.getColorSelectedForeground());
            }
            return lbl;
        }
    }
    private static class DurationRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = -614495795501324589L;

        @Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            final int duration = ((Integer) value).intValue();
            final JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            lbl.setText(PtDurationUtil.formatDurationShort(duration));
            if(isSelected) {
                lbl.setBackground(Constants.getColorSelectedBackground());
                lbl.setForeground(Constants.getColorSelectedForeground());
            }
            lbl.setHorizontalAlignment(SwingConstants.RIGHT); // set duration right aligned

            return lbl;
        }
    }



    private static class DateAddedRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 5247456238762520415L;

        @Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            final Date dateAdded = (Date) value;
            final JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            lbl.setText(Movie.DATE_ADDED_FORMAT_SHORT.format(dateAdded));
            if(isSelected) {
                lbl.setBackground(Constants.getColorSelectedBackground());
                lbl.setForeground(Constants.getColorSelectedForeground());
            }
            return lbl;
        }
    }

    private static class ResolutionRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 2629156722065858479L;

        @Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            final Resolution resolution = (Resolution) value;
            final JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            lbl.setText(resolution.getFormattedString());
            if(isSelected) {
                lbl.setBackground(Constants.getColorSelectedBackground());
                lbl.setForeground(Constants.getColorSelectedForeground());
            }
            return lbl;
        }
    }

//  private static class Renderer extends DefaultTableCellRenderer {
//      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//          final Movie movie = getMovie(table, row);
//          final JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//      }
//  }
}
