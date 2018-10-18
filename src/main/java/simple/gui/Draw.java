package simple.gui;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.awt.FontMetrics;
import java.awt.Color;
import java.awt.Font;
import java.awt.BasicStroke;

class Draw {
    private static class _StrokeData {
        public int thickness;
        public boolean isRound;

        public _StrokeData(int thickness, boolean isRound) {
            this.thickness = thickness;
            this.isRound = isRound;
        }

        @Override
        public int hashCode() {
            return Objects.hash(thickness, isRound);
        }
    }

    public static final Color EMPTY_COLOR = new Color(0, 0, 0, 0);
    
    private static final Map<_StrokeData, BasicStroke> _STROKE_CACHE = new HashMap<_StrokeData, BasicStroke>();

    private Image       _backImage;
    private Graphics2D  _g2D;
    private FontMetrics _fontMetrics;
    private Color       _fill;
    private Color       _stroke;

    public Draw(Vector2D dimensions) {
        _backImage = new Image(dimensions.x(), dimensions.y());
        _g2D = _backImage.graphics2D();
        _fontMetrics = _g2D.getFontMetrics();
        _fill = EMPTY_COLOR;
        _stroke = EMPTY_COLOR;
    }

    public Draw(Image imageContext) {
        _backImage = imageContext;
        _g2D = _backImage.graphics2D();
        _fontMetrics = _g2D.getFontMetrics();
        _fill = EMPTY_COLOR;
        _stroke = EMPTY_COLOR;
    }

    //------- GETTERS -------//

    /** Returns the backing image object */
    public Image       backImage()   { return _backImage; }

    /** Returns the DrawObject's stored Graphics2D object. */
    public Graphics2D  g2D()         { return _g2D; }

    /** Returns a FontMetrics object from the stored Graphics2D object's current font. */
    public FontMetrics fontMetrics() { return _fontMetrics; }

    /** Returns the stored fill color */
    public Color       fill()        { return _fill; }

    public Color       stroke()      { return _stroke; }
    
    //------- SETTERS -------//

    /**
     * Sets whether to render text with anti-aliasing enabled.
     * @param antiAlias
     */
    public void setAntiAliasing(boolean antiAlias) {
        if (antiAlias) {
            _g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        } else {
            _g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        }
    }

    /** Sets the drawing fill color. 
     * @param fill      New color to use for the fill 
     */
    public void setFill(Color fill) { _fill = fill; }

    /** Sets the drawing stroke color. Stroke is used for the borders of shapes as well as for rendering text. This will set
     * the stroke thickness to 1.
     * @param stroke    New color to use for the stroke
    */
    public void setStroke(Color stroke) { setStroke(stroke, 1); }

    /** Sets the drawing stroke color. Stroke is used for the borders of shapes as well as for rendering text.
     * @param stroke    New color to use for the stroke
     * @param thickness Thickness value for the stroke on drawing
    */
    public void setStroke(Color stroke, int thickness) { _setStroke(stroke, thickness); }

    /** Sets the drawing stroke color and makes the ends of the stroke round. Stroke is used for the borders of shapes as well as for rendering text.
     * @param stroke    New color to use for the stroke
     * @param thickness Thickness value for the stroke on drawing
    */
    public void setStrokeRound(Color stroke, int thickness) { _setStrokeRound(stroke, thickness); }
        
    // Retrieves a stroke object from the cache or adds to it, then sets the stroke
    private void _setStroke(Color stroke, int thickness) {
        _StrokeData sd = new _StrokeData(thickness, false);
        BasicStroke bs;
        if (_STROKE_CACHE.containsKey(sd)) {
            bs = _STROKE_CACHE.get(sd);
        } else {
            bs = new BasicStroke(thickness);
            _STROKE_CACHE.put(sd, bs);
        }
        _g2D.setStroke(bs);
        _stroke = stroke;
    }
    // Retrieves a stroke object from the cache or adds to it, then sets the stroke
    private void _setStrokeRound(Color stroke, int thickness) {
        _StrokeData sd = new _StrokeData(thickness, true);
        BasicStroke bs;
        if (_STROKE_CACHE.containsKey(sd)) {
            bs = _STROKE_CACHE.get(sd);
        } else {
            bs = new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            _STROKE_CACHE.put(sd, bs);
        }
        _g2D.setStroke(bs);
        _stroke = stroke;
    }
}