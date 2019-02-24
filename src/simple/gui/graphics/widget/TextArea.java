package simple.gui.graphics.widget;

import java.util.List;

public class TextArea extends Widget {

    ////////////////////////////////////////////////////////////////
    // ------------------------- STATICS ------------------------ //
    ////////////////////////////////////////////////////////////////

    public static enum Alignment {
        NORTHWEST, NORTH, NORTHEAST, WEST, CENTER, EAST, SOUTHWEST, SOUTH, SOUTHEAST;
        public boolean isTopAligned() { return this == NORTH || this == NORTHWEST || this == NORTHEAST; }

        public boolean isBottomAligned() { return this == SOUTH || this == SOUTHWEST || this == SOUTHEAST; }

        public boolean isLeftAligned() { return this == WEST || this == NORTHWEST || this == SOUTHWEST; }

        public boolean isRightAligned() { return this == EAST || this == NORTHEAST || this == SOUTHEAST; }

        public boolean centeredYAxis() { return this == NORTH || this == CENTER || this == SOUTH; }

        public boolean centeredXAxis() { return this == WEST || this == CENTER || this == EAST; }
    }


    ////////////////////////////////////////////////////////////////
    // ------------------------- FIELDS ------------------------- //
    ////////////////////////////////////////////////////////////////

    private List<String> _textDisplay;
    private String       _baseText;


    ////////////////////////////////////////////////////////////////
    // ----------------------- PROPERTIES ----------------------- //
    ////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////
    // ------------------------- SETTERS ------------------------ //
    ////////////////////////////////////////////////////////////////

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
