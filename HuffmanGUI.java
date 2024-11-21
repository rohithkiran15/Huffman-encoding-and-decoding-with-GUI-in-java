import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.awt.*;
import java.awt.event.*;

// Node class for Huffman tree
class Node {
    char ch;
    int freq;
    Node left, right;

    Node(char ch, int freq, Node left, Node right) {
        this.ch = ch;
        this.freq = freq;
        this.left = left;
        this.right = right;
    }
}

// Huffman encoding and decoding
class HuffmanCoding {
    private static Map<Character, String> huffmanCode = new HashMap<>();
    private static Map<String, Character> reverseHuffmanCode = new HashMap<>();

    // Function to build Huffman Tree
    public static Node buildHuffmanTree(String text) {
        if (text == null || text.length() == 0) {
            return null;
        }

        // count frequency of appearance of each character
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : text.toCharArray()) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }

        // Create a priority queue to store live nodes of Huffman tree
        PriorityQueue<Node> pq = new PriorityQueue<>((l, r) -> l.freq - r.freq);

        // Create a leaf node for each character and add it to the priority queue.
        for (Map.Entry<Character, Integer> entry : freq.entrySet()) {
            pq.add(new Node(entry.getKey(), entry.getValue(), null, null));
        }

        // do till there is more than one node in the queue
        while (pq.size() != 1) {
            // Remove the two nodes of highest priority (lowest frequency) from the queue
            Node left = pq.poll();
            Node right = pq.poll();

            // Create a new internal node with these two nodes as children and with frequency equal to the sum of the two nodes' frequencies. Add the new node to the priority queue.
            int sum = left.freq + right.freq;
            pq.add(new Node('\0', sum, left, right));
        }

        // root stores pointer to the root of Huffman Tree
        return pq.peek();
    }

    // Traverse the Huffman Tree and store Huffman Codes in a map.
    public static void encode(Node root, String str) {
        if (root == null) {
            return;
        }

        // found a leaf node
        if (isLeaf(root)) {
            huffmanCode.put(root.ch, str);
            reverseHuffmanCode.put(str, root.ch);
        }

        encode(root.left, str + '0');
        encode(root.right, str + '1');
    }

    // Utility function to check if Huffman Tree contains only a single node
    public static boolean isLeaf(Node root) {
        return root.left == null && root.right == null;
    }

    // Function to encode given text
    public static String encodeText(String text) {
        Node root = buildHuffmanTree(text);
        encode(root, "");
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            sb.append(huffmanCode.get(c));
        }
        return sb.toString();
    }

    // Function to decode the encoded text
    public static String decodeText(String encodedText) {
        StringBuilder sb = new StringBuilder();
        String temp = "";
        for (char bit : encodedText.toCharArray()) {
            temp += bit;
            if (reverseHuffmanCode.containsKey(temp)) {
                sb.append(reverseHuffmanCode.get(temp));
                temp = "";
            }
        }
        return sb.toString();
    }
}

public class HuffmanGUI extends Frame implements ActionListener {
    TextArea inputText;
    TextArea outputText;
    Button encodeButton;
    Button decodeButton;
    int len_plainText= 0;
    int len_encodeText= 0;
    Label statusLabel;
    String text = "Enter the text bits is "+len_plainText+" and result bits is "+len_encodeText;

    HuffmanGUI() {
        // Create a frame
        Frame frame = new Frame("Huffman Encoding/Decoding");

        // Create text areas
        inputText = new TextArea("", 10, 40);
        outputText = new TextArea("", 10, 40);
        // outputText.setEditable(false);

        // Create buttons
        encodeButton = new Button("Encode");
        decodeButton = new Button("Decode");

        // Set layout
        frame.setLayout(new BorderLayout(10, 10));

        // Add components to frame
        Panel textPanel = new Panel(new GridLayout(1, 2, 10, 10));
        textPanel.add(inputText);
        textPanel.add(outputText);

        Panel buttonPanel = new Panel(new FlowLayout());
        buttonPanel.add(encodeButton);
        buttonPanel.add(decodeButton);
        statusLabel = new Label(text, Label.CENTER);
        frame.add(statusLabel, BorderLayout.NORTH);
        frame.add(textPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        encodeButton.addActionListener(this);
        decodeButton.addActionListener(this);

        // Set frame properties
        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        String input = inputText.getText();
        String encodedinput = outputText.getText();
        if (e.getSource() == encodeButton) {
            String encodedText = HuffmanCoding.encodeText(input);
            outputText.setText(encodedText);
        } else if (e.getSource() == decodeButton) {
            String decodedText = HuffmanCoding.decodeText(encodedinput);
            inputText.setText(decodedText);
        }
        len_encodeText = outputText.getText().length();
        len_plainText = input.length();
        text = "Enter the text bits is "+len_plainText*8+" and result bits is "+len_encodeText;
        statusLabel.setText(text);
    }

    public static void main(String[] args) {
        new HuffmanGUI();
    }
}