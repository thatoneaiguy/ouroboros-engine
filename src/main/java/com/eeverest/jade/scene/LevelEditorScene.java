package com.eeverest.jade.scene;

import com.eeverest.jade.KeyListener;
import com.eeverest.jade.Scene;
import com.eeverest.jade.Window;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene {
    private boolean changingScene = false;
    private float timeToChangeScene = 2.0f;

    public LevelEditorScene() {
        System.out.println("Level editor loaded successfully");
    }

    /**
     * Defaults to {@link Window} colour ( #FFFFFF )
     */


    @Override
    public void update(float dt) {
        if (!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) changingScene = true;

        if (changingScene && timeToChangeScene > 0) {
            timeToChangeScene -= dt;
            Window.get().r -= dt * 5.0f;
            Window.get().g -= dt * 5.0f;
            Window.get().b -= dt * 5.0f;
        } else if (changingScene) {
            Window.changeScene(1);
        }
    }
}
