class GameOverScreen{
  
  Button menuButton;
  
  GameOverScreen(){
    menuButton = new Button(GameState.titleScreen, 300, 440, 150, 90, "Menu");
  }
  
  void display(int score)
  {//displays the game over screen
    background(0);
    textSize(80);
    fill(255, 5, 117);
    text("Game Over", 180, 200);
    textSize(40);
    fill(0, 255, 0);
    text("Score: "+score, 275, 370);
    menuButton.display();
  }
}