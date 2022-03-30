package edu.pacific.comp55.starter;

import java.awt.event.MouseEvent;

import acm.graphics.GObject;

public class HowToPlayPane extends GraphicsPane {
	private MainApplication program;
	private GParagraph para;
	private GButton menu;
	private GButton play;
	
	public HowToPlayPane (MainApplication app) {
		this.program = app;
		para = new GParagraph("How To Play", 200, 200);//I am changing this
		
	}
	
	@Override
	public void showContents() {
		program.add(para);
		//program.add(para);
	}

	@Override
	public void hideContents() {
		//program.remove(img);
		//program.remove(para);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
