package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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

    void getTiles(int startX, int startY, int endX, int endY, Array<Rectangle> tiles, Pool<Rectangle> rectPool) {
        rectPool.freeAll(tiles);
        tiles.clear();
        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                TiledMapTileLayer.Cell cell = layerGround.getCell(x, y);
                if (cell != null) {
                    Rectangle rect = rectPool.obtain();
                    rect.set(x, y, 1, 1);
                    tiles.add(rect);
                }
            }
        }
    }

    Rectangle getTile(Vector2 start, Vector2 end, float width, float height){
//        System.out.println("FINDING " + start + "  ->>> " + end);
        Vector2 step = new Vector2(end).sub(start);
//        System.out.println("STEEEEEPPPP " + step);
        if(Math.abs(step.x) > 0 || Math.abs(step.y) > 0) {
            step.scl(1 / Math.max(Math.abs(step.x), Math.abs(step.y)));
        }
//        System.out.println("STEEEEEPPPP " + step);
        int posX, posY;
        TiledMapTileLayer.Cell cell;
        start.x = Math.round(start.x);
        start.y = Math.round(start.y);
        end.x = Math.round(end.x);
        end.y = Math.round(end.y);

        while(!start.equals(end)){
            posX = (int) start.x;
            posY = (int) start.y;
//            System.out.println("MIRANDO  " + posX + " , " + posY);
            cell = layerGround.getCell(posX, posY);
            if (cell != null) {
                Rectangle rect = new Rectangle();
                rect.set(posX, posY, 1, 1);
//                System.out.println("cell === " + rect);
                return rect;
            }

            posX = (int) (start.x + width);
            posY = (int) start.y;
//            System.out.println("MIRANDO  " + posX + " , " + posY);
            cell = layerGround.getCell(posX, posY);
            if (cell != null) {
                Rectangle rect = new Rectangle();
                rect.set(posX, posY, 1, 1);
//                System.out.println("cell === " + rect);
                return rect;
            }

            posX = (int) start.x;
            posY = (int) (start.y + height);
//            System.out.println("MIRANDO  " + posX + " , " + posY);
            cell = layerGround.getCell(posX, posY);
            if (cell != null) {
                Rectangle rect = new Rectangle();
                rect.set(posX, posY, 1, 1);
//                System.out.println("cell === " + rect);
                return rect;
            }

            posX = (int) (start.x + width);
            posY = (int) (start.y + height);
//            System.out.println("MIRANDO  " + posX + " , " + posY);
            cell = layerGround.getCell(posX, posY);
            if (cell != null) {
                Rectangle rect = new Rectangle();
                rect.set(posX, posY, 1, 1);
//                System.out.println("cell === " + rect);
                return rect;
            }

            start.add(step);
        }

        return null;
    }

    int getWidth() {
        return layerGround.getWidth();
    }

    int getHeight() {
        return layerGround.getHeight();
    }
}
