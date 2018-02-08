package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by gerard on 08/02/2018.
 */

public abstract class MyMapObject {
    Vector2 position = new Vector2();
    Animation<TextureRegion> animation;
    TextureRegion currentFrame;
    float stateTime;
    float width;
    float height;

    void render(SpriteBatch batch) {
        currentFrame = animation.getKeyFrame(stateTime);
        width = currentFrame.getRegionWidth()*GameScreen.PPM;
        height = currentFrame.getRegionHeight()*GameScreen.PPM;

        batch.draw(currentFrame, position.x, position.y, width, height);
    }

    void update(float delta) {
        stateTime += delta;
    }
}
