import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Igra {
    private JFrame okvir;
    private JPanel panelResetke;
    private JLabel labelaTajmera;
    private Kartica posljednja;
    private Kartica pretposljednja;
    private List<Kartica> kartice;
    private boolean zapoceto;
    private long vrijemePocetka;
    private Timer tajmer;
    private int nadjeniParovi;
    private int ukupnoParova;

    public Igra() {
        okvir = new JFrame("Igra Memorijskih Kartica");
        okvir.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panelResetke = new JPanel(new GridLayout(4, 4));
        okvir.add(panelResetke, BorderLayout.CENTER);

        kartice = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            kartice.add(new Kartica(i));
            kartice.add(new Kartica(i));
        }
        Collections.shuffle(kartice);

        for (Kartica kartica : kartice) {
            panelResetke.add(kartica);
        }

        labelaTajmera = new JLabel("Vrijeme: 0 s");
        labelaTajmera.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        labelaTajmera.setFont(new Font("Arial", Font.BOLD, 18));
        okvir.add(labelaTajmera, BorderLayout.SOUTH);

        okvir.pack();
        okvir.setVisible(true);

        zapoceto = false;
        nadjeniParovi = 0;
        ukupnoParova = kartice.size() / 2; // Calculate the total number of pairs

        tajmer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                azurirajLabeluTajmera();
            }
        });
    }

    public void azurirajLabeluTajmera() {
        long trenutnoVrijeme = System.nanoTime();
        long protekloVrijeme = trenutnoVrijeme - vrijemePocetka;
        // 1 s = 1_000_000_000 ns
        double protekloVrijemeSekunde = (double) protekloVrijeme / 1_000_000_000;
        String tekstLabele = String.format("Vrijeme: %.3f s", protekloVrijemeSekunde);
        labelaTajmera.setText(tekstLabele);
    }

    public void provjeriPogodak(Kartica novaKartica) {
        if (!zapoceto) {
            vrijemePocetka = System.nanoTime();
            zapoceto = true;
            tajmer.start();
        }

        if (pretposljednja != null && posljednja == null && novaKartica == pretposljednja) {
            return;
        }

        if (pretposljednja == null) {
            pretposljednja = novaKartica;
        } else if (posljednja == null) {
            posljednja = novaKartica;
            if (pretposljednja.getVrijednost() == posljednja.getVrijednost()) {
                posljednja.setEnabled(false);
                pretposljednja.setEnabled(false);
                pretposljednja = null;
                posljednja = null;

                nadjeniParovi++;
                if (nadjeniParovi == ukupnoParova) {
                    tajmer.stop();
                    azurirajLabeluTajmera();
                    labelaTajmera.setText(labelaTajmera.getText() + ". Igra je završena.");
                }
            }
        } else {
            pretposljednja.okreniNaNalicje();
            posljednja.okreniNaNalicje();
            pretposljednja = novaKartica;
            pretposljednja.okreniNaLice();
            posljednja = null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Igra igra = new Igra();

                for (Kartica kartica : igra.kartice) {
                    kartica.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (!kartica.jeOkrenuta()) {
                                kartica.okreniNaLice();
                            }
                            igra.provjeriPogodak(kartica);
                        }
                    });
                }
            }
        });
    }
}