/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myGames;

import gameCore.Tank;
import gameCore.TankWorld;
import java.awt.Point;
import modifiers.weapons.AbstractWeapon;

public class BulletPowerUp extends AbstractWeapon {
	public BulletPowerUp(){
		super(TankWorld.getInstance());
	}
	
	public void fireWeapon(Ship theTank) {
		super.fireWeapon(theTank);
		Point location = theTank.getLocationPoint();
		Point offset = theTank.getGunLocation();
		location.x+=offset.x;
		location.y+=offset.y;
		Point speed = new Point(0,-15*direction);
		int strength = 20;
		reload = 25;
		
		bullets = new Bullet[3];
		bullets[0] = new SimpleBullet(location, speed, strength, -5, (Tank) theTank);
		bullets[1] = new SimpleBullet(location, speed, strength, 5, (Tank) theTank);
                bullets[2] = new SimpleBullet(location, speed, strength, 10, (Tank) theTank);
				
		this.setChanged();
		
		this.notifyObservers();

        }    
}
