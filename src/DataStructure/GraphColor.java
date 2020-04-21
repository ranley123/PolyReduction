package DataStructure;

public class GraphColor extends Graph {
    private int numColor = 0;

    public void setNumColor(int numColor){
        this.numColor = numColor;
    }
    public int getNumColor(){
        return numColor;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("p edge " + this.getNumNode() + " " + this.getNumEdge() + "\n");
        sb.append("colours " + this.getNumColor() + "\n");
        for(Edge edge: this.getEdges()){
            sb.append("e " + edge.w.id + " " + edge.v.id + "\n");
        }
        return sb.toString();
    }
}
