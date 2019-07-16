package ru.avramovanton.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import ru.avramovanton.base.BaseScreen;


public class MenuScreen<stage> extends BaseScreen {

    private Texture img;
    private Vector2 touch;
    private Vector2 v;
    private Vector2 pos;
    private Vector2 target;
    private Vector2 vSpeed = new Vector2(0.5f, 0.5f);
    private boolean dragEnabled = true;
    private int speed = 1;



    @Override
    public void show() {
        super.show();
        img = new Texture("badlogic.jpg");
        v = new Vector2(0, 0);
        pos = new Vector2((Gdx.graphics.getWidth()-img.getWidth())/2, (Gdx.graphics.getHeight()-img.getHeight())/2);
        System.out.println(pos);
        touch = pos.cpy();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        Gdx.gl.glClearColor(0.26f, 0.5f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        drawimg(batch);
        batch.draw(img, pos.x, pos.y);
        batch.end();
        processTouch();


    }

    @Override
    public void dispose() {
        super.dispose();
        img.dispose();
    }

    @Override
    public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button) {
        super.touchDown(screenX, screenY, pointer, button);
        if (button == Input.Buttons.LEFT) {
            if (isClicked(screenX, screenY)) {
                dragEnabled = true;
                System.out.println(touch);



            }
        }
        return false;
    }

    public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
        super.touchUp(screenX, screenY, pointer, button);
        if (button == Input.Buttons.LEFT) {
            captureTouch(screenX, screenY);
            dragEnabled = false;
            vSpeed.set(touch).sub(pos).setLength(speed);
            //pos = touch;
        }
        return false;
    }

    private boolean isClicked(final float clickX, final float clickY) {
        if (pos == null) return false;
        if (img == null) return false;
        final float realClickY = Gdx.graphics.getHeight() - clickY;
        return (clickX >= pos.x && clickX <= pos.x+img.getWidth() &&
                realClickY >= pos.y && realClickY <= pos.y+img.getHeight());
    }

    private void processTouch() {
        if (pos == null) return;
        if (touch == null) return;
        if (pos.equals(touch)) return;
        final Vector2 vDest = touch.cpy().sub(pos);
        final Vector2 vDir = vDest.cpy().nor();
        final Vector2 vSpeed = vDir.scl(speed);
        if (vSpeed.len() > vDest.len()) {
            pos.set(touch);
        }
        else {
            pos.add(vSpeed);
        }
    }

    public boolean touchDragged(final int screenX, final int screenY, final int pointer) {
        super.touchDragged(screenX, screenY, pointer);
        if (pos == null) return false;
        if (touch == null) return false;
        if (dragEnabled) {
            captureTouch(screenX, screenY);
            pos.set(touch);
        }
        return false;
    }

    private void captureTouch(final int touchX, final int touchY) {
        if (img == null) return;
        touch = new Vector2(   touchX-img.getWidth()/2,
                Gdx.graphics.getHeight() - touchY - img.getHeight()/2);
    }

    private void drawimg( final SpriteBatch batch) {
        if ((img != null) && (pos != null)) {
            batch.draw(img, pos.x, pos.y);
        }
    }

}
