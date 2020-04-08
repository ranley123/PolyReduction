public class ThreeSAT_TO_GraphColoring {
    public static void main(String[] args){
        if(args.length < 1){
            System.out.println("Please use java ThreeSAT_TO_GraphColoring [input filename]");
        }
        String inputFile = args[0];
//        String outputFile = args[1];

        // starts to read file
        Parser parser = new Parser(inputFile);
        SAT sat = parser.readFile(inputFile);
        ThreeSAT sat3 = new ThreeSAT();
        sat3.setNumVar(sat.getNumVar());
        sat3.setNumClause(sat.getClauses().size());
        sat3.setClauses(sat.getClauses());

        GraphColor graphColor = Reduction.reduce3SATToGraphColoring(sat3);

//        graphColor.print();
//        sat3.print();
    }
}
