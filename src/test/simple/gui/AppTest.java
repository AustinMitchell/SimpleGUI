package test.simple.gui;

import java.awt.Color;

import simple.gui.Input;
import simple.gui.SimpleGUIApp;
import simple.gui.data.IntVector2D;

@SuppressWarnings("serial")
public class AppTest extends SimpleGUIApp {
    public static void main(String[] args) {
        SimpleGUIApp.start(new AppTest(), "App Test");
    }
    
    public AppTest() {
        super(500, 500, 30);
    }
    
    IntVector2D control;
    
    @Override
    public void setup() {
        control = new IntVector2D(200, 200);
    }
    
    @Override
    public void loop() {
        draw().setStroke(Color.BLACK);
        draw().ovalCentered(control.asConst(), new IntVector2D(Input.mouse().approxdist(control)));        
    }
}
