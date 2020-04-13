import java.util.ArrayList;

public class GraphColor {
    private int numNode = 0;
    private int numEdge = 0;
    private int numColor = 0;
    private ArrayList<Node> nodes = new ArrayList<>();
    private ArrayList<Edge> edges = new ArrayList<>();

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

    public void setNumColor(int numColor){
        this.numColor = numColor;
    }

    public int getNumColor(){
        return numColor;
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

}
