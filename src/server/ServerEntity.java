/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.Message;
import com.jme3.scene.Node;
import messages.EntityAddMessage;
import messages.EntityRemoveMessage;
import messages.EntityUpdateMessage;
import util.ModelCreator;

/**
 *
 * @author Rory
 */
public abstract class ServerEntity {

    protected int id;
    protected ServerMain serverMain;
    protected Node spatial;
    protected RigidBodyControl physicsControl;

    public ServerEntity(ServerMain serverMain, Vector3f position) {
        //Variables
        this.serverMain = serverMain;
        id = initialID();

        //Create the spatial
        spatial = ModelCreator.getModel(getModelID(), serverMain);
        spatial.move(position);

        //Create the physicsControl
        physicsControl = initialPhysicsControl();
        spatial.addControl(physicsControl);
        physicsControl.setSpatial(spatial);
        
        init();
    }

    protected Message createAddMessage() {
        return new EntityAddMessage(this);
    }

    public int getID() {
        return id;
    }

    public abstract int getModelID();

    public Node getSpatial() {
        return spatial;
    }

    public Vector3f getPosition() {
        return physicsControl.getPhysicsLocation();
    }

    public Quaternion getRotation() {
        return spatial.getLocalRotation();
    }

    public Vector3f getVelocity() {
        return physicsControl.getLinearVelocity();
    }

    protected void init() {
        //Add to room
        serverMain.getRootNode().attachChild(spatial);
        serverMain.getServerAppState().add(this);
        serverMain.getServerAppState().getPhysicsSpace().add(physicsControl);

        //Send message to client
        serverMain.getServer().broadcast(createAddMessage());
    }

    protected int initialID() {
        return serverMain.getServerAppState().allocateID();
    }

    protected abstract RigidBodyControl initialPhysicsControl();

    public void remove() {
        serverMain.getServerAppState().remove(this);
        serverMain.getRootNode().detachChild(spatial);
        serverMain.getServerAppState().getPhysicsSpace().remove(physicsControl);
        serverMain.getServer().broadcast(new EntityRemoveMessage(this));
    }

    public void setPosition(Vector3f newPosition) {
        physicsControl.setPhysicsLocation(newPosition);
    }

    public void setRotation(Quaternion newRotation) {
        spatial.setLocalRotation(newRotation);
    }

    public void setVelocity(Vector3f newVelocity) {
        physicsControl.setLinearVelocity(newVelocity);
    }

    public void update(float tpf) {
        if (physicsControl.isActive()) {
            serverMain.getServer().broadcast(new EntityUpdateMessage(this));
        }
    }
}
