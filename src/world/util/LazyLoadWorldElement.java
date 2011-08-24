/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package world.util;

import org.lwjgl.util.Point;

/**
 *
 * @author bloodrizer
 */
public abstract class LazyLoadWorldElement<ElementType> extends java.util.HashMap<Point, ElementType>{
    private static Point util_point     = new Point(0,0);

    
    public ElementType get_cached(int x, int y){
        util_point.setLocation(x, y);
        return get_cached(util_point);
    }
    
    public ElementType get_cached(Point origin){
        ElementType element =  get(origin);
        if (element == null){
            
            element = precache(new Point(origin));
            put(origin, element);
            return element;
        }
        
        return element;
    }
    
    public abstract ElementType precache(Point origin);
}
