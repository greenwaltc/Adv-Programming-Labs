package spell;

public class Trie implements ITrie {

    private Node root = new Node();
    private Node currentNode = new Node();
    private int wordCount = 0;
    private int nodeCount = 1; //The root node counts as 1
    private static boolean equal;

    public Trie() {
    }

    @Override
    public void add(String word) {

        currentNode = root;
        int index;
        String w = word.toLowerCase();
        char[] characters = w.toCharArray();

        for (int i = 0; i < characters.length; i++){

            index = characters[i] - 'a';

            if (currentNode.nodes[index] == null){
                currentNode.nodes[index] = new Node();
                nodeCount++;
            }

            currentNode = currentNode.nodes[index];

            if (i == characters.length - 1){

                if (currentNode.getValue() == 0) wordCount++;
                currentNode.incrementValue();
            }
        }
    }

    @Override
    public INode find(String word) {
        currentNode = root;
        int index;
        String w = word.toLowerCase();
        char[] characters = w.toCharArray();

        for (int i = 0; i < characters.length; ++i){

            index = characters[i] - 'a';

            if (i < characters.length - 1){

                if (currentNode.nodes[index] != null){

                    currentNode = currentNode.nodes[index];
                }
                else {

                    return null;
                }
            }

            else if ( (i == characters.length - 1) &&
                    (currentNode.nodes[index] != null) &&
                    (currentNode.nodes[index].getValue() > 0) ){

                currentNode = currentNode.nodes[index];
            }

            else {

                return null;
            }
        }
        return currentNode;
    }



    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }

    /*------------------------*/

    @Override
    public String toString() {

        StringBuilder curWorld = new StringBuilder();
        StringBuilder output = new StringBuilder();

        toString_Helper(root, curWorld, output);

        return output.toString();
    }

    private void toString_Helper(INode n, StringBuilder curWord, StringBuilder output){

        if (n.getValue() > 0){
            output.append(curWord.toString());
            output.append("\n");
        }

        for (int i = 0; i < n.getChildren().length; ++i){

            INode child = n.getChildren()[i];

            if (child != null){

                char childLetter = (char)('a' + i);
                curWord.append(childLetter);

                toString_Helper(child, curWord, output);

                curWord.deleteCharAt(curWord.length() - 1);
            }
        }

    }

    @Override
    public int hashCode() {

        int temp = 0;

        for (int i = root.nodes.length - 1; i >= 0; --i){

            if (root.nodes[i] != null){

                temp = i;
                break;
            }
        }

        return nodeCount * wordCount * temp;
    }

    @Override
    public boolean equals(Object obj) {

        Trie other = (Trie)obj;

        //check for null
        //check for this
        //check the classes
        //check nodeCount and wordCount
        if (other == null ||
            this.getClass() != obj.getClass() ||
            (this.nodeCount != other.nodeCount || this.wordCount != other.wordCount)){

            return false;
        }

        //compare to objects
        equal = true;

        return equals_Helper(root, other.root);
    }

    private boolean equals_Helper(Node n1, Node n2) {

        if (n1 == null && n2 == null){

            return equal;
        }

        else if (n1 != null && n2 != null) {

            //Compare count in the two nodes
            if (n1.getValue() != n2.getValue()) {
                equal = false;
                return equal;
            }

            //Check to see if they have children in the same positions
            for (int i = 0; i < n1.nodes.length; ++i) {

                if ((n1.nodes[i] == null && n2.nodes[i] != null) ||
                    (n1.nodes[i] != null && n2.nodes[i] == null)) {

                    return equal;
                }
            }

            //Recursively compare the children
            for (int i = 0; i < n1.nodes.length; ++i) {

                equals_Helper(n1.nodes[i], n2.nodes[i]);
            }

            return equal;
        }

        else{

            return equal;
        }
    }

}