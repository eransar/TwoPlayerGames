import java.util.List;

public class Minimax implements ISolver {
    @Override
    public String getSolverName() {
        return "Minimax";
    }

    @Override
    public double solve
            (IBoard board) {
        Node root = new Node(board, Node.NodeType.MAX);
        return MinimaxAlgorithm(root);
    }


    private double MinimaxAlgorithm
            (
                    Node node
            ) {
        // Initialization
        double value;
        if (node.getNodeType() == Node.NodeType.MAX)
            value = -Double.MAX_VALUE;
        else
            value = Double.MAX_VALUE;

        // Explore Child Nodes
        List<Node> children = node.getNodeChildren();
        for (Node child : children) {
            double childValue;
            if (child.isTerminalNode())
                childValue = child.getScore();
            else
                childValue = MinimaxAlgorithm(child);
            if (node.getNodeType() == Node.NodeType.MAX)
                value = Double.max(value, childValue);
            else
                value = Double.min(value, childValue);
        }
        return value;
    }

}
