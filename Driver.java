
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This driver class is used to help students validate their input/output file
 * and make sure that it conforms to the assignment requirements
 * 
 * Copyright reserved.
 * 
 * Monash University, 2015
 * 
 * Owner: Ingrid Zukerman, Ilana Lichtenstein and Xuhui Zhang
 * 
 * Updated by: A. K. M. Azad
 * 
 * This file is available only for students of FIT5047.
 *  
 * */

public class Driver {
    public static boolean verboseMode = true; // Toggles between full and partial output.
    // The number of iteration specified in the input file, for which diagnostic information should be printed:
    private static int iterationNum;
    // Making these static so they can be initialised in the main method and used in CheckOutfileFormat() later
    static String inputFile;
    static String outputFile;

    public static void main(String[] args) throws IOException {
        // Convert your input file into an array:
        ArrayList<String> inputArray = new ArrayList<String>();
        // Going to work out our input and output files to save hassle later.
        inputFile = args[0];
        outputFile = args[1];
        // For debug purposes mainly
        System.out.println("Input file: " + inputFile);
        System.out.println("Output file: " + outputFile);
        /**
         * STEP 1:
         * 
         * Read your test file from your local machine
         * 
         * */
        FileInputStream fstream;
        try {
            fstream = new FileInputStream(inputFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String strLine;

            System.out.println("Your input file:");
            System.out.println("****************");
            //Read File Line By Line
            while ((strLine = br.readLine()) != null)   {
                // Print the content on the console
                System.out.println(strLine);
                inputArray.add(strLine);
            }

            System.out.println("****************");
            //Close the input stream
            br.close();

            /**
             * STEP 2:
             * 
             * Read your test file and check its format
             * 
             * */
            CheckInputFileFormat(inputArray);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        /**
         * STEP 3:
         * 
         * Call you own classes and functions
         * 
         * Generate a output file which contains the result.
         * 
         * The format of your result is checked by running 
         * 
         * the following code:
         * 
         */
         // Maybe write a method that gets the strategy out of the array, and save the strategy to a string
         String strategy = inputArray.get(0);
         // Only do this if i end up needing it, delete otherwise
        
         //Loading the grid from the input...
         int graphSize = Integer.parseInt(inputArray.get(2));
         Graph graph = new Graph(graphSize);
         graph.loadGraph(inputArray);
         graph.buildEdges();
         Search search = new Search(strategy, iterationNum, graph);
         OutputBlock output = search.run();
        
        //writeOutput(output);
        // Now we're checking the output file, fingers crossed... 
        System.out.println("****************");
        System.out.println("Checking output file format...");
        CheckOutfileFormat(inputArray);

    }
    
    /**
     * Writes an output block to file
     *
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y
     */
    public void writeOutput(OutputBlock output)
    {
        // put your code here

    }

    
    /**
     * Tells if we're running in verbose mode or not
     *
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y
     */
    public static boolean verbose()
    {
        // put your code here
        return verboseMode;
    }

    
    /**************************************
     * File checking functions start here *
     **************************************/
     
     
    // The function to check format of input file:
    private static void CheckInputFileFormat(ArrayList<String> inputArray)
    {

        /**
         * For an acceptable input file:
         * 
         * First line: a single letter (D, B, U or A)
         * 
         * Second line: a non-negative number (number of iterations)
         * 
         * Third line: a non-negative number (the map row or column number)
         * 
         * Fourth line & onwards : a sequence of letters (R, X, S or G) and the length is depends on the 
         *                         number specified in the third line.
         * 
         * */

        String singleLetterExpression = "[DBUA]";
        String positiveNumExpression = "(^[1-9]\\d*$)|(0)";

        // Check whether there is empty lines in the end of the file:
        if(inputArray.get(inputArray.size()-1).trim().equals(""))
            System.out.println("Please remove the empty line in the end of input file!");

        // Check every line to make sure there is no empty one:
        for(String line: inputArray)
        {
            if(line.trim().equals(""))
            {
                System.out.println("Empty line exists!");
                return;
            }
        }

        // Check the first line:
        Pattern pattern = Pattern.compile(singleLetterExpression);
        Matcher matcher = pattern.matcher(inputArray.get(0));       
        if(matcher.matches() == false)
            System.out.println("Incorrect format for first line in the input file!");

        // Check the second line:
        pattern = Pattern.compile(positiveNumExpression);
        matcher = pattern.matcher(inputArray.get(1));
        if(matcher.matches() == false)
            System.out.println("Incorrect format for second line in the input file!");

        // Check the third line:
        pattern = Pattern.compile(positiveNumExpression);
        matcher = pattern.matcher(inputArray.get(2));
        if(matcher.matches() == false)
            System.out.println("Incorrect format for third line in the input file!");

        // Check the total line number and map description:
        if(matcher.matches() == true)
        {
            // The total line number should be (3 + mapDimensionNum):
            int mapDimensionNum = Integer.parseInt(inputArray.get(2));
            String mapRowLettersExpression = "[RXSG]{"+mapDimensionNum+"}";
            if(inputArray.size() != mapDimensionNum+3)
                System.out.println("Incorrect number of total lines!");
            pattern = Pattern.compile(mapRowLettersExpression);
        }   

        int startingTileNum = 0;
        int goalTileNum = 0;
        boolean mapValid = true;
        // Check every row of the map description:
        for(int i = 3; i < inputArray.size(); i++)
        {
            matcher = pattern.matcher(inputArray.get(i));
            //boolean isCorrect = true;
            if(matcher.matches() == false)
            {
                System.out.println("Incorrect map description!");
                mapValid = false;
                break;           
            }
            else
            {
                for(int n = 0; n < inputArray.get(i).length(); n++)
                {
                    if(inputArray.get(i).charAt(n) == 'S')
                        startingTileNum++;
                    if(inputArray.get(i).charAt(n) == 'G')
                        goalTileNum++;
                }

            }
        }
        // If the map description is valid, check how may 'S' and 'G' are there:
        if(mapValid == true)
        {
            // There should be one and only one starting and goal tile in the map:
            if(startingTileNum != 1 || goalTileNum != 1)
                System.out.println("There should be one and only one starting tile or goal tile!");
        }

    }

    // The function to check output file:
    /* Check number of lines of file is 3n+1, where n is line 2 of input file
     * check format of path and of diagnostic lines
     */
    private static void CheckOutfileFormat(ArrayList<String> inputArray) throws IOException
    {
        boolean isCorrect = true;

        String positiveNumExpression = "(^[1-9]\\d*$)|(0)";

        ArrayList<String> outputArray = new ArrayList<String>();

        FileInputStream fstream;

        try {
            fstream = new FileInputStream(outputFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String strLine;

            System.out.println("Your output file:");
            System.out.println("****************");
            //Read File Line By Line
            while ((strLine = br.readLine()) != null)   {
                // Print the content on the console
                System.out.println(strLine);
                outputArray.add(strLine);
            }

            System.out.println("****************");
            //Close the input stream
            br.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 

        
        // Check whether there is empty lines in the end of the file:
        if(outputArray.get(outputArray.size()-1).trim().equals(""))
            System.out.println("Please remove the empty line in the end of output file!");

        // Check every line to make sure there is no empty one:
        for(String line: outputArray)
        {
            if(line.trim().equals(""))
            {
                System.out.println("Empty line exists!");
                //return;
            }
        }

        // Check the number of lines in output file:
        Integer NumDiagnosticLines = null;
        Pattern pat = Pattern.compile(positiveNumExpression);
        Matcher mat = pat.matcher(inputArray.get(1));
        if(mat.matches() == false)
        {
            System.out.println("Incorrect format for second line in the input file!");
            return;
        }
        if(mat.matches() == true)
        {
            NumDiagnosticLines = Integer.parseInt(inputArray.get(1));
        }

        Integer NumExpectedLinesInOutput = 3 * NumDiagnosticLines + 1; //initialise

        Integer NumLinesInOutput = outputArray.size();

        //Compare expected number against actual number of lines in output
        if(!NumExpectedLinesInOutput.equals(NumLinesInOutput)){
            System.out.println("Total number of lines in output file is not correct. \nThe number of diagnostic lines given in the input file is " + NumExpectedLinesInOutput +
                ". but your output file has " + NumLinesInOutput + " lines." );

            return;
        }

        // Prepare for regular expression matching
        String actions = "(S)|(R)|(RD)|(D)|(LD)|(L)|(LU)|(U)|(RU)|(G)";     
        Pattern pattern = Pattern.compile(actions);
        String splitAt = " ";  //whitespace

        // Check first line of output (result and path cost):   
        String result = outputArray.get(0);
        //System.out.println("Path result is " + result);
        String[] tokens = result.split(splitAt); // tokenize by whitespace
        //*******************

        if(!result.trim().equals("NO-PATH")) // if the first line is not 'NO-PATH'
        {
            if(tokens.length!=2)
            {
                System.out.println("Format error: First line should be 'NO-PATH' or resulting path and its cost (case sensitive)");
                return;
            }

            // Check actions on path
            String resultActions[]=tokens[0].split("-");

            //checkPathActions(inputActions, pattern);
            int S_num = 0;
            int G_num = 0;
            for (String k: resultActions){ // for state in path
                //System.out.println("action is:" + k);
                Matcher matcher = pattern.matcher(k);
                if(matcher.matches() == false)
                {
                    System.out.println("Incorrect format for first line in the output file!. Path does not match regular expressions.");
                }
                // Get the number of S and G:
                if(k.equals("S"))
                    S_num++;
                if(k.equals("G"))
                    G_num++;                
            }

            if(S_num !=1 || G_num!=1)
                System.out.println("Sorry, the path should contain one and only one S or G");

            // Check cost integer 
            //Integer cost = Integer.parseInt(tokens[1]);
            //System.out.println("cost is " + cost);
            //if(!(cost instanceof Integer)){
            //  System.out.println("Incorrect format for first line in the input file!. Cost is not an integer.");          
            //  return;
            //}

            // Use regular expression to do this job or it will crash if the token is not a number:
            Pattern pattern1 = Pattern.compile(positiveNumExpression);
            Matcher matcher = pattern1.matcher(tokens[1]);      
            if(matcher.matches() == false)
            {
                System.out.println("Incorrect format for first line in the output file!. Cost is not a non-negative integer.");
                return;
            }
        }

        ////******************************

        // For search methods B, D, U, A
        int i = 1;
        while(i < outputArray.size())
        {
            //Line 1 - Path to node being expanded
            String pathToCurrentNode=outputArray.get(i); // get line with e.g. S 0 0 0
            tokens = pathToCurrentNode.split(splitAt); // tokenize by whitespace
            if(tokens.length != 4)
            {
                System.out.println("Format error: First Diagnosis line should contain only 4 infomation: Expanded_node, g, h, f");
                return;
            }
            String path=tokens[0]; //get path e.g. R-SD
            String actionsCurrentNode[]=path.split("-");
            for (String k: actionsCurrentNode){ // for state in path
                //System.out.println("k is:" + k);
                Matcher matcher = pattern.matcher(k);
                if(matcher.matches() == false)
                    System.out.println("Incorrect format for PATH to current expanded NODE on line " + (i+1));

            }

            //              Integer g=Integer.parseInt(tokens[1]); //get  g
            //              Integer h=Integer.parseInt(tokens[2]); //get  h
            //              Integer f=Integer.parseInt(tokens[3]); //get  f
            //              System.out.println("f,g,h is: " + g + " " + h + " " + f);
            //
            //              if(!(g instanceof Integer) || !(h instanceof Integer) || !(f instanceof Integer)){
            //                  System.out.println("Incorrect format for first line in the input file!. Cost is not an integer.");          
            //                  return;
            //              }

            // Use regular expression to do this job or it will crash if the token is not a number:
            Pattern pattern1 = Pattern.compile(positiveNumExpression);
            Matcher matcherG = pattern1.matcher(tokens[1]);
            Matcher matcherH = pattern1.matcher(tokens[2]);
            Matcher matcherF = pattern1.matcher(tokens[3]);
            if(matcherG.matches()== false || matcherH.matches()== false || matcherF.matches()== false)
            {
                System.out.println("Incorrect format for g, h or f in the input file!. Not an integer.");
                return;
            }

            //Line 2 - Open List
            i=i+1; // get OPEN
            String openLine=outputArray.get(i); // get line with e.g. OPEN f
            System.out.println(openLine);

            tokens = openLine.split(splitAt); // tokenize by whitespace

            if(!tokens[0].equals("OPEN")){ 
                System.out.println("Formatting error. Line should begin with OPEN");
            }

            for(int j=1;j<tokens.length; j++) // for node in open list
            {
                path=tokens[j]; //get path e.gR-SD
                String openActions[]=path.split("-");
                for (String k: openActions){ // for state in path
                    //System.out.println("k is:" + k);
                    Matcher matcher = pattern.matcher(k);
                    if(matcher.matches() == false)
                        System.out.println("Incorrect format for OPEN list on line " + (i+1));

                }

            }

            //Part 3 Closed List
            i=i+1; 
            String closedLine=outputArray.get(i); // get line with e.g. OPEN f
            tokens = closedLine.split(splitAt);
            System.out.println("closed is " + tokens[0]);
            if(!tokens[0].equals("CLOSED")){ 
                System.out.println("Formatting error. Line should begin with CLOSED");
            }

            for(int j=1;j<tokens.length; j++) // for node in open list
            {
                path=tokens[j]; //get path R-SD
                String closedActions[]=path.split("-");
                for (String k: closedActions){ // for state in path
                    //System.out.println("action is:" + k);
                    Matcher matcher = pattern.matcher(k);
                    if(matcher.matches() == false)
                        System.out.println("Incorrect format for CLOSED list on line " + (i+1));

                }

            }
            // Increment i to start again
            i=i+1;

            //return isCorrect;
        }
    }
    // Why is this here?
    /*
    private boolean checkPathActions(ArrayList<String> actionList, Pattern pattern){

    boolean trueActionList = true;

    for (String k: actionList){ // for state in path
    System.out.println("k is:" + k);
    Matcher matcher = pattern.matcher(k);
    if(matcher.matches() == false)
    System.out.println("Incorrect format for second line in the input file!");
    trueActionList = false;     
    }

    return trueActionList;

    }*/

}
