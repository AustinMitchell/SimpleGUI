package simple.gui;

import simple.gui.data.*;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.awt.FontMetrics;
import java.awt.Color;
import java.awt.Font;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;

class Draw {
    private static class _StrokeData {
        public int      thickness;
        public boolean  isRound;

        public _StrokeData(int thickness, boolean isRound) {
            this.thickness  = thickness;
            this.isRound    = isRound;
        }

        @Override
        public int hashCode() {
            return Objects.hash(thickness, isRound);
        }
    }

    /** Stores font data for caching use. */
    public static class SimpleFont{
        private String  _name;
        private int     _style;
        private int     _size;

        public String   name()  { return _name; }
        public int      style() { return _style; }
        public int      size()  { return _size; }

        public SimpleFont(String name, int style, int size) {
            _name   = name;
            _style  = style;
            _size   = size;
        }

        @Override
        public int hashCode() {
            return Objects.hash(_name, _style, _size);
        }
    }

    /** Multiplies each value in a Color object by a constant.
     * @param c				Base color
     * @param scale			Constant scalar value **/
    public static Color scaleColor(Color c, float scale) {
        if (c==null) return null;
        return new Color((int)(c.getRed()*scale), (int)(c.getGreen()*scale), (int)(c.getBlue()*scale));
    }
    /** Multiplies each value in a Color object by a constant. Each color is multiplied by a different constant.
     * @param c				Base color
     * @param rscale		Constant scalar value for red
     * @param gscale		Constant scalar value for green
     * @param bscale		Constant scalar value for blue**/
    public static Color scaleColor(Color c, float rscale, float gscale, float bscale) {
        if (c==null) return null;
        return new Color(c.getRed()*rscale, c.getGreen()*gscale, c.getBlue()*bscale);
    }
    /** Multiplies each value in a Color object by a constant. Each color is multiplied by a different constant.
     * @param c             Base color
     * @param rscale        Constant scalar value for red
     * @param gscale        Constant scalar value for green
     * @param bscale        Constant scalar value for blue
     * @param ascale        Constant scalar value for alpha**/
    public static Color scaleColor(Color c, float rscale, float gscale, float bscale, float ascale) {
        if (c==null) return null;
        return new Color(c.getRed()*rscale, c.getGreen()*gscale, c.getBlue()*bscale, c.getAlpha()*ascale);
    }

    public static final Color EMPTY_COLOR = new Color(0, 0, 0, 0);
    public static final IntVector2D ORIGIN = new IntVector2D(0, 0);
    
    private static final Map<_StrokeData, BasicStroke> _STROKE_CACHE = new HashMap<_StrokeData, BasicStroke>();
    private static final Map<Draw.SimpleFont, Font> _FONT_CACHE = new HashMap<Draw.SimpleFont, Font>();

    private Image           _canvas;
    private Graphics2D      _g2D;  
    private Draw.SimpleFont _font;
    private FontMetrics     _fontMetrics;
    private Color           _fill;
    private Color           _stroke;
    private boolean         _expandCanvas;
    private IntVector2D     _offset;
    private IntVector2D     _size;

    public Draw(int width, int height) {
        this(new Image(width, height));
        clear();
    }

    public Draw(ConstIntVector2D dimensions) {
        this(dimensions.x(), dimensions.y());
        clear();
    }

    public Draw(Image imageContext) {
        _canvas         = imageContext;
        _g2D            = _canvas.graphics2D();
        _fontMetrics    = _g2D.getFontMetrics();
        _fill           = EMPTY_COLOR;
        _stroke         = EMPTY_COLOR;
        _offset         = new IntVector2D(0, 0);
        _size           = new IntVector2D(imageContext.size());

        setFont(new Draw.SimpleFont("Consolas", Font.PLAIN, 12));
        setAntiAliasing(true);
    }

    //////////////////////////////
    //------- PROPERTIES -------//
    //////////////////////////////

    /** Returns the width of the drawing context */
    public int              width()         { return _size.x(); }

    /** Returns the height of the drawing context */
    public int              height()        { return _size.y(); }

    /** Returns the dimensions of the drawing context */
    public ConstIntVector2D size()          { return _size.asConst(); }

    /** Returns the internal offset of the drawing context used against drawing operations */
    public ConstIntVector2D offset()        { return _offset.asConst(); }

    /** Returns the backing image object */
    public Image            canvas()        { return _canvas; }

    /** Returns the stored Graphics2D object. */
    public Graphics2D       g2D()           { return _g2D; }

    /** Returns the current text drawing font*/
    public Draw.SimpleFont  font()          { return _font; }

    /** Returns a FontMetrics object from the stored Graphics2D object's current font. */
    public FontMetrics      fontMetrics()   { return _fontMetrics; }

    /** Returns the stored fill color */
    public Color            fill()          { return _fill; }

    /** Returns the stored stroke color */
    public Color            stroke()        { return _stroke; }
    
    ///////////////////////////
    //------- SETTERS -------//
    ///////////////////////////

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

    /** Set the font to use for text drawing operations
     * @param font  Draw.SimpleFont object
     */
    public void setFont(Draw.SimpleFont font) { 
        Font awtFont;
        
        if (_FONT_CACHE.containsKey(font)) {
            awtFont = _FONT_CACHE.get(font);
        } else {
            awtFont = new Font(font._name, font._size, font._style);
            _FONT_CACHE.put(font, awtFont);
        }

        _font = font;
        _g2D.setFont(awtFont);
        _fontMetrics = _g2D.getFontMetrics();
    }

    /**  Set whether to expand canvas on drawing operations. If false, when you draw out of bounds of the backing image, the out of bounds operations
     * would get cut off. If true, the image will be expanded when you draw out of bounds.
     * @param expandCanvas  New expansion setting
     */
    public void setExpandCanvas(boolean expandCanvas) { _expandCanvas = expandCanvas; }

    private void checkBounds(IntVector2D... points) {
        if (!_expandCanvas) { return; }

        IntVector2D currentSize    = _size.copy();
        IntVector2D currentOffset  = _offset.copy();
        boolean recreateImage   = false;

        for (IntVector2D p: points) {
            if (p.x() < -currentOffset.x()) {
                currentOffset.setX(p.x());
                recreateImage = true;
            }
            if (p.y() < -currentOffset.y()) {
                currentOffset.setY(-p.y());
                recreateImage = true;
            }
            if (p.x() > currentSize.x()) {
                currentSize.setX(p.x());
                recreateImage = true;
            }
            if (p.y() > currentSize.y()) {
                currentSize.setY(p.y());
                recreateImage = true;
            }
        }

        if (recreateImage) {
            Image    oldImage   = _canvas;
            IntVector2D offsetDiff = currentOffset.sub(_offset);

            _canvas = new Image(currentSize.add(currentOffset));
            _g2D = _canvas.graphics2D();

            oldImage.draw(_g2D, offsetDiff.x(), offsetDiff.y());

            _offset = currentOffset;
            _size = currentSize;
            setFont(_font);
        }
    }

    /////////////////////////////////
    //------- SHAPE DRAWING -------//
    /////////////////////////////////

    /** Clears the drawing context */
    public void clear() {
        _g2D.setComposite(AlphaComposite.Clear);
        _g2D.fillRect(0, 0, _canvas.width(), _canvas.height());
        _g2D.setComposite(AlphaComposite.SrcOver);
    }

    /** Draws a line from ({x1, y1}) to ({x2, y2}). Color is set by stroke
     * @param x1    x-coordinate of first point
     * @param y1    y-coordinate of first point
     * @param x2    x-coordinate of second point
     * @param y2    y-coordinate of second point
     */
    public void line(int x1, int y1, int x2, int y2) {
        line(new IntVector2D(x1, y1), new IntVector2D(x2, y2));
    }

    /** Draws a line from {p1} to {p2}. Color is set by stroke
     * @param p1    Coordinate of first point
     * @param p2    Coordinate of second point
     */
    public void line(IntVector2D p1, IntVector2D p2) {
        if (_expandCanvas) checkBounds(p1, p2);

        if (_stroke != null) {
            _g2D.setColor(_stroke);
            _g2D.drawLine(p1.x()+offset().x(),
                          p1.y()+offset().y(),
                          p2.x()+offset().x(),
                          p2.y()+offset().y());
        }
    }

    /** Draws a polygon defined by a series of points. The outline is specified by stroke, the fill by fill. 
     * @param points   Sequence of Vector2D objects 
     */
    public void polygon(IntVector2D... points) {
        if (_expandCanvas) checkBounds(points);

        int[] x_coord = new int[points.length];
        int[] y_coord = new int[points.length];

        for (int i=0; i<points.length; i++) {
            x_coord[i] = points[i].x()+offset().x();
            y_coord[i] = points[i].y()+offset().y();
        }

        if (_fill != null) {
            _g2D.setColor(_fill);
            _g2D.fillPolygon(x_coord, y_coord, points.length);
        }
        
        if (_stroke != null) {
            _g2D.setColor(_stroke);
            _g2D.drawPolygon(x_coord, y_coord, points.length);
        }
    }

    /** Draws a rectangle with the bottom-left point at ({x}, {y}) with the dimensions ({width}, {height})
     * @param x         x-coordinate of bottom-left corner
     * @param y         y-coordinate of bottom-left corner
     * @param width     Width of rectangle
     * @param height    Height of rectangle
     */
    public void rect(int x, int y, int width, int height) {
        rect(new IntVector2D(x, y), new IntVector2D(width, height));
    }

    /** Draws a rectangle with the bottom-left point at {pos} with the dimensions {dim}
     * @param pos   Coordinate of bottom-left corner
     * @param dim   Dimensions of rectangle
     */
    public void rect(IntVector2D pos, IntVector2D dim) {
        if (_expandCanvas) checkBounds(pos, pos.add(dim));

        if (_fill != null) {
            _g2D.setColor(_fill);
            _g2D.fillRect(pos.x()+offset().x(),
                          pos.y()+offset().y(),
                          dim.x(),
                          dim.y());
        }
        if (_stroke != null) {
            _g2D.setColor(_stroke);
            _g2D.drawRect(pos.x()+offset().x(),
                          pos.y()+offset().y(),
                          dim.x(),
                          dim.y());
        }
    }

    /** Draws an arc with the center point at ({x}, {y}) with the dimensions ({width}, {height}), between the angles {startAngle} and {endAngle}
     * @param x             x-coordinate of center
     * @param y             y-coordinate of center
     * @param width         Width of rectangle
     * @param height        Height of rectangle
     * @param startAngle    Angle to begin drawing the arc
     * @param endAngle      Angle to end drawing the arc
     */
    public void arc(int x, int y, int width, int height, int startAngle, int endAngle) {
        arc(new IntVector2D(x, y), new IntVector2D(width, height), startAngle, endAngle);
    }

    /** Draws an arc with the center point at {pos} with the dimensions {dim}, between the angles {startAngle} and {endAngle}
     * @param pos           Coordinate of center
     * @param dim           Dimensions of rectangle
     * @param startAngle    Angle to begin drawing the arc
     * @param endAngle      Angle to end drawing the arc
     */
    public void arc(IntVector2D pos, IntVector2D dim, int startAngle, int endAngle) {
        if (_expandCanvas) checkBounds(pos.sub(dim.div(2)),
                                       pos.add(dim.div(2)));

        if (_fill != null) {
            _g2D.setColor(_fill);
            _g2D.fillArc(pos.x()+offset().x(),
                         pos.y()+offset().y(),
                         dim.x(),
                         dim.y(),
                         startAngle,
                         endAngle);
        }
        if (_stroke != null) {
            _g2D.setColor(_stroke);
            _g2D.drawArc(pos.x()+offset().x(),
                         pos.y()+offset().y(),
                         dim.x(),
                         dim.y(),
                         startAngle,
                         endAngle);
        }
    }

    /** Draws an oval with the bottom-left point at ({x}, {y}) with dimensions ({width}, {height})
     * @param x         x-coordinate of bottom-left point
     * @param y         y-coordinate of bottom-left point
     * @param width     Width of oval
     * @param height    Height of oval
     */
    public void oval(int x, int y, int width, int height) {
        oval(new IntVector2D(x, y), new IntVector2D(width, height));
    }

    /** Draws an oval with the bottom-left point at {pos} with dimensions {dim}
     * @param pos   Coordinate of bottom-left point
     * @param dim   Dimensions of oval
     */
    public void oval(IntVector2D pos, IntVector2D dim) {
        if (_expandCanvas) checkBounds(pos, pos.add(dim));

        if (_fill != null) {
            _g2D.setColor(_fill);
            _g2D.fillOval(pos.x()+offset().x(),
                          pos.y()+offset().y(),
                          dim.x(),
                          dim.y());
        }
        if (_stroke != null) {
            _g2D.setColor(_stroke);
            _g2D.drawOval(pos.x()+offset().x(),
                          pos.y()+offset().y(),
                          dim.x(),
                          dim.y());
        }
    }

     /** Draws an oval with the center point at ({x}, {y}) with radii ({radiusX}, {radiusY})
     * @param x         x-coordinate of center
     * @param y         y-coordinate of center
     * @param radiusX   x-radius of oval
     * @param radiusY   y-radius of oval
     */
    public void ovalCentered(int x, int y, int radiusX, int radiusY) {
        ovalCentered(new IntVector2D(x, y), new IntVector2D(radiusX, radiusY));
    }

    /** Draws an oval with the center point at {pos} with {dim} storing x and y radii
     * @param pos   Coordinate of center
     * @param dim   Dimensions of the x and y radii
     */
    public void ovalCentered(IntVector2D pos, IntVector2D dim) {
        if (_expandCanvas) checkBounds(pos.sub(dim.div(2)),
                                       pos.add(dim.div(2)));

        if (_fill != null) {
            _g2D.setColor(_fill);
            _g2D.fillOval(pos.x()-dim.x()+offset().x(),
                          pos.y()-dim.y()+offset().y(),
                          dim.x()*2,
                          dim.y()*2);
        }
        if (_stroke != null) {
            _g2D.setColor(_stroke);
            _g2D.drawOval(pos.x()-dim.x()+offset().x(),
                          pos.y()-dim.y()+offset().y(),
                          dim.x()*2,
                          dim.y()*2);
        }
    }

    ////////////////////////////////
    //------- TEXT DRAWING -------//
    ////////////////////////////////

    /** Draw text with the bottom-left corner at ({x}, {y})
     * @param textToDraw    Text to draw
     * @param x             x-coordinate of bottom-left corner
     * @param y             y-coordinate of bottom-left corner
     */
    public void text(String textToDraw, int x, int y) {
        if (_stroke != null) {
            _g2D.setColor(_stroke);
            _g2D.drawString(textToDraw, x, y + _fontMetrics.getMaxAscent());
        }
    }

    /** Draw text with the bottom-left corner at {pos}
     * @param textToDraw    Text to draw
     * @param pos           Coordinate of bottom-left corner
     */
    public void text(String textToDraw, IntVector2D pos) {
        //TODO: checkBounds
        text(textToDraw, pos.x(), pos.y());
    }
    
    /** Draw text using {font} with the bottom-left corner at ({x}, {y})
     * @param textToDraw    Text to draw
     * @param x             x-coordinate of bottom-left corner
     * @param y             y-coordinate of bottom-left corner
     * @param font          Font to set drawing context to
     */
    public void text(String textToDraw, int x, int y, SimpleFont font) {
        setFont(font);
        text(textToDraw, x, y);
    }
    
    /** Draw text using {font} with the bottom-left corner at {pos}
     * @param textToDraw    Text to draw
     * @param pos           Coordinate of bottom-left corner
     * @param font          Font to set drawing context to
     */
    public void text(String textToDraw, IntVector2D pos, SimpleFont font) {
        setFont(font);
        text(textToDraw, pos.x(), pos.y());
    }

    /** Draw text with the bottom-right corner at ({x}, {y})
     * @param textToDraw    Text to draw
     * @param x             x-coordinate of bottom-right corner
     * @param y             y-coordinate of bottom-right corner
     */
    public void textRight(String textToDraw, int x, int y) {
        if (_stroke != null) {
            _g2D.setColor(_stroke);
            _g2D.drawString(textToDraw, x-_fontMetrics.stringWidth(textToDraw), y + _fontMetrics.getMaxAscent());
        }
    }

    /** Draw text with the bottom-right corner at {pos}
     * @param textToDraw    Text to draw
     * @param pos           Coordinate of bottom-right corner
     */
    public void textRight(String textToDraw, IntVector2D pos) {
        //TODO: checkBounds
        textRight(textToDraw, pos.x(), pos.y());
    }
    
    /** Draw text using {font} with the bottom-right corner at ({x}, {y})
     * @param textToDraw    Text to draw
     * @param x             x-coordinate of bottom-right corner
     * @param y             y-coordinate of bottom-right corner
     * @param font          Font to set drawing context to
     */
    public void textRight(String textToDraw, int x, int y, SimpleFont font) {
        setFont(font);
        textRight(textToDraw, x, y);
    }
    
    /** Draw text using {font} with the bottom-right corner at {pos}
     * @param textToDraw    Text to draw
     * @param pos           Coordinate of bottom-right corner
     * @param font          Font to set drawing context to
     */
    public void textRight(String textToDraw, IntVector2D pos, SimpleFont font) {
        setFont(font);
        textRight(textToDraw, pos.x(), pos.y());
    }

    /** Draw text with the center point at ({x}, {y})
     * @param textToDraw    Text to draw
     * @param x             x-coordinate of center point
     * @param y             y-coordinate of center point
     */
    public void textCentered(String textToDraw, int x, int y) {
        if (_stroke != null) {
            _g2D.setColor(_stroke);
            _g2D.drawString(textToDraw, x - _fontMetrics.stringWidth(textToDraw)/2, (int)(y + _fontMetrics.getStringBounds(textToDraw, _g2D).getHeight()/4.0));
        }
    }

    /** Draw text with the center point at {pos}
     * @param textToDraw    Text to draw
     * @param pos           Coordinate of center point
     */
    public void textCentered(String textToDraw, IntVector2D pos) {
        //TODO: checkBounds
        textCentered(textToDraw, pos.x(), pos.y());
    }
    
    /** Draw text using {font} with the center point at ({x}, {y})
     * @param textToDraw    Text to draw
     * @param x             x-coordinate of center point
     * @param y             y-coordinate of center point
     * @param font          Font to set drawing context to
     */
    public void textCentered(String textToDraw, int x, int y, SimpleFont font) {
        setFont(font);
        textCentered(textToDraw, x, y);
    }
    
    /** Draw text using {font} with the center point at {pos}
     * @param textToDraw    Text to draw
     * @param pos           Coordinate of center point
     * @param font          Font to set drawing context to
     */
    public void textCentered(String textToDraw, IntVector2D pos, SimpleFont font) {
        setFont(font);
        textCentered(textToDraw, pos.x(), pos.y());
    }

    /////////////////////////////////
    //------- IMAGE DRAWING -------//
    /////////////////////////////////

    /** Draw image with the bottom-left corner at ({x}, {y})
     * @param imageToDraw   Image to draw
     * @param x             x-coordinate of bottom-left corner
     * @param y             x-coordinate of bottom-left corner
     */
    public void image(Image imageToDraw, int x, int y) {
        imageToDraw.draw(_g2D, x, y);
    }

    /** Draw image with the bottom-left corner at {pos}
     * @param imageToDraw   Image to draw
     * @param pos           Coordinate of bottom-left corner
     */
    public void image(Image imageToDraw, IntVector2D pos) {
        //TODO: checkBounds
        imageToDraw.draw(_g2D, pos.x(), pos.y());
    }

    /** Draw image with the center point at ({x}, {y})
     * @param imageToDraw   Image to draw
     * @param x             x-coordinate of center
     * @param y             x-coordinate of center
     */
    public void imageCentered(Image imageToDraw, int x, int y) {
        imageToDraw.drawCentered(_g2D, x, y);
    }
    /** Draw image with the center point at {pos}
     * @param imageToDraw   Image to draw
     * @param pos           Coordinate of center
     */
    public void imageCentered(Image imageToDraw, IntVector2D pos) {
        //TODO: checkBounds
        imageToDraw.drawCentered(_g2D, pos.x(), pos.y());
    }

    /** Draw image with the center point at ({x}, {y}) rotated by {angle} radians
     * @param imageToDraw   Image to draw
     * @param x             x-coordinate of center
     * @param y             x-coordinate of center
     */
    public void imageRotated(Image imageToDraw, int x, int y, double angle) {
        imageToDraw.drawRotated(_g2D, x, y, angle);
    }

    /** Draw image with the center point at {pos} rotated by {angle} radians
     * @param imageToDraw   Image to draw
     * @param pos           Coordinate of center
     */
    public void imageRotated(Image imageToDraw, IntVector2D pos, double angle) {
        //TODO: checkBounds
        imageToDraw.drawRotated(_g2D, pos.x(), pos.y(), angle);
    }

    /** Draws another Draw object onto the back image. Draw's the context's origin at ({x}, {y}). If the context has not been offset, this point will
     * be the bottom-left corner of the context's backing image
     * @param drawOther Draw object to render on this context
     * @param x         x-coordinate to render the context's origin point
     * @param y         y-coordinate to render the context's origin point
     */
    public void drawOthercontext(Draw drawContext, int x, int y) {
        //TODO: Complete
    }
}