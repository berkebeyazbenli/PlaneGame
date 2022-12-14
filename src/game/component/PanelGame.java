package game.component;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JComponent;

import game.obj.Bullet;
import game.obj.Player;
import game.obj.Rocket;



public class PanelGame extends JComponent {
	//berke yarramı yesin
	private Graphics2D g2;
	private BufferedImage image;
	private int width;
	private int height;
	private Thread thread;
	private boolean start=true;
	private Key key;
	private int shotTime=0;
	private int point =0;
	
	//game FPS
	
	private final int FPS=60;
	private final int TARGET_TIME=1000000000/FPS;
	//Game Object
	private Player player;
	private List<Bullet> bullets;
	private List<Rocket> rockets;
	private int k =1;
	private int r=0;
	private boolean carpısma=true;
	
	public void start() {
		width=getWidth();
		height=getHeight();
		image=new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
		g2=image.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		
		
		
		
		thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(start) {
					checkPlane(player);
					long startTime=System.nanoTime();
				
			        drawBackground();
				    drawGame();
					render();
					long time=System.nanoTime()-startTime;
					if(time<TARGET_TIME) {
						long sleep=(TARGET_TIME-time)/1000000;
						sleep(sleep);
						
					}
					
				}
				
			}
			
		});

		initObjectGame();
		initKeyboard();
		initBullets();
		thread.start();
		
		}
	
	private void addRocket() {
		Random ran=new Random();
		int locationY=ran.nextInt(height-50)+25;
		
		Rocket rocket=new Rocket();
		rocket.changeLocation(0, locationY);
		rocket.changeAngle(0);
		rockets.add(rocket);
		int locationY2=ran.nextInt(height-50)+25;
		Rocket rocket2=new Rocket();
		rocket2.changeLocation(width, locationY2);
		rocket2.changeAngle(180);
		rockets.add(rocket2);
	}
	
	
	
	
	private void initObjectGame() {
		player=new Player();
		player.changeLocation(150, 150);
		rockets =new ArrayList<>();
		new Thread(new Runnable() {

			@Override
			public void run() {
				while(start) {
				
					addRocket();
					sleep(2000/k);
					
				
				}
				
			}
			
		}).start();
		
	}
	private void initKeyboard() {
		key=new Key();
		requestFocus();
		addKeyListener((KeyListener) new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if(e.getKeyCode()==KeyEvent.VK_A) {
					key.setKey_left(true);
					
				}else if(e.getKeyCode()==KeyEvent.VK_D) {
					key.setKey_right(true);
				
				}else if(e.getKeyCode()==KeyEvent.VK_W) {
					key.setKey_w(true);
				
				}else if(e.getKeyCode()==KeyEvent.VK_J) {
					key.setKey_j(true);
				
				}else if(e.getKeyCode()==KeyEvent.VK_K) {
					key.setKey_k(true);
				}else if(e.getKeyCode()==KeyEvent.VK_SPACE) {
					switch (r){
						case 0:
							break;
						case 1:
							start=true;
							start();
							repaint();
							r=0;
							point=0;
							break;
					}
				
					
				}
					
			}
			@Override
			public void keyReleased(KeyEvent e) {
				
				if(e.getKeyCode()==KeyEvent.VK_A) {
					key.setKey_left(false);
				
				}else if(e.getKeyCode()==KeyEvent.VK_D) {
					key.setKey_right(false);
				
				}else if(e.getKeyCode()==KeyEvent.VK_W) {
					key.setKey_w(false);
				
				}else if(e.getKeyCode()==KeyEvent.VK_J) {
					key.setKey_j(false);
				
				}else if(e.getKeyCode()==KeyEvent.VK_K) {
					key.setKey_k(false);
				}else if(e.getKeyCode()==KeyEvent.VK_SPACE) {
					key.setKey_space(false);
				}
			}
		});
		new Thread(new Runnable() {

			@Override
			public void run() {
				float s=0.5f;
				while(start) {
					float angle=player.getAngle();
					if(key.isKey_left()) {
						angle-=s;
					}
					
					if(key.isKey_right()) {
						angle+=s;
					}
					if(key.isKey_j()||key.isKey_k()) {
						if(shotTime==0) {
							if(key.isKey_j()) {
								bullets.add(0,new Bullet(player.getX(),player.getY(),player.getAngle(),10,3f));
								
							}else {
								bullets.add(0,new Bullet(player.getX(),player.getY(),player.getAngle(),20,3f));
							}
						}
						shotTime++;
						//atış sikliği
						if(shotTime==45) {
						
							shotTime=0;
						}
					}else {
						shotTime=0;
						
					}
					if(key.isKey_w()) {
						player.speedUp();
					}else {
						player.speedDown();
					}
					player.update();
					player.changeAngle(angle);
					for(int i=0; i<rockets.size();i++) {
						Rocket rocket=rockets.get(i);
						if(rocket!=null) {
						rocket.update();
							if(!rocket.check(width, height)) {
								rockets.remove(rocket);
								
							}
						}
					}
					//roket yavaşlığı
					sleep(5);
				}
				
			}
			
		}).start();
	}
	
	private void initBullets() {
		bullets=new ArrayList<>();
		new Thread(new Runnable() {

			@Override
			public void run() {
				while(start) {
					for(int i=0; i<bullets.size();i++) {
						Bullet bullet=bullets.get(i);
						if(bullet!=null) {
							bullet.update();
							try {
								checkBullet(bullet);
							} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if(!bullet.check(width,height)) {
							bullets.remove(bullet);	
							
							
							}
							}else {
								bullets.remove(bullet);
							}
					//mermi yavaşlığı
					}sleep(5);
				}
				
			}
			
		}).start();
	}
	
	private void checkBullet(Bullet bullet) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		for(int i=0;i<rockets.size();i++) {
			Rocket rocket=rockets.get(i);
			if(rocket!=null) {
				Area area=new Area(bullet.getShape());
				area.intersect(rocket.getShape());
				
		
				
				if(!area.isEmpty()) {
					rockets.remove(rocket);
					bullets.remove(bullet);
					point+=1000;
					
					File file =new File("src/fireVoice.wav");
					AudioInputStream audioStream=AudioSystem.getAudioInputStream(file);
					Clip clip=AudioSystem.getClip();
					clip.open(audioStream);
					
					clip.start();
					
					if(point%10000==0) {
						k+=1;
						
						
						
						
						
						
					}
				}
			}
		}
	}
	    private void checkPlane(Player player) {
		for(int i=0;i<rockets.size();i++) {
			Rocket rocket=rockets.get(i);
			if(rocket!=null) {
				Area area1=new Area(player.getShape());
				Area area2=new Area(rocket.getShape());
				area1.intersect(area2);
				if(area1.isEmpty()==false) {
//					GAME STOPS
				
				
					g2.setColor(new Color(0,255,0));
					g2.setFont(new Font("serif",Font.BOLD,60));
					g2.drawString("GAME OVER", 500, 300);
					g2.setFont(new Font("s",Font.BOLD,35));
					g2.drawString("Press Space to Restart", 500, 400);
					r=1;


					g2.dispose();
					start=false;
					
				}
			}
		}
	}
	
	

	
	private void drawBackground() {
		g2.setColor(new Color(0,0,0));
		g2.fillRect(0, 0, width, height);
		
		
	}
	
	private void drawGame() {
	
		player.draw(g2);
		for(int i=0;i<bullets.size();i++) {
			Bullet bullet=bullets.get(i);
			if(bullet!=null) {
				bullet.draw(g2);
			}
		}
		for(int i=0; i<rockets.size();i++) {
			Rocket rocket=rockets.get(i);
			if(rocket!=null) {
				rocket.draw(g2);
				
			}
		}
		g2.setColor(new Color(255,255,0));
		g2.setFont(new Font("serif",Font.BOLD,20));
		g2.drawString("POINTS: "+point, 100, 100);
		
	}
	private void render() {
		Graphics g=getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		
		
	}
	
	
	
	
	private void sleep(long speed) {
		try {
			Thread.sleep(speed);
		} catch (InterruptedException ex) {
			
			System.err.println(ex);
		}
	
		
	}

	
}