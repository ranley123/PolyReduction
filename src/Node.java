public class Node {
    int id;
    public Node(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Node guest = (Node) obj;
        return guest.id == this.id;
    }
}
