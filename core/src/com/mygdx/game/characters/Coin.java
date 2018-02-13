package com.mygdx.game.characters;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets;
import com.mygdx.game.Collider;
import com.mygdx.game.World;

public class Coin extends GameCharacter {

    public Coin(World world, Vector2 position){
        super(world, position);

        animation = Assets.coin;
        velocity = new Vector2(0, 0);
    }

    public void onCollision(Collider.Side side, Vector2 point){

    }

    @Override
    public void addVelocity(float x, float y) {

    }

    @Override
    public Type getType() {
        return Type.COIN;
    }
}
