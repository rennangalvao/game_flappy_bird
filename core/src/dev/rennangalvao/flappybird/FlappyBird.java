package dev.rennangalvao.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

	private SpriteBatch batch;
	private  Texture[] passaros;
	private Texture fundo;
	private Texture canoBaixoMaior;
	private Texture canoTopoMaior;
	private Random numerosRandomico;
	private BitmapFont fonte;
	private Circle passarCirculo;
	private Rectangle rectangleCanoTopoMaior;
	private Rectangle rectangleCanoBaixoMaior;
	//private ShapeRenderer shape;

	// Atributos de configuração
	private int larguraDispositivo;
	private int alturaDispositivo;
	private int estadoJogo = 0; // 0 -> Game não iniciado 1-. game iniciado
	private int pontuacao = 0;

	private float variacao = 0;
	private float velocidadeQueda = 0;
	private float posicaoInicialVertical;
	private float posicaoMovimentoCanoHorizontal;
	private float espacoEntreCanos;

	private float deltaTime;

	private float alturaEntreCanosRondomica;
	private boolean marcouPonto = false;



	@Override
	public void create () { // inicializa o game

		batch = new SpriteBatch();

		numerosRandomico = new Random();

		passarCirculo = new Circle();
		//rectangleCanoTopoMaior = new Rectangle();
		//rectangleCanoBaixoMaior = new Rectangle();

		//shape = new ShapeRenderer();

		fonte = new BitmapFont();
		fonte.setColor(Color.WHITE);
		fonte.getData().setScale(6);

		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");

		fundo = new Texture("fundo.png");

		canoBaixoMaior = new Texture("cano_baixo_maior.png");
		canoTopoMaior = new Texture("cano_topo_maior.png");

		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo  = Gdx.graphics.getHeight();
		posicaoInicialVertical = alturaDispositivo / 2;
		posicaoMovimentoCanoHorizontal = larguraDispositivo - 100;
		espacoEntreCanos = 300;


	}

	@Override
	public void render () { // loop


		// bater assas
		deltaTime = Gdx.graphics.getDeltaTime();
		variacao += deltaTime * 10;
		if(variacao > 2) variacao = 0;

		// Inicia game
		if (estadoJogo == 0 ){ //Não iniciado
			if ( Gdx.input.justTouched() ) {
				estadoJogo = 1;
			}
		} else {

			//Decrementar
			posicaoMovimentoCanoHorizontal -= deltaTime * 200;

			//Velocidade
			velocidadeQueda ++;

			//queda passaro
			if(posicaoInicialVertical > 0 || velocidadeQueda < 0)
				posicaoInicialVertical = posicaoInicialVertical - velocidadeQueda;

			// Toque de tela*
			if ( Gdx.input.justTouched() ){
				velocidadeQueda = -10;
			}

			// Verifca se o cano saiu inteiramente da tela.
			if (posicaoMovimentoCanoHorizontal < - canoTopoMaior.getWidth() ){
				posicaoMovimentoCanoHorizontal = larguraDispositivo;
				alturaEntreCanosRondomica = numerosRandomico.nextInt(400) - 200;
				marcouPonto = false;
			}

			//Verifica pontuação
			if (posicaoMovimentoCanoHorizontal < 120 ){
				if ( !marcouPonto ){
					pontuacao ++;
					marcouPonto = true;
				}
			}
		}

		//Abertura looop
		batch.begin();

		// img fundo
		batch.draw( fundo, 0,0, larguraDispositivo, alturaDispositivo );

		// CanoTop
		batch.draw( canoTopoMaior, posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2  + alturaEntreCanosRondomica);
		batch.draw( canoBaixoMaior, posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 - canoBaixoMaior.getHeight() - espacoEntreCanos / 2 + alturaEntreCanosRondomica);

		// Movimento troca imagns - taber assas
		batch.draw(passaros[ ( int )variacao ], 120, posicaoInicialVertical);


		//Mostrar pontuação na tela
		fonte.draw(batch, String.valueOf(pontuacao), larguraDispositivo / 2, alturaDispositivo - 50);

		//fechamento
		batch.end();

		//Detectando colisão
		passarCirculo.set( 120 + passaros[0].getWidth() /2, posicaoInicialVertical + passaros[0].getHeight() / 2, passaros[0].getWidth() / 2);
		rectangleCanoBaixoMaior = new Rectangle(
				posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 - canoBaixoMaior.getHeight() - espacoEntreCanos / 2 + alturaEntreCanosRondomica,
				canoBaixoMaior.getWidth(), canoBaixoMaior.getHeight()
		);
		rectangleCanoTopoMaior = new Rectangle(
				posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2  + alturaEntreCanosRondomica, canoTopoMaior.getWidth(), canoTopoMaior.getHeight()
		);

		// Desenhar formas
		/*shape.begin( ShapeRenderer.ShapeType.Filled);
		shape.circle( passarCirculo.x, passarCirculo.y, passarCirculo.radius );
		shape.rect(rectangleCanoBaixoMaior.x, rectangleCanoBaixoMaior.y, rectangleCanoBaixoMaior.width, rectangleCanoBaixoMaior.height);
		shape.rect(rectangleCanoTopoMaior.x, rectangleCanoTopoMaior.y, rectangleCanoTopoMaior.width, rectangleCanoTopoMaior.height);
		shape.setColor(Color.RED);
		shape.end();
		*/

		//Teste de colisão
		if ( Intersector.overlaps(passarCirculo, rectangleCanoBaixoMaior) || Intersector.overlaps(passarCirculo, rectangleCanoTopoMaior) ){
			Gdx.app.log("Colisão", "Houve Colisão");
		}

	}

}
