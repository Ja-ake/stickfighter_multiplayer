/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messages;

import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Rory
 */
@Serializable
public class InputMessage extends AbstractMessage {

    private ArrayList<String> pressed;
    private ArrayList<String> released;
    private Vector3f facing;

    public InputMessage() {
    }

    public InputMessage(ArrayList<String> pressed, ArrayList<String> released, Vector3f facing) {
        this.pressed = new ArrayList(pressed);
        this.released = new ArrayList(released);
        this.facing = facing;
        setReliable(false);
    }

    public Vector3f getFacing() {
        return facing;
    }

    public ArrayList<String> getPressed() {
        return pressed;
    }

    public ArrayList<String> getReleased() {
        return released;
    }
}
