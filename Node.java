import java.util.*;
/**
 * Write a description of class junk_delete here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Node
{
    // instance variables - replace the example below with your own
    private char nodeType; //This specifies that the node is either S, G, R or X
    private ArrayList edges; //An array of all the neighbours of this node
    private int g; //The cost to reach this node
    private int h; //The heuristic from this node
    private String path; //The path used to get here, used for diagnostic output
    private int coordX; //The x-coordinate of this node
    private int coordY; //The y-coordinate of this node
    // We need the coordinates to work out our heuristic, 
    // and also to work out which directions will have a neighbour.

    /**
     * Constructor for objects of class junk_delete
     */
    public Node(int x, int y, char type)
    {
        // initialise instance variables
        path = ""; //No path yet, initialising this for safety.
        //edges = //we can work out the edges basde on the coordinates. Somehow...
        coordX = x;
        coordY = y;
        nodeType = type;
    }

    /**
     * Returns the cost to reach this node. Used to sort for UCS.
     *
     * @return the value of g
     */
    public int getCost()
    {
        // put your code here
        if (g == 0 && nodeType != 'S') {
            System.err.println("Can't get path cost to this node!");
            System.err.println("Node has not been examined, or something has gone wrong.");
        }
        return g;
    }

    /**
     * Returns the value of h for a node
     *
     * @return the value of h
     */
    public int getHeuristic()
    {
        // put your code here
        if (h == 0 && nodeType != 'G') {
            System.err.println("Can't get heuristic for this node!");
            System.err.println("Node has not been examined, or something has gone wrong.");
        }
        return h;
    }

    /**
     * Returns f(n) = g(n) + h(n), used for A/A*
     *
     * 
     * @return     the sum of g and n
     */
    public int getFn()
    {
        // put your code here
        return g + h;
    }

    /**
     * Returns a String representation of the Node's x and y coordinates
     * e.g "1,1"
     *
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y
     */
    public String getCoords()
    {
        String coords = coordX + "," + coordY;
        return coords;
    }

    
    
    // Inner classes seem to screw up BlueJ formatting, 
    // so I put them down here so I don't have to look at them too much.
    
    /**
     * This is the Comparator used when sorting by path cost.
     */
    public static Comparator<Node> GComparator = new Comparator<Node>() 
        {
            public int compare(Node n1, Node n2)
            {
                return n1.getCost() - n2.getCost();
            }
        };

    /**
     * This is the Comparator used when sorting by f (path cost plus heuristic) used for A/A*.
     */
    public static Comparator<Node> FComparator = new Comparator<Node>() 
        {
            public int compare(Node n1, Node n2)
            {
                return n1.getFn() - n2.getFn();
            }
        };

}
