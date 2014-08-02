/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import util.MathEx;
import util.SphericalCoords;

/**
 *
 * @author Rory
 */
public class Camera3D {

    private static final Vector3f UP = new Vector3f(0, 1, 0);
    private Camera camera;
    private GameAppState gameAppState;

    public Camera3D(GameAppState gameAppState) {
        this.gameAppState = gameAppState;
        camera = gameAppState.getClientMain().getCamera();
    }

    public SphericalCoords getFacing() {
        return MathEx.rectangularToSpherical(camera.getDirection());
    }

    public Vector3f getLocation() {
        return camera.getLocation();
    }

    public void positionBehind(Vector3f focus, SphericalCoords facing, float distance) {
        CollisionResults results = new CollisionResults();
        Ray ray = new Ray(focus, MathEx.sphericalToRectangular(facing).negate());
        //gameAppState.getNodeWithoutPlayer().collideWith(ray, results);
        gameAppState.getLevelModel().collideWith(ray, results);

//        System.out.println(results);
//        System.out.println(results.getClosestCollision().getDistance());

        if (results.size() == 0 || results.getClosestCollision().getDistance() > distance) {
            Vector3f position = focus.subtract(MathEx.sphericalToRectangular(facing.setR(distance)));
            camera.setLocation(position);
        } else {
            Vector3f position = results.getClosestCollision().getContactPoint().interpolate(focus, .1f);
            camera.setLocation(position);
        }
        camera.lookAt(focus, UP);

//        //The ideal camera spot
//        Vector3f prefPos = position.subtract(MathEx.sphericalToRectangular(facing.setR(distance)));
//        //Do a ray test and find the closest value
//        List<PhysicsRayTestResult> rayTest = space.rayTest(position, prefPos);
//        PhysicsRayTestResult minResult = null;
//        for (PhysicsRayTestResult prtr : rayTest) {
//            if (minResult == null || prtr.getHitFraction() < minResult.getHitFraction()) {
//                minResult = prtr;
//            }
//        }
//        //If no hits, use the ideal
//        if (minResult == null) {
//            camera.setLocation(prefPos);
//        } else {
//            Vector3f newPos = position.subtract(MathEx.sphericalToRectangular(facing.setR(minResult.getHitFraction() * distance)));
//            camera.setLocation(newPos);
//        }
//        camera.lookAt(position, UP);
    }
}
