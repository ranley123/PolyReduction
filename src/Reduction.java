import DataStructure.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Reduction {
    static int offset; // if new literal is needed then this is the pointer index

    /**
     * reduces SAT to 3SAT
     * @param sat
     * @return
     */
    public static ThreeSAT reduceSATTo3SAT(SAT sat){
        ThreeSAT sat3 = new ThreeSAT();
        ArrayList<Clause> clauses = sat.getClauses();
        offset = sat.getNumVar() + 1;

        for(int i = 0; i < clauses.size(); i++){
            Clause curClause = clauses.get(i);

            // when the clause has only 1 literal, expands it into 4 clauses with 2 dummy variables
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
            // when the clause has only 2 literals, expands it into 2 clauses with 1 dummy variables
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
            // when the clause has more than 3 literals, splits it into 2 clauses recursively
            else{
                to3SATClauses(sat3, curClause);
            }
        }
        sat3.setNumClause(sat3.getClauses().size());
        sat3.setNumVar(offset - 1);
        return sat3;
    }

    /**
     * split a clause with more than 4 literals into sub clauses recursively
     * @param sat3      - the 3 sat instance
     * @param curClause - the current clause to be split
     */
    public static void to3SATClauses(ThreeSAT sat3, Clause curClause){
        ArrayList<Integer> curLiterals = curClause.getLiterals();

        if(curClause.getNumLiteral() <= 0){
            return;
        }
        // if the current clause is a valid clause
        else if (curClause.getNumLiteral() <= 3){
            sat3.addClause(curClause);
            return;
        }

        // if the current clause still needs spilts
        int limit = Math.min(2, curLiterals.size());

        // the first clause is a valid clause with only 3 literals
        Clause newClause = new Clause();
        newClause.addLiteral(offset);
        for(int i = 0; i < limit; i++){
            newClause.addLiteral(curLiterals.get(i));
        }
        sat3.addClause(newClause);

        // the remaining clause to be further split
        Clause leftClause = new Clause();
        leftClause.addLiteral(offset * -1);
        offset++;
        for(int i = limit; i < curLiterals.size(); i++){
            leftClause.addLiteral(curLiterals.get(i));
        }

        to3SATClauses(sat3, leftClause);

    }

    /**
     * reduces Graph Coloring Problem into SAT Problem
     * @param graphColor
     * @return
     */
    public static SAT reduceGraphColoringToSAT(GraphColor graphColor){
        SAT sat = new SAT();
        int numNode = graphColor.getNumNode();
        int numColor = graphColor.getNumColor();
        int numVar = numNode * graphColor.getNumColor();

        // add type 1 clause: endpoints of each edge not share same color
        // each node of the edge now expands to |number of color| variables
        // each variable represents one color for this node
        // for algorithm, please refer to report
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
        // i.e. a1 or a2 or a3
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
//        int numColor = numVar;
//        ArrayList<Node> nodes = new ArrayList<>();
//        ArrayList<Clause> clauses = sat3.getClauses();
//        int numClause = sat3.getClauses().size();
//        int numB = numColor - 2;
//
//        int varStart = 0 * numVar; // numVar
//        int negVarStart = 1 * numVar; // numVar
//        int clauseStart = 2 * numVar;
//        int BStart = clauseStart + 3 * numClause;
//        ArrayList<Integer> clauseStarts = new ArrayList<>();
//        clauseStarts.add(clauseStart);
//        int offset = 0;
//
//        // make var nodes
//        for(int i = 1; i <= numVar; i++){
//            nodes.add(new Node(i));
//        }
//
//        // make neg var nodes
//        for(int i = 1; i <= numVar; i++){
//            nodes.add(new Node(-1 * i));
//        }
//
//        // make clause nodes, each clause has 3 node
//        for(int i = clauseStart; i < BStart; i++){
//            if(offset == 3){
//                clauseStarts.add(clauseStart);
//                offset = 0;
//            }
//            nodes.add(new Node(i));
//            offset++;
//        }
//
//        // make B nodes
//        for(int i = 0; i < numB; i++){
//            nodes.add(new Node(BStart + i));
//        }
//
//
//        // connect var nodes to negvar nodes
//        for(int i = 0; i < negVarStart; i++){
//            Edge newEdge = new Edge(nodes.get(varStart + i), nodes.get(negVarStart + i));
//            graphColor.addEdge(newEdge);
//        }
////        System.out.println("var negvar: " + graphColor.getEdges().size());
//
//        // connect var and negvar to B nodes
//        for(int i = BStart; i < BStart + numB; i++){
//            Node B = nodes.get(i);
//
//            for(int j = 0; j < negVarStart; j++){
//                graphColor.addEdge(new Edge(nodes.get(j), B));
//                graphColor.addEdge(new Edge(nodes.get(j + numVar), B));
//            }
//        }
////        System.out.println("var b: " + graphColor.getEdges().size());
//
//        // connect B to B nodes
//        for(int i = BStart; i < BStart + numB; i++){
//            Node b1 = nodes.get(i);
//            for(int j = i + 1; j < BStart + numB; j++){
//                Node b2 = nodes.get(j);
//                Edge edge = new Edge(b1, b2);
//                graphColor.addEdge(edge);
//            }
//        }
////        System.out.println("b b: " + graphColor.getEdges().size());
//
//        // connect clause nodes
//        for(int i = 0; i < clauseStarts.size(); i++){
//            int curClauseStart = clauseStarts.get(i);
//            Node c1 = nodes.get(curClauseStart);
//            Node c2 = nodes.get(curClauseStart + 1);
//            Node c3 = nodes.get(curClauseStart + 2);
//
//            graphColor.addEdge(new Edge(c1, c2));
//            graphColor.addEdge(new Edge(c2, c3));
//            graphColor.addEdge(new Edge(c1, c3));
//        }
////        System.out.println("clause: " + graphColor.getEdges().size());
//
//
//        // connect clause node to var nodes
//        for(int i = 0; i < numClause; i++){
//            ArrayList<Integer> literals = clauses.get(i).getLiterals();
//            Node var1 = getNodeByLiteral(nodes, numVar, literals.get(0));
//            Node var2 = getNodeByLiteral(nodes, numVar, literals.get(1));
//            Node var3 = getNodeByLiteral(nodes, numVar, literals.get(2));
//            int curClauseStart = clauseStarts.get(i);
//
//            graphColor.addEdge(new Edge(nodes.get(curClauseStart), var1));
//            graphColor.addEdge(new Edge(nodes.get(curClauseStart + 1), var2));
//            graphColor.addEdge(new Edge(nodes.get(curClauseStart + 2), var3));
//        }
//
//        // connect numB - 1 B nodes to every other node (clause nodes)
//        for(int i = BStart + 1; i < BStart + numB; i++){
//            for(int j = clauseStart; j < BStart; j++){
//                graphColor.addEdge(new Edge(nodes.get(i), nodes.get(j)));
//            }
//        }
//
//        graphColor.setNumEdge(graphColor.getEdges().size());
//        graphColor.setNodes(nodes);
//        graphColor.setNumNode(nodes.size());
//        graphColor.setNumColor(numVar);
//
//        return graphColor;
//    }

    /**
     * get the node from the node list given literal value
     * index 0 - n-1 : positive vars indices
     * index n - 2n-1: negative vars indices
     * @param nodes     - a list of all nodes
     * @param numVar    - the number of variables
     * @param literal   - the literal we want to find
     * @return
     */
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

    /**
     * get the literal value of the current node
     * @param clauses   - a list of all clauses
     * @param node      - the given node
     * @return the literal value of the node in the clauses
     */
    public static int getLiteralByNode(ArrayList<Clause> clauses, Node node){
        int index = node.id - 1; // get the node index in nodes
        int clauseIndex = index / 3; // get the clause index the node belongs to
        int nodeIndex = index % 3; // get the literal index the node represents for
        return clauses.get(clauseIndex).getLiterals().get(nodeIndex);
    }

    /**
     * recursively make gadgets vertices and edges, return the top vertex to connect to F and B
     * @param nodes         - a list of all nodes
     * @param edges         - a list of all edges
     * @param gadgetStart   - the starting index of the current 6 gadget nodes
     * @param node1         - the literal 1
     * @param node2         - the literal 2
     * @param node3         - the literal 3
     * @return
     */
    public static Node makeGadget(ArrayList<Node> nodes, ArrayList<Edge> edges, int gadgetStart, Node node1, Node node2, Node node3){
        // only one means the gadget is completed
        if(node1 != null && node2 == null && node3 == null){
            return node1;
        }
        // two means that literal 1 and literal 2 have been connected.
        // now to connect the result node of the first 2 literals with the literal 3
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
        // three means making gadget is just starting, there are 3 to be connected
        if(node1 != null && node2 != null && node3 != null){
            // gadget first 2
            Node node = makeGadget(nodes, edges, gadgetStart, node1, node2, null);
            // gadget the result of above and the left one
            Node res = makeGadget(nodes, edges, gadgetStart + 3, node, node3, null);
            return res;
        }
        else{
            return null;
        }
    }

    /**
     * reduces 3-SAT instance to K Graph Coloring problem
     * default k: the number of variables
     * @param sat3
     * @return
     */
    public static GraphColor reduce3SATToGraphColoring(ThreeSAT sat3){
        GraphColor graphColor = new GraphColor();
        int numVar = sat3.getNumVar();
        ArrayList<Node> nodes = new ArrayList<>();
        ArrayList<Clause> clauses = sat3.getClauses();
        int numClause = sat3.getClauses().size();
        int numColor = numVar; // default k: number of vars

        // record starting indices for clusters
        int varStart = 0 * numVar; // size: numVar
        int negVarStart = 1 * numVar; // size: numVar
        int TStart = 2 * numVar; // size: 1
        int FStart = 2 * numVar + 1; // size: 1
        int BStart = FStart + 1; // size: numColor -2
        int gadgetStart = BStart + numColor - 2; // size: 6 * number of clauses
        // the list of starting indices of all gadgets
        ArrayList<Integer> gadgetStarts = new ArrayList<>();
        gadgetStarts.add(gadgetStart);

        // make pos vars nodes
        for(int i = 1; i <= numVar; i++){
            nodes.add(new Node(i));
        }

        // make neg vars nodes
        for(int i = negVarStart; i < TStart; i++){
            nodes.add(new Node(i + 1));
        }

        // make T and F nodes
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

            // get all 3 literal nodes
            Node node1 = getNodeByLiteral(nodes, numVar, literals.get(0));;
            Node node2 = getNodeByLiteral(nodes, numVar, literals.get(1));;
            Node node3 = getNodeByLiteral(nodes, numVar, literals.get(2));;

            // the ending vertex to connect B and F
            Node res = makeGadget(nodes, graphColor.getEdges(), curGadgetStart, node1, node2, node3);

            Edge edge1 = new Edge(res, B);
            Edge edge2 = new Edge(res, F);
            graphColor.addEdge(edge1);
            graphColor.addEdge(edge2);

            // add starting index of the gadget nodes for this clause
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

        // connect B, gadget nodes except the very base vertex
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

    /**
     * reduces 3-SAT to Clique problem
     * @param sat3
     * @return
     */
    public static Clique reduce3SATToClique(ThreeSAT sat3){
        Clique clique = new Clique();
        ArrayList<Clause> clauses = sat3.getClauses();
        ArrayList<Node> nodes = new ArrayList<>();
        ArrayList<Edge> edges = new ArrayList<>();
        HashMap<Integer, Integer> clauseIndexMap = new HashMap<>();
        int pointer = 0;
        int kSize = sat3.getClauses().size();

        // make nodes: size of nodes is the number of literals
        for(int i = 0; i < clauses.size(); i++){
            clauseIndexMap.put(i, pointer);
            int start = clauseIndexMap.get(i);
            for(int j = 0; j < 3; j++){
                Node node = new Node(start + j + 1);
                nodes.add(node);
                pointer++;
            }
        }

        // make edges
        for(int i = 0; i < clauses.size(); i++){
            if(i == clauses.size() - 1){ // if the clause is the last one
                break;
            }
            // for each literal node in the current clause, connect
            for(int nodeIndex = clauseIndexMap.get(i); nodeIndex < clauseIndexMap.get(i + 1); nodeIndex++){
                Node node1 = nodes.get(nodeIndex);

                // for each other clause (only connects nodes in other clauses)
                for(int j = i + 1; j < clauses.size(); j++){
                    Clause nextClause = clauses.get(j);

                    // for each node in the next clause
                    for(int k = 0; k < nextClause.getNumLiteral(); k++){
                        // if the literal values of 2 nodes are complements, not connected
                        if(nextClause.getLiterals().get(k) == -1 * getLiteralByNode(clauses, node1)){
                            continue;
                        }
                        // else connect
                        Node targetNode = nodes.get(clauseIndexMap.get(j) + k);
                        Edge edge = new Edge(node1, targetNode);
                        if(!edges.contains(edge))
                            edges.add(edge);
                    }
                }
            }
        }

        clique.setEdges(edges);
        clique.setNodes(nodes);
        clique.setNumEdge(edges.size());
        clique.setNumNode(nodes.size());
        clique.setClauseIndexMap(clauseIndexMap);
        clique.setK(kSize);

        return clique;
    }

    /**
     * reduces Clique problem to Vertex Cover
     * constructs a complement graph of clique graph
     * @param clique
     * @return
     */
    public static VertexCover reduceCliqueToVertexCover(Clique clique){
        VertexCover vertexCover = new VertexCover();
        ArrayList<Node> nodes = clique.getNodes();
        ArrayList<Edge> cliqueEdges = clique.getEdges();
        ArrayList<Edge> edges = new ArrayList<>();
        HashMap<Integer, Integer> clauseIndexMap = clique.getClauseIndexMap();
        int kSize = nodes.size() - clique.getK();

        // find edges that not included in the clique
        for(int i = 0; i < nodes.size(); i++){
            for(int j = i + 1; j < nodes.size(); j++){
                Edge edge = new Edge(nodes.get(i), nodes.get(j));
                if(cliqueEdges.contains(edge)){
                  continue;
                }
                else{
                    if(!edges.contains(edge))
                        edges.add(edge);
                }
            }
        }

        vertexCover.setEdges(edges);
        vertexCover.setNodes(new ArrayList<>(nodes));
        vertexCover.setNumEdge(edges.size());
        vertexCover.setNumNode(nodes.size());
        vertexCover.setClauseIndexMap(new HashMap<>(clauseIndexMap));
        vertexCover.setK(kSize);

        return vertexCover;
    }


}
