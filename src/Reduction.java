import DataStructure.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Reduction {
    static final int ZERO = 0;
    static final int ONE = 1;
    static final int TWO = 2;
    static int offset;

    public static ThreeSAT reduceSATTo3SAT(SAT sat){
        ThreeSAT sat3 = new ThreeSAT();
        ArrayList<Clause> clauses = sat.getClauses();
        offset = sat.getNumVar() + 1;

        for(int i = 0; i < clauses.size(); i++){
            Clause curClause = clauses.get(i);
            if(curClause.getNumLiteral() == 1){
                Clause c = new Clause();
                c.addLiteral(curClause.getLiterals().get(0));
                c.addLiteral(offset);
                c.addLiteral(offset + 1);
                clauses.add(c);

                c = new Clause();
                c.addLiteral(curClause.getLiterals().get(0));
                c.addLiteral(offset);
                c.addLiteral(-1 * (offset + 1));
                clauses.add(c);

                c = new Clause();
                c.addLiteral(curClause.getLiterals().get(0));
                c.addLiteral(-1 * offset);
                c.addLiteral((offset + 1));
                clauses.add(c);

                c = new Clause();
                c.addLiteral(curClause.getLiterals().get(0));
                c.addLiteral(-1 * offset);
                c.addLiteral(-1 * (offset + 1));
                clauses.add(c);

                offset += 2;
            }
            else if(curClause.getNumLiteral() == 2){
                Clause c = new Clause();
                c.addLiteral(curClause.getLiterals().get(0));
                c.addLiteral(curClause.getLiterals().get(1));
                c.addLiteral(offset);
                clauses.add(c);

                c = new Clause();
                c.addLiteral(curClause.getLiterals().get(0));
                c.addLiteral(curClause.getLiterals().get(1));
                c.addLiteral(-1 * offset);
                clauses.add(c);

                offset++;
            }
            else if (curClause.getNumLiteral() == 3){
                sat3.addClause(curClause);
            }
            else{
                to3SATClauses(sat3, curClause);
            }
        }
        sat3.setNumClause(sat3.getClauses().size());
        sat3.setNumVar(offset - 1);
        return sat3;
    }


    public static void to3SATClauses(ThreeSAT sat3, Clause curClause){
        ArrayList<Integer> curLiterals = curClause.getLiterals();

        if(curClause.getNumLiteral() <= 0){
            return;
        }
        else if (curClause.getNumLiteral() <= 3){
            sat3.addClause(curClause);
            return;
        }

        int limit = Math.min(2, curLiterals.size());

        // the first clause
        Clause newClause = new Clause();
        newClause.addLiteral(offset);
        for(int i = 0; i < limit; i++){
            newClause.addLiteral(curLiterals.get(i));
        }
        sat3.addClause(newClause);

        // the remaining clause
        Clause leftClause = new Clause();
        leftClause.addLiteral(offset * -1);
        offset++;
        for(int i = limit; i < curLiterals.size(); i++){
            leftClause.addLiteral(curLiterals.get(i));
        }

        to3SATClauses(sat3, leftClause);

    }

    public static SAT reduceGraphColoringToSAT(GraphColor graphColor){
        SAT sat = new SAT();
        int numNode = graphColor.getNumNode();
        int numColor = graphColor.getNumColor();
        int numVar = numNode * graphColor.getNumColor();

        // add type 1 clause: endpoints of each edge not share same color
        ArrayList<Edge> edges = graphColor.getEdges();
        for(Edge edge: edges){
            int w_start = (edge.w.id) * numColor;
            int v_start = (edge.v.id) * numColor;
            for(int i = 0; i < numColor; i++){
                Clause newClause = new Clause();
                newClause.addLiteral(-1 * (w_start - i));
                newClause.addLiteral(-1 * (v_start - i));
                sat.addClause(newClause);
            }
        }

        // add type 2 clause: at-least-one color
        for(int i = 1; i < numVar + 1; i += numColor){
            Clause newClause = new Clause();
            for(int j = i; j < i + numColor; j++){
                newClause.addLiteral(j);
            }
            sat.addClause(newClause);
        }

        // add type 3 clause: at-most-one color
        for(int i = -1 * numVar; i < 0; i += numColor){
            ArrayList<Integer> temp = new ArrayList<>();
            for(int j = i; j < i + numColor; j++){
                temp.add(j);
            }
            CombinationGenerator combinationGenerator = new CombinationGenerator(temp);
            ArrayList<CombinationGenerator.Pair> pairs = combinationGenerator.getPairs();
            for(CombinationGenerator.Pair pair: pairs){
                Clause newClause = new Clause();
                newClause.addLiteral(pair.var1);
                newClause.addLiteral(pair.var2);
                sat.addClause(newClause);
            }
        }

        sat.setNumVar(numVar);
        sat.setNumClause(sat.getClauses().size());

        return sat;
    }

    public static GraphColor reduce3SATToGraphColoring(ThreeSAT sat3){
        GraphColor graphColor = new GraphColor();
        int numVar = sat3.getNumVar();
        int numColor = numVar;
        ArrayList<Node> nodes = new ArrayList<>();
        ArrayList<Clause> clauses = sat3.getClauses();
        int numClause = sat3.getClauses().size();
        int numB = numColor - 2;

        int varStart = 0 * numVar; // numVar
        int negVarStart = 1 * numVar; // numVar
        int clauseStart = 2 * numVar;
        int BStart = clauseStart + 3 * numClause;
        ArrayList<Integer> clauseStarts = new ArrayList<>();
        clauseStarts.add(clauseStart);
        int offset = 0;

        // make var nodes
        for(int i = 1; i <= numVar; i++){
            nodes.add(new Node(i));
        }

        // make neg var nodes
        for(int i = 1; i <= numVar; i++){
            nodes.add(new Node(-1 * i));
        }

        // make clause nodes, each clause has 3 node
        for(int i = clauseStart; i < BStart; i++){
            if(offset == 3){
                clauseStarts.add(clauseStart);
                offset = 0;
            }
            nodes.add(new Node(i));
            offset++;
        }

        // make B nodes
        for(int i = 0; i < numB; i++){
            nodes.add(new Node(BStart + i));
        }


        // connect var nodes to negvar nodes
        for(int i = 0; i < negVarStart; i++){
            Edge newEdge = new Edge(nodes.get(varStart + i), nodes.get(negVarStart + i));
            graphColor.addEdge(newEdge);
        }
//        System.out.println("var negvar: " + graphColor.getEdges().size());

        // connect var and negvar to B nodes
        for(int i = BStart; i < BStart + numB; i++){
            Node B = nodes.get(i);

            for(int j = 0; j < negVarStart; j++){
                graphColor.addEdge(new Edge(nodes.get(j), B));
                graphColor.addEdge(new Edge(nodes.get(j + numVar), B));
            }
        }
//        System.out.println("var b: " + graphColor.getEdges().size());

        // connect B to B nodes
        for(int i = BStart; i < BStart + numB; i++){
            Node b1 = nodes.get(i);
            for(int j = i + 1; j < BStart + numB; j++){
                Node b2 = nodes.get(j);
                Edge edge = new Edge(b1, b2);
                graphColor.addEdge(edge);
            }
        }
//        System.out.println("b b: " + graphColor.getEdges().size());

        // connect clause nodes
        for(int i = 0; i < clauseStarts.size(); i++){
            int curClauseStart = clauseStarts.get(i);
            Node c1 = nodes.get(curClauseStart);
            Node c2 = nodes.get(curClauseStart + 1);
            Node c3 = nodes.get(curClauseStart + 2);

            graphColor.addEdge(new Edge(c1, c2));
            graphColor.addEdge(new Edge(c2, c3));
            graphColor.addEdge(new Edge(c1, c3));
        }
//        System.out.println("clause: " + graphColor.getEdges().size());


        // connect clause node to var nodes
        for(int i = 0; i < numClause; i++){
            ArrayList<Integer> literals = clauses.get(i).getLiterals();
            Node var1 = getNodeByLiteral(nodes, numVar, literals.get(0));
            Node var2 = getNodeByLiteral(nodes, numVar, literals.get(1));
            Node var3 = getNodeByLiteral(nodes, numVar, literals.get(2));
            int curClauseStart = clauseStarts.get(i);

            graphColor.addEdge(new Edge(nodes.get(curClauseStart), var1));
            graphColor.addEdge(new Edge(nodes.get(curClauseStart + 1), var2));
            graphColor.addEdge(new Edge(nodes.get(curClauseStart + 2), var3));
        }

        // connect numB - 1 B nodes to every other node (clause nodes)
        for(int i = BStart + 1; i < BStart + numB; i++){
            for(int j = clauseStart; j < BStart; j++){
                graphColor.addEdge(new Edge(nodes.get(i), nodes.get(j)));
            }
        }

        graphColor.setNumEdge(graphColor.getEdges().size());
        graphColor.setNodes(nodes);
        graphColor.setNumNode(nodes.size());
        graphColor.setNumColor(numVar);

        return graphColor;
    }

    public static Node getNodeByLiteral(ArrayList<Node> nodes, int numVar, int literal){
        int index = 0;
        if(literal < 0){
            index = literal * -1 + numVar - 1;
        }
        else{
            index = literal - 1;
        }
        return nodes.get(index);
    }

//    public static GraphColor reduce3SATToGraphColoring(ThreeSAT sat3){
//        GraphColor graphColor = new GraphColor();
//        int numVar = sat3.getNumVar();
//        ArrayList<Node> nodes = new ArrayList<>();
//        ArrayList<Clause> clauses = sat3.getClauses();
//        int numClause = sat3.getClauses().size();
//        int numColor = numVar;
//
//        int varStart = 0 * numVar; // numVar
//        int negVarStart = 1 * numVar; // numVar
//        int TStart = 2 * numVar; // 1
//        int FStart = 2 * numVar + 1; // 1
//        int BStart = FStart + 1; // numColor -2
//        int gadgetStart = BStart + numColor - 2;
//        ArrayList<Integer> gadgetStarts = new ArrayList<>();
//        gadgetStarts.add(gadgetStart);
//
//        // make nodes
//        for(int i = 1; i <= numVar; i++){
//            nodes.add(new Node(i));
//        }
//
//        // make neg vars
//        for(int i = 1; i <= numVar; i++){
//            nodes.add(new Node(-1 * i));
//        }
//
//        // make T and F Nodes
//        nodes.add(new Node(TStart + 1));
//        nodes.add(new Node(FStart + 1));
//
//        // make B nodes
//        for(int i = BStart; i < gadgetStart; i++){
//            nodes.add(new Node(i + 1));
//        }
//
//        // make gadget nodes and edges
//        for(int i = 0; i < numClause; i++){
//            Clause clause = clauses.get(i);
//            ArrayList<Integer> literals = clause.getLiterals();
//            Node B = nodes.get(BStart);
//            Node F = nodes.get(FStart);
//
//            int curGadgetStart = gadgetStarts.get(i);
//            // make gadget nodes
//            for(int j = 0; j < 3 * (clause.getNumLiteral() - 1); j++){
//                nodes.add(new Node(curGadgetStart + j + 1));
//            }
//
//            Node node1 = null;
//            Node node2 = null;
//            Node node3 = null;
//
//            switch (clause.getNumLiteral()){
//                case 1:
//                    node1 = getNodeByLiteral(nodes, numVar, literals.get(0));
//                    break;
//                case 2:
//                    node1 = getNodeByLiteral(nodes, numVar, literals.get(0));
//                    node2 = getNodeByLiteral(nodes, numVar, literals.get(1));
//                    break;
//                case 3:
//                    node1 = getNodeByLiteral(nodes, numVar, literals.get(0));
//                    node2 = getNodeByLiteral(nodes, numVar, literals.get(1));
//                    node3 = getNodeByLiteral(nodes, numVar, literals.get(2));
//                    break;
//                default:
//                    break;
//            }
//
//            Node res = makeGadget(nodes, graphColor.getEdges(), curGadgetStart, node1, node2, node3);
//            Edge edge1 = new Edge(res, B);
//            Edge edge2 = new Edge(res, F);
//            graphColor.addEdge(edge1);
//            graphColor.addEdge(edge2);
//
//
//            if(i != numClause - 1){
//                gadgetStarts.add(curGadgetStart + 3 * (clause.getNumLiteral() - 1));
//            }
//        }
////        System.out.println("gadget edges: " + graphColor.getEdges().size());
//
//        // connect var nodes to negvar nodes
//        for(int i = 0; i < numVar; i++){
//            Edge newEdge = new Edge(nodes.get(varStart + i), nodes.get(negVarStart + i));
//            graphColor.addEdge(newEdge);
//        }
////        System.out.println("var neg var edges: " + graphColor.getEdges().size());
//
//
//        // connect var and negvar to B nodes
//        for(int i = 0; i < numVar; i++){
//            Node var = nodes.get(varStart + i);
//            Node negVar = nodes.get(negVarStart + i);
//            for(int j = BStart; j < gadgetStart; j++){
//                Node base = nodes.get(j);
//                Edge edge1 = new Edge(var, base);
//                Edge edge2 = new Edge(negVar, base);
//                graphColor.addEdge(edge1);
//                graphColor.addEdge(edge2);
//            }
//        }
//
////        System.out.println("b var edges: " + graphColor.getEdges().size());
//
//
//        // connect T, F, B
//        Node T = nodes.get(TStart);
//        Node F = nodes.get(FStart);
//        Edge edgeTF = new Edge(T, F);
//        graphColor.addEdge(edgeTF);
//        for(int i = BStart; i < gadgetStart; i++){
//            Node B = nodes.get(i);
//            Edge edgeTB0 = new Edge(T, B);
//            Edge edgeFB0 = new Edge(F, B);
//            graphColor.addEdge(edgeFB0);
//            graphColor.addEdge(edgeTB0);
//        }
////        System.out.println("t f b edges: " + graphColor.getEdges().size());
//
//        // connect B nodes
//        for(int i = BStart; i < gadgetStart; i++){
//            Node B0 = nodes.get(i);
//            for(int j = i + 1; j < gadgetStart; j++){
//                Node B1 = nodes.get(j);
//                Edge edge = new Edge(B0, B1);
//                graphColor.addEdge(edge);
//            }
//        }
////        System.out.println("b b edges: " + graphColor.getEdges().size());
//
//        // connect B, gadget nodes
//        for (int i = BStart + 1; i < gadgetStart; i++){
//            for(int j = gadgetStart; j < nodes.size(); j++){
//                Edge edge = new Edge(nodes.get(i), nodes.get(j));
//                graphColor.addEdge(edge);
//            }
//        }
////        System.out.println("b gadget edges: " + graphColor.getEdges().size());
//
//        graphColor.setNumColor(numColor);
//        graphColor.setNodes(nodes);
//        graphColor.setNumEdge(graphColor.getEdges().size());
//        graphColor.setNumNode(nodes.size());
//        return graphColor;
//    }


    public static Node makeGadget(ArrayList<Node> nodes, ArrayList<Edge> edges, int gadgetStart, Node node1, Node node2, Node node3){
        // only one
        if(node1 != null && node2 == null && node3 == null){
            return node1;
        }
        // two
        if(node1 != null && node2 != null && node3 == null){
            Node a = nodes.get(gadgetStart);
            Node b = nodes.get(gadgetStart + 1);
            Node c = nodes.get(gadgetStart + 2);
            edges.add(new Edge(node1, a));
            edges.add(new Edge(node2, b));
            edges.add(new Edge(a, c));
            edges.add(new Edge(a, b));
            edges.add(new Edge(b, c));

            return c;
        }
        if(node1 != null && node2 != null && node3 != null){
            Node node = makeGadget(nodes, edges, gadgetStart, node1, node2, null);
            Node res = makeGadget(nodes, edges, gadgetStart + 3, node, node3, null);
            return res;
        }
        else{
            return null;
        }
    }

    public static Clique reduce3SATToClique(ThreeSAT sat3){
        Clique clique = new Clique();
        ArrayList<Clause> clauses = sat3.getClauses();
        ArrayList<Node> nodes = new ArrayList<>();
        ArrayList<Edge> edges = new ArrayList<>();
        HashMap<Integer, Integer> clauseIndexMap = new HashMap<>();
        int pointer = 0;

        // make nodes
        for(int i = 0; i < clauses.size(); i++){
            clauseIndexMap.put(i, pointer);
            for(Integer literal: clauses.get(i).getLiterals()){
                Node node = new Node(literal);
                nodes.add(node);
                pointer++;
            }
        }
//        System.out.println(clauseIndexMap);

        // make edges
        for(int i = 0; i < clauses.size(); i++){
            if(i == clauses.size() - 1){
                break;
            }
            for(int nodeIndex = clauseIndexMap.get(i); nodeIndex < clauseIndexMap.get(i + 1); nodeIndex++){
                Node node1 = nodes.get(nodeIndex);
                for(int j = i + 1; j < clauses.size(); j++){
                    makeCliqueEdges(nodes, edges, clauseIndexMap.get(j), node1, clauses.get(j));
//                    clauses.get(j).print();
                }
            }
        }
        clique.setEdges(edges);
        clique.setNodes(nodes);
        clique.setNumEdge(edges.size());
        clique.setNumNode(nodes.size());
        clique.setClauseIndexMap(clauseIndexMap);

        return clique;
    }

    public static VertexCover reduceCliqueToVertexCover(Clique clique){
        VertexCover vertexCover = new VertexCover();
        ArrayList<Node> nodes = clique.getNodes();
        ArrayList<Edge> cliqueEdges = clique.getEdges();
        ArrayList<Edge> edges = new ArrayList<>();
        HashMap<Integer, Integer> clauseIndexMap = clique.getClauseIndexMap();

        for(int i = 0; i < nodes.size(); i++){
            for(int j = i + 1; j < nodes.size(); j++){
                Edge edge = new Edge(nodes.get(i), nodes.get(j));
                if(cliqueEdges.contains(edge)){
                  continue;
                }
                else{
                    edges.add(edge);
                }
            }
        }

        vertexCover.setEdges(edges);
        vertexCover.setNodes(nodes);
        vertexCover.setNumEdge(edges.size());
        vertexCover.setNumNode(nodes.size());
        vertexCover.setClauseIndexMap(clauseIndexMap);
        return vertexCover;
    }

    public static void makeCliqueEdges(ArrayList<Node> nodes, ArrayList<Edge> edges, int clauseIndex, Node node, Clause nextClause){
        ArrayList<Integer> literals = nextClause.getLiterals();
        for(int i = 0; i < nextClause.getNumLiteral(); i++){
            if(literals.get(i) == -1 * node.id){
                continue;
            }
            Node targetNode = nodes.get(clauseIndex + i);
            Edge edge = new Edge(node, targetNode);
            edges.add(edge);
        }
    }


}
