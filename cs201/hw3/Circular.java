// Circular.java
// (c) 1997 Kim Bruce; Modified 1999 Andrea Danyluk, Daniel Scharstein
//
// Interface for collection of objects in a circular arrangement.

public interface Circular
{
    // pre:  this is non-empty
    // post: moves current to successor position in arrangement
    public void next();

    // pre:
    // post: returns number of elts in object (0 if empty)
    public int size();

    // pre:  this is non-empty
    // post: returns the current element
    public Object getCurrent();

    // pre:  this is non-empty
    // post: Current elt is deleted, if non-empty, successor of old current
    //       is new current elt
    public void removeCurrent();

    // pre:
    // post: If this was empty, obj is inserted as only elt, otherwise added
    //       after current elt.  Either way, obj is now current elt
    public void addAfterCurrent(Object obj);

    // pre:
    // post: collection of objects is now empty
    public void clear();
}
