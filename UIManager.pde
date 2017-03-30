class UIManager{
  
  GameManager gameMan;
  TitleScreen ts;
  GameOverScreen gos;
  ControlsScreen cs;
  float score;
  boolean isPaused;
  
  UIManager(){
    score = 0;
    gameMan = new GameManager(1);
    ts = new TitleScreen();
    gos = new GameOverScreen();
    cs = new ControlsScreen();
    isPaused = false;
  }
  
  void update(){
    if(gs == GameState.titleScreen)
    {
      ts.display();
    }
    if(gs == GameState.controls)
    {
      cs.display();
    }
    if(gs == GameState.game)
    {
      gameMan.display();
      gameMan.update();
    }
    if(gs == GameState.pause)
    {//pauses the game
      if(inputMan.isKeyReleased('p'))
      {
        isPaused = true;
      }
      //draws pause screen overtop game
      fill(0, 15);
      noStroke();
      rect(0, 0, width, height);
      fill(255);
      textSize(80);
      text("PAUSE", 275, 328);
      if(inputMan.isKeyPressed('p') && isPaused)
      {
        gs = GameState.game;
        isPaused = false;
        uiMan.gameMan.canBePaused = false;
      }
    }
    if(gs == GameState.gameOver)
    {
      gos.display((int)score);
    }
  }
  
  void resetToMenu(){
    ts = new TitleScreen();
    gameMan = new GameManager(1);
    gos = new GameOverScreen();
    isPaused = false;
  }
}