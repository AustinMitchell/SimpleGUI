package simple.gui.data;

import java.util.Objects;

/** Represents a 2D vector and a bunch of operations on vectors. */
public class IntVector2D implements ConstIntVector2D {	
    /** The components of the vector**/
    private int _x, _y;


    //////////////////////////////
    // ------ PROPERTIES ------ //
    //////////////////////////////


    /** Returns x component value */
    public int x() { return _x; }
    /** Returns y component value */
    public int y() { return _y; }

    /** Returns the magnitude(length) of this vector **/
    public double mag() { return Math.sqrt(_x*_x + _y*_y); }

    /** Returns the distance between this vector and another vector. 
     * @param v   Vector to measure against
     * **/
    public double dist(ConstIntVector2D v) { return sub(v).mag(); }

    /** Returns the distance between this vector and another vector using an approximation with integers
     * @param vec   Vector to measure against
     * @return      Approximate distance 
     * **/
    public int approxdist(ConstIntVector2D v) {
        int min, max, approx;
        int dx, dy;
        dx = (int)(_x-v.x());
        dy = (int)(_y-v.y());

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

    /** Checks if the vector inside a box, whose bottom left corner and top right corner are defined by {p1} and {p2}. When called, this method will
     * adjust the x and y components of this vector to keep them within the bounding box.
     * @param p1   Lower left boundbox corner
     * @param p2   Upper right boundbox corner
     * @return     If this vector is bound by the box made my {p1} and {p2}
     * **/
    public boolean isBoundWithinPoints(ConstIntVector2D p1, ConstIntVector2D p2) {
        return _x >= p1.x() && _x <= p2.x() &&
               _y >= p1.y() && _y <= p2.y();
    }

    /** Checks if the vector inside a box, whose bottom-left corner and side lengths are defined by {pos} and {dim}. When called, this methodwill
     * adjust the x and y components of this vector to keep them within the bounding box.
     * @param pos   Bottom left corner of bounding box
     * @param dim   Dimensions of bounding box
     * @return      If this vector is bound by the box made my {pos} and {dim}
     * **/
    public boolean isBoundWithinBox(ConstIntVector2D pos, ConstIntVector2D dim) {
        return _x >= pos.x() && _x <= (pos.x()+dim.x()) &&
               _y >= pos.y() && _y <= (pos.y()+dim.y());
    }

    /** Returns the angle of this vector (essentially compares it to the vector[1, 0]) **/
    public double angle() { return angle(new IntVector2D(1, 0)); }


    ///////////////////////////
    // ------ SETTERS ------ //
    ///////////////////////////


    /** Set x component value */
    public IntVector2D setX(int x) {
        _x = x;
        return this;
    }
    /** Returns y as an integer */
    public IntVector2D setY(int y) { 
        _y = y;
        return this;
    }

    /** Set vector components equal to another vector */
    public IntVector2D set(int x, int y) {
        _x = x;
        _y = y;
        return this;
    }

    /** Set vector components equal to another vector */
    public IntVector2D set(ConstIntVector2D vec) {
        _x = vec.x();
        _y = vec.y();
        return this;
    }
    

    ////////////////////////////////
    // ------ CONSTRUCTORS ------ //
    ////////////////////////////////


    /** Creates a new vector [x, y] from doubles */
    public IntVector2D(int x, int y) {
        _x = x;
        _y = y;
    }
    /** Creates a copy of the vector toCopy */
    public IntVector2D(ConstIntVector2D vec) {
        _x = vec.x();
        _y = vec.y();
    }
    /** Creates a copy of the vector toCopy */
    public IntVector2D(ConstVector2D vec) {
        _x = (int)Math.round(vec.x());
        _y = (int)Math.round(vec.y());
    }

    //////////////////////////////
    // ------ OPERATIONS ------ //
    //////////////////////////////

    /** Returns a vector with the same component values **/
    public IntVector2D copy() { return new IntVector2D(_x, _y); }

    /** Returns a ConstIntVector2D handle for the vector **/
    public ConstIntVector2D asConst() { return (ConstIntVector2D)this; }

    /** Returns a ConstIntVector2D handle for the vector **/
    public Vector2D asVector2D() { return new Vector2D(_x, _y); }

    /** [new vector] Binds the vector inside a box, whose bottom left corner and top right corner are defined by {p1} and {p2}. When called, this method will
     * adjust the x and y components of this vector to keep them within the bounding box.
     * @param p1   Lower left boundbox corner
     * @param p2   Upper right boundbox corner
     * @return     The same vector instance 
     * **/
    public IntVector2D bindWithinPoints(ConstIntVector2D p1, ConstIntVector2D p2) {
        int newx = Math.max(p1.x(), Math.min(p2.x(), _x));
        int newy = Math.max(p1.y(), Math.min(p2.y(), _y));
        return new IntVector2D(newx, newy); 
    }

    /** [new vector] Binds the vector inside a box, whose bottom-left corner and side lengths are defined by {pos} and {dim}. When called, this method will adjust
     * the x and y components of this vector to keep them within the bounding box.
     * @param pos   Bottom left corner of bounding box
     * @param dim   Dimensions of bounding box
     * @return      The same vector instance
     * **/
    public IntVector2D bindWithinBox(ConstIntVector2D pos, ConstIntVector2D dim) {
        int newx = Math.max(pos.x(), Math.min(pos.x()+dim.x(), _x));
        int newy = Math.max(pos.y(), Math.min(pos.y()+dim.y(), _y));
        return new IntVector2D(newx, newy); 
    }

    /** [new vector] Returns the dotproduct of this and another vector **/
    public double dotProduct(ConstIntVector2D v) { return _x*v.x() + _y*v.y(); }
    /** [new vector] Returns the angle between this and another vector **/
    public double angle(ConstIntVector2D v) { return Math.acos((dotProduct(v) / (mag()*v.mag()))); }

    /** [new vector] Normalizes this vector (adjust the x and y components so the magnitude is 1) **/
    public IntVector2D norm() { double m = mag(); return new IntVector2D((int)Math.round(_x/m), (int)Math.round(_y/m)); }
    /** [new vector] Normalizes this vector and multiplies it by a scalar (adjust the x and y components so the magnitude is c) **/
    public IntVector2D norm(double c) { return norm().i_mult(c); }
    /** [new vector] Rotates this vector by the given angle **/
    public IntVector2D rotate(double radians) {
        int newx = (int)Math.round(_x*Math.cos(radians) - _y*Math.sin(radians));
        int newy = (int)Math.round(_y*Math.cos(radians) + _x*Math.sin(radians));
        return new IntVector2D(newx, newy); 
    }
    /** [new vector] Adjusts the x and y components of this vector to match a vector of the given angle with the same magnitude **/
    public IntVector2D atAngle(double radians) { 
        double m = mag();
        int newx = (int)Math.round(Math.cos(radians)*m);
        int newy = (int)Math.round(Math.sin(radians)*m);
        return new IntVector2D(newx, newy); 
    }

    /** [new vector] Multiplies the components of this vector by -1 **/
    public IntVector2D neg() { return new IntVector2D(_x*-1, _y*-1); }
    /** [new vector] Inverts the components with regards to the line y = x **/
    public IntVector2D inv() { return new IntVector2D(_y, _x); }
    
    /** [new vector] Adds the components of another vector to components of this vector **/
    public IntVector2D add (ConstIntVector2D v) { return new IntVector2D(_x+v.x(), _y+v.y()); }
    /** [new vector] Subtracts the components of another vector from components of this vector **/
    public IntVector2D sub (ConstIntVector2D v) { return new IntVector2D(_x-v.x(), _y-v.y()); }
    /** [new vector] Multiplies the components of this vector by components of another vector **/
    public IntVector2D mult(ConstIntVector2D v) { return new IntVector2D(_x*v.x(), _y*v.y()); }
    /** [new vector] Divides the components of this vector by the components of another vector. WARNING: Does not check for 0 **/
    public IntVector2D div (ConstIntVector2D v) { return new IntVector2D(_x/v.x(), _y/v.y()); }
    
    /** [new vector] Adds the given scalar to each component of this vector **/
    public IntVector2D add (int c) { return new IntVector2D(_x+c, _y+c); }
    /** [new vector] Subtracts the given scalar from each component of this vector **/
    public IntVector2D sub (int c) { return new IntVector2D(_x-c, _y-c); }
    /** [new vector] Multiplies each component of this vector by the given scalar **/
    public IntVector2D mult(double c) { return new IntVector2D((int)Math.round(_x*c), (int)Math.round(_y*c)); }
    /** [new vector] Divides each component of this vector by the given scalar **/
    public IntVector2D div (double c) { return new IntVector2D((int)Math.round(_x/c), (int)Math.round(_y/c)); }
    
    /** [new vector] Adds each given scalar to each component of this vector respectively **/
    public IntVector2D add (int cx, int cy) { return new IntVector2D(_x+cx, _y+cy); }
    /** [new vector] Subtracts each given scalar from each component of this vector respectively **/
    public IntVector2D sub (int cx, int cy) { return new IntVector2D(_x-cx, _y-cy); }
    /** [new vector] Multiplies each component of this vector by each given scalar respectively **/
    public IntVector2D mult(double cx, double cy) { return new IntVector2D((int)Math.round(_x*cx), (int)Math.round(_y*cy)); }
    /** [new vector] Divides each component of this vector by each given scalar respectively **/
    public IntVector2D div (double cx, double cy) { return new IntVector2D((int)Math.round(_x/cx), (int)Math.round(_y/cy)); }
    
    ///////////////////////////////////////
    // ------ IN-PLACE OPERATIONS ------ //
    ///////////////////////////////////////

    /** [in-place] Binds the vector inside a box, whose bottom left corner and top right corner are defined by {p1} and {p2}. When called, this method will
     * adjust the x and y components of this vector to keep them within the bounding box.
     * @param p1   Lower left boundbox corner
     * @param p2   Upper right boundbox corner
     * @return     The same vector instance 
     * **/
    public IntVector2D i_bindWithinPoints(ConstIntVector2D p1, ConstIntVector2D p2) {
        _x = Math.max(p1.x(), Math.min(p2.x(), _x));
        _y = Math.max(p1.y(), Math.min(p2.y(), _y));
        return this; 
    }

    /** [in-place] Binds the vector inside a box, whose bottom-left corner and side lengths are defined by {pos} and {dim}. When called, this method will adjust
     * the x and y components of this vector to keep them within the bounding box.
     * @param pos   Bottom left corner of bounding box
     * @param dim   Dimensions of bounding box
     * @return      The same vector instance
     * **/
    public IntVector2D i_bindWithinBox(ConstIntVector2D pos, ConstIntVector2D dim) {
        _x = Math.max(pos.x(), Math.min(pos.x()+dim.x(), _x));
        _y = Math.max(pos.y(), Math.min(pos.y()+dim.y(), _y));
        return this; 
    }

    /** [in-place] Normalizes this vector (adjust the x and y components so the magnitude is 1) **/
    public IntVector2D i_norm() {
        double m = mag();
        set((int)Math.round(_x/m), (int)Math.round(_y/m));
        return this;
    }
    /** [in-place] Normalizes this vector and multiplies it by a scalar (adjust the x and y components so the magnitude is c) **/
    public IntVector2D i_norm(double c) { return i_norm().i_mult(c); }
    /** [in-place] Rotates this vector by the given angle **/
    public IntVector2D i_rotate(double radians) {
        int newx = (int)Math.round(_x*Math.cos(radians) - _y*Math.sin(radians));
        int newy = (int)Math.round(_y*Math.cos(radians) + _x*Math.sin(radians));
        set(newx, newy); 
        return this;
    }
    /** [in-place] Adjusts the x and y components of this vector to match a vector of the given angle with the same magnitude **/
    public IntVector2D i_atAngle(double radians) { 
        double m = mag();
        int newx = (int)Math.round(Math.cos(radians)*m);
        int newy = (int)Math.round(Math.sin(radians)*m);
        set(newx, newy);
        return this;
    }

    /** [in-place] Multiplies the components of this vector by -1 **/
    public IntVector2D i_neg() { return set(_x*-1, _y*-1); }
    /** [in-place] Inverts the components with regards to the line y = x **/
    public IntVector2D i_inv() { return set(_y, _x); }
    
    /** [in-place] Adds the components of another vector to components of this vector **/
    public IntVector2D i_add (ConstIntVector2D v) { return set(_x+v.x(), _y+v.y()); }
    /** [in-place] Subtracts the components of another vector from components of this vector **/
    public IntVector2D i_sub (ConstIntVector2D v) { return set(_x-v.x(), _y-v.y()); }
    /** [in-place] Multiplies the components of this vector by components of another vector **/
    public IntVector2D i_mult(ConstIntVector2D v) { return set(_x*v.x(), _y*v.y()); }
    /** [in-place] Divides the components of this vector by the components of another vector. WARNING: Does not check for 0 **/
    public IntVector2D i_div (ConstIntVector2D v) { return set(_x/v.x(), _y/v.y()); }
    
    /** [in-place] Adds the given scalar to each component of this vector **/
    public IntVector2D i_add (int c) { return set(_x+c, _y+c); }
    /** [in-place] Subtracts the given scalar from each component of this vector **/
    public IntVector2D i_sub (int c) { return set(_x-c, _y-c); }
    /** [in-place] Multiplies each component of this vector by the given scalar **/
    public IntVector2D i_mult(double c) { return set((int)Math.round(_x*c), (int)Math.round(_y*c)); }
    /** [in-place] Divides each component of this vector by the given scalar **/
    public IntVector2D i_div (double c) { return set((int)Math.round(_x/c), (int)Math.round(_y/c)); }
    
    /** [in-place] Adds each given scalar to each component of this vector respectively **/
    public IntVector2D i_add (int cx, int cy) { return set(_x+cx, _y+cy); }
    /** [in-place] Subtracts each given scalar from each component of this vector respectively **/
    public IntVector2D i_sub (int cx, int cy) { return set(_x-cx, _y-cy); }
    /** [in-place] Multiplies each component of this vector by each given scalar respectively **/
    public IntVector2D i_mult(double cx, double cy) { return set((int)Math.round(_x*cx), (int)Math.round(_y*cy)); }
    /** [in-place] Divides each component of this vector by each given scalar respectively **/
    public IntVector2D i_div (double cx, double cy) { return set((int)Math.round(_x/cx), (int)Math.round(_y/cy)); }

    ////////////////////////////////////
    // ------ OBJECT OVERRIDES ------ //
    ////////////////////////////////////

    /** Returns a string representation of this vector **/
    @Override
    public String toString() {
        return "Vector: X=" + _x + "; Y=" + _y;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof IntVector2D)) { return false; }

        IntVector2D vec = (IntVector2D) other;
        return _x == vec.x() && _y == vec.y();
    }

    @Override
    public int hashCode() {
        return Objects.hash(_x, _y);
    }
}
