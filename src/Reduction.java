import java.util.ArrayList;
public class Reduction {
    static final int ZERO = 0;
    static final int ONE = 1;
    static final int TWO = 2;
    static int offset;

    public static ThreeSAT reduceSATTo3SAT(SAT sat){
        ThreeSAT sat3 = new ThreeSAT();
        ArrayList<Clause> clauses = sat.getClauses();
        offset = sat.getNumVar() + 1;
        sat3.setNumVar(sat.getNumVar());

        for(int i = 0; i < clauses.size(); i++){
            Clause curClause = clauses.get(i);

            if (curClause.getNumLiteral() <= 3){
                sat3.addClause(curClause);
            }

            else{
                to3SATClauses(sat3, curClause);
            }
        }
        sat3.setNumClause(sat3.getClauses().size());
        return sat3;
    }


    public static void to3SATClauses(ThreeSAT sat3, Clause curClause){
        ArrayList<Integer> curLiterals = curClause.getLiterals();

        if(curClause.getNumLiteral() <= 0){
            return;
        }
        else if (curClause.getNumLiteral() <= 3){
            sat3.addClause(curClause);
            return;
        }

        int limit = Math.min(2, curLiterals.size());

        // the first clause
        Clause newClause = new Clause();
        newClause.addLiteral(offset);
        for(int i = 0; i < limit; i++){
            newClause.addLiteral(curLiterals.get(i));
        }
        sat3.addClause(newClause);

        // the remaining clause
        Clause leftClause = new Clause();
        leftClause.addLiteral(offset * -1);
        offset++;
        for(int i = limit; i < curLiterals.size(); i++){
            leftClause.addLiteral(curLiterals.get(i));
        }

        to3SATClauses(sat3, leftClause);

    }

    public static SAT reduceGraphColoringToSAT(GraphColor graphColor){
        SAT sat = new SAT();
        int numNode = graphColor.getNumNode();
        int numColor = graphColor.getNumColor();
        int numVar = numNode * graphColor.getNumColor();

        // add type 1 clause: endpoints of each edge not share same color
        ArrayList<Edge> edges = graphColor.getEdges();
        for(Edge edge: edges){
            int w_start = (edge.w.id) * numColor;
            int v_start = (edge.v.id) * numColor;
            for(int i = 0; i < numColor; i++){
                Clause newClause = new Clause();
                newClause.addLiteral(-1 * (w_start - i));
                newClause.addLiteral(-1 * (v_start - i));
                sat.addClause(newClause);
            }
        }

        // add type 2 clause: at-least-one color
        for(int i = 1; i < numVar + 1; i += numColor){
            Clause newClause = new Clause();
            for(int j = i; j < i + numColor; j++){
                newClause.addLiteral(j);
            }
            sat.addClause(newClause);
        }

        // add type 3 clause: at-most-one color
        for(int i = -1 * numVar; i < 0; i += numColor){
            ArrayList<Integer> temp = new ArrayList<>();
            for(int j = i; j < i + numColor; j++){
                temp.add(j);
            }
            CombinationGenerator combinationGenerator = new CombinationGenerator(temp);
            ArrayList<CombinationGenerator.Pair> pairs = combinationGenerator.getPairs();
            for(CombinationGenerator.Pair pair: pairs){
                Clause newClause = new Clause();
                newClause.addLiteral(pair.var1);
                newClause.addLiteral(pair.var2);
                sat.addClause(newClause);
            }
        }

        sat.setNumVar(numVar);
        sat.setNumClause(sat.getClauses().size());

        return sat;
    }

    public static GraphColor reduce3SATToGraphColoring(ThreeSAT sat3){
        GraphColor graphColor = new GraphColor();

        return graphColor;
    }


}
