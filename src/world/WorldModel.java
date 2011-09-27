/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world;

import events.*;
import events.EventManager;
import events.IEventListener;
import events.network.*;
import game.GameEnvironment;
import game.combat.Damage.DamageType;
import game.ent.Entity;
import game.ent.decals.EntDecalBlood;
import org.lwjgl.util.Point;
import world.layers.UndergroundLayer;
import world.layers.WorldLayer;
import world.util.LazyLoadWorldElement;

/**
 *
 * @author Administrator
 */
public class WorldModel implements IEventListener {

    public static final int LAYER_COUNT = 10;    //max depth of geometry layers

    protected final java.util.HashMap<Integer, WorldLayer> worldLayers
            = new java.util.HashMap<Integer, WorldLayer>(LAYER_COUNT);

    /**
    * Load region data from server including village assigment, ownership and so on
    *
    * origin - location of region block, e.g. [-1,1]
    */
    public static LazyLoadWorldElement<WorldRegion> worldRegions =
    new LazyLoadWorldElement<WorldRegion>(){
        @Override
        protected WorldRegion precache(Point origin) {

                WorldRegion region = new WorldRegion();
                region.origin.setLocation(origin);

                //TODO: load region from some external storage
                //(i.e. use server API)
                region.load_data();
                return region;
        }
    };




    public WorldModel(){
        /*
         * Create placeholder for every possile layers, starting 0 as a ground layer,
         * and ending LAYER_COUNT as a deapest underground layer
         *
         */
        for (int i = 0; i< LAYER_COUNT; i++ ){
            WorldLayer layer = null;
            if (i == WorldLayer.GROUND_LAYER){
                layer = new WorldLayer();
            }else{
                layer = new UndergroundLayer();
            }
            layer.set_zindex(i);
            worldLayers.put(i, layer);
        }
    }

    public WorldLayer getWorldLayer(int layer_id){
        return worldLayers.get(layer_id);
    }




    //----------------------------EVENTS SHIT-----------------------------------
    public void e_on_event(Event event){
       if (event instanceof EEntityMove){
           EEntityMove move_event = (EEntityMove)event;

           //TODO: fix me
           getWorldLayer(move_event.entity.getLayerId()).
                   move_entity(move_event.entity, move_event.getTo());

           if (move_event.entity.isPlayerEnt()){
               WorldViewCamera.target.setLocation(move_event.entity.origin);
           }
       }
       else if(event instanceof EEntitySpawn){
           EEntitySpawn spawn_event = (EEntitySpawn)event;
           //-------------------------------------------------------------------
           WorldChunk new_chunk = getLayer().get_cached_chunk(WorldChunk.get_chunk_coord(spawn_event.ent.origin));

           //System.out.println("setting new chunk for a spawned entity");

           EEntityChangeChunk e_change_chunk = new EEntityChangeChunk(spawn_event.ent,null,new_chunk);
           e_change_chunk.setManager(environment.getEventManager());
           e_change_chunk.post();

           Point ent_origin = spawn_event.ent.origin;
           WorldTile spawn_tile = getLayer().get_tile(ent_origin.getX(), ent_origin.getY());

           /* Some ents are prohabited from spawning. Normaly, that should be checked at the server side
            *
            * For now we will check it at client side (totems for example)
            */
           Point region_coord = WorldRegion.get_region_coord(ent_origin);
           //TODO: get region instance and check if totem can be build there

           if (spawn_tile != null){
                spawn_tile.add_entity(spawn_event.ent);
           }else{

               //show some debug info to hint me next time when this fucking world model crashs again

               if (!environment.getEventManager().listeners_sync.contains(this)){
                   System.err.println("World model is not subscribed to the event manager");
               }

               WorldLayer layer = worldLayers.get(spawn_event.ent.getLayerId());
               System.err.println("Tile data size:"+layer.tile_data.size());
               System.err.println("Chunk data size:"+layer.chunk_data.size());
               //layer.g

               throw new RuntimeException("Failed to assign spawned entity to tile@"+ent_origin+"["+getLayer().get_zindex()+"] - tile is null!");

           }

           if (spawn_event.ent.light_amt > 0.0f){
                getLayer().invalidate_light();
           }


           //spawn_event.ent.origin = spawn_event.origin;
           //-------------------------------------------------------------------
       }else if(event instanceof EEntityChangeChunk){



           EEntityChangeChunk e_change_chunk = (EEntityChangeChunk)event;

           //System.err.println("setting new chunk @ for ent "+Entity.toString(e_change_chunk.ent));

           Entity ent = e_change_chunk.ent;
           //ent.set_chunk(e_change_chunk.to);
           e_change_chunk.to.add_entity(ent);

           if (ent.isPlayerEnt()){
                getLayer().update_terrain();

                WorldCluster.locate(e_change_chunk.to.origin);
                getLayer().chunk_gc();
           }

       }else if(event instanceof ETakeDamage){
           //drop blood if someone is taking damage
           ETakeDamage dmg_event = (ETakeDamage)event;
           if (dmg_event.dmg.type != DamageType.DMG_FIRE &&
               dmg_event.dmg.type != DamageType.DMG_MAGIC ){
               Point dmg_origin = new Point();
               dmg_origin.setLocation(dmg_event.ent.origin);

               WorldTile tile = getLayer().get_tile(dmg_origin.getX(), dmg_origin.getY());
               if (!tile.has_ent(EntDecalBlood.class)){
                   EntDecalBlood blood = new EntDecalBlood();
                   blood.setLayerId(dmg_event.ent.getLayerId());
                   blood.spawn(dmg_origin);

                   //TODO: set random dx, dy for more natural blood drops?
               }else{
                   EntDecalBlood blood = (EntDecalBlood)tile.getEntity(EntDecalBlood.class);
                   blood.nextTile();
               }
           }
       }


    }
    //--------------------------------------------------------------------------
    public void e_on_event_rollback(Event event){
       if (event instanceof EEntityMove){
           EEntityMove move_event = (EEntityMove)event;
           getLayer().move_entity(move_event.entity, move_event.getFrom());
       }
    }

    //TODO:
    //link layer to the entity layer
    private WorldLayer getLayer() {
        return worldLayers.get(WorldLayer.GROUND_LAYER);
    }

    //--------------------------------------------------------------------------

    public void update(){
        //1. update timer data
        //2. check if think call is allowed
        //3. call think

        WorldTimer.tick();


        for(WorldLayer layer: worldLayers.values()){
            layer.update();
        }



    }

    private GameEnvironment environment = null;

    public void setEnvironment(GameEnvironment environment) {
        this.environment = environment;
        environment.getEventManager().subscribe(this);
    }

}
