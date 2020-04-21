import DataStructure.GraphColor;
import DataStructure.SAT;
import DataStructure.ThreeSAT;

public class CircularTest {
    public static void main(String[] args){
        String filename = "./src/graph.txt";
        Parser parser = new Parser(filename);


        GraphColor graphColor = parser.readGraph(filename);
        graphColor.print();

        SAT res = Reduction.reduceGraphColoringToSAT(graphColor);
        res.print();

        ThreeSAT sat3 = Reduction.reduceSATTo3SAT(res);
        sat3.print();
    }
}
