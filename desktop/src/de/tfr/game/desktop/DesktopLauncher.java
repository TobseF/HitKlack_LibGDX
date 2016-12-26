package de.tfr.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.tfr.game.HitKlack;

/**
 * ## Run configuration setup:
 * Set the working directory to `\hitklack\android\assets`
 * @author Tobse4Git@gmail.com
 */
public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1280;
        config.height = 720;
        new LwjglApplication(new HitKlack(), config);
    }
}
