package simple.gui.data;

/** Represents a 2D vector and a bunch of operations on vectors. The difference between this and Vector2D is that all operations
 * that modify the vector components now return a new vector instead of modifying this one.
*/
public interface ConstIntVector2D {	

    /** Returns x component value */
    public int x();
    /** Returns y component value */
    public int y();

    /** Returns the magnitude(length) of this vector **/
    public double mag();

    /** Returns the distance between this vector and another vector. 
     * @param v   Vector to measure against
    * **/
    public double dist(ConstIntVector2D v);

    /** Returns the distance between this vector and another vector using an approximation with integers
     * @param vec   Vector to measure against
    * @return      Approximate distance 
    * **/
    public int approxdist(ConstIntVector2D v);

    /** Checks if the vector inside a box, whose bottom left corner and top right corner are defined by {p1} and {p2}. When called, this method will
     * adjust the x and y components of this vector to keep them within the bounding box.
    * @param p1   Lower left boundbox corner
    * @param p2   Upper right boundbox corner
    * @return     If this vector is bound by the box made my {p1} and {p2}
    * **/
    public boolean isBoundWithinPoints(ConstIntVector2D p1, ConstIntVector2D p2);

    /** Checks if the vector inside a box, whose bottom-left corner and side lengths are defined by {pos} and {dim}. When called, this methodwill
     * adjust the x and y components of this vector to keep them within the bounding box.
    * @param pos   Bottom left corner of bounding box
    * @param dim   Dimensions of bounding box
    * @return      If this vector is bound by the box made my {pos} and {dim}
    * **/
    public boolean isBoundWithinBox(ConstIntVector2D pos, ConstIntVector2D dim);

    /** Returns the angle of this vector (essentially compares it to the vector[1, 0]) **/
    public double angle();

     /** Returns a vector with the same component values **/
    public IntVector2D copy();

    /** Returns a ConstVector2D handle for the vector **/
    public ConstIntVector2D asConst();

    /** Returns a Vector2D object with the same properties */
    public Vector2D asVector2D();

    /** [new vector] Binds the vector inside a box, whose bottom left corner and top right corner are defined by {p1} and {p2}. When called, this method will
     * adjust the x and y components of this vector to keep them within the bounding box.
     * @param p1   Lower left boundbox corner
     * @param p2   Upper right boundbox corner
     * @return     The same vector instance 
     * **/
    public IntVector2D bindWithinPoints(ConstIntVector2D p1, ConstIntVector2D p2);

    /** [new vector] Binds the vector inside a box, whose bottom-left corner and side lengths are defined by {pos} and {dim}. When called, this method will adjust
     * the x and y components of this vector to keep them within the bounding box.
     * @param pos   Bottom left corner of bounding box
     * @param dim   Dimensions of bounding box
     * @return      The same vector instance
     * **/
    public IntVector2D bindWithinBox(ConstIntVector2D pos, ConstIntVector2D dim);

    /** [new vector] Returns the dotproduct of this and another vector **/
    public double dotProduct(ConstIntVector2D v);
    /** [new vector] Returns the angle between this and another vector **/
    public double angle(ConstIntVector2D v);

    /** [new vector] Normalizes this vector (adjust the x and y components so the magnitude is 1) **/
    public IntVector2D norm();
    /** [new vector] Normalizes this vector and multiplies it by a scalar (adjust the x and y components so the magnitude is c) **/
    public IntVector2D norm(double c);
    /** [new vector] Rotates this vector by the given angle **/
    public IntVector2D rotate(double radians);
    /** [new vector] Adjusts the x and y components of this vector to match a vector of the given angle with the same magnitude **/
    public IntVector2D atAngle(double radians);

    /** [new vector] Multiplies the components of this vector by -1 **/
    public IntVector2D neg();
    /** [new vector] Inverts the components with regards to the line y = x **/
    public IntVector2D inv();
    
    /** [new vector] Adds the components of another vector to components of this vector **/
    public IntVector2D add (ConstIntVector2D v);
    /** [new vector] Subtracts the components of another vector from components of this vector **/
    public IntVector2D sub (ConstIntVector2D v);
    /** [new vector] Multiplies the components of this vector by components of another vector **/
    public IntVector2D mult(ConstIntVector2D v);
    /** [new vector] Divides the components of this vector by the components of another vector. WARNING: Does not check for 0 **/
    public IntVector2D div (ConstIntVector2D v);
    
    /** [new vector] Adds the given scalar to each component of this vector **/
    public IntVector2D add (int c);
    /** [new vector] Subtracts the given scalar from each component of this vector **/
    public IntVector2D sub (int c);
    /** [new vector] Multiplies each component of this vector by the given scalar **/
    public IntVector2D mult(double c);
    /** [new vector] Divides each component of this vector by the given scalar **/
    public IntVector2D div (double c);
    
    /** [new vector] Adds each given scalar to each component of this vector respectively **/
    public IntVector2D add (int cx, int cy);
    /** [new vector] Subtracts each given scalar from each component of this vector respectively **/
    public IntVector2D sub (int cx, int cy);
    /** [new vector] Multiplies each component of this vector by each given scalar respectively **/
    public IntVector2D mult(double cx, double cy);
    /** [new vector] Divides each component of this vector by each given scalar respectively **/
    public IntVector2D div (double cx, double cy);
}
