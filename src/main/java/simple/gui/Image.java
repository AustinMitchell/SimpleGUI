package simple.gui;

import simple.gui.data.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

/** Object that works as an overhead for the BufferedImage object. Has its own limited functionality, but if you wish to work directly 
 * with the BufferedImage, you can get this from the Image object. Ths object's funcionality is currently limited as it's meant to have
 * only simple and easy to understand usage. **/
public final class Image {
    public static class ResLoader {
        public static InputStream load(String path) {
            InputStream input = ResLoader.class.getResourceAsStream(path);
            if (input == null) {
                input = ResLoader.class.getResourceAsStream("/"+path);
            }
            return input;
        }
    }
    
    public static enum Orientation {
        UP, RIGHT, DOWN, LEFT;
        public static String toString(Orientation o) {
            switch(o) {
                case UP:
                    return "UP";
                case RIGHT:
                    return "RIGHT";
                case DOWN:
                    return "DOWN";
                case LEFT:
                    return "LEFT";
                default:
                    return "";
            }
        }
        public static int toInt(Orientation o) {
            switch(o) {
                case UP:
                    return 0;
                case RIGHT:
                    return 1;
                case DOWN:
                    return 2;
                case LEFT:
                    return 3;
                default:
                    return 0;
            }
        }
        public static Orientation fromInt(int i) {
            // same as i%4 but faster
            switch(i&3) {
            case 0:
                return UP;
            case 1:
                return RIGHT;
            case 2:
                return DOWN;
            case 3:
                return LEFT;
            default:
                return UP;
            }
        }
            
        public static int[][] getRotationMatrix(Orientation o1, Orientation o2) {
            int[][] matrix = new int[2][2];
            switch(Math.floorMod(o2.ordinal() - o1.ordinal(), 4)) {
                case 0:
                    matrix[0][0]=1; matrix[0][1]=0;
                    matrix[1][0]=0; matrix[1][1]=1;
                    break;
                case 1:
                    matrix[0][0]=0; matrix[0][1]=-1;
                    matrix[1][0]=1; matrix[1][1]=0;
                    break;
                case 2:
                    matrix[0][0]=-1; matrix[0][1]=0;
                    matrix[1][0]=0; matrix[1][1]=-1;
                    break;
                case 3:
                    matrix[0][0]=0; matrix[0][1]=1;
                    matrix[1][0]=-1; matrix[1][1]=0;
                    break;
            }
            return matrix;
        }
    }
    
    private BufferedImage   _image;            
    private String          _filename;                   
    private IntVector2D     _size;
    private Orientation     _orientation;


    //////////////////////////////
    //------- PROPERTIES -------//
    //////////////////////////////

    /** Returns the width of the image */
    public int              width()         { return _size.x(); }

    /** Returns the height of the image */
    public int              height()        { return _size.y(); }

    /** Returns the dimensions of the image */
    public ConstIntVector2D size()          { return _size.asConst(); }

    /** Returns orientation of the image **/
    public Orientation      orientation()   { return _orientation; }

    /** Returns the BufferedImage object from the image. **/
    public BufferedImage    bufferedImage() { return _image; }

    /** Returns a Graphics2D object generated by the backing image */
    public Graphics2D       graphics2D()    { return _image.createGraphics(); }

    /** Returns the filename of the image. If it is an image created without a filename, then a default filename is used:
     *the String (image width) + "-by-" + (image height). **/
    public String           fileName()      { return _filename; }
    

    ///////////////////////////
    //------- SETTERS -------//
    ///////////////////////////


    /** Set the orientation of the image. This does not affect the image, but it will affect the output of reorient() **/
    public void setOrientation(Orientation orientation) { _orientation = orientation; }
    
    /** Sets the image's filename (name stored in the object, not the actual filename on the computer). **/
    public void setFileName(String fileName) { _filename = fileName; }
    

    ////////////////////////////////
    //------- CONSTRUCTORS -------//
    ////////////////////////////////


    /** Creates an empty image with a default filename of the given width and height. 
     * @param width     Width of new image
     * @param height    Height of new image
     */
    public Image(int width, int height) {
        if (width < 0) { throw new IllegalArgumentException("width must be non-negative"); }
        if (height < 0) { throw new IllegalArgumentException("height must be non-negative"); }

        _size        = new IntVector2D(width, height);
        _image       = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); // set to TYPE_INT_ARGB to support transparency
        _filename    = width + "-by-" + height;
        _orientation = Orientation.UP;
    }

    /** Creates an empty image with a default filename with the width and height defined by {dimensions}. 
     * @param dimensions    Dimensions of new image
     * **/
    public Image(ConstIntVector2D dimensions) {
        this(dimensions.x(), dimensions.y());
    }

    /** Creates an image using the same filename and color data from another image. **/
    public Image(Image image) {
        _size        = new IntVector2D(image._size);
        _image       = new BufferedImage(_size.x(), _size.y(), BufferedImage.TYPE_INT_ARGB);
        _filename    = image.fileName();
        _orientation = image.orientation();

        setPixels(image.getPixelsNoCopy());
    }
    
    /** Creates and image with a default filename using the color data from a BufferedImage. 
     * @param image     BufferedImage object
     * @param copyImage If enabled, it will make a new BufferedImage by copying pixel data from
     * the original. Otherwise, it will use the same base object. **/
    public Image(BufferedImage image, boolean copyImage) {
        _size = new IntVector2D(image.getWidth(), image.getHeight()); 
        
        if (copyImage) {
            _image = new BufferedImage(_size.x(), _size.y(), BufferedImage.TYPE_INT_ARGB);
            setPixels(((DataBufferInt)image.getRaster().getDataBuffer()).getData());
        } else {
            _image = image;
        }
        _filename = _size.x() + "-by-" + _size.y();
        _orientation = Orientation.UP;
    }
    
    public Image(InputStream is) {
        BufferedImage imageToCopy;
        try {
            BufferedImage temp = ImageIO.read(is);
            imageToCopy = new BufferedImage(temp.getWidth(), temp.getHeight(), BufferedImage.TYPE_INT_ARGB);
            imageToCopy.createGraphics().drawImage(temp, 0, 0, null);
            
            _size        = new IntVector2D(imageToCopy.getWidth(), imageToCopy.getHeight());
            _image       = new BufferedImage(_size.x(), _size.y(), BufferedImage.TYPE_INT_ARGB);
            _filename    = _size.x() + "-by-" + _size.y();
            _orientation = Orientation.UP;
            
            setPixels(((DataBufferInt)imageToCopy.getRaster().getDataBuffer()).getData());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not open file stream: " + is);
        }
    }

    /** Creates a new image from a given filename. First searches through working directory, then looks at the directoy of 
     * the .class file. **/
    public Image(String filename) {
        _filename = filename;
        try {
            // try to read from file in working directory
            File file = new File(_filename);
            if (file.isFile()) {
                BufferedImage temp = ImageIO.read(file);
                _image = new BufferedImage(temp.getWidth(), temp.getHeight(), BufferedImage.TYPE_INT_ARGB);
                _image.createGraphics().drawImage(temp, 0, 0, null);
            }

            // now try to read from file in same directory as this .class file
            else {
                URL url = getClass().getResource(_filename);
                if (url == null) { url = new URL(_filename); }
                _image = ImageIO.read(url);
            }
            _size = new IntVector2D(_image.getWidth(), _image.getHeight());
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not open file: " + _filename);
        }
        _orientation = Orientation.UP;
    }

    /** Creates file from a File object. **/
    public Image(File file) {
        try { 
            BufferedImage temp = ImageIO.read(file);
            _image = new BufferedImage(temp.getWidth(), temp.getHeight(), BufferedImage.TYPE_INT_ARGB);
            _image.createGraphics().drawImage(temp, 0, 0, null);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not open file: " + file);
        }
        if (_image == null) {
            throw new RuntimeException("Invalid image file: " + file);
        }
        _size        = new IntVector2D(_image.getWidth(), _image.getHeight());
        _filename    = file.getName();
        _orientation = Orientation.UP;
    }

    /** Return a new image object identical to the current image. */
    public Image copy() { return new Image(this); }
    
    /** Returns a copy of an image object resized to new bounds. Scales pixels by deciding which pixel has the best claim (which pixel is closest
     * to the empty pixel upon resize). Does not perform any anti-aliasing. WARNING: If you shrink an image and set that image to it, the
     * old data is permanently lost.
     * @param image			Image object to resize. 
     * @param newWidth		New width to scale image to.
     * @param newHeight		New height to scale image to. **/
    public static Image resize(Image image, int newWidth, int newHeight) {
        return image.resize(newWidth, newHeight);
    }
    public static Image resizeScaledWidth(Image image, int newWidth) {
        return image.resizeScaledWidth(newWidth);
    }
    public static Image resizeScaledHeight(Image image, int newHeight) {
        return image.resizeScaledHeight(newHeight);
    }
    /** Returns a copy of the image resized to new bounds. Scales pixels by deciding which pixel has the best claim (which pixel is closest
     * to the empty pixel upon resize). Does not perform any anti-aliasing. WARNING: If you skrink the image and set this image to it, the
     * old data is permanently lost.
     * @param width		New width to scale image to.
     * @param height		New height to scale image to. **/
    public Image resize(int width, int height) {
        Image newImage = new Image(width, height);
        newImage.setOrientation(_orientation);
        float newToOldScaleX = (float)_size.x()/width;
        float newToOldScaleY = (float)_size.y()/height;
        
        int[] newImagePixels = newImage.getPixelsNoCopy();
        int[] oldImagePixels = this.getPixelsNoCopy();
        for (int x=0; x<width; x++) {
            int newx = (int)(x*newToOldScaleX);
            for (int y=0; y<height; y++) {
                newImagePixels[x + y*width] = oldImagePixels[newx + _size.x()*(int)(y*newToOldScaleY)];
            }
        }
        return newImage;
    }
    public Image resizeScaledWidth(int width) {
        float ratio =  width/(float)_size.x();
        return resize(width, (int)(_size.y()*ratio));
    }
    public Image resizeScaledHeight(int height) {
        float ratio =  height/(float)_size.y();
        return resize((int)(_size.x()*ratio), height);
    }
    
    /** Returns a new image set to the new orientation given. **/
    public static Image reorient(Image image, Orientation orientation) {
        return image.reorient(orientation);
    }
    /** Returns a new image set to the new orientation given. **/
    public Image reorient(Orientation orientation) {
        int[][] matrix      = Orientation.getRotationMatrix(_orientation, orientation);
        int     newWidth    = Math.abs(_size.x()*matrix[0][0] + _size.y()*matrix[0][1]);
        int     newHeight   = Math.abs(_size.x()*matrix[1][0] + _size.y()*matrix[1][1]);
        Image   newImage    = new Image(newWidth, newHeight);
        
        newImage.setOrientation(orientation);
        
        int[]   newImagePixels = newImage.getPixelsNoCopy();
        int[]   oldImagePixels = this.getPixelsNoCopy();
        int     newx = 0;
        int     newy = 0;

        for (int x=0; x<_size.x(); x++) {
            int xcomponent1 = x*matrix[0][0];
            int xcomponent2 = x*matrix[1][0];
            for (int y=0; y<_size.y(); y++) {
                newx = xcomponent1 + y*matrix[0][1];
                if (newx < 0) {
                    newx += newWidth;
                }
                newy = xcomponent2 + y*matrix[1][1];
                if (newy < 0) {
                    newy += newHeight;
                }
                
                newImagePixels[newx + newy*newWidth] = oldImagePixels[x + _size.x()*y];
            }
        }
        return newImage;
    }
    
    /** Returns an image rotated 90 degrees clockwise by rotationsRight many times, and sets new orientation. A negative number means left turns. **/
    public static Image rotate(Image image, int rotationsRight) {
        return image.rotate(rotationsRight);
    }
    /** Returns an image rotated 90 degrees clockwise by rotationsRight many times, and sets new orientation. A negative number means left turns. **/
    public Image rotate(int rotationsRight) {
        return reorient(Orientation.fromInt(Orientation.toInt(_orientation)+rotationsRight));
    }
    
    /** Returns the color data of the image at the pixel (w, h) as an integer. Note that the point (0, 0) is the origin of the image, not the screen. **/
    public int get(int x, int y) {
        if (x < 0 || x >= _size.x())  throw new IndexOutOfBoundsException("x must be between 0 and " + (_size.x()-1) + ", recieved " + x);
        if (y < 0 || y >= _size.y()) throw new IndexOutOfBoundsException("y must be between 0 and " + (_size.y()-1) + ", recieved " + y);
        return _image.getRGB(x, y);
    }
    /** Returns the color data of the image at the pixel (w, h) as a Color object. Note that the point (0, 0) is the origin of the image, not the screen. **/
    public Color getColor(int x, int y) {
        if (x < 0 || x >= _size.x())  throw new IndexOutOfBoundsException("x must be between 0 and " + (_size.x()-1) + ", recieved " + x);
        if (y < 0 || y >= _size.y()) throw new IndexOutOfBoundsException("y must be between 0 and " + (_size.y()-1) + ", recieved " + y);
        return new Color(_image.getRGB(x, y), true);
    }
    /** Returns the color data of all the pixels as an array of integers. Note that this creates a copy of the original pixels. **/
    public int[] getPixels() {
        int[] pixelData = new int[_size.x()*_size.y()];
        System.arraycopy(((DataBufferInt)_image.getRaster().getDataBuffer()).getData(), 0, pixelData, 0, _size.x()*_size.y());
        return pixelData;
    }
    
    /** Returns the color data of all the pixels as an array of integers. Returns the base array. Changes to the returned array WILL modify the image data.
     * Important note: If Image is used in context of an ImageBox and not just a standalone image, you need to take into account that the backing image 
     * used to preserve image quality needs to be updated on changes made to the pixel array. <P>
     * Note that this is essentially the fastest way to change pixel data directly. **/
    public int[] getPixelsNoCopy() {
        return ((DataBufferInt)_image.getRaster().getDataBuffer()).getData();
    }
    
    /** Sets the color data of the image at the pixel (w, h) using an integer. Note that th point (0, 0) is the origin of the image, not the screen. **/
    public void set(int x, int y, int color) {
        if (x < 0 || x >= _size.x())  throw new IndexOutOfBoundsException("x must be between 0 and " + (_size.x()-1) + ", recieved " + x);
        if (y < 0 || y >= _size.y()) throw new IndexOutOfBoundsException("y must be between 0 and " + (_size.y()-1) + ", recieved " + y);
        _image.setRGB(x, y, color);
    }
    /** Sets the color data of the image at the pixel (w, h) using a Color object. Note that th point (0, 0) is the origin of the image, not the screen. **/
    public void setColor(int x, int y, Color color) {
        if (x < 0 || x >= _size.x())  throw new IndexOutOfBoundsException("x must be between 0 and " + (_size.x()-1) + ", recieved " + x);
        if (y < 0 || y >= _size.y()) throw new IndexOutOfBoundsException("y must be between 0 and " + (_size.y()-1) + ", recieved " + y);
        if (color == null) throw new NullPointerException("can't set Color to null");
        _image.setRGB(x, y, color.getRGB());
    }
    /** Sets all the color data of the image from an array of integers. **/
    public void setPixels(int[] pixelData) {
        System.arraycopy(pixelData, 0, ((DataBufferInt)_image.getRaster().getDataBuffer()).getData(), 0, _size.x()*_size.y());
    }

    /** Draws an image to a Graphics2D object with the origin at point x, y on the screen. 
     * Don't call this yourself. Use the DrawModule image() method. **/
    public void draw(Graphics2D g, int x, int y) {
        g.drawImage(_image, x, y, null);
    }
    /** Draws an image to a Graphics2D object with the center of the image at point x, y on the screen. 
     * Don't call this yourself. Use the DrawModule imageCentered() method. **/
    public void drawCentered(Graphics2D g, int x, int y) {
        g.drawImage(_image, x-(int)_image.getWidth()/2, y-(int)_image.getHeight()/2, null);
    }
    /** Draws and image rotated by an angle to a Graphics2D object with the center of the image at point x, y on the screen. 
     * Don't call this yourself. Use the DrawModule imageRotated() method. **/
    public void drawRotated(Graphics2D g, int x, int y, double angle) {
        g.rotate(angle, x, y);
        g.drawImage(_image, x-(int)_image.getWidth()/2, y-(int)_image.getHeight()/2, null);
        g.rotate(-angle, x, y);
    }

    /** Returns whether an object equals this Image object. They must both be Image objects, and their color data must match exactly. **/
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        
        Image that = (Image) obj;

        if (_size.x() != that.size().x()) return false;
        if (_size.y() != that.size().y()) return false;
        
        int[] thisImagePixels = this.getPixelsNoCopy();
        int[] thatImagePixels = that.getPixelsNoCopy();
        int area = _size.x()*_size.y();
        for (int i = 0; i < area; i++) {
            if (thisImagePixels[i] != thatImagePixels[i]) {
                return false;
            }
        }
        return true;
    }
}