/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.scene.Node;
import com.jme3.system.JmeContext;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.GraphicsManager;
import util.NetworkUtil;

/**
 *
 * @author Rory
 */
public class ServerMain extends SimpleApplication {

    private Server server;
    private ServerAppState serverAppState;
    private ServerListener serverListener;
    private int levelID;
    private static final boolean SERVER_DISPLAY = false;

    public static void main(String[] args) {
        Logger.getLogger("").setLevel(Level.SEVERE);
        NetworkUtil.serialize();
        ServerMain app = new ServerMain();
        if (SERVER_DISPLAY) {
            app.start();
        } else {
            app.start(JmeContext.Type.Headless);
        }
    }

    @Override
    public void destroy() {
        server.close();
        super.destroy();
    }

    public int getLevelID() {
        return levelID;
    }

    public Server getServer() {
        return server;
    }

    public ServerAppState getServerAppState() {
        return serverAppState;
    }

    private void loadRoom(int levelID) {
        this.levelID = levelID;
        Node levelModel = null;
        switch (levelID) {
            case 0:
                assetManager.registerLocator("town.zip", ZipLocator.class);
                levelModel = (Node) assetManager.loadModel("main.scene");
        }

        rootNode.attachChild(levelModel);
        CollisionShape levelShape = CollisionShapeFactory.createMeshShape(levelModel);
        RigidBodyControl phys = new RigidBodyControl(levelShape, 0);
        serverAppState.getPhysicsSpace().add(phys);
    }

    @Override
    public void simpleInitApp() {
        if (SERVER_DISPLAY) {
            flyCam.setMoveSpeed(100);
            GraphicsManager.createLighting(this, rootNode);
            GraphicsManager.createFilters(this);
        }
        serverAppState = new ServerAppState();
        stateManager.attach(serverAppState);

        loadRoom(0);

        try {
            server = Network.createServer(NetworkUtil.PORT);
        } catch (IOException ex) {
            Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, null, ex);
            stop();
        }
        server.start();
        serverListener = new ServerListener(this);
        server.addMessageListener(serverListener);
        server.addConnectionListener(serverListener);

        System.out.println("Server running.");
    }

    @Override
    public void simpleUpdate(float tpf) {
        serverListener.processMessages();
    }
}
