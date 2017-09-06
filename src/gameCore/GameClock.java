package gameCore;

import java.util.Observable;

/*Game clock ticks on every frame and notifies observers to update*/
class GameClock extends Observable {
	public int startTime;
	public int frame;
	
	public GameClock(){
		startTime = (int) System.currentTimeMillis();
		frame = 0;
	}
		
	public void tick(){
		frame++;
		setChanged();
		this.notifyObservers();
	}
	
	public int getFrame(){
		return this.frame;
	}
	
	public int getTime(){
		return (int)System.currentTimeMillis()-startTime;
	}
}
