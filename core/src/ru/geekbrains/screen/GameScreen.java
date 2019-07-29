package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.EnemyPool;
import ru.geekbrains.sprite.Background;
import ru.geekbrains.sprite.Bullet;
import ru.geekbrains.sprite.MainShip;
import ru.geekbrains.sprite.Star;
import ru.geekbrains.utils.EnemyGenerator;

public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 64;

    private TextureAtlas atlas;
    private Texture bg;
    private Background background;

    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private EnemyGenerator enemyGenerator;

    private Star[] starArray;
    private MainShip mainShip;
    private Music music;

    @Override
    public void show() {
        super.show();
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        atlas = new TextureAtlas("textures/mainAtlas.tpack");
        bg = new Texture("textures/bg.png");
        background = new Background(new TextureRegion(bg));
        starArray = new Star[STAR_COUNT];
        for (int i = 0; i < STAR_COUNT; i++) {
            starArray[i] = new Star(atlas);
        }
        bulletPool = new BulletPool();
        enemyPool = new EnemyPool(bulletPool, worldBounds);
        enemyGenerator = new EnemyGenerator(enemyPool, atlas, worldBounds);
        mainShip = new MainShip(atlas, bulletPool);
        music.setLooping(true);
        music.play();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        checkCollisions();
        freeAllDestroyedActiveSprites();
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star:starArray) {
            star.resize(worldBounds);
        }
        mainShip.resize(worldBounds);
    }

    @Override
    public void dispose() {
        atlas.dispose();
        bg.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        music.dispose();
        mainShip.dispose();
        super.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        mainShip.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        mainShip.keyUp(keycode);
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        mainShip.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        mainShip.touchUp(touch, pointer, button);
        return false;
    }

    private void update(float delta) {
        for (Star star:starArray) {
            star.update(delta);
        }
        bulletPool.updateActiveSprites(delta);
        enemyPool.updateActiveSprites(delta);
        mainShip.update(delta);
        enemyGenerator.generate(delta);
    }

    private void checkCollisions() {

        List<EnemyShip> ships = enemyPool.getActiveObjects();
        List<Bullet> bullets = bulletPool.getActiveObjects();


        for (Bullet bullet : bullets) {
            for (EnemyShip ship : ships) {
                if (ship.isContact(bullet) && bullet.getOwner() != ship) {
                    System.out.println("Bullet hit enemy: " + ship. + " with damage:" + bullet.getDamage());
                    ship.hit(bullet.getDamage());
                    bullet.setDestroyed(true);
                    break;
                }
            }
            if (mainShip.isContact(bullet) && bullet.getOwner() != mainShip) {
                System.out.println("Bullet hit main ship: " + mainShip.getHealth() + " with damage:" + bullet.getDamage());
                mainShip.hit(bullet.getDamage());
                bullet.setDestroyed(true);
            }
        }


        for (EnemyShip ship : ships) {
            if (mainShip.isContact(ship)) {
                System.out.println("Enemy ship hit main ship: " + mainShip.getHealth() + " with damage:" + ship.getDamage() * 2);
                mainShip.hit(ship.getDamage() * 2);
                ship.setDestroyed(true);
            }
        }
        if (mainShip.getHealth() <= 0) {
            System.out.println("GAME OVER");    //TODO: Make correct game over
            gameOver = true;
        }

    }

    private void freeAllDestroyedActiveSprites() {
        bulletPool.freeAllDestroyedActiveSprites();
        enemyPool.freeAllDestroyedActiveSprites();
    }

    private void draw() {
        Gdx.gl.glClearColor(0.26f, 0.5f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (Star star:starArray) {
            star.draw(batch);
        }
        mainShip.draw(batch);
        bulletPool.drawActiveSprites(batch);
        enemyPool.drawActiveSprites(batch);
        batch.end();
    }

}
