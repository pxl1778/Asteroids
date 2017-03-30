class ControlButton extends Button{
  
  Boolean isClassicButton;
  
  ControlButton(GameState s, float xCoord, float yCoord, float w, float h, String n, Boolean icb)
  {
    super(s, xCoord, yCoord, w, h, n);
    isClassicButton = icb;
  }
  
  void clicked(){
    if(mouseX > x && mouseX < x+rectWidth && mouseY > y && mouseY < y+rectHeight)
    {
      classic = isClassicButton;
      uiMan.gameMan = new GameManager(1);
      gs = state;
    }
  }
}