/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author Rory
 */
@Serializable
public class ServerJoinMessage extends AbstractMessage {

    private int levelID;
    private int playerID;

    public ServerJoinMessage() {
    }

    public ServerJoinMessage(int levelID, int playerID) {
        this.levelID = levelID;
        this.playerID = playerID;
    }

    public int getLevelID() {
        return levelID;
    }

    public int getPlayerID() {
        return playerID;
    }
}
