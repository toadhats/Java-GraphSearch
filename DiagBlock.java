import java.util.*;
/**
 * A container for diagnostic information for a single iteration 
 * 
)
 */
public class DiagBlock
{
    // This is an extremely basic object
    private List<String> diagLines;

    /**
     * Constructor for objects of class DiagBlock
     */
    public DiagBlock()
    {
	// initialise instance variables
	diagLines = new ArrayList<String>(3);
    }

    /**
     * An example of a method - replace this comment with your own
     * @param line 
     *
     */
    public void addLine(String line)
    {
	diagLines.add(line);
    }


    /**
     * Returns a multi-line string, which is a full 3 line diagnostic block.
     *
     * @return     A diagnostic block as a string.
     */
    public String toString()
    {
	// put your code here
	StringBuilder sb = new StringBuilder();
	sb.append(diagLines.get(0));
	sb.append("\n");
	sb.append(diagLines.get(1));
	sb.append("\n");
	sb.append(diagLines.get(2));
	// do we need another newline here?
	return sb.toString();
    }

}
