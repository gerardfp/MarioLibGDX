package com.mygdx.game.characters;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets;
import com.mygdx.game.Collider;
import com.mygdx.game.World;

public class Goomba extends GameCharacter {

    public Goomba(World world, Vector2 position) {
        super(world, position);
        animation = Assets.goombaWalk;
        setVelocity(4,0);
    }

    @Override
    public Type getType() {
        return Type.GOOMBA;
    }

    @Override
    public void update(float delta){
        super.update(delta);
    }

    @Override
    public void onCollision(Collider.Side side, Vector2 point){
        if (side == Collider.Side.LEFT) {
            velocity.x *= -1;
            position.x = point.x - width;
        } else if (side == Collider.Side.RIGHT) {
            velocity.x *= -1;
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
