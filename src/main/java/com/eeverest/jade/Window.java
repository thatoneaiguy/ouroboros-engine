package com.eeverest.jade;

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
    private float r, g, b, a;
    private boolean fadeToBlack = false;

    private static Window window = null;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Ouroboros";

        // rgba dev tests
        r = 1;
        g = 0;
        b = 0;
        a = 0;
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
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

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
    }

    public void loop() {
        float beginTime = Time.getTime();
        float endTime = Time.getTime();

        while ( !glfwWindowShouldClose(glfwWindow) ) {
            // key events
            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if ( fadeToBlack ) r = Math.max(r - 0.01f, 0 );

            if ( KeyListener.isKeyPressed( GLFW_KEY_SPACE )) fadeToBlack = true;

            glfwSwapBuffers(glfwWindow);

            endTime = Time.getTime();
            float dt = endTime - beginTime;
        }
    }
}
