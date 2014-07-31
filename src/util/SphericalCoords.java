/*
 * SphericalCoords (should be SphericalCoordinates or SphericalVector)
 */
package util;

import com.jme3.network.serializing.Serializable;

public class SphericalCoords {

    public final float r, t, p;

    public SphericalCoords() {
        r = 0;
        t = 0;
        p = 0;
    }

    public SphericalCoords(float r, float t, float p) {
        this.r = r;
        this.t = t;
        this.p = p;
    }

    public SphericalCoords addP(float f) {
        return new SphericalCoords(r, t, p + f);
    }

    public SphericalCoords addT(float f) {
        return new SphericalCoords(r, t + f, p);
    }

    public SphericalCoords setR(float f) {
        return new SphericalCoords(f, t, p);
    }

    public SphericalCoords setP(float f) {
        return new SphericalCoords(r, t, f);
    }

    public SphericalCoords setT(float f) {
        return new SphericalCoords(r, f, p);
    }

    @Override
    public String toString() {
        return r + " " + t + " " + p;
    }
}
