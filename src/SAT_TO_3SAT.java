import DataStructure.SAT;
import DataStructure.ThreeSAT;

public class SAT_TO_3SAT {
    public static void main(String[] args){
        if(args.length != 2){
            System.out.println("Please use java SAT_TO_3SAT [input filename] [output filename]");
            return;
        }
        String inputFile = args[0];
        String outputFile = args[1];

        // starts to read file
        Parser parser = new Parser(inputFile);
        SAT sat = parser.readFile(inputFile);
        ThreeSAT sat3 = Reduction.reduceSATTo3SAT(sat);
//        sat3.print();
        sat3.output(outputFile);
    }

}
