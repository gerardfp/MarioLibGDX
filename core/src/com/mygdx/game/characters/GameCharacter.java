package com.mygdx.game.characters;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Collider;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.World;


public abstract class GameCharacter {

    public enum Type {
        COIN, GOOMBA, MARIO;
    }

    World world;

    public Vector2 position = new Vector2();
    public Vector2 velocity = new Vector2();
    Animation<TextureRegion> animation;
    TextureRegion currentFrame;
    float stateTime;
    public float width;
    public float height;
    public boolean grounded;

    GameCharacter(World world, Vector2 position) {
        this.world = world;
        this.position = position;
    }

    void setCurrentFrame() {
        currentFrame = animation.getKeyFrame(stateTime, true);
        width = currentFrame.getRegionWidth()* GameScreen.PPM;
        height = currentFrame.getRegionHeight()*GameScreen.PPM;
    }

    public void render(){
        setCurrentFrame();
        world.batch.draw(currentFrame, position.x, position.y, width, height);
    }

    public void update(float delta) {
        stateTime += delta;
    }

    public void setVelocity(float x, float y){
        velocity.x = x;
        velocity.y = y;
    }

    public void addVelocity(float x, float y){
        velocity.x += x;
        velocity.y += y;
    }

    public abstract Type getType();
    public abstract void onCollision(Collider.Side side, Vector2 point);
}
