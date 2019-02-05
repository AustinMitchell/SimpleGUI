package simple.gui;

import javax.swing.*;

import simple.gui.Draw;
import simple.gui.data.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;

@SuppressWarnings("serial")
public abstract class SimpleGUIApp extends JPanel implements Runnable {
    private static class GUIRunWindow extends JFrame{
        public GUIRunWindow(SimpleGUIApp programToRun, String title, boolean isUndecorated) {
            super(title);
            setContentPane(programToRun);
            setUndecorated(isUndecorated);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setResizable(false);
            pack();
            setVisible(true);
        }
        @SuppressWarnings("unused")
        public GUIRunWindow(SimpleGUIApp programToRun, String title) {
            super(title);
            setContentPane(programToRun);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setResizable(false);
            pack();
            setVisible(true);
        }
    }
    
    /** Maximum width the window can be for you screen size **/
    public static final int MAXWIDTH = (int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    /** Maximum height the window can be for your sceen size **/
    public static final int MAXHEIGHT = (int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    
    public static void start(SimpleGUIApp mainProgram, String name, boolean isUndecorated) {
        mainProgram.setFrame(new GUIRunWindow(mainProgram, name, isUndecorated)); 
    }
    public static void start(SimpleGUIApp mainProgram, String name) {
        mainProgram.setFrame(new GUIRunWindow(mainProgram, name, false)); 
    }
    
    private Color       _backgroundColor;
    private boolean     _isRunning;
    private IntVector2D _dimensions;
    private int         _fps;
    private int         _delayTime;
    private JFrame      _frame;
    private Thread      _thread;
    private Draw        _draw;
    private Graphics    _graphics;
        
    /** Returns the width of the window frame **/
    public int              width()             { return _dimensions.x(); }

    /** Returns the height of the window frame **/
    public int              height()            { return _dimensions.y(); }

    /** Returns the dimensions of the window frame */
    public ConstIntVector2D dimensions()        { return _dimensions.asConst(); }

    /** Returns the target frames per second for the program **/
    public int              fps()               { return _fps; }

    /** Returns the target delay time for the program given the FPS **/
    public int              delay()             { return _delayTime; }

    /** Returns the current background color (color that each frame starts as) **/
    public Color            backgroundColor()   { return _backgroundColor; }

    /** Returns the JFrame object for the window **/
    public JFrame           jframe()            { return _frame; }

    /** Returns the drawing context for the window */
    public Draw             draw()              { return _draw; }




    private void setFrame(JFrame frame_) { _frame = frame_; }
    
    /** Sets the background color to the given color **/
    public void setBackgroundColor(Color c) { _backgroundColor = c; }
    
    /** Signals the program to terminate **/
    public void quit() { _isRunning = false; }

    /** Minimizes the window into the explorer bar **/
    public void minimize() { _frame.setState(Frame.ICONIFIED); }
    
    /** Creates a new SimpleGUIApp with a given width, height and target frames per second **/
    public SimpleGUIApp(int width, int height, int fps) {
        super();
        _backgroundColor = new Color(180, 180, 180);
        _isRunning = false;
        _dimensions = new IntVector2D(width, height);
        _fps = fps;
        _delayTime = 1000/fps;
        _frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        _graphics = super.getGraphics();
        
        setPreferredSize(new Dimension(width, height));
        setFocusable(true);
        requestFocus();
    }
    
    private void initGraphics() {
        _draw = new Draw(_dimensions);
        _isRunning = true;
    }
    
    /** Method required for the Runnable Interface; Contains the program loop **/
    public void run() {
        initGraphics();
        setup();
        while(_isRunning) {
            Input.update();
            loop();
        }
        System.exit(0);
    }
    
    /** Method called before the loop begins; Set up variables here, not in your contructor **/
    public abstract void setup();
    /** Method called each frame **/
    public abstract void loop();
    
    /** Draws whatever is on the DrawModule image buffer to the program window **/
    protected void DrawToScreen() {
        _graphics.drawImage(_draw.canvas().bufferedImage(), 0, 0, null);
    }
    /** Covers the DrawModule image buffer with the background color, effectively clearing it **/
    protected void cls() {
        _draw.setFill(_backgroundColor);
        _draw.setStroke(null);
        _draw.rect(Draw.ORIGIN, _dimensions);
    }
    /** Calls DrawToScreen(), Timer.correctedDelay() with your target FPS in mind and cls(). This is the most convenient way to update the screen **/
    protected void updateView() {
        DrawToScreen();
        Timer.correctedDelay(_delayTime);
        cls();
    }
    
    /** Method required for the Runnable interface. **/
    public void addNotify() {
        super.addNotify();
        if(_thread == null) {
            _thread = new Thread(this);
            addMouseListener(Input.getListener());
            addMouseMotionListener(Input.getListener());
            addMouseWheelListener(Input.getListener());
            addKeyListener(Input.getListener());
            _thread.start();
        }
    }
}
