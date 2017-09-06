/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameCore;

import modifiers.weapons.TankWeapon;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import modifiers.motions.InputController;
import myGames.BigExplosion;
import myGames.PlayerShip;


    public class Tank extends PlayerShip {
        public int turningAngle;
        public Tank(Point location, Image img, int[] controls, String name) {
            super(location, new Point(0,0), img, controls, name);
            resetPoint = new Point(location);
        this.gunLocation = new Point(32,32);
        
        this.name = name;
        weapon = new TankWeapon();
        motion = new InputController(this, controls, TankWorld.getInstance());
        lives = 1;
        health = 100;
        strength = 100;
        score = 0;
        respawnCounter=0;   
        width = 64;
        height = 64;
        turningAngle = 180;
        this.location = new Rectangle(location.x,location.y,width,height);
        }
        
        public void rotate(int angle) {
            this.turningAngle += angle;
            if (this.turningAngle >= 360) {
                this.turningAngle = 0;
            } else if (this.turningAngle < 0) {
                this.turningAngle = 359;
            }
        }        
        
        public void update(int w, int h) {
    	if(isFiring){
    		int frame = TankWorld.getInstance().getFrameNumber();
    		if(frame>=lastFired+weapon.reload){
    			fire();
    			lastFired= frame;
    		}
    	}
    	
    	if(right==1 || left==1){
    		this.rotate(4*(left-right));
    	}
    	if(down==1 || up==1){
        	int y = (int)(6*(double)Math.cos(Math.toRadians(turningAngle+90)));
        	int x = (int)(6*(double)Math.sin(Math.toRadians(this.turningAngle+90)));
    		location.x+=x*(up-down);
    		location.y+=y*(up-down);
        }
    	
    	if(location.y<0) location.y=0;
    	if(location.y>h-this.height) location.y=h-this.height;
    	if(location.x<0) location.x=0;
    	if(location.x>w-this.width) location.x=w-this.width;
    }    
        
        public void draw(Graphics g, ImageObserver obs) {
    	if(respawnCounter<=0)
    		g.drawImage(img,
                            location.x,location.y,location.x+this.getSizeX(), location.y+this.getSizeY(),
                            (turningAngle/6)*this.getSizeX(), 0, ((turningAngle/6)*this.getSizeX())+this.getSizeX(),this.getSizeY(),obs);
    	else if(respawnCounter==80){
    		TankWorld.getInstance().addClockObserver(this.motion);
    		respawnCounter -=1;
    	}
    	else if(respawnCounter<80){
    		if(respawnCounter%2==0)
        		g.drawImage(img,
        			    location.x,location.y,location.x+this.getSizeX(), location.y+this.getSizeY(), 
                                    (turningAngle/6)*this.getSizeX(), 0, ((turningAngle/6)*this.getSizeX())+this.getSizeX(),this.getSizeY(), obs);
    		respawnCounter -= 1;
    	}
    	else
    		respawnCounter -= 1;
    }   
        
    public void respawn(){
    	this.setLocation(resetPoint);
    	health=strength;
    	respawnCounter=160;
    	this.weapon = new TankWeapon();
    }        
        
    public void die(){
    	this.show=false;
    	TankWorld.setSpeed(new Point(0,0));
    	BigExplosion explosion = new BigExplosion(new Point(location.x,location.y));
    	TankWorld.getInstance().addBackground(explosion);
    	lives-=1;
    	if(lives>=0){
        	TankWorld.getInstance().removeClockObserver(this.motion);
    		respawn();
    	}
    	else{
    		this.motion.delete(this);
    	}
    }
    
        
    }
