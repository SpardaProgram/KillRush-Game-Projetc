package com.spardagames.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import com.spardagames.main.Game;
import com.spardagames.main.Sound;
import com.spardagames.world.Camera;
import com.spardagames.world.World;


public class Jogador extends GameObjects {
	public boolean direita,esquerda,cima,baixo;
	public int direita_dir=0,esquerda_dir=1;
	public int dir=direita_dir;
	public double speed = 2.0;
	
	private int frames=0, maxFrames = 5, index = 0, maxIndex = 3;
	private boolean moved = false;
	private BufferedImage[] paraDireita;
	private BufferedImage[] paraEsquerda;
	private BufferedImage damage;
	
	public double vidaMax = 100;
	public double vida = 100;
	
	public int ammo=0;
	
	public boolean damaged=false;
	
	private int damageFrames = 0;
	
	private boolean gun = false;
	
	public boolean bulletShoots = false;
	
	public Jogador(int x, int y, int largura, int altura, BufferedImage sprite) {
		super(x,y,largura,altura,sprite);
		
		paraDireita = new BufferedImage[4];
		paraEsquerda = new BufferedImage[4];
		damage = Game.spritesheet.getSprite(0,16,16,16);
		
		for(int i=0;i<4;i++) {
			paraDireita[i] = Game.spritesheet.getSprite(32 + (i*16), 0, 16, 16);
		}
		for(int i=0;i<4;i++) {
			paraEsquerda[i] = Game.spritesheet.getSprite(32 + (i*16), 16, 16, 16);
		}
		
	
		 
	}
	
	public void tick() {
		moved = false;
		if(direita && World.isFree((int)(speed+x),this.getY())){
			moved=true;
			dir = direita_dir;
			x+=speed;
		}
		else if(esquerda && World.isFree((int)(x-speed),this.getY())) {
			moved=true;
			dir = esquerda_dir;
			x-=speed;
		}
		if(cima && World.isFree(this.getX(),this.getY()-(int)(speed))) {
			moved=true;
			y-=speed;
		}
		else if(baixo && World.isFree(this.getX(),this.getY()+(int)(speed))) {
			moved=true;
			y+=speed;
		}
		if(moved) {
			frames++;
			if(frames == maxFrames) {
				frames=0;
				index++;
				if(index > maxIndex) {
					index = 0;
				}
			}
			
			
		}
		this.checkMedkit();
		this.checkAmmo();
		this.checkGun();
		
		if(damaged) {
			this.damageFrames++;
			if(this.damageFrames == 8) {
				this.damageFrames = 0;
				damaged = false;
			}
		}
		if(bulletShoots) {
			bulletShoots = false;
			if(gun && ammo>0) {
				Sound.shoot.play();
				int dx = 0;
				int px = 0;
				int py = 6;
			if(dir == direita_dir) {
				px = 18;
				dx = 1;
			}else {
				px = -8;
				dx = -1;
			}
			
			Shoot ammoshoot = new Shoot(this.getX()+px,this.getY()+py,3,3,null,dx,0);
			Game.shoots.add(ammoshoot);
			ammo--;
			}
		}
		if(vida<=0) {
			vida=0;
			Game.gameState = "GAME_OVER";
			
		}
		Camera.x = Camera.clamp(this.getX()-(Game.largura/2),0,World.largura*16 - Game.largura) ;
		Camera.y = Camera.clamp(this.getY()-(Game.altura/2),0,World.altura*16 - Game.altura);
	}
	public void checkGun() {
		for(int i=0;i<Game.entities.size();i++) {
			GameObjects atual = Game.entities.get(i);
			if(atual instanceof Arma) {
				if(GameObjects.Colliding(this, atual)) {
					gun=true;
					
					Game.entities.remove(atual);
				}
			}
		}
	}
	
	public void checkAmmo() {
		for(int i=0;i<Game.entities.size();i++) {
			GameObjects atual = Game.entities.get(i);
			if(atual instanceof Ammo) {
				if(GameObjects.Colliding(this, atual)) {
					ammo+=15;
					
					Game.entities.remove(atual);
				}
			}
		}
	}
	
	public void checkMedkit() {
		for(int i=0;i<Game.entities.size();i++) {
			GameObjects atual = Game.entities.get(i);
			if(atual instanceof Medkit) {
				if(GameObjects.Colliding(this, atual)) {
					vida+=10;
					if(vida>100) {
						vida=100;
					}
					Game.entities.remove(atual);
				}
			}
		}
	}
	
	
	public void render(Graphics g) {
		if(!damaged) {
			if(dir == direita_dir) {
			g.drawImage(paraDireita[index],this.getX() - Camera.x,this.getY() - Camera.y, null);
			if(gun) {
				g.drawImage(GameObjects.GUN_RIGHT, this.getX()+5 - Camera.x,this.getY()-Camera.y,null);
			}
			}else if(dir == esquerda_dir) {
			g.drawImage(paraEsquerda[index],this.getX() - Camera.x,this.getY() - Camera.y, null);
			if(gun) {
				g.drawImage(GameObjects.GUN_LEFT, this.getX()-5 - Camera.x,this.getY()-Camera.y,null);
			}
			}
			
		}else {
			g.drawImage(damage, this.getX()-Camera.x, this.getY()-Camera.y,null);
		}
	}
	
	
	

	
	
	

	
	

}
