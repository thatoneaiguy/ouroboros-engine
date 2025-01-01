package com.eeverest.jade.scene;

import com.eeverest.jade.Scene;
import com.eeverest.jade.Window;

public class LevelScene extends Scene {
    public LevelScene() {
        System.out.println("Level loaded successfully");
    }

    // ! LEVEL IS DEFAULT BLACK

    @Override
    public void update(float dt) {
        Window.get().r = 0;
        Window.get().b = 0;
        Window.get().g = 0;
    }
}
