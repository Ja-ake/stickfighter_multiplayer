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
public class EntityAddMessage extends AbstractMessage {

    private int id;
    private Vector3f position;
    private Quaternion rotation;
    private int modelID;

    public EntityAddMessage() {
    }

    public EntityAddMessage(ServerEntity se) {
        id = se.getID();
        position = se.getPosition();
        rotation = se.getRotation();
        modelID = se.getModelID();
    }

    public ClientEntity createEntity(GameAppState gameAppState) {
        ClientEntity ce = new ClientEntity(gameAppState, id, modelID);
        ce.setPosition(position);
        ce.setRotation(rotation);
        return ce;
    }
}
