/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

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

    public Camera3D(Camera cam) {
        camera = cam;
    }

    public SphericalCoords getFacing() {
        return MathEx.rectangularToSpherical(camera.getDirection());
    }

    public Vector3f getLocation() {
        return camera.getLocation();
    }

    public void positionBehind(Vector3f position, SphericalCoords facing, float radius) {
        camera.setLocation(position.subtract(MathEx.sphericalToRectangular(facing.setR(radius))));
        camera.lookAt(position, UP);
    }
}
