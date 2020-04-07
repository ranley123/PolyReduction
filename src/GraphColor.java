import java.util.ArrayList;

public class GraphColor {
    int numNode = 0;
    int numEdge = 0;
    int numColor = 0;
    ArrayList<Node> nodes = new ArrayList<>();
    ArrayList<Edge> edges = new ArrayList<>();

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

    public void setNodes(ArrayList<Node> nodes){
        this.nodes = nodes;
    }
    public void setEdges(ArrayList<Edge> edges){
        this.edges = edges;
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