package myGames;

import java.awt.Image;
import java.awt.Point;

import gameCore.TankWorld;

/* Small explosions happen whenever an enemy dies */
public class SmallExplosion extends BackgroundObject {
	int timer;
	int frame;
	static Image animation[] = new Image[] {TankWorld.sprites.get("explosion1_1"),
			TankWorld.sprites.get("explosion1_2"),
			TankWorld.sprites.get("explosion1_3"),
			TankWorld.sprites.get("explosion1_4"),
			TankWorld.sprites.get("explosion1_5"),
			TankWorld.sprites.get("explosion1_6")};
	public SmallExplosion(Point location) {
		super(location, animation[0]);
		timer = 0;
		frame=0;
		TankWorld.sound.play("Resources/snd_explosion2.wav");
	}
	
	public void update(int w, int h){
    	super.update(w, h);
    	timer++;
    	if(timer%6==0){
    		frame++;
    		if(frame< animation.length)
    			this.img = animation[frame];
    		else
    			this.show = false;
                
    	}

	}
        
        public boolean collision(GameObject obj) {
            return false;
        }
}
