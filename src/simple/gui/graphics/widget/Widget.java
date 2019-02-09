package simple.gui.graphics.widget;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import simple.gui.Input;
import simple.gui.data.ConstIntVector2D;
import simple.gui.data.IntVector2D;
import simple.gui.graphics.Draw;
import simple.gui.graphics.DrawOperation;

/**
 * Class which all widgets are derived from. Controls most internal mechanisms, only 
 * requires the user to implement the drawWidget and updateWidget functions
 */
public abstract class Widget {
    ////////////////////////////////////////////////////////////////
    // ------------------------- STATICS ------------------------ //
    ////////////////////////////////////////////////////////////////


    
    public static final Color DEFAULT_FILL_COLOR    = Color.white;
    public static final Color DEFAULT_STROKE_COLOR  = Color.black;

    ////////////////////////////////////////////////////////////////
    // ------------------------- FIELDS ------------------------- //
    ////////////////////////////////////////////////////////////////

    protected IntVector2D   _pos;
    protected IntVector2D   _size;
    protected IntVector2D   _posRelative;
    protected Color         _fillColor;
    protected Color         _strokeColor;

    private boolean     _isHovering;
    private boolean     _isClicking;
    private boolean     _isClicked;
    private boolean     _isEnabled;
    private boolean     _isVisible;
    private boolean     _isBlocked;
    private boolean     _flaggedForRendering;
    
    private Draw            _drawCanvas;
    private DrawOperation   _drawBefore;
    private DrawOperation   _drawAfter;
    private List<Widget> _childWidgets;

    ////////////////////////////////////////////////////////////////
    // ----------------------- PROPERTIES ----------------------- //
    ////////////////////////////////////////////////////////////////

    /** Returns a constant reference to position vector relative to its parent widget */
    public ConstIntVector2D pos()           { return _pos; }
    
    /** Returns a vector for the mouse position relative to the widget */
    protected ConstIntVector2D posRelative() { return _posRelative; }

    /** Returns a constant reference to size vector */
    public ConstIntVector2D size()          { return _size; }

    /** Returns the fill color */
    public Color            fillColor()     { return _fillColor; }

    /** Returns the stroke color */
    public Color            strokeColor()   { return _strokeColor; }

    /** Returns if the mouse is considered to be hovering over the widget */
    public boolean          isHovering()    { return _isHovering; }

    /** Returns if the mouse is considered to be clicking on the widget */
    public boolean          isClicking()    { return _isClicking; }

    /** Returns if the widget has been considered to have been clicked. This is the transition from mouse button down to mouse button up */
    public boolean          isClicked()     { return _isClicked; }

    /** Returns if the widget is enabled for user interaction. If disabled, no updates will occur */
    public boolean          isEnabled()     { return _isEnabled; }

    /** Returns if the widget is visible to the user. If disabled, no drawing will take place */
    public boolean          isVisible()     { return _isVisible; }

    /** Returns if the widget is blocked. This will mean no mouse interaction is possible. */
    public boolean          isBlocked()     { return _isBlocked; }
    
    /** Returns if the mouse is contained within the widget*/
    public boolean          containsMouse() {
        return Input.mouse().isBoundWithinBox(_pos, _size);
    }
    
    /** Return the draw operation that occurs before the call to 'render' */
    public DrawOperation    drawBefore()    { return _drawBefore; }
    
    /** Return the draw operation that occurs after the call to 'render' */
    public DrawOperation    drawAfter()     { return _drawAfter; }

    /** Returns the widget's drawing context */
    public Draw             draw()          { return _drawCanvas; }
    
    /** Returns if the widget has been flagged for rendering */
    private boolean flaggedForRendering()   { 
        if (_flaggedForRendering) {
            _flaggedForRendering = false;
            return true;
        }
        return false;
    }

    ////////////////////////////////////////////////////////////////
    // ------------------------- SETTERS ------------------------ //
    ////////////////////////////////////////////////////////////////
    
    /** Sets widget position to new vector. Should return the widget. */
    public Widget setPosition(ConstIntVector2D pos) {
        return addPosition(_pos.sub(pos));
    }
    
    /** Modifies position by the given difference vector. Updates all child positions as well. */
    public Widget addPosition(ConstIntVector2D diff) {
        _pos.i_add(diff);
        for (Widget w: _childWidgets) {
            w.addPosition(diff);
        }
        return this;
    }
    
    /** Sets widget position to new vector. Should return the widget. */
    protected Widget setRelativePosition(ConstIntVector2D pos) { _posRelative = pos.copy(); return this; }

    /** Sets widget size to new vector. Should return the widget. */
    public Widget setSize(ConstIntVector2D size)            { _size = size.copy();          return this; }

    /** Sets widget fill color to new color. Should return the widget. */
    public Widget setFillColor(Color fillColor)             { _fillColor = fillColor;       return this; }

    /** Sets widget stroke color to new color. Should return the widget. */
    public Widget setStrokeColor(Color strokeColor)         { _strokeColor = strokeColor;   return this; }

    /** Enables or disables widget. Should return the widget. */
    public Widget setIfEnabled(boolean isEnabled)           { _isEnabled = isEnabled;       return this; }
    /** Sets enabled to true. Cannot override. Should return the widget. */
    public final Widget enable()                            { return setIfEnabled(true); }
    /** Sets enabled to false. Cannot override. Should return the widget. */
    public final Widget disable()                           { return setIfEnabled(false); }

    /** Sets widget visible or invisible. Should return the widget. */
    public Widget setIfVisible(boolean isVisible)           { _isVisible = isVisible;       return this; }
    /** Sets visiblity to true. Cannot override. Should return the widget. */
    public final Widget makeVisible()                       { return setIfVisible(true); }
    /** Sets visiblity to false. Cannot override. Should return the widget. */
    public final Widget makeInvisible()                     { return setIfVisible(false); }

    /** Set draw operation to do before the call to renderWidget() */
    public Widget setDrawBefore(DrawOperation drawBefore)   { _drawBefore = drawBefore;     return this; }
    
    /** Set draw operation to do after the call to renderWidget() */
    public Widget setDrawAfter(DrawOperation drawAfter)     { _drawAfter = drawAfter;       return this; }

    
    /** Blocks widget for one frame. Cannot override. */
    public final void blockWidget()                         { _isBlocked = true; }
    
    /** Flags the widget for rendering on a call to draw() */
    private void flagForRendering()                         { _flaggedForRendering = true; }


    ////////////////////////////////////////////////////////////////
    // ---------------------- CONSTRUCTORS ---------------------- //
    ////////////////////////////////////////////////////////////////

    public Widget() {
        this(new IntVector2D(0, 0), new IntVector2D(10, 10));
    }
    
    public Widget(ConstIntVector2D pos, ConstIntVector2D size) {
        _pos            = pos.copy();
        _posRelative    = pos.copy();
        _size           = size.copy();
        
        _fillColor      = DEFAULT_FILL_COLOR;
        _strokeColor    = DEFAULT_STROKE_COLOR;

        _isHovering     = false;
        _isClicking     = false;
        _isClicked      = false;
        _isEnabled      = true;
        _isVisible      = true;
        _isBlocked      = false;
        
        _drawCanvas    	= new Draw(_size);
        _drawCanvas.setExpandCanvas(true);
        
        _childWidgets   = new ArrayList<>();
        
        _flaggedForRendering = true;
    }

    ////////////////////////////////////////////////////////////////
    // ------------------------ OVERRIDES ----------------------- //
    ////////////////////////////////////////////////////////////////



    ////////////////////////////////////////////////////////////////
    // ------------------------- METHODS ------------------------ //
    ////////////////////////////////////////////////////////////////
    
    /** Draws another widget onto this widget's drawing canvas. This should be used when rendering other widgets onto yourself */
    protected final void drawAnotherWidget(Widget other) {
        if (other.isVisible()) {
            _drawCanvas.drawOthercontext(other.draw(), other.posRelative());
        }
    }
    
    /**
     * Registers widget as a child of this widget. This is necessary for the chaining process of updates and rendering.
     * If your widget creates and stores widgets inside it, they need to be registered to the parent. The position of the registered
     * widget will be offset by the position of this widget, but internally the relative position will not change unless explicitly stated
     * @param widget    New child widget to register
     */
    protected void registerWidget(Widget widget) {
        _childWidgets.add(widget);
        widget.addPosition(_pos);
    }
    
    /** 
     * Updates the widget.
     * @return boolean Returns whether the widget should be flagged for rendering
     */
    protected abstract boolean  updateWidget();
    /** Renders the widget and all of its children. When other rendering widgets to this widget, use drawAnotherWidget() */
    protected abstract void     renderWidget();
    
    /** 
     * Updates all child widgets and this widget. If any child has been updated, it flags for rendering a new canvas.
     * If it has updated, it flags for rendering as well. If disabled, this widget won't 
     */
    public final boolean update() {
        if (!_isEnabled) {
            return false;
        }
        
        checkMouse();
        
        for(Widget w: _childWidgets) {
            if (w.updateWidget()) {
                flagForRendering();
            }
        }
        if (updateWidget()) {
            flagForRendering();
        }
        
        return _flaggedForRendering;
    }
    
    public final void render() {
    	if (_isVisible) {
    		return;
    	}
    	
        for(Widget w: _childWidgets) {
            if (w.flaggedForRendering()) {
                w.render();
            }
        }
        if (_flaggedForRendering) {
            renderWidget();
            _flaggedForRendering = false;
        }
    }
    
    protected final void checkMouse() {
        _isClicked = false;
        
        if (_isBlocked) {
            // If blocked, disable everything for this iteration
            _isBlocked  = false;
            _isHovering = false;
            _isClicking = false;
        } else if (!containsMouse()) {
            // If mouse isn't in the box, disable states
            _isHovering = false;
            _isClicking = false;
        } else if (!Input.mousePressed()) {
            if (_isClicking) {
                // If mouse isn't pressed but was pressed last frame, consider the widget 'clicked'
                _isHovering = false;
                _isClicking = false;
                _isClicked  = true;
            } else {
                // Otherwise, mouse is just hovering over this widget
                _isHovering = true;
                _isClicking = false;
            }
        } else if (_isHovering && Input.mousePressed()) {
            // If mouse was hovering last frame and mouse is pressed, then the widget is currently being clicked on
            _isHovering = false;
            _isClicking = true;
        }
    }

}
