package simple.gui.data;

/** Represents a 2D vector and a bunch of operations on vectors. The difference between this and Vector2D is that all operations
 * that modify the vector components now return a new vector instead of modifying this one.
*/
public interface ConstVector2D {	

    /** Returns x component value */
    public double x();
    /** Returns y component value */
    public double y();

    /** Returns the magnitude(length) of this vector **/
    public double mag();

    /** Returns the distance between this vector and another vector. 
     * @param v   Vector to measure against
    * **/
    public double dist(ConstVector2D v);

    /** Returns the distance between this vector and another vector using an approximation with integers
     * @param vec   Vector to measure against
    * @return      Approximate distance 
    * **/
    public int approxdist(ConstVector2D v);

    /** Checks if the vector inside a box, whose bottom left corner and top right corner are defined by {p1} and {p2}. When called, this method will
     * adjust the x and y components of this vector to keep them within the bounding box.
    * @param p1   Lower left boundbox corner
    * @param p2   Upper right boundbox corner
    * @return     If this vector is bound by the box made my {p1} and {p2}
    * **/
    public boolean isBoundWithinPoints(ConstVector2D p1, ConstVector2D p2);

    /** Checks if the vector inside a box, whose bottom-left corner and side lengths are defined by {pos} and {dim}. When called, this methodwill
     * adjust the x and y components of this vector to keep them within the bounding box.
    * @param pos   Bottom left corner of bounding box
    * @param dim   Dimensions of bounding box
    * @return      If this vector is bound by the box made my {pos} and {dim}
    * **/
    public boolean isBoundWithinBox(ConstVector2D pos, ConstVector2D dim);

    /** Returns the angle of this vector (essentially compares it to the vector[1, 0]) **/
    public double angle();

     /** Returns a vector with the same component values **/
    public Vector2D copy();

    /** Returns a ConstVector2D handle for the vector **/
    public ConstVector2D asConst();

    /** [new vector] Binds the vector inside a box, whose bottom left corner and top right corner are defined by {p1} and {p2}. When called, this method will
     * adjust the x and y components of this vector to keep them within the bounding box.
     * @param p1   Lower left boundbox corner
     * @param p2   Upper right boundbox corner
     * @return     The same vector instance 
     * **/
    public Vector2D bindWithinPoints(ConstVector2D p1, ConstVector2D p2);

    /** [new vector] Binds the vector inside a box, whose bottom-left corner and side lengths are defined by {pos} and {dim}. When called, this method will adjust
     * the x and y components of this vector to keep them within the bounding box.
     * @param pos   Bottom left corner of bounding box
     * @param dim   Dimensions of bounding box
     * @return      The same vector instance
     * **/
    public Vector2D bindWithinBox(ConstVector2D pos, ConstVector2D dim);

    /** [new vector] Returns the dotproduct of this and another vector **/
    public double dotProduct(ConstVector2D v);
    /** [new vector] Returns the angle between this and another vector **/
    public double angle(ConstVector2D v);

    /** [new vector] Normalizes this vector (adjust the x and y components so the magnitude is 1) **/
    public Vector2D norm();
    /** [new vector] Normalizes this vector and multiplies it by a scalar (adjust the x and y components so the magnitude is c) **/
    public Vector2D norm(double c);
    /** [new vector] Rotates this vector by the given angle **/
    public Vector2D rotate(double radians);
    /** [new vector] Adjusts the x and y components of this vector to match a vector of the given angle with the same magnitude **/
    public Vector2D atAngle(double radians);

    /** [new vector] Multiplies the components of this vector by -1 **/
    public Vector2D neg();
    /** [new vector] Inverts the components with regards to the line y = x **/
    public Vector2D inv();
    
    /** [new vector] Adds the components of another vector to components of this vector **/
    public Vector2D add (ConstVector2D v);
    /** [new vector] Subtracts the components of another vector from components of this vector **/
    public Vector2D sub (ConstVector2D v);
    /** [new vector] Multiplies the components of this vector by components of another vector **/
    public Vector2D mult(ConstVector2D v);
    /** [new vector] Divides the components of this vector by the components of another vector. WARNING: Does not check for 0 **/
    public Vector2D div (ConstVector2D v);
    
    /** [new vector] Adds the given scalar to each component of this vector **/
    public Vector2D add (double c);
    /** [new vector] Subtracts the given scalar from each component of this vector **/
    public Vector2D sub (double c);
    /** [new vector] Multiplies each component of this vector by the given scalar **/
    public Vector2D mult(double c);
    /** [new vector] Divides each component of this vector by the given scalar **/
    public Vector2D div (double c);
    
    /** [new vector] Adds each given scalar to each component of this vector respectively **/
    public Vector2D add (double cx, double cy);
    /** [new vector] Subtracts each given scalar from each component of this vector respectively **/
    public Vector2D sub (double cx, double cy);
    /** [new vector] Multiplies each component of this vector by each given scalar respectively **/
    public Vector2D mult(double cx, double cy);
    /** [new vector] Divides each component of this vector by each given scalar respectively **/
    public Vector2D div (double cx, double cy);
}
