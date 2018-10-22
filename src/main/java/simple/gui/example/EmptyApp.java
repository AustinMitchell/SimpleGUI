package simple.gui.example;

import simple.gui.SimpleGUIApp;

@SuppressWarnings("serial")
public class EmptyApp extends SimpleGUIApp {
    public static void main(String[] args) { EmptyApp.start(new EmptyApp(), "Empty App"); }
    public EmptyApp() { super(500, 500, 30); }

    @Override
    public void setup() {

    }

    @Override
    public void loop() {

    } 
}