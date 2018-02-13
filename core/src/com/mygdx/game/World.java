package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.characters.GameCharacter;
import com.mygdx.game.characters.Mario;

import java.util.Iterator;

public class World {
    final float GRAVITY = -0.9f;

    OrthographicCamera camera;
    public SpriteBatch batch = new SpriteBatch();

    Map map;
    Mario mario;
    Array<GameCharacter> gameCharacters = new Array<GameCharacter>();

    Collider collider;

    public World(OrthographicCamera camera) {
        this.camera = camera;

        map = new Map(this);
        collider = new Collider(map, gameCharacters);
    }

    public void render(float delta) {
        update(delta);

        map.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        mario.render();
        for (GameCharacter gameCharacter : gameCharacters) {
            gameCharacter.render();
        }
        batch.end();

        camera.position.x = MathUtils.clamp(mario.position.x, camera.viewportWidth / 2f, map.getWidth() - camera.viewportWidth / 2f);
        camera.position.y = MathUtils.clamp(mario.position.y, camera.viewportHeight / 2f, map.getHeight() - camera.viewportHeight / 2f);
        camera.update();
    }

    void update(float delta) {
        mario.update(delta);
        contact(delta);
        applyPhysics(mario, delta);

        for (GameCharacter gameCharacter : gameCharacters) {
            gameCharacter.update(delta);
            applyPhysics(gameCharacter, delta);
        }
    }

    void applyPhysics(GameCharacter character, float delta){
        character.addVelocity(0, GRAVITY);

        collider.collide(character, delta);

        character.position.x += character.velocity.x * delta;
        character.position.y += character.velocity.y * delta;

        if (Math.abs(character.velocity.x) < 0.1f) {
            character.velocity.x = 0;
        }
    }

    void contact(float delta) {
        Rectangle startPosition = new Rectangle(mario.position.x, mario.position.y, mario.width, mario.height);
        Rectangle endPosition = new Rectangle(mario.position.x + mario.velocity.x * delta, mario.position.y + mario.velocity.y * delta, mario.width, mario.height);

        Iterator<GameCharacter> gameCharacterIterator = gameCharacters.iterator();
        while (gameCharacterIterator.hasNext()) {
            GameCharacter gameCharacter = gameCharacterIterator.next();
            Rectangle gameCharacterRectangle = new Rectangle();
            gameCharacterRectangle.set(gameCharacter.position.x, gameCharacter.position.y, gameCharacter.width, gameCharacter.height);

            if (Intersector.overlaps(endPosition, gameCharacterRectangle)) {
                switch (gameCharacter.getType()){
                    case GOOMBA:
                        Collider.Collision collision = collider.getCollision(gameCharacterRectangle, startPosition.getCenter(new Vector2()));
                        if(collision.side == Collider.Side.TOP) {
                            gameCharacters.removeValue(gameCharacter, false);
                            mario.onCollision(collision.side, collision.point);
                            mario.jump();
                        } else {
                            mario.setState(Mario.State.DEAD);
                        }
                        break;
                    case COIN:
                        gameCharacters.removeValue(gameCharacter,false);
                        mario.coinsCount++;
                        break;
                }
            }
        }
    }
}
