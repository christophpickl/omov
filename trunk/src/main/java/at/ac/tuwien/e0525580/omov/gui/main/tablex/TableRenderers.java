package at.ac.tuwien.e0525580.omov.gui.main.tablex;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.bo.Quality;
import at.ac.tuwien.e0525580.omov.bo.Resolution;
import at.ac.tuwien.e0525580.omov.bo.Movie.MovieField;
import at.ac.tuwien.e0525580.omov.gui.comp.rating.RatingField;
import at.ac.tuwien.e0525580.omov.util.NumberUtil;

class TableRenderers {
    
    private TableRenderers() {
        
    }
    
    public static void updateRenderers(MovieTableX table) {
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
        // director
        // actors
    }

    private static Movie getMovie(JTable table, final int row) {
        final MovieTableModel model = (MovieTableModel) table.getModel();
        final Movie movie = model.getMovieAt(((MovieTableX) table).convertRowIndexToModel(row));
        return movie;
    }


    private static class DefaultRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 783309666699748436L;

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            final JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if(isSelected) {
                lbl.setBackground(MovieTableX.COLOR_SELECTED_BG);
                lbl.setForeground(MovieTableX.COLOR_SELECTED_FG);
            }
            return lbl;
        }
    }
    
    
    private static class TitleRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 783309666699748436L;
        private static final Font FONT_TITLE_SEEN = new Font("Default", Font.PLAIN, 12);
        private static final Font FONT_TITLE_UNSEEN = new Font("Default", Font.BOLD, 12);
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            final JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            final Movie movie = getMovie(table, row);
            
            lbl.setFont(movie.isSeen() ? FONT_TITLE_SEEN : FONT_TITLE_UNSEEN);
            if(isSelected) {
                lbl.setBackground(MovieTableX.COLOR_SELECTED_BG);
                lbl.setForeground(MovieTableX.COLOR_SELECTED_FG);
            }
            
            return lbl;
        }
    }
    private static class RatingRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 783309666699748436L;
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            final Movie movie = getMovie(table, row);
            
            if(movie.getRating() == 0 && isSelected == false) {
                final JLabel lbl = new JLabel(); // display nothing if not selected and not yet rated
                if(isSelected) lbl.setBackground(MovieTableX.COLOR_SELECTED_BG);
                lbl.setOpaque(true);
                return lbl;
            }
            final Color primaryColor = isSelected ? MovieTableX.COLOR_SELECTED_FG : null; // null == default (dark gray)
            final RatingField field = new RatingField(movie.getRating(), primaryColor);
            if(isSelected) field.setBackground(MovieTableX.COLOR_SELECTED_BG);
            field.setOpaque(true);
            
            return field;
        }
    }
    private static class QualityRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 783309666699748436L;
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            final Quality quality = (Quality) value;
            final JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            lbl.setText(quality.label());
            if(isSelected) {
                lbl.setBackground(MovieTableX.COLOR_SELECTED_BG);
                lbl.setForeground(MovieTableX.COLOR_SELECTED_FG);
            }
            return lbl;
        }
    }
    private static class DurationRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 783309666699748436L;
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            final int duration = (Integer) value;
            final JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            lbl.setText(NumberUtil.formatDurationShort(duration));
            if(isSelected) {
                lbl.setBackground(MovieTableX.COLOR_SELECTED_BG);
                lbl.setForeground(MovieTableX.COLOR_SELECTED_FG);
            }
            lbl.setHorizontalAlignment(SwingConstants.RIGHT); // set duration right aligned
            
            return lbl;
        }
    }


    
    private static class DateAddedRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 783309666699748436L;
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            final Date dateAdded = (Date) value;
            final JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            lbl.setText(Movie.DATE_ADDED_FORMAT_SHORT.format(dateAdded));
            if(isSelected) {
                lbl.setBackground(MovieTableX.COLOR_SELECTED_BG);
                lbl.setForeground(MovieTableX.COLOR_SELECTED_FG);
            }
            return lbl;
        }
    }

    private static class ResolutionRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 783309666699748436L;
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            final Resolution resolution = (Resolution) value;
            final JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            lbl.setText(resolution.getFormattedString());
            if(isSelected) {
                lbl.setBackground(MovieTableX.COLOR_SELECTED_BG);
                lbl.setForeground(MovieTableX.COLOR_SELECTED_FG);
            }
            return lbl;
        }
    }

//  private static class Renderer extends DefaultTableCellRenderer {
//      private static final long serialVersionUID = 783309666699748436L;
//      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//          final Movie movie = getMovie(table, row);
//          final JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//      }
//  }
}
