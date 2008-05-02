package net.sourceforge.omov.app.gui.export;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

class ComboMovieSelection extends JComboBox {

    private static final long serialVersionUID = -597660834469335997L;

    public ComboMovieSelection() {
        super(new ComboMovieModel());
        this.setSelectedIndex(0);
        this.setOpaque(false);
    }
    
    public MovieSelectionMode getMovieSelectionMode() {
        return ((ComboMovieModel) this.getModel()).getSelectionMode(this.getSelectedIndex());
    }
    
    enum MovieSelectionMode {
        ALL("all"), VISIBLE("visible"), SELECTED("selected");
        
        private final String label;
        private MovieSelectionMode(String label) {
            this.label = label;
        }
        public String getLabel() {
            return this.label;
        }
    }
    
    private static class ComboMovieModel extends DefaultComboBoxModel{
        private static final long serialVersionUID = -3341594987214035821L;
        
        private static final MovieSelectionMode[] DATA = new MovieSelectionMode[] { MovieSelectionMode.ALL, MovieSelectionMode.VISIBLE, MovieSelectionMode.SELECTED};
        
        public Object getElementAt(int row) {
            return DATA[row].getLabel();
        }
        
        public MovieSelectionMode getSelectionMode(int row) {
            return DATA[row];
        }

        public int getSize() {
            return 3;
        }
        
    }
}
