package com.spardagames.main;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.spardagames.entities.GameObjects;
import com.spardagames.entities.Monstro;
import com.spardagames.entities.Jogador;
import com.spardagames.entities.Shoot;
import com.spardagames.graficos.SpriteSheet;
import com.spardagames.graficos.UI;
import com.spardagames.world.World;





public class Game extends Canvas implements Runnable,KeyListener{
	
	
	private static final long serialVersionUID = 1L;
	public boolean active=false;
	public static final int altura=240;
	public static final int largura=240;
	public static final int escala=3;
	public Thread thread;
	public BufferedImage layer = new BufferedImage(largura,altura,BufferedImage.TYPE_INT_RGB);
	public static List<GameObjects> entities;
	public static List<Monstro> monstros;
	public static List<Shoot> shoots;
	public static SpriteSheet spritesheet;
	public static Jogador player;
	public static Random rand;
	public UI ui;
	public static String gameState = "MENU";
	private int CUR_LEVEL = 1,MAX_LEVEL = 2;
	private boolean showMessageGameOver = false;
	private int framesGameOver = 0;
	private boolean restartGame = false;
	public Menu menu;
	public static World world;
	
	public Game() {

		addKeyListener(this);
		rand = new Random();
		active=true;
		this.setPreferredSize(new Dimension(largura*escala,altura*escala));
		ui = new UI();
		entities = new ArrayList <GameObjects>();
		monstros = new ArrayList <Monstro>();
		shoots = new ArrayList <Shoot>();
		spritesheet = new SpriteSheet("/GameObjects.png");
		player = new Jogador(0,0,16,16,spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
		world = new World("/level1.png");
		menu = new Menu();
		
		
	}
	
	public static void main(String[] args) {
		Sound.music.loop();
		Game game = new Game();
		JFrame frame = new JFrame("Kill Rush");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(game);
		frame.pack();
		frame.setVisible(true);
	
		
		
		new Thread(game).start();
		
		
		
	}
	
	public void tick() {
		if(gameState == "NORMAL") {
			this.restartGame=false;
			for(int i = 0 ; i < entities.size(); i++){
	
				GameObjects e = entities.get(i);
				e.tick();
			
		}
			for(int i = 0; i<shoots.size();i++) {
			
				shoots.get(i).tick();
		}
		
			if(monstros.size() == 0) {
				CUR_LEVEL++;
			if(CUR_LEVEL>MAX_LEVEL) {
				CUR_LEVEL=1;
			}
			String newWorld = "level"+CUR_LEVEL+".png";
			World.restartGame(newWorld);
		}
			}else if(gameState == "GAME_OVER") {
				this.framesGameOver++;
			if(this.framesGameOver == 30) {
				this.framesGameOver = 0;
				if(this.showMessageGameOver) {
					this.showMessageGameOver = false;
					 
						
					
				}else {
					this.showMessageGameOver = true;
				}
			if(restartGame) {
				this.restartGame = false;
				gameState = "NORMAL";
				CUR_LEVEL=1;
				String newWorld = "level"+CUR_LEVEL+".png";
				World.restartGame(newWorld);
				
				}
				}
			}else if(gameState == "MENU") {
				menu.tick();
		}

	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = layer.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0,0,largura,altura);
		world.render(g);
		for(int i = 0 ; i < entities.size(); i++){
			
			GameObjects e = entities.get(i);
			e.render(g);
			
		}
		for(int i = 0; i<shoots.size();i++) {
			
			shoots.get(i).render(g);
		}
		ui.render(g);
		g = bs.getDrawGraphics();
		g.drawImage(layer, 0, 0, largura*escala, altura*escala,null);
		g.setFont(new Font("arial",Font.BOLD,20));
		g.setColor(Color.yellow);
		g.drawString("Munição: "+player.ammo, 10, 100);
		if(gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,200));
			g2.fillRect(0, 0, largura*escala, altura*escala);
			g.setFont(new Font("arial",Font.BOLD,50));
			g.setColor(Color.red);
			g.drawString("Game Over", (largura*escala)/2 - 120, (altura*escala)/2);
			g.setFont(new Font("arial",Font.BOLD,15));
			if(showMessageGameOver) {
			g.drawString(">PRESSIONE ENTER PARA REINICIAR<", (largura*escala)/2 - 130, (altura*escala)/2 + 30);
			}
		}else if(gameState == "MENU") {
			menu.render(g);
		}
		bs.show();
	}
	
	public void run(){
		long lastTime = System.nanoTime();
		double fps = 60.0;
		double ns = 1000000000/fps;
		double delta=0;
		requestFocus();
		while(active) {
			long now = System.nanoTime();
			delta+= (now - lastTime) / ns;
			lastTime=now;
			if(delta>=1) {
				tick();
				render();
				delta--;
			}
			
		}
		
	}

	public void keyTyped(KeyEvent e) {

		
	}

	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_D) {
			player.direita = true;
			
		}else if(e.getKeyCode() == KeyEvent.VK_A) {
			player.esquerda = true;
			
		}
		if(e.getKeyCode() == KeyEvent.VK_W) {
			player.cima = true;
			if(gameState == "MENU") {
				menu.cima = true;
			}

			
		}else if(e.getKeyCode() == KeyEvent.VK_S) {
			player.baixo = true;
			if(gameState == "MENU") {
				menu.baixo = true;
			}

		}
		if(e.getKeyCode() == KeyEvent.VK_K) {
			player.bulletShoots = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.restartGame = true;
			if(gameState == "MENU"){
				menu.enter = true;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			gameState = "MENU";
			menu.pause = true;
		}
		
		
	}
	
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_D) {
			player.direita = false;
			
		}else if(e.getKeyCode() == KeyEvent.VK_A) {
			player.esquerda = false;
			
		}
		if(e.getKeyCode() == KeyEvent.VK_W) {
			player.cima = false;
			
		}else if(e.getKeyCode() == KeyEvent.VK_S) {
			player.baixo = false;
			
		}
		
	}

}



