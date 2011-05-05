/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world;

import org.lwjgl.util.Point;

/**
 *
 * @author Administrator
 */
//this shit works as a mediator between WorldView and a WorldModel
public class WorldCluster {
    public static Point origin = new Point(0,0);
    public static int CLUSTER_SIZE = 3;


    public static void locate(Point target){

        System.out.println("changing location of world cluster:");
        System.out.println(target);

        origin.setLocation(
                target.getX()-
                    ((CLUSTER_SIZE-1)/2),
                target.getY()-
                    ((CLUSTER_SIZE-1)/2)
        );
        System.out.println(origin);
    }

    public static void moveto(Point origin){
        origin.setLocation(origin);
    }

    public static boolean chunk_in_cluster(Point point){
        int x = point.getX();
        int y = point.getY();
        int cx = origin.getX();
        int cy = origin.getY();

        return ( x>=cx && x <=cx+CLUSTER_SIZE-1 ) && ( y>=cy && y <= cy + CLUSTER_SIZE-1 );

    }
}
