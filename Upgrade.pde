class Upgrade {
  PVector position, velocity;
  BoundingCircle bc;
  Ship ship;
  String upgradeName;
  color c;

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

  void display() {
    position.add(velocity);
    bc.x = position.x;
    bc.y = position.y;
    stroke(c);
    strokeWeight(3);
    ellipse(position.x, position.y, 8, 8);
    wrap();
    collisionWithShip();
  }

  void collisionWithShip() {//checks collision with the ship
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

  void powerUp() {//turns on the corresponding upgrades for the ship
    if (upgradeName == "bulletSpeed")
    {
      ship.bulletSpeed = 10;
    }
    if (upgradeName == "shipSpeed")
    {
      ship.maxSpeed = 4.0;
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
      ship.firingSpeed = .4;
    }
  }

  void removePowerUp() {
    if (upgradeName == "bulletSpeed")
    {
      ship.bulletSpeed = 5;
    }
    if (upgradeName == "shipSpeed")
    {
      ship.maxSpeed = 2.0;
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
      ship.firingSpeed = .2;
    }
  }

  void wrap() {
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