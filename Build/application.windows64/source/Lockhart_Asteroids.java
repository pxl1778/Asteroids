import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.*; 
import ddf.minim.analysis.*; 
import ddf.minim.effects.*; 
import ddf.minim.signals.*; 
import ddf.minim.spi.*; 
import ddf.minim.ugens.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Lockhart_Asteroids extends PApplet {








enum GameState {
  titleScreen, game, gameOver, pause, controls;
}

enum ShipState {
  active, invincible;
}

Minim minim;
AudioPlayer player;
InputManager inputMan;//Handles all input
UIManager uiMan;//Handles displaying the game screens
GameState gs;//allows for different screens/levels
int mouseCount;
ArrayList<AudioPlayer> bigSounds, littleSounds;
Boolean classic;

public void setup() {
  
  classic = false;
  minim = new Minim(this);
  bigSounds = new ArrayList<AudioPlayer>();
  littleSounds = new ArrayList<AudioPlayer>();
  bigSounds.add(minim.loadFile("Soundfiles/BigAsteroids/Big Asteroid1.mp3"));
  for (int i= 1; i < 8; i++)
  {
    bigSounds.add(minim.loadFile("SoundFiles/BigAsteroids/Big Asteroid"+i+".mp3"));
  }
  for (int i = 1; i<10; i++)
  {
    littleSounds.add(minim.loadFile("SoundFiles/LittleAsteroids/Little asteroids"+i+".mp3"));
  }
  player = minim.loadFile("AsteroidsBackground1.wav");
  inputMan = new InputManager();
  uiMan = new UIManager();
  gs = GameState.titleScreen;
  mouseCount = 0;
  player.play();
  player.loop();
}

public void draw() {
  uiMan.update();
}

public void keyPressed() {
  inputMan.recordKeyPress(key);
}

public void keyReleased() {

  inputMan.recordKeyRelease(key);
}

public void mousePressed() {
  mouseCount++;
  if (mouseCount == 1)
  {
    if (gs == GameState.titleScreen)
    {
      uiMan.ts.start.clicked();
      uiMan.ts.controls.clicked();
    }
    if (gs == GameState.controls)
    {
      uiMan.cs.normalButton.clicked();
      uiMan.cs.classicButton.clicked();
    }
    if (gs == GameState.game && !classic)
    {//fires a bullet when the mouse is clicked.
      uiMan.gameMan.ship.fire();
    }
    if (gs == GameState.gameOver)
    {
      uiMan.gos.menuButton.clicked();
    }
  }
}

public void mouseReleased()
{
  mouseCount = 0;
}
class BoundingCircle{
  float x;
  float y;
  float radius;
  int c;
  
  BoundingCircle(float xCoord, float yCoord, float rad)
  {
    x = xCoord;
    y = yCoord;
    radius = rad;
    c = color(255, 0, 0);
  }
  
  public void display(){
    stroke(c);
    ellipse(x, y, radius*2, radius*2);
  }
  
  public boolean collide(BoundingCircle other)
  {
    if(dist(x, y, other.x, other.y) < radius + other.radius)
    {
      return true;
    }
    return false;
  }
}
class Bullet {
  PVector velocity, position;
  BoundingCircle bc;
  int c;
 
  Bullet(PVector v, PVector p) {
    velocity = v;
    position = p;
    bc = new BoundingCircle(position.x, position.y, 3);
    c = color(255);
  }
  
  public void display(){
    position.add(velocity);
    bc.x = position.x;
    bc.y = position.y;
    strokeWeight(1);
    noFill();
    stroke(c);
    ellipse(bc.x, bc.y, bc.radius*2, bc.radius*2);
    collision();
  }
  
  public void collision(){
    for(int i=0; i < uiMan.gameMan.enemies.size(); i++)
    {
      if(bc.radius + uiMan.gameMan.enemies.get(i).bc.radius > dist(bc.x, bc.y, uiMan.gameMan.enemies.get(i).bc.x, uiMan.gameMan.enemies.get(i).bc.y))
      {
        uiMan.gameMan.enemies.get(i).c = color(0, 255, 0);
        uiMan.gameMan.enemies.get(i).breakApart();
        uiMan.gameMan.ship.bullets.remove(this);
      }
    }
  }
}
class Button{
  
  GameState state;
  float x, y, rectWidth, rectHeight;
  String name;
  
  Button(GameState s, float xCoord, float yCoord, float w, float h, String n)
  {
    state = s;
    x = xCoord;
    y = yCoord;
    rectWidth = w;
    rectHeight = h;
    name = n;
  }
  
  public void display(){
    strokeWeight(8);
    fill(0);
    rect(x, y, rectWidth, rectHeight);
    stroke(0, 255, 0);
    fill(0, 255, 0, 30);
    rect(x, y, rectWidth, rectHeight);
    mouseHover();
    textSize(35);
    fill(255);
    text(name, x+30, y+(rectHeight - 30));
    
  }
  
  public void mouseHover(){
    if(mouseX > x && mouseX < x+rectWidth && mouseY > y && mouseY < y+rectHeight)
    {
      fill(0, 255, 0);
      rect(x, y, rectWidth, rectHeight);
    }
  }
  
  public void clicked(){
    if(mouseX > x && mouseX < x+rectWidth && mouseY > y && mouseY < y+rectHeight)
    {
      gs = state;
    }
  }
}
class ControlButton extends Button{
  
  Boolean isClassicButton;
  
  ControlButton(GameState s, float xCoord, float yCoord, float w, float h, String n, Boolean icb)
  {
    super(s, xCoord, yCoord, w, h, n);
    isClassicButton = icb;
  }
  
  public void clicked(){
    if(mouseX > x && mouseX < x+rectWidth && mouseY > y && mouseY < y+rectHeight)
    {
      classic = isClassicButton;
      uiMan.gameMan = new GameManager(1);
      gs = state;
    }
  }
}
class ControlsScreen{
  
  ControlButton normalButton, classicButton;
  
  ControlsScreen(){
    normalButton = new ControlButton(GameState.titleScreen, 125, 400, 190, 90, "Normal", false);
    classicButton = new ControlButton(GameState.titleScreen, 465, 400, 190, 90, "Classic", true);
  }
  
  public void display(){
    background(0);
    fill(0, 255, 0);
    textSize(70);
    text("Controls", 250, 150);
    textSize(20);
    text("Shoot in the direction\nof the mouse and \nclick to fire", 125, 300);
    text("Shoot in the direction\nof the ship and hit \nspacebar to fire", 465, 300);
    normalButton.display();
    classicButton.display();
  }
}
class Enemy {
  BoundingCircle bc;
  PVector velocity, position;
  int c;
  boolean subdivision, collidedWithShip;
  PShape rockShape;

  Enemy() {//For random spawning
    subdivision = false;
    collidedWithShip = false;
    position = spawn();
    velocity = new PVector(random(8)-4, random(8)-4);
    bc = new BoundingCircle(position.x, position.y, random(8, 25));
    c = color(0, 198, 255);
    createEnemy();
  }

  Enemy(PVector p, float rad) {//For when the asteroid breaks apart.
    subdivision = true;
    collidedWithShip = false;
    position = PVector.add(p, new PVector(random(10)-5, random(10)-5));
    bc = new BoundingCircle(position.x, position.y, rad);
    velocity = PVector.random2D();
    velocity.mult(random(4)-2);
    c = color(169, 230, 255);
    createEnemy();
  }
  
  //Returns a PVector of the position the enemy should be spawned at.
  public PVector spawn(){
    float x = random(width);
    float y = random(height);
    while (x < 100 && x > 700)
    {//making sure the enemies don't spawn too close to the player
      x = random(width);
    }
    while (y < 100 && y > 500)
    {//making sure the enemies don't spawn too close to the player
      y = random(height);
    }
    return new PVector(x, y);
  }

  public void display() {
    position.add(velocity);
    bc.x = position.x;
    bc.y = position.y;
    wrap();
    stroke(c);
    strokeWeight(5);
    //ellipse(bc.x, bc.y, bc.radius*2, bc.radius*2);
    shape(rockShape, position.x, position.y);
    collisionWithShip();
  }

  public void createEnemy()
  {
    rockShape = createShape(GROUP);
    PVector temp1;
    PVector temp2;
    PVector firstPoint = PVector.fromAngle(0).mult(bc.radius);
    firstPoint.add(random(4)-2, random(4)-2);
    temp1 = firstPoint.copy();
    for (int i = 1; i < 7; i++)
    {
      temp2 = PVector.fromAngle((PI/4)*i).mult(bc.radius);
      temp2.add(new PVector(random(4)-2, random(4) - 2));
      stroke(c);
      strokeWeight(5);
      rockShape.addChild(createShape(LINE, temp1.x, temp1.y, temp2.x, temp2.y));
      rockShape.addChild(createShape(ELLIPSE, 0, 0, 4, 4));
      temp1 = temp2.copy();
    }
    rockShape.addChild(createShape(LINE, temp1.x, temp1.y, firstPoint.x, firstPoint.y));
  }

  public void collisionWithShip() {//Checks if the enemy has collided with the ship.
    if (bc.radius + uiMan.gameMan.ship.bc.radius > dist(bc.x, bc.y, uiMan.gameMan.ship.bc.x, uiMan.gameMan.ship.bc.y) && collidedWithShip == false && uiMan.gameMan.ship.ss == ShipState.active)
    {
      uiMan.gameMan.ship.hit();
      collidedWithShip = true;
    }
    if (bc.radius + uiMan.gameMan.ship.bc.radius < dist(bc.x, bc.y, uiMan.gameMan.ship.bc.x, uiMan.gameMan.ship.bc.y))
    {
      collidedWithShip = false;
    }
  }

  public void breakApart() {
    if (subdivision==false)
    {
      int temp = (int)random(bigSounds.size());
      bigSounds.get(temp).rewind();
      bigSounds.get(temp).play();
      uiMan.gameMan.enemies.add(new Enemy(position, bc.radius/2));
      uiMan.gameMan.enemies.add(new Enemy(position, bc.radius/2));
      uiMan.gameMan.enemies.remove(this);
      uiMan.gameMan.score += 25;
      float r = random(15);
      if(r < 1)
      {
        uiMan.gameMan.upgrades.add(new Upgrade(position.copy(), uiMan.gameMan.ship));
      }
    } else
    {
      int temp = (int)random(littleSounds.size());
      littleSounds.get(temp).rewind();
      littleSounds.get(temp).play();
      uiMan.gameMan.enemies.remove(this);
      uiMan.gameMan.score += 50;
    }
  }

  public void wrap() {
    //Allows the enemy to wrap around to the other side of the screen.
    if (position.x > width)
    {
      position.x = 0;
    }
    if (position.x < 0)
    {
      position.x = width;
    }
    if (position.y > height)
    {
      position.y = 0;
    }
    if (position.y < 0)
    {
      position.y = height;
    }
  }
}
class EnemyBullet extends Bullet {

  EnemyShooter es;
  EnemyBullet(PVector v, PVector p, EnemyShooter enemShoot) {
    super(v, p);
    c = color(255, 0, 0);
    es = enemShoot;
  }

  public void collision() {
    if(bc.radius + uiMan.gameMan.ship.bc.radius > dist(bc.x, bc.y, uiMan.gameMan.ship.bc.x, uiMan.gameMan.ship.bc.y))
    {
      uiMan.gameMan.ship.hit();
      es.bullets.remove(this);
    }
  }
}
class EnemyShooter extends Enemy {

  float gunAngle, fireCount;
  ArrayList<EnemyBullet> bullets;
  Boolean canFire;

  EnemyShooter() {
    subdivision = false;
    collidedWithShip = false;
    position = spawn();
    bc = new BoundingCircle(position.x, position.y, 12);
    canFire = true;
    bullets = new ArrayList<EnemyBullet>();
    c = color(204, 41, 85);
  }

  //Determines which side of the screen the shooter will come from.
  //also determines the direction of speed.
  public PVector spawn() {
    float temp = random(4);
    float x;
    float y;
    if (temp < 1)
    {
      x = 0;
      y = random(height);
      velocity = new PVector(2, 0);
    } else if (temp < 2)
    {
      x = random(width);
      y = height;
      velocity = new PVector(0, -2);
    } else if (temp < 3)
    {
      x = width;
      y = random(height);
      velocity = new PVector(-2, 0);
    } else
    {
      x = random(width);
      y = 0;
      velocity = new PVector(0, 2);
    }
    return new PVector(x, y);
  }

  //displays the enemy and updates it.
  public void display() {
    //draws ship
    noFill();
    stroke(c);
    strokeWeight(3);
    ellipse(bc.x, bc.y, bc.radius*2, bc.radius*2);
    //draws gun
    gunAngle = atan2(uiMan.gameMan.ship.position.y - position.y, uiMan.gameMan.ship.position.x - position.x);
    line(position.x, position.y, position.x + bc.radius*cos(gunAngle), position.y + bc.radius*sin(gunAngle));
    update();
  }

  //updates the position and shows the bullets
  public void update(){
    fire();
    for(int i = 0; i< bullets.size(); i++)
    {
      bullets.get(i).display();
    }
    position.add(velocity);
    bc.x = position.x;
    bc.y = position.y;
    collisionWithShip();
    wrap();
  }
  
  //fires a bullet at incremented times
  public void fire() {
    if (fireCount > 30)
    {
      PVector dir = PVector.fromAngle(gunAngle);
      bullets.add(new EnemyBullet(new PVector(dir.x*2, dir.y*2), new PVector(bc.x + cos(gunAngle)*bc.radius, bc.y + sin(gunAngle)*bc.radius), this));
      fireCount = 0;
    } else
    {
      fireCount += .2f;
    }
  }
  
  //plays a sound file and adds a score when it is shot.
  public void breakApart() {
    int temp = (int)random(bigSounds.size());
    bigSounds.get(temp).rewind();
    bigSounds.get(temp).play();
    uiMan.gameMan.enemies.remove(this);
    uiMan.gameMan.score += 150;
  }
}
class GameManager {

  Ship ship;
  ArrayList<Enemy> enemies;
  ArrayList<Upgrade> upgrades;
  int level, score, lives;
  boolean canBePaused;
  PShape lifeIcon;

  GameManager(int l) {
    level = l;
    if(classic)
    {
      ship = new ShipClassic(.02f, 2.0f, .99f, 5);
    }
    else
    {
      ship = new Ship(.02f, 2.0f, .99f, 5);
    }
    enemies = new ArrayList<Enemy>();
    for (int i = 0; i < level*2; i++)
    {
      enemies.add(new Enemy());
    }
    score = 0;
    lives = 3;
    canBePaused = false;
    lifeIcon = createShape(GROUP);
    strokeWeight(2);
    stroke(0, 255, 0);
    noFill();
    lifeIcon.addChild(createShape(ELLIPSE, 12, 12, 12, 12));
    lifeIcon.addChild(createShape(LINE, 12, 12, 18, 6));
    upgrades = new ArrayList<Upgrade>();
  }

  public void display() {
    background(0);
    ship.update();
    for (int i=0; i<enemies.size(); i++)
    {
      enemies.get(i).display();
    }
    textSize(20);
    fill(255);
    text("Level: "+level, 5, 20);
    text("Score: " + score, 600, 20);
    for (int i=0; i<lives; i++)
    {
      stroke(0, 255, 0);
      shape(lifeIcon, 70+(i*24), 570);
      text("Lives:", 10, 590);
    }
  }

  public void update() {
    if (enemies.size() == 0)
    {
      resetGame();
    }
    if (inputMan.isKeyReleased('p'))
    {
      canBePaused = true;
    }
    if (inputMan.isKeyPressed('p') && canBePaused)
    {
      gs = GameState.pause;
      uiMan.isPaused = false;
    }
    if (lives <= 0)
    {
      gs = GameState.gameOver;
      uiMan.score = score;
      uiMan.gameMan = new GameManager(1);
    }
    for (int i=0; i < upgrades.size(); i++)
    {
      upgrades.get(i).display();
    }
  }

  public void resetGame() {
    ship.position = new PVector(width/2, height/2);
    level ++;
    for (int i=0; i<level*2; i++)
    {
      float temp = random(10);
      if (temp < 1)
      {
        enemies.add(new EnemyShooter());
      } else
      {
        enemies.add(new Enemy());
      }
    }
    if (level%5 == 0)
    {
      lives ++;
    }
    ship.invincibleCount = 0;
    ship.ss = ShipState.invincible;
  }
}
class GameOverScreen{
  
  Button menuButton;
  
  GameOverScreen(){
    menuButton = new Button(GameState.titleScreen, 300, 440, 150, 90, "Menu");
  }
  
  public void display(int score)
  {//displays the game over screen
    background(0);
    textSize(80);
    fill(255, 5, 117);
    text("Game Over", 180, 200);
    textSize(40);
    fill(0, 255, 0);
    text("Score: "+score, 275, 370);
    menuButton.display();
  }
}
class InputManager{
  boolean[] keys;
  boolean[] prevKeys;
  
  InputManager(){
    keys = new boolean[256];
    prevKeys = new boolean[256];
  }
  
  public void recordKeyPress(int k){
    keys[k] = true;
    prevKeys[k] = false;
  }
  
  public void recordKeyRelease(int k) {
    keys[k] = false;
    prevKeys[k] = true;
  }
  
  public boolean isKeyPressed(int k){
    return keys[k];
  }
  
  public boolean isKeyReleased(int k){
    return !keys[k];
  }
  
}
class Ship {
  BoundingCircle bc;
  PShape gun;
  PVector position, velocity, acceleration, direction, fixedDir;
  float maxSpeed, decceleration, mouseAngle, bulletSpeed, invincibleCount, fireCount, firingSpeed;
  ArrayList<Bullet> bullets;
  ArrayList<Upgrade> upgrades;
  boolean tripleBullet, buttShot;//upgrade booleans
  ShipState ss;//Used to make the ship invincible at the start of every level or when it loses a life.

  Ship(float a, float ms, float d, float fs) {
    ss = ShipState.invincible;
    bc = new BoundingCircle(width/2, height/2, 15);
    position = new PVector(width/2, height/2);
    velocity = new PVector(0, 0);
    acceleration = new PVector(a, a);
    direction = new PVector(-1, 0);
    maxSpeed = ms;
    decceleration = d;
    fixedDir = direction.copy();
    bulletSpeed = fs;
    bullets = new ArrayList<Bullet>();
    invincibleCount= 0;
    upgrades = new ArrayList<Upgrade>();
    tripleBullet = false;
    buttShot = false;
    fireCount = 0;
    firingSpeed = .2f;
  }

  public void display() {
    //Drawing The Ship
    noFill();
    stroke(21, 255, 0);
    strokeWeight(5);
    ellipse(bc.x, bc.y, bc.radius*2, bc.radius*2);
    //Drawing The Gun
    mouseAngle = atan2(mouseY - bc.y, mouseX - bc.x);
    line(bc.x, bc.y, bc.x + (bc.radius*2/1.4f * cos(mouseAngle)), bc.y + (bc.radius*2/1.4f * sin(mouseAngle)));
    if (tripleBullet)//draws side guns for triplebullet upgrade
    {
      ellipse(bc.x +(bc.radius*1.4f*cos(mouseAngle+PI/2)), bc.y + (bc.radius*1.4f*sin(mouseAngle+PI/2)), 4, 4);
      ellipse(bc.x + (bc.radius*1.4f*cos(mouseAngle-PI/2)), bc.y + (bc.radius*1.4f*sin(mouseAngle-PI/2)), 4, 4);
    }
    if (buttShot)//draws back gun for buttShot upgrade
    {
      line(bc.x, bc.y, bc.x + (bc.radius*2/1.4f * cos(mouseAngle + PI)), bc.y + (bc.radius*2/1.4f * sin(mouseAngle + PI)));
    }
    //Drawing the directional
    stroke(232, 207, 21);
    ellipse(bc.x + direction.x*bc.radius, bc.y + direction.y*bc.radius, 8, 8);
  }

  public void update() {//Checks key inputs, draws bullets, and removes bullets if off screen.
    movement();
    for (int i=0; i<bullets.size(); i++)
    {
      bullets.get(i).display();
    }
    int count = 0;
    while (count < bullets.size())
    {//Checks to see if the bullets are still on screen or not. If not, they are removed from the list
      if (bullets.get(count).position.x > width || bullets.get(count).position.x < 0 || bullets.get(count).position.y > height || bullets.get(count).position.y < 0)
      {
        bullets.remove(count);
      }
      count++;
    }
    display();
    if (ss == ShipState.invincible)
    {//Keeps the ship invincible for a short time
      //Draws an ellipse to 
      invincibleCount += .35f;
      noStroke();
      fill(255, 120 - invincibleCount);
      ellipse(position.x, position.y, 60, 60);
      if (invincibleCount >= 80)
      {
        ss = ShipState.active;
      }
    }
    fireCount += firingSpeed;
  }

  public void movement() {
    //Checks keys that are pressed and always updates
    //the position of the ship.
    //fixedDir allows the ship to keep moving in the direction it was propelled.
    if (inputMan.isKeyPressed('w'))
    {
      velocity.add(acceleration);
      fixedDir = direction.copy();
    }
    if (inputMan.isKeyReleased('w'))
    {
      velocity.mult(decceleration);
    }
    if (inputMan.isKeyPressed('a'))
    {
      direction.rotate(-.1f);
    }
    if (inputMan.isKeyPressed('d'))
    {
      direction.rotate(.1f);
    }
    velocity.limit(maxSpeed);
    position.x += velocity.x * fixedDir.x;
    position.y += velocity.y * fixedDir.y;
    bc.x = position.x;
    bc.y = position.y;
    wrap();
  }

  public void wrap() {
    //Allows the ship to wrap around to the other side of the screen.
    if (position.x > width)
    {
      position.x = 0;
    }
    if (position.x < 0)
    {
      position.x = width;
    }
    if (position.y > height)
    {
      position.y = 0;
    }
    if (position.y < 0)
    {
      position.y = height;
    }
  }

  public void fire() {
    if (fireCount > 5)
    {
      PVector dir = PVector.fromAngle(mouseAngle);
      bullets.add(new Bullet(new PVector(dir.x * bulletSpeed, dir.y * bulletSpeed), new PVector(bc.x + (bc.radius/1.4f * cos(mouseAngle)), bc.y + (bc.radius/1.4f * sin(mouseAngle)))));
      if (tripleBullet)//activates the triple Bullet upgrade
      {
        PVector temp = new PVector(bc.x + (bc.radius*1.4f * cos(mouseAngle+PI/2)), bc.y + (bc.radius*1.4f * sin(mouseAngle+PI/2)));
        bullets.add(new Bullet(new PVector(dir.x * bulletSpeed, dir.y * bulletSpeed), temp));
        temp = new PVector(bc.x + (bc.radius*1.4f * cos(mouseAngle-PI/2)), bc.y + (bc.radius*1.4f * sin(mouseAngle-PI/2)));
        bullets.add(new Bullet(new PVector(dir.x * bulletSpeed, dir.y * bulletSpeed), temp));
      }
      if (buttShot)
      {
        dir.rotate(PI);
        bullets.add(new Bullet(new PVector((dir.x) * bulletSpeed, (dir.y) * bulletSpeed), new PVector(bc.x + (bc.radius/1.4f * cos(mouseAngle+PI)), bc.y + (bc.radius/1.4f * sin(mouseAngle+PI)))));
        dir.rotate(PI);
      }
      fireCount = 0;
    }
  }

  public void hit() {
    uiMan.gameMan.lives--;
    position.x = width/2;
    position.y = height/2;
    ss = ShipState.invincible;
    invincibleCount = 0;
    if (upgrades.size() > 0)
    {
      upgrades.get(0).removePowerUp();
      upgrades.remove(0);
    }
  }
}
class ShipClassic extends Ship {

  ShipClassic(float a, float ms, float d, float fs)
  {
    super(a, ms, d, fs);
  }

  public void display() {
    //Drawing The Ship
    noFill();
    stroke(21, 255, 0);
    strokeWeight(5);
    ellipse(bc.x, bc.y, bc.radius*2, bc.radius*2);
    mouseAngle = direction.heading();//mouseAngle will be wherever the shipClassic is headed
    if (tripleBullet)//draws side guns for triplebullet upgrade
    {
      ellipse(bc.x +(bc.radius*1.4f*cos(mouseAngle+PI/2)), bc.y + (bc.radius*1.4f*sin(mouseAngle+PI/2)), 4, 4);
      ellipse(bc.x + (bc.radius*1.4f*cos(mouseAngle-PI/2)), bc.y + (bc.radius*1.4f*sin(mouseAngle-PI/2)), 4, 4);
    }
    if (buttShot)//draws back gun for buttShot upgrade
    {
      line(bc.x, bc.y, bc.x + (bc.radius*2/1.4f * cos(mouseAngle + PI)), bc.y + (bc.radius*2/1.4f * sin(mouseAngle + PI)));
    }
    //Drawing The Gun    
    stroke(21, 255, 0);
    strokeWeight(5);
    line(bc.x, bc.y, bc.x + direction.x*bc.radius, bc.y + direction.y*bc.radius);
    //Drawing the directional
    stroke(232, 207, 21);
    ellipse(bc.x + direction.x*bc.radius, bc.y + direction.y*bc.radius, 8, 8);
    if (inputMan.isKeyPressed(32))
    {
      fire();
    }
  }

  public void fire() {
    if (fireCount > 5)
    {
      bullets.add(new Bullet(new PVector(direction.x * bulletSpeed, direction.y * bulletSpeed), new PVector(bc.x + (bc.radius/1.4f * direction.x), bc.y + (bc.radius/1.4f * direction.y))));
      if (tripleBullet)//activates the triple Bullet upgrade
      {
        PVector temp = new PVector(bc.x + (bc.radius*1.4f * cos(mouseAngle+PI/2)), bc.y + (bc.radius*1.4f * sin(mouseAngle+PI/2)));
        bullets.add(new Bullet(new PVector(direction.x * bulletSpeed, direction.y * bulletSpeed), temp));
        temp = new PVector(bc.x + (bc.radius*1.4f * cos(mouseAngle-PI/2)), bc.y + (bc.radius*1.4f * sin(mouseAngle-PI/2)));
        bullets.add(new Bullet(new PVector(direction.x * bulletSpeed, direction.y * bulletSpeed), temp));
      }
      if (buttShot)
      {
        direction.rotate(PI);
        bullets.add(new Bullet(new PVector((direction.x) * bulletSpeed, (direction.y) * bulletSpeed), new PVector(bc.x + (bc.radius/1.4f * cos(mouseAngle+PI)), bc.y + (bc.radius/1.4f * sin(mouseAngle+PI)))));
        direction.rotate(PI);
      }
      fireCount = 0;
    }
  }
}
class TitleScreen {

  Button start, controls;
  
  TitleScreen() {
    start = new Button(GameState.game, 310, 300, 170, 90, "START");
    controls = new Button(GameState.controls, 270, 450, 250, 90, "CONTROLS");
    background(0);  
  }

  public void display() {
    background(0);
    textSize(70);
    fill(0, 255, 0);
    text("Asteroids", height/2 - 60, 200);
    stroke(0);
    fill(0);
    strokeWeight(5);
    rect(310, 350, 170, 100);
    start.display();
    controls.display();
  }
}
class UIManager{
  
  GameManager gameMan;
  TitleScreen ts;
  GameOverScreen gos;
  ControlsScreen cs;
  float score;
  boolean isPaused;
  
  UIManager(){
    score = 0;
    gameMan = new GameManager(1);
    ts = new TitleScreen();
    gos = new GameOverScreen();
    cs = new ControlsScreen();
    isPaused = false;
  }
  
  public void update(){
    if(gs == GameState.titleScreen)
    {
      ts.display();
    }
    if(gs == GameState.controls)
    {
      cs.display();
    }
    if(gs == GameState.game)
    {
      gameMan.display();
      gameMan.update();
    }
    if(gs == GameState.pause)
    {//pauses the game
      if(inputMan.isKeyReleased('p'))
      {
        isPaused = true;
      }
      //draws pause screen overtop game
      fill(0, 15);
      noStroke();
      rect(0, 0, width, height);
      fill(255);
      textSize(80);
      text("PAUSE", 275, 328);
      if(inputMan.isKeyPressed('p') && isPaused)
      {
        gs = GameState.game;
        isPaused = false;
        uiMan.gameMan.canBePaused = false;
      }
    }
    if(gs == GameState.gameOver)
    {
      gos.display((int)score);
    }
  }
  
  public void resetToMenu(){
    ts = new TitleScreen();
    gameMan = new GameManager(1);
    gos = new GameOverScreen();
    isPaused = false;
  }
}
class Upgrade {
  PVector position, velocity;
  BoundingCircle bc;
  Ship ship;
  String upgradeName;
  int c;

  Upgrade(PVector p, Ship s) {
    bc = new BoundingCircle(p.x, p.y, 4);
    velocity = PVector.random2D().mult(random(3));
    position = p;
    ship = s;
    float temp = random(5);
    //Randomize what kind of upgrade it is.
    if (temp < 1)
    {
      upgradeName = "bulletSpeed";
      c = color(255, 82, 159);
    } else if (temp < 2)
    {
      upgradeName = "shipSpeed";
      c = color(255, 84, 0);
    } else if (temp < 3)
    {
      upgradeName = "tripleBullet";
      c = color(0, 255, 89);
    } else if (temp < 4)
    {
      upgradeName = "buttShot";
      c = color(251, 253, 255);
    } else if (temp < 5)
    {
      upgradeName = "firingSpeed";
      c = color(100, 150, 200);
    }
  }

  public void display() {
    position.add(velocity);
    bc.x = position.x;
    bc.y = position.y;
    stroke(c);
    strokeWeight(3);
    ellipse(position.x, position.y, 8, 8);
    wrap();
    collisionWithShip();
  }

  public void collisionWithShip() {//checks collision with the ship
    if (bc.radius + ship.bc.radius > dist(bc.x, bc.y, ship.bc.x, ship.bc.y))
    {
      Boolean temp = true;
      for (int i=0; i<ship.upgrades.size(); i++)
      {
        if (ship.upgrades.get(i).upgradeName == this.upgradeName)
        {
          temp = false;
          uiMan.gameMan.score += 200;
        }
      }
      if (temp)
      {
        ship.upgrades.add(this);
        powerUp();
      }
      uiMan.gameMan.upgrades.remove(this);
    }
  }

  public void powerUp() {//turns on the corresponding upgrades for the ship
    if (upgradeName == "bulletSpeed")
    {
      ship.bulletSpeed = 10;
    }
    if (upgradeName == "shipSpeed")
    {
      ship.maxSpeed = 4.0f;
    }
    if (upgradeName == "tripleBullet")
    {
      ship.tripleBullet = true;
    }
    if (upgradeName == "buttShot")
    {
      ship.buttShot = true;
    }
    if (upgradeName == "firingSpeed")
    {
      ship.firingSpeed = .4f;
    }
  }

  public void removePowerUp() {
    if (upgradeName == "bulletSpeed")
    {
      ship.bulletSpeed = 5;
    }
    if (upgradeName == "shipSpeed")
    {
      ship.maxSpeed = 2.0f;
    }
    if (upgradeName == "tripleBullet")
    {
      ship.tripleBullet = false;
    }
    if (upgradeName == "buttShot")
    {
      ship.buttShot = false;
    }
    if (upgradeName == "firingSpeed")
    {
      ship.firingSpeed = .2f;
    }
  }

  public void wrap() {
    //Allows the upgrade to wrap around to the other side of the screen.
    if (position.x > width)
    {
      position.x = 0;
    }
    if (position.x < 0)
    {
      position.x = width;
    }
    if (position.y > height)
    {
      position.y = 0;
    }
    if (position.y < 0)
    {
      position.y = height;
    }
  }
}
  public void settings() {  size(800, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Lockhart_Asteroids" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
