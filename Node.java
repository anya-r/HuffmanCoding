//nested node class
public class Node implements Comparable<Node>
{
    int value;
    Node left;
    Node right;
    int frequency;

    Node(int value, int freq, Node l, Node r) 
    {
        this.value = value;
        frequency = freq;
        left = l;
        right = r;
    }

    public boolean isLeaf()
    {
        //System.out.println("IS LEAF");
        if (left == null && right == null)
            return true;
        return false; 
    }

    public int compareTo(Node other) 
    {
        if (this.frequency > other.frequency)
            return 1;
        if (this.frequency < other.frequency)
            return -1;
        return 1;
    }


}