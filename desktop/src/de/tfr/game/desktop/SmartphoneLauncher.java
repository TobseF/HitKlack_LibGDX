package de.tfr.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.tfr.game.HitKlack;

/**
 * Good for testing the app on a desktop machine: Runs the game with smartphone aspect ratio in a small Quarter FHD resolution (1080 / 2 x 1920 / 2) (540 x
 * 960).
 * ## Run configuration setup:
 * Set the working directory to `\hitklack\android\assets`
 *
 * @author Tobse4Git@gmail.com
 */
public class SmartphoneLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1080 / 2;
		config.height = 1920 / 2;
		new LwjglApplication(new HitKlack(), config);
	}
}
