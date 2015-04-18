import java.util.*;
/**
 * Write a description of class Graph here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Graph
{
    // instance variables
    private int gridSize; // Passed in when we read from the input file. Seems like the most logical place to store this.
                         // Called it gridSize so I don't mix it up with graph.size()
    
    private LinkedHashMap<String,Node> nodes; 
    // Using a linked hashmap in case i want to iterate over this
    // Not that useful for the assignment, but fun for later.

    /**
     * Constructor for objects of class Graph
     */
    public Graph(int size)
    {
        // initialise instance variables
        nodes = new LinkedHashMap<String,Node>();
        gridSize = size;
    }

    /**
     * Retrieves a node from the graph by key.
     *
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y
     */
    public Node getNode(String coords)
    {
        // put your code here
        return nodes.get(coords);
    }

    /**
     * Puts a Node into the Graph
     *
     * @param  coords   A String containing coordinates, used as the identifier for the node.
     */
    public void putNode(String coords, Node node)
    {
        nodes.put(coords, node);
        //Debug output
        System.out.println("Put new node at (" + coords + ")");
    }

    /**
     * Returns the side length of the graph. We need this, I think.
     *
     * @return     the length of a side
     */
    public int getGridSize()
    {
        // put your code here
        return gridSize;
    }

    /**
     * Loads from an input array into a graph
     *
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y
     */
    public void loadGraph(ArrayList<String> input)
    {
        int line = 3; //we know based on the specifications that the map starts at input[3] so we just initialise to 3
        
        int size = Integer.parseInt(input.get(2));
        int y = 1; //we can initialise this here and increment it at the end of each line processed to keep track of our y coordinate
        while (line <= size + 2)
        {
            String row = input.get(line); //We have a line, e.g. SXR
            int col = 1;//we use this to know what column we're in, which gives us the x coordinate. 
                        //Gets reset to 1 each time we get a new line
                        
            
            for (char c: row.toCharArray())
            {
                int x = col;
                //Work out where we are on the input map so we can give the node coordinates
                String coords = x + "," + y;
                Node newNode = new Node(x, y, c);
                putNode(coords, newNode);
                col++;//incrementing our column number, when we finish the row this resets to 1
            }
            
            
            // Now we increment line and our y coordinate and move on
            line++;
            y++;
        }

    }
    
}
