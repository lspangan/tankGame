/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myGames;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.ImageObserver;

import gameCore.TankWorld;

public class DestructibleWall extends BackgroundObject{
        int spawnCount = 7000;
	public DestructibleWall(int x, int y){
		super(new Point(x*32, y*32), new Point(0,0), TankWorld.sprites.get("wall2"));
	}    
        
    public boolean collision(GameObject obj) {
        if(location.intersects(obj.getLocation())){
        	if(obj instanceof SimpleBullet) {
        		this.show = false;
                }
        	return true;
                
        } 
        return false;
    }
        
    public void draw(Graphics g, ImageObserver obs) {
    	if(show){
    		super.draw(g, obs);
        } else{
    		this.spawnCount--;
    		if(this.spawnCount<0){
    			this.spawnCount = 7000;
    			this.show = true;
    		}
    	}
    }
        
}
