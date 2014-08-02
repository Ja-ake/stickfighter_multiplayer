/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.network.Filters;
import com.jme3.network.HostedConnection;
import java.util.ArrayList;

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
            getServerMain().getServer().broadcast(Filters.equalTo(conn), se.createAddMessage());
        }
    }

    public ServerMain getServerMain() {
        return (ServerMain) app;
    }

    @Override
    public void physicsTick(PhysicsSpace space, float f) {
        super.physicsTick(space, f);
        tick(f * 1000);
    }

    public void remove(ServerEntity se) {
        entityList.remove(se);
    }

    public void tick(float tpf) {
        getServerMain().getServerListener().processMessages();
        for (ServerEntity se : entityList) {
            se.update(tpf);
        }
    }
}
