/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messages;

import client.ClientEntity;
import client.GameAppState;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import server.ServerEntity;

/**
 *
 * @author Rory
 */
@Serializable
public class EntityUpdateMessage extends AbstractMessage {

    private int id;
    private Vector3f position;
    private Quaternion rotation;

    public EntityUpdateMessage() {
    }

    public EntityUpdateMessage(ServerEntity se) {
        id = se.getID();
        position = se.getPosition();
        rotation = se.getRotation();
        setReliable(false);
    }

    public void updateEntity(GameAppState gameAppState) {
        ClientEntity ce = gameAppState.getEntityMap().get(id);
        if (ce == null) {
            System.out.println("ClientEntity for EntityUpdateMessage does not exist, id: " + id);
            return;
        }
        ce.setPosition(position);
        ce.setRotation(rotation);
    }
}
