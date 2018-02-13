package com.mygdx.game.characters;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets;
import com.mygdx.game.Collider;
import com.mygdx.game.Controls;
import com.mygdx.game.World;

public class Mario extends GameCharacter {
    static float DAMPING = 0.67f;
    static final Vector2 MAX_VELOCITY = new Vector2(10,19);

    public enum State {
        IDLE, WALKING, JUMPING, DEAD, DAMPING
    }

    enum Direction {
        LEFT, RIGHT
    }

    State state = State.IDLE;
    Direction direction = Direction.RIGHT;
    public int coinsCount;

    public Mario(World world, Vector2 position) {
        super(world, position);
        animation = Assets.marioIdle;
    }

    @Override
    public Type getType() {
        return Type.MARIO;
    }

    @Override
    public void render(){
        setCurrentFrame();

        if (direction == Direction.LEFT) {
            world.batch.draw(currentFrame, position.x + width, position.y, -width, height);
        } else {
            world.batch.draw(currentFrame, position.x, position.y, width, height);
        }
    }

    public void update(float delta){
        super.update(delta);

        if(velocity.x > 0 && grounded) {
            setState(Mario.State.DAMPING);
        }

        if (Controls.isRightPressed()) {
            velocity.x = MAX_VELOCITY.x;
            if (grounded) setState(Mario.State.WALKING);
            direction = Mario.Direction.RIGHT;
        }
        if (Controls.isLeftPressed()) {
            velocity.x = -MAX_VELOCITY.x;
            if (grounded) setState(Mario.State.WALKING);
            direction = Mario.Direction.LEFT;
        }
        if(Controls.isJumpPressed() && grounded) {
            jump();
        }

        if (velocity.x == 0 && grounded) {
            setState(Mario.State.IDLE);
        }

        velocity.x *= Mario.DAMPING;
    }

    public void setState(State state){
        this.state = state;

        animation = Assets.marioIdle;
        if (state == State.JUMPING) {
            animation = Assets.marioJump;
        } else if (state == State.DAMPING) {
            animation = Assets.marioDamp;
        } else if (state == State.WALKING) {
            animation = Assets.marioWalk;
        } else if (state == State.DEAD) {
            animation = Assets.marioDead;
        }
    }

    public void jump(){
        velocity.y = MAX_VELOCITY.y;
        setState(Mario.State.JUMPING);
        grounded = false;
    }

    public void onCollision(Collider.Side side, Vector2 point){
        if (side == Collider.Side.LEFT) {
            velocity.x = 0;
            position.x = point.x - width;
        } else if (side == Collider.Side.RIGHT) {
            velocity.x = 0;
            position.x = point.x;
        } else if (side == Collider.Side.BOTTOM) {
            velocity.y = 0;
            position.y = point.y - height;
        } else if (side == Collider.Side.TOP) {
            velocity.y = 0;
            position.y = point.y;
            grounded = true;
        }
    }
}
