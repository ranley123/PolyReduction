package DataStructure;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Graph {
    private int numNode = 0;
    private int numEdge = 0;
    private ArrayList<Node> nodes = new ArrayList<>();
    private ArrayList<Edge> edges = new ArrayList<>();

    private HashMap<Integer, Integer> clauseIndexMap = new HashMap<>();

    public void setNumNode(int numNode){
        this.numNode = numNode;
    }

    public int getNumNode(){
        return numNode;
    }

    public void setNumEdge(int numEdge){
        this.numEdge = numEdge;
    }

    public int getNumEdge(){
        return numEdge;
    }

    public void addEdge(Edge edge){
        edges.add(edge);
    }
    public void setNodes(ArrayList<Node> nodes){
        this.nodes = nodes;
    }
    public ArrayList<Node> getNodes(){
        return nodes;
    }
    public void setEdges(ArrayList<Edge> edges){
        this.edges = edges;
    }
    public ArrayList<Edge> getEdges(){
        return edges;
    }

    public void setClauseIndexMap(HashMap<Integer, Integer> clauseIndexMap){
        this.clauseIndexMap = clauseIndexMap;
    }

    public HashMap<Integer, Integer> getClauseIndexMap(){
        return clauseIndexMap;
    }
    public void print(){
        StringBuilder sb = new StringBuilder();
        for(Node node: nodes){
            sb.append(node.id + ",");
        }
        System.out.println("Nodes: " + sb.toString());

        sb = new StringBuilder();
        for(Edge edge: edges){
            sb.append("(" + edge.w.id + "," + edge.v.id + "),");
        }
        System.out.println("Edges: " + sb.toString());
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("p edge " + this.getNumNode() + " " + this.getNumEdge() + "\n");
        for(Edge edge: edges){
            sb.append("e " + edge.w.id + " " + edge.v.id + "\n");
        }
        return sb.toString();
    }

    public void output(String filename){
        try{
            FileWriter writer = new FileWriter(filename);

            writer.write(this.toString());
            writer.flush();
            writer.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
