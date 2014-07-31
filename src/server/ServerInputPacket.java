/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.HashMap;
import messages.InputMessage;

/**
 *
 * @author Rory
 */
public class ServerInputPacket {

    private final ArrayList<String> down = new ArrayList();
    private final ArrayList<String> pressed = new ArrayList();
    private final ArrayList<String> released = new ArrayList();
    private final HashMap<String, Float> downValues = new HashMap();
    private Vector3f facing = Vector3f.UNIT_X;

    public void add(InputMessage im) {
        down.addAll(im.getDown());
        pressed.addAll(im.getPressed());
        released.addAll(im.getReleased());
        for (String s : im.getDownValues().keySet()) {
            if (downValues.containsKey(s)) {
                downValues.put(s, downValues.get(s) + im.getDownValues().get(s));
            } else {
                downValues.put(s, im.getDownValues().get(s));
            }
        }
        facing = im.getFacing();
    }

    public void clear() {
        down.clear();
        pressed.clear();
        released.clear();
        downValues.clear();
    }

    public Vector3f getFacing() {
        return facing;
    }

    public float getValue(String name) {
        if (!downValues.containsKey(name)) {
            return 0;
        }
        return downValues.get(name);
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
