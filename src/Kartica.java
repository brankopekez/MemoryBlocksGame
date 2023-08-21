import javax.swing.*;
import java.awt.*;

public class Kartica extends JButton {
    private final int vrijednost;
    private boolean okrenuta;

    public Kartica(int value) {
        this.vrijednost = value;
        this.okrenuta = false;
        setPreferredSize(new Dimension(100, 100));
        setFont(new Font("Arial", Font.BOLD, 40));
    }

    public int getVrijednost() {
        return vrijednost;
    }

    public boolean jeOkrenuta() {
        return okrenuta;
    }

    public void okreniNaLice() {
        okrenuta = true;
        setText(String.valueOf(vrijednost));
    }

    public void okreniNaNalicje() {
        okrenuta = false;
        setText("");
    }
}