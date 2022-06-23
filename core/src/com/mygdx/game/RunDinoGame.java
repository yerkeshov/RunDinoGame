package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.screens.EndScreen;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.screens.TitleScreen;

public class RunDinoGame extends Game {
	public static final String TITLE = "Run Dino Game";
	public static final int V_WIDTH = 1334;
	public static final int V_HEIGHT = 750;

	public OrthographicCamera camera;
	public SpriteBatch batch;

	public BitmapFont font;

	public TitleScreen titleScreen;
	public PlayScreen playScreen;
	public EndScreen endScreen;

	public Sound jumpSound;
	public Sound deadSound;
	public Sound scoreSound;
	public Music menuMusic;
	public Music gameMusic;

	@Override
	public void create() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, V_WIDTH, V_HEIGHT);
		batch = new SpriteBatch();

		font = new BitmapFont();
		font.setColor(Color.BLACK);

		titleScreen = new TitleScreen(this);
		playScreen = new PlayScreen(this);
		endScreen = new EndScreen(this);

		// load the jump sound effect and the dead sound effect
		jumpSound = Gdx.audio.newSound(Gdx.files.internal("jumping.wav"));
		deadSound = Gdx.audio.newSound(Gdx.files.internal("dead.wav"));
		scoreSound = Gdx.audio.newSound(Gdx.files.internal("scoreup.wav"));
		menuMusic = Gdx.audio.newMusic(Gdx.files.internal("menu.mp3"));
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("game.mp3"));

		this.setScreen(titleScreen);
	}

	@Override
	public void render() {
		super.render();

		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
		titleScreen.dispose();
		playScreen.dispose();
		endScreen.dispose();
		jumpSound.dispose();
		deadSound.dispose();
		gameMusic.dispose();
		menuMusic.dispose();
	}
}
