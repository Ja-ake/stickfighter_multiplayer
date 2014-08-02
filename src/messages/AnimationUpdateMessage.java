/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messages;

import client.ClientAnimatedEntity;
import client.GameAppState;
import com.jme3.animation.LoopMode;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import server.ServerAnimatedEntity;

/**
 *
 * @author Rory
 */
@Serializable
public class AnimationUpdateMessage extends AbstractMessage {

    private int id;
    private String animation;
    private float animationTime;
    private LoopMode repeat;

    public AnimationUpdateMessage() {
    }

    public AnimationUpdateMessage(ServerAnimatedEntity sae) {
        id = sae.getID();
        animation = sae.getAnimChannel().getAnimationName();
        animationTime = sae.getAnimChannel().getTime();
        repeat = sae.getAnimChannel().getLoopMode();
    }

    public void updateEntity(GameAppState gameAppState) {
        if (gameAppState.getEntityMap().get(id) instanceof ClientAnimatedEntity) {
            ClientAnimatedEntity cae = (ClientAnimatedEntity) gameAppState.getEntityMap().get(id);
            if (cae == null) {
                System.out.println("ClientEntity for AnimationUpdateMessage does not exist, id: " + id);
                return;
            }
            cae.setAnimation(animation, animationTime, repeat);
        } else {
            System.out.println("ERROR: ClientEntity "+id+" is not instance of ClientAnimatedEntity: "+gameAppState.getEntityMap().keySet());
        }
    }
}
