/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameCore;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import myGames.BulletPowerUp;
import modifiers.weapons.TankWeapon;
import modifiers.AbstractGameModifier;
import myGames.PowerUp;
import myGames.DestructibleWall;
import myGames.IndestructibleWall;
import modifiers.weapons.PulseWeapon;

public class SourceReader extends AbstractGameModifier implements Observer {
    int w, h;
    Integer position;
    String filename;
    BufferedReader reader; 
    int endgameDelay = 100;
	/*Constructor sets up arrays of enemies in a LinkedHashMap*/
	public SourceReader(String filename){
		super();
		this.filename = filename;
		String line;
		try {
			reader = new BufferedReader(new InputStreamReader(TankWorld.class.getResource(filename).openStream()));
			line = reader.readLine();
			w = line.length();
			h=0;
			while(line!=null){
				h++;
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	public void read(Object theObject){
	}
	
	public void load(){
		TankWorld world = TankWorld.getInstance();
		
		try {
			reader = new BufferedReader(new InputStreamReader(TankWorld.class.getResource(filename).openStream()));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		String line;
		try {
			line = reader.readLine();
			w = line.length();
			h=0;
			while(line!=null){
				for(int i = 0, n = line.length() ; i < n ; i++) { 
				    char c = line.charAt(i);
				    
				    if(c=='1'){
					int[] controls = {KeyEvent.VK_A,KeyEvent.VK_W, KeyEvent.VK_D, KeyEvent.VK_S, KeyEvent.VK_SPACE};
					world.addPlayer(new Tank(new Point(i*32, h*32),world.sprites.get("player1"), controls, "1"));
				    }
				    
				    if(c=='2'){
				    	int[] controls = {KeyEvent.VK_LEFT,KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_ENTER};
					world.addPlayer(new Tank(new Point(i*32, h*32),world.sprites.get("player2"), controls, "2"));
				    }
				    
				    if(c=='3'){
				    	world.addPowerUp(new PowerUp(new Point(i*32, h*32), 0, new BulletPowerUp()));
				    }
                                    
                                    if(c=='4'){
                                        world.addPowerUp(new PowerUp(new Point(i*32, h*32), 0, new PulseWeapon()));
                                    }
                                    
                                    if(c=='h'){
                                        world.addHealthPowerUp(new PowerUp(new Point(i*32, h*32), 0, new TankWeapon()));
                                    }
                                    
                                    if(c =='l'){
                                        world.addLifePowerUp(new PowerUp(new Point(i*32, h*32), 0, new TankWeapon()));
                                    }
                                    
                                    if(c=='r'){
                                        world.addRespawn(new PowerUp(new Point(i*32, h*32), 0, new TankWeapon()));
                                    }
                                    
				    if(c=='w'){
				    	IndestructibleWall wall = new IndestructibleWall(i,h);
				    	world.addBackground(wall);
				    }
				    
				    if(c=='b'){
				    	DestructibleWall wall = new DestructibleWall(i,h);
				    	world.addBackground(wall);
				    }                                    
				}
				h++;
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		TankWorld world = TankWorld.getInstance();
		if(world.isGameOver()){
			if(endgameDelay<=0){
				world.removeClockObserver(this);
				world.finishGame();
			} else endgameDelay--;
		}   
        }
}
