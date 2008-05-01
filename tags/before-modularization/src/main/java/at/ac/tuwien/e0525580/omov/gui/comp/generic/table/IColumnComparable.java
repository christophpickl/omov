package at.ac.tuwien.e0525580.omov.gui.comp.generic.table;

public interface IColumnComparable<T> {

    abstract int compareValue(T m1, T m2);
}
