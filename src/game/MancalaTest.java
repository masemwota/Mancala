package game;

import java.util.Scanner;

import javax.swing.JFrame;

public class MancalaTest 
{
	public static void main(String [] args)
	{
		//to start the game
	
		BoardModel barModel = new BoardModel(); 
		//barModel.addStones(4);
		BoardComponent myFrame = new BoardComponent(barModel);
	}
}
