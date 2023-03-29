import java.io.File;
import java.util.Arrays;
import java.util.Scanner;
import java.io.IOException;

public class HuffmanCompressor
{
    public static void compress(String fileName) throws IOException
    {
        int index = fileName.indexOf(".txt");
        String name = fileName.substring(0, index);

        int[] charFrequencies = new int[256];

        Scanner scanner = new Scanner(new File(fileName));
        while (scanner.hasNextLine())
        {
            String line = scanner.nextLine();
            for (int i = 0; i < line.length(); i++)
            {
                    int ascii = (int)line.charAt(i);
                    charFrequencies[ascii]++;
            }

        }
        HuffmanTree tree = new HuffmanTree(charFrequencies);
        tree.write(name);

        scanner = new Scanner(new File(fileName));

        BitOutputStream stream = new BitOutputStream(name + ".short");

        while (scanner.hasNextLine())
        {
            String line = scanner.nextLine();
            
            char[] chars = line.toCharArray();
            
                for (int i = 0; i < chars.length; i++)
                {
                    String path = tree.getCode(chars[i]);

                    for (int j = 0; j < path.length(); j++)
                    {
                        if (path.charAt(j) == '0')
                            stream.writeBit(0);
                        else
                            stream.writeBit(1);
                            
                    }
                }
            }
            scanner.close();


    }
    public static void expand(String codeFile, String fileName) throws IOException
    {
        HuffmanTree rebuild = new HuffmanTree(codeFile);
        BitInputStream stream = new BitInputStream(fileName + ".short");
        rebuild.decode(stream, fileName);
    }


    public static void main(String[] args) throws IOException 
    {
        compress("happy hip hop.txt");
        expand("happy hip hop.code", "happy hip hop");
    }


}