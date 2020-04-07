public class SAT_TO_3SAT {


    public static void main(String[] args){
        if(args.length < 1){
            System.out.println("Please use java SAT_TO_3SAT [input filename]");
        }
        String inputFile = args[0];
//        String outputFile = args[1];

        // starts to read file
        Parser parser = new Parser(inputFile);
        SAT sat = parser.sat;
        ThreeSAT sat3 = Reduction.ReduceSATTo3SAT(sat);
        sat3.print();
    }
}
