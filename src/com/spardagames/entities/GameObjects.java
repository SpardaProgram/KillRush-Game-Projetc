package com.spardagames.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.spardagames.main.Game;
import com.spardagames.world.Camera;

public class GameObjects {
	public static BufferedImage MedKit_EN = Game.spritesheet.getSprite(6*16, 0, 16, 16);
	public static BufferedImage Arma_EN = Game.spritesheet.getSprite(7*16, 0, 16, 16);
	public static BufferedImage Ammo_EN = Game.spritesheet.getSprite(6*16, 16, 16, 16);
	public static BufferedImage GUN_RIGHT = Game.spritesheet.getSprite(128, 0, 16, 16);
	public static BufferedImage GUN_LEFT = Game.spritesheet.getSprite(144, 0, 16, 16);
	public static BufferedImage Monstro_EN = Game.spritesheet.getSprite(7*16, 16, 16, 16);
	public static BufferedImage Monstro_FEEDBACK = Game.spritesheet.getSprite(16, 16, 16, 16);
	protected double x;
	protected double y;
	protected int largura;
	protected int altura;
	private BufferedImage sprite;
	private int maskx,masky,mlargura,maltura;
	
	public GameObjects(int x, int y, int largura, int altura, BufferedImage sprite) {
		this.largura=largura;
		this.altura=altura;
		this.sprite=sprite;
		this.x = x;
		this.y = y;
		this.maskx=0;
		this.masky=0;
		this.mlargura=largura;
		this.maltura=altura;
	}
	
	public void setMask(int maskx, int masky, int mlargura, int maltura) {
		this.maskx=maskx;
		this.masky=masky;
		this.mlargura=mlargura;
		this.maltura=maltura;
		
	}
	
	public void setX(int newX) {
		this.x = newX;
	}
	public void setY(int newY) {
		this.y = newY;
	}
	
	public int getX() {
		return (int)this.x;
	
	}

	public int getY() {
		return (int)this.y;
	
	}

	public int getLargura() {
		
		return this.largura;
	}

	public int getAltura() {
		
		return this.altura;
	}
	
	public static boolean Colliding(GameObjects e1, GameObjects e2) {
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx,e1.getY() + e1.masky,e1.mlargura,e1.maltura);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx,e2.getY() + e2.masky,e2.mlargura,e2.maltura);
		
		return e1Mask.intersects(e2Mask);
	}
	
	public void render(Graphics g) {
			
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
		//g.setColor(Color.red);
		//g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y,mlargura,maltura);
	}


	public void tick() {

	
	}
	
	
	

	
}
