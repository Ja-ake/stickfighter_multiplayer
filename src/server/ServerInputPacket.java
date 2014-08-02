/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.jme3.math.Vector3f;
import java.util.ArrayList;
import messages.InputMessage;

/**
 *
 * @author Rory
 */
public class ServerInputPacket {

    private final ArrayList<String> down = new ArrayList();
    private final ArrayList<String> pressed = new ArrayList();
    private final ArrayList<String> released = new ArrayList();
    private Vector3f facing = Vector3f.UNIT_X;

    public void add(InputMessage im) {
        pressed.addAll(im.getPressed());
        released.addAll(im.getReleased());
        
        down.addAll(im.getPressed());
        down.removeAll(im.getReleased());
        
        facing = im.getFacing();
    }
    
    public void clear() {
        pressed.clear();
        released.clear();
    }

    public Vector3f getFacing() {
        return facing;
    }

    public boolean isDown(String name) {
        return down.contains(name);
    }

    public boolean isPressed(String name) {
        return pressed.contains(name);
    }

    public boolean isReleased(String name) {
        return released.contains(name);
    }
}
