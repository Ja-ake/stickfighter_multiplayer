/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.jme3.math.Vector3f;
import com.jme3.network.ConnectionListener;
import com.jme3.network.Filters;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Server;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import messages.InputMessage;
import messages.ServerJoinMessage;

/**
 *
 * @author Rory
 */
public class ServerListener implements MessageListener<HostedConnection>, ConnectionListener {

    private ServerMain serverMain;
    private Queue<Runnable> messageQueue;

    public ServerListener(ServerMain serverMain) {
        this.serverMain = serverMain;
        messageQueue = new ConcurrentLinkedQueue();
    }

    @Override
    public void messageReceived(final HostedConnection conn, final Message m) {
        //System.out.println(m);
        messageQueue.add(new Runnable() {
            @Override
            public void run() {
                if (m instanceof InputMessage) {
                    ((ServerInputPacket) conn.getAttribute("Input")).add((InputMessage) m);
                }
            }
        });
    }

    @Override
    public void connectionAdded(Server server, final HostedConnection conn) {
        //conn.setAttribute("Input", new ServerInputPacket());
        messageQueue.add(new Runnable() {
            @Override
            public void run() {
                System.out.println("Player " + conn.getId() + " has joined the server.");
                serverMain.getServerAppState().broadcastAll(conn);
                ServerPlayer sp = new ServerPlayer(serverMain, new Vector3f(0, 10, 0), conn);
                serverMain.getServer().broadcast(Filters.equalTo(conn), new ServerJoinMessage(serverMain.getLevelID(), sp.getID()));
            }
        });
    }

    @Override
    public void connectionRemoved(Server server, final HostedConnection conn) {
        messageQueue.add(new Runnable() {
            @Override
            public void run() {
                System.out.println("Player " + conn.getId() + " has left the server.");
                ((ServerPlayer) conn.getAttribute("ServerPlayer")).remove();
            }
        });

    }

    public void processMessages() {
        for (HostedConnection conn : serverMain.getServer().getConnections()) {
            if (!conn.attributeNames().contains("Input")) {
                conn.setAttribute("Input", new ServerInputPacket());
            }
            ((ServerInputPacket) conn.getAttribute("Input")).clear();
        }
        while (!messageQueue.isEmpty()) {
            messageQueue.poll().run();
        }
    }
}
