package hu.elte.szhor.generator.util;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

class CoordinateTest {

    @Test
    void shouldEqualCoordinatesWithSameRowAndColumnAndHaveSameHashCode() {
        var c1 = new Coordinate(2, 4);
        var c2 = new Coordinate(2, 4);
        var c3 = new Coordinate(2, 5);

        assertThat(c1).isEqualTo(c2);
        assertThat(c1).hasSameHashCodeAs(c2);
        assertThat(c1).isNotEqualTo(c3);
        assertThat(c1.rowIndex()).isEqualTo(2);
        assertThat(c1.columnIndex()).isEqualTo(4);

        var set = new HashSet<Coordinate>();
        set.add(c1);
        assertThat(set).contains(c2);
    }
}
