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
  
  void display(){
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
  
  void mouseHover(){
    if(mouseX > x && mouseX < x+rectWidth && mouseY > y && mouseY < y+rectHeight)
    {
      fill(0, 255, 0);
      rect(x, y, rectWidth, rectHeight);
    }
  }
  
  void clicked(){
    if(mouseX > x && mouseX < x+rectWidth && mouseY > y && mouseY < y+rectHeight)
    {
      gs = state;
    }
  }
}