package com.mygdx.game.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.RunDinoGame;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class TitleScreen implements Screen {
    private final RunDinoGame app;

    private Stage stage;
    private Skin skin;

    TextButton.TextButtonStyle textButtonStyle;
    private TextButton buttonPlay, buttonExit;

    // Background image
    private Texture textureBg;

    // Info label
    private Label labelInfo;

    // Author label
    private Label labelAuthor;

    private ShapeRenderer shapeRenderer;

    public TitleScreen(RunDinoGame app) {
        this.app = app;
        this.stage = new Stage(new FitViewport(RunDinoGame.V_WIDTH, RunDinoGame.V_HEIGHT, app.camera));
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {
        System.out.println("Title Screen");
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        this.skin = new Skin();
        this.skin.addRegions(new TextureAtlas(Gdx.files.internal("ui/uiskin.atlas")));
        this.skin.add("default-font", app.font);
        this.skin.load(Gdx.files.internal("ui/uiskin.json"));

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = app.font;
        textButtonStyle.up = skin.getDrawable("default");
        textButtonStyle.down = skin.getDrawable("default");
        textButtonStyle.checked = skin.getDrawable("default");

        textureBg = new Texture("dino_bg2.png");

        // start the playback of the background music immediately
        app.menuMusic.setLooping(true);
        app.menuMusic.play();


        initButtons();
        initInfoLabel();
    }

    private void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);
        app.batch.begin();
        app.batch.draw(textureBg, 0 , 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        app.batch.end();

        stage.draw();
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
        skin.dispose();
        shapeRenderer.dispose();
        textureBg.dispose();
    }

    // Initialize the info label
    private void initInfoLabel() {
        labelInfo = new Label("Welcome to Run Dino Game!\n\nClick on 'Play' to start", skin, "default");
        labelInfo.setPosition(Gdx.graphics.getWidth() / 2 - labelInfo.getWidth() / 2, Gdx.graphics.getHeight() / 2 + 55);
        labelInfo.setAlignment(Align.center);
        labelInfo.addAction(sequence(alpha(0f), delay(.5f), fadeIn(.5f)));
        stage.addActor(labelInfo);

        labelAuthor = new Label("| Bauyrzhan Yerkeshov | yerkeshov@gmail.com |", skin, "default");
        labelAuthor.setPosition(Gdx.graphics.getWidth() - labelAuthor.getWidth() - 10, 10);
        labelAuthor.addAction(sequence(alpha(0f), delay(.5f), fadeIn(.5f)));
        stage.addActor(labelAuthor);
    }

    private void initButtons() {
        buttonPlay = new TextButton("Play", skin, "default");
        buttonPlay.setSize(280, 60);
        buttonPlay.setPosition((int)((Gdx.graphics.getWidth() / 2) - (buttonPlay.getWidth() / 2)), (int) (Gdx.graphics.getHeight() / 2));
        buttonPlay.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.menuMusic.stop();
                app.setScreen(app.playScreen);
            }
        });

        buttonExit = new TextButton("Exit", skin, "default");
        buttonExit.setSize(280, 60);
        buttonExit.setPosition((int)((Gdx.graphics.getWidth() / 2) - (buttonExit.getWidth() / 2)), (int)(Gdx.graphics.getHeight() / 2 - buttonExit.getHeight() - 40));
        buttonExit.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        stage.addActor(buttonPlay);
        stage.addActor(buttonExit);
    }
}
