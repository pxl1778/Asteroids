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
    firingSpeed = .2;
  }

  void display() {
    //Drawing The Ship
    noFill();
    stroke(21, 255, 0);
    strokeWeight(5);
    ellipse(bc.x, bc.y, bc.radius*2, bc.radius*2);
    //Drawing The Gun
    mouseAngle = atan2(mouseY - bc.y, mouseX - bc.x);
    line(bc.x, bc.y, bc.x + (bc.radius*2/1.4 * cos(mouseAngle)), bc.y + (bc.radius*2/1.4 * sin(mouseAngle)));
    if (tripleBullet)//draws side guns for triplebullet upgrade
    {
      ellipse(bc.x +(bc.radius*1.4*cos(mouseAngle+PI/2)), bc.y + (bc.radius*1.4*sin(mouseAngle+PI/2)), 4, 4);
      ellipse(bc.x + (bc.radius*1.4*cos(mouseAngle-PI/2)), bc.y + (bc.radius*1.4*sin(mouseAngle-PI/2)), 4, 4);
    }
    if (buttShot)//draws back gun for buttShot upgrade
    {
      line(bc.x, bc.y, bc.x + (bc.radius*2/1.4 * cos(mouseAngle + PI)), bc.y + (bc.radius*2/1.4 * sin(mouseAngle + PI)));
    }
    //Drawing the directional
    stroke(232, 207, 21);
    ellipse(bc.x + direction.x*bc.radius, bc.y + direction.y*bc.radius, 8, 8);
  }

  void update() {//Checks key inputs, draws bullets, and removes bullets if off screen.
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
      invincibleCount += .35;
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

  void movement() {
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
      direction.rotate(-.1);
    }
    if (inputMan.isKeyPressed('d'))
    {
      direction.rotate(.1);
    }
    velocity.limit(maxSpeed);
    position.x += velocity.x * fixedDir.x;
    position.y += velocity.y * fixedDir.y;
    bc.x = position.x;
    bc.y = position.y;
    wrap();
  }

  void wrap() {
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

  void fire() {
    if (fireCount > 5)
    {
      PVector dir = PVector.fromAngle(mouseAngle);
      bullets.add(new Bullet(new PVector(dir.x * bulletSpeed, dir.y * bulletSpeed), new PVector(bc.x + (bc.radius/1.4 * cos(mouseAngle)), bc.y + (bc.radius/1.4 * sin(mouseAngle)))));
      if (tripleBullet)//activates the triple Bullet upgrade
      {
        PVector temp = new PVector(bc.x + (bc.radius*1.4 * cos(mouseAngle+PI/2)), bc.y + (bc.radius*1.4 * sin(mouseAngle+PI/2)));
        bullets.add(new Bullet(new PVector(dir.x * bulletSpeed, dir.y * bulletSpeed), temp));
        temp = new PVector(bc.x + (bc.radius*1.4 * cos(mouseAngle-PI/2)), bc.y + (bc.radius*1.4 * sin(mouseAngle-PI/2)));
        bullets.add(new Bullet(new PVector(dir.x * bulletSpeed, dir.y * bulletSpeed), temp));
      }
      if (buttShot)
      {
        dir.rotate(PI);
        bullets.add(new Bullet(new PVector((dir.x) * bulletSpeed, (dir.y) * bulletSpeed), new PVector(bc.x + (bc.radius/1.4 * cos(mouseAngle+PI)), bc.y + (bc.radius/1.4 * sin(mouseAngle+PI)))));
        dir.rotate(PI);
      }
      fireCount = 0;
    }
  }

  void hit() {
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