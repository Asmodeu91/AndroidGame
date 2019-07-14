package ru.avramovanton.sprite;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import ru.avramovanton.base.Sprite;
import ru.avramovanton.math.Rect;
import ru.avramovanton.math.Rnd;

public class MainShip extends Sprite {

    private Vector2 vTouch = new Vector2(pos);
    private Vector2 vBuf = new Vector2(pos);
    protected Vector2 vSpeed = new Vector2();
    protected Rect worldBounds;
    private boolean pressedLeft = false;
    private boolean pressedRight = false;
    private boolean pressedUp = false;
    private boolean pressedDown = false;

    public MainShip(TextureAtlas atlas) {
        super(atlas.findRegion("main_ship"));
        this.atlas = atlas;

    }

    @Override
    public void update(final float delta) {
        super.update(delta);
        vBuf.set(vTouch);
        System.out.println(vTouch);
        if (vBuf.sub(pos).len() > vSpeed.len()) {
            pos.add(vSpeed);
        }
        else {
            pos.set(vTouch);
        }

    }

    public boolean keyDown(final int keycode) {
        if (worldBounds == null) return false;
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = true;
                pressedRight = false;
                vTouch.x = worldBounds.getLeft();
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = true;
                pressedLeft = false;
                vTouch.x = worldBounds.getRight();
                break;
            case Input.Keys.W:
            case Input.Keys.UP:
                pressedUp = true;
                pressedDown = false;
                vTouch.y = worldBounds.getTop();
                break;
            case Input.Keys.S:
            case Input.Keys.DOWN:
                pressedDown = true;
                pressedUp = false;
                vTouch.y = worldBounds.getBottom();
                break;
        }
        vSpeed.set(vTouch).sub(pos);//.setLength(shipSpeed);
        return false;
    }

    public boolean keyUp(final int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                if (pressedLeft)
                    vTouch.x = pos.x;
                pressedLeft = false;
                vSpeed.set(vTouch).sub(pos);
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                if (pressedRight)
                    vTouch.x = pos.x;
                pressedRight = false;
                vSpeed.set(vTouch).sub(pos);
                break;
            case Input.Keys.W:
            case Input.Keys.UP:
                if (pressedUp)
                    vTouch.y = pos.y;
                pressedUp = false;
                vSpeed.set(vTouch).sub(pos);
                break;
            case Input.Keys.S:
            case Input.Keys.DOWN:
                if (pressedDown)
                    vTouch.y = pos.y;
                pressedDown = false;
                vSpeed.set(vTouch).sub(pos);
                break;

        }
        return false;
    }

    public boolean touchDown(final Vector2 touch, final int pointer, final int button) {
        return false;
    }

    public boolean touchUp(final Vector2 touch, final int pointer, final int button) {
        if (button == Input.Buttons.LEFT) {
            vTouch.set(touch);
            vSpeed.set(vTouch).sub(pos);

        }
        return false;
    }


    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.25f);
        //setBottom(worldBounds.getBottom() + 0.04f);

    }




}
