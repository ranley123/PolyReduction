import java.util.ArrayList;

public class SAT {
    private int numVar;
    private int numClause;
    private ArrayList<Clause> clauses;

    public void setNumVar(int numVar){
        this.numVar = numVar;
    }

    public void setNumClause(int numClause){
        this.numClause = numClause;
    }

    public void setClauses(ArrayList<Clause> clauses){
        this.clauses = clauses;
    }

    public ArrayList<Clause> getClauses(){
        return clauses;
    }

    public int getNumVar(){
        return numVar;
    }

    public void addClause(Clause clause){
        clauses.add(clause);
    }

    public SAT(){
        clauses = new ArrayList<>();
    }

}
