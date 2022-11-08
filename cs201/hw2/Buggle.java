// Buggle.java
// Implements a "Buggle" that can move forward, turn left or right by
// 90 degrees, and can return a string representation of itself
// CS 201 HW 2 problem 5

public class Buggle {

    protected int x;
    protected int y;
    protected int direction;

    // construct a new Buggle
    public Buggle() {
        this.x = 1;
        this.y = 1;
        this.direction = 1;
    }

    // move forward one grid cell by adding/subtracting to the x or y
    // coordinate depending on the direction the buggle is facing
    public void forward() {
        if (this.direction == 1) {
            this.x += 1;
        }
        if (this.direction == 2) {
            this.y += 1;
        }
        if (this.direction == 3) {
            this.x -= 1;
        }
        if (this.direction == 4) {
            this.y -= 1;
        }
    }

    // move forward 'k' grid cells in the same way forward() works
    public void forward(int k) {
        if (this.direction == 1) {
            this.x += k;
        }
        if (this.direction == 2) {
            this.y += k;
        }
        if (this.direction == 3) {
            this.x -= k;
        }
        if (this.direction == 4) {
            this.y -= k;
        }
    }

    // rotate 90 degrees to right (EAST -> SOUTH -> WEST -> NORTH -> EAST)
    // EAST = 1, SOUTH = 2, WEST = 3, NORTH = 4
    public void right() {
        if (this.direction == 1) {
            this.direction += 3;
        }
        else if (this.direction == 2) {
            this.direction -= 1;
        }
        else if (this.direction == 3) {
            this.direction -= 1;
        }
        else if (this.direction == 4) {
            this.direction -= 1;
        }
    }

    // rotate 90 degrees to left (EAST -> NORTH -> WEST -> SOUTH -> EAST)
    public void left() {
        if (this.direction == 1) {
            this.direction = 2;
        }
        else if (this.direction == 2) {
            this.direction += 1;
        }
        else if (this.direction == 3) {
            this.direction += 1;
        }
        else if (this.direction == 4) {
            this.direction = 1;
        }
    }

    // return a string representation of the object
    public String toString() {

        // Changes the int value of the direction to a string of its
        // corresponding direction
        String east = "EAST";
        String north = "NORTH";
        String west = "WEST";
        String south = "SOUTH";

        if (this.direction == 1) {
            return "x=" + x + ", y=" + y + ", direction=" + east;
        }
        if (this.direction == 2) {
            return "x=" + x + ", y=" + y + ", direction=" + north;
        }
        if (this.direction == 3) {
            return "x=" + x + ", y=" + y + ", direction=" + west;
        }
        if (this.direction == 4) {
            return "x=" + x + ", y=" + y + ", direction=" + south;
        } else
            return "";

    }
}
