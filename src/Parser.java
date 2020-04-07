import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {
    int numVar;
    int numClause;
    boolean initialised;
    String inputFile;

    ArrayList<Clause> clauses;
    SAT sat;

    public Parser(String inputFile){
        this.inputFile = inputFile;
        clauses = new ArrayList<>();
        sat = new SAT();
        readFile(inputFile);
    }

    public void initSAT(){
        sat.setNumVar(numVar);
        sat.setNumClause(numClause);
        sat.setClauses(clauses);
    }

    public void readFile(String filename) {
        try{
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = "";

            // read line by line
            while((line = reader.readLine()) != null){
                if(line.length() == 0)
                    continue;
                line = line.strip();
                String[] words = line.split("\\s+");
                if(words[0].equals("c")){ // extra information so don't need to be processed
                    continue;
                }
                else if(!words[0].equals("c") && !initialised){
                    initialised = true;
                    numVar = Integer.parseInt(words[2]);
                    numClause = Integer.parseInt(words[3]);
                }
                else{ // construct a new clause
                    Clause curClause = new Clause();
                    for(String word: words){
                        int num = Integer.parseInt(word);
                        if(num != 0)
                            curClause.addLiteral(num);
                    }
                    clauses.add(curClause);
                }
            }

            initSAT();

        }
        catch (FileNotFoundException e){
            System.err.println("Error: file not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
