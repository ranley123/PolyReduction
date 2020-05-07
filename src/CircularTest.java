import DataStructure.GraphColor;
import DataStructure.SAT;
import DataStructure.ThreeSAT;

import java.util.Collection;

public class CircularTest {
    public static void main(String[] args){
        // the most typical example is 4, since the result file would not be too large.
        // by using 4, you can have a circular test: SAT to 3-SAT to Graph Color
//        NegativeSATGenerator generator = new NegativeSATGenerator(2);

        String satFilename = "sat.cnf";
        String graphFilename = "graph.col";

        SAT_TO_3SAT sat_to_3SAT = new SAT_TO_3SAT();
        sat_to_3SAT.main(new String[] {satFilename, graphFilename});

    }
}
