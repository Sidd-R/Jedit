import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

class MainFrame extends JFrame implements ActionListener {
    static MainFrame mainFrame;
    public static void main(String[] args) throws BadLocationException {
        mainFrame = new MainFrame();
    }
    static MainFrame getFrame() {return mainFrame;}
    TextBox textBox;
    JScrollPane scrollPane;
    JMenuBar menuBar;
    JLabel bottomBar;
    static String currentFile;
    JMenu file, edit, appearance, fontSize;
    JMenuItem open, saveAs, save, exit, findReplace, copyAll, selectAll, paste, fontColor, fullScreenMode;
    JSpinner spinner;

    MainFrame() throws BadLocationException {
        super("Jedit");
        initiateComponents();
        initiateMenuBar();
        initiateMainFrame(false);
    }
    void initiateComponents() throws BadLocationException {
        // mainframe components
        textBox = new TextBox(getContentPane(),this);
        scrollPane = new JScrollPane(textBox);
        bottomBar = new JLabel();
        menuBar = new JMenuBar();

//        textBox.addKeyListener();

        // menu-bar components
        open = new JMenuItem("open");
        saveAs = new JMenuItem("save as");
        save = new JMenuItem("save");
        exit = new JMenuItem("exit");
        findReplace = new JMenuItem("find & replace");
        copyAll = new JMenuItem("copy all");
        selectAll = new JMenuItem("select all");
        paste = new JMenuItem("paste");
        file = new JMenu("file");
        edit = new JMenu("edit");
        appearance = new JMenu("appearance");
        fontSize = new JMenu("font size");
        spinner = new JSpinner();
        fontColor = new JMenuItem("font color");
        fullScreenMode = new JMenuItem("enter fullscreen");
    }
    void initiateMainFrame(boolean unDecorated) {
        setUndecorated(unDecorated);

        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setFocusable(false);
//        scrollPane.getTopLevelAncestor().setBackground(Color.GRAY);
        scrollPane.getVerticalScrollBar().setBackground(Color.DARK_GRAY);
//        textArea.setLineWrap(true);
//        textArea.setWrapStyleWord(true);
        add(scrollPane);
        add(bottomBar,BorderLayout.SOUTH);
        setJMenuBar(menuBar);


        setVisible(true);
//        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400,400));
        setLocation(40,40);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        /*addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(getFrame(),
                        "Are you sure you want to close this window?", "Close Window?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                    System.exit(0);
                }
            }
        });*/
    }

    void initiateMenuBar(){
        fontSize.add(spinner);
        spinner.setValue(20);
        spinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                textBox.setFont(new Font(textBox.getFont().getFamily(),Font.PLAIN,(int)spinner.getValue()));
            }
        });

        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(appearance);
        file.add(open);
        file.add(save);
        file.add(saveAs);
        file.add(exit);
//        edit.add(findReplace);
        edit.add(selectAll);
        edit.add(copyAll);
        edit.add(paste);
        appearance.add(fontSize);
        appearance.add(fontColor);
        appearance.add(fullScreenMode);

        open.addActionListener(this);
        save.addActionListener(this);
        saveAs.addActionListener(this);
        exit.addActionListener(this);
        findReplace.addActionListener(this);
        copyAll.addActionListener(this);
        selectAll.addActionListener(this);
        paste.addActionListener(this);
        fontColor.addActionListener(this);
        fullScreenMode.addActionListener(this);

        file.setFocusable(false);
        edit.setFocusable(true);
        file.setFont(new Font(file.getFont().getFamily(),Font.PLAIN,15));
//            UIManager.put("MenuBar.selectionBackground", Color.decode("#0092e4"));
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == open) open();
        else if (e.getSource() == save) if (currentFile == null) saveAs(); else save();
        else if (e.getSource() == saveAs) saveAs();
        else if (e.getSource() == exit) System.exit(0);
        else if (e.getSource() == findReplace) findReplace();
        else if (e.getSource() == selectAll) textBox.selectAll();
        else if (e.getSource() == copyAll) copy(textBox.getText());
        else if (e.getSource() == paste) textBox.paste();
        else if (e.getSource() == fontColor) textBox.setForeground(JColorChooser.showDialog(null,"choose a color", Color.BLACK));
        else if (e.getSource() == fullScreenMode) fullscreen();
    }

    void open() {
        JFileChooser fc = new JFileChooser();
        int i = fc.showOpenDialog(null);
        if (i == JFileChooser.APPROVE_OPTION) {
            FileReader fr = null;
            try {
                fr = new FileReader(fc.getSelectedFile().getAbsolutePath());
                getFrame().setTitle(fc.getSelectedFile().getAbsolutePath());
                String text = "";
                int data = fr.read();
                while (data != -1) {
                    text += (char)data;
                    data = fr.read();
                }
                textBox.setText(text);
                fr.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    void save() {
        try {
            FileWriter fw = new FileWriter(currentFile);
            String text = textBox.getText();
            for (int i = 0 ; i < text.length() ; i++) {
                fw.write(text.charAt(i));
            }
            fw.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    void saveAs() {
        JFileChooser fc = new JFileChooser();
        int i = fc.showSaveDialog(this);
        if (i == JFileChooser.APPROVE_OPTION) {
            try {
                FileWriter fw = new FileWriter(fc.getSelectedFile().getAbsolutePath());
                String text = textBox.getText();
                for (i = 0 ; i < text.length() ; i++) {
                    fw.write(text.charAt(i));
                }
                fw.close();

                currentFile = fc.getSelectedFile().getAbsolutePath();
                getFrame().setTitle(currentFile);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    void copy(String text) {
        StringSelection ss = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(ss,null);
    }
    void findReplace() {
        textBox.select(3,10);
        JPanel panel = new JPanel();
        JLabel labToFind ,labToReplace, label;
        JTextField find, replace;
        label = new JLabel("Find & Replace");
        labToFind = new JLabel("\nTo Find:");
        labToReplace = new JLabel("To Replace:");
        find = new JTextField();
        replace = new JTextField();
        label.setPreferredSize(new Dimension(200,50));
        labToFind.setPreferredSize(new Dimension(200,25));
        labToReplace.setPreferredSize(new Dimension(200,25));
        find.setPreferredSize(new Dimension(180,25));
        replace.setPreferredSize(new Dimension(180,25));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(new Font(null,Font.BOLD,22));

        panel.add(label);
        panel.add(labToFind);
        panel.add(find);
        panel.add(labToReplace);
        panel.add(replace);

        panel.setPreferredSize(new Dimension(200,400));
        panel.setLayout(new FlowLayout());
        panel.setOpaque(false);

        getFrame().add(panel,BorderLayout.EAST);
        SwingUtilities.updateComponentTreeUI(getFrame());
    }
    void fullscreen() {
        if (isUndecorated()) {
            dispose();
            initiateMainFrame(false);
            fullScreenMode.setText("enter fullscreen");
        }
        else {
            dispose();
            initiateMainFrame(true);
            fullScreenMode.setText("exit fullscreen");
        }
    }

}