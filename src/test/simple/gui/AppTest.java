package test.simple.gui;

import java.awt.Color;

import simple.gui.SimpleGUIApp;
import simple.gui.data.IntVector2D;
import simple.gui.graphics.widget.Button;

@SuppressWarnings("serial")
public class AppTest extends SimpleGUIApp {
    public static void main(String[] args) {
        SimpleGUIApp.start(new AppTest(), "App Test");
    }

    public AppTest() {
        super(500, 500, 60);
    }
    
    Button button;
    int clickcount;
    
    @Override
    public void setup() {
        button = new Button(new IntVector2D(200, 200), new IntVector2D(200, 100));
        button.setFillColor(Color.black);
        button.expectDarkPalette();
        clickcount = 0;
    }

    @Override
    public void loop() {
        button.update();
        
        draw().setStroke(Color.black);
        if (button.isHovering()) {
            draw().text("Hovering", 0, 0);
        } else if (button.isClicking()) {
            draw().text("clicking", 0, 0);
        } else if (button.isClicked()) {
        	clickcount++;
        } else {
            draw().text("None", 0, 0);
        }
        
        draw().text(""+clickcount, 0, 12);
        
        drawWidget(button);
    }
}
