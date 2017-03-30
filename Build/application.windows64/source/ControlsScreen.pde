class ControlsScreen{
  
  ControlButton normalButton, classicButton;
  
  ControlsScreen(){
    normalButton = new ControlButton(GameState.titleScreen, 125, 400, 190, 90, "Normal", false);
    classicButton = new ControlButton(GameState.titleScreen, 465, 400, 190, 90, "Classic", true);
  }
  
  void display(){
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