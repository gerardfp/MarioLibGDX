package com.mygdx.game.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.World;

/**
 * Created by gerard on 01/02/18.
 */

public class GameScreen extends MyGdxGameScreen {
    public static float PPM = 1/16f;
    public static int SCENE_WIDTH = 16;
    public static int SCENE_HEIGHT = 8;

    World world;

    FitViewport viewport;
    OrthographicCamera camera;
    float timer;

    public GameScreen(Game game){
        super(game);
    }

    @Override
    public void show() {

        if (! Gdx.app.getType().equals(Application.ApplicationType.Android)) {
            Gdx.graphics.setWindowedMode(1024,512);
        }

        camera = new OrthographicCamera();
        viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, camera);
        viewport.apply();

        camera.position.set(SCENE_WIDTH/2, SCENE_HEIGHT/2, 0);
        world = new World(camera);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.23f,0.73f,0.98f,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
