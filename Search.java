import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * This is the brains of the operation. All the real search logic happens here
 * 
 * @author (Jonathan)
 */
public class Search {
  // instance variables - replace the example below with your own
  private String strategy;
  private int diagIterations;
  private int currentIterations;
  private Graph graph;
  private LinkedList<Node> open;
  private Set<Node> closed;
  private OutputBlock output;
  private List<DiagBlock> diagQueue;
  private String position; // We keep track of where we are
  // at,
  private String path; // The final path.

  /**
   * Constructor for objects of class Search
   * 
   * @param strat
   * @param iterations
   * @param newGraph
   */
  public Search(String strat, int iterations, Graph newGraph) {
    // initialise instance variables
    strategy = strat;
    diagIterations = iterations;
    currentIterations = 0;
    graph = newGraph;
    open = new LinkedList<>();
    closed = new LinkedHashSet<>();
    output = new OutputBlock(strategy, diagIterations, graph.getGridSize());
    diagQueue = new LinkedList<>();
    position = graph.findStart().getCoords();
    // path = new StringBuilder("S");

    // We can add the starting node to the open set now.
    open.add(graph.findStart());

  }

  /**
   * When this method is called, we start the search for a path. DiagBlocks are generated and kept
   * in an OutputBlock, which is returned by the method at the end.
   * 
   * @return an outputblock containing the output of the search
   * 
   */
  public OutputBlock run() {
    long startTime = System.currentTimeMillis();
    boolean diag = false; // We'll flip this back if we need to.
    switch (strategy) {
      case "D":
        System.out.println("Initiating Depth-First search...");
        break;
      case "B":
        System.out.println("Initiating Breadth-First search...");
        break;
      case "U":
        System.out.println("Initiating Uniform Cost search...");
        break;
      case "A":
        System.out.println("Initiating A/A* search...");
        break;
      default:
        System.out.println("Search strategy not available, something broke.");
        break;
    }
    boolean finished = false;
    // We want to run until we're done
    while (!finished) {
      // Time to expand the node we've chosen (or started at)
      currentIterations++; // Increment iterations
      // Checking if we need diagnostics
      if (currentIterations <= diagIterations) {
        diag = true;
      } else {
        diag = false;
      }

      Node currentNode = graph.getNode(position);

      if (Driver.verbose()) {
        System.out.println("# Looking at node (" + currentNode.getCoords() + ") #");
        System.out
            .println("Type: " + currentNode.getNodeType() + " | g: " + currentNode.getCost()
                + " | h: " + currentNode.getHeuristic() + " | f: " + currentNode.getFn()
                + " | Parent: (" + currentNode.getCameFrom() + ")\n" + "Path: "
                + currentNode.getPath());
      }

      // If we need diagnostics prepare a block
      if (diag) {
        DiagBlock thisDiag = new DiagBlock();
        thisDiag.addLine(currentNode.getPath() + " " + currentNode.getCost() + " "
            + currentNode.getHeuristic() + " " + currentNode.getFn());
        StringBuilder sb1 = new StringBuilder();
        sb1.append("OPEN");
        for (Node diagOpenNode : open) {
          sb1.append(" " + diagOpenNode.getPath());
        }
        thisDiag.addLine(sb1.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("CLOSED");
        for (Node diagClosedNode : closed) {
          sb2.append(" " + diagClosedNode.getPath());
        }
        thisDiag.addLine(sb2.toString());
        diagQueue.add(thisDiag);
      }
      // Grabbing neighbours
      LinkedHashMap<String, Edge> neighbours = currentNode.getEdges();

      if (currentNode.getNodeType() == 'G') {
        // If we're on the goal node, we finish and start packing up our
        // output
        // If we were using BFS or DFS we won't get here, because those
        // strategies finish when they
        // see the goal, not when they select it for expansion.
        System.out.println("Found the goal node at position (" + currentNode.getCoords() + ")");

        finished = true;
        path = currentNode.getPath() + "-G " + currentNode.getRealCost();
        if (Driver.verbose()) {
          System.out.println("Final path: " + path);
        }
        output.setPath(path);
        // put the path into the output block
      } else {
        // Adding this node to the closed set
        closed.add(currentNode);
        // Removing it from the open set.
        open.remove(currentNode);

        // Now adding children of this node to the open set
        for (Edge neighbour : neighbours.values()) {
          // Checking if this neighbour is already in the closed set.
          if (!(closed.contains(neighbour.getDest()) || open.contains(neighbour.getDest()))) {
            // It's not in the closed set.

            if (Driver.verbose()) {
              System.out.println("--> Looking at neighbour (" + neighbour.getDest().getCoords()
                  + ")");
            }
            neighbour.getDest().cameFrom(position);
            neighbour.getDest().setPath(currentNode.getPath() + "-" + neighbour.getDirection());
            neighbour.getDest().setRealCost(currentNode.getRealCost() + neighbour.getEdgeCost());

            switch (strategy) {
              case "B":
              case "D":
                neighbour.getDest().setCost(currentNode.getCost() + 1);
                break;
              case "U":
              case "A":
                neighbour.getDest().setCost(currentNode.getCost() + neighbour.getEdgeCost());
                break;
              default:
                System.err.println("Something has gone terribly wrong with the strategy again.");
                break;
            }
            // We've given it a cost based on the strategy we're, now we add it to the open set.
            open.addLast(neighbour.getDest());
            // Maybe we found the goal, we can do this in BFS and DFS
            if (((strategy == "B") || (strategy == "D"))
                && (neighbour.getDest().getNodeType() == 'G')) {
              // If we see the goal while doing BFS or DFS, we end
              // here
              System.out.println("Found the goal node while looking at neighbour ("
                  + currentNode.getCoords() + ")");

              finished = true;
              path = neighbour.getDest().getPath() + "-G " + currentNode.getRealCost();
              if (Driver.verbose()) {
                System.out.println("Final path: " + path);
              }
              output.setPath(path);
              // Putting the path into the output block
            }
          }

        }
        // Done adding neighbours to the open set
      }
      // Done with this node
      if (!finished) {
        selectNextNode();
      }

    }
    // And we're done, returning the output block now.
    long endTime = System.currentTimeMillis();
    long processTime = (endTime - startTime);
    System.out.println("Took " + processTime + "ms");
    for (DiagBlock db : diagQueue) {
      output.addDiagBlock(db);
    }
    return output;
  }


  /**
   * Gets the coordinates of the node to be expanded
   * 
   */
  public void selectNextNode() {
    // Which node we pick from the queue depends on the strategy we're using
    Node nextNode;
    switch (strategy) {
      case "B":
        nextNode = open.getFirst();
        break;
      case "D":
        nextNode = open.getLast();
        break;
      case "U":
        sortUcs();
        nextNode = open.getFirst();
        break;
      case "A":
        sortA();
        nextNode = open.getFirst();
        break;
      default:
        System.out.println("Search strategy not available, something broke.");
        nextNode = null;
        break;
    }
    if (nextNode == null) {
      System.out.println("Didn't get a node from the open set - it's probably empty.");
    } else {
      position = nextNode.getCoords();
    }


    /*
     * When this is called, the pathfinding loop will be about to repeat with a new position.
     */
  }

  /*
   * Sorts the open set by g(n) - for a Uniform Cost Search
   */
  public void sortUcs() {
    Collections.sort(open, Node.GComparator);
  }

  /*
   * Sorts the open set by f(n), used for A/A*
   */
  public void sortA() {
    // put your code here
    Collections.sort(open, Node.FComparator);
  }

}
