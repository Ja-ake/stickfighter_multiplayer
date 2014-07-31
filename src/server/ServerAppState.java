/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.network.Filters;
import com.jme3.network.HostedConnection;
import java.util.ArrayList;
import messages.EntityAddMessage;

/**
 *
 * @author Rory
 */
public class ServerAppState extends BulletAppState {

    private ArrayList<ServerEntity> entityList;
    private int maxID = 1000;

    public ServerAppState() {
        entityList = new ArrayList();
    }

    public void add(ServerEntity se) {
        entityList.add(se);
    }

    public int allocateID() {
        maxID++;
        return maxID;
    }
    
    public void broadcastAll(HostedConnection conn) {
        for (ServerEntity se : entityList) {
            getServerMain().getServer().broadcast(Filters.equalTo(conn), new EntityAddMessage(se));
        }
    }

    public ServerMain getServerMain() {
        return (ServerMain) app;
    }

    @Override
    public void prePhysicsTick(PhysicsSpace space, float tpf) {
        super.prePhysicsTick(space, tpf);
        for (ServerEntity se : entityList) {
            se.update(tpf);
        }
    }

    public void remove(ServerEntity se) {
        entityList.remove(se);
    }
}
