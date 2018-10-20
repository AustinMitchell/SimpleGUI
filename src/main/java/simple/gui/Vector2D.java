package simple.gui;

import java.util.Objects;

/** Represents a 2D vector and a bunch of operations on vectors. */
public class Vector2D {	
    /** The components of the vector**/
    public double x, y;

    /** Returns x as an integer */
    public int x() { return (int)x; }
    /** Returns y as an integer */
    public int y() { return (int)y; }

    /** Creates a new vector [x, y] from integers */
    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }
    /** Creates a new vector [x, y] from doubles */
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }
    /** Creates a copy of the vector toCopy */
    public Vector2D(Vector2D toCopy) {
        this.x = toCopy.x;
        this.y = toCopy.y;
    }

    /** Binds the vector inside a box, whose bottom left corner and top right corner are defined by {p1} and {p2}. When called, this method will
     * adjust the x and y components of this vector to keep them within the bounding box.
     * @param p1   Lower left boundbox corner
     * @param p2   Upper right boundbox corner
     * @return     The same vector instance 
     * **/
    public Vector2D bindWithinPoints(Vector2D p1, Vector2D p2) {
        this.x = Math.max(p1.x, Math.min(p2.x, this.x));
        this.y = Math.max(p1.y, Math.min(p2.y, this.y));
        return this; 
    }

    /** Binds the vector inside a box, whose bottom-left corner and side lengths are defined by {pos} and {dim}. When called, this method will adjust
     * the x and y components of this vector to keep them within the bounding box.
     * @param pos   Bottom left corner of bounding box
     * @param dim   Dimensions of bounding box
     * @return      The same vector instance
     * **/
    public Vector2D bindWithinBox(Vector2D pos, Vector2D dim) {
        this.x = Math.max(pos.x, Math.min(pos.x+dim.x, this.x));
        this.y = Math.max(pos.y, Math.min(pos.y+dim.y, this.y));
        return this; 
    }

    /** Checks if the vector inside a box, whose bottom left corner and top right corner are defined by {p1} and {p2}. When called, this method will
     * adjust the x and y components of this vector to keep them within the bounding box.
     * @param p1   Lower left boundbox corner
     * @param p2   Upper right boundbox corner
     * @return     If this vector is bound by the box made my {p1} and {p2}
     * **/
    public boolean isBoundWithinPoints(Vector2D p1, Vector2D p2) {
        return this.x >= p1.x && this.x <= p2.x &&
               this.y >= p1.y && this.y <= p2.y;
    }

    /** Checks if the vector inside a box, whose bottom-left corner and side lengths are defined by {pos} and {dim}. When called, this methodwill
     * adjust the x and y components of this vector to keep them within the bounding box.
     * @param pos   Bottom left corner of bounding box
     * @param dim   Dimensions of bounding box
     * @return      If this vector is bound by the box made my {pos} and {dim}
     * **/
    public boolean isBoundWithinBox(Vector2D pos, Vector2D dim) {
        return this.x >= pos.x && this.x <= (pos.x+dim.x) &&
               this.y >= pos.y && this.y <= (pos.y+dim.y);
    }

    /** Returns a vector with the same component values **/
    public Vector2D copy() { return new Vector2D(x, y); }

    /** Returns the magnitude(length) of this vector **/
    public double mag() { return Math.sqrt(x*x + y*y); }

    /** Returns the distance between this vector and another vector. 
     * @param v   Vector to measure against
     * **/
    public double dist(Vector2D v) { return sub(v).mag(); }

    /** Returns the distance between this vector and another vector using an approximation with integers
     * @param vec   Vector to measure against
     * @return      Approximate distance 
     * **/
    public int approxdist(Vector2D v) {
        int min, max, approx;
        int dx, dy;
        dx = (int)(x-v.x);
        dy = (int)(y-v.y);

        if ( dx < 0 ) dx = -dx;
        if ( dy < 0 ) dy = -dy;

        if ( dx < dy )
        {
           min = (int)dx;
           max = (int)dy;
        } else {
           min = (int)dy;
           max = (int)dx;
        }

        approx = ( max * 1007 ) + ( min * 441 );
        if ( max < ( min << 4 ))
           approx -= ( max * 40 );

        // add 512 for proper rounding
        return (( approx + 512 ) >> 10 );
    }

    /** Returns the dotproduct of this and another vector **/
    public double dotProduct(Vector2D v) { return this.x*v.x + this.y*v.y; }
    /** Returns the angle between this and another vector **/
    public double angle(Vector2D v) { return Math.acos((this.dotProduct(v) / (this.mag()*v.mag()))); }
    /** Returns the angle of this vector (essentially compares it to the vector[1, 0]) **/
    public double angle() { return this.angle(new Vector2D(1, 0)); }

    /** Normalizes this vector (adjust the x and y components so the magnitude is 1) **/
    public Vector2D normalize() { double m = mag(); return new Vector2D(x/m, y/m); }
    /** Normalizes this vector and multiplies it by a scalar (adjust the x and y components so the magnitude is c) **/
    public Vector2D normalize(double c) { return normalize().mult(c); }
    /** Rotates this vector by the given angle **/
    public Vector2D rotate(double radians) {
        double newx = x*Math.cos(radians) - y*Math.sin(radians);
        double newy = y*Math.cos(radians) + x*Math.sin(radians);
        return new Vector2D(newx, newy); 
    }
    /** Adjusts the x and y components of this vector to match a vector of the given angle with the same magnitude **/
    public Vector2D setAngle(double radians) { 
        double m = mag();
        double newx = Math.cos(radians)*m;
        double newy = Math.sin(radians)*m;
        return new Vector2D(newx, newy); 
    }

    /** Multiplies the components of this vector by -1 **/
    public Vector2D neg() { return new Vector2D(x*-1, y*-1); }
    /** Inverts the components with regards to the line y = x **/
    public Vector2D inv() { return new Vector2D(y, x); }
    
    /** Adds the components of another vector to components of this vector **/
    public Vector2D add (Vector2D v) { return new Vector2D(x+v.x, y+v.y); }
    /** Subtracts the components of another vector from components of this vector **/
    public Vector2D sub (Vector2D v) { return new Vector2D(x-v.x, y-v.y); }
    /** Multiplies the components of this vector by components of another vector **/
    public Vector2D mult(Vector2D v) { return new Vector2D(x*v.x, y*v.y); }
    /** Divides the components of this vector by the components of another vector. WARNING: Does not check for 0 **/
    public Vector2D div (Vector2D v) { return new Vector2D(x/v.x, y/v.y); }
    
    /** Adds the given scalar to each component of this vector **/
    public Vector2D add (double c) { return new Vector2D(x+c, y+c); }
    /** Subtracts the given scalar from each component of this vector **/
    public Vector2D sub (double c) { return new Vector2D(x-c, y-c); }
    /** Multiplies each component of this vector by the given scalar **/
    public Vector2D mult(double c) { return new Vector2D(x*c, y*c); }
    /** Divides each component of this vector by the given scalar **/
    public Vector2D div (double c) { return new Vector2D(x/c, y/c); }
    
    /** Adds each given scalar to each component of this vector respectively **/
    public Vector2D add (double cx, double cy) { return new Vector2D(x+cx, y+cy); }
    /** Subtracts each given scalar from each component of this vector respectively **/
    public Vector2D sub (double cx, double cy) { return new Vector2D(x-cx, y-cy); }
    /** Multiplies each component of this vector by each given scalar respectively **/
    public Vector2D mult(double cx, double cy) { return new Vector2D(x*cx, y*cy); }
    /** Divides each component of this vector by each given scalar respectively **/
    public Vector2D div (double cx, double cy) { return new Vector2D(x/cx, y/cy); }
    
    /** Returns a string representation of this vector **/
    @Override
    public String toString() {
        return "Vector: X=" + x + "; Y=" + y;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Vector2D)) { return false; }

        Vector2D vec = (Vector2D) other;
        return this.x == vec.x && this.y == vec.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
