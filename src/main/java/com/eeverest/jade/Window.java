package com.eeverest.jade;

import com.eeverest.jade.scene.LevelEditorScene;
import com.eeverest.jade.scene.LevelScene;
import com.eeverest.util.Time;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    int width, height;
    String title;
    private long glfwWindow;

    // testing rgba values
    public float r, g, b, a;
    private boolean fadeToBlack = false;

    private static Window window = null;

    private static Scene currentScene = null;

    private Window() {
        this.width = 1920 / 2;
        this.height = 1080 / 2;
        this.title = "Ouroboros";

        r = 1;
        b = 1;
        g = 1;
        a = 1;
    }

    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.init();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.init();
                break;
            default:
                assert false: "An unknown scene ( " + newScene + ") is attempting to load.";
                break;
        }
    }

    public static Window get() {
        if ( Window.window == null ) Window.window = new Window();

        return Window.window;
    }

    public void run() {
        System.out.println("LWJGL Loaded on version " + Version.getVersion());

        init();
        loop();

        // be a good person, free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // and the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        // error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // init glfw so we can have windows
        if ( !glfwInit() ) throw new IllegalStateException("Unable to load GLFW");
        // conf
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

        // and create it
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if ( glfwWindow == NULL ) throw new IllegalStateException("Failed to create GLFW window");

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        // use opengl
        glfwMakeContextCurrent(glfwWindow);
        // default enable vsync cause im not a monster
        glfwSwapInterval(1);

        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        Window.changeScene(0);
    }

    public void loop() {
        float beginTime = Time.getTime();
        float endTime = Time.getTime();
        float dt = -1.0f;

        while ( !glfwWindowShouldClose(glfwWindow) ) {
            // key events
            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if (dt >= 0) currentScene.update(dt);

            glfwSwapBuffers(glfwWindow);

            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }
}
