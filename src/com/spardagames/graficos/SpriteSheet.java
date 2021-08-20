package com.spardagames.graficos;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {
	private BufferedImage spritesheet;
	public SpriteSheet(String path) {
		
		try {
			spritesheet = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	public BufferedImage getSprite(int x, int y, int largura, int altura) {
		return spritesheet.getSubimage(x, y, largura, altura);
	}
}
