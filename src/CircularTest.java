import DataStructure.GraphColor;
import DataStructure.SAT;
import DataStructure.ThreeSAT;

import java.util.Collection;

public class CircularTest {
    public static void main(String[] args){
        // the most typical example is 4, since the result file would not be too large.
        // by using 4, you can have a circular test: SAT to 3-SAT to Graph Color
        NegativeSATGenerator generator = new NegativeSATGenerator(2);

        String filename = "./src/input.txt";
        Parser parser = new Parser(filename);

        SAT sat = parser.readFile(filename);
        ThreeSAT sat3 = Reduction.reduceSATTo3SAT(sat);
        GraphColor graphColor = Reduction.reduce3SATToGraphColoring(sat3);
        graphColor.output("output.col");
    }
}
