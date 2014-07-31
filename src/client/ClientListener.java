/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import messages.*;

/**
 *
 * @author Rory
 */
public class ClientListener implements MessageListener<Client> {
    
    private ClientMain clientMain;
    private Queue<Message> messageQueue;
    
    public ClientListener(ClientMain clientMain) {
        this.clientMain = clientMain;
        messageQueue = new ConcurrentLinkedQueue();
    }
    
    @Override
    public void messageReceived(Client source, Message m) {
        //System.out.println(m);
        messageQueue.add(m);
    }
    
    public void processMessages() {
        GameAppState gameAppState = clientMain.getStateManager().getState(GameAppState.class);
        while (!messageQueue.isEmpty()) {
            Message m = messageQueue.poll();
            if (m instanceof EntityAddMessage) {
                ((EntityAddMessage) m).createEntity(gameAppState);
            } else if (m instanceof EntityRemoveMessage) {
                gameAppState.getEntityMap().get(((EntityRemoveMessage) m).getId()).remove();
            } else if (m instanceof EntityUpdateMessage) {
                ((EntityUpdateMessage) m).updateEntity(gameAppState);
            } else if (m instanceof AnimationUpdateMessage) {
                ((AnimationUpdateMessage) m).updateEntity(gameAppState);
            } else if (m instanceof ServerJoinMessage) {
                gameAppState.loadRoom(((ServerJoinMessage) m).getLevelID());
                gameAppState.setPlayerID(((ServerJoinMessage) m).getPlayerID());
            }
        }
    }
}
