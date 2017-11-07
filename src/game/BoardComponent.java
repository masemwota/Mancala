package game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.*;
import javax.swing.event.ChangeListener;

/**
 * View Component of the game
 * @author Stan Yanakiev
 * @author Marietta Asemwota 
 * @author Yihua Li
 */
public class BoardComponent extends JComponent implements ActionListener
{
	BoardModel boardModel;
	
	private boolean designChosen, stonesChosen;
	private int design, stones;
	
	private JFrame frame;
	
	//we are going to use panels like screens and switch back and forth
	private JPanel panel;
	private JPanel panel1;
	
	
	public BoardComponent(BoardModel m)
	{
		frame = new JFrame();
		boardModel = m;
		
		frame.setSize(200, 200);
		frame.setLayout(new BorderLayout());
		
		panel = new JPanel(); 
		panel1 = new JPanel();
		
		designChosen = false; 
		stonesChosen = false;
		
		design = 0; 
		stones = 0;
		
		getDecisions();
	}
	
	
	
	public void getDecisions()
	{
		JTextField textField = new JTextField(40);
		textField.setEditable(false);
		textField.setText("Please Choose A Design");
		
		JButton button1 = new JButton("Design1");
		JButton button2 = new JButton("Design2");
		
		button1.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						textField.setText("How many stones do you want in each pit (3 or 4)");
						
						if(designChosen == false)
						{
							//System.out.println("Design 1 chosen");
							button1.setText("3 Stones");
							button2.setText("4 Stones");
							designChosen = true;
							design = 1;
						}
						else //design already chosen -- choose stones
						{
							//System.out.println("3 Stones chosen");
							stonesChosen = true;
							stones = 3; 
							
							drawBoard();
							
							frame.remove(panel);
							frame.setContentPane(panel1);
							frame.repaint();
						}
					}

				
				});
		
		
		button2.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						textField.setText("How many stones do you want in each pit (3 or 4)");
						//if(!button2.getText().equals("4 Stones"))
						if(designChosen == false)
						{
							//System.out.println("Design 2 chosen");
							button2.setText("4 Stones");
							button1.setText("3 Stones");
							
							designChosen = true;
							design = 2;
						}
						else
						{
							//System.out.println("4 Stones chosen");
							stonesChosen = true;
							stones = 4;
							
							drawBoard();
							
							frame.remove(panel);
							frame.setContentPane(panel1);
							frame.repaint();
						}
					}
				});
		panel.add(button1);
		panel.add(button2);
		
		frame.add(textField, BorderLayout.NORTH);
		frame.add(panel, BorderLayout.CENTER);
		
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void drawBoard()
	{
		System.out.println("draw board");
		
		JTextField mytextField = new JTextField(20);
		mytextField.setEditable(false);
		mytextField.setText("Hello World");
		
		panel1.add(mytextField);
		frame.add(panel1);
		frame.pack(); 
		frame.setVisible(true);
		repaint();
		
		getInfo();
		//boolean turn, int pit, int stones
		//we need to keep in mind that the pit you choose for player b starts from right
		
		System.out.println("Player A moves from pit 4");
		boardModel.placeStones(false, 4);
		
		System.out.println("Player B moves from pit 3 (from the right)");
		boardModel.placeStones(true, 3);
		
		System.out.println("Player A moves from pit 2");
		boardModel.placeStones(false, 2);
		
		System.out.println("Player B moves from pit 5"); 
		boardModel.placeStones(true, 5);
		
		System.out.println("Player B moves from pit 7 (mancala B)");
		boardModel.placeStones(true, 7);
	}
	
	public void getInfo()
	{
		System.out.println("Design " + design);
		System.out.println(stones + " stones");
		
		boardModel.setStones(stones);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//
	}
}