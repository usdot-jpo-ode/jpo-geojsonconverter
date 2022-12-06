package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class MapNodeTest {
    @Test
    public void testNodes() {
        Integer[] expectedDelta = new Integer[1];
        expectedDelta[0] = 1;
        MapNode mapNode = new MapNode();
        mapNode.setDelta(expectedDelta);
        assertEquals(expectedDelta[0], mapNode.getDelta()[0]);
    }

    @Test
    public void testDWidth() {
        Integer expectedDWidth = 1;
        MapNode mapNode = new MapNode();
        mapNode.setDWidth(expectedDWidth);
        assertEquals(expectedDWidth, mapNode.getDWidth());
    }

    @Test
    public void testDElevation() {
        Integer expectedDElevation = 1;
        MapNode mapNode = new MapNode();
        mapNode.setDElevation(expectedDElevation);
        assertEquals(expectedDElevation, mapNode.getDElevation());
    }

    @Test
    public void testStopLine() {
        Boolean expectedStopLine = true;
        MapNode mapNode = new MapNode();
        mapNode.setStopLine(expectedStopLine);
        assertEquals(expectedStopLine, mapNode.getStopLine());
    }

    @Test
    public void testEquals() {
        MapNode object = new MapNode();
        MapNode otherObject = new MapNode();
        boolean equals = object.equals(object);
        assertEquals(equals, true);
        
        boolean otherEquals = object.equals(otherObject);
        assertEquals(otherEquals, true);

        String string = "string";
        boolean notEquals = otherObject.equals(string);
        assertEquals(notEquals, false);
    }
    
    @Test
    public void testHashCode() {
        MapNode mapNode = new MapNode();
        Integer hash = mapNode.hashCode();
        assertNotNull(hash);
    }
    
    @Test
    public void testToString() {
        MapNode mapNode = new MapNode();
        String string = mapNode.toString();
        assertNotNull(string);
    }
}
