package game.obj;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;

import javax.swing.ImageIcon;

public class Player {
	public Player() {
		this.image=new ImageIcon(getClass().getResource("/game/image/plane.png")).getImage();
		this.image_speed=new ImageIcon(getClass().getResource("/game/image/plane_speed.png")).getImage();;
		Path2D a=new Path2D.Double();
		a.moveTo(0, PLAYER_SIZE/2);
		a.lineTo(15, 10);
		a.lineTo(PLAYER_SIZE-5, 13);
		a.lineTo(PLAYER_SIZE+10, PLAYER_SIZE/2);
		a.lineTo(PLAYER_SIZE-5, PLAYER_SIZE-13);
		a.lineTo(15, PLAYER_SIZE-10);
		playerShape=new Area(a);
	}
	
	
	public static final double PLAYER_SIZE=64;
	private double x;
	private double y;
	private final float MAX_SPEED=1f;
	private float speed=0f;
	private float angle=0f;
	private final Image image;
	private final Image image_speed;
	private boolean speedUp;
	private final Area playerShape;
	
	public void changeLocation(double x, double y) {
		this.x=x;
		this.y=y;
	}
	public void update() {
		x+=Math.cos(Math.toRadians(angle))*speed;
		y+=Math.sin(Math.toRadians(angle))*speed;
	}
	
	public void changeAngle(float angle) {
		if(angle<0) {
			angle=359;
		}else if(angle>359) {
			angle=0;
		}
		this.angle=angle;
		
	}
	public void draw(Graphics2D g2) {
		AffineTransform oldTransform=g2.getTransform();
		g2.translate(x, y);
		AffineTransform tran=new AffineTransform();
		tran.rotate(Math.toRadians(angle+45),PLAYER_SIZE/2,PLAYER_SIZE/2);
		g2.drawImage(speedUp?image_speed: image, tran, null);
		Shape shape=getShape();
		
		g2.setTransform(oldTransform);
		
		g2.setColor(new Color(0,255,0));
		g2.draw(shape);
		g2.draw(shape.getBounds2D());
		
	}
	
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public float getAngle() {
		return angle;
	}
	public void speedUp() {
		speedUp=true;
		if(speed>MAX_SPEED) {
			speed=MAX_SPEED;
		}else {
			speed+=0.01f;
		}
		
	}
	public void speedDown() {
		speedUp=false;
		if(speed<=0) {
			speed=0;
		}else {
			speed-=0.003f;
		}
		
	}
	public Area getShape() {
		AffineTransform afx=new AffineTransform();
		afx.translate(x, y);
		
		afx.rotate(Math.toRadians(angle),PLAYER_SIZE/2,PLAYER_SIZE/2);
		
		return new Area(afx.createTransformedShape(playerShape));
		

		
	}
	
	
}
