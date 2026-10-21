package hu.elte.szhor.core.model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

class NodeTest {

    @Test
    void shouldCreateNodeWithCoordinates() {
        var node = new Node(2, 3);

        assertThat(node.getX()).isEqualTo(2);
        assertThat(node.getY()).isEqualTo(3);
        assertThat(node.getMobileRobot()).isNull();
        assertThat(node.getSettledRobot()).isNull();
        assertThat(node.getIdAsString()).isEqualTo(Integer.toString(node.getId()));
    }

    @Test
    void shouldEqualNodeWithSameCoordinatesAndHaveSameHashCode() {
        var node1 = Node.create(1, 1);
        var node2 = Node.create(1, 1);
        var node3 = Node.create(1, 2);

        assertThat(node1).isEqualTo(node2);
        assertThat(node1).hasSameHashCodeAs(node2);
        assertThat(node1).isNotEqualTo(node3);

        var set = new HashSet<Node>();
        set.add(node1);
        assertThat(set).contains(node2);
    }
}
