package com.spardagames.graficos;

import java.awt.Color;
import java.awt.Graphics;


import com.spardagames.main.Game;


public class UI {
	
	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(20, 10, (int) ((Game.player.vidaMax/100)*50), 10);
		g.setColor(Color.RED);
		g.fillRect(20, 10,(int) ((Game.player.vida/100)*50), 10);
	}

}
