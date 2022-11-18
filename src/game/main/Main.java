package game.main;

import java.awt.BorderLayout;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import game.component.PanelGame;

public class Main extends JFrame{

	
	public Main() {
		init();
	}
	
	
	
	
	
	
	
	
	private void init() {
		setTitle("Rittenhouse");
		setSize(1366, 768);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		PanelGame panelGame=new PanelGame();
		add(panelGame);
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowOpened(WindowEvent e) {
				panelGame.start();
			}
		});
		
	}








	public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		Main main=new Main();
		main.setVisible(true);
		
		
		
		
		
		File file =new File("src/action.wav");
		AudioInputStream audioStream=AudioSystem.getAudioInputStream(file);
		Clip clip=AudioSystem.getClip();
		clip.open(audioStream);
		
		clip.start();
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		
		
	
		
		
		
		
		
		
		
	}
}
