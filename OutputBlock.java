import java.util.*;
/**
 * A container for all the info that goes into an output file.
 * 
 */
public class OutputBlock
{
    // instance variables - replace the example below with your own
    private String path;
    private List<DiagBlock> diagBlocks;

    /**
     * Constructor for objects of class OutputBlock
     */
    public OutputBlock(String strategy, int debugIterations, int mapSize)
    {
        // initialise instance variables
        diagBlocks = new ArrayList<DiagBlock>();
    }

    /**
     * Gets the path we stored here.
     *
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y
     */
    public String getPath()
    {
        // put your code here
        return path;
    }

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y
     */
    public void setPath(String finalPath)
    {
        // put your code here
        path = finalPath;
    }

    /**
     * Gets the diag blocks out ready to be printed to the output file.
     *
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y
     */
    public ArrayList<String> getDiagBlocks()
    {
        ArrayList<String> temp = new ArrayList<String>();
        for (DiagBlock block: diagBlocks)
        {
            temp.add(block.toString());
        }
        return temp;
    }

    /**
     * Adds a diag blod to the output block
     *
     * @param  db   a diag block
     */
    public void addDiagBlock(DiagBlock db)
    {
        diagBlocks.add(db);
    }

    
}
