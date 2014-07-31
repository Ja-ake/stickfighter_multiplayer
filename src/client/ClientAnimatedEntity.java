/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;

/**
 *
 * @author Rory
 */
public class ClientAnimatedEntity extends ClientEntity implements AnimEventListener {

    private AnimControl animControl;
    private AnimChannel animChannel;

    public ClientAnimatedEntity(GameAppState gameAppState, int id, int modelID) {
        super(gameAppState, id, modelID);

        animControl = spatial.getControl(AnimControl.class);
        animControl.addListener(this);
        animChannel = animControl.createChannel();
    }

    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        
    }

    @Override
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        
    }
    
    public void setAnimation(String name, float time, LoopMode repeat) {
        animChannel.setAnim(name);
        animChannel.setTime(time);
        animChannel.setLoopMode(repeat);
    }
}
