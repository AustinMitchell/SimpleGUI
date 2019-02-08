package simple.gui;

import javax.swing.*;

import simple.gui.data.*;
import simple.gui.graphics.Draw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;

@SuppressWarnings("serial")
public abstract class SimpleGUIApp extends JPanel implements Runnable {

    ////////////////////////////////////////////////////////////////
    // ------------------------- STATICS ------------------------ //
    ////////////////////////////////////////////////////////////////

    private static JFrame makeNewFrame(SimpleGUIApp programToRun, String title) {
        return makeNewFrame(programToRun, title, false);
    }
    private static JFrame makeNewFrame(SimpleGUIApp programToRun, String title, boolean isUndecorated) {
        JFrame newFrame = new JFrame(title);
        newFrame.setContentPane(programToRun);
        newFrame.setUndecorated(isUndecorated);
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setResizable(false);
        newFrame.pack();
        newFrame.setVisible(true);
        return newFrame;
    }

    /** Maximum width the window can be for you screen size **/
    public static final int MAXWIDTH = (int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    /** Maximum height the window can be for your sceen size **/
    public static final int MAXHEIGHT = (int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    public static void start(SimpleGUIApp mainProgram, String name, boolean isUndecorated) {
        mainProgram.setFrame(makeNewFrame(mainProgram, name, isUndecorated)); 
    }
    public static void start(SimpleGUIApp mainProgram, String name) {
        mainProgram.setFrame(makeNewFrame(mainProgram, name)); 
    }

    ////////////////////////////////////////////////////////////////
    // ------------------------- FIELDS ------------------------- //
    ////////////////////////////////////////////////////////////////

    private Color       _backgroundColor;
    private boolean     _isRunning;
    private IntVector2D _dimensions;
    private int         _fps;
    private int         _delayTime;
    private JFrame      _frame;
    private Draw        _draw;

    ////////////////////////////////////////////////////////////////
    // ----------------------- PROPERTIES ----------------------- //
    ////////////////////////////////////////////////////////////////

    /** Returns the width of the window frame **/
    public int              width()             { return _dimensions.x(); }

    /** Returns the height of the window frame **/
    public int              height()            { return _dimensions.y(); }

    /** Returns the dimensions of the window frame */
    public ConstIntVector2D dimensions()        { return _dimensions; }

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

    ////////////////////////////////////////////////////////////////
    // ------------------------- SETTERS ------------------------ //
    ////////////////////////////////////////////////////////////////

    private void setFrame(JFrame frame) { _frame = frame; }

    /** Sets the background color to the given color **/
    public final void setBackgroundColor(Color c) { _backgroundColor = c; }

    /** Signals the program to terminate **/
    public final void quit() { _isRunning = false; }

    /** Minimizes the window into the explorer bar **/
    public final void minimize() { _frame.setState(Frame.ICONIFIED); }


    ////////////////////////////////////////////////////////////////
    // ---------------------- CONSTRUCTORS ---------------------- //
    ////////////////////////////////////////////////////////////////

    /** Creates a new SimpleGUIApp with a given width, height and target frames per second **/
    public SimpleGUIApp(int width, int height, int fps) {
        super();
        _backgroundColor = new Color(180, 180, 180);
        _isRunning = false;
        _dimensions = new IntVector2D(width, height);
        _fps = fps;
        _delayTime = 1000/fps;
        _frame = (JFrame) SwingUtilities.getWindowAncestor(this);

        addMouseListener(Input.getListener());
        addMouseMotionListener(Input.getListener());
        addMouseWheelListener(Input.getListener());
        addKeyListener(Input.getListener());

        setPreferredSize(new Dimension(width, height));
        setFocusable(true);
        requestFocus();
    }

    ////////////////////////////////////////////////////////////////
    // ------------------------ OVERRIDES ----------------------- //
    ////////////////////////////////////////////////////////////////

    /** Method required for the Runnable Interface; Contains the program loop **/
    @Override
    public final void run() {
        initGraphics();
        setup();
        while(_isRunning) {
            Input.update();
            loop();
            updateView();
        }
        System.exit(0);
    }

    /** Method required for the Runnable interface. **/
    @Override
    public final void addNotify() {
        super.addNotify();
        Thread thread = new Thread(this);
        addMouseListener(Input.getListener());
        addMouseMotionListener(Input.getListener());
        addMouseWheelListener(Input.getListener());
        addKeyListener(Input.getListener());
        thread.start();
    }

    ////////////////////////////////////////////////////////////////
    // ------------------------- METHODS ------------------------ //
    ////////////////////////////////////////////////////////////////

    private void initGraphics() {
        _draw = new Draw(_dimensions);
        _isRunning = true;
    }

    /** Method called before the loop begins; Set up variables here, not in your contructor **/
    public abstract void setup();
    /** Method called each frame **/
    public abstract void loop();

    /** Draws whatever is on the DrawModule image buffer to the program window **/
    private void drawToScreen() {
        Graphics graphics = super.getGraphics();
        graphics.drawImage(_draw.canvas().bufferedImage(), 0, 0, null);
        graphics.dispose();
    }
    /** Covers the DrawModule image buffer with the background color, effectively clearing it **/
    private void cls() {
        _draw.setFill(_backgroundColor);
        _draw.setStroke(null);
        _draw.rect(Draw.ORIGIN, _dimensions);
    }
    /** Calls DrawToScreen(), Timer.correctedDelay() with your target FPS in mind and cls(). This is the most convenient way to update the screen **/
    private void updateView() {
        drawToScreen();
        Timer.correctedDelay(_delayTime);
        cls();
    }
}
