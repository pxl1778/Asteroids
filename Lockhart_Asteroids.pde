import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.effects.*;
import ddf.minim.signals.*;
import ddf.minim.spi.*;
import ddf.minim.ugens.*;

enum GameState {
  titleScreen, game, gameOver, pause, controls;
}

enum ShipState {
  active, invincible;
}

Minim minim;
AudioPlayer player;
InputManager inputMan;//Handles all input
UIManager uiMan;//Handles displaying the game screens
GameState gs;//allows for different screens/levels
int mouseCount;
ArrayList<AudioPlayer> bigSounds, littleSounds;
Boolean classic;

void setup() {
  size(800, 600);
  classic = false;
  minim = new Minim(this);
  bigSounds = new ArrayList<AudioPlayer>();
  littleSounds = new ArrayList<AudioPlayer>();
  bigSounds.add(minim.loadFile("Soundfiles/BigAsteroids/Big Asteroid1.mp3"));
  for (int i= 1; i < 8; i++)
  {
    bigSounds.add(minim.loadFile("SoundFiles/BigAsteroids/Big Asteroid"+i+".mp3"));
  }
  for (int i = 1; i<10; i++)
  {
    littleSounds.add(minim.loadFile("SoundFiles/LittleAsteroids/Little asteroids"+i+".mp3"));
  }
  player = minim.loadFile("AsteroidsBackground1.wav");
  inputMan = new InputManager();
  uiMan = new UIManager();
  gs = GameState.titleScreen;
  mouseCount = 0;
  player.play();
  player.loop();
}

void draw() {
  uiMan.update();
}

void keyPressed() {
  inputMan.recordKeyPress(key);
}

void keyReleased() {

  inputMan.recordKeyRelease(key);
}

void mousePressed() {
  mouseCount++;
  if (mouseCount == 1)
  {
    if (gs == GameState.titleScreen)
    {
      uiMan.ts.start.clicked();
      uiMan.ts.controls.clicked();
    }
    if (gs == GameState.controls)
    {
      uiMan.cs.normalButton.clicked();
      uiMan.cs.classicButton.clicked();
    }
    if (gs == GameState.game && !classic)
    {//fires a bullet when the mouse is clicked.
      uiMan.gameMan.ship.fire();
    }
    if (gs == GameState.gameOver)
    {
      uiMan.gos.menuButton.clicked();
    }
  }
}

void mouseReleased()
{
  mouseCount = 0;
}
