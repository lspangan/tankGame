package myGames;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.ImageObserver;
import java.util.Observable;
import java.util.Observer;

import gameCore.TankWorld;
import modifiers.AbstractGameModifier;
import modifiers.motions.InputController;
import modifiers.weapons.SimpleWeapon;

public class PlayerShip extends Ship implements Observer {
    public int lives;
    public int score;
    public Point resetPoint;
    public int respawnCounter;
    public int lastFired=0;
    public boolean isFiring=false;
    // movement flags
    public int left=0,right=0,up=0,down=0;
    public String name;

    public PlayerShip(Point location, Point speed, Image img, int[] controls, String name) {
        super(location,speed,100,img);
        resetPoint = new Point(location);
        this.gunLocation = new Point(18,0);
        
        this.name = name;
        weapon = new SimpleWeapon();
        motion = new InputController(this, controls);
        lives = 2;
        health = 100;
        strength = 100;
        score = 0;
        respawnCounter=0;
    }

    public void draw(Graphics g, ImageObserver observer) {
    	if(respawnCounter<=0)
    		g.drawImage(img, location.x, location.y, observer);
    	else if(respawnCounter==80){
    		TankWorld.getInstance().addClockObserver(this.motion);
    		respawnCounter -=1;
    	}
    	else if(respawnCounter<80){
    		if(respawnCounter%2==0) g.drawImage(img, location.x, location.y, observer);
    		respawnCounter -= 1;
    	}
    	else
    		respawnCounter -= 1;
    }
    
    public void damage(int damageDone){
    	if(respawnCounter<=0)
    		super.damage(damageDone);
    }
    
    public void update(int w, int h) {
    	if(isFiring){
    		int frame = TankWorld.getInstance().getFrameNumber();
    		if(frame>=lastFired+weapon.reload){
    			fire();
    			lastFired= frame;
    		}
    	}
    	
    	if((location.x>0 || right==1) && (location.x<w-width || left==1)){
    		location.x+=(right-left)*speed.x;
    	}
    	if((location.y>0 || down==1) && (location.y<h-height || up==1)){
    		location.y+=(down-up)*speed.x;
    	}
    }
    
    public void startFiring(){
    	isFiring=true;
    }
    
    public void stopFiring(){
    	isFiring=false;
    }
    
    public void fire()
    {
    	if(respawnCounter<=0){
    		weapon.fireWeapon(this);
    		TankWorld.getInstance().sound.play("Resources/snd_explosion1.wav");
    	}
    }
    
    public void die(){
    	this.show=false;
    	BigExplosion explosion = new BigExplosion(new Point(location.x,location.y));
    	TankWorld.getInstance().addBackground(explosion);
    	lives-=1;
    	if(lives>=0){
        	TankWorld.getInstance().removeClockObserver(this.motion);
    		reset();
    	}
    	else{
    		this.motion.delete(this);
    	}
    }
    
    public void reset(){
    	this.setLocation(resetPoint);
    	health=strength;
    	respawnCounter=160;
    	this.weapon = new SimpleWeapon();
    }
    
    public int getLives(){
    	return this.lives;
    }
    
    public int getScore(){
    	return this.score;
    }
    
    public String getName(){
    	return this.name;
    }
    
    public void incrementScore(int increment){
    	score += increment;
    }
    
    public boolean isDead(){
    	if(lives<0 && health<=0)
    		return true;
    	else
    		return false;
    }
    
	public void update(Observable o, Object arg) {
		AbstractGameModifier modifier = (AbstractGameModifier) o;
		modifier.read(this);
	}
}
