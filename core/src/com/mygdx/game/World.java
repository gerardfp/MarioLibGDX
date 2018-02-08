package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by gerard on 02/02/18.
 */

public class World {
    final float GRAVITY = -0.9f;

    OrthographicCamera camera;

    Map map;
    Mario mario;
    Array<Coin> coins = new Array<Coin>();
    Array<Goomba> goombas = new Array<Goomba>();


    private Pool<Rectangle> rectPool;
    private Array<Rectangle> tiles = new Array<Rectangle>();
    private Array<Rectangle> nearestTiles = new Array<Rectangle>();

    World(OrthographicCamera camera) {
        this.camera = camera;

        map = new Map(camera);
        mario = new Mario(camera);

        rectPool = new Pool<Rectangle>() {
            @Override
            protected Rectangle newObject () {
                return new Rectangle();
            }
        };
    }

    void render(float delta) {
        map.render();
        mario.render();

        updateMario(delta);

        camera.position.x = MathUtils.clamp(mario.position.x, camera.viewportWidth / 2f,  map.getWidth() - camera.viewportWidth / 2f);
        camera.position.y = MathUtils.clamp(mario.position.y, camera.viewportHeight / 2f,  map.getHeight() - camera.viewportHeight / 2f);
        camera.update();
    }

    void updateMario(float delta) {
        mario.stateTime += delta;

        if(mario.velocity.x > 0 && mario.grounded) {
            mario.setState(Mario.State.Damping);
        }

        if (Controls.isRightPressed()) {
            mario.velocity.x = mario.maxVelocity.x;
            if (mario.grounded) mario.setState(Mario.State.Walking);
            mario.direction = Mario.Direction.Right;
        }
        if (Controls.isLeftPressed()) {
            mario.velocity.x = -mario.maxVelocity.x;
            if (mario.grounded) mario.setState(Mario.State.Walking);
            mario.direction = Mario.Direction.Left;
        }
        if(Controls.isJumpPressed() && mario.grounded) {
            mario.velocity.y = mario.maxVelocity.y;
            mario.setState(Mario.State.Jumping);
            mario.grounded = false;
        }

        if (mario.velocity.x == 0 && mario.grounded) {
            mario.setState(Mario.State.Idle);
        }

        mario.velocity.x *= Mario.DAMPING;
        mario.velocity.y += GRAVITY;

        collide(delta);

        mario.position.x += mario.velocity.x * delta;
        mario.position.y += mario.velocity.y * delta;

        if(mario.velocity.x < 0.1f) {
            mario.velocity.x = 0;
        }
        if(mario.position.y < 0){
            mario.position.y = 0;
            mario.grounded = true;
        }
    }

    void collide(float delta){
        Rectangle startPosition = new Rectangle(mario.position.x, mario.position.y, mario.width, mario.height);
        Rectangle endPosition = new Rectangle(mario.position.x+mario.velocity.x*delta, mario.position.y+mario.velocity.y*delta, mario.width, mario.height);

        map.getTiles(endPosition, tiles, rectPool);

        Rectangle nearestTile = null;
        float nearestTileDst=0, tileDst;
        Vector2 tileCenter = new Vector2();
        Vector2 startCenter = new Vector2();
        startPosition.getCenter(startCenter);
        for (Rectangle tile: tiles) {
            if (nearestTile == null) {
                nearestTile = tile;
                nearestTileDst = tile.getCenter(tileCenter).dst2(startCenter);
            } else {
                tileDst = tile.getCenter(tileCenter).dst2(startCenter);
                if(tileDst < nearestTileDst) {
                    nearestTileDst = tileDst;
                }
            }
        }

        nearestTiles.clear();
        for (Rectangle tile: tiles) {
            tileDst = tile.getCenter(tileCenter).dst2(startCenter);
            if(tileDst - nearestTileDst < 0.4f) {
                nearestTiles.add(tile);
            }
        }

        for (Rectangle tile: nearestTiles) {
            int collision = 0; // 0=left 1=right 2=bottom 3=up
            Vector2 collisionPoint = null;

            Vector2 sideMiddle = tile.getCenter(tileCenter).add(-tile.width / 2, 0);
            float nearestSideDst = startCenter.dst2(sideMiddle);
            collisionPoint = new Vector2(sideMiddle);

            sideMiddle = tile.getCenter(tileCenter).add(tile.width / 2, 0);
            float sideDst = startCenter.dst2(sideMiddle);
            if (sideDst < nearestSideDst) {
                nearestSideDst = sideDst;
                collision = 1;
                collisionPoint = new Vector2(sideMiddle);
            }

            sideMiddle = tile.getCenter(tileCenter).add(0, -tile.height / 2);
            sideDst = startCenter.dst2(sideMiddle);
            if (sideDst < nearestSideDst) {
                nearestSideDst = sideDst;
                collision = 2;
                collisionPoint = new Vector2(sideMiddle);
            }

            sideMiddle = tile.getCenter(tileCenter).add(0, tile.height / 2);
            sideDst = startCenter.dst2(sideMiddle);
            if (sideDst < nearestSideDst) {
                collision = 3;
                collisionPoint = new Vector2(sideMiddle);
            }

            if (collision == 0) {
                mario.velocity.x = 0;
                mario.position.x = collisionPoint.x - mario.width;
            } else if (collision == 1) {
                mario.velocity.x = 0;
                mario.position.x = collisionPoint.x;
            } else if (collision == 2) {
                mario.velocity.y = 0;
                mario.position.y = collisionPoint.y - mario.height;
            } else if (collision == 3) {
                mario.velocity.y = 0;
                mario.position.y = collisionPoint.y;
                mario.grounded = true;
            }
        }
    }
}
