class Bullet {
  PVector velocity, position;
  BoundingCircle bc;
  color c;
 
  Bullet(PVector v, PVector p) {
    velocity = v;
    position = p;
    bc = new BoundingCircle(position.x, position.y, 3);
    c = color(255);
  }
  
  void display(){
    position.add(velocity);
    bc.x = position.x;
    bc.y = position.y;
    strokeWeight(1);
    noFill();
    stroke(c);
    ellipse(bc.x, bc.y, bc.radius*2, bc.radius*2);
    collision();
  }
  
  void collision(){
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