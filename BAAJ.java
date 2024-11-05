import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
public class BAAJ
{
    private JFrame frame;
    private CardLayout cardLayout;
    public static int numEncryptions;

    public BAAJ() {
        frame = new JFrame("BAAJ CIPHER");
        cardLayout = new CardLayout();
        frame.setLayout(cardLayout);

        JPanel encryptPanel = new JPanel();
        encryptPanel.setLayout(new GridLayout(7, 1));
        JTextField plainTextField = new JTextField();
        JButton nextButtonEncrypt = new JButton("Next");
        JButton backButtonEncrypt = new JButton("Back");
        JTextArea outputFieldEncrypt = new JTextArea();
        outputFieldEncrypt.setEditable(false);
        JTextField outputFieldEncrypt2 = new JTextField();
        outputFieldEncrypt2.setEditable(false);

        encryptPanel.add(new JLabel("Enter Plain Text:"));
        encryptPanel.add(plainTextField);
        encryptPanel.add(new JLabel("Encrypted Text:"));
        encryptPanel.add(outputFieldEncrypt);
        encryptPanel.add(new JLabel("Number of Encryptions:"));
        encryptPanel.add(outputFieldEncrypt2);
        encryptPanel.add(nextButtonEncrypt);
        encryptPanel.add(backButtonEncrypt);

        JPanel decryptPanel = new JPanel();
        decryptPanel.setLayout(new GridLayout(5, 1));
        JTextField cipherTextField = new JTextField();
        JTextField keyField = new JTextField();
        JButton nextButtonDecrypt = new JButton("Next");
        JButton backButtonDecrypt = new JButton("Back");
        JTextArea outputFieldDecrypt = new JTextArea();
        outputFieldDecrypt.setEditable(false);

        decryptPanel.add(new JLabel("Enter Cipher Text:"));
        decryptPanel.add(cipherTextField);
        decryptPanel.add(new JLabel("Enter Key (Number):"));
        decryptPanel.add(keyField);
        decryptPanel.add(nextButtonDecrypt);
        decryptPanel.add(outputFieldDecrypt);
        decryptPanel.add(backButtonDecrypt);

        frame.add(encryptPanel, "Encrypt");
        frame.add(decryptPanel, "Decrypt");

        
        JPanel mainPanel = new JPanel();
        JButton encryptButton = new JButton("Encrypt");
        JButton decryptButton = new JButton("Decrypt");

        encryptButton.addActionListener(e -> cardLayout.show(frame.getContentPane(), "Encrypt"));
        decryptButton.addActionListener(e -> cardLayout.show(frame.getContentPane(), "Decrypt"));

        mainPanel.add(encryptButton);
        mainPanel.add(decryptButton);

        frame.add(mainPanel, "Main");


        nextButtonEncrypt.addActionListener(e -> {
            String plainText = plainTextField.getText();
            String encryptedText = encrypt(plainText);
            outputFieldEncrypt.setText(encryptedText);
            outputFieldEncrypt2.setText(String.valueOf(numEncryptions));
        });

        nextButtonDecrypt.addActionListener(e -> {
            String cipherText = cipherTextField.getText();
            int key;
            try {
                key = Integer.parseInt(keyField.getText());
                String decryptedText = decrypt(cipherText, key);
                outputFieldDecrypt.setText(decryptedText);
            } catch (NumberFormatException ex) {
                outputFieldDecrypt.setText("Please enter a valid number for the key.");
            }
        });

        
        backButtonEncrypt.addActionListener(e -> cardLayout.show(frame.getContentPane(), "Main"));
        backButtonDecrypt.addActionListener(e -> cardLayout.show(frame.getContentPane(), "Main"));

        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setVisible(true);
        cardLayout.show(frame.getContentPane(), "Main");
    }

    public static int generateRandomIterations() {
        Random rand = new Random();
        return rand.nextInt(5) + 1;
    }

    public static String vigenereEncrypt(String text, String key) {
        StringBuilder result = new StringBuilder();
        text = text.toUpperCase();
        key = key.toUpperCase();
        for (int i = 0, j = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isLetter(c)) {
                result.append((char) ((c + key.charAt(j) - 2 * 'A') % 26 + 'A'));
                j = (j + 1) % key.length();
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public static String columnarTransposition(String text, int numCols) {
        int numRows = (int) Math.ceil((double) text.length() / numCols);
        char[][] grid = new char[numRows][numCols];

        int pos = 0;
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                if (pos < text.length()) {
                    grid[row][col] = text.charAt(pos++);
                } else {
                    grid[row][col] = 'X';
                }
            }
        }

        StringBuilder transposedText = new StringBuilder();
        for (int col = 0; col < numCols; col++) {
            for (int row = 0; row < numRows; row++) {
                transposedText.append(grid[row][col]);
            }
        }

        return transposedText.toString();
    }

    public static String encrypt(String plainText) {
        String key = "ukybdsayTFAvhvavjda";
        numEncryptions = generateRandomIterations();
        String encryptedText = plainText;
        for (int i = 0; i < numEncryptions; i++) {
            encryptedText = vigenereEncrypt(encryptedText, key);
        }
        encryptedText = columnarTransposition(encryptedText, 5);
        return encryptedText;
    }

    public static String vigenereDecrypt(String text, String key) {
        StringBuilder result = new StringBuilder();
        text = text.toUpperCase();
        key = key.toUpperCase();

        for (int i = 0, j = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isLetter(c)) {
                result.append((char) ((c - key.charAt(j) + 26) % 26 + 'A'));
                j = (j + 1) % key.length();
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public static String columnarTranspositionDecrypt(String text, int numCols) {
        int numRows = (int) Math.ceil((double) text.length() / numCols);
        char[][] grid = new char[numRows][numCols];
        int pos = 0;
        for (int col = 0; col < numCols; col++) {
            for (int row = 0; row < numRows; row++) {
                if (pos < text.length()) {
                    grid[row][col] = text.charAt(pos++);
                } else {
                    grid[row][col] = 'X';
                }
            }
        }
        StringBuilder transposedText = new StringBuilder();
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                transposedText.append(grid[row][col]);
            }
        }
        return transposedText.toString().replaceAll("X+$", "");
    }

    public static String decrypt(String cipherText, int numEncryptions) {
        String key = "ukybdsayTFAvhvavjda";
        String decryptedText = columnarTranspositionDecrypt(cipherText, 5);
        for (int i = 0; i < numEncryptions; i++) {
            decryptedText = vigenereDecrypt(decryptedText, key);
        }
        return decryptedText;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BAAJ::new);
    }
}