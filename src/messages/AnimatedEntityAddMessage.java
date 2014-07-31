/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messages;

import client.ClientAnimatedEntity;
import client.GameAppState;
import com.jme3.animation.LoopMode;
import com.jme3.network.serializing.Serializable;
import server.ServerAnimatedEntity;

/**
 *
 * @author Rory
 */
@Serializable
public class AnimatedEntityAddMessage extends EntityAddMessage {

    private String animation;
    private float animationTime;
    private LoopMode repeat;

    public AnimatedEntityAddMessage() {
    }

    public AnimatedEntityAddMessage(ServerAnimatedEntity sae) {
        super(sae);
        animation = sae.getAnimChannel().getAnimationName();
        animationTime = sae.getAnimChannel().getTime();
        repeat = sae.getAnimChannel().getLoopMode();
    }

    @Override
    public ClientAnimatedEntity createEntity(GameAppState gameAppState) {
        ClientAnimatedEntity cae = new ClientAnimatedEntity(gameAppState, id, modelID);
        cae.setPosition(position);
        cae.setRotation(rotation);
        cae.setAnimation(animation, animationTime, repeat);
        return cae;
    }
}
