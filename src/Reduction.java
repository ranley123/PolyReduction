import DataStructure.*;

import java.lang.reflect.Array;
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
        sat3.setNumVar(sat.getNumVar());

        for(int i = 0; i < clauses.size(); i++){
            Clause curClause = clauses.get(i);

            if (curClause.getNumLiteral() <= 3){
                sat3.addClause(curClause);
            }

            else{
                to3SATClauses(sat3, curClause);
            }
        }
        sat3.setNumClause(sat3.getClauses().size());
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

//    public static GraphColor reduce3SATToGraphColoring(ThreeSAT sat3){
//        GraphColor graphColor = new GraphColor();
//        int numVar = sat3.getNumVar();
//        ArrayList<Node> nodes = new ArrayList<>();
//        ArrayList<Clause> clauses = sat3.getClauses();
//        // 1 to n(numNode) are variables
//        // n + 1 to 2n are neg variables
//        // 2n + 1 to 2n + m (numClause) are clause variables
//        if(numVar <= 3){
//            numVar = 4;
//        }
//            int numClause = sat3.getClauses().size();
//
//        // make nodes
//        for(int i = 1; i <= numVar; i++){
//            nodes.add(new Node(i));
//        }
//
//        for(int i = 1; i <= numVar; i++){
//            nodes.add(new Node(-1 * i));
//        }
//
//        int varStart = 0 * numVar;
//        int negVarStart = 1 * numVar;
//        int colorStart = 2 * numVar;
//        int clauseStart = 3 * numVar;
//
//        for(int i = 0; i < numVar; i++){
//            nodes.add(new Node(colorStart + i + 1));
//        }
//
//        for(int i = 0; i < numClause; i++){
//            nodes.add(new Node(clauseStart + i + 1));
//        }
//
//        graphColor.setNodes(nodes);
//        graphColor.setNumNode(nodes.size());
//        graphColor.setNumColor(numVar);
//
//        // connect var nodes to negvar nodes
//        for(int i = 0; i < numVar; i++){
//            Edge newEdge = new Edge(nodes.get(varStart + i), nodes.get(negVarStart + i));
//            graphColor.addEdge(newEdge);
//        }
//
//        // connect var and negvar to color nodes
//        for(int i = 0; i < numVar; i++){
//            Node var = nodes.get(varStart + i);
//            Node negVar = nodes.get(negVarStart + i);
//            for(int j = 0; j < numVar; j++){
//                if(i == j)
//                    continue;
//                Node color = nodes.get(colorStart + j);
//                Edge edge1 = new Edge(var, color);
//                Edge edge2 = new Edge(negVar, color);
//                graphColor.addEdge(edge1);
//                graphColor.addEdge(edge2);
//            }
//        }
//
//        // connect color to color nodes
//        for(int i = 0; i < numVar; i++){
//            Node color1 = nodes.get(colorStart + i);
//            for(int j = i + 1; j < numVar; j++){
//                Node color2 = nodes.get(colorStart + j);
//                Edge edge = new Edge(color1, color2);
//                graphColor.addEdge(edge);
//            }
//        }
//
//        // connect clause node to var nodes
//        for(int i = 0; i < numClause; i++){
//            Node clauseNode = nodes.get(clauseStart + i);
//            Clause clause = clauses.get(i);
//
//            for(int j = 0; j < numVar; j++){
//                Node var = nodes.get(varStart + j);
//                Node negVar = nodes.get(negVarStart + j);
//                int value = var.getId();
//
//                if(clause.hasLiteral(value)){
//                    Edge edge = new Edge(negVar, clauseNode);
//                    graphColor.addEdge(edge);
//                }
//                else if(clause.hasLiteral(value * -1)){
//                    Edge edge = new Edge(var, clauseNode);
//                    graphColor.addEdge(edge);
//                }
//                else{
//                    Edge edge1 = new Edge(var, clauseNode);
//                    Edge edge2 = new Edge(negVar, clauseNode);
//                    graphColor.addEdge(edge1);
//                    graphColor.addEdge(edge2);
//                }
//            }
//        }
//
//        graphColor.setNumEdge(graphColor.getEdges().size());
//        return graphColor;
//    }

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

    public static GraphColor reduce3SATToGraphColoring(ThreeSAT sat3){
        GraphColor graphColor = new GraphColor();
        int numVar = sat3.getNumVar();
        ArrayList<Node> nodes = new ArrayList<>();
        ArrayList<Clause> clauses = sat3.getClauses();
        int numClause = sat3.getClauses().size();
        int numColor = numVar;

        int varStart = 0 * numVar; // numVar
        int negVarStart = 1 * numVar; // numVar
        int TStart = 2 * numVar; // 1
        int FStart = 2 * numVar + 1; // 1
        int BStart = FStart + 1; // numColor -2
        int gadgetStart = BStart + numColor - 2;
        ArrayList<Integer> gadgetStarts = new ArrayList<>();
        gadgetStarts.add(gadgetStart);

        // make nodes
        for(int i = 1; i <= numVar; i++){
            nodes.add(new Node(i));
        }

        // make neg vars
        for(int i = 1; i <= numVar; i++){
            nodes.add(new Node(-1 * i));
        }

        // make T and F Nodes
        nodes.add(new Node(TStart + 1));
        nodes.add(new Node(FStart + 1));

        // make B nodes
        for(int i = BStart; i < gadgetStart; i++){
            nodes.add(new Node(i + 1));
        }

        // make gadget nodes and edges
        for(int i = 0; i < numClause; i++){
            Clause clause = clauses.get(i);
            ArrayList<Integer> literals = clause.getLiterals();
            Node B = nodes.get(BStart);
            Node F = nodes.get(FStart);

            int curGadgetStart = gadgetStarts.get(i);
            // make gadget nodes
            for(int j = 0; j < 3 * (clause.getNumLiteral() - 1); j++){
                nodes.add(new Node(curGadgetStart + j + 1));
            }

            Node node1 = null;
            Node node2 = null;
            Node node3 = null;

            switch (clause.getNumLiteral()){
                case 1:
                    node1 = getNodeByLiteral(nodes, numVar, literals.get(0));
                    break;
                case 2:
                    node1 = getNodeByLiteral(nodes, numVar, literals.get(0));
                    node2 = getNodeByLiteral(nodes, numVar, literals.get(1));
                    break;
                case 3:
                    node1 = getNodeByLiteral(nodes, numVar, literals.get(0));
                    node2 = getNodeByLiteral(nodes, numVar, literals.get(1));
                    node3 = getNodeByLiteral(nodes, numVar, literals.get(2));
                    break;
                default:
                    break;
            }

            Node res = makeGadget(nodes, graphColor.getEdges(), curGadgetStart, node1, node2, node3);
            Edge edge1 = new Edge(res, B);
            Edge edge2 = new Edge(res, F);
            graphColor.addEdge(edge1);
            graphColor.addEdge(edge2);

            if(i != numClause - 1){
                gadgetStarts.add(curGadgetStart + 3 * (clause.getNumLiteral() - 1));
            }
        }


        // connect var nodes to negvar nodes
        for(int i = 0; i < numVar; i++){
            Edge newEdge = new Edge(nodes.get(varStart + i), nodes.get(negVarStart + i));
            graphColor.addEdge(newEdge);
        }

        // connect var and negvar to B nodes
        for(int i = 0; i < numVar; i++){
            Node var = nodes.get(varStart + i);
            Node negVar = nodes.get(negVarStart + i);
            for(int j = BStart; j < gadgetStart; j++){
                Node base = nodes.get(j);
                Edge edge1 = new Edge(var, base);
                Edge edge2 = new Edge(negVar, base);
                graphColor.addEdge(edge1);
                graphColor.addEdge(edge2);
            }
        }

        // connect T, F, B
        Node T = nodes.get(TStart);
        Node F = nodes.get(FStart);
        Edge edgeTF = new Edge(T, F);
        graphColor.addEdge(edgeTF);
        for(int i = BStart; i < gadgetStart; i++){
            Node B = nodes.get(i);
            Edge edgeTB0 = new Edge(T, B);
            Edge edgeFB0 = new Edge(F, B);
            graphColor.addEdge(edgeFB0);
            graphColor.addEdge(edgeTB0);
        }

        // connect B nodes
        for(int i = BStart; i < gadgetStart; i++){
            Node B0 = nodes.get(i);
            for(int j = i + 1; j < gadgetStart; j++){
                Node B1 = nodes.get(j);
                Edge edge = new Edge(B0, B1);
                graphColor.addEdge(edge);
            }
        }

        // connect B, gadget nodes
        for (int i = BStart + 1; i < gadgetStart; i++){
            for(int j = gadgetStart; j < nodes.size(); j++){
                Edge edge = new Edge(nodes.get(i), nodes.get(j));
                graphColor.addEdge(edge);
            }
        }

        graphColor.setNumColor(numColor);
        graphColor.setNodes(nodes);
        graphColor.setNumEdge(graphColor.getEdges().size());
        graphColor.setNumNode(nodes.size());
        return graphColor;
    }

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
