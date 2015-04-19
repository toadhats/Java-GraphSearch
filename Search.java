import java.util.*;
/**
 * This is the brains of the operation. All the real search logic happens here
 * 
 * @author (Jonathan) 
 */
public class Search
{
    // instance variables - replace the example below with your own
    private String strategy;
    private int diagIterations;
    private Graph graph;
    private LinkedList<Node> open;
    private Set<Node> closed;
    private OutputBlock output;
    private String position; // We keep track of where we are via the coordinates.
    private String pathToHere; // This changes every time we 'move' i.e. expand a node. Used to print diag.
    private String goalNode; // Coordinates of the goal node, used to calculate heuristic.

    /**
     * Constructor for objects of class Search
     */
    public Search(String strat, int iterations, Graph newGraph)
    {
        // initialise instance variables
        strategy = strat;
        diagIterations = iterations;
        graph = newGraph;
        open = new LinkedList<Node>();
        closed = new LinkedHashSet<Node>();
        output = new OutputBlock(strategy, diagIterations, graph.getGridSize());
        position = graph.findStart().getCoords();
        pathToHere = "S";

        // We can add the starting node to the open set now.
        open.add(graph.findStart());

    }

    /**
     * When this method is called, we start the search for a path. DiagBlocks are generated and kept 
     * in an OutputBlock, which is returned by the method at the end.
     *
     */
    public OutputBlock run()
    {
        long startTime = System.currentTimeMillis();
        // This is mainly to make sure we made it this far

        switch (strategy)
        {
            case "D": System.out.println("Initiating Depth-First search..."); break;
            case "B": System.out.println("Initiating Breadth-First search..."); break;
            case "U": System.out.println("Initiating Uniform Cost search..."); break;
            case "A": System.out.println("Initiating A/A* search..."); break;
            default: System.out.println("Search strategy not available, something broke."); break;
        }
        boolean finished = false;
        // We want to run until we're done
        while (!finished)
        {
            Node n = graph.getNode(position);
            if (Driver.verbose()) {
                System.out.println("# Looking at node (" + n.getCoords() + ") #");
                System.out.println("Type: " + n.getNodeType() + " | g: " + n.getCost() + " | h: " 
                    + n.getHeuristic() + " | f: " + n.getFn() );
            }

            // Grabbing neighbours
            LinkedHashMap<String,Edge> neighbours = n.getEdges();

            if (n.getNodeType() == 'G')
            {
                // If we're on the goal node, we finish and start packing up our output
                // If we were using BFS or DFS we won't get here, because those strategies finish when they 
                // see the goal, not when they select it for expansion.
                System.out.println("Found the goal node at position (" + n.getCoords() + ")");
                finished = true;
            }
            else 
            {
                // Adding this node to the closed set
                closed.add(n);
                // Removing it from the open set.
                open.remove(n);

                // Now adding children of this node to the open set
                for (Edge neighbour: neighbours.values())
                {
                    // Checking if this neighbour is already in the closed set.
                    if (!closed.contains(neighbour.getDest())) {
                        // It's not in the closed set.
                        switch (strategy)
                        {
                            case "B":
                            case "D":
                            neighbour.getDest().setCost(1);
                            break;
                            case "U":
                            case "A":
                            neighbour.getDest().setCost(n.getCost() + neighbour.getEdgeCost());
                        }
                        // We've given it a cost based on the strategy we're using, now we add it to the open set.
                        open.addLast(neighbour.getDest());
                    }

                }
                // Done adding neighbours to the open set
            }
            // Done with this node
            selectNextNode();
        }
        // And we're done, returning the output block now.
        long endTime = System.currentTimeMillis();
        long processTime = (endTime - startTime);
        System.out.println("Took " + processTime + "ms");
        return output;
    }

    /**
     * Selects the appropriate next node to expand from the open set
     *
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y
     */
    public void selectNextNode()
    {
        //Which node we pick from the queue depends on the strategy we're using
        Node nextNode;
        switch (strategy)
        {
            case "B": 
            nextNode = open.getFirst(); break;
            case "D": 
            nextNode = open.getLast(); break;
            case "U": 
            sortUCS(); 
            nextNode = open.getFirst();
            break;
            case "A": 
            sortA();
            nextNode = open.getFirst();
            break;
            default: System.out.println("Search strategy not available, something broke."); nextNode = null; break;
        }
        position = nextNode.getCoords();
        // When this is called, the pathfinding loop will repeat with a new position.
    }

    /**
     * Sorts the open set by g(n) - for a Uniform Cost Search
     *
     */
    public void sortUCS()
    {
        // put your code here
        Collections.sort(open, Node.GComparator);
    }

    /**
     * Sorts the open set by f(n), used for A/A*
     *
     */
    public void sortA()
    {
        // put your code here
        Collections.sort(open, Node.FComparator);
    }

}
