package entities;

public class Intersection {
  private String key;
  private Tile[] tiles = new Tile[3];
  private Player owner;
  private boolean isCity;

  public Intersection(Tile t1, Tile t2, Tile t3) {
    key = t1.getKey() + "_" + t2.getKey() + "_" + t3.getKey();
    tiles[0] = t1;
    tiles[1] = t2;
    tiles[2] = t3;
    owner = null;
    isCity = false;
  }
  
  public void upgrade() {
    isCity = true;
  }

  public String getKey() {
    return key;
  }
  
  public Player getOwner() {
    return owner;
  }
  
  public boolean isCity() {
    return isCity;
  }
  
  public void setOwner(Player p) {
    owner = p;
  }
  
}
