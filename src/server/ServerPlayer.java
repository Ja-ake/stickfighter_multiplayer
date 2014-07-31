/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.HostedConnection;
import util.MathEx;
import util.SphericalCoords;

/**
 *
 * @author Rory
 */
public class ServerPlayer extends ServerAnimatedEntity {

    private SphericalCoords facing;
    private HostedConnection player;

    public ServerPlayer(ServerMain serverMain, Vector3f position, HostedConnection player) {
        super(serverMain, position);
        this.player = player;
        player.setAttribute("ServerPlayer", this);

        facing = new SphericalCoords(1, 0, FastMath.HALF_PI);
    }

    protected PlayerControl getControl() {
        return (PlayerControl) physicsControl;
    }

    public ServerInputPacket getInput() {
        return player.getAttribute("Input");
    }

    public float getMass() {
        return 10000;
    }

    @Override
    public int getModelID() {
        return 0;
    }

    public float getMoveDirection() {
        //Calculate Move Direction based on keyboard input
        //Returns -1 if no keys are pressed
        boolean up = getInput().isDown("Move Forward");
        boolean down = getInput().isDown("Move Back");
        boolean left = getInput().isDown("Move Left");
        boolean right = getInput().isDown("Move Right");

        if (up && !down) {
            if (left && !right) {
                return -FastMath.QUARTER_PI;
            } else if (right && !left) {
                return FastMath.QUARTER_PI;
            } else {
                return 0;
            }
        } else if (down && !up) {
            if (left && !right) {
                return FastMath.QUARTER_PI - FastMath.PI;
            } else if (right && !left) {
                return FastMath.PI - FastMath.QUARTER_PI;
            } else {
                return FastMath.PI;
            }
        } else {
            if (left && !right) {
                return -FastMath.HALF_PI;
            } else if (right && !left) {
                return FastMath.HALF_PI;
            } else {
                return -1;
            }
        }
    }

    @Override
    protected String initialAnimation() {
        return "Stand";
    }

    @Override
    protected RigidBodyControl initialPhysicsControl() {
        PlayerControl scc = new PlayerControl(serverMain, 2f, new CapsuleCollisionShape(.8f, 2f), getMass());

        scc.setDamping(0.5f, 0.5f);
        scc.setSleepingThresholds(0.7f, 0.7f);
        scc.setAngularFactor(0);
        scc.setMoveSpeed(40);
        scc.setMoveSlopeSpeed(0.3f);
        scc.setJumpSpeed(45);
        scc.setGravity(new Vector3f(0, -100, 0));

        return scc;
    }

    public void move(float tpf) {
        if (getInput() == null) {
            return;
        }
        float moveDir = getMoveDirection();
        //Move
        getControl().setMove(moveDir != -1);
        if (moveDir != -1) {
            getControl().setMove(true);
            SphericalCoords newMoveDir;
            if (Math.abs(moveDir) < .5) {
                newMoveDir = facing.addT(moveDir).setP(FastMath.HALF_PI);
            } else if (Math.abs(moveDir) < 1) {
                newMoveDir = facing.addT(moveDir).setP(FastMath.HALF_PI).setR(.75f);
            } else {
                newMoveDir = facing.addT(moveDir).setP(FastMath.HALF_PI).setR(.5f);
            }
            getControl().setWalkDirection(MathEx.sphericalToRectangular(newMoveDir));
            setAnimation("Run", false);
        } else {
            getControl().setWalkDirection(new Vector3f(0, 0, 0));
            setAnimation("Stand", false);
        }
        //Jump
        if (getInput().isPressed("Jump")) {
            getControl().jump();
        }
    }

    public void setAnimation(String name, boolean interrupt) {
        if (!animChannel.getAnimationName().equals(name) || interrupt) {
            animChannel.setAnim(name, .5f);
        }
    }

    @Override
    public void update(float tpf) {
        move(tpf);
        //System.out.println(getPosition());

        getControl().setGravity(new Vector3f(0, -100, 0));
        getControl().setRotationInUpdate(new Quaternion().fromAngleAxis(-facing.t + FastMath.HALF_PI, Vector3f.UNIT_Y));

        //Turn View
        if (getInput() != null) {
            facing = MathEx.rectangularToSpherical(getInput().getFacing());
        }
//        if (getInput() != null) {
//            setFacing(getFacing().addP(-getInput().getValue("Look Up")));
//            setFacing(getFacing().addP(getInput().getValue("Look Down")));
//            setFacing(getFacing().addT(-getInput().getValue("Look Left")));
//            setFacing(getFacing().addT(getInput().getValue("Look Right")));
//        }

        //Cap View
        if (facing.p < .2) {
            facing = facing.setP(.2f);
        }
        if (facing.p > Math.PI - .2) {
            facing = facing.setP(FastMath.PI - .2f);
        }

        //Set Camera
        //serverAppState.getCamera().positionBehind(getPosition().add(0, 3, 0), MathEx.sphericalToRectangular(getFacing()), 10);
        super.update(tpf);
    }
}
