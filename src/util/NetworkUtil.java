/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.jme3.network.serializing.Serializer;
import messages.*;

/**
 *
 * @author Rory
 */
public abstract class NetworkUtil {

    public static final int PORT = 1337;

    public static void serialize() {
        Serializer.registerClass(EntityAddMessage.class);
        Serializer.registerClass(EntityRemoveMessage.class);
        Serializer.registerClass(EntityUpdateMessage.class);
        Serializer.registerClass(InputMessage.class);
        Serializer.registerClass(ServerJoinMessage.class);
    }
}
