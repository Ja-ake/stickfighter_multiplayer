/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import com.jme3.app.SimpleApplication;
import com.jme3.network.Client;
import com.jme3.network.Network;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.NetworkUtil;

/**
 *
 * @author Rory 
 */
public class ClientMain extends SimpleApplication {

    private Client client;
    private ClientListener clientListener;

    public static void main(String[] args) {
        Logger.getLogger("").setLevel(Level.SEVERE);
        NetworkUtil.serialize();
        ClientMain app = new ClientMain();
        app.start();
    }

    public void connect(String ip) {
        try {
            client = Network.connectToServer(ip, NetworkUtil.PORT);
            System.out.println("Connected to server: " + ip);
        } catch (IOException ex) {
            Logger.getLogger(ClientMain.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Failed to connect to server: " + ip);
            return;
        }
        client.start();
        clientListener = new ClientListener(this);
        client.addMessageListener(clientListener);

        stateManager.getState(GameAppState.class).setEnabled(true);
        stateManager.getState(MenuAppState.class).setEnabled(false);
    }

    @Override
    public void destroy() {
        client.close();
        super.destroy();
    }

    public Client getClient() {
        return client;
    }

    @Override
    public void simpleInitApp() {
        //flyCam.setMoveSpeed(100);
        flyCam.setEnabled(false);

        stateManager.attach(new MenuAppState());
        stateManager.attach(new GameAppState(this));
        stateManager.getState(GameAppState.class).setEnabled(false);

        connect("localhost");
    }

    @Override
    public void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf);
        clientListener.processMessages();
    }
}
