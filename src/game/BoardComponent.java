package game;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * View Component of the game
 * @author Stan Yanakiev
 * @author Marietta Asemwota
 * @author Yihua Li
 */
public class BoardComponent extends JComponent implements ChangeListener
{
    BoardModel boardModel;

    private int design, stones;
    private BoardFormatter b;
    private JFrame frame;
    private JButton undo;

    //we are going to use panels like screens and switch back and forth
    private JPanel panel1;
    private JPanel pitsPanel;
    private int pitCounterA;
    private int pitCounterB;

    private JTextArea playerText;

    private JLabel manAStones;
    private JLabel manBStones;




    public BoardComponent(BoardModel m)
    {
        frame = new JFrame();
        boardModel = m;

        frame.setLayout(new BorderLayout());

        panel1 = new JPanel();

        design = 0;
        stones = 0;

        getDecisions();
    }



    public void getDecisions()
    {
        Object[] designs = {"Design C", "Design B", "Design A"};
        int selectDesign = JOptionPane.showOptionDialog(panel1, "Please choose a design", "Board Formatter",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, designs, designs[2]);
        if(selectDesign == 2)
        {
            b = new DesignA();
            drawBoard(b);
        }
        else if(selectDesign == 1){
            b = new DesignB();
            drawBoard(b);
        }
        else if(selectDesign == 0){
            b = new DesignC();
            drawBoard(b);
        }
        else{
            System.exit(0);
        }

        Object[] stonesNum = {"4 stones", "3 stones"};
        int selectStone = JOptionPane.showOptionDialog(panel1, "How many stones do you want in each pit", "Board Formatter",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, stonesNum, stonesNum[1]);
        if(selectStone == 1){
            stones = 3;
            boardModel.setStones(stones);
            frame.repaint();
        }
        else if(selectStone == 0){
            stones = 4;
            boardModel.setStones(stones);
            frame.repaint();
        }
        else{
            System.exit(0);
        }
        frame.add(panel1);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void drawBoard(BoardFormatter b)
    {
        System.out.println("Board Drawn");
        Color boardColor = b.formatBoardColor();
        panel1.setLayout(new BorderLayout());


        //pits panel
        //JPanel pitsPanel = new JPanel();
        pitsPanel = new JPanel();

        pitsPanel.setLayout(new GridLayout(4, 6, 5, 5));
        //pits label B6-B1
        for(int i = 6; i > 0; i--){
            JLabel t = new JLabel("B"+i , SwingConstants.CENTER);
            pitsPanel.add(t);
        }

        //B6-B1 => Pit12-Pit7
        for (pitCounterB = 12; pitCounterB > 6; pitCounterB--)
        {
            Pits pitCount = new Pits(pitCounterB);
            JLabel label = new JLabel(pitCount);
            label.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e) {
                    //if a move is already made
                    System.out.println("Undo chances / Stack is empty: " + boardModel.stackIsEmpty());
                    if (boardModel.stackIsEmpty()) {
                        if (!boardModel.playerBTurn)
                        {
                            JOptionPane.showMessageDialog(label, "This pit doesn't belongs to you");
                        }
                        // If clicked on an empty pit, pop up message
                        else if (boardModel.isEmpty(boardModel.playerBTurn, pitCount.pitsIndex - 7 + 1))
                        {
                            JOptionPane.showMessageDialog(null, "Can not pick an empty pit. Try Again");
                        }

                        else
                        {
                            System.out.println("Player B Move Read");
                            boardModel.placeStones(boardModel.playerBTurn, pitCount.pitsIndex - 7 + 1);
                        }
                    }

                    else
                    {
                        JOptionPane.showMessageDialog(null, "A move has already been made. Either undo it or mark it as done");
                    }
                }
            });
            pitsPanel.add(label);
        }

        // change 2
        // A1-A6 => Pit0-Pit5
        for (pitCounterA = 0; pitCounterA < 6; pitCounterA++)
        {
            Pits pitCount = new Pits(pitCounterA);
            JLabel label1 = new JLabel(pitCount);
            // JButton button1 = new JButton();
            // System.out.println("Outside of clicked: " + pitCounterA);
            label1.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    System.out.println("Undo chances: " + boardModel.stackIsEmpty());
                    if(boardModel.stackIsEmpty())
                    {

                        if (boardModel.playerBTurn)
                        {
                            JOptionPane.showMessageDialog(null, "This pit doesn't belong to you");
                        }
                        // If clicked on an empty pit, pop up message
                        else if(boardModel.isEmpty(boardModel.playerBTurn, pitCount.pitsIndex + 1))
                        {
                            JOptionPane.showMessageDialog(null, "Can not pick an empty pit. Try Again");
                        }
                        else
                        {
                            System.out.println("Player A Move Read");

                            boardModel.placeStones(boardModel.playerBTurn, pitCount.pitsIndex + 1);
                            // System.out.println("Inside of clicked: " + pitCount.pitsIndex);
                        }
                    }

                    else
                    {
                        JOptionPane.showMessageDialog(null, "A move has already been made. Either undo it or mark it as done");
                    }
                }
            });
            //label1.add(button1);
            //pitsPanel.add(button1);
            pitsPanel.add(label1);

        }
        //pits Label A1-A6
        for(int i = 1; i < 7; i++) {
            JLabel t = new JLabel("A" + i, SwingConstants.CENTER);
            pitsPanel.add(t);
        }

        pitsPanel.setBackground(boardColor);

        //mancala B panel
        JPanel mancalaB = new JPanel();
        mancalaB.setBackground(boardColor);
        mancalaB.setLayout(new GridBagLayout());
        JTextArea leftText = new JTextArea();

        manBStones = new JLabel(""+ boardModel.boardB[6], SwingConstants.CENTER);

        leftText.setText("M\nA\nN\nC\nA\nL\nA\n\nB");
        leftText.setEditable(false);
        JLabel manB = new JLabel(new Pits(13));

        leftText.setBackground(new Color(0,0,0,0));
        manBStones.setBackground(new Color(0, 0,0, 0));
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        mancalaB.add(leftText, c);
        c.gridx = 1;
        mancalaB.add(manB, c);
        c.gridy = 1;
        mancalaB.add(manBStones, c);


        //mancala A panel
        JPanel mancalaA = new JPanel();
        mancalaA.setBackground(boardColor);
        mancalaA.setLayout(new GridBagLayout());
        JTextArea rightText = new JTextArea();
        rightText.setText("M\nA\nN\nC\nA\nL\nA\n\nA");
        rightText.setEditable(false);

        manAStones = new JLabel(""+ boardModel.boardA[6], SwingConstants.CENTER);
        JLabel manA = new JLabel(new Pits(6));
        rightText.setBackground(new Color(0,0,0,0));
        manBStones.setBackground(new Color(0, 0,0, 0));
        GridBagConstraints c1 = new GridBagConstraints();
        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.gridx = 0;
        c1.gridy = 0;
        mancalaA.add(manA, c1);
        c1.gridx = 1;
        mancalaA.add(rightText, c1);
        c1.gridx = 0;
        c1.gridy = 1;
        mancalaA.add(manAStones, c1);



        //undo panel and player turn panel
        JPanel southPanel = new JPanel();
        undo = new JButton("undo (" + this.boardModel.getPlayerUndo() + ")");
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Undo called");
                boolean actionUndone = boardModel.undo();
                boardModel.emptyStack();
                if(!actionUndone)
                    JOptionPane.showMessageDialog(null, "There are no more undo chances. Mark move as done.");
            }
        });

        //timer
        JButton moveDone = new JButton("Move done");
        moveDone.addActionListener(
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        if (boardModel.stack.isEmpty())
                            JOptionPane.showMessageDialog(panel1, "Make a move first");
                        else {
                            if(boardModel.freeTurn && (!boardModel.isGameFinished().equals("A") || !boardModel.isGameFinished().equals("B"))){
                                //skip opponent's turn once
                                boardModel.setTurn();
                                boardModel.setTurn();
                                boardModel.emptyStack();
                                JOptionPane.showMessageDialog(panel1, "You got a free turn!");
                                boardModel.freeTurn = false;
                            }
                            else {
                                System.out.println("Move is done");
                                boardModel.setTurn();
                                boardModel.emptyStack();
                            }
                        }

                        String finish = boardModel.isGameFinished();
                        if (finish.equals("A") || finish.equals("B") )
                        {
                            if (boardModel.boardA[6] > boardModel.boardB[6]) {
                            JOptionPane.showMessageDialog(null, "Player A Wins!");
                            System.exit(0);
                        }
                            else if (boardModel.boardB[6] > boardModel.boardA[6]) {
                                JOptionPane.showMessageDialog(null, "Player B Wins!");
                                System.exit(0);
                            }
                            else {
                                JOptionPane.showMessageDialog(null, "Tie!");
                                System.exit(0);
                            }
                        }
                    }
                });


        southPanel.add(undo);//end undo panel
        southPanel.add(moveDone);


        //JPanel playerTurn = new JPanel();
        playerText = new JTextArea();
        playerText.setEditable(false);

        if(boardModel.playerBTurn)
            playerText.setText("Player B Turn");

        else
            playerText.setText("Player A Turn");

        southPanel.add(playerText);


        panel1.add(mancalaA, BorderLayout.EAST);
        panel1.add(pitsPanel, BorderLayout.CENTER);
        panel1.add(mancalaB, BorderLayout.WEST);
        panel1.add(southPanel,BorderLayout.SOUTH);
        //panel1.add(playerTurn, BorderLayout.SOUTH);
        frame.add(panel1);
        frame.pack();
        frame.setVisible(true);

    }

    /**
     *  The pits and mancala that implemented as icons
     */
    public class Pits implements Icon {
        private int pitsIndex;
        private Shape mancala, pits;
        public Pits(int pitsIndex) {
            this.pitsIndex = pitsIndex;
        }


        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D)g;

            if(pitsIndex == 6 || pitsIndex == 13){
                mancala = b.formatMancalaShape();
                g2.setColor(b.formatPitColor());
                g2.fill(mancala);

                //Mancala A => boardA[6]
                if (pitsIndex == 6) {
                    for (int i = 0; i < boardModel.boardA[pitsIndex]; i++) {
                        //System.out.println(boardModel.boardA[pitsIndex]);
                        Shape stones = b.formatMancalaStoneShape(boardModel.boardA[pitsIndex], i);
                        g2.setColor(b.formatStoneColor());
                        g2.draw(stones);
                        g2.fill(stones);
                    }
                }
                //Mancala B => boardB[ 13 - 7 = 6]
                else {
                    for (int i = 0; i < boardModel.boardB[pitsIndex - 7]; i++) {
                        Shape stones = b.formatMancalaStoneShape(boardModel.boardB[pitsIndex - 7], i);
                        g2.setColor(b.formatStoneColor());
                        g2.draw(stones);
                        g2.fill(stones);
                    }
                }
            }
            //Pits
            else{
                pits = b.formatPitShape();
                g2.setColor(b.formatPitColor());
                g2.fill(pits);
                //A1-A6 => boardA[0]-boardA[5]
                if (pitsIndex < 6) {
                    for (int i = 0; i < boardModel.boardA[pitsIndex]; i++) {
                        Shape stones = b.formatPitStoneShape(boardModel.boardA[pitsIndex], i);
                        g2.setColor(b.formatStoneColor());
                        g2.draw(stones);
                        g2.fill(stones);
                    }
                }
                //B6-B1 => boardB[12 - 7 = 5]-boardB[7 - 7 = 0]
                else {
                    for (int i = 0; i < boardModel.boardB[pitsIndex - 7]; i++) {
                        Shape stones = b.formatPitStoneShape(boardModel.boardB[pitsIndex - 7], i);
                        g2.setColor(b.formatStoneColor());
                        g2.draw(stones);
                        g2.fill(stones);
                    }
                }
            }
        }

        @Override
        public int getIconWidth() {
            return 100;
        }

        @Override
        public int getIconHeight() {
            if (pitsIndex == 6 || pitsIndex == 13){
                return 200;
            }
            else
                return 100;
        }

    }


    public void getInfo()
    {
        System.out.println("Design " + design +" chosen");
        System.out.println(stones + " Stones Chosen");
        System.out.println("-------------------------------");
        boardModel.setStones(stones);
    }

    @Override
    public void stateChanged(ChangeEvent e)
    {
        manAStones.setText("" + boardModel.boardA[6]);
        manBStones.setText("" + boardModel.boardB[6]);
        undo.setText("undo (" + boardModel.getPlayerUndo() + ")");
        frame.repaint();
//        panel1.repaint();

        if(boardModel.playerBTurn)
            playerText.setText("Player B Turn");

        else
            playerText.setText("Player A Turn");

    }
}