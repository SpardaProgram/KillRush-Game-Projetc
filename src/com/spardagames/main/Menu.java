package com.spardagames.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Menu {
	public String[] options = {"New Game", "Load Game", "Quit"};
	
	public int currentOption = 0;
	public int maxOption = options.length - 1;
	
	public boolean cima,baixo,enter,pause = false;
	
	public void tick() {
		if(cima) {
			cima = false;
			currentOption--;
			if(currentOption<0) {
				currentOption = maxOption;
			}
		}
		if(baixo) {
			baixo = false;
			currentOption++;
			if(currentOption>maxOption) {
				currentOption = 0;
			}
		}
		if(enter) {
			enter = false;
			if(options[currentOption] == "New Game" || options[currentOption] == "Continue") {
				Game.gameState = "NORMAL";
				pause = false;
			}else if(options[currentOption] == "Quit") {
				System.exit(1);
			}
		}
		
	
		
	}

	
		
	
	public static void saveGame(String[] val1, int[] val2, int encode) {
		BufferedWriter write = null;
		try {
			write = new BufferedWriter(new FileWriter("save.txt"));
		}catch(IOException e) {}
		
		for(int i = 0; i<val1.length; i++) {
			String current = val1[i];
			current+=":";
			char[] value = Integer.toString(val2[i]).toCharArray();
			for(int n = 0; n < value.length; n++) {
				value[n]+=encode;
				current+=value[n];
			}
			try {
				write.write(current);
				if(i < val1.length - 1)
					write.newLine();
			}catch(IOException e) {}
		}
		try {
			write.flush();
			write.close();
		}catch(IOException e) {}
		
	}
		
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.largura*Game.escala, Game.altura*Game.escala);
		g.setColor(Color.red);
		g.setFont(new Font("arial", Font.BOLD,36));
		g.drawString("Kill Rush", (Game.largura*Game.escala)/2 - 70, (Game.altura*Game.escala)/2-200);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD,24));
		if(pause == false)
		g.drawString("New Game", (Game.largura*Game.escala)/2 -60,300);
		else
		g.drawString("Continue", (Game.largura*Game.escala)/2 -60,300);
		g.drawString("Load Game", (Game.largura*Game.escala)/2 -60,340);
		g.drawString("Quit", (Game.largura*Game.escala)/2 -60,380);
	
		if(options[currentOption] == "New Game") {
			g.drawString(">", (Game.largura*Game.escala)/2 - 90, 300);
		}else if(options[currentOption] == "Load Game") {
			g.drawString(">", (Game.largura*Game.escala)/2 - 90, 340);
		}else if(options[currentOption] == "Quit") {
			g.drawString(">", (Game.largura*Game.escala)/2 - 90, 380);
		}
	}
}
