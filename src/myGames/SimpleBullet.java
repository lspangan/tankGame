package myGames;

import java.awt.Point;
import java.awt.Graphics;
import java.awt.image.ImageObserver;

import gameCore.TankWorld;
import gameCore.Tank;


/*Bullets fired by player and enemy weapons*/
public class SimpleBullet extends Bullet {
	public SimpleBullet(Point location, Point speed, int strength, Tank t){
		this(location, speed, strength, 0, t);
	}
	
	public SimpleBullet(Point location, Point speed, int strength, int offset, Tank owner){
		super(location, speed, strength, new Motion(owner.turningAngle+offset), owner);
		this.setImage(TankWorld.sprites.get("bullet"));
	}
        
        public void draw (Graphics g, ImageObserver obs) {
            if (show) {
                g.drawImage(img, location.x, location.y, null);
            }
        }
}