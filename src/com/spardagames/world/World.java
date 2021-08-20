package com.spardagames.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.spardagames.entities.Monstro;
import com.spardagames.entities.Jogador;
import com.spardagames.graficos.SpriteSheet;
import com.spardagames.entities.Ammo;
import com.spardagames.entities.Arma;
import com.spardagames.entities.GameObjects;
import com.spardagames.entities.Medkit;
import com.spardagames.main.Game;

public class World {
	
	public static Tile[] tiles;
	public static int largura,altura;
	public static final int tile_size = 16;
	
	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth()* map.getHeight()];
			largura = map.getWidth();
			altura = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels ,0, map.getWidth());
			for(int xx=0;xx<map.getWidth();xx++) {
				for(int yy = 0;yy<map.getHeight();yy++) {
					int pixelAtual = pixels[xx + (yy*map.getWidth())];
					tiles[xx +(yy *largura)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);
					if(pixelAtual == 0xFF000000) {
						//Chão
						tiles[xx +(yy *largura)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);				
						
					}else if(pixelAtual == 0xFFFFFFFF){
						//Parede
						tiles[xx +(yy *largura)] = new WallTile(xx*16,yy*16,Tile.TILE_WALL);					
					
					}else if(pixelAtual == 0xFF0026FF) {
						//Player
						Game.player.setX(xx*16);
						Game.player.setY(yy*16);
					}
					else if(pixelAtual == 0xFFFF0000){
						//enemy
						Monstro en = new Monstro(xx*16,yy*16,16,16,GameObjects.Monstro_EN);
						Game.entities.add(en);
						Game.monstros.add(en);
						
					}else if(pixelAtual == 0xFF404040) {
						//arma
						Game.entities.add(new Arma(xx*16,yy*16,16,16,GameObjects.Arma_EN));
					}else if(pixelAtual == 0xFF4CFF00) {
						//Medkit
						Medkit kit = new Medkit(xx*16,yy*16,16,16,GameObjects.MedKit_EN);
						kit.setMask(8, 8, 8, 8);
						Game.entities.add(kit);
					}else if(pixelAtual == 0xFFFFF951) {
						//ammo
						Game.entities.add(new Ammo(xx*16,yy*16,16,16,GameObjects.Ammo_EN));
					}
				}
				
				}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static boolean isFree(int xnext, int ynext) {
		int x1 = xnext/tile_size;
		int y1 = ynext/tile_size;
		
		int x2 = (xnext+tile_size-1)/tile_size;
		int y2 = ynext / tile_size;
		
		int x3 = xnext / tile_size;
		int y3 = (ynext+tile_size-1) / tile_size;
		
		int x4 = (xnext+tile_size-1)/tile_size;
		int y4 = (ynext+tile_size-1) / tile_size;
		
		return !((tiles[x1 + (y1 * World.largura)] instanceof WallTile) || (tiles[x2 + (y2 * World.largura)] instanceof WallTile) || (tiles[x3 + (y3 * World.largura)] instanceof WallTile) || (tiles[x4 + (y4 * World.largura)] instanceof WallTile));
		
		
	}
	
	public static void restartGame(String level) {
		Game.entities.clear();
		Game.monstros.clear();
		Game.entities = new ArrayList <GameObjects>();
		Game.monstros = new ArrayList <Monstro>();
		Game.spritesheet = new SpriteSheet("/GameObjects.png");
		Game.player = new Jogador(0,0,16,16,Game.spritesheet.getSprite(32, 0, 16, 16));
		Game.entities.add(Game.player);
		Game.world = new World("/"+level);
		return;
	}
	
	public void render(Graphics g) {
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;
		int xfinal = xstart + (Game.largura/16);
		int yfinal = ystart + (Game.altura/16);
		for(int xx = xstart;xx <= xfinal;xx++) {
			for(int yy = ystart;yy <= yfinal;yy++) {
				if(xx<0 || yy<0 || xx>=largura || yy>=altura) {
					continue;
				}
				Tile tile = tiles[xx + (yy*largura)];
				tile.render(g);
			}
		}
	}

}
