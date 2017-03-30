class EnemyBullet extends Bullet {

  EnemyShooter es;
  EnemyBullet(PVector v, PVector p, EnemyShooter enemShoot) {
    super(v, p);
    c = color(255, 0, 0);
    es = enemShoot;
  }

  void collision() {
    if(bc.radius + uiMan.gameMan.ship.bc.radius > dist(bc.x, bc.y, uiMan.gameMan.ship.bc.x, uiMan.gameMan.ship.bc.y))
    {
      uiMan.gameMan.ship.hit();
      es.bullets.remove(this);
    }
  }
}