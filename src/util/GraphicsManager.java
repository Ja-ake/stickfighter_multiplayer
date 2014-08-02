/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.CartoonEdgeFilter;
import com.jme3.renderer.Caps;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;

/**
 *
 * @author Rory
 */
public class GraphicsManager {

    public static void createFilters(SimpleApplication app) {
        if (app.getRenderer().getCaps().contains(Caps.GLSL100)) {
            FilterPostProcessor fpp = new FilterPostProcessor(app.getAssetManager());
            //fpp.setNumSamples(4);
            int numSamples = app.getContext().getSettings().getSamples();
            if (numSamples > 0) {
                fpp.setNumSamples(numSamples);
            }
            CartoonEdgeFilter toon = new CartoonEdgeFilter();
            //toon.setEdgeColor(ColorRGBA.Black);
            fpp.addFilter(toon);
            app.getViewPort().addProcessor(fpp);
        }
    }

    public static void createLighting(SimpleApplication app, Node node) {
        app.getViewPort().setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        //Ambient
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(4f));
        node.addLight(al);

        //Sun
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(1.6f, -1, 1));
        sun.setColor(ColorRGBA.White.mult(.4f));
        node.addLight(sun);

        //Shadows
        int shadowMapSize = 1024 * 1;
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(app.getAssetManager(), shadowMapSize, 3);
        dlsr.setLight(sun);
        app.getViewPort().addProcessor(dlsr);

        DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(app.getAssetManager(), shadowMapSize, 3);
        dlsf.setLight(sun);
        dlsf.setEnabled(true);
        FilterPostProcessor fpp = new FilterPostProcessor(app.getAssetManager());
        fpp.addFilter(dlsf);
        app.getViewPort().addProcessor(fpp);

        node.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
    }
}
