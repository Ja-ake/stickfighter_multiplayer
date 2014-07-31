/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Rory
 */
public abstract class ModelCreator {

    public static Node getModel(int id, SimpleApplication app) {
        switch (id) {
            case 0: //Stick figure
                //Material
                Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
                mat.setBoolean("UseMaterialColors", true);
                mat.setColor("Diffuse", ColorRGBA.White);
                //Body
                Node stick = (Node) app.getAssetManager().loadModel("Models/Stick/StickMesh.mesh.xml");
                Spatial body = stick.getChild(0);
                body.setMaterial(mat);
                body.setLocalTranslation(0, -3.5f, 0);
                //Head
                Spatial head = ((Node) app.getAssetManager().loadModel("Models/Stick/HeadMesh.mesh.xml")).getChild(0);
                head.setMaterial(mat);
                head.setLocalTranslation(0, -3.5f, 0);
                stick.attachChild(head);

                stick.scale(.5f);
                return stick;
        }
        return null;
    }
}
