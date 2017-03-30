class BoundingCircle{
  float x;
  float y;
  float radius;
  color c;
  
  BoundingCircle(float xCoord, float yCoord, float rad)
  {
    x = xCoord;
    y = yCoord;
    radius = rad;
    c = color(255, 0, 0);
  }
  
  void display(){
    stroke(c);
    ellipse(x, y, radius*2, radius*2);
  }
  
  boolean collide(BoundingCircle other)
  {
    if(dist(x, y, other.x, other.y) < radius + other.radius)
    {
      return true;
    }
    return false;
  }
}