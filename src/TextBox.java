import javax.swing.*;
import javax.swing.event.CaretListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TextBox extends JTextPane implements KeyListener {
    TextBox(Container container, JFrame frame) throws BadLocationException{
        this.addKeyListener(this);
        StyledDocument doc = (StyledDocument) getDocument();
        this.setFont(new Font("Monospaced", Font.BOLD,20));
    }

    @Override
    public void keyTyped(KeyEvent e) {
//        System.out.println("1");

    }
    @Override
    public void keyPressed(KeyEvent e) {
    }
    boolean checkFront(int i, String text) {
        if (text.length()-1 == i) return true;
        else if (text.charAt(i+1) == 13 || text.charAt(i+1) == ' '|| text.charAt(i+1) == '\t') return true;
        else return false;
    }
    boolean checkBack(int i, String text, int size) {
        if (i-size+1 == 0) return true;
        else if (text.charAt(i-size) == '\n' || text.charAt(i-size) == ' ' || text.charAt(i-size) == ';' || text.charAt(i-size) == '\t') return true;
        else return false;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        String text = getText();

        if (e.getKeyChar() == '{' ||  e.getKeyChar() == '[') {
            try {
                getDocument().insertString(getCaretPosition(),(char)(2+e.getKeyChar())+"",null);
                setCaretPosition(getCaretPosition()-1);
            } catch (BadLocationException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getKeyChar() == '(') {
            try {
                getDocument().insertString(getCaretPosition(),(char)(1+e.getKeyChar())+"",null);
                setCaretPosition(getCaretPosition()-1);
            } catch (BadLocationException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getKeyChar() == 39 || e.getKeyChar() == '"') {
            try {
                getDocument().insertString(getCaretPosition(),e.getKeyChar()+"",null);
                setCaretPosition(getCaretPosition()-1);
            } catch (BadLocationException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getKeyChar() == '\t') {
            try {
                getDocument().remove(getCaretPosition()-1,1);
                getDocument().insertString(getCaretPosition(),"    ",null);
            } catch (BadLocationException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getKeyCode() == 10) {
            int cr = 0;
//            System.out.println(text.charAt(getCaretPosition()-2));
            for (int g = 0 ; g < getCaretPosition() ; g++) {
                if (text.charAt(g) == 13)
                    cr++;
            }

            if (text.charAt(getCaretPosition()-2+cr) == '{') {
                System.out.println("jjjj");
                int k,count = 0;
                for (k = getCaretPosition()-3 ; text.charAt(k) == ' ' ; k--) {count++;}
                if (text.charAt(k) == 10 || text.charAt(k) == 13) {
                    String f = "";
                    while (count > 0) {
                        f += " ";
                    }
                    System.out.println(count);
                    try {
                        getDocument().insertString(getCaretPosition(),"\n\n"+f,null);
                    } catch (BadLocationException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
        SimpleAttributeSet hs = new SimpleAttributeSet();
        StyleConstants.setForeground(hs,Color.BLACK);
        StyledDocument doc = (StyledDocument) getDocument();
        doc.setCharacterAttributes(0,doc.getLength(),hs,true);

        SimpleAttributeSet ss = new SimpleAttributeSet();
        StyleConstants.setForeground(ss,Color.BLUE);
        StyleConstants.setUnderline(ss,true);
        StyleConstants.setRightIndent(ss, 5);

        int noCR = 0;
        for (int i = 2 ; i < text.length() ; i++) {
            char x = text.charAt(i);
            if (x == 13) noCR++;
            else if (x == 'r' && text.charAt(i-1) == 'o' && text.charAt(i-2) == 'f') {
                if (checkFront(i,text) && checkBack(i,text,3)) {
                    doc.setCharacterAttributes(i-2-noCR,3,ss,true);
                }
            } else if (x == 'f' && text.charAt(i-1) == 'i') {
                if (checkFront(i,text) && checkBack(i,text,2)) {
                    doc.setCharacterAttributes(i-1-noCR,2,ss,true);
                }
            } else if (x == 'e' && text.charAt(i-1) == 's' && text.charAt(i-2) == 'l' && text.charAt(i-3) == 'e') {
                if (checkFront(i,text) && checkBack(i,text,4)) {
                    doc.setCharacterAttributes(i-3-noCR,4,ss,true);
                }
            }
        }
    }
}
