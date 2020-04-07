import java.util.ArrayList;
import java.util.HashMap;

public class Clause {
    ArrayList<Integer> literals;
    private int numLiteral = 0;

    public Clause(){
        literals = new ArrayList<>();
    }

    public void addLiteral(int literal){
        literals.add(literal);
        numLiteral++;
    }

    public ArrayList<Integer> getLiterals(){
        return literals;
    }

    public boolean hasLiteral(int literal){
        return literals.contains(literal);
    }

    public int getNumLiteral(){
        return  numLiteral;
    }

    public void print(){
        StringBuilder sb = new StringBuilder();
        for(Integer i: this.getLiterals()){
            sb.append(i + ", ");
        }
        System.out.println(sb.toString());
    }

}