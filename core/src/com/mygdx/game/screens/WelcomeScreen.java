package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by gerard on 01/02/18.
 */

public class WelcomeScreen extends MyGdxGameScreen {

    Texture logo;
    SpriteBatch batch;
    float timer;

    WelcomeScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        logo = new Texture("mario_bros.png");
        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        timer += delta;
        System.out.println(timer);
        if (timer > 0.5) {
            game.setScreen(new MenuScreen(game));
            dispose();
            return;
        }

        batch.begin();
        batch.draw(logo, 100,100);
        batch.end();
    }

    @Override
    public void dispose() {
        logo.dispose();
        batch.dispose();
    }
}
