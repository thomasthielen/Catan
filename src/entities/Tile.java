package entities;

public class Tile {
  private String key;
  private Type type;
  private int number;
  
  public Tile(String s) {
    key = s;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    
    if (!(o instanceof Tile)) {
      return false;
    }
    
    Tile t = (Tile) o;
    return this.key.equals(t.getKey());
  }
  
  public boolean equals(String s) {
    return this.key.equals(s);
  }

  public void setType(Type type) {
    this.type = type;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public String getKey() {
    return key;
  }
  
  public Type getType() {
    return type;
  }
  
  public int getNumber() {
    return number;
  }
}
