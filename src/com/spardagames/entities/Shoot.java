package com.spardagames.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.spardagames.main.Game;
import com.spardagames.world.Camera;


public class Shoot extends GameObjects {
	
	private int dx;
	private int dy;
	private double spd = 4;
	private int life = 100, curLife=0;
	
	public Shoot(int x, int y, int largura, int altura, BufferedImage sprite, int dx, int dy) {
		super(x, y, largura, altura, sprite);
		this.dx=dx;
		this.dy=dy;

	}

	public void tick() {
		x+=dx*spd;
		y+=dy*spd;
		curLife++;
		
			if(curLife == life ) {
				Game.shoots.remove(this);
				return;
			}
		}
		
		

	
	public void render(Graphics g) {
		g.setColor(Color.yellow);
		g.fillOval(this.getX()-Camera.x,this.getY()-Camera.y, 2, 2);
	}


}
