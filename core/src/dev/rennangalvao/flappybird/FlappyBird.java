package dev.rennangalvao.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

	private SpriteBatch batch;
	private  Texture[] passaros;
	private Texture fundo;
	private Texture canoBaixo;
	private Texture canoTopo;
	private Texture gameOver;
	private Random numerosRandomico;
	private BitmapFont fonte;
	private BitmapFont mensagen;
	private Circle passarCirculo;
	private Rectangle rectangleCanoTopo;
	private Rectangle rectangleCanoBaixo;
	//private ShapeRenderer shape;

	// Atributos de configuração
	private float larguraDispositivo;
	private float alturaDispositivo;
	private int estadoJogo = 0; // 0 -> Game não iniciado 1-. game iniciado 2-> Game Over
	private int pontuacao = 0;

	private float variacao = 0;
	private float velocidadeQueda = 0;
	private float posicaoInicialVertical;
	private float posicaoMovimentoCanoHorizontal;
	private float espacoEntreCanos;

	private float deltaTime;

	private float alturaEntreCanosRondomica;
	private boolean marcouPonto = false;

	//Câmera
	private OrthographicCamera camera;
	private Viewport viewport;
	private final float VIRTUAL_WIDTH = 768;
	private final float VIRTUAL_HEIGHT = 1024;




	@Override
	public void create () { // inicializa o game

		batch = new SpriteBatch();

		numerosRandomico = new Random();

		passarCirculo = new Circle();
		//rectangleCanoTopo = new Rectangle();
		//rectangleCanoBaixo = new Rectangle();

		//shape = new ShapeRenderer();

		fonte = new BitmapFont();
		fonte.setColor(Color.WHITE);
		fonte.getData().setScale(6);

		mensagen = new BitmapFont();
		mensagen.setColor(Color.WHITE);
		mensagen.getData().setScale(3);

		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");

		fundo = new Texture("fundo.png");

		gameOver = new Texture("game_over.png");

		canoBaixo = new Texture("cano_baixo.png");
		canoTopo = new Texture("cano_topo.png");


		/********************************************************
		 * Configuração da câmera
		 */

		camera = new OrthographicCamera();
		camera.position.set(VIRTUAL_WIDTH/2, VIRTUAL_HEIGHT/2, 0);
		viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);

		larguraDispositivo = VIRTUAL_WIDTH;
		alturaDispositivo  = VIRTUAL_HEIGHT;

		posicaoInicialVertical = alturaDispositivo / 2;
		posicaoMovimentoCanoHorizontal = larguraDispositivo - 100;
		espacoEntreCanos = 250;


	}

	@Override
	public void render () { // loop

		camera.update();

		//limpar frames anteriores
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		// bater assas
		deltaTime = Gdx.graphics.getDeltaTime();
		variacao += deltaTime * 10;
		if(variacao > 2) variacao = 0;

		// Inicia game
		if (estadoJogo == 0 ){ //Não iniciado
			if ( Gdx.input.justTouched() ) {
				estadoJogo = 1;
			}

		} else { //iniciado

			//Velocidade
			velocidadeQueda ++;

			//queda passaro não passar da tela
			if(posicaoInicialVertical > 0 || velocidadeQueda < 0)
				posicaoInicialVertical = posicaoInicialVertical - velocidadeQueda;

			if ( estadoJogo == 1 ) {

				//Decrementar movimento canos
				posicaoMovimentoCanoHorizontal -= deltaTime * 200;

				// Toque de tela*
				if (Gdx.input.justTouched()) {
					velocidadeQueda = -15;
				}

				// Verifca se o cano saiu inteiramente da tela.
				if (posicaoMovimentoCanoHorizontal < -canoTopo.getWidth()) {
					posicaoMovimentoCanoHorizontal = larguraDispositivo;
					alturaEntreCanosRondomica = numerosRandomico.nextInt(400) - 200;
					marcouPonto = false;
				}

				//Verifica pontuação
				if (posicaoMovimentoCanoHorizontal < 120) {
					if (!marcouPonto) {
						pontuacao++;
						marcouPonto = true;
					}
				}
			}else { //Game Over

				if (Gdx.input.justTouched() ){
					estadoJogo = 0;
					pontuacao = 0;
					velocidadeQueda = 0;
					posicaoInicialVertical = alturaDispositivo / 2;
					posicaoMovimentoCanoHorizontal = larguraDispositivo - 100;

				}
			}
		}
		//Configuração dados de projeção da camera
		batch.setProjectionMatrix( camera.combined );
		//Abertura looop
		batch.begin();

		// img fundo
		batch.draw( fundo, 0,0, larguraDispositivo, alturaDispositivo );

		// CanoTop
		batch.draw( canoTopo, posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2  + alturaEntreCanosRondomica);
		batch.draw( canoBaixo, posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaEntreCanosRondomica);

		// Movimento troca imagns - taber assas
		batch.draw(passaros[ ( int )variacao ], 120, posicaoInicialVertical);


		//Mostrar pontuação na tela
		fonte.draw(batch, String.valueOf(pontuacao), larguraDispositivo / 2, alturaDispositivo - 50);

		//GameOver
		if ( estadoJogo == 2 ){
			batch.draw(gameOver, larguraDispositivo / 2 - gameOver.getWidth() / 2, alturaDispositivo / 2);
			mensagen.draw(batch, "Toque para reiniciar", larguraDispositivo / 2 - 200, alturaDispositivo / 2 - gameOver.getHeight() / 2);
		}

		//fechamento
		batch.end();

		//Detectando colisão
		passarCirculo.set( 120 + passaros[0].getWidth() /2, posicaoInicialVertical + passaros[0].getHeight() / 2, passaros[0].getWidth() / 2);
		rectangleCanoBaixo = new Rectangle(
				posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaEntreCanosRondomica,
				canoBaixo.getWidth(), canoBaixo.getHeight()
		);
		rectangleCanoTopo = new Rectangle(
				posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2  + alturaEntreCanosRondomica, canoTopo.getWidth(), canoTopo.getHeight()
		);

		// Desenhar formas
		/*shape.begin( ShapeRenderer.ShapeType.Filled);
		shape.circle( passarCirculo.x, passarCirculo.y, passarCirculo.radius );
		shape.rect(rectangleCanoBaixo.x, rectangleCanoBaixoy, rectangleCanoBaixo.width, rectangleCanoBaixo.height);
		shape.rect(rectangleCanoTopo.x, rectangleCanoTopo.y, rectangleCanoTopo.width, rectangleCanoTopo.height);
		shape.setColor(Color.RED);
		shape.end();
		*/

		//Colisão
		if ( Intersector.overlaps(passarCirculo, rectangleCanoBaixo) || Intersector.overlaps(passarCirculo, rectangleCanoTopo) || posicaoInicialVertical >= alturaDispositivo || posicaoInicialVertical <= 0){
			estadoJogo = 2;
		}

	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}
}
