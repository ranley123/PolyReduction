package DataStructure;

public class Edge {
    public Node w;
    public Node v;

    public Edge(Node w, Node v){
        if(w.id > v.id){
            this.w = v;;
            this.v = w;
        }
        else{
            this.w = w;
            this.v = v;
        }
    }

    /**
     * edges are equal if they have nodes with same literals
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Edge guest = (Edge) obj;
        return (guest.w == this.w && guest.v == this.v) || (guest.v == this.w && guest.w == this.v);
    }

    @Override
    public String toString(){
        return "(" + w.id + ", " + v.id + ")";
    }

}
