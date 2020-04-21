package DataStructure;

import java.util.ArrayList;

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
        System.out.println(this.toString());
    }

    @Override
    public String toString(){
        String res = "";
        for(Integer i: this.getLiterals()){
            res += (i + " ");
        }
        res += "0";
        return res;
    }

}