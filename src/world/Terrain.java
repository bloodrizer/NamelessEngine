/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world;

import world.util.Noise;

/**
 *
 * @author Administrator
 */
public class Terrain {
    public static Noise noise = new Noise();

    public static final int TERRAIN_HEIGHT = 255;    //255

    public static void setup(){
        noise.noiseDetail(3,0.5f);
    }

    //function generates virtual terrain height, based on PerlinNoise Map
    public static int get_height(int x, int y){
        //todo: call setup();
        noise.noiseDetail(3,0.5f);

        float noiseScale = 0.03f;
        float noiseVal = noise.noise((float)x*noiseScale,(float)y*noiseScale);

        //System.out.println(noiseVal);

        return (int)(noiseVal*TERRAIN_HEIGHT);
    }

    public static int FORREST_HEIGHT = 120;
    public static int LAKE_HEIGHT = 120;
    public static int TREE_RATE = 10;

    public static boolean is_forrest(WorldTile tile){
        if (tile.get_height() > FORREST_HEIGHT){
            return true;
        }
        return false;
    }

    public static boolean is_tree(WorldTile tile){

        if (!is_forrest(tile)){
            return false;
        }

        int chance = (int)Math.round(
                Math.random()* tile.get_height()
                );
        if (chance < TREE_RATE){
            return true;
        }

        return false;
    }

    public static boolean is_lake(WorldTile tile){
        if (tile.get_height() < 60){
            return true;
        }
        return false;
    }


    /*void generateHeightmap(){
        float noiseVal;
        float noiseScale = 0.03f;
        for(int y = 0; y < mheight; y++){
          for(int x = 0; x < mwidth; x++){
           // noiseDetail(3,0.5);
            noiseVal = noise(x*noiseScale,y*noiseScale); //this generates the int value to put into the 2d array for height, all generated with perlin noise
            this.map[x][y].theight = noiseVal*255;
            //println(this.map[x][y].theight);
          }
        }
    }*/
}








/*import java.util.Vector.*;
Map world;

void setup(){
  size(500,500); //Create screen size 500 pixles by 500 pixles
  noiseDetail(3,0.5);//set the perlin noise detail
  world = new Map(100,100);//create a map object
}

void draw(){
  background(0);
  world.display();
}

void mousePressed() {
  //world.highest = world.map[mouseX/5][mouseY/5];
  world.generateRiver(mouseX/5,mouseY/5); //generates a new river where you click

  //noiseDetail(round(random(99)),0.5);
  //world = new Map(100,100);
}

class Map{
  int mwidth;
  int mheight;

  Tile map[][];
  Map(int w, int h){
    this.mwidth = w;
    this.mheight = h;
    map = new Tile [w][h]; //take the two int inputs and generate a 2d array to fill with tile objects

    initializeMap();
    generateHeightmap();

    generateLakes();
    generateMountains();
    addRivers(20);
    generateForest();
  }
  void initializeMap(){
    for(int x = 0; x < this.mwidth; x++){
      for(int y = 0; y < this.mheight; y++){
        map[x][y] = new Tile(x,y,0);         //sets all of the map initially to the same height

      }
    }
  }
  void generateHeightmap(){
    float noiseVal;
    float noiseScale = 0.03;
    for(int y = 0; y < mheight; y++){
      for(int x = 0; x < mwidth; x++){
       // noiseDetail(3,0.5);
        noiseVal = noise(x*noiseScale,y*noiseScale); //this generates the int value to put into the 2d array for height, all generated with perlin noise
        this.map[x][y].theight = noiseVal*255;
        //println(this.map[x][y].theight);
      }
    }
  }

  void generateLakes(){
    for(int y = 0; y < mheight; y++){
      for(int x = 0; x < mwidth; x++){
        if(this.map[x][y].theight < 100){
          this.map[x][y].type = 1;
        }
        else if(this.map[x][y].theight <110){
          this.map[x][y].type = 4;
        }
      }
    }
  }
  void addRivers(int num){
    for(int i = 0; i < num; i++){
      int x = round(random(1,99));
      int y = round(random(1,99));
      generateRiver(x,y);
    }
  }
  void generateMountains(){
    for(int y = 0; y < mheight; y++){
      for(int x = 0; x < mwidth; x++){
        if(this.map[x][y].theight > 150){
          this.map[x][y].type = 2;
        }
        if(this.map[x][y].theight > 170){
          this.map[x][y].type = 3;
        }
      }
    }
  }
  void generateForest(){
    for(int y = 0; y < mheight; y++){
      for(int x = 0; x < mwidth; x++){
        int chance = round(random(0,map[x][y].theight));
        if(chance < 15&&map[x][y].type!=1&&map[x][y].type!=2&&map[x][y].type!=3&&map[x][y].type!=4){
          map[x][y].type = 5;
        }
      }
    }


  }
  void generateRiver(int x, int y){ //complicated and probably poor way to generate rivers, but it makes good rivers that follow the height.
    Vector visited = new Vector();
    Tile lowest = map[x][y];
    float up;
    float down;
    float right;
    float left;
    for(int i = 0; i < 100; i++){
      lowest.type = 1;
      int move = round(random(0,100));
      if(lowest.x != 0 && lowest.x != 99 && lowest.y !=0 && lowest.y != 99){
        up = map[lowest.x][lowest.y-1].theight;
        down = map[lowest.x][lowest.y+1].theight;
        left = map[lowest.x-1][lowest.y].theight;
        right = map[lowest.x+1][lowest.y].theight;
        if(move > 80){
          int rand = round(random(3));
          switch(rand){
          case 0:
            lowest = map[lowest.x+1][lowest.y];
            break;
          case 1:
            lowest = map[lowest.x-1][lowest.y];
            break;
          case 2:
            lowest = map[lowest.x][lowest.y+1];
            break;
          case 3:
            lowest = map[lowest.x][lowest.y-1];
            break;
          }
        }
        else if(up < down && up < left && up<right){
          lowest = map[lowest.x][lowest.y-1];
        }
        else if(down < up&& down < left && down<right){
          lowest = map[lowest.x][lowest.y+1];
        }
        else if(left < down && left < up && left<right){
          lowest = map[lowest.x-1][lowest.y];
        }
        else if(right < down && right <left && right<up){
          lowest = map[lowest.x+1][lowest.y];
        }

        if(!visited.contains((Tile)lowest)){
          lowest.type = 1;
          visited.add(lowest);
        }
        else{
          int rand = round(random(3));
          switch(rand){
          case 0:
            lowest = map[lowest.x+1][lowest.y];
            break;
          case 1:
            lowest = map[lowest.x-1][lowest.y];
            break;
          case 2:
            lowest = map[lowest.x][lowest.y+1];
            break;
          case 3:
            lowest = map[lowest.x][lowest.y-1];
            break;
          }
          visited.add(lowest);
          lowest.type = 1;
        }
      }
    }
  }

  void display(){
    for(int x = 0; x < this.mwidth; x++){
      for(int y = 0; y < this.mheight; y++){
        map[x][y].display();
      }
    }
  }

}


class Tile{
  float theight;
  int type;
  int x;
  int y;
  Tile(int tx, int ty, int t){
    this.x = tx;//xlocation on the screen of the tile
    this.y = ty;//ylocation on the screen of the tile
    this.type = t;//what type the tile is, water/sand/grass etc.
  }
  void determineType(int t){
  }
  void display(){
    noStroke();
    switch(this.type){ //based on the type, draw a different color
    case 0:
      fill(0,255,0,theight);
      rect(x*5,y*5,5,5);
      break;
    case 1:
      fill(0,0,255,theight);
      rect(x*5,y*5,5,5);
      break;
    case 2:
      fill(100,100,100,theight);
      rect(x*5,y*5,5,5);
      break;
    case 3:
      fill(220,220,220,theight);
      rect(x*5,y*5,5,5);
      break;
    case 4:
      fill(237,255,49,theight);
      rect(x*5,y*5,5,5);
      break;
    case 5:
      fill(19,64,7);
      rect(x*5,y*5,5,5);
      break;
    }
  }

}
*/