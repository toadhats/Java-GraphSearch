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
    // Using a linked hashmap in case i want to iterate over this, which I do.

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
        if (Driver.verbose()){
        System.out.println("Put new node at (" + coords + ")");}
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

    /**
     * Looks at the coordinates of each node to work out what its edges are and where they go to.
     *
     */
    public void buildEdges()
    {
        long startTime = System.currentTimeMillis();
        int edgeCount = 0;
        // We're going to iterate over all the nodes in the graph. I knew a LinkedHashMap was a good idea.
        System.out.println("Building connections between nodes");
        for (Node node: nodes.values() )
        {
            Set<String> dirs = new TreeSet<String>(Arrays.asList(new String[]{"R", "RD", "D", "LD", "L", "LU", "U", "RU"})); 
            int x = node.getX();
            int y = node.getY();
            // now we know where this node is.

            Set<String> blockedDirs = new TreeSet<String>();
            // first we check if we can do nothing, to get this done faster. if we get through, the node is in the middle of the grid, all directions are valid.
            if ( !(gridSize < x && x > 1) && !(gridSize < y && y > 1) ) { 

                //Evaluating the x...
                if ( gridSize == x) {
                    // this means we're on the rightmost edge
                    blockedDirs.addAll(Arrays.asList(new String[]{"R", "RD", "RU"}));
                }
                else if (x == 1) {
                    // This means we're on the left
                    blockedDirs.addAll(Arrays.asList(new String[]{"LD", "L", "LU"}));
                }

                // Evaluating the y...
                if ( gridSize == y) {
                    //this means we're on the bottom of the grid
                    blockedDirs.addAll(Arrays.asList(new String[]{"RD", "D", "LD"}));
                }
                else if (y == 1) {
                    // This means we're at the top
                    blockedDirs.addAll(Arrays.asList(new String[]{"LU", "U", "RU"}));
                }
            }
            dirs.removeAll(blockedDirs);
            // Now we have the list of valid directions.
            // We're going to loop through them all, see where they go, make an edge for each, put the edge into the node.
            for (String dir:dirs)
            {
                int targetX = x;
                int targetY = y; // Initialising these to the node's own location, because it makes the next part look nicer.
                switch (dir){
                    case "R":
                    targetX++;
                    break;
                    case "RD":
                    targetX++;
                    targetY++;
                    break;
                    case "D":
                    targetY++;
                    break;
                    case "LD":
                    targetX--;
                    targetY++;
                    break;
                    case "L":
                    targetX--;
                    break;
                    case "LU":
                    targetX--;
                    targetY--;
                    break;
                    case "U":
                    targetY--;
                    break;
                    case "RU":
                    targetX++;
                    targetY++;
                    break;
                    default:
                    System.err.println("Got an invalid direction while building edges. Something went very wrong.");
                    break;
                }
                //Turning the target coordinates we got into a string
                String targetCoords = targetX + "," + targetY;
                //Using that string to pull our target node out of the grid to pass to the edge
                Edge newEdge = new Edge(dir, nodes.get(targetCoords));
                node.addEdge(newEdge);
                if (Driver.verbose()){
                System.out.println("(" + node.getCoords() + "): New edge has been added to the " + dir + " leading to (" + targetCoords + ")" );}
                edgeCount++;
            }
            // When we get to here we should have added all the edges for this node
            if (Driver.verbose()){
            System.out.println("(" + node.getCoords() + "): =======> All edges for this node added.");}
        }
        //When we get to here we should have processed all the nodes.
        System.out.println("### All nodes have been processed! ###");
        long endTime = System.currentTimeMillis();
        long processTime = (endTime - startTime);
        System.out.println("Found " + edgeCount + " edges. (" + processTime + "ms)");
    }
    
    
    
    /**
     * Finds and returns the start node.
     *
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y
     */
    public Node findStart()
    {
        // put your code here
        Node startNode = null; // I know this is bad but i'm tired ok.
        for (Node node: nodes.values())
        {
            if (node.getNodeType() == 'S') {
                startNode = node;}
            
        }
        return startNode;
    }
}
