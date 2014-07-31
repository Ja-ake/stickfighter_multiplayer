/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import server.ServerEntity;

/**
 *
 * @author Rory
 */
@Serializable
public class EntityRemoveMessage extends AbstractMessage {

    private int id;

    public EntityRemoveMessage() {
    }

    public EntityRemoveMessage(ServerEntity se) {
        id = se.getID();
    }

    public int getId() {
        return id;
    }
}
