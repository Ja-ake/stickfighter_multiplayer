/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import java.util.ArrayList;
import java.util.HashMap;
import messages.InputMessage;
import util.MathEx;
import util.SphericalCoords;

/**
 *
 * @author Rory
 */
public class InputListener implements ActionListener, AnalogListener {

    private final ArrayList<String> down = new ArrayList();
    private final ArrayList<String> pressed = new ArrayList();
    private final ArrayList<String> released = new ArrayList();
    private final HashMap<String, Float> downValues = new HashMap();
    private ClientMain clientMain;

    public InputListener(ClientMain clientMain) {
        this.clientMain = clientMain;
    }

    public SphericalCoords calculateFacing(SphericalCoords facing) {
        if (downValues.containsKey("Look Up")) {
            facing = facing.addP(-downValues.get("Look Up"));
        }
        if (downValues.containsKey("Look Down")) {
            facing = facing.addP(downValues.get("Look Down"));
        }
        if (downValues.containsKey("Look Left")) {
            facing = facing.addT(-downValues.get("Look Left"));
        }
        if (downValues.containsKey("Look Right")) {
            facing = facing.addT(downValues.get("Look Right"));
        }

        downValues.remove("Look Up");
        downValues.remove("Look Down");
        downValues.remove("Look Left");
        downValues.remove("Look Right");
        return facing;
    }

    public void clear() {
        pressed.clear();
        released.clear();
        downValues.clear();
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (isPressed) {
            pressed.add(name);
            down.add(name);
        } else {
            released.add(name);
            down.remove(name);
        }
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        if (downValues.containsKey(name)) {
            downValues.put(name, value + downValues.get(name));
        } else {
            downValues.put(name, value);
        }
    }

    public void sendInputMessage(SphericalCoords facing) {
        clientMain.getClient().send(new InputMessage(pressed, released, MathEx.sphericalToRectangular(facing)));
        clear();
    }
}
