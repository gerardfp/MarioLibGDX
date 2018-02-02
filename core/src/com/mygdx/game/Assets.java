package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by gerard on 02/02/18.
 */

public class Assets {
    static TextureAtlas textureAtlas = new TextureAtlas("sprites/assets.txt");

    static Animation<TextureRegion> marioWalk = new Animation<TextureRegion>(0.1f, textureAtlas.findRegions("mario/walk"));
    static Animation<TextureRegion> marioJump = new Animation<TextureRegion>(0.1f, textureAtlas.findRegions("mario/jump"));
    static Animation<TextureRegion> marioIdle = new Animation<TextureRegion>(0.1f, textureAtlas.findRegions("mario/idle"));
    static Animation<TextureRegion> marioDamp = new Animation<TextureRegion>(0.1f, textureAtlas.findRegions("mario/damp"));
    static Animation<TextureRegion> marioDead = new Animation<TextureRegion>(0.1f, textureAtlas.findRegions("mario/dead"));
}
