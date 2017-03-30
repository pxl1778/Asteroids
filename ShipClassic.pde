class ShipClassic extends Ship {

  ShipClassic(float a, float ms, float d, float fs)
  {
    super(a, ms, d, fs);
  }

  void display() {
    //Drawing The Ship
    noFill();
    stroke(21, 255, 0);
    strokeWeight(5);
    ellipse(bc.x, bc.y, bc.radius*2, bc.radius*2);
    mouseAngle = direction.heading();//mouseAngle will be wherever the shipClassic is headed
    if (tripleBullet)//draws side guns for triplebullet upgrade
    {
      ellipse(bc.x +(bc.radius*1.4*cos(mouseAngle+PI/2)), bc.y + (bc.radius*1.4*sin(mouseAngle+PI/2)), 4, 4);
      ellipse(bc.x + (bc.radius*1.4*cos(mouseAngle-PI/2)), bc.y + (bc.radius*1.4*sin(mouseAngle-PI/2)), 4, 4);
    }
    if (buttShot)//draws back gun for buttShot upgrade
    {
      line(bc.x, bc.y, bc.x + (bc.radius*2/1.4 * cos(mouseAngle + PI)), bc.y + (bc.radius*2/1.4 * sin(mouseAngle + PI)));
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

  void fire() {
    if (fireCount > 5)
    {
      bullets.add(new Bullet(new PVector(direction.x * bulletSpeed, direction.y * bulletSpeed), new PVector(bc.x + (bc.radius/1.4 * direction.x), bc.y + (bc.radius/1.4 * direction.y))));
      if (tripleBullet)//activates the triple Bullet upgrade
      {
        PVector temp = new PVector(bc.x + (bc.radius*1.4 * cos(mouseAngle+PI/2)), bc.y + (bc.radius*1.4 * sin(mouseAngle+PI/2)));
        bullets.add(new Bullet(new PVector(direction.x * bulletSpeed, direction.y * bulletSpeed), temp));
        temp = new PVector(bc.x + (bc.radius*1.4 * cos(mouseAngle-PI/2)), bc.y + (bc.radius*1.4 * sin(mouseAngle-PI/2)));
        bullets.add(new Bullet(new PVector(direction.x * bulletSpeed, direction.y * bulletSpeed), temp));
      }
      if (buttShot)
      {
        direction.rotate(PI);
        bullets.add(new Bullet(new PVector((direction.x) * bulletSpeed, (direction.y) * bulletSpeed), new PVector(bc.x + (bc.radius/1.4 * cos(mouseAngle+PI)), bc.y + (bc.radius/1.4 * sin(mouseAngle+PI)))));
        direction.rotate(PI);
      }
      fireCount = 0;
    }
  }
}