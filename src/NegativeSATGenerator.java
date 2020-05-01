import DataStructure.Clause;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class NegativeSATGenerator {
    private int numVar;
    private int numClause;
    private ArrayList<Clause> clauses;

    public NegativeSATGenerator(int numVar){
        this.numVar = numVar;
        clauses = new ArrayList<>();
        generate(new ArrayList<Integer>(), 1);
        this.numClause = clauses.size();
        output("./src/input.txt");
    }

    private void generate(ArrayList<Integer> literals, int start){
        if(start > numVar){
            Clause clause = new Clause();
            for(Integer i: literals){
                clause.addLiteral(i);
            }
            clauses.add(clause);
            return;
        }
        literals.add(start);
        generate(literals, start + 1);
        literals.remove(literals.size() - 1);
        literals.add(-1 * start);
        generate(literals, start + 1);
        literals.remove(literals.size() - 1);
    }

    private void output(String filename){
        try{
            FileWriter writer = new FileWriter(filename);
//            writer.write();
            StringBuilder sb = new StringBuilder();
            sb.append("p cnf " + this.numVar + " " + this.numClause + "\n");
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

    public static void main(String[] args){
        NegativeSATGenerator generator = new NegativeSATGenerator(5);
    }
}
