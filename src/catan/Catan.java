package catan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import entities.*;
import exceptions.WrongOwnerException;

public class Catan {
  private static ArrayList<Tile> land_tiles = new ArrayList<Tile>();
  private static ArrayList<Tile> water_tiles = new ArrayList<Tile>();
  private static ArrayList<Tile> tiles = new ArrayList<Tile>();
  private static ArrayList<Border> borders = new ArrayList<Border>();
  private static ArrayList<Intersection> intersections = new ArrayList<Intersection>();

  public static void initialize_map_entities() {
    // generate water tiles
    for (int i = 1; i <= 18; i++) {
      Tile w = new Tile("W" + i);
      w.setType(Type.WATER);
      water_tiles.add(w);
    }
    // generate land tiles
    for (char c = 'A'; c <= 'S'; c++) {
      land_tiles.add(new Tile("" + c));
    }
    // combine all tiles accordingly
    List<Integer> positions_water = Arrays.asList(4, 8, 9, 14, 15, 21, 22, 27, 28, 32);
    int index_land = 0;
    int index_water = 0;
    for (int i = 0; i <= 36; i++) {
      if (i < 4 || positions_water.contains(i) || i > 32) {
        tiles.add(water_tiles.get(index_water++));
      } else {
        tiles.add(land_tiles.get(index_land++));
      }
    }
    // generate borders
    // a) vertical borders (i.e. A-B or W7-D)
    for (int i = 4; i <= 31; i++) {
      if (i != 3 && i != 8 && i != 14 && i != 21 && i != 27) {
        borders.add(new Border(tiles.get(i), tiles.get(i + 1)));
      }
    }
    // b) horizontal borders (i.e. A-D or W3-C)
    for (int i = 0; i <= 14; i++) {
      Tile origin = tiles.get(i);
      int steps = i < 4 ? 4 : i < 9 ? 5 : 6;
      for (int j = steps; j <= steps + 1; j++) {
        Tile target = tiles.get(i + j);
        if (!water_tiles.contains(target)) {
          borders.add(new Border(origin, target));
        }
      }
    }
    for (int i = 22; i <= 36; i++) {
      Tile origin = tiles.get(i);
      int steps = i < 28 ? 6 : i < 33 ? 5 : 4;
      for (int j = steps + 1; j >= steps; j--) {
        Tile target = tiles.get(i - j);
        if (!water_tiles.contains(target)) {
          borders.add(new Border(target, origin));
        }
      }
    }
    // generate intersections
    for (int i = 0; i <= 14; i++) {
      Tile origin = tiles.get(i);
      int steps = i < 4 ? 4 : i < 9 ? 5 : 6;
      Tile target = tiles.get(i + steps + 1);
      // intersection below
      Tile target_below = tiles.get(i + steps);
      intersections.add(new Intersection(origin, target_below, target));
      // intersection to the right
      if (i != 3 && i != 8 && i != 14) {
        Tile target_right = tiles.get(i + 1);
        intersections.add(new Intersection(origin, target_right, target));
      }
    }
    for (int i = 22; i <= 36; i++) {
      Tile origin = tiles.get(i);
      int steps = i < 28 ? 6 : i < 33 ? 5 : 4;
      Tile target = tiles.get(i - steps);
      // intersection above
      Tile target_above = tiles.get((i - steps - 1));
      intersections.add(new Intersection(target_above, target, origin));
      // intersection to the right
      if (i != 27 && i != 32 && i != 36) {
        Tile target_right = tiles.get(i + 1);
        intersections.add(new Intersection(target, origin, target_right));
      }
    }
  }

  public static void game_setup() {
    // roll the productions types
    Type[] type = { Type.HILL, Type.FOREST, Type.MOUNTAIN, Type.FIELDS, Type.PASTURE, Type.DESERT };
    int[] type_count = { 3, 4, 3, 4, 4, 1 };
    for (Tile t : land_tiles) {
      Random rand = new Random();
      int i;
      // keep rolling until an available type is reached
      do {
        i = rand.nextInt(6);
      } while (type_count[i] == 0);
      t.setType(type[i]);
      type_count[i]--;
    }
    // roll the numbers of all tiles except water / desert
    int[] number = { 2, 3, 4, 5, 6, 8, 9, 10, 11, 12 };
    int[] number_count = { 1, 2, 2, 2, 2, 2, 2, 2, 2, 1 };
    for (Tile t : land_tiles) {
      if (t.getType() != Type.DESERT) {
        Random rand = new Random();
        int i;
        // keep rolling until an available number is reached
        do {
          i = rand.nextInt(10);
        } while (number_count[i] == 0);
        t.setNumber(number[i]);
        number_count[i]--;
      }
    }
  }

  public static void place_road(Border b, Player p) {
    if (b.getOwner() == null) {
      b.setOwner(p);
    }
  }

  public static void place_settlement(Intersection i, Player p) {
    if (i.getOwner() == null) {
      i.setOwner(p);
    }
  }

  public static void upgrade_settlement(Intersection i, Player p) throws WrongOwnerException {
    if (i.getOwner() == p) {
      if (!i.isCity()) {
        i.upgrade();
      }
    } else {
      throw new WrongOwnerException();
    }
  }

  public static void print_map() {
    for (int i = 0; i <= 36; i++) {
      Tile t = tiles.get(i);
      System.out.print(t.getKey() + "-" + t.getType() + "-" + t.getNumber() + " ");
      if (i == 3 || i == 8 || i == 14 || i == 21 || i == 27 || i == 32 || i == 36) {
        System.out.println();
      }
    }
  }

  public static void main(String[] args) {
    initialize_map_entities();
    System.out.println("Tiles: (" + tiles.size() + ")");
    for (Tile t : tiles) {
      System.out.print(t.getKey() + " ");
    }
    System.out.println("\n\nBorders: (" + borders.size() + ")");
    for (Border b : borders) {
      System.out.print(b.getKey() + " ");
    }
    System.out.println("\n\nIntersections: (" + intersections.size() + ")");
    for (Intersection i : intersections) {
      System.out.print(i.getKey() + " ");
    }

    game_setup();
    System.out.println("\n\nMap:");
    print_map();
  }
}
