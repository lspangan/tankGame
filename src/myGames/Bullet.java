/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myGames;

import java.awt.Point;

import gameCore.TankWorld;
import modifiers.motions.MotionController;

/*Bullets fired by player and enemy weapons*/
public class Bullet extends MoveableObject {
	protected PlayerShip owner;
	boolean friendly;
	
	public Bullet(Point location, Point speed, int strength, MotionController motion, GameObject owner){
		super(location, speed, TankWorld.sprites.get("enemybullet1"));
		this.strength=strength;
		if(owner instanceof PlayerShip){
			this.owner = (PlayerShip) owner;
			this.friendly=true;
			this.setImage(TankWorld.sprites.get("bullet"));
		}
		this.motion = motion;
		motion.addObserver(this);
	}
	
	public PlayerShip getOwner(){
		return owner;
	}
	
	public boolean isFriendly(){
		if(friendly){
			return true;
		}
		return false;
	}
}
