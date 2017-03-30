class InputManager{
  boolean[] keys;
  boolean[] prevKeys;
  
  InputManager(){
    keys = new boolean[256];
    prevKeys = new boolean[256];
  }
  
  void recordKeyPress(int k){
    keys[k] = true;
    prevKeys[k] = false;
  }
  
  void recordKeyRelease(int k) {
    keys[k] = false;
    prevKeys[k] = true;
  }
  
  boolean isKeyPressed(int k){
    return keys[k];
  }
  
  boolean isKeyReleased(int k){
    return !keys[k];
  }
  
}