package modifiers.weapons;

import gameCore.Tank;
import gameCore.TankWorld;
import java.awt.Point;

import myGames.Bullet;
import myGames.PlayerShip;
import myGames.Ship;
import modifiers.motions.SimpleMotion;
import myGames.SimpleBullet;

public class PulseWeapon extends AbstractWeapon {
	public PulseWeapon(){
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
                int reload = 15;
		bullets = new Bullet[3];
		bullets[0] = new SimpleBullet(location, speed, strength, -5, (Tank) theTank);
		bullets[1] = new SimpleBullet(location, speed, strength, 5, (Tank) theTank);
                bullets[2] = new SimpleBullet(location, speed, strength, 10, (Tank) theTank);
				
		this.setChanged();
		this.notifyObservers();

        }
}