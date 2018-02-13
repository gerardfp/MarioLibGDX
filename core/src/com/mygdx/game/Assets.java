package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
    public static TextureAtlas textureAtlas = new TextureAtlas("sprites/assets.txt");

    public static Animation<TextureRegion> marioWalk = new Animation<TextureRegion>(0.1f, textureAtlas.findRegions("mario/walk"));
    public static Animation<TextureRegion> marioJump = new Animation<TextureRegion>(0.1f, textureAtlas.findRegions("mario/jump"));
    public static Animation<TextureRegion> marioIdle = new Animation<TextureRegion>(0.1f, textureAtlas.findRegions("mario/idle"));
    public static Animation<TextureRegion> marioDamp = new Animation<TextureRegion>(0.1f, textureAtlas.findRegions("mario/damp"));
    public static Animation<TextureRegion> marioDead = new Animation<TextureRegion>(0.1f, textureAtlas.findRegions("mario/dead"));
    public static Animation<TextureRegion> goombaWalk = new Animation<TextureRegion>(0.1f, textureAtlas.findRegions("goomba/walk"));
    public static Animation<TextureRegion> goombaDead = new Animation<TextureRegion>(0.1f, textureAtlas.findRegions("goomba/dead"));
    public static Animation<TextureRegion> coin = new Animation<TextureRegion>(0.1f, textureAtlas.findRegions("coin/coin"));

}
