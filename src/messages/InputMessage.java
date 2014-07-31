/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messages;

import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Rory
 */
@Serializable
public class InputMessage extends AbstractMessage {

    private ArrayList<String> down;
    private ArrayList<String> pressed;
    private ArrayList<String> released;
    private HashMap<String, Float> downValues;
    private Vector3f facing;

    public InputMessage() {
    }

    public InputMessage(ArrayList<String> down, ArrayList<String> pressed, ArrayList<String> released, HashMap<String, Float> downValues, Vector3f facing) {
        this.down = new ArrayList(down);
        this.pressed = new ArrayList(pressed);
        this.released = new ArrayList(released);
        this.downValues = new HashMap(downValues);
        this.facing = facing;
        setReliable(false);
    }

    public ArrayList<String> getDown() {
        return down;
    }

    public ArrayList<String> getPressed() {
        return pressed;
    }

    public ArrayList<String> getReleased() {
        return released;
    }

    public HashMap<String, Float> getDownValues() {
        return downValues;
    }

    public Vector3f getFacing() {
        return facing;
    }
}
