package simple.gui;

import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;

import simple.gui.data.*;

/** This is a static class which allows for a simple interface with the mouse and keyboard. **/
public class Input {
    static class Listener implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
        public void keyPressed(KeyEvent e) {
            _KEY_CHARS[e.getKeyChar()] = true;
            _KEY_CODES[e.getKeyCode()] = true;
        }

        public void keyReleased(KeyEvent e) {
            _KEY_CHARS[e.getKeyChar()] = false;
            _KEY_CODES[e.getKeyCode()] = false;
            _CODE_RELEASED_BUFFER.add(e.getKeyCode());
            _CHAR_RELEASED_BUFFER.add(e.getKeyChar());
        }

        public void keyTyped(KeyEvent e) {
            _CODE_BUFFER.add(e.getKeyCode());
            _CHAR_BUFFER.add(e.getKeyChar());
        }

        public void mousePressed(MouseEvent e) { _MOUSE_DOWN = true; }

        public void mouseReleased(MouseEvent e) { _MOUSE_DOWN = false; }

        public void mouseDragged(MouseEvent e) { _NEW_POS.set(e.getX(), e.getY()); }

        public void mouseMoved(MouseEvent e) { _NEW_POS.set(e.getX(), e.getY()); }

        public void mouseWheelMoved(MouseWheelEvent e) { _BUFFERED_NOTCHES = e.getWheelRotation(); }

        public void mouseClicked(MouseEvent e) { _MOUSE_CLICKED_FLAG = true; }

        public void mouseEntered(MouseEvent e) { /* Event is handled with clicking and positions states */ }

        public void mouseExited(MouseEvent e) { /* Event is handled with clicking and positions states */ }
    }

    private static IntVector2D _POS                = new IntVector2D(0, 0);
    private static IntVector2D _OLD_POS            = new IntVector2D(0, 0);
    private static IntVector2D _NEW_POS            = new IntVector2D(0, 0);
    private static IntVector2D _POS_SHIFT          = new IntVector2D(0, 0);
    private static boolean     _MOUSE_DOWN         = false;
    private static boolean     _MOUSE_CLICKED      = false;
    private static boolean     _MOUSE_CLICKED_FLAG = false;
    private static int         _BUFFERED_NOTCHES   = 0;
    private static int         _MOUSE_NOTCHES      = 0;

    private static boolean[] _KEY_CODES = new boolean[65536];
    private static boolean[] _KEY_CHARS = new boolean[65536];

    private static Queue<Integer>   _CODE_BUFFER           = new LinkedList<>();
    private static Queue<Character> _CHAR_BUFFER           = new LinkedList<>();
    private static Queue<Integer>   _CODE_RELEASED_BUFFER  = new LinkedList<>();
    private static Queue<Character> _CHAR_RELEASED_BUFFER  = new LinkedList<>();
    private static char             _CURRENT_CHAR          = 0;
    private static int              _CURRENT_CODE          = 0;
    private static char             _CURRENT_RELEASED_CHAR = 0;
    private static int              _CURRENT_RELEASED_CODE = 0;

    /** Returns whether the mouse button is pressed down or not **/
    public static boolean mousePressed() { return _MOUSE_DOWN; }

    /** Returns whether the mouse button has been clicked */
    public static boolean mouseClicked() { return _MOUSE_CLICKED; }

    /** Returns the x coordinate of the mouse from the previous frame **/
    public static int mouseOldX() { return _OLD_POS.x(); }

    /** Returns the y coordinate of the mouse from the previous frame **/
    public static int mouseOldY() { return _OLD_POS.y(); }

    /** Returns the x coordinate of the mouse from the current frame **/
    public static int mouseX() { return _NEW_POS.x(); }

    /** Returns the y coordinate of the mouse from the current frame **/
    public static int mouseY() { return _NEW_POS.y(); }

    /** Returns the x coordinate of the mouse from the current frame **/
    public static int mouseShiftX() { return _POS.sub(_OLD_POS).x(); }

    /** Returns the y coordinate of the mouse from the current frame **/
    public static int mouseShiftY() { return _POS.sub(_OLD_POS).y(); }

    /** Returns the mouse position */
    public static ConstIntVector2D mouse() { return _POS; }

    /** Returns the mouse position of the previous frame */
    public static ConstIntVector2D mouseOld() { return _OLD_POS; }

    /** Returns the a vector of the shift between the mouse positions of this frame and last frame */
    public static ConstIntVector2D mouseShift() { return _POS_SHIFT; }

    /** Returns the number of notches the mouse wheel changes by. A negative value is wheeldown, positive is wheelup */
    public static int mouseWheelNotches() { return _MOUSE_NOTCHES; }

    /** Returns if the mouse wheel has rotated upwards */
    public static boolean mouseWheelUp() { return _MOUSE_NOTCHES < 0; }

    /** Returns if the mouse wheel has rotated downwards */
    public static boolean mouseWheelDown() { return _MOUSE_NOTCHES > 0; }


    /** Returns whether the given key is pressed, using given keycode **/
    public static boolean keyDown(int key) { return (key >= 0 && key < 65536) && (_KEY_CODES[key]); }

    /** Returns whether the given key is pressed, using given character **/
    public static boolean keyDown(char key) { return (key >= 0 && key < 65536) && (_KEY_CHARS[key]); }

    /** Returns the keycode of the first key in the typing buffer **/
    public static int getCode() { return _CURRENT_CODE; }

    /** Returns the character of the first key in the typing buffer **/
    public static char getChar() { return _CURRENT_CHAR; }

    /** Returns the keycode of the first key in the release buffer **/
    public static int releasedCode() { return _CURRENT_RELEASED_CODE; }

    /** Returns the character of the first key in the release buffer **/
    public static char releasedChar() { return _CURRENT_RELEASED_CHAR; }

    /** Updates the internal state of Input. This is used internally in SimpleGUIApp, and should not be called anywhere
     * else **/
    public static void update() {
        _OLD_POS   = _POS;
        _POS       = _NEW_POS;
        _POS_SHIFT = _POS.sub(_OLD_POS);

        if (_MOUSE_CLICKED_FLAG) {
            _MOUSE_CLICKED      = true;
            _MOUSE_CLICKED_FLAG = false;
        } else {
            _MOUSE_CLICKED = false;
        }

        _MOUSE_NOTCHES    = _BUFFERED_NOTCHES;
        _BUFFERED_NOTCHES = 0;

        if (_CODE_BUFFER.peek() != null) {
            _CURRENT_CODE = _CODE_BUFFER.poll();
        } else {
            _CURRENT_CODE = 0;
        }

        if (_CHAR_BUFFER.peek() != null) {
            _CURRENT_CHAR = _CHAR_BUFFER.poll();
        } else {
            _CURRENT_CHAR = 0;
        }

        if (_CODE_RELEASED_BUFFER.peek() != null) {
            _CURRENT_RELEASED_CODE = _CODE_RELEASED_BUFFER.poll();
        } else {
            _CURRENT_RELEASED_CODE = 0;
        }

        if (_CHAR_RELEASED_BUFFER.peek() != null) {
            _CURRENT_RELEASED_CHAR = _CHAR_RELEASED_BUFFER.poll();
        } else {
            _CURRENT_RELEASED_CHAR = 0;
        }
    }

    private static Listener listener = new Listener();

    /** Returns an object that uses the KeyListener, MouseListener, MouseMotionListener and MouseWheelListener
     * interfaces. Used internally for SimpleGUIApp **/
    public static Listener getListener() { return listener; }
}
