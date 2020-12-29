import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.HashMap;
import java.util.Map;

public class kitFrame extends JFrame {
    Clipboard clipboard;
    HashMap<String, String> menuMap = new HashMap();
    JPanel namePanel, xPanel, yPanel, costPanel, resultPanel, jlPanel, fzPanel, showPanel, lorePanel, radioPanel, commandPanel, menuPanel;
    JTextField yField = new JTextField(30);
    JTextField menuField = new JTextField(30);
    JTextField xField = new JTextField(30);
    JTextField costField = new JTextField(30);
    JTextField nameField = new JTextField(30);
    JTextArea jlField = new JTextArea(8, 30);
    JTextField showField = new JTextField(30);
    JTextField loreField = new JTextField(30);
    JTextField commandField = new JTextField(30);
    JTextArea resultArea = new JTextArea(20, 40);
    ButtonGroup g1 = new ButtonGroup();
    JRadioButton yesButton = new JRadioButton("有附魔效果");
    JRadioButton noButton = new JRadioButton("无附魔效果", true);
    JButton fzButton = new JButton("复制代码"), resultButton = new JButton("显示结果"), menuButton = new JButton("自动复制权限代码");
    Box vB1 = Box.createVerticalBox();

    public kitFrame(String title) {
        super(title);
        menuMap = initMap();
        init();
        initField();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    public void init() {
        addActions(nameField);
        addActions(loreField);
        addActions(showField);
        addActions(yField);
        addActions(xField);
        addActions(costField);
        addActions(commandField);
    }


    public void initField() {
        clipboard = this.getToolkit().getSystemClipboard();
        resultPanel = new JPanel();
        jlField.setLineWrap(true);
        JLabel resultLabel = new JLabel("菜单代码:");
        JLabel jlLabel = new JLabel("简略输入:");
        g1.add(noButton);
        g1.add(yesButton);
        radioPanel = new JPanel();
        resultArea.setLineWrap(true);
        resultPanel = new JPanel();
        fzPanel = new JPanel();
        fzPanel.add(resultButton);
        fzPanel.add(fzButton);

        menuPanel = createPane("菜单名称:", menuField);
        menuPanel.add(menuButton);

        showPanel = createPane("显示图标:", showField);
        lorePanel = createPane("描述信息", loreField);
        namePanel = createPane("    礼包名:", nameField);
        yPanel = createPane("格子行数:", yField);
        xPanel = createPane("格子列数:", xField);
        costPanel = createPane("        花费:", costField);
        jlPanel = new JPanel();
        jlPanel.add(jlLabel);
        jlPanel.add(jlField);
        commandPanel = createPane("执行命令:", commandField);
        radioPanel.add(noButton);
        radioPanel.add(yesButton);
        vB1.add(new JScrollPane(jlPanel));
        vB1.add(namePanel);
        vB1.add(lorePanel);
        vB1.add(showPanel);
        vB1.add(costPanel);
        vB1.add(yPanel);
        vB1.add(xPanel);
        vB1.add(commandPanel);
        vB1.add(radioPanel);
        resultPanel.add(resultLabel);
        resultPanel.add(new JScrollPane(resultArea));
        vB1.add(resultPanel);
        vB1.add(menuPanel);
        this.add(vB1, BorderLayout.CENTER);
        this.add(fzPanel, BorderLayout.SOUTH);

        jlField.addCaretListener(e -> {
            if (jlField.getText().length() != 0) {
                nameField.setEditable(false);
                costField.setEditable(false);
                xField.setEditable(false);
                yField.setEditable(false);
                loreField.setEditable(false);
                showField.setEditable(false);
                commandField.setEditable(false);
            } else {
                nameField.setEditable(true);
                costField.setEditable(true);
                xField.setEditable(true);
                yField.setEditable(true);
                loreField.setEditable(true);
                showField.setEditable(true);
                commandField.setEditable(true);
            }
        });
        resultButton.addActionListener(e -> {
            if (isIllegal()) {
                JOptionPane.showMessageDialog(this, "x和y不要留空!");
            } else if (isIntergerIllegal()) {
                JOptionPane.showMessageDialog(this, "x应该在1~9之间,y应该在1~6之间!");
            } else {
                resultArea.setText("");
                if (jlField.getText().length() != 0) {
                    resultArea.setText(menuJl(jlField.getText()));
                } else {
                    resultArea.setText(output());
                }

            }
        });
        fzButton.addActionListener(e -> {
            onCopy();
        });
        menuButton.addActionListener(e -> {
            onCopyMenu();
        });

    }

    private void onCopy() {
        StringSelection copyResult = new StringSelection(resultArea.getText());
        clipboard.setContents(copyResult, null);
    }

    private void onCopyMenu() {
        StringBuilder sb = new StringBuilder();
        sb.append("lp group default permission set chestcommands.open.").append(menuField.getText()).append(".yml");
        StringSelection copyResult = new StringSelection(sb.toString());
        clipboard.setContents(copyResult, null);
    }

    public JPanel createPane(String name, JTextField textField) {
        JPanel tempPanel = new JPanel();
        JLabel tempLabel = new JLabel(name, JLabel.RIGHT);
        tempPanel.add(tempLabel);
        tempPanel.add(textField);
        Border emptyBorder = BorderFactory.createEmptyBorder(8, 0, 8, 0);
        tempPanel.setBorder(emptyBorder);
        return tempPanel;
    }

    public HashMap<String, String> initMap() {
        HashMap<String, String> m = new HashMap<>();
        m.put("1", "A");
        m.put("2", "B");
        m.put("3", "C");
        m.put("4", "D");
        m.put("5", "E");
        m.put("6", "F");
        return m;
    }

    public String menuJl(String rtemp) {
        StringBuilder sb = new StringBuilder();
        String[] input = rtemp.split("\\|");
        String[] message = new String[7];
        for (int i = 0; i < input.length && i <= 6; i++) {
            message[i] = input[i];
        }

        sb.append(menuMap.get(message[5])).append(message[4]).append(":\n");
        sb.append("  NAME: '").append(message[0]).append("'\n");
        if (!message[1].isEmpty()) {
            sb.append("  LORE:\n");
            String[] lores = message[1].split(";");
            for (int j = 0; j < lores.length; j++) {
                sb.append("    - '").append(lores[j]).append("'\n");
            }
        }
        if (!message[2].isEmpty()) {
            if (message[2].contains(":")) {
                String[] shows = message[2].split(":");
                sb.append("  ID: ").append(shows[0]).append("\n").append("  DATA-VALUE: ").append(shows[1]).append("\n");
            } else {
                sb.append("  ID: ").append(message[2]).append("\n");
            }
        }
        if (!message[3].isEmpty()) {
            if (message[3].contains("p")) {
                String[] points = message[3].split("p");
                sb.append("  POINTS: ").append(points[0]).append("\n");
            } else if (message[3].contains("v")) {
                String[] vaults = message[3].split("v");
                sb.append("  PRICE: ").append(vaults[0]).append("\n");
            }
        }
        if (!message[4].isEmpty()) {
            sb.append("  COMMAND: '").append(message[4]).append("'\n");
        }
        if (yesButton.isSelected()) {
            sb.append("  ENCHANTMENT: 'PROTECTION,1'\n");
        }
        sb.append("  POSITION-X: ").append(message[6]).append("\n");
        sb.append("  POSITION-Y: ").append(message[5]).append("\n");
        return sb.toString();
    }

    public void addActions(JTextField jTextField) {
        jTextField.addCaretListener(e -> {
            if (allEmpty()) {
                jlField.setEditable(true);
            } else {
                jlField.setEditable(false);
            }
        });
    }

    public boolean isEmpty(JTextField jTextField) {
        if (jTextField.getText().length() != 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean allEmpty() {
        if (isEmpty(nameField) && isEmpty(showField) && isEmpty(loreField) && isEmpty(xField) && isEmpty(yField) && isEmpty(costField) && isEmpty(commandField)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean allFull() {
        if (isEmpty(nameField) || isEmpty(showField) || isEmpty(loreField) || isEmpty(xField) || isEmpty(yField) || isEmpty(costField) || isEmpty(commandField)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isIllegal() {
        if ((xField.getText().length() == 0 || yField.getText().length() == 0) && jlField.getText().length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isIntergerIllegal() {
        if (jlField.getText().length() == 0 && Integer.valueOf(xField.getText()) > 9 || Integer.valueOf(xField.getText()) < 1 || Integer.valueOf(yField.getText()) > 6 || Integer.valueOf(yField.getText()) < 1) {
            return true;
        } else {
            return false;
        }
    }

    public String output() {
        StringBuilder sb = new StringBuilder();
        String name = null, lore = null, show = null, pay = null, y = null, x = null, command = null;
        name = nameField.getText();
        show = showField.getText();
        pay = costField.getText();
        lore = loreField.getText();
        y = yField.getText();
        x = xField.getText();
        command = commandField.getText();
        sb.append(menuMap.get(y)).append(x).append(":\n");
        sb.append("  NAME: '").append(name).append("'\n");
        if (!lore.isEmpty()) {
            sb.append("  LORE:\n");
            String[] lores = lore.split(";");
            for (int j = 0; j < lores.length; j++) {
                sb.append("    - '").append(lores[j]).append("'\n");
            }
        }
        if (!show.isEmpty()) {
            if (show.contains(":")) {
                String[] shows = show.split(":");
                sb.append("  ID: ").append(shows[0]).append("\n").append("  DATA-VALUE: ").append(shows[1]).append("\n");
            } else {
                sb.append("  ID: ").append(show).append("\n");
            }
        }
        if (!pay.isEmpty()) {
            if (pay.contains("p")) {
                String[] points = pay.split("p");
                sb.append("  POINTS: ").append(points[0]).append("\n");
            } else if (pay.contains("v")) {
                String[] vaults = pay.split("v");
                sb.append("  PRICE: ").append(vaults[0]).append("\n");
            }
        }
        if (!command.isEmpty()) {
            sb.append("  COMMAND: '").append(command).append("'\n");
        }
        if (yesButton.isSelected()) {
            sb.append("  ENCHANTMENT: 'PROTECTION,1'\n");
        }
        sb.append("  POSITION-X: ").append(x).append("\n");
        sb.append("  POSITION-Y: ").append(y).append("\n");
        return sb.toString();
    }

}
