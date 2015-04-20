import java.util.*;
/**
 * Represents an edge, or a direction it is possible to move in from a given node. 
 * Stored in a HashMap for each Node instance.
 * 
 */
public class Edge
{
    public static final Set<String> CARDINALS = new HashSet<String>(Arrays.asList(new String[]{"R", "D", "L", "U"})); // Seems like the best way to decide the cost....
    //private static final Set<String> DIAGONALS = new HashSet<String>(Arrays.asList(new String[]{"RD", "LD", "LU", "RU"})); // I only really need one of these but I'm keeping both for now
    private int cost; //The cost of moving along this edge to the neighbour, either 1 or 2
    private String direction; // the direction of the edge, might not need this since it's also used as the key for this Edge in the map
    // private String destinationC; // the coordinates of the node this edge points to. I could store a reference to the Node itself but this is getting too complicated already.
    private Node destination; // Going to try keeping a reference to a Node instead, so we can access the methods of the destination Node from the Edge
    /**
     * Constructor for objects of class Edge
     */
    public Edge(String newDirection, Node newDestination)
    {
        // initialise instance variables
        direction = newDirection;
        destination = newDestination;
        // Work out the cost of this edge
        if (CARDINALS.contains(direction)) {
            cost = 2;
        }
        else {
            cost = 1;
        }
    }

    /**
     * Returns the cost of an edge
     *
     * @return     the value of cost (int)
     */
    public int getEdgeCost()
    {
        // put your code here
        return cost;
    }


    /**
     * Returns the direction of an Edge, used when putting edges into Nodes
     *
     * @return     String containing edge direction
     */
    public String getDirection()
    {
        // put your code here
        return direction;
    }

    /**
     * Returns a reference to the Node this edge leads to.
     *
     * @return     coordinates of destination node
     */
    public Node getDest()
    {
        // put your code here
        return destination;
    }

}
