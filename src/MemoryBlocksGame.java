import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemoryBlocksGame {
    private JFrame frame;
    private JPanel gridPanel;
    private JLabel timerLabel;
    private MemoryBlock last;
    private MemoryBlock nextToTheLast;
    private List<MemoryBlock> blocks;
    private boolean firstClick;
    private long startTime;
    private Timer elapsedTimer;
    private int matchedPairs;
    private int totalPairs;

    public MemoryBlocksGame() {
        frame = new JFrame("Memory Blocks Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gridPanel = new JPanel(new GridLayout(4, 4));
        frame.add(gridPanel, BorderLayout.CENTER);

        blocks = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            blocks.add(new MemoryBlock(i));
            blocks.add(new MemoryBlock(i));
        }
        Collections.shuffle(blocks);

        for (MemoryBlock block : blocks) {
            gridPanel.add(block);
        }

        timerLabel = new JLabel("Vrijeme: 0 s");
        timerLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        timerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        frame.add(timerLabel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);

        firstClick = false;
        matchedPairs = 0;
        totalPairs = blocks.size() / 2; // Calculate the total number of pairs

        // Create a Timer to update the elapsed time label
        elapsedTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTimerLabel();
            }
        });
    }

    public void updateTimerLabel() {
        long currentTime = System.nanoTime();
        long elapsedTime = currentTime - startTime;
        // 1 second = 1_000_000_000 nano seconds
        double elapsedTimeInSecond = (double) elapsedTime / 1_000_000_000;
        String labelText = String.format("Vrijeme: %.3f s", elapsedTimeInSecond);
        timerLabel.setText(labelText);
    }

    public void checkMatch(MemoryBlock block) {
        if (!firstClick) {
            startTime = System.nanoTime();
            firstClick = true;

            // Start the elapsed timer
            elapsedTimer.start();
        }

        if (nextToTheLast != null && last == null && block == nextToTheLast) {
            return;
        }

        if (nextToTheLast == null) {
            nextToTheLast = block;
        } else if (last == null) {
            last = block;
            if (nextToTheLast.getValue() == last.getValue()) {
                last.setEnabled(false);
                nextToTheLast.setEnabled(false);
                nextToTheLast = null;
                last = null;

                matchedPairs++;
                if (matchedPairs == totalPairs) {
                    elapsedTimer.stop();
                    long currentTime = System.nanoTime();
                    long elapsedTime = currentTime - startTime;
                    // 1 second = 1_000_000_000 nano seconds
                    double elapsedTimeInSecond = (double) elapsedTime / 1_000_000_000;
                    String labelText = String.format("Vrijeme: %.3f s. Igra je završena.", elapsedTimeInSecond);
                    timerLabel.setText(labelText);
                }
            }
        } else {
            nextToTheLast.unflip();
            last.unflip();
            nextToTheLast = block;
            nextToTheLast.flip();
            last = null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MemoryBlocksGame game = new MemoryBlocksGame();

                for (MemoryBlock block : game.blocks) {
                    block.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (!block.isFlipped()) {
                                block.flip();
                            }
                            game.checkMatch(block);
                        }
                    });
                }
            }
        });
    }
}