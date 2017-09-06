package modifiers.weapons;

import java.util.Observer;

import gameCore.TankWorld;
import myGames.Bullet;
import myGames.PlayerShip;
import myGames.Ship;
import modifiers.AbstractGameModifier;


/*Weapons are fired by motion controllers on behalf of players or ships
 * They observe motions and are observed by the Game World
 */
public abstract class AbstractWeapon extends AbstractGameModifier {
	public Bullet[] bullets;
	boolean friendly;
	int lastFired=0, reloadTime;
	protected int direction;
	public int reload = 5;
	
	public AbstractWeapon(){
		this.addObserver(TankWorld.getInstance());
	}
        
        public AbstractWeapon(Observer obs) {
            super();
            this.addObserver(obs);
        }
	
	public void fireWeapon(Ship theShip){
		if(theShip instanceof PlayerShip){
			direction = 1;
		}
		else{
			direction = -1;
		}
	}
	
	/* read is called by Observers when they are notified of a change */
	public void read(Object theObject) {
		TankWorld world = (TankWorld) theObject;
		world.addBullet(bullets);	
	}
	
	public void remove(){
		this.deleteObserver(TankWorld.getInstance());
	}
}
