package DataStructure;
import java.io.FileWriter;
import java.io.IOException;

/**
 * class extends the Graph class to add color property
 * The toString() and output() should be overwritten for colours specified
 */
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
        sb.append("k " + this.getNumColor() + "\n");
        for(Edge edge: this.getEdges()){
            sb.append("e " + edge.w.id + " " + edge.v.id + "\n");
        }
        return sb.toString();
    }
    @Override
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
