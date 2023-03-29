import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;


public class HuffmanTree
{        
    public Node root;
    public String[] codes = new String[257];

    public HuffmanTree(int[] counts)
    {
        Queue<Node> tree = new PriorityQueue<Node>();

        for (int i = 0; i < counts.length; i++)
        {
            if (counts[i] > 0)
            {
                tree.add(new Node(i, counts[i], null, null));
            }
        }
        tree.add(new Node(256, 1, null, null));

        while (tree.size() > 1)
        {
            Node x = tree.poll();
            Node y = tree.poll();
            Node combined = new Node(-1, (x.frequency + y.frequency), x, y);
            tree.add(combined);
        }
        root = tree.poll();
        

    }

    public void write(String fileName) throws IOException
    {

        PrintWriter codeOut = new PrintWriter(new File(fileName + ".code"));
        Node temp = root;
        
        writeHelper(temp, "", codeOut);

		codeOut.close();
        
    }
    
        private void writeHelper(Node node, String code, PrintWriter out) 
        {
            if (node == null) 
                return;
            
            if (node.isLeaf()) 
            {
                out.println(node.value);
                out.println(code);
                codes[node.value] = code;
            }
            
            else 
            {
                writeHelper(node.left, code + "0", out);
                writeHelper(node.right, code + "1", out);
            }
        }
    public Node getRoot()
    {
        return root;
    }


    public void encode(BitOutputStream out, String fileName) throws IOException
    {
        out = new BitOutputStream(fileName + ".short");
        Node temp = root;
        encodeHelper(temp, "", out);

        out.close();

    }
        private void encodeHelper(Node node, String code, BitOutputStream stream)
        {
            if (node == null)
                return;
            if (node.isLeaf())
            {
                for (int i = 0; i < code.length(); i++)
                {
                    char c = code.charAt(i);
                    if (c == '0')
                        stream.writeBit(0);
                    else if (c == '1')
                        stream.writeBit(1);
                }
            }

            else
            {
                encodeHelper(node.left, code + "0", stream);
                encodeHelper(node.right, code + "1", stream);
            }

        }

    public HuffmanTree(String codeFile) throws IOException
    {
        Node decodeRoot = new Node(-1,0, null, null);
        Scanner input = new Scanner(new File(codeFile));
        Node current;

        while (input.hasNextLine()) 
        {
            current = decodeRoot;
       
            int asciiCode = Integer.parseInt(input.nextLine());
            String path = input.nextLine();

            char c = '\0';

            for (int i = 0; i < path.length(); i++) 
            {                
                c = path.charAt(i);

                if (c == '0') 
                {
                    if (current.left == null) {
                        current.left = new Node(-1, -1, null, null);
                    }
                    
                    if (i == path.length() - 1) 
                    {
                        current.left.value = asciiCode;
                    }
                    else
                    {
                        current = current.left;
                    }
                    
                } 

                else if (c == '1') 
                {
                    if (current.right == null) {
                        current.right = new Node(-1, -1, null, null);
                    }
                    if (i == path.length() - 1) 
                    {
                        current.right.value = asciiCode;
                    }
                    else
                    {
                        current = current.right;
                    }
                
                }
               
            }
            
        }

        input.close();
        root = decodeRoot;
    }

    public void decode(BitInputStream in, String outFile) throws IOException
    {
        String fileName = outFile + ".new";
        PrintWriter output = new PrintWriter(new File(fileName));
        int bit;
        Node temp = root;
        boolean hitEOF = false;
        while (!hitEOF)
        {
            if (temp.isLeaf())
            {
                output.print((char)temp.value);
                temp = root;

            }
            else
            {
                bit = in.readBit();

                if (bit == -1)
                {
                    hitEOF = true;
                    break;
                }
                if (bit == 0)
                {
                    temp = temp.left;
                }
                else if (bit == 1)
                {
                    temp = temp.right;
                }
            }
        }
        output.close();
    }


    public String getCode(char x)
    {
        return codes[x];
    }
       

}

