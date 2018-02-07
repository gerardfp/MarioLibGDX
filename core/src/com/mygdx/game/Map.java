package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by gerard on 05/02/2018.
 */

public class Map {
    OrthographicCamera camera;
    MapRenderer mapRenderer;
    TiledMapTileLayer layerGround;

    Map(OrthographicCamera camera) {
        this.camera = camera;

        TiledMap map = new TmxMapLoader().load("maps/level1.tmx");

        mapRenderer = new OrthogonalTiledMapRenderer(map,GameScreen.PPM);
        layerGround = (TiledMapTileLayer) map.getLayers().get("ground");
    }

    void render() {
        mapRenderer.setView(camera);
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

//    Rectangle getTile(Vector2 start, Vector2 end){
//        float a = end.y - start.y;
//        float b = end.x - start.x;
//
//        int signx = (int) b/ (int)Math.abs(b);
//        int signy = (int) a/ (int)Math.abs(a);
//
//        for (int x = (int)start.x; x*signx<=(int)end.x*signx; x+=signx) {
//            int y = (int) (a/b*(x-start.x)+start.y);
//            TiledMapTileLayer.Cell cell = layerGround.getCell(x, y);
//            if (cell != null) {
//                return new Rectangle(x, y, 1, 1);
//            }
//        }
//
//        for(int y = (int)start.y; y*signy<=(int)end.y*signy; y+=signy) {
//            int x = (int) (b/a*(y-start.y)+start.x);
//            TiledMapTileLayer.Cell cell = layerGround.getCell(x, y);
//            if (cell != null) {
//                return new Rectangle(x, y, 1, 1);
//            }
//        }
//        return null;
//    }

    int getWidth() {
        return layerGround.getWidth();
    }

    int getHeight() {
        return layerGround.getHeight();
    }
}
