public class GraphColoring_TO_SAT {
    public static void main(String[] args){
        if(args.length < 1){
            System.out.println("Please use java GraphColoring_TO_SAT [input filename]");
        }
        String inputFile = args[0];
//        String outputFile = args[1];

        // starts to read file
        Parser parser = new Parser(inputFile);
        GraphColor graphColor = parser.readGraph(inputFile);
        SAT sat = Reduction.reduceGraphColoringToSAT(graphColor);
//        graphColor.print();
        sat.print();
    }
}
