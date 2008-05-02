package net.sourceforge.omov.app.gui.comp.generic.table;

public interface IColumnComparable<T> {

    abstract int compareValue(T m1, T m2);
}
