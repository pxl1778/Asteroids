class TitleScreen {

  Button start, controls;
  
  TitleScreen() {
    start = new Button(GameState.game, 310, 300, 170, 90, "START");
    controls = new Button(GameState.controls, 270, 450, 250, 90, "CONTROLS");
    background(0);  
  }

  void display() {
    background(0);
    textSize(70);
    fill(0, 255, 0);
    text("Asteroids", height/2 - 60, 200);
    stroke(0);
    fill(0);
    strokeWeight(5);
    rect(310, 350, 170, 100);
    start.display();
    controls.display();
  }
}