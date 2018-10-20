package simple.gui;

import static org.junit.Assert.*;

import org.junit.Test;

public class DrawTest {
    @Test
    public void verifyCanvasExpand() {
        Draw drawContext = new Draw(100, 100);
        drawContext.setExpandCanvas(true);

        drawContext.line(20, -10, 110, 50);

        assertEquals(0, drawContext.offset().x());
        assertEquals(10, drawContext.offset().y());
        assertEquals(110, drawContext.size().x());
        assertEquals(100, drawContext.size().y());
    }
}