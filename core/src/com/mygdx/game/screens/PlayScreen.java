package com.mygdx.game.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.RunDinoGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayScreen implements Screen{
    // App reference
    private final RunDinoGame app;

    // Stage vars
    private final Stage stage;

    float points = 0;
    int distance = 200;
    Texture obj;
    List<Rectangle> cactus;

    // Dino textures
    Texture texture;
    Texture texture2;
    Texture texture3;
    Texture texture4;
    TextureRegion spr1;
    TextureRegion spr2;
    TextureRegion spr3;
    TextureRegion spr4;

    // Road textures
    Texture road_texture;
    Texture road_texture2;
    Texture road_texture3;
    List<Rectangle> road_list;
    List<Texture> road_texture_arr;


    float speedY = 0;
    Animation<TextureRegion> anim;
    float fds;
    Rectangle pos;
    float speed = 40;
    int gamePlaceY;



    public PlayScreen(RunDinoGame app) {
        this.app = app;
        this.stage = new Stage(new FitViewport(RunDinoGame.V_WIDTH, RunDinoGame.V_HEIGHT, app.camera));
    }

    @Override
    public void show() {
        System.out.println("PLAY");
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        gamePlaceY = (int)(Gdx.graphics.getHeight() / 2 - 80);

        // start the playback of the background music immediately
        app.gameMusic.setLooping(true);
        app.gameMusic.play();
        app.gameMusic.setVolume(0.7f);

        initGame();
    }

    private void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();
        runGame();
    }

    private void initGame(){
        cactus = new ArrayList<>();

        pos = new Rectangle(0,0,40,43);

        obj = new Texture(Gdx.files.internal("obj.png"));


        int x = 400;
        for (int i = 0; i < 100; i++)
        {
            cactus.add(new Rectangle (x, gamePlaceY,23,46));
            x += distance + new Random().nextInt(100);
        }

        // init Dino texture
        texture4 = new Texture(Gdx.files.internal("s1.png"));
        texture2 = new Texture(Gdx.files.internal("s2.png"));
        texture3 = new Texture(Gdx.files.internal("s3.png"));
        texture = new Texture(Gdx.files.internal("s4.png"));
        spr1 = new TextureRegion(texture4,40,43);
        spr2 = new TextureRegion(texture2,40,43);
        spr3 = new TextureRegion(texture3,40,43);
        spr4 = new TextureRegion(texture,40,43);
        anim = new Animation<>(0.05f, spr1,  spr3, spr4);
        //anim.setPlayMode(Animation.PlayMode.LOOP);

        road_list = new ArrayList<>();
        int road_distance_add = 0;
        for (int i = 0; i < 100; i++)
        {
            road_list.add(new Rectangle (road_distance_add, gamePlaceY - 14,150,80));
            road_distance_add += 150;
        }

        road_texture = new Texture(Gdx.files.internal("land1.png"));
        road_texture2 = new Texture(Gdx.files.internal("land2.png"));
        road_texture3 = new Texture(Gdx.files.internal("land3.png"));

        // Add all road texture to one Array
        road_texture_arr = new ArrayList<>();
        road_texture_arr.add(road_texture);
        road_texture_arr.add(road_texture2);
        road_texture_arr.add(road_texture3);

    }

    public void runGame(){
        points = pos.x / 15;
        Gdx.gl.glClearColor(247,247,247,255);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        app.camera.update();
        app.batch.setProjectionMatrix(app.camera.combined);
        app.batch.begin();
        app.font.setColor(Color.BLACK);

        // Draw road
        for (Rectangle road : road_list){
            app.batch.draw(road_texture, road.x, road.y, 150, 80);
        }

        app.font.draw(app.batch, (int) (points) + " points", app.camera.position.x - 10, Gdx.graphics.getHeight() - 100);
        app.batch.draw(anim.getKeyFrame(fds, true), pos.x, pos.y,80,88);

        for (Rectangle r : cactus){
            app.batch.draw(obj, r.x, r.y, 46, 90);
        }

        app.batch.end();
        app.camera.translate( 130 * Gdx.graphics.getDeltaTime(),0);
        fds += Gdx.graphics.getDeltaTime();


        // Jump if player touch screen or pressed SPACE keyboard
        if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            if (pos.y == gamePlaceY) {
                app.jumpSound.play();
                speedY = 560;
            }
        }

        if (pos.y > gamePlaceY)
            speed = 150;
        else
            speed = 140;


        // End game, if player lose
        for (Rectangle r : cactus)
        {
            if (r.getCenter(new Vector2()).dst(pos.getCenter(new Vector2())) < 40)
            {
                app.deadSound.play();
                app.gameMusic.stop();
                app.setScreen(new EndScreen(app, (int) points));
            }
        }

        pos.x += speed * Gdx.graphics.getDeltaTime();

        pos.y += speedY * Gdx.graphics.getDeltaTime();
        speedY -= 1250 * Gdx.graphics.getDeltaTime();

        if (pos.y < gamePlaceY)
        {
            pos.y = gamePlaceY;
            speedY = 0;
        }

        if((int)(points) % 100 == 0){
            app.scoreSound.play();
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        obj.dispose();
        texture.dispose();
        texture2.dispose();
        texture3.dispose();
        texture4.dispose();
        road_texture.dispose();
        road_texture2.dispose();
        road_texture3.dispose();
    }
}
