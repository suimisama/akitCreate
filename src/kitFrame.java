import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import javax.swing.undo.UndoManager;


public class kitFrame extends JFrame {
    JTabbedPane jTabbedPane = new JTabbedPane(JTabbedPane.TOP);
    // 菜单粗略设计栏的组件
    DealProperties dealProperties = new DealProperties();
    ResourceBundle resourceBundle = ResourceBundle.getBundle("config");
    String isInitPath = resourceBundle.getString("isInitPath");
    boolean isFull = true;
    int nums = 0;
    Clipboard clipboard;
    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("文件"), settingMenu = new JMenu("设置");
    JMenuItem createPathItem = new JMenuItem("选择菜单存放路径"), alwaysFrontItem = new JMenuItem("总在最前");
    ArrayList<String> saveList = new ArrayList<>();
    HashMap<String, String> menuMap = new HashMap<>();
    HashMap<String, String> colorMap = new HashMap<>();
    HashMap<Integer, String> clearMap = new HashMap<>();
    JPanel namePanel, xPanel, yPanel, costPanel, resultPanel, jlPanel, fzPanel, fzPanel2, showPanel, lorePanel, radioPanel, commandPanel, menuPanel, newMenuPanel, authorPanel, colorDescriptionPanel;
    JLabel authorLabel = new JLabel("作者:W_Pencil,版本号:1.0"), colorLabel1 = new JLabel("&4=大红 &c=浅红 &6=土黄 &e=金黄 &2=绿 &a=浅绿 &b=蓝绿"),
            colorLabel2 = new JLabel("&3=天蓝 &1=深蓝 &9=蓝紫 &d=粉红 &5=品红 &f=白 &7=灰 &8=深灰\n"), colorLabel3 = new JLabel("&0=黑 &l=加粗 &o=倾斜 &n=下划线 &m=删除线");
    JTextField newMenuField = new JTextField(30);
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
    ButtonGroup g1 = new ButtonGroup(), g2 = new ButtonGroup();
    JRadioButton yesButton = new JRadioButton("有附魔效果");
    JRadioButton noButton = new JRadioButton("无附魔效果", true);
    JRadioButton yesOpenButton = new JRadioButton("有KEEP-OPEN");
    JRadioButton noOpenButton = new JRadioButton("无KEEP-OPEN", true);
    JButton fzButton = new JButton("复制代码"), clearButton = new JButton("清空结果"), clearRearButton = new JButton("删除尾部"), resultButton = new JButton("显示结果"), menuButton = new JButton("复制权限代码"), appendButton = new JButton("尾部添加");
    JButton mbButton = new JButton("生成文件");
    Box vB1 = Box.createVerticalBox(), vB2 = Box.createVerticalBox(), vB3 = Box.createHorizontalBox(),  vB4 = Box.createVerticalBox();
    UndoManager undoManager = new UndoManager();

    // 菜单详细细节添加


    public kitFrame(String title) {
        super(title);
        if (isInitPath.equals("true")) {
            initProperties();
        }
        initManager();
        initMenu();
        initMap();
        init();
        initField();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.setSize(950, 560);
        this.pack();
        centerOnScreen();
        this.setVisible(true);
    }

    public void initJTabbedPanel() {
        jTabbedPane.addTab("粗略编辑", vB1);
        jTabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        jTabbedPane.addTab("详细参数", vB4);
        jTabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        this.add(jTabbedPane);
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

    public void initMenu() {
        fileMenu.add(createPathItem);

        settingMenu.add(alwaysFrontItem);

        menuBar.add(fileMenu);
        menuBar.add(settingMenu);

        createPathItem.addActionListener(e -> {
            onCreateChooser();
            String key = "isInitPath";
            String finalValue = "false";
//            dealProperties.setProper(key, finalValue);
        });
        alwaysFrontItem.addActionListener(e -> {
            this.setAlwaysOnTop(true);
        });
        this.setJMenuBar(menuBar);
    }

    private void onCreateChooser() {
        boolean flag = true;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int ret = fileChooser.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File getPath = fileChooser.getSelectedFile();
            String key = "path";
            String finalValue = getPath.getAbsolutePath();
//            dealProperties.setProper(key, finalValue);
        }
    }

    public void initField() {

        colorDescriptionPanel = new JPanel();
        colorDescriptionPanel.setLayout(new BorderLayout());
        colorDescriptionPanel.add(colorLabel1, BorderLayout.NORTH);
        colorDescriptionPanel.add(colorLabel2, BorderLayout.CENTER);
        colorDescriptionPanel.add(colorLabel3, BorderLayout.SOUTH);

        colorLabel3.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 0));
        colorLabel2.setBorder(BorderFactory.createEmptyBorder(0, 8, 4, 0));
        colorLabel1.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));

        authorPanel = new JPanel();
        authorPanel.add(authorLabel);
        clipboard = this.getToolkit().getSystemClipboard();
        resultPanel = new JPanel();
        jlField.setLineWrap(true);

        JLabel resultLabel = new JLabel("菜单代码:");
        JLabel jlLabel = new JLabel("简略输入:");
        JLabel fenge = new JLabel(" | ");

        g1.add(noButton);
        g1.add(yesButton);
        g2.add(yesOpenButton);
        g2.add(noOpenButton);

        radioPanel = new JPanel();

        resultArea.setLineWrap(true);

        resultPanel = new JPanel();

        fzPanel = new JPanel();
        fzPanel2 = new JPanel();
        fzPanel.add(resultButton);
        fzPanel.add(fzButton);
        fzPanel.add(appendButton);
        fzPanel2.add(clearButton);
        fzPanel2.add(clearRearButton);
        fzPanel2.add(mbButton);

        menuPanel = createPane("菜单名称:", menuField);
        menuPanel.add(menuButton);

        showPanel = createPane("显示图标:", showField);
        lorePanel = createPane("描述信息:", loreField);
        namePanel = createPane("格子名称:", nameField);
        yPanel = createPane("格子行数:", yField);
        xPanel = createPane("格子列数:", xField);
        costPanel = createPane("消耗金额:", costField);
        newMenuPanel = createPane("新菜单名:", newMenuField);

        jlPanel = new JPanel();
        jlPanel.add(jlLabel);
        jlPanel.add(jlField);
        jlPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        commandPanel = createPane("执行命令:", commandField);

        radioPanel.add(noButton);
        radioPanel.add(yesButton);
        radioPanel.add(fenge);
        radioPanel.add(noOpenButton);
        radioPanel.add(yesOpenButton);

        vB1.add(jlPanel);
        vB1.add(newMenuPanel);
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
        vB2.add(resultPanel);
        vB2.add(menuPanel);
        vB2.add(fzPanel);
        vB2.add(fzPanel2);
        vB2.add(colorDescriptionPanel);
        initJTabbedPanel();
        vB3.add(jTabbedPane);
        vB3.add(vB2);
        this.add(authorPanel, BorderLayout.PAGE_START);
        this.add(vB3, BorderLayout.CENTER);

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
                    nameField.setText("");
                    costField.setText("");
                    xField.setText("");
                    yField.setText("");
                    loreField.setText("");
                    showField.setText("");
                    commandField.setText("");
                } else {
                    resultArea.setText(output());
                    nameField.setText("");
                    costField.setText("");
                    xField.setText("");
                    yField.setText("");
                    loreField.setText("");
                    showField.setText("");
                    commandField.setText("");
                }
                if (newMenuField.getText().length() != 0) {
                    /*
                     * 二次过滤
                     * 第一次过滤[]的中文颜色前缀,第二次过滤&开头的英文颜色前缀
                     */
                    String temp = newMenuField.getText().substring(newMenuField.getText().lastIndexOf("]") + 1);
                    String finalTemp = temp.substring(temp.lastIndexOf("&") + 2);
                    menuField.setText(finalTemp);
                }
                nums++;
            }
        });


        xField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                isFull = true;
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        yField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                isFull = true;
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        fzButton.addActionListener(e -> {
            onCopy();
        });
        menuButton.addActionListener(e -> {
            onCopyMenu();
        });
        menuField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    showCopyResult();
                }
            }
        });
        appendButton.addActionListener(e -> {

            if (isFull) {
                onAppend();
            }
        });
        clearButton.addActionListener(e -> {
            onClear();
            isFull = true;
        });
        clearRearButton.addActionListener(e -> {
            resultArea.setText(getString(resultArea.getText()));
            if (jlField.getText().length() == 0 && xField.getText().length() != 0 && yField.getText().length() != 0) {
                if (Integer.valueOf(xField.getText()) > 1) {
                    xField.setText(Integer.toString(Integer.valueOf(xField.getText()) - 1));
                } else if (Integer.valueOf(yField.getText()) > 0) {
                    yField.setText(Integer.toString(Integer.valueOf(yField.getText()) - 1));
                    xField.setText("9");
                }
            }
            isFull = true;
        });
        mbButton.addActionListener(e -> {
            try {
                createMenu();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }


    private void onClear() {
        resultArea.setText("");
        nums = 0;
    }

    private void onAppend() {
        if (isIllegal()) {
            JOptionPane.showMessageDialog(this, "x和y不要留空!");
        } else if (isIntergerIllegal()) {
            JOptionPane.showMessageDialog(this, "x应该在1~9之间,y应该在1~6之间!");
        } else {
            if (Integer.valueOf(xField.getText()) < 9) {
                xField.setText(Integer.toString(Integer.valueOf(xField.getText()) + 1));
            } else if (Integer.valueOf(yField.getText()) < 6) {
                yField.setText(Integer.toString(Integer.valueOf(yField.getText()) + 1));
                xField.setText("1");
            }
            if (newMenuField.getText().length() != 0) {
                menuField.setText(replaceSubString(newMenuField.getText()));
            }
            if (jlField.getText().length() != 0) {
                StringBuilder fore = new StringBuilder(resultArea.getText());
                if (!resultArea.getText().trim().equals("")) {
                    fore.append("\n");
                }
                fore.append(menuJl(jlField.getText()));
                resultArea.setText(fore.toString());
            } else {
                StringBuilder fore = new StringBuilder(resultArea.getText());
                if (!resultArea.getText().trim().equals("")) {
                    fore.append("\n");
                }
                fore.append(output());
                resultArea.setText(fore.toString());
            }
            menuField.setForeground(Color.lightGray);
            menuField.setBackground(Color.white);
            if (Integer.valueOf(yField.getText()) == 6 && Integer.valueOf(xField.getText()) == 9) {
                isFull = false;
            }
        }
    }

    public void initManager() {
        resultArea.getDocument().addUndoableEditListener(undoManager);
        resultArea.addKeyListener(new KeyListener() {

            @Override
            public void keyReleased(KeyEvent arg0) {
            }

            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_Z) {
                    if (undoManager.canUndo()) {
                        undoManager.undo();
                    }
                }
                if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_Y) {
                    if (undoManager.canRedo()) {
                        undoManager.redo();
                    }
                }
            }

            @Override
            public void keyTyped(KeyEvent arg0) {
            }
        });
        UndoManager manager = new UndoManager();
        jlField.getDocument().addUndoableEditListener(undoManager);
        jlField.addKeyListener(new KeyListener() {

            @Override
            public void keyReleased(KeyEvent arg0) {
            }

            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_Z) {
                    if (undoManager.canUndo()) {
                        undoManager.undo();
                    }
                }
                if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_Y) {
                    if (undoManager.canRedo()) {
                        undoManager.redo();
                    }
                }
            }

            @Override
            public void keyTyped(KeyEvent arg0) {
            }
        });
    }

    private String getString(String result) {
        int index = 0;
        String[] r = result.split("\\r?\\n");
        StringBuilder sb = new StringBuilder();
        for (int i = r.length - 1; i >= 0; i--) {
            if (r[i].equals("")) {
                index = i;
                break;
            }
        }
        for (int i = 0; i < index; i++) {
            sb.append(r[i]).append("\n");
        }
        return sb.toString();
    }

    private void onCopy() {
        StringSelection copyResult = new StringSelection(resultArea.getText());
        clipboard.setContents(copyResult, null);
    }

    private void onCopyMenu() {
        if (menuField.getText().length() != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("lp group default permission set chestcommands.open.").append(menuField.getText()).append(".yml");
            StringSelection copyResult = new StringSelection(sb.toString());
            menuField.setForeground(Color.lightGray);
            menuField.setBackground(Color.white);
            menuField.setText("复制成功！");
            menuField.setEditable(false);
            clipboard.setContents(copyResult, null);
        } else if (menuField.getText().length() == 0 || "复制成功！".equals(menuField.getText().trim())) {
            JOptionPane.showMessageDialog(this, "菜单名不能为空!");
        }
    }

    public JPanel createPane(String name, JTextField textField) {
        JPanel tempPanel = new JPanel();
        JLabel tempLabel = new JLabel(name, JLabel.RIGHT);
        tempPanel.add(tempLabel);
        tempPanel.add(textField);
        Border emptyBorder = BorderFactory.createEmptyBorder(8, 0, 8, 0);
        tempPanel.setBorder(emptyBorder);
        addUnRedo(textField);
        return tempPanel;
    }

    public void initProperties() {
        String proPath = System.getProperty("user.dir") + "\\src\\config.properties";
        String pathValue = System.getProperty("user.dir") + "\\src\\";
        String key = "path";
//        dealProperties.setProper(key, pathValue);
    }

    public void initMap() {
        menuMap.put("1", "A");
        menuMap.put("2", "B");
        menuMap.put("3", "C");
        menuMap.put("4", "D");
        menuMap.put("5", "E");
        menuMap.put("6", "F");
        colorMap.put("[大红]", "&4");
        colorMap.put("[浅红]", "&c");
        colorMap.put("[土黄]", "&6");
        colorMap.put("[金黄]", "&e");
        colorMap.put("[绿]", "&2");
        colorMap.put("[浅绿]", "&a");
        colorMap.put("[蓝绿]", "&b");
        colorMap.put("[天蓝]", "&3");
        colorMap.put("[深蓝]", "&1");
        colorMap.put("[蓝紫]", "&9");
        colorMap.put("[粉红]", "&d");
        colorMap.put("[品红]", "&5");
        colorMap.put("[白]", "&f");
        colorMap.put("[灰]", "&7");
        colorMap.put("[深灰]", "&8");
        colorMap.put("[黑]", "&0");
        colorMap.put("[加粗}", "&l");
        colorMap.put("[倾斜]", "&o");
        colorMap.put("[下划线]", "&n");
        colorMap.put("[删除线]", "&m");
    }

    public String menuJl(String rtemp) {
        StringBuilder sb = new StringBuilder();
        String[] input = rtemp.split("\\|");
        String[] message = new String[7];
        for (int i = 0; i < input.length && i <= 6; i++) {
            message[i] = input[i];
        }
        if (resultArea.getText().trim().equals("") && !newMenuField.getText().trim().equals("")) {
            sb.append("menu-settings:").append("\n");
            sb.append("  name: '").append(replaceSubString(newMenuField.getText())).append("'\n");
            if (Integer.valueOf(message[4]) < 6) {
                sb.append("  rows: ").append(Integer.valueOf(message[4]) + 1).append("\n");
            } else {
                sb.append("  rows: ").append("6").append("\n");
            }
            sb.append("  open-with-item:\n");
            sb.append("\n");
        }
        sb.append(menuMap.get(message[4])).append(message[5]).append(":\n");
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
        } else {
            sb.append("  ID: ").append("160\n").append("  DATA-VALUE: 7\n");
        }
        if (!message[3].isEmpty()) {
            if (message[3].contains("p")) {
                String[] points = message[3].split("p");
                sb.append("  POINTS: ").append(points[0]).append("\n");
            } else if (message[3].contains("v")) {
                String[] vaults = message[3].split("v");
                sb.append("  PRICE: ").append(vaults[0]).append("\n");
            } else {
                sb.append("  PRICE: ").append(message[3]).append("\n");
            }
        }
        if (!message[6].isEmpty()) {
            sb.append("  COMMAND: '").append(message[4]).append("'\n");
        }
        if (yesButton.isSelected()) {
            sb.append("  ENCHANTMENT: 'PROTECTION,1'\n");
        }
        if (yesOpenButton.isSelected()) {
            sb.append("  KEEP-OPEN: true\n");
        }
        sb.append("  POSITION-X: ").append(message[5]).append("\n");
        sb.append("  POSITION-Y: ").append(message[4]).append("\n");
        return replaceSubString(sb.toString());
    }

    public String replaceSubString(String parent) {
        String a1 = parent.replace("[大红]", "&4");
        String a2 = a1.replace("[浅红]", "&c");
        String a3 = a2.replace("[土黄]", "&6");
        String a4 = a3.replace("[金黄]", "&e");
        String a5 = a4.replace("[绿]", "&2");
        String a6 = a5.replace("[浅绿]", "&a");
        String a7 = a6.replace("[蓝绿]", "&b");
        String a8 = a7.replace("[天蓝]", "&3");
        String a9 = a8.replace("[深蓝]", "&1");
        String a10 = a9.replace("[蓝紫]", "&9");
        String a11 = a10.replace("[粉红]", "&d");
        String a12 = a11.replace("[品红]", "&5");
        String a13 = a12.replace("[白]", "&f");
        String a14 = a13.replace("[灰]", "&7");
        String a15 = a14.replace("[深灰]", "&8");
        String a16 = a15.replace("[黑]", "&0");
        String a17 = a16.replace("[加粗]", "&l");
        String a18 = a17.replace("[倾斜]", "&o");
        String a19 = a18.replace("[下划线]", "&n");
        String a20 = a19.replace("[删除线]", "&m");
        return a20;
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

//    public boolean allFull() {
//        if (isEmpty(nameField) || isEmpty(showField) || isEmpty(loreField) || isEmpty(xField) || isEmpty(yField) || isEmpty(costField) || isEmpty(commandField)) {
//            return false;
//        } else {
//            return true;
//        }
//    }

    public boolean isIllegal() {
        if (jlField.getText().length() == 0 && (xField.getText().length() == 0 || yField.getText().length() == 0)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isIntergerIllegal() {
        if (jlField.getText().length() == 0 && (Integer.valueOf(xField.getText()) > 9 || Integer.valueOf(xField.getText()) < 1 || Integer.valueOf(yField.getText()) > 6 || Integer.valueOf(yField.getText()) < 1)) {
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
        if (resultArea.getText().trim().equals("") && !newMenuField.getText().trim().equals("")) {
            sb.append("menu-settings:").append("\n");
            sb.append("  name: '").append(replaceSubString(newMenuField.getText())).append("'\n");
            sb.append("  rows: ").append(Integer.valueOf(y) + 2).append("\n");
            sb.append("  open-with-item:\n");
            sb.append("\n");
        }
        sb.append(menuMap.get(y)).append(x).append(":\n");
        sb.append("  NAME: '").append(name).append("'\n");
        if (!lore.isEmpty()) {
            sb.append("  LORE:\n");
            String[] lores = lore.split(";");
            for (int j = 0; j < lores.length; j++) {
                replaceSubString(lores[j]);
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
        } else {
            sb.append("  ID: ").append("160\n").append("  DATA-VALUE: 7\n");
        }
        if (!pay.isEmpty()) {
            if (pay.contains("p")) {
                String[] points = pay.split("p");
                sb.append("  POINTS: ").append(points[0]).append("\n");
            } else if (pay.contains("v")) {
                String[] vaults = pay.split("v");
                sb.append("  PRICE: ").append(vaults[0]).append("\n");
            } else {
                sb.append("  PRICE: ").append(pay).append("\n");
            }
        }
        if (!command.isEmpty()) {
            sb.append("  COMMAND: '").append(command).append("'\n");
        }
        if (yesButton.isSelected()) {
            sb.append("  ENCHANTMENT: 'PROTECTION,1'\n");
        }
        if (yesOpenButton.isSelected()) {
            sb.append("  KEEP-OPEN: true\n");
        }
        sb.append("  POSITION-X: ").append(x).append("\n");
        sb.append("  POSITION-Y: ").append(y).append("\n");
        return replaceSubString(sb.toString());
    }

    public void showCopyResult() {
        if (!menuField.isEditable()) {
            menuField.setText("");
            menuField.setForeground(Color.BLACK);
            menuField.setEditable(true);
        }
    }

    public void centerOnScreen() {
        // 获取屏幕大小
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        // 获取软件窗口大小
        int width = this.getWidth();
        int height = this.getHeight();
        // 根据公式让屏幕处于中央
        this.setBounds((screenWidth - width) / 2, (screenHeight - height) / 2, width, height);
    }

    public void save(String path) throws IOException {
        File tempFile = new File(path);
        if (!tempFile.exists()) {
            tempFile.createNewFile();
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        bw.write(resultArea.getText());
        bw.close();
    }

    public void createMenu() throws IOException {
        if (newMenuField.getText().length() != 0 && resultArea.getText().length() != 0) {
            String path = System.getProperty("user.dir") + "\\src\\" + newMenuField.getText().substring(newMenuField.getText().lastIndexOf("]") + 1) + ".yml";
            save(getCreatePath());
            JOptionPane.showMessageDialog(this, "创建成功!");
        } else {
            JOptionPane.showMessageDialog(this, "创建失败！菜单名和菜单代码不能为空！");
        }
    }

    private String getCreatePath() {
//        String path = resourceBundle.getString("path");
        String path = System.getProperty("user.dir") + "\\src\\" + newMenuField.getText() + ".yml";
        return path;
    }

    public void addUnRedo(JTextField jTextField) {
        jTextField.getDocument().addUndoableEditListener(undoManager);
        jTextField.addKeyListener(new KeyListener() {

            @Override
            public void keyReleased(KeyEvent arg0) {
            }

            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_Z) {
                    if (undoManager.canUndo()) {
                        undoManager.undo();
                    }
                }
                if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_Y) {
                    if (undoManager.canRedo()) {
                        undoManager.redo();
                    }
                }
            }

            @Override
            public void keyTyped(KeyEvent arg0) {
            }
        });
    }

    // 获取光标位置
    public void getCursorPosition(JTextArea text) {
        text.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent e) {
                try {
                    int pos = text.getCaretPosition();
                    int row = text.getLineOfOffset(pos) + 1;
                    int col = pos - text.getLineStartOffset(row - 1) + 1;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        });
    }
}
