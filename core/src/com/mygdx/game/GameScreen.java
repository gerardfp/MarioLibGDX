package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Created by gerard on 01/02/18.
 */

public class GameScreen extends MyGdxGameScreen {

    World world;

    FitViewport viewport;
    OrthographicCamera camera;

    GameScreen(Game game){
        super(game);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(512, 256, camera);
        viewport.apply();

        camera.position.set(256, 128, 0);
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
