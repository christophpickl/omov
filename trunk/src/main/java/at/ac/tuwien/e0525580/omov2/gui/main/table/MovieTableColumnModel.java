package at.ac.tuwien.e0525580.omov2.gui.main.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

import at.ac.tuwien.e0525580.omov2.bo.movie.Movie;
import at.ac.tuwien.e0525580.omov2.bo.movie.Movie.MovieField;
import at.ac.tuwien.e0525580.omov2.gui.comp.rating.RatingField;

public class MovieTableColumnModel extends DefaultTableColumnModel {

    private static final long serialVersionUID = 7710678522012774954L;

    private static final Font FONT_TITLE_SEEN = new Font("Default", Font.PLAIN, 12);
    private static final Font FONT_TITLE_UNSEEN = new Font("Default", Font.BOLD, 12);

    private static Color COLOR_EVEN = Color.WHITE;
    private static Color COLOR_ODD = new Color(241, 245, 250);
    private static Color COLOR_SELECTED = new Color(61, 128, 223);
    
    public void addColumn(final TableColumn column) {
        super.addColumn(column);
        
        this.addMyHeaderRenderer(column);
        
        
        this.addCustomCellRenderer(column);
//        final String headerTitle = (String) column.getHeaderValue();
//        if (headerTitle.equals(MovieField.RATING.name())) {
//            column.setCellRenderer(new RatingRenderer());
//        }
    }

    private void addMyHeaderRenderer(final TableColumn column) {
        column.setHeaderRenderer(new SelectionTableCellRenderer());
    }

//    public static class RatingRenderer extends RatingField implements TableCellRenderer {
//        private static final long serialVersionUID = 6752980659437308938L;
//        public RatingRenderer() {
//            super(0);
//        }
//        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//            final MovieTableModel model = (MovieTableModel) table.getModel();
//            final Movie movie = model.getMovieAt(row);
//            this.setValue(movie.getRating());
//            this.setOpaque(true);
//            this.setBackground(Color.RED);
//            return this;
//        }
//    }
    
    
    private void addCustomCellRenderer(final TableColumn column) {
        column.setCellRenderer(new DefaultTableCellRenderer() {
            
            private static final long serialVersionUID = 5468986787028229952L;
            public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int colIndex) {
                final Component superComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, colIndex);

                final MovieTableModel model = (MovieTableModel) table.getModel();
                final Movie movie = model.getMovieAt(row);
                

                final Color bgColor;
                final Color fgColor;
                if(isSelected) {
                    bgColor = COLOR_SELECTED;
                    fgColor = Color.WHITE;
                } else {
                    bgColor = (row % 2 == 0 ? COLOR_EVEN : COLOR_ODD);
                    fgColor = Color.BLACK;
                }
                
                final String headerTitle = (String) column.getHeaderValue();
                if (headerTitle.equals(MovieField.TITLE.label())) {
                    superComponent.setFont(movie.isSeen() ? FONT_TITLE_SEEN : FONT_TITLE_UNSEEN);
                    
                } else if(headerTitle.equals(MovieField.DURATION.label())) {
                    this.setHorizontalAlignment(SwingConstants.RIGHT); // set duration right aligned
                    
                } else if(headerTitle.equals(MovieField.RATING.label())) {
                    if(movie.getRating() == 0 && isSelected == false) {
                        final JLabel lbl = new JLabel(); // display nothing if not selected and not yet rated
                        lbl.setBackground(bgColor);
                        lbl.setOpaque(true);
                        return lbl;
                    }
                    final Color primaryColor = isSelected ? Color.WHITE : null; // null == default (dark gray)
                    final RatingField field = new RatingField(movie.getRating(), primaryColor);
                    field.setBackground(bgColor);
                    field.setOpaque(true);
                    
                    return field;
                }

                this.setBackground(bgColor);
                this.setForeground(fgColor);
                
                return superComponent;
            }
        });
    }
    
}