package DataStructure;
import java.io.FileWriter;
import java.io.IOException;
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

    public void print(){
        for(Clause clause: clauses){
            clause.print();
        }
        System.out.println(clauses.size());
    }

    /**
     * output SAT instance to a file
     * @param filename
     */
    public void output(String filename){
        try{
            FileWriter writer = new FileWriter(filename);
            StringBuilder sb = new StringBuilder();
            sb.append("p cnf " + this.getNumVar() + " " + this.getClauses().size() + "\n");
            for(Clause clause: clauses){
                sb.append(clause.toString() + "\n");
            }
            writer.write(sb.toString());
            writer.flush();
            writer.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

}
