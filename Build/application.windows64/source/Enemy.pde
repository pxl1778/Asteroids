class Enemy {
  BoundingCircle bc;
  PVector velocity, position;
  color c;
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
  PVector spawn(){
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

  void display() {
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

  void createEnemy()
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

  void collisionWithShip() {//Checks if the enemy has collided with the ship.
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

  void breakApart() {
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

  void wrap() {
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