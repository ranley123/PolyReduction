package DataStructure;

import java.io.FileWriter;
import java.io.IOException;

public class VertexCover extends Graph {
    int k = 0;

    public void setK(int k){
        this.k = k;
    }

    public int getK(){
        return k;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("p edge " + this.getNumNode() + " " + this.getNumEdge() + "\n");
        sb.append("k " + k + "\n");
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
