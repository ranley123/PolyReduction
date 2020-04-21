import DataStructure.Clique;
import DataStructure.SAT;
import DataStructure.ThreeSAT;
import DataStructure.VertexCover;

public class ThreeSAT_TO_VertexCover {
    public static void main(String[] args){
        if(args.length < 1){
            System.out.println("Please use java ThreeSAT_TO_VertexCover [input filename]");
        }
        String inputFile = args[0];

        Parser parser = new Parser(inputFile);
        SAT sat = parser.readFile(inputFile);
        ThreeSAT sat3 = Reduction.reduceSATTo3SAT(sat);

        Clique clique = Reduction.reduce3SATToClique(sat3);
        VertexCover vertexCover = Reduction.reduceCliqueToVertexCover(clique);
//        clique.print();
        vertexCover.print();
    }
}
