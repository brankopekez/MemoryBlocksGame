import javax.swing.*;
import java.awt.*;

public class MemoryBlock extends JButton {
    private final int value;
    private boolean isFlipped;

    public MemoryBlock(int value) {
        this.value = value;
        this.isFlipped = false;
        setPreferredSize(new Dimension(100, 100));
        setFont(new Font("Arial", Font.BOLD, 40));
    }

    public int getValue() {
        return value;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public void flip() {
        isFlipped = true;
        setText(String.valueOf(value));
    }

    public void unflip() {
        isFlipped = false;
        setText("");
    }
}