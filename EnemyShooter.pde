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
  PVector spawn() {
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
  void display() {
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
  void update(){
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
  void fire() {
    if (fireCount > 30)
    {
      PVector dir = PVector.fromAngle(gunAngle);
      bullets.add(new EnemyBullet(new PVector(dir.x*2, dir.y*2), new PVector(bc.x + cos(gunAngle)*bc.radius, bc.y + sin(gunAngle)*bc.radius), this));
      fireCount = 0;
    } else
    {
      fireCount += .2;
    }
  }
  
  //plays a sound file and adds a score when it is shot.
  void breakApart() {
    int temp = (int)random(bigSounds.size());
    bigSounds.get(temp).rewind();
    bigSounds.get(temp).play();
    uiMan.gameMan.enemies.remove(this);
    uiMan.gameMan.score += 150;
  }
}