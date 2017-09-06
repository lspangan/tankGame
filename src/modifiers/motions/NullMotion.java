/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modifiers.motions;

import java.util.Observable;

/*Motion controller that does nothing*/
public class NullMotion extends MotionController {

	public NullMotion() {
		super();
	}

	@Override
	public void update(Observable o, Object arg) {}

}
