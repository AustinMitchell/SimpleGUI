package simple.gui.graphics.widget;

import java.awt.Color;
import java.util.EnumMap;

import simple.gui.data.ConstIntVector2D;
import simple.gui.data.IntVector2D;
import simple.gui.graphics.Draw;

import java.util.Map;

public class Button extends Widget {

    ////////////////////////////////////////////////////////////////
    // ------------------------- STATICS ------------------------ //
    ////////////////////////////////////////////////////////////////

    public enum State { DISABLED, NONE, HOVERING, CLICKING }

    private static final Map<State, Color> LIGHT_PALETTE = Map.of(
            State.DISABLED, new Color(0, 0, 0, 60),
            State.NONE,     new Color(0, 0, 0, 40),
            State.HOVERING, new Color(0, 0, 0, 20),
            State.CLICKING, new Color(0, 0, 0, 0));

    private static final Map<State, Color> DARK_PALETTE = Map.of(
            State.DISABLED, new Color(255, 255, 255, 0),
            State.NONE,     new Color(255, 255, 255, 20),
            State.HOVERING, new Color(255, 255, 255, 40),
            State.CLICKING, new Color(255, 255, 255, 60));

    ////////////////////////////////////////////////////////////////
    // ------------------------- FIELDS ------------------------- //
    ////////////////////////////////////////////////////////////////

    private State                  _state;
    private Color                  _maskColor;
    private Map<State, Color>  _stateColors;

    ////////////////////////////////////////////////////////////////
    // ----------------------- PROPERTIES ----------------------- //
    ////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////
    // ------------------------- SETTERS ------------------------ //
    ////////////////////////////////////////////////////////////////


    @Override
    public Widget setFillColor(Color fillColor) {
        super.setFillColor(fillColor);
        return this;
    }

    private void setState(State state) {
        _state = state;
        _maskColor = _stateColors.get(state);
    }

    /** Sets the button to expect a lighter fill color so it can show different states better */
    public void expectLightPalette() {
        _stateColors = LIGHT_PALETTE;
        setState(_state);
    }

    /** Sets the button to expect a darker fill color so it can show different states better */
    public void expectDarkPalette() {
        _stateColors = DARK_PALETTE;
        setState(_state);
    }

    ////////////////////////////////////////////////////////////////
    // ---------------------- CONSTRUCTORS ---------------------- //
    ////////////////////////////////////////////////////////////////

    public Button() {
        this(new IntVector2D(0, 0), new IntVector2D(10, 10));
    }

    public Button(ConstIntVector2D pos, ConstIntVector2D size) {
        super(pos, size);

        _stateColors = LIGHT_PALETTE;

        setState(State.NONE);
    }


    ////////////////////////////////////////////////////////////////
    // ------------------------ OVERRIDES ----------------------- //
    ////////////////////////////////////////////////////////////////

    @Override
    protected boolean updateWidget() {
        State oldState = _state;

        if (!isEnabled()) {
            setState(State.DISABLED);
        } else if (isHovering()) {
            setState(State.HOVERING);
        } else if (isClicking()) {
            setState(State.CLICKING);
        } else {
            setState(State.NONE);
        }

        return oldState != _state;
    }

    @Override
    protected void renderWidget() {
        draw().setStroke(null);
        draw().setFill(_fillColor);
        draw().rect(Draw.ORIGIN, _size);

        draw().setFill(_maskColor);
        draw().rect(Draw.ORIGIN, _size);

        draw().setFill(null);
        draw().setStroke(_strokeColor, 2);
        draw().rect(Draw.ORIGIN, _size);
    }


    ////////////////////////////////////////////////////////////////
    // ------------------------- METHODS ------------------------ //
    ////////////////////////////////////////////////////////////////


}
