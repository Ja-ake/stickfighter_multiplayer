/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import util.ModelCreator;

/**
 *
 * @author Rory
 */
public class ClientEntity {

    protected int id;
    protected GameAppState gameAppState;
    protected Node spatial;

    public ClientEntity(GameAppState gameAppState, int id, int modelID) {
        this.gameAppState = gameAppState;
        this.id = id;
        spatial = ModelCreator.getModel(modelID, gameAppState.getClientMain());
        
        gameAppState.add(this);
        gameAppState.getNode().attachChild(spatial);
    }
    
    public int getID() {
        return id;
    }
    
    public Vector3f getPosition() {
        return spatial.getLocalTranslation();
    }

    public void remove() {
        gameAppState.getNode().detachChild(spatial);
    }

    public void setPosition(Vector3f newPosition) {
        spatial.setLocalTranslation(newPosition);
    }

    public void setRotation(Quaternion newRotation) {
        spatial.setLocalRotation(newRotation);
    }
}
