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
    private MemoryBlock flippedBlock;
    private List<MemoryBlock> blocks;

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

        frame.pack();
        frame.setVisible(true);
    }

    public void checkMatch(MemoryBlock block) {
        if (block == flippedBlock) {
            return;
        }

        if (flippedBlock == null) {
            flippedBlock = block;
        } else {
            if (flippedBlock.getValue() == block.getValue()) {
                flippedBlock.setEnabled(false);
                block.setEnabled(false);
            } else {
                // Delay a bit before unflipping the unmatched blocks
                MemoryBlock t = flippedBlock;
                Timer timer = new Timer(300, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        t.unflip();
                        block.unflip();
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
            flippedBlock = null;
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
                                game.checkMatch(block);
                            }
                        }
                    });
                }
            }
        });
    }
}