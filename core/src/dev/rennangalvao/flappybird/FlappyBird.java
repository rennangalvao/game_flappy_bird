package dev.rennangalvao.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

	private SpriteBatch batch;
	private  Texture[] passaros;
	private Texture fundo;
	private Texture canoBaixo;
	private Texture canoTopo;
	private Texture canoBaixoMaior;
	private Texture canoTopoMaior;
	private Random numerosRandomico;

	// Atributos de configuração
	private int larguraDispositivo;
	private int alturaDispositivo;

	private float variacao = 0;
	private float velocidadeQueda = 0;
	private float posicaoInicialVertical;
	private float posicaoMovimentoCanoHorizontal;
	private float espacoEntreCanos;

	private float deltaTime;

	private float alturaEntreCanosRondomica;



	@Override
	public void create () { // inicializa o game

		batch = new SpriteBatch();

		numerosRandomico = new Random();

		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");

		fundo = new Texture("fundo.png");

		canoBaixoMaior = new Texture("cano_baixo_maior.png");
		canoTopoMaior = new Texture("cano_topo_maior.png");
		canoBaixo = new Texture("cano_baixo.png");
		canoTopo = new Texture("cano_topo.png");

		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo  = Gdx.graphics.getHeight();
		posicaoInicialVertical = alturaDispositivo / 2;
		posicaoMovimentoCanoHorizontal = larguraDispositivo - 100;
		espacoEntreCanos = 300;


	}

	@Override
	public void render () { // loop

		deltaTime = Gdx.graphics.getDeltaTime();

		// Incremento
		variacao += deltaTime * 10;
		if(variacao > 2) variacao = 0;

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
		}

		//Abertura looop
		batch.begin();

		// img fundo
		batch.draw( fundo, 0,0, larguraDispositivo, alturaDispositivo );

		// CanoTop
		batch.draw( canoTopoMaior, posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2  + alturaEntreCanosRondomica);
		batch.draw( canoBaixoMaior, posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 - canoBaixoMaior.getHeight() - espacoEntreCanos / 2 + alturaEntreCanosRondomica);

		// Movimento bater asas
		batch.draw(passaros[ ( int )variacao ], 120, posicaoInicialVertical);

		//fechamento
		batch.end();

	}

}
