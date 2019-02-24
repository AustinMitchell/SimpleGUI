package simple.gui.graphics.widget;

import java.awt.Color;
import java.util.List;

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

    private List<String> _textDisplay;
    private String       _baseText;
    private Alignment    _alignment;
    private Color        _textColor;
    private boolean      _isActive;
    private boolean      _isEditable;


    ////////////////////////////////////////////////////////////////
    // ----------------------- PROPERTIES ----------------------- //
    ////////////////////////////////////////////////////////////////

    /** Returns the string this text object will render */
    public String text() { return _baseText; }

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

    public TextObject setText(String text) {
        // TODO: Complete when the other shits done
        return this;
    }



    ////////////////////////////////////////////////////////////////
    // ---------------------- CONSTRUCTORS ---------------------- //
    ////////////////////////////////////////////////////////////////

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
    /* ====================== SUBSECTION ====================== */
}
