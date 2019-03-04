package simple.gui.graphics.widget;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import simple.gui.data.ConstIntVector2D;
import simple.gui.data.IntVector2D;
import simple.gui.graphics.Draw.SimpleFont;

public abstract class TextObject extends Widget {

    ////////////////////////////////////////////////////////////////
    // ------------------------- STATICS ------------------------ //
    ////////////////////////////////////////////////////////////////

    public enum Alignment {
        NORTHWEST, NORTH, NORTHEAST, WEST, CENTER, EAST, SOUTHWEST, SOUTH, SOUTHEAST;
        public boolean isTopAligned() { return (this == NORTH) || (this == NORTHWEST) || (this == NORTHEAST); }

        public boolean isBottomAligned() { return (this == SOUTH) || (this == SOUTHWEST) || (this == SOUTHEAST); }

        public boolean isLeftAligned() { return (this == WEST) || (this == NORTHWEST) || (this == SOUTHWEST); }

        public boolean isRightAligned() { return (this == EAST) || (this == NORTHEAST) || (this == SOUTHEAST); }

        public boolean centeredYAxis() { return (this == NORTH) || (this == CENTER) || (this == SOUTH); }

        public boolean centeredXAxis() { return (this == WEST) || (this == CENTER) || (this == EAST); }
    }


    ////////////////////////////////////////////////////////////////
    // ------------------------- FIELDS ------------------------- //
    ////////////////////////////////////////////////////////////////

    private String[]	_textDisplay;
    private String      _baseText;
    private int			_lineSpacing;
    private int			_borderSpacing;
    private Alignment	_alignment;
    private Color       _textColor;
    private boolean     _isActive;
    private boolean     _isEditable;


    ////////////////////////////////////////////////////////////////
    // ----------------------- PROPERTIES ----------------------- //
    ////////////////////////////////////////////////////////////////

    /** Returns the string this text object will render */
    public String text() { return _baseText; }
    
    /** Returns the amount of space between the lines */
    public int lineSpacing() { return _lineSpacing; }
    
    /** Returns the amount of space between the text and widget borders */
    public int borderSpacing() { return _borderSpacing; }

    /** Returns the alignment of this text object */
    public Alignment alignment() { return _alignment; }

    /** Returns whether this text object is currently active (i.e. accepting user input) */
    public boolean isActive() { return _isActive; }

    /** Returns whether this text object is editable by the user */
    public boolean isEditable() { return _isEditable; }

    /** Returns the color used to render the text */
    public Color textColor() { return _textColor; }

    /** returns the font object used to render the text */
    public SimpleFont font() { return draw().font(); }


    ////////////////////////////////////////////////////////////////
    // ------------------------- SETTERS ------------------------ //
    ////////////////////////////////////////////////////////////////

    /** Enables or disables text object, then returns itself. */
    @Override
    public TextObject setIfEnabled(boolean isEnabled) {
        super.setIfEnabled(isEnabled);
        _isActive = isEnabled;
        return this;
    }

    /** Activates or deactivates the text object, then returns itself. */
    public TextObject setIfActive(boolean isActive) {
        _isActive = isActive;
        return this;
    }

    /** Set the string to render on the text object, then return itself */
    public TextObject setText(String text) {
        _baseText = text;
        formatText();
        return this;
    }
    
    /** Set the spacing between lines of text, then return itself */
    public TextObject setLineSpacing(int lineSpacing) {
    	_lineSpacing = lineSpacing;
    	return this;
    }
    
    /** Set the spacing between the widget borders and the text, then return itself */
    public TextObject setBorderSpacing(int borderSpacing) {
    	_borderSpacing = borderSpacing;
    	return this;
    }

    /** Set the font to render the text with */
    public TextObject setFont(SimpleFont font) {
    	draw().setFont(font);
    	return this;
    }


    ////////////////////////////////////////////////////////////////
    // ---------------------- CONSTRUCTORS ---------------------- //
    ////////////////////////////////////////////////////////////////

    public TextObject() {
    	this("");
    }
    
    public TextObject(String initialText) {
    	this(new IntVector2D(0, 0), new IntVector2D(10, 10), initialText);
    }
    
    public TextObject(ConstIntVector2D pos, ConstIntVector2D size) {
    	this(pos, size, "");
    }
    
    public TextObject(ConstIntVector2D pos, ConstIntVector2D size, String initialText) {
    	super(pos, size);
    	
        _alignment		= Alignment.NORTHWEST;
        _textColor		= Color.BLACK;
        _isActive		= false;
        _isEditable		= false;
        
        setText(initialText);
    }
    
    ////////////////////////////////////////////////////////////////
    // ------------------------ OVERRIDES ----------------------- //
    ////////////////////////////////////////////////////////////////

    @Override
    protected boolean updateWidget() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected void renderWidget() {
        // TODO Auto-generated method stub

    }


    ////////////////////////////////////////////////////////////////
    // ------------------------- METHODS ------------------------ //
    ////////////////////////////////////////////////////////////////

    /* ========================================================== */
    /* ==================== TEXT FORMATTING ===================== */
    
    // Calculates how many lines we can have in our display
    private int calculateDisplayLines() {
    	return (size().y() / (draw().fontMetrics().ascent()+_lineSpacing));
    }
    
    /** Calculates the maximum width of a line */
    protected int maxLineWidth() {
    	return size().x() - _borderSpacing;
    }
    
    // Formats text into the display, and reformats display if necessary
    private void formatText() {
    	int numLines = calculateDisplayLines();
    	if (_textDisplay == null || numLines != _textDisplay.length) {
    		_textDisplay = new String[numLines];
    	}
    	
    	
    }
    
    private int findFittingEndIndex(String str, int numLines, int maxIndex) {
    	// TODO: binary search shit
    	return 0;
    }
}
