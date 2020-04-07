import java.util.ArrayList;

public class ThreeSAT{
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

    public void addClause(Clause clause){
        clauses.add(clause);
    }

    public ThreeSAT(){
        clauses = new ArrayList<>();
    }

    public void print(){
        for(Clause clause: clauses){
            clause.print();
        }
    }
}
