/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.io;

import events.network.*;
import game.build.BuildManager;
import game.ent.Entity;
import game.ent.EntityManager;
import game.ent.EntityNPC;
import game.ent.buildings.EntBuilding;
import game.ent.controller.NpcController;
import java.io.IOException;
import org.lwjgl.util.Point;
import player.Player;

/**
 *
 * @author Administrator
 */
public class GameServerLayer extends IoLayer {
     public GameServerLayer(String host, int port) throws IOException{
         super(host,port);
         String[] whitelist = {
            "0x0010",
            "0x260",
            "0x220",
            "0x02A0"
         };
         set_whitelist(whitelist);
     }

     @Override
     protected void parse_network_data(String[] data){


         if (data[0].equals("0x0100") && data.length == 3){      //spawn player
            System.out.println("spawning player");

            EPlayerLogon event = new EPlayerLogon(new Point(
                 Integer.parseInt( data[1] ),
                 Integer.parseInt( data[2] )
            ));
            event.post();
         }

         if (data[0].equals("0x02A1")){

            int uid = Integer.parseInt( data[1] );

            StringBuilder sb = new StringBuilder();
            for (int i = 2; i<data.length; i++){
                sb.append(data[i].concat(" "));
            }


            String msg = sb.toString();

            if (uid!=Player.character_id){
               EChatMessage chat_event = new EChatMessage(uid, msg);
               chat_event.dispatch();
               chat_event.post();
            }
         }

         if (data[0].equals("0x0200")){  //EntSpawn
             //4 123 0 3 9

            int spawn_type = Integer.parseInt( data[1] );

            switch (spawn_type){
                case 4: //player_ent
                    Entity mplayer_ent = new EntityNPC();
                    mplayer_ent.set_controller(new NpcController());


                    //EntityManager.add(mplayer_ent);       ?
                    mplayer_ent.spawn(Integer.parseInt( data[2] )    //uid
                                                , new Point(
                                              Integer.parseInt( data[4] ),
                                              Integer.parseInt( data[5] )
                                        ));
                    break;

                    case 0: //object

                                            Class building = BuildManager.get_building(data[2]);
                                            EntBuilding ent_building;
                                            try {
                                                ent_building = (EntBuilding) building.newInstance();
                                            } catch (Exception ex) {
                                                //Logger.getLogger(Io.class.getName()).log(Level.SEVERE, null, ex);
                                                ex.printStackTrace();
                                                return;
                                            }

                                            //EntityManager.add(ent_building);

                                            //ent_building.spawn(54321, tile_coord);
                                            ent_building.set_blocking(true);

                                            ent_building.spawn( 54321           //<<<<<< UID THERE!!!!!!!!!!
                                                    , new Point(
                                                  Integer.parseInt( data[3] ),
                                                  Integer.parseInt( data[4] )
                                            ));

                                    break;
                                }


                            }
                            if (data[0].equals("0x0201")){  //EntRemove
                                Entity ent = EntityManager.get_entity(
                                        Integer.parseInt( data[1] )
                                );
                                EntityManager.remove_entity(ent);

                            }
                            if (data[0].equals("0x0280")){  //ENTMove
                                Entity ent = EntityManager.get_entity(Integer.parseInt( data[1]));
                                if (ent==null){
                                    System.err.println("EntityMove::invalid id");
                                    return;
                                }

                                 if(data[2].equals("2")){   //assign path
                                     if (ent.controller != null){
                                            ((NpcController)ent.controller).set_destination(new Point(
                                                        Integer.parseInt( data[3] ),
                                                        Integer.parseInt( data[4] )
                                            ));
                                     }

                                 }else{   //assign position
                                        ent.move_to(new Point(
                                                Integer.parseInt( data[3] ),
                                                Integer.parseInt( data[4] )
                                        ));
                                 }
                            }
    }

}
