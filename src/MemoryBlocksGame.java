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
    private MemoryBlock last;
    private MemoryBlock nextToTheLast;
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

        frame.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.PAGE_END);

        JLabel statusLine = new JLabel("Vrijeme: ", SwingConstants.LEADING);
        statusLine.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        statusLine.setFont(new Font("Arial", Font.BOLD, 18));
        frame.add(statusLine, BorderLayout.PAGE_END);

        frame.pack();
        frame.setVisible(true);
    }

    public void checkMatch(MemoryBlock block) {
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