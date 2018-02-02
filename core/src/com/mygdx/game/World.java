package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by gerard on 02/02/18.
 */

public class World {

    OrthographicCamera camera;

    Mario mario;

    final float GRAVITY = 2.5f;

    World(OrthographicCamera camera) {
        this.camera = camera;

        mario = new Mario(camera);
    }

    void render(float delta) {
        if(Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            mario.velocity.y = mario.maxVelocity.y;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            mario.velocity.x = mario.maxVelocity.x;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            mario.velocity.x = -mario.maxVelocity.x;
        }

        mario.velocity.x *= mario.DAMPING;
        mario.velocity.y -= GRAVITY;

        mario.position.x += mario.velocity.x * delta;
        mario.position.y += mario.velocity.y * delta;

        if(mario.position.y < 0) {
            mario.position.y = 0;
        }

        mario.render();
    }
}
