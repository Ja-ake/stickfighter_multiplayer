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

        ClientMain app2 = new ClientMain();
        app2.start(JmeContext.Type.Headless);

        ClientMain app3 = new ClientMain();
        app3.start();
    }
}
