package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by gerard on 01/02/18.
 */

public class Mario {
    SpriteBatch batch;
    OrthographicCamera camera;

    Vector2 position = new Vector2(1,1);
    Vector2 velocity = new Vector2(0,0);
    Vector2 maxVelocity = new Vector2(30,55);
    final float DAMPING = 0.95f;
    float stateTime = 0;

    Mario(OrthographicCamera camera) {
        this.camera = camera;
        batch = new SpriteBatch();
    }

    void render(){
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(Assets.marioWalk.getKeyFrame(stateTime, true), position.x, position.y);
        batch.end();
    }
}
