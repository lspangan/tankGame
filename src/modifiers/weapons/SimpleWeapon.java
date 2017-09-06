/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modifiers.weapons;


import java.awt.Point;

import myGames.Bullet;
import myGames.Ship;
import modifiers.motions.SimpleMotion;


public class SimpleWeapon extends AbstractWeapon {
	int strength;
	
	public SimpleWeapon(){
		this(5,10);
	}
	
	public SimpleWeapon(int reload){
		this(5,reload);
	}
	
	public SimpleWeapon(int strength, int reload){
		super();
		this.reload = reload;
		this.strength = strength;
	}
	
	@Override
	public void fireWeapon(Ship theShip) {
		super.fireWeapon(theShip);
		Point location = theShip.getLocationPoint();
		Point offset = theShip.getGunLocation();
		location.x+=offset.x;
		location.y+=offset.y;
		Point speed = new Point(0,-15*direction);
		
		Bullet bullet = new Bullet(location, speed, strength, new SimpleMotion(), theShip);
		bullets = new Bullet[1];
		bullets[0] = bullet;
				
		this.setChanged();
		
		this.notifyObservers();
	}

}