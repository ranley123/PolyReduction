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

    public static GraphColor reduce3SATToGraphColoring(ThreeSAT sat3){
        GraphColor graphColor = new GraphColor();
        int numVar = sat3.getNumVar();
        int numClause = sat3.getClauses().size();
        ArrayList<Node> nodes = new ArrayList<>();
        ArrayList<Clause> clauses = sat3.getClauses();
        // 1 to n(numNode) are variables
        // n + 1 to 2n are neg variables
        // 2n + 1 to 2n + m (numClause) are clause variables

        // make nodes
        for(int i = 1; i <= numVar; i++){
            nodes.add(new Node(i));
        }

        for(int i = 1; i <= numVar; i++){
            nodes.add(new Node(-1 * i));
        }

        int varStart = 0 * numVar;
        int negVarStart = 1 * numVar;
        int colorStart = 2 * numVar;
        int clauseStart = 3 * numVar;

        for(int i = 0; i < numVar; i++){
            nodes.add(new Node(colorStart + i + 1));
        }

        for(int i = 0; i < numClause; i++){
            nodes.add(new Node(clauseStart + i + 1));
        }

        graphColor.setNodes(nodes);
        graphColor.setNumNode(nodes.size());
        graphColor.setNumColor(numVar);

        // connect var nodes to negvar nodes
        for(int i = 0; i < numVar; i++){
            Edge newEdge = new Edge(nodes.get(varStart + i), nodes.get(negVarStart + i));
            graphColor.addEdge(newEdge);
        }

        // connect var and negvar to color nodes
        for(int i = 0; i < numVar; i++){
            Node var = nodes.get(varStart + i);
            Node negVar = nodes.get(negVarStart + i);
            for(int j = 0; j < numVar; j++){
                if(i == j)
                    continue;
                Node color = nodes.get(colorStart + j);
                Edge edge1 = new Edge(var, color);
                Edge edge2 = new Edge(negVar, color);
                graphColor.addEdge(edge1);
                graphColor.addEdge(edge2);
            }
        }

        // connect color to color nodes
        for(int i = 0; i < numVar; i++){
            Node color1 = nodes.get(colorStart + i);
            for(int j = i + 1; j < numVar; j++){
                Node color2 = nodes.get(colorStart + j);
                Edge edge = new Edge(color1, color2);
                graphColor.addEdge(edge);
            }
        }

        // connect clause node to var nodes
        for(int i = 0; i < numClause; i++){
            Node clauseNode = nodes.get(clauseStart + i);
            Clause clause = clauses.get(i);

            for(int j = 0; j < numVar; j++){
                Node var = nodes.get(varStart + j);
                Node negVar = nodes.get(negVarStart + j);
                int value = var.getId();

                if(clause.hasLiteral(value)){
                    Edge edge = new Edge(negVar, clauseNode);
                    graphColor.addEdge(edge);
                }
                else if(clause.hasLiteral(value * -1)){
                    Edge edge = new Edge(var, clauseNode);
                    graphColor.addEdge(edge);
                }
                else{
                    Edge edge1 = new Edge(var, clauseNode);
                    Edge edge2 = new Edge(negVar, clauseNode);
                    graphColor.addEdge(edge1);
                    graphColor.addEdge(edge2);
                }
            }
        }

        graphColor.setNumEdge(graphColor.getEdges().size());
        return graphColor;
    }


}
