package spell;

class Node implements INode {

    private Integer count = 0;
    Node[] nodes = new Node[26];

    @Override
    public int getValue() {
        return count;
    }

    @Override
    public void incrementValue() {
        ++count;
    }

    @Override
    public Node[] getChildren() {
        return nodes;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}