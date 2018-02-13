package com.mygdx.game;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.characters.Coin;
import com.mygdx.game.characters.Goomba;
import com.mygdx.game.characters.Mario;
import com.mygdx.game.screens.GameScreen;

public class Map {
    World world;
    TiledMap map;
    MapRenderer mapRenderer;
    TiledMapTileLayer layerGround;


    Map(World world) {
        this.world = world;

        map = new TmxMapLoader().load("maps/level1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, GameScreen.PPM);
        layerGround = (TiledMapTileLayer) map.getLayers().get("ground");
        loadObjects();

    }

    void loadObjects(){
        for(MapObject mapObject: map.getLayers().get("mario").getObjects()) {
            world.mario = new Mario(world, getObjectPosition(mapObject));
        }

        for(MapObject mapObject: map.getLayers().get("goombas").getObjects()) {
            world.gameCharacters.add(new Goomba(world, getObjectPosition(mapObject)));
        }

        for(MapObject mapObject: map.getLayers().get("coins").getObjects()) {
            world.gameCharacters.add(new Coin(world, getObjectPosition(mapObject)));
        }
    }

    Vector2 getObjectPosition(MapObject mapObject) {
        float x = ((TiledMapTileMapObject) mapObject).getX()*GameScreen.PPM;
        float y = ((TiledMapTileMapObject) mapObject).getY()*GameScreen.PPM;
        return new Vector2(x, y);
    }

    void render() {
        mapRenderer.setView(world.camera);
        mapRenderer.render();
    }

    void getTiles(Rectangle position, Array<Rectangle> tiles, Pool<Rectangle> rectPool) {
        rectPool.freeAll(tiles);
        tiles.clear();
        for (int y = (int) position.y; y <= (int) (position.y + position.height); y++) {
            for (int x = (int) position.x; x <= (int)(position.x + position.width); x++) {
                TiledMapTileLayer.Cell cell = layerGround.getCell(x, y);
                if (cell != null) {
                    Rectangle rect = rectPool.obtain();
                    rect.set(x, y, 1, 1);
                    tiles.add(rect);
                }
            }
        }
    }

    int getWidth() {
        return layerGround.getWidth();
    }

    int getHeight() {
        return layerGround.getHeight();
    }
}
