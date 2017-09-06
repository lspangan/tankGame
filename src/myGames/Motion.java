/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myGames;

import gameCore.TankWorld;
import modifiers.motions.MotionController;

public class Motion extends MotionController {
    int x, y;
    public Motion(int turningAngle) {
        super(TankWorld.getInstance());
        x = (int)(10*(double)Math.sin(Math.toRadians(turningAngle + 90)));
        y = (int)(10*(double)Math.cos(Math.toRadians(turningAngle + 90)));
    }
    public void read(Object theObject) {
        MoveableObject object = (MoveableObject) theObject;
        object.move(x, y);     
    }
}