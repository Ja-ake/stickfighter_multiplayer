/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.jme3.app.Application;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsRayTestResult;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import java.util.List;

public class PlayerControl extends RigidBodyControl implements PhysicsTickListener {

    //The app herp derp
    private Application app;
    //Should you move, should you jump
    private boolean doMove, doJump, hasJumped = false;
    //The direction to walk
    private Vector3f walkDirection = Vector3f.ZERO;
    //Add this to the jump, no idea why this isn't part of jumpSpeedY, maybe for X and Z directions?
    private Vector3f additiveJumpSpeed = Vector3f.ZERO;
    //Temp var used to rotate
    private Quaternion newRotation;
    //Not completely sure, used to put some limits on stopping and jumping, where the timers tick up to the max
    private int stopTimer, jumpTimer, maxStopTimer, maxJumpTimer;
    //If you've moved since the last timer update?
    private boolean hasMoved = false;
    //The angle of the thing you're standing on
    private float angleNormals = 0;
    //The object below you
    private PhysicsRayTestResult physicsClosestTest;
    //Movement constants
    private float jumpSpeedY, moveSpeed, moveSpeedMultiplier, moveSlopeSpeed, slopeLimitAngle, stopDamping, centerToBottomHeight;
    private float frictionWalk, frictionStop, mainWalkInterpolation, otherWalkInterpolation;

    public PlayerControl(Application app, float centerToBottomHeight, CollisionShape colShape, float mass) {
        super(colShape, mass);
        this.app = app;
        this.centerToBottomHeight = centerToBottomHeight;

        jumpSpeedY = 45f;
        moveSpeed = 40f;
        moveSpeedMultiplier = 1;
        moveSlopeSpeed = 0.3f;
        slopeLimitAngle = FastMath.DEG_TO_RAD * 45f;
        stopDamping = 0.8f;

        stopTimer = 0;
        jumpTimer = 0;
        maxStopTimer = 30;
        maxJumpTimer = 20;

        frictionWalk = 0.01f;
        frictionStop = 2f;
        mainWalkInterpolation = 0.7f;
        otherWalkInterpolation = 0.9f;

        setAngularFactor(0);

        app.getStateManager().getState(BulletAppState.class).getPhysicsSpace().addTickListener(this);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        activate();
        if (spatial != null && newRotation != null) {
            spatial.setLocalRotation(newRotation);
//            newRotation = null;
        }
    }

//    @Override
//    protected void controlRender(RenderManager rm, ViewPort vp) {
////        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    @Override
    public void prePhysicsTick(PhysicsSpace space, float tpf) {
        //Update the angle of the ground you're on
        if (physicsClosestTest != null) {
            angleNormals = physicsClosestTest.getHitNormalLocal().normalizeLocal().angleBetween(Vector3f.UNIT_Y);
        }
        //Set friction, depending on whether you're moving or stopped
        if (angleNormals < slopeLimitAngle && physicsClosestTest != null && (!doMove && !doJump && !hasJumped)) {
            setFriction(frictionStop);
        } else {
            setFriction(frictionWalk);
        }
        //If you should move...
        if (doMove) {
            //Calculate the direction in which to apply force
            Vector3f moveCharVec = walkDirection.mult(moveSpeed * moveSpeedMultiplier);
            moveCharVec.setY(getLinearVelocity().getY());
            //Depending on where you are...
            if ((angleNormals < slopeLimitAngle && physicsClosestTest != null) || !isActive()) {
                //If you're on a gentle slope
                setLinearVelocity(moveCharVec.interpolate(getLinearVelocity(), mainWalkInterpolation));
            } else if (angleNormals > slopeLimitAngle && angleNormals < FastMath.DEG_TO_RAD * 80f && physicsClosestTest != null) {
                //If you're on a steep slope
                applyCentralForce((walkDirection.mult(moveSlopeSpeed).setY(0f)));
//                setLinearVelocity(moveCharVec.interpolate(getLinearVelocity(), 0.99f));
            } else {
                //If you're in the air
//                physSp.applyCentralForce((walkDirection.mult(moveSlopeSpeed).setY(0f)));
//                setLinearVelocity(moveCharVec.setY(getLinearVelocity().getY()));
                setLinearVelocity(moveCharVec.interpolate(getLinearVelocity(), otherWalkInterpolation));
            }
            //Update variables
            hasMoved = true;
            hasJumped = false;
            stopTimer = 0;
            jumpTimer = 0;
        }
        //Step the jump timer
        if (jumpTimer > 0) {
            if (jumpTimer > maxJumpTimer) {
                jumpTimer = 0;
            } else {
                jumpTimer++;
            }
        }
        //If you should jump...
        if (doJump && !hasJumped && (physicsClosestTest != null || !isActive())) {
            //If the slope is gentle
            if ((angleNormals < slopeLimitAngle)) {
                //Jump
//                clearForces();
                setLinearVelocity(getLinearVelocity().setY(jumpSpeedY).add(additiveJumpSpeed));
                //setLinearVelocity(new Vector3f(getLinearVelocity().x, jumpSpeedY, getLinearVelocity().z).addLocal(additiveJumpSpeed));
                //setLinearVelocity(getLinearVelocity().add(Vector3f.UNIT_Y.clone().multLocal(jumpSpeedY).addLocal(additiveJumpSpeed)));
//                physSp.applyImpulse(Vector3f.UNIT_Y.mult(jumpSpeed), Vector3f.ZERO);
                hasJumped = true;
                jumpTimer = 1;
            }
        }
        //If you should stop...
        if ((hasMoved || hasJumped) && physicsClosestTest != null && angleNormals < slopeLimitAngle && !doMove) {
            //If you've both jumped and moved
            if (hasJumped && hasMoved) {
                //Say you haven't jumped (no idea why)
                hasJumped = false;
                jumpTimer = 0;
            }
            //If you're not jumping
            if (jumpTimer == 0) {
                if (stopTimer < maxStopTimer) {
                    //If you're not done stopping
//                setLinearDamping(1f);
//                setFriction(10f);
                    setLinearVelocity(getLinearVelocity().multLocal(new Vector3f(stopDamping, 1, stopDamping)));
                    stopTimer += 1;
                } else {
                    //If you are done stopping
//                    setLinearDamping(0.5f);
//                    setFriction(0.3f);
                    stopTimer = 0;
                    hasMoved = false;
                    hasJumped = false;
                    jumpTimer = 0;
                }
            }
        }
//        else {
//            setLinearDamping(0.5f);
//            setFriction(0.3f);
//        }
        //Update variables
        if (doJump) {
            doJump = false; // set it after damping
        }
    }

    //This method calculates the closest object below you, if there is one
    @Override
    public void physicsTick(PhysicsSpace space, float tpf) {
        physicsClosestTest = null;
        angleNormals = 0f;
        float closestFraction = centerToBottomHeight * 10f;

        if (isActive()) {
            List<PhysicsRayTestResult> results = space.rayTest(getPhysicsLocation().add(Vector3f.UNIT_Y.mult(-0.5f * centerToBottomHeight)),
                    getPhysicsLocation().add(Vector3f.UNIT_Y.mult(-1.5f * centerToBottomHeight)));
            for (PhysicsRayTestResult physicsRayTestResult : results) {

                if (physicsRayTestResult.getHitFraction() < closestFraction) {
                    if (!spatial.equals(physicsRayTestResult.getCollisionObject().getUserObject())) {
                        if (physicsRayTestResult.getCollisionObject() instanceof GhostControl == false) {
                            physicsClosestTest = physicsRayTestResult;
                            closestFraction = physicsRayTestResult.getHitFraction();
                        }
                    }
                }
            }
        }
    }

    // DESTROY METHOD
    @Override
    public void destroy() {
        physicsClosestTest = null;
        walkDirection = null;
        app.getStateManager().getState(BulletAppState.class).getPhysicsSpace().removeTickListener(this);
//        spatial.removeControl(this);
        app.getStateManager().getState(BulletAppState.class).getPhysicsSpace().remove(this);

        app = null;
        spatial.removeControl(this);
//        this = null;

    }

    public Vector3f getWalkDirection() {
        return walkDirection;
    }

    public void setWalkDirection(Vector3f walkDirection) {
        this.walkDirection = walkDirection;
    }

    public void setRotationInUpdate(Quaternion newRotation) {
        this.newRotation = newRotation;
    }

    public boolean isDoMove() {
        return doMove;
    }

    public void setMove(boolean doMove) {
        this.doMove = doMove;
    }

    public void jump() {
        doJump = true;
    }

    public float getJumpSpeed() {
        return jumpSpeedY;
    }

    public void setJumpSpeed(float jumpSpeed) {
        jumpSpeedY = jumpSpeed;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public float getMoveSlopeSpeed() {
        return moveSlopeSpeed;
    }

    public void setMoveSlopeSpeed(float moveSlopeSpeed) {
        this.moveSlopeSpeed = moveSlopeSpeed;
    }

    public float getSlopeLimitAngle() {
        return slopeLimitAngle;
    }

    public void setSlopeLimitAngle(float slopeLimitAngle) {
        this.slopeLimitAngle = slopeLimitAngle;
    }

    public float getStopDamping() {
        return stopDamping;
    }

    public void setStopDamping(float stopDamping) {
        this.stopDamping = stopDamping;
    }

    public Vector3f getAdditiveJumpSpeed() {
        return additiveJumpSpeed;
    }

    public void setAdditiveJumpSpeed(Vector3f additiveJumpSpeed) {
        this.additiveJumpSpeed = additiveJumpSpeed;
    }

    public float getMoveSpeedMultiplier() {
        return moveSpeedMultiplier;
    }

    public void setMoveSpeedMultiplier(float moveSpeedMultiplier) {
        this.moveSpeedMultiplier = moveSpeedMultiplier;
    }

    public float getFrictionWalk() {
        return frictionWalk;
    }

    public void setFrictionWalk(float frictionWalk) {
        this.frictionWalk = frictionWalk;
    }

    public float getFrictionStop() {
        return frictionStop;
    }

    public void setFrictionStop(float frictionStop) {
        this.frictionStop = frictionStop;
    }

    public float getMainWalkInterpolation() {
        return mainWalkInterpolation;
    }

    public void setMainWalkInterpolation(float mainWalkInterpolation) {
        this.mainWalkInterpolation = mainWalkInterpolation;
    }

    public float getOtherWalkInterpolation() {
        return otherWalkInterpolation;
    }

    public void setOtherWalkInterpolation(float otherWalkInterpolation) {
        this.otherWalkInterpolation = otherWalkInterpolation;
    }

    public int getMaxStopTimer() {
        return maxStopTimer;
    }

    public void setMaxStopTimer(int maxStopTimer) {
        this.maxStopTimer = maxStopTimer;
    }

    public int getMaxJumpTimer() {
        return maxJumpTimer;
    }

    public void setMaxJumpTimer(int maxJumpTimer) {
        this.maxJumpTimer = maxJumpTimer;
    }
}
