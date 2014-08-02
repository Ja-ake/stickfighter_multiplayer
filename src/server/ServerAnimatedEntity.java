/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.math.Vector3f;
import com.jme3.network.Message;
import messages.AnimatedEntityAddMessage;
import messages.AnimationUpdateMessage;

/**
 *
 * @author Rory
 */
public abstract class ServerAnimatedEntity extends ServerEntity implements AnimEventListener {

    protected AnimControl animControl;
    protected AnimChannel animChannel;
    protected boolean sendAnimUpdate;

    public ServerAnimatedEntity(ServerMain serverMain, Vector3f position) {
        super(serverMain, position);
        sendAnimUpdate = true;
    }

    @Override
    protected Message createAddMessage() {
        return new AnimatedEntityAddMessage(this);
    }

    public AnimChannel getAnimChannel() {
        return animChannel;
    }

    @Override
    protected void init() {
        animControl = spatial.getControl(AnimControl.class);
        animControl.addListener(this);
        animChannel = animControl.createChannel();
        animChannel.setAnim(initialAnimation());
        animChannel.setLoopMode(LoopMode.Loop);

        super.init();
    }

    protected abstract String initialAnimation();

    @Override
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        sendAnimUpdate = true;
    }

    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        if (sendAnimUpdate) {
            serverMain.getServer().broadcast(new AnimationUpdateMessage(this));
            sendAnimUpdate = false;
        }
    }
}
