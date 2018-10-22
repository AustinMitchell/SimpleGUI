package simple.gui;

import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;

import simple.gui.Vector2D;

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
        public void mouseDragged(MouseEvent e) { _NEW_POS.x = e.getX(); _NEW_POS.y = e.getY(); }
        public void mouseMoved(MouseEvent e) { _NEW_POS.x = e.getX(); _NEW_POS.y = e.getY(); }

        public void mouseWheelMoved(MouseWheelEvent e) { _BUFFERED_NOTCHES = e.getWheelRotation(); }
        
        public void mouseClicked(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
    }

    private static Vector2D _POS = new Vector2D(0, 0);
    private static Vector2D _OLD_POS = new Vector2D(0, 0);
    private static Vector2D _NEW_POS = new Vector2D(0, 0);
    private static boolean _MOUSE_DOWN = false;
    private static int _BUFFERED_NOTCHES=0, mouseNotches=0;
    
    private static boolean[] _KEY_CODES = new boolean[65536];
    private static boolean[] _KEY_CHARS = new boolean[65536];
    
    private static Queue<Integer> _CODE_BUFFER = new LinkedList<Integer>();
    private static Queue<Character> _CHAR_BUFFER = new LinkedList<Character>();
    private static Queue<Integer> _CODE_RELEASED_BUFFER = new LinkedList<Integer>();
    private static Queue<Character> _CHAR_RELEASED_BUFFER = new LinkedList<Character>();
    private static char _CURRENT_CHAR = 0;
    private static int _CURRENT_CODE = 0;
    private static char _CURRENT_RELEASED_CHAR = 0;
    private static int _CURRENT_RELEASED_CODE = 0;

    /** Returns whether the mouse button is pressed down or not **/
    public static boolean mousePressed() { return _MOUSE_DOWN; }
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

    public static Vector2D mouse()      { return _POS.copy(); }
    public static Vector2D mouseOld()   { return _OLD_POS.copy(); }
    public static Vector2D mouseShift() { return _POS.sub(_OLD_POS); }
    
    public static int mouseWheelNotches()  { return mouseNotches; }
    public static boolean mouseWheelUp()   { return mouseNotches < 0; }
    public static boolean mouseWheelDown() { return mouseNotches > 0; }
    
    
    /** Returns whether the given key is pressed, using given keycode **/
    public static boolean keyDown(int key) { return (key>=0 && key<65536) ? (_KEY_CODES[key]) : false; }
    /** Returns whether the given key is pressed, using given character**/
    public static boolean keyDown(char key) { return (key>=0 && key<65536) ? (_KEY_CHARS[key]) : false; }
    /** Returns the keycode of the first key in the typing buffer **/
    public static int getCode() { return _CURRENT_CODE; }
    /** Returns the character of the first key in the typing buffer **/
    public static char getChar() { return _CURRENT_CHAR; }
    /** Returns the keycode of the first key in the release buffer **/
    public static int releasedCode() { return _CURRENT_RELEASED_CODE; }
    /** Returns the character of the first key in the release buffer **/
    public static char releasedChar() { return _CURRENT_RELEASED_CHAR; }

    /** Updates the internal state of Input. This is used internally in SimpleGUIApp, and should not be called anywhere else **/
    public static void update() {
        _OLD_POS = _POS;
        _POS = _NEW_POS;
        
        mouseNotches = _BUFFERED_NOTCHES;
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
    /** Returns an object that uses the KeyListener, MouseListener, MouseMotionListener and MouseWheelListener interfaces. Used internally for SimpleGUIApp**/
    public static Listener getListener() { return listener; }
}
