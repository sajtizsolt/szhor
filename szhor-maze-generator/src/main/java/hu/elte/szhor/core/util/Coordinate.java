package hu.elte.szhor.core.util;

import java.util.Objects;

public class Coordinate {
    public int rowIndex;
    public int columnIndex;

    public Coordinate(int rowIndex, int columnIndex) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof Coordinate)) return false;

        var other = (Coordinate) object;
        return Objects.equals(this.rowIndex, other.rowIndex)
                && Objects.equals(this.columnIndex, other.columnIndex);
    }
}
