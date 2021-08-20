package com.spardagames.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.spardagames.main.Game;
import com.spardagames.main.Sound;
import com.spardagames.world.Camera;
import com.spardagames.world.World;

public class Monstro extends GameObjects{
	
	private double speed = 1;
	
	private int frames=0, maxFrames = 5, index = 0, maxIndex = 3;
	private BufferedImage[] paraDireita;
	private BufferedImage[] paraEsquerda;
	private int direita_dir=0,esquerda_dir=1;
	private int dir=direita_dir;
	private int vida = 10;
	private boolean damaged = false;
	private int damageFrames = 2,damageCurrent = 0;
	
	public Monstro(int x, int y, int largura, int altura, BufferedImage sprite) {
		super(x, y, largura, altura, sprite);
		
		paraDireita = new BufferedImage[4];
		paraEsquerda = new BufferedImage[4];
		
		for(int i=0;i<4;i++) {
			paraDireita[i] = Game.spritesheet.getSprite(7*16, 16 + (i*16), 16, 16);
		}
		for(int i=0;i<4;i++) {
			paraEsquerda[i] = Game.spritesheet.getSprite(8*16, 16 + (i*16), 16, 16);
		}
		
	}
	
	public void tick() {
			if(collidingWithPlayer() == false) {
				if((int)x < Game.player.getX() && World.isFree((int)(x+speed), this.getY()) && !isColliding((int)(x+speed), this.getY())) {				 
					dir = direita_dir;
					x+=speed;
				}else if((int)x > Game.player.getX() && World.isFree((int)(x-speed), this.getY()) && !isColliding((int)(x-speed), this.getY())) {				
					dir = esquerda_dir;
					x-=speed;
				}if((int)y < Game.player.getY() && World.isFree(this.getX(), (int)(y+speed)) && !isColliding(this.getX(), (int)(y+speed))) {
					y+=speed;
				}else if((int)y > Game.player.getY() && World.isFree(this.getX(), (int)(y-speed)) && !isColliding(this.getX(), (int)(y-speed))) {				
					y-=speed;
				}
				
				frames++;
				if(frames == maxFrames) {
					frames=0;
					index++;
					if(index > maxIndex) {
						index = 0;
						
					}
					collidingBullet();
					if(vida<=0) {
						destroySelf();
						return;
					}
					if(damaged) {
						this.damageCurrent++;
						if(this.damageCurrent==this.damageFrames) {
							this.damageCurrent = 0;
							this.damaged = false;
						}
					}
				}
				
				
			}else {
				if(Game.rand.nextInt(100)<10) {
					Sound.hit.play();
					Game.player.vida-=Game.rand.nextInt(3);
					Game.player.damaged = true;
					System.out.println("Vida: "+Game.player.vida);
				}
				
			}
			
	
			
			
		}
	public void destroySelf() {
		Game.monstros.remove(this);
		Game.entities.remove(this);
	}
	
	public void collidingBullet() {
		for(int i = 0;i<Game.shoots.size();i++) {
			GameObjects e = Game.shoots.get(i);
			if(e instanceof Shoot) {
				if(GameObjects.Colliding(this, e)) {
					damaged = true;
					vida--;
					Game.shoots.remove(i);
					return;
				}
			}
		}
	}
	public boolean collidingWithPlayer() {
		Rectangle monstroCurrent = new Rectangle(this.getX(),this.getY(),World.tile_size,World.tile_size);
		Rectangle player = new Rectangle(Game.player.getX(),Game.player.getY(),16,16);
		return monstroCurrent.intersects(player);
	}
	
	public boolean isColliding(int xnext, int ynext) {
		Rectangle monstroCurrent = new Rectangle(xnext,ynext,World.tile_size,World.tile_size);
		for(int i=0;i<Game.monstros.size(); i++) {
			Monstro e = Game.monstros.get(i);
			if(e == this) 
				continue;
			Rectangle targetMonstro = new Rectangle(e.getX(),e.getY(),World.tile_size,World.tile_size);
			if(monstroCurrent.intersects(targetMonstro)){
				return true;
			}

			
		}
		return false;
	}
	
	public void render(Graphics g) {
		if(!damaged) {
		if(dir == direita_dir) {
			g.drawImage(paraDireita[index],this.getX() - Camera.x,this.getY() - Camera.y, null);
		}else if(dir == esquerda_dir) {
			g.drawImage(paraEsquerda[index],this.getX() - Camera.x,this.getY() - Camera.y, null);
		}
		}else {
			g.drawImage(GameObjects.Monstro_FEEDBACK,this.getX() - Camera.x,this.getY() - Camera.y, null);
		}
	}
}


