package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by gerard on 01/02/18.
 */

public class Mario {
    static float DAMPING = 0.67f;

    SpriteBatch batch;
    OrthographicCamera camera;

    float width;
    float height;

    enum State {
        Idle, Walking, Jumping, Dead, Damping
    }

    enum Direction {
        Left, Right
    }

    final Vector2 maxVelocity = new Vector2(10,19);
    final Vector2 position = new Vector2(0f,0f);
    final Vector2 velocity = new Vector2(0,0);

    State state = State.Idle;
    float stateTime = 0;
    Direction direction = Direction.Right;

    Animation<TextureRegion> stateAnimation = Assets.marioIdle;

    boolean grounded = true;

    Mario(OrthographicCamera camera) {
        this.camera = camera;
        batch = new SpriteBatch();

        TextureRegion textureRegion = stateAnimation.getKeyFrame(stateTime, true);
        width = textureRegion.getRegionWidth()*GameScreen.PPM;
        height = textureRegion.getRegionHeight()*GameScreen.PPM;
    }

    void render(){
        TextureRegion textureRegion = stateAnimation.getKeyFrame(stateTime, true);

        width = textureRegion.getRegionWidth()*GameScreen.PPM;
        height = textureRegion.getRegionHeight()*GameScreen.PPM;

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        if(direction == Direction.Left) {
            batch.draw(textureRegion, position.x + width, position.y, -width, height);
        } else {
            batch.draw(textureRegion, position.x, position.y, width, height);
        }
        batch.end();
    }

    void setState(State state){
        this.state = state;

        stateAnimation = Assets.marioIdle;
        if (state == State.Jumping) {
            stateAnimation = Assets.marioJump;
        } else if (state == State.Damping) {
            stateAnimation = Assets.marioDamp;
        } else if (state == State.Walking) {
            stateAnimation = Assets.marioWalk;
        } else if (state == State.Dead) {
            stateAnimation = Assets.marioDead;
        }
    }

}
