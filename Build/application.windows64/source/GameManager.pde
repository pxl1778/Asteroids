class GameManager {

  Ship ship;
  ArrayList<Enemy> enemies;
  ArrayList<Upgrade> upgrades;
  int level, score, lives;
  boolean canBePaused;
  PShape lifeIcon;

  GameManager(int l) {
    level = l;
    if(classic)
    {
      ship = new ShipClassic(.02, 2.0, .99, 5);
    }
    else
    {
      ship = new Ship(.02, 2.0, .99, 5);
    }
    enemies = new ArrayList<Enemy>();
    for (int i = 0; i < level*2; i++)
    {
      enemies.add(new Enemy());
    }
    score = 0;
    lives = 3;
    canBePaused = false;
    lifeIcon = createShape(GROUP);
    strokeWeight(2);
    stroke(0, 255, 0);
    noFill();
    lifeIcon.addChild(createShape(ELLIPSE, 12, 12, 12, 12));
    lifeIcon.addChild(createShape(LINE, 12, 12, 18, 6));
    upgrades = new ArrayList<Upgrade>();
  }

  void display() {
    background(0);
    ship.update();
    for (int i=0; i<enemies.size(); i++)
    {
      enemies.get(i).display();
    }
    textSize(20);
    fill(255);
    text("Level: "+level, 5, 20);
    text("Score: " + score, 600, 20);
    for (int i=0; i<lives; i++)
    {
      stroke(0, 255, 0);
      shape(lifeIcon, 70+(i*24), 570);
      text("Lives:", 10, 590);
    }
  }

  void update() {
    if (enemies.size() == 0)
    {
      resetGame();
    }
    if (inputMan.isKeyReleased('p'))
    {
      canBePaused = true;
    }
    if (inputMan.isKeyPressed('p') && canBePaused)
    {
      gs = GameState.pause;
      uiMan.isPaused = false;
    }
    if (lives <= 0)
    {
      gs = GameState.gameOver;
      uiMan.score = score;
      uiMan.gameMan = new GameManager(1);
    }
    for (int i=0; i < upgrades.size(); i++)
    {
      upgrades.get(i).display();
    }
  }

  void resetGame() {
    ship.position = new PVector(width/2, height/2);
    level ++;
    for (int i=0; i<level*2; i++)
    {
      float temp = random(10);
      if (temp < 1)
      {
        enemies.add(new EnemyShooter());
      } else
      {
        enemies.add(new Enemy());
      }
    }
    if (level%5 == 0)
    {
      lives ++;
    }
    ship.invincibleCount = 0;
    ship.ss = ShipState.invincible;
  }
}