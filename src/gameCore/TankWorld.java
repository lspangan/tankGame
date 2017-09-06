package gameCore;

import ui.*;
import modifiers.*;
import myGames.*;
import java.awt.*;
import java.awt.image.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Observer;
import java.util.Random;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.util.Observable;

import javax.swing.*;

import modifiers.motions.MotionController;
import modifiers.weapons.AbstractWeapon;

// extending JPanel to hopefully integrate this into an applet
// but I want to separate out the Applet and Application implementations
public final class TankWorld extends JPanel implements Runnable, Observer {

    private Thread thread;
    
    // GameWorld is a singleton class!
    private static final TankWorld game = new TankWorld();
    public static final GameSounds sound = new GameSounds();
    public static final GameClock clock = new GameClock();

    public SourceReader reader;
   
    private BufferedImage bimg, t1view, t2view;
    int score = 0, life = 4;
    static Point speed = new Point(0,0), arena;
    Random generator = new Random();
    int sizeX, sizeY;
    boolean splitScreen;
    
    /*Some ArrayLists to keep track of game things*/
    private ArrayList<BackgroundObject> background;
    private ArrayList<Bullet> bullets;
    private ArrayList<PlayerShip> players;
    private ArrayList<InterfaceObject> ui;
    private ArrayList<Ship> powerups;
    private ArrayList<Ship> healthPowerUps;
    private ArrayList<Ship> lifePowerUps;
    private ArrayList<Ship> respawn;
    
    
    public static HashMap<String, Image> sprites;
    public static HashMap<String, MotionController> motions = new HashMap<String, MotionController>();

    // is player still playing, did they win, and should we exit
    boolean gameOver, gameWon, gameFinished;
    ImageObserver observer;
        
    // constructors makes sure the game is focusable, then
    // initializes a bunch of ArrayLists
    private TankWorld(){
        this.setFocusable(true);
        background = new ArrayList<BackgroundObject>();
        bullets = new ArrayList<Bullet>();
        players = new ArrayList<PlayerShip>();
        ui = new ArrayList<InterfaceObject>();
        powerups = new ArrayList<Ship>();
        healthPowerUps = new ArrayList<Ship>();
        lifePowerUps = new ArrayList<Ship>();
        respawn = new ArrayList<Ship>();
        sprites = new HashMap<String,Image>();
    }
    
    /* This returns a reference to the currently running game*/
    public static TankWorld getInstance(){
    	return game;
    }

    /*Game Initialization*/
    public void init() {
        setBackground(Color.BLACK);
        sound.playLoop("ResourcesTank/Chapter11/LOTRgame.wav");
        loadSprites();
        reader = new SourceReader("ResourcesTank/Chapter11/wall.txt");
        reader.addObserver(this);
        clock.addObserver(reader);
        arena = new Point(reader.w*32,reader.h*32);
        TankWorld.setSpeed(new Point(0,0));
        gameOver = false;
        observer = this;
        
        addBackground(new Background(arena.x,arena.y,speed, sprites.get("background")));

        reader.load();
    }
    
    /*Functions for loading image resources*/
    private void loadSprites() {
        sprites.put("background", getSprite("ResourcesTank/Chapter11/Background.png"));
        sprites.put("wall1", getSprite("ResourcesTank/Chapter11/Blue_wall1.png"));
        sprites.put("wall2", getSprite("ResourcesTank/Chapter11/Blue_wall2.png"));

        sprites.put("bullet", getSprite("Resources/bullet.png"));
        sprites.put("enemybullet1", getSprite("Resources/enemybullet1.png"));
        sprites.put("shell", getSprite("ResourcesTank/Chapter11/Shell_heavy_46.png"));

        sprites.put("explosion1_1", getSprite("Resources/explosion1_1.png"));
        sprites.put("explosion1_2", getSprite("Resources/explosion1_2.png"));
        sprites.put("explosion1_3", getSprite("Resources/explosion1_3.png"));
        sprites.put("explosion1_4", getSprite("Resources/explosion1_4.png"));
        sprites.put("explosion1_5", getSprite("Resources/explosion1_5.png"));
        sprites.put("explosion1_6", getSprite("Resources/explosion1_6.png"));
        sprites.put("explosion2_1", getSprite("Resources/explosion2_1.png"));
        sprites.put("explosion2_2", getSprite("Resources/explosion2_2.png"));
        sprites.put("explosion2_3", getSprite("Resources/explosion2_3.png"));
        sprites.put("explosion2_4", getSprite("Resources/explosion2_4.png"));
        sprites.put("explosion2_5", getSprite("Resources/explosion2_5.png"));
        sprites.put("explosion2_6", getSprite("Resources/explosion2_6.png"));
        sprites.put("explosion2_7", getSprite("Resources/explosion2_7.png"));

        sprites.put("life1", getSprite("ResourcesTank/Chapter11/Tank_grey_basic.png"));
        sprites.put("life2", getSprite("ResourcesTank/Chapter11/Tank_grey_basic.png"));

        sprites.put("gameover", getSprite("ResourcesTank/Chapter11/gameover3.png"));
        sprites.put("powerup", getSprite("Resources/powerup.png"));

        sprites.put("player1", getSprite("ResourcesTank/Chapter11/Tank_red_basic_strip60.png"));
        sprites.put("player2", getSprite("ResourcesTank/Chapter11/Tank_blue_basic_strip60.png"));
    }
    
    public Image getSprite(String name) {
        URL url = TankWorld.class.getResource(name);
        Image img = java.awt.Toolkit.getDefaultToolkit().getImage(url);
        try {
            MediaTracker tracker = new MediaTracker(this);
            tracker.addImage(img, 0);
            tracker.waitForID(0);
        } catch (Exception e) {
        }
        return img;
    }
    
    
    
    /********************************
     * 	These functions GET things	*
     * 		from the game world		*
     ********************************/
    
    public int getFrameNumber(){
    	return clock.getFrame();
    }
    
    public int getTime(){
    	return clock.getTime();
    }
    
    public void removeClockObserver(Observer theObject){
    	clock.deleteObserver(theObject);
    }
    
    public ListIterator<BackgroundObject> getBackgroundObjects(){
    	return background.listIterator();
    }
    
    public ListIterator<PlayerShip> getPlayers(){
    	return players.listIterator();
    }
    
    
    public int countPlayers(){
    	return players.size();
    }
    
    public void setDimensions(int w, int h){
    	this.sizeX = w;
    	this.sizeY = h;
    }
    
    public ListIterator<Bullet> getBullets(){
    	return bullets.listIterator();
    }  
    
    public static Point getSpeed(){
    	return new Point(TankWorld.speed);
    }  
    
    public static void setSpeed(Point speed){
    	TankWorld.speed = speed;
    }
    
    /********************************
     * 	These functions ADD things	*
     * 		to the game world		*
     ********************************/
    
    public void addBullet(Bullet...newObjects){
    	for(Bullet bullet : newObjects){
            bullets.add(bullet);
    	}
    }
    
    public void addPlayer(PlayerShip...newObjects){
    	for(PlayerShip player : newObjects){
    		players.add(player);
    		ui.add(new InfoBar(player,Integer.toString(players.size())));
    	}
    }
    
    // add background items (islands)
    public void addBackground(BackgroundObject...newObjects){
    	for(BackgroundObject object : newObjects){
    		background.add(object);
    	}
    }
    
    // add power ups to the game world
    public void addPowerUp(Ship powerup){
    	powerups.add(powerup);
    }
    
    public void addHealthPowerUp(Ship powerup){
        healthPowerUps.add(powerup);
    }
    
    public void addLifePowerUp(Ship powerup){
        lifePowerUps.add(powerup);
    }
    
    public void addRespawn(Ship respawns){
        respawn.add(respawns);
    }
    
    public void addClockObserver(Observer theObject){
    	clock.addObserver(theObject);
    }
    
    // this is the main function where game stuff happens!
    // each frame is also drawn here
    public void drawFrame(int w, int h, Graphics2D g2) {
        ListIterator<?> iterator = getBackgroundObjects();
        // iterate through all blocks
        while (iterator.hasNext()) {
            BackgroundObject obj = (BackgroundObject) iterator.next();
            obj.update(w, h);
            obj.draw(g2, this);

            if (obj instanceof BigExplosion || obj instanceof SmallExplosion) {
                if (!obj.show) {
                    iterator.remove();
                }
                continue;
            }

            // check player-block collisions
            ListIterator<PlayerShip> players = getPlayers();
            while (players.hasNext() && obj.show) {
                Tank player = (Tank) players.next();
                if (obj.collision(player)) {
                    Rectangle location = obj.getLocation();
                    Rectangle playerLocation = player.getLocation();
                    if (playerLocation.y < location.y) {
                        player.move(0, -2);
                    }
                    if (playerLocation.y > location.y) {
                        player.move(0, 2);
                    }
                    if (playerLocation.x < location.x) {
                        player.move(-2, 0);
                    }
                    if (playerLocation.x > location.x) {
                        player.move(2, 0);
                    }
                }
            }
        }

        PlayerShip p1 = players.get(0);
        PlayerShip p2 = players.get(1);

        if (!gameFinished) {

            ListIterator<Bullet> bullets = this.getBullets();
            while (bullets.hasNext()) {
                Bullet bullet = bullets.next();
                if (bullet.getY() > h + 10 || bullet.getY() < -10
                        || bullet.getX() > w + 10 || bullet.getX() < -10) {
                    bullets.remove();
                } else {
                    iterator = this.getBackgroundObjects();
                    while (iterator.hasNext()) {
                        GameObject other = (GameObject) iterator.next();
                        if (other.show && other.collision(bullet)) {
                            addBackground(new SmallExplosion(bullet.getLocationPoint()));
                            bullets.remove();
                            break;
                        }
                    }
                }
                bullet.draw(g2, this);
            }

            // update players and draw
            iterator = getPlayers();
            while (iterator.hasNext()) {
                PlayerShip player = (PlayerShip) iterator.next();
                if (player.isDead()) {
                    endGame(true);
                    continue;
                }

                bullets = this.getBullets();
                while (bullets.hasNext()) {
                    Bullet bullet = bullets.next();
                    if (bullet.collision(player) && player.respawnCounter <= 0 && bullet.getOwner() != player) {
                        player.damage(bullet.getStrength());
                        bullet.getOwner().incrementScore(bullet.getStrength());
                        addBackground(new SmallExplosion(bullet.getLocationPoint()));
                        bullets.remove();
                        if (player.isDead()) {
                            player.setLocation(player.resetPoint);
                        }
                    }

                }

            }

            // powerups
            iterator = powerups.listIterator();
            while (iterator.hasNext()) {
                Ship powerup = (Ship) iterator.next();
                ListIterator<PlayerShip> players = getPlayers();
                while (players.hasNext()) {
                    PlayerShip player = players.next();
                    if (powerup.collision(player)) {
                        AbstractWeapon weapon = powerup.getWeapon();
                        player.setWeapon(weapon);
                        powerup.die();
                        iterator.remove();
                    }
                }
                powerup.draw(g2, this);
            }

            iterator = healthPowerUps.listIterator();
            while (iterator.hasNext()) {
                Ship powerup = (Ship) iterator.next();
                ListIterator<PlayerShip> players = getPlayers();
                while (players.hasNext()) {
                    PlayerShip player = players.next();
                    if (powerup.collision(player)) {
                        AbstractWeapon weapon = powerup.getWeapon();
                        player.setWeapon(weapon);
                        player.setHealth(100);
                        powerup.die();
                        iterator.remove();
                    }
                }
                powerup.draw(g2, this);
            }

            iterator = lifePowerUps.listIterator();
            while (iterator.hasNext()) {
                Ship powerup = (Ship) iterator.next();
                ListIterator<PlayerShip> players = getPlayers();
                while (players.hasNext()) {
                    PlayerShip player = players.next();
                    if (powerup.collision(player)) {
                        AbstractWeapon weapon = powerup.getWeapon();
                        player.setWeapon(weapon);
                        player.lives++;

                        powerup.die();
                        iterator.remove();
                    }
                }
                powerup.draw(g2, this);
            }

            iterator = respawn.listIterator();
            while (iterator.hasNext()) {
                Ship powerup = (Ship) iterator.next();
                ListIterator<PlayerShip> players = getPlayers();
                while (players.hasNext()) {
                    PlayerShip player = players.next();
                    if (powerup.collision(player)) {
                        AbstractWeapon weapon = powerup.getWeapon();
                        player.setWeapon(weapon);
                        player.setLocation(player.resetPoint);
                        powerup.die();
                        iterator.remove();
                    }
                }
                powerup.draw(g2, this);
            }

            // player-to-player collisions
            p1.update(w, h);

            if (p1.collision(p2)) {
                Rectangle pLoc1 = p1.getLocation();
                Rectangle pLoc2 = p2.getLocation();
                if (pLoc1.y < pLoc2.y) {
                    p1.move(0, -2);
                }
                if (pLoc1.y > pLoc2.y) {
                    p1.move(0, 2);
                }
                if (pLoc1.x < pLoc2.x) {
                    p1.move(-2, 0);
                }
                if (pLoc1.x > pLoc2.x) {
                    p1.move(2, 0);
                }
            }
            p2.update(w, h);
            if (p2.collision(p1)) {
                Rectangle pLoc1 = p2.getLocation();
                Rectangle pLoc2 = p1.getLocation();
                if (pLoc1.y < pLoc2.y) {
                    p2.move(0, -1);
                }
                if (pLoc1.y > pLoc2.y) {
                    p2.move(0, 1);
                }
                if (pLoc1.x < pLoc2.x) {
                    p2.move(-1, 0);
                }
                if (pLoc1.x > pLoc2.x) {
                    p2.move(1, 0);
                }
            }
            p1.draw(g2, this);
            p2.draw(g2, this);
        } // end game stuff
        else {
            g2.setColor(Color.white);
            g2.setFont(new Font("Calibri", Font.PLAIN, 120));
            if (gameOver == true) {
                g2.drawImage(sprites.get("gameover"), w/3, h/3, 300, 400, null);       
            }
        }

    }
        
    /* End the game, and signal either a win or loss */
    public void endGame(boolean win){
    	this.gameOver = true;
    	this.gameWon = win;
    }
    
    public boolean isGameOver(){
    	return gameOver;
    }
    
    // signal that we can stop entering the game loop
    public void finishGame(){
    	gameFinished = true;
    }        

    public Graphics2D createGraphics2D(int w, int h) {
        Graphics2D g2 = null;
        if (bimg == null || bimg.getWidth() != w || bimg.getHeight() != h) {
            bimg = (BufferedImage) createImage(w, h);
        }
        g2 = bimg.createGraphics();
        g2.setBackground(getBackground());
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2.clearRect(0, 0, w, h);
        return g2;
    }

    /* paint each frame */
    public void paint(Graphics g) {
        if(players.size()!=0)
        	clock.tick();
    	Dimension windowSize = getSize();
        Graphics2D g2 = createGraphics2D(arena.x, arena.y);
        drawFrame(arena.x, arena.y, g2);
        g2.dispose();
        //g.drawImage(bimg, 0, 0, this);
        
        int x1 = this.players.get(0).getX() - windowSize.width/4 > 0 ? this.players.get(0).getX() - windowSize.width/4 : 0;
        int y1 = this.players.get(0).getY() - windowSize.height/2 > 0 ? this.players.get(0).getY() - windowSize.height/2 : 0;
        
        if(x1 > arena.x-windowSize.width/2){
        	x1 = arena.x-windowSize.width/2;
        }
        if(y1 > arena.y-windowSize.height){
        	y1 = arena.y-windowSize.height;
        }
        
        int x2 = this.players.get(1).getX() - windowSize.width/4 > 0 ? this.players.get(1).getX() - windowSize.width/4 : 0;
        int y2 = this.players.get(1).getY() - windowSize.height/2 > 0 ? this.players.get(1).getY() - windowSize.height/2 : 0;
        
        if(x2 > arena.x-windowSize.width/2){
        	x2 = arena.x-windowSize.width/2;
        }
        if(y2 > arena.y-windowSize.height){
        	y2 = arena.y-windowSize.height;
        }
        
        t1view = bimg.getSubimage(x1, y1, windowSize.width/2, windowSize.height);
        t2view = bimg.getSubimage(x2, y2, windowSize.width/2, windowSize.height);
        g.drawImage(t1view, 0, 0, this);
        g.drawImage(t2view, windowSize.width/2, 0, this);
        g.drawRect(windowSize.width/2-1, 0, 1, windowSize.height);
        g.drawRect(windowSize.width/2-76, 399, 151, 151);
        g.drawImage(bimg, windowSize.width/2-75, 400, 150, 150, observer);
        
        // interface stuff
        ListIterator<InterfaceObject> objects = ui.listIterator();
        int offset = 0;
        while(objects.hasNext()){
        	InterfaceObject object = objects.next();
        	object.draw(g, offset, windowSize.height);
        	offset += 500;
        }
    }

    /* start the game thread*/
    public void start() {
        thread = new Thread(this);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    /* run the game */
    public void run() {
    	
        Thread me = Thread.currentThread();
        while (thread == me) {
        	this.requestFocusInWindow();
            repaint();
          
          try {
                thread.sleep(23); // pause a little to slow things down
            } catch (InterruptedException e) {
                break;
            }
            
        }
    }
    
    

    /*I use the 'read' function to have observables act on their observers.
     */
	@Override
	public void update(Observable o, Object arg) {
		AbstractGameModifier modifier = (AbstractGameModifier) o;
		modifier.read(this);
	}
        
	public static void main(String argv[]) {
	    final TankWorld game = TankWorld.getInstance();
	    JFrame f = new JFrame("Tank Game");
	    f.addWindowListener(new WindowAdapter() {
		    public void windowGainedFocus(WindowEvent e) {
		        game.requestFocusInWindow();
		    }
	    });
	    f.getContentPane().add("Center", game);
	    f.pack();
	    f.setSize(new Dimension(1500, 900));
            game.setDimensions(1400, 900);
	    game.init();
	    f.setVisible(true);
	    f.setResizable(false);
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    game.start();
	}        
}