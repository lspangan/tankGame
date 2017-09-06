/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modifiers.motions;

import myGames.MoveableObject;

public class SimpleMotion extends MotionController {
	
	public SimpleMotion() {
		super();
	}
	
	public void read(Object theObject){
		MoveableObject object = (MoveableObject) theObject;
		object.move();
	}
}