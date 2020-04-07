public class Edge {
    Node w;
    Node v;
    public Edge(Node w, Node v){
        this.w = w;
        this.v = v;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Edge guest = (Edge) obj;
        return guest.w == this.w && guest.v == this.v;
    }


}
