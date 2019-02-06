package simple.gui.graphics;

/** Interface for injecting customized drawing operations */
public interface DrawOperation {
    /** Performs some operation onto a draw context */
    public void draw(Draw drawContext);
}
