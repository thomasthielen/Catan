package entities;

public class Border {
  private String key;
  private Tile[] tiles = new Tile[2];
  private Player owner;
  
  public Border (Tile t1, Tile t2) {
    key = t1.getKey() + "_" + t2.getKey();
    tiles[0] = t1;
    tiles[1] = t2;
    owner = null;
  }
  
  public void setOwner(Player p) {
    owner = p;
  }
  
  public String getKey() {
    return this.key;
  }
  
  public Player getOwner() {
    return this.owner;
  }
}
