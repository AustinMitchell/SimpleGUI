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
        super(500, 500, 144);
    }

    IntVector2D control;
    IntVector2D radius;

    @Override
    public void setup() {
        control = new IntVector2D(200, 200);
        radius = new IntVector2D(0);
    }

    @Override
    public void loop() {
        radius.setX((int)Input.mouse().dist(control));
        radius.setY(radius.x());

        draw().setStroke(Color.BLACK);
        draw().ovalCentered(control, radius);        
    }
}
