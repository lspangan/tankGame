/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myGames;

import java.awt.Point;
import gameCore.TankWorld;

public class IndestructibleWall extends BackgroundObject {
	public IndestructibleWall(int x, int y){
		super(new Point(x*32, y*32), new Point(0,0), TankWorld.sprites.get("wall1"));
	} 
	
	public void damage(int damage){
		return;
	}
}