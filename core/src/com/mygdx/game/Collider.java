package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.characters.GameCharacter;


public class Collider {
    public enum Side {
        LEFT, RIGHT, TOP, BOTTOM;
    }

    public class Collision {
        Side side;
        Vector2 point;
        Collision(Side side, Vector2 point){
            this.side = side;
            this.point = point;
        }
    }


    private Array<Rectangle> tiles = new Array<Rectangle>();
    private Array<Rectangle> nearestTiles = new Array<Rectangle>();
    private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject() {
            return new Rectangle();
        }
    };

    Map map;
    Array<GameCharacter> gameCharacters;

    Collider(Map map, Array<GameCharacter> gameCharacters){
        this.map = map;
        this.gameCharacters = gameCharacters;
    }

    void collide(GameCharacter character, float delta) {
        Rectangle startPosition = new Rectangle(character.position.x, character.position.y, character.width, character.height);
        Rectangle endPosition = new Rectangle(character.position.x + character.velocity.x * delta, character.position.y + character.velocity.y * delta, character.width, character.height);

        // get tiles that overlap endPosition
        map.getTiles(endPosition, tiles, rectPool);

        // get minimum dinstance between startCenter and tiles that overlap
        Rectangle nearestTile = null;
        float nearestTileDst = 0, tileDst;
        Vector2 tileCenter = new Vector2();
        Vector2 startCenter = new Vector2();
        startPosition.getCenter(startCenter);
        for (Rectangle tile : tiles) {
            if (nearestTile == null) {
                nearestTile = tile;
                nearestTileDst = tile.getCenter(tileCenter).dst2(startCenter);
            } else {
                tileDst = tile.getCenter(tileCenter).dst2(startCenter);
                if (tileDst < nearestTileDst) {
                    nearestTileDst = tileDst;
                }
            }
        }

        // get nearest tiles
        nearestTiles.clear();
        for (Rectangle tile : tiles) {
            tileDst = tile.getCenter(tileCenter).dst2(startCenter);
            if (tileDst - nearestTileDst < 0.4f) {
                nearestTiles.add(tile);
            }
        }

        // get collisionPoint and Side
        for (Rectangle tile : nearestTiles) {
            Collision collision = getCollision(tile, startCenter);

            character.onCollision(collision.side, collision.point);
        }
    }

    Collision getCollision(Rectangle rectangle, Vector2 point){
        Vector2 rectangleCenter = new Vector2();
        Vector2 sideMiddle = rectangle.getCenter(rectangleCenter).add(-rectangle.width / 2, 0);
        float nearestSideDst = point.dst2(sideMiddle);
        Side side = Side.LEFT;
        Vector2 collisionPoint = new Vector2(sideMiddle);

        sideMiddle = rectangle.getCenter(rectangleCenter).add(rectangle.width / 2, 0);
        float sideDst = point.dst2(sideMiddle);
        if (sideDst < nearestSideDst) {
            nearestSideDst = sideDst;
            side = Side.RIGHT;
            collisionPoint = new Vector2(sideMiddle);
        }

        sideMiddle = rectangle.getCenter(rectangleCenter).add(0, -rectangle.height / 2);
        sideDst = point.dst2(sideMiddle);
        if (sideDst < nearestSideDst) {
            nearestSideDst = sideDst;
            side = Side.BOTTOM;
            collisionPoint = new Vector2(sideMiddle);
        }

        sideMiddle = rectangle.getCenter(rectangleCenter).add(0, rectangle.height / 2);
        sideDst = point.dst2(sideMiddle);
        if (sideDst < nearestSideDst) {
            side = Side.TOP;
            collisionPoint = new Vector2(sideMiddle);
        }

        return new Collision(side, collisionPoint);
    }
}
