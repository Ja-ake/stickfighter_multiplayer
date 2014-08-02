/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import client.ClientMain;
import com.jme3.system.JmeContext;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rory
 */
public class TestingMain {

    public static void main(String[] args) {
        Logger.getLogger("").setLevel(Level.SEVERE);
        NetworkUtil.serialize();

        for (int i = 0; i < 20; i++) {
            ClientMain app = new ClientMain();
            app.start(JmeContext.Type.Headless);
        }
    }
}
