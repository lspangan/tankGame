package modifiers.weapons;

import gameCore.Tank;
import gameCore.TankWorld;
import java.awt.Point;

import myGames.SimpleBullet;
import myGames.Bullet;
import myGames.Ship;


public class TankWeapon extends AbstractWeapon {
	
	public TankWeapon(){
		super(TankWorld.getInstance());
	}
	
	public void fireWeapon(Ship theTank) {
		super.fireWeapon(theTank);
		Point location = theTank.getLocationPoint();
		Point offset = theTank.getGunLocation();
		location.x+=offset.x;
		location.y+=offset.y;
		Point speed = new Point(0,-15*direction);
		int strength = 10;
		reload = 15;
		
		SimpleBullet bullet = new SimpleBullet(location, speed, strength, (Tank) theTank);
		bullets = new Bullet[1];
		bullets[0] = bullet;
                
		this.setChanged();
		
		this.notifyObservers();

        }
}
