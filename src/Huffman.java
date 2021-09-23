import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Huffman {


    /**
     * Initialize global variables you create
     */
    public Huffman() {
        //TODO

    }

    /**
     * Produces the output frequency.txt
     *
     * @param input - File containing the message
     * @throws Exception
     */
    public void frequency(String input) throws Exception {
        //TODO
        BufferedWriter writer = new BufferedWriter(new FileWriter("frequency.txt"));
        File file = new File(input);
        Scanner sc = new Scanner(file);

        //LinkedHashMap preserves the order that it was put in
        LinkedHashMap<Character, Integer> freq = new LinkedHashMap<Character, Integer>();

        //obtain character one by one from each line and hash it into its corresponding key
        while (sc.hasNextLine()) {
            String line = sc.nextLine();

            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);

                if (c == ' ') {
                    if (freq.containsKey('S')) {
                        freq.put('S', freq.get('S') + 1);
                    } else {
                        freq.put('S', 1);
                    }
                } else {
                    if (freq.containsKey(c)) {
                        freq.put(c, freq.get(c) + 1);
                    } else {
                        freq.put(c, 1);
                    }
                }
            }

            //reached end of the line
            if (sc.hasNextLine()) {
                if (freq.containsKey('N')) {
                    freq.put('N', freq.get('N') + 1);
                } else {
                    freq.put('N', 1);
                }
            }
        }

        try {
            for (Character key : freq.keySet()) {
                if (key == 'S') {
                    writer.write(key + " " + freq.get(key) + "\n");
                } else if (key == 'N') {
                    writer.write(key + " " + freq.get(key) + "\n");
                } else {
                    writer.write(key + " " + freq.get(key) + "\n");
                }
                writer.flush();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Construct a Huffman tree from the frequencies given in the text file as a parameter
     * Produces the output codes.txt and tree.txt
     *
     * @param freqFile - File containing the frequencies
     * @throws Exception
     */
    public void buildTree(String freqFile) throws Exception {
        /** use the queue below **/
        PriorityQueue<HuffmanNode> queue = new PriorityQueue<>();
        //TODO
        LinkedHashMap<Character, String> codeMap = new LinkedHashMap<>();
        Queue<String> treeMap = new LinkedList<>();

        BufferedWriter treeWriter = new BufferedWriter(new FileWriter("tree.txt"));
        BufferedWriter codeWriter = new BufferedWriter(new FileWriter("codes.txt"));
        File file = new File(freqFile);
        Scanner sc = new Scanner(file);

        while (sc.hasNextLine()) {
            HuffmanNode node = new HuffmanNode();
            String[] arr = new String[2];
            String line = sc.nextLine();

            arr[0] = line.split(" ")[0];
            arr[1] = line.split(" ")[1];

            char letter = arr[0].charAt(0);
            int freq = Integer.parseInt(arr[1]);

            //create a single node treeWriter
            node.letter = letter;
            node.frequency = freq;
            queue.add(node);
        }

        while (queue.size() > 1) {
            HuffmanNode left = queue.poll();
            HuffmanNode right = queue.poll();

            //Create a new node with the smallest node as children to merge as one
            HuffmanNode node = new HuffmanNode();
            node.frequency = left.frequency + right.frequency;
            node.left = left;
            node.right = right;
            queue.add(node);
        }

        HuffmanNode root = queue.peek();
        tree(root, "", treeMap);

        codeTree(root, "", codeMap);

        try {
            while (!treeMap.isEmpty()) {
                String remove = treeMap.poll();
                treeWriter.write(remove + "\n");
                treeWriter.flush();
            }
            treeWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //write the binary codeWriter for each character
        try {
            for (char key : codeMap.keySet()) {
                codeWriter.write(key + " " + codeMap.get(key) + "\n");
                codeWriter.flush();
            }
            codeWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tree(HuffmanNode root, String route, Queue treeMap) {
        Stack<HuffmanNode> stack = new Stack<HuffmanNode>();
        stack.push(root);

        String left = "";
        String right = "";
        String middle = "";

        while (stack.size() > 0) {
            HuffmanNode middleNode = stack.pop();
            HuffmanNode leftNode = middleNode.left;
            HuffmanNode rightNode = middleNode.right;

            left = "";
            right = "";
            middle = Integer.toString(middleNode.frequency);
            if (rightNode != null) {
                if (rightNode.letter != '\0') {//node has a character
                    right = Character.toString(rightNode.letter);
                } else { //The node is the root, not a character
                    stack.push(middleNode.right);
                    right = Integer.toString(middleNode.right.frequency);
                }
            }
            if (leftNode != null) {
                if (leftNode.letter != '\0') {//node has a character
                    left = Character.toString(leftNode.letter);
                } else { //The node is the root, not a character
                    stack.push(middleNode.left);
                    left = Integer.toString(middleNode.left.frequency);
                }
            }
            treeMap.add(left + "-" + middle + "-" + right);
        }

    }


    public void codeTree(HuffmanNode root, String code, LinkedHashMap codeMap) {
        if (root == null) {
            return;
        }

        //add 0 for left node
        if (root.left != null) {
            codeTree(root.left, code + "0", codeMap);
        }
        //inorder traversal
        if ((root.left == null) && (root.right == null)) {
            codeMap.put(root.letter, code);
        }

        //add 1 for right node
        if (root.right != null) {
            codeTree(root.right, code + "1", codeMap);
        }
    }

    /**
     * Produces the output encode.bin
     *
     * @param code    - File containing the bit codes
     * @param message -  File containing the message
     * @throws Exception
     */
    public void encode(String code, String message) throws Exception {
        //TODO
        BufferedWriter writer = new BufferedWriter(new FileWriter("encode.bin"));

        HashMap<Character, String> codeMap = new HashMap<Character, String>();

        File codeFile = new File(code);
        Scanner codeSc = new Scanner(codeFile);

        while (codeSc.hasNextLine()) {
            String line = codeSc.nextLine();
            char letter = line.charAt(0);
            String biCode = line.substring(2, line.length());
            codeMap.put(letter, biCode);
        }

        File messageFile = new File(message);
        Scanner codeMsg = new Scanner(messageFile);

        try {
            int total = 1141;
            while (codeMsg.hasNextLine()) {
                int index = 0;
                String line = codeMsg.nextLine();

                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);

                    if (Character.isWhitespace(c)) {
                        index += codeMap.get('S').length();
                        writer.write(codeMap.get('S'));
                    } else {
                        index += codeMap.get(c).length();
                        writer.write(codeMap.get(c));
                    }

                    writer.flush();
                }
                //end of line
                if (codeMsg.hasNextLine()) {
                    writer.write(codeMap.get('N'));
                }
            }
            writer.write('\n');
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Produces the output decode.txt
     *
     * @param tree   - File containing the Huffman tree
     * @param encode - - File containing the encoded message
     * @throws Exception
     */
    public void decode(String tree, String encode) throws Exception {
        //TODO
        Queue<HuffmanNode> queue = new LinkedList<>();
        String[] codeMap = new String[1];
        codeMap[0] = "";

        BufferedWriter decodeWriter = new BufferedWriter(new FileWriter("decode.txt"));

        File file = new File(encode);
        Scanner sc = new Scanner(file);
        String msg = sc.nextLine();

        file = new File(tree);
        sc = new Scanner(file);
        String[] arr = new String[3];
        String line = sc.nextLine();
        arr[0] = line.split("-")[0];
        arr[1] = line.split("-")[1];
        arr[2] = line.split("-")[2];

        HuffmanNode node;
        HuffmanNode root = new HuffmanNode();
        root.left = new HuffmanNode();
        root.right = new HuffmanNode();


        root.frequency = Integer.parseInt(arr[1]);
        if (arr[0].matches("\\d*")) {
            root.left.frequency = Integer.parseInt(arr[0]);
        } else {
            root.left.letter = arr[0].charAt(0);
        }

        if (arr[2].matches("\\d*")) {
            root.right.frequency = Integer.parseInt(arr[2]);
        } else {
            root.right.letter = arr[2].charAt(0);
        }

        queue.add(root);

        while (sc.hasNextLine()) {//add all the node into the priority queue
            HuffmanNode leftNode = new HuffmanNode();
            HuffmanNode middleNode = new HuffmanNode();
            HuffmanNode rightNode = new HuffmanNode();

            line = sc.nextLine();
            arr[0] = line.split("-")[0];
            arr[1] = line.split("-")[1];
            arr[2] = line.split("-")[2];

            String left = arr[0];
            String middle = arr[1];
            String right = arr[2];

            if (left.matches("\\d*")) {
                leftNode.frequency = Integer.parseInt(left);
            } else {
                leftNode.letter = left.charAt(0);
            }

            if (middle.matches("\\d*")) {
                middleNode.frequency = Integer.parseInt(middle);
            } else {
                middleNode.letter = middle.charAt(0);
            }

            if (right.matches("\\d*")) {
                rightNode.frequency = Integer.parseInt(right);
            } else {
                rightNode.letter = right.charAt(0);
            }

            //link the new node to the existing node from the queue
            while (queue.size() > 0) {
                node = queue.poll();

                if (node.frequency == middleNode.frequency) {
                    node.right = rightNode;
                    node.left = leftNode;

                    if (node.left.frequency != 0) {
                        queue.add(node.left);
                    }
                    if (node.right.frequency != 0) {
                        queue.add(node.right);
                    }
                    break;
                } else {
                    if (node.left != null) {
                        queue.add(node.left);
                    }
                    if (node.right != null) {
                        queue.add(node.right);
                    }
                    if (node.left == null && node.right == null) {
                        queue.add(node);
                    }
                }
            }
        }

        int index = 0;
        decodeTree(root, root, msg, index, codeMap);
        decodeWriter.write(codeMap[0]);
        decodeWriter.flush();
        decodeWriter.write('\n');
        decodeWriter.close();
    }

    public void decodeTree(HuffmanNode root, HuffmanNode node, String msg, int index, String[] codeMap) {
        if (node == null && index >= msg.length()) {
            return;
        }
        while (index <= msg.length()) {
            if (node.left == null && node.right == null) {
                //            try {
                if (node.letter == 'S') {
                    codeMap[0] = codeMap[0] + " ";
                } else if (node.letter == 'N') {
                    codeMap[0] = codeMap[0] + "\n";
                } else {
                    codeMap[0] = codeMap[0] + node.letter;
                }

                node = root;
                if (index >= msg.length()) {
                    break;
                }
            }
            if (index < msg.length()) {
                if (msg.charAt(index) == '0' && node.left != null) {
                    index = index + 1;
                    node = node.left;
                } else if (msg.charAt(index) == '1' && node.right != null) {
                    index = index + 1;
                    node = node.right;
                }
            }
        }
    }

    /**
     * Auxiliary class for Huffman
     */
    class HuffmanNode implements Comparable<HuffmanNode> {
        int frequency;
        int index;
        char letter;
        HuffmanNode left;
        HuffmanNode right;
        HuffmanNode prev;


        /**
         * Uses frequency to determine the nodes order in the queue
         * Note: DO NOT MODIFY THIS FUNCTION
         *
         * @param node of type HuffmanNode
         * @return frequency of key node subtracted by frequency of node from parameter
         */
        @Override
        public int compareTo(HuffmanNode node) {
            return frequency - node.frequency;
        }

    }


}