package com.pixmeg.catapult.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pixmeg.catapult.controller.CatapultTutorial;
import com.pixmeg.catapult.model.CtptModel;

public class MainScreen implements Screen {
    private CatapultTutorial parent;
    private CtptModel model;

    private static final float W_WIDTH = 800;
    private static final float W_HEIGHT = 480;

    private Viewport viewport;
    private OrthographicCamera camera;
    private ShapeRenderer renderer;
    private Box2DDebugRenderer debugRenderer;

    private static Array<Body> deadBodies;

    public MainScreen(CatapultTutorial catapultTutorial) {
        parent = catapultTutorial;

        camera = new OrthographicCamera();
        viewport = new StretchViewport(W_WIDTH, W_HEIGHT, camera);
        renderer = new ShapeRenderer();

        model = new CtptModel(viewport);
        debugRenderer = new Box2DDebugRenderer();

        deadBodies = new Array<Body>();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(model);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.setProjectionMatrix(camera.combined);
        renderer.setColor(Color.ORANGE);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.circle(model.anchor.x,model.anchor.y,5);
        renderer.setColor(Color.WHITE);
        renderer.circle(model.firingPosition.x,model.firingPosition.y,5);
        renderer.line(model.firingPosition,model.anchor);
        renderer.end();

        debugRenderer.render(model.world, camera.combined);

        model.logicStep(delta);

        if(deadBodies != null) {
            for (Body ballBody : deadBodies) {
                if (ballBody.getPosition().y < 200) {
                    deadBodies.removeValue(ballBody, true);
                    model.world.destroyBody(ballBody);
                }
            }
        }
    }

    public static void addDeadBody(Body body){
        deadBodies.add(body);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
    }
}
