package DataStructure;

import java.util.ArrayList;

/**
 * class for Clause
 */
public class Clause {
    ArrayList<Integer> literals;
    private int numLiteral = 0;

    public Clause(){
        literals = new ArrayList<>();
    }

    /**
     * add a new literal to list
     * @param literal
     */
    public void addLiteral(int literal){
        literals.add(literal);
        numLiteral++;
    }

    /**
     * return a list of literals
     * @return
     */
    public ArrayList<Integer> getLiterals(){
        return literals;
    }

    public boolean hasLiteral(int literal){
        return literals.contains(literal);
    }

    /**
     * get number of literals
     * @return
     */
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