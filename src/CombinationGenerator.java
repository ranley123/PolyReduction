import java.util.ArrayList;

public class CombinationGenerator {
    ArrayList<Pair> pairs;
    ArrayList<Integer> vars;

    public CombinationGenerator(ArrayList<Integer> vars){
        pairs = new ArrayList<>();
        this.vars = vars;
        makeCombinations(0);
    }

    /**
     * generate all subsets of a list of vars with size 2 by backtracking
     * @param start
     */
    private void makeCombinations(int start){
        if(start >= vars.size() - 1)
            return;
        for(int i = start + 1; i < vars.size(); i++){
            pairs.add(new Pair(vars.get(start), vars.get(i)));
        }
        makeCombinations(start + 1);
    }

    public ArrayList<Pair> getPairs(){
        return pairs;
    }

    public class Pair{
        int var1;
        int var2;

        public Pair(int var1, int var2){
            this.var1 = var1;
            this.var2 = var2;
        }
    }
}
