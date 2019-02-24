package test.simple.gui;

import static org.junit.Assert.*;

import org.junit.Test;

import simple.gui.data.IntVector2D;

public class VectorTest {
    @Test
    public void verifyOperators() {
        IntVector2D v1 = new IntVector2D(100, 100);
        IntVector2D v2 = new IntVector2D(150, 150);
        IntVector2D v3 = new IntVector2D(200, 200);

        assertTrue(v2.isBoundWithinPoints(v1, v3));
        assertTrue(v2.isBoundWithinBox(v1, v1));
    }
}