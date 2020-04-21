import DataStructure.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {

    String inputFile;


    public Parser(String inputFile){
        this.inputFile = inputFile;
    }

    public SAT readFile(String filename) {
        ArrayList<Clause> clauses = new ArrayList<>();
        SAT sat = new SAT();
        int numVar = 0;
        int numClause = 0;
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
                else if(words[0].equals("p")){
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
            sat.setNumVar(numVar);
            sat.setNumClause(numClause);
            sat.setClauses(clauses);

            return sat;
        }
        catch (FileNotFoundException e){
            System.err.println("Error: file not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public GraphColor readGraph(String filename){
        GraphColor graphColor = new GraphColor();
        int numNode = 0;
        int numEdge = 0;
        int numColor = 0;
        ArrayList<Node> nodes = new ArrayList<>();
        ArrayList<Edge> edges = new ArrayList<>();

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
                else if(words[0].equals("p")){
                    if(!words[1].equals("edge")){
                        throw new IOException();
                    }
                    numNode = Integer.parseInt(words[2]);
                    numEdge = Integer.parseInt(words[3]);

                    String nextLine = reader.readLine();
                    if(!nextLine.startsWith("colours")){
                        throw new IOException("no colours specified");
                    }
                    numColor = Integer.parseInt(nextLine.split("\\s+")[1]);

                }
                else{ // construct a new edge
                    int start = Integer.parseInt(words[1]);
                    int end = Integer.parseInt(words[2]);

                    Node w = new Node(start);
                    Node v = new Node(end);

                    if(!nodes.contains(w)){
                        nodes.add(w);
                    }
                    else{
                        int index = nodes.indexOf(w);
                        w = nodes.get(index);
                    }
                    if(!nodes.contains(v)){
                        nodes.add(v);
                    }
                    else{
                        int index = nodes.indexOf(v);
                        v = nodes.get(index);
                    }

                    Edge e = new Edge(w, v);
                    if(!edges.contains(e)){
                        edges.add(e);
                    }
                }
            }

            graphColor.setNumColor(numColor);
            graphColor.setNumEdge(numEdge);
            graphColor.setNumNode(numNode);
            graphColor.setNodes(nodes);
            graphColor.setEdges(edges);

        }
        catch (FileNotFoundException e){
            System.err.println("Error: file not found");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return graphColor;
    }
}
