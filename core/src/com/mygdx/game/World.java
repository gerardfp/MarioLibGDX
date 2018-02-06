package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import sun.java2d.ReentrantContextProviderTL;

/**
 * Created by gerard on 02/02/18.
 */

public class World {
    final float GRAVITY = -0.9f;

    OrthographicCamera camera;

    Map map;
    Mario mario;


    private Pool<Rectangle> rectPool;
    private Array<Rectangle> tiles = new Array<Rectangle>();

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


        updateMario(delta);

        map.render();
        mario.render();

        camera.position.x = MathUtils.clamp(mario.position.x, camera.viewportWidth / 2f,  map.getWidth() - camera.viewportWidth / 2f);
        camera.position.y = MathUtils.clamp(mario.position.y, camera.viewportHeight / 2f,  map.getHeight() - camera.viewportHeight / 2f);
        camera.update();
    }

    void updateMario(float delta) {
        mario.stateTime += delta;

        if(!mario.grounded)
            mario.setState(Mario.State.Damping);

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
        //mario.velocity.y += GRAVITY;

        collide(delta);

        mario.position.x += mario.velocity.x * delta;
        mario.position.y += mario.velocity.y * delta;

        if(mario.position.y < 0){
            mario.position.y = 0;
            mario.grounded = true;
        }
    }

    void collide(float delta) {
        Vector2 start = new Vector2();
        Vector2 end = new Vector2();


        start.x = mario.position.x;
        start.y = mario.position.y;

        end.x = mario.position.x + mario.velocity.x*delta;
        end.y = mario.position.y + mario.velocity.y*delta;

        Rectangle rectangle = map.getTile(start, end, mario.width, mario.height);

        System.out.println("RECTANGLE = " + rectangle);
        System.out.println("ENDDD = " + end.x + "," +end.y);

        if(rectangle != null) {
            if (rectangle.x > end.x) {
                mario.position.x = rectangle.x - mario.width;
            } else if (rectangle.x < end.x){
                mario.position.x = rectangle.x + rectangle.width;
            }

            if (rectangle.y >= end.y) {
                mario.position.y = rectangle.y + rectangle.height;
            } else if (rectangle.y < end.y){
                mario.position.y = rectangle.y - mario.height;
            }

        }
    }

    void collide2(float delta){
        int startX, startY, endX, endY;

        if (mario.direction == Mario.Direction.Right) {
            startX = (int)(mario.position.x + mario.width);
            endX = (int)(mario.position.x + mario.width + mario.velocity.x*delta);
        } else {
            startX = (int)(mario.position.x + mario.velocity.x*delta);
            endX = (int)(mario.position.x);
        }

        startY = (int)(mario.position.y);
        endY = (int)(mario.position.y + mario.height);

        map.getTiles(startX, startY, endX, endY, tiles, rectPool);
        Rectangle firstLeft=null, firstRight=null;
        for(Rectangle tile: tiles) {
            System.out.println("TILEEE " + tile);
            if(mario.direction == Mario.Direction.Right) {
                if (firstRight == null) {
                    firstRight = tile;
                }  else if (tile.x < firstRight.x) {
                    firstRight = tile;
                }
            } else {
                if(firstLeft == null) {
                    firstLeft = tile;
                } else if(tile.x > firstLeft.x) {
                    firstLeft = tile;
                }
            }
        }

        if(firstRight != null) {
            mario.velocity.x = 0;
            mario.position.x = firstRight.x - mario.width;
            System.out.println(" new post " + mario.position.x + " --> " + firstRight + "  - " +mario.width);
        } else if(firstLeft != null) {
            mario.velocity.x = 0;
            mario.position.x = firstLeft.x + firstLeft.width;
        }

        System.out.println("FIRSTRIGHT " + firstRight);

        if (mario.velocity.y > 0) {
            startY = (int)(mario.position.y + mario.height + mario.velocity.y*delta);
            endY = (int)(mario.position.y + mario.height);
        } else {
            startY = (int)(mario.position.y + mario.velocity.y*delta);
            endY = (int)(mario.position.y);
        }

        startX = (int)(mario.position.x);
        endX = (int)(mario.position.x + mario.width);

        map.getTiles(startX, startY, endX, endY, tiles, rectPool);
        Rectangle firstDown=null, firstUp=null;
        for(Rectangle tile: tiles) {
            if(mario.velocity.y > 0) {
                if(firstUp == null) {
                    firstUp = tile;
                } else if(tile.y < firstUp.y) {
                    firstUp = tile;
                }
            } else if (mario.velocity.y < 0){
                if(firstDown == null) {
                    firstDown = tile;
                } else if(tile.y > firstDown.y) {
                    firstDown = tile;
                }
            }
        }

        if(firstUp != null) {
            mario.velocity.y = 0;
            mario.position.y = firstUp.y - mario.height;
        } else if(firstDown != null) {
            mario.velocity.y = 0;
            mario.position.y = firstDown.y + firstDown.height;
            mario.grounded = true;
        }
    }
}
