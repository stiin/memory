package Lab7;

/* 
 * Kristin
 * DD2385 Lab 2 MVC
 * 20160423
 */

import java.awt.Color;
import javax.swing.JButton;

public class Square extends JButton {
	
	private int i;
	private int j;
	
	public Square(int i, int j) {
		super();
		this.i = i;
		this.j = j;
		this.setBackground(Color.white);
	}
	
	public Integer getI() {
		return i;
	}
	
	public Integer getJ() {
		return j;
	}	
	
}
