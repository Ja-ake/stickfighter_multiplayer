/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import java.util.HashMap;
import util.GraphicsManager;

/**
 *
 * @author Rory
 */
public class GameAppState extends AbstractAppState {

    private ClientMain clientMain;
    private Node node;
    private HashMap<Integer, ClientEntity> entityMap;
    private InputListener inputListener;
    private Camera3D camera;
    private int playerID;

    public GameAppState(ClientMain clientMain) {
        this.clientMain = clientMain;
        entityMap = new HashMap();
    }

    public void add(ClientEntity ce) {
        entityMap.put(ce.getID(), ce);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        clientMain.getRootNode().detachChild(node);
    }

    public Camera3D getCamera() {
        return camera;
    }

    public ClientMain getClientMain() {
        return clientMain;
    }

    public HashMap<Integer, ClientEntity> getEntityMap() {
        return entityMap;
    }

    public Node getNode() {
        return node;
    }

    public int getPlayerID() {
        return playerID;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        node = new Node();
        clientMain.getRootNode().attachChild(node);
        camera = new Camera3D(clientMain.getCamera());
        initKeys();
        GraphicsManager.createLighting(clientMain, node);
        GraphicsManager.createFilters(clientMain);
    }

    private void initKeys() {
        HashMap<String, Trigger> map = new HashMap();

        map.put("Move Forward", new KeyTrigger(KeyInput.KEY_W));
        map.put("Move Left", new KeyTrigger(KeyInput.KEY_A));
        map.put("Move Back", new KeyTrigger(KeyInput.KEY_S));
        map.put("Move Right", new KeyTrigger(KeyInput.KEY_D));
        map.put("Jump", new KeyTrigger(KeyInput.KEY_SPACE));

        map.put("Look Up", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        map.put("Look Down", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        map.put("Look Left", new MouseAxisTrigger(MouseInput.AXIS_X, true));
        map.put("Look Right", new MouseAxisTrigger(MouseInput.AXIS_X, false));

        for (String s : map.keySet()) {
            clientMain.getInputManager().addMapping(s, map.get(s));
        }

        inputListener = new InputListener(clientMain);
        clientMain.getInputManager().addListener(inputListener, map.keySet().toArray(new String[0]));
    }

    public void loadRoom(int levelID) {
        System.out.println("Loading level " + levelID);
        Node levelModel = null;
        switch (levelID) {
            case 0:
                clientMain.getAssetManager().registerLocator("town.zip", ZipLocator.class);
                levelModel = (Node) clientMain.getAssetManager().loadModel("main.scene");
        }
        node.attachChild(levelModel);
    }

    public void remove(ClientEntity ce) {
        entityMap.remove(ce.getID());
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    @Override
    public void render(RenderManager rm) {
        super.render(rm);
        if (entityMap.containsKey(playerID)) {
            camera.positionBehind(entityMap.get(playerID).getPosition(), inputListener.calculateFacing(camera.getFacing()), 10);
        }
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        clientMain.getInputManager().setCursorVisible(false);
        if (entityMap.containsKey(playerID)) {
            camera.positionBehind(entityMap.get(playerID).getPosition(), inputListener.calculateFacing(camera.getFacing()), 10);
        }
        inputListener.sendInputMessage(camera.getFacing());
    }
}
