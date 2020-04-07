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
        int numVar = graphColor.getNumNode() * graphColor.getNumColor();
        sat.setNumVar(numVar);


        return sat;
    }
}
