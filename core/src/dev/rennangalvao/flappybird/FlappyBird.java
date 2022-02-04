package dev.rennangalvao.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class FlappyBird extends ApplicationAdapter {

	private SpriteBatch batch;
	private  Texture[] passaros;
	private Texture fundo;

	// Atributos de configuração
	private int larguraDispositivo;
	private int alturaDispositivo;

	private float variacao = 0;
	private float velocidadeQueda = 0;
	private float posicaoInicialVertical;



	@Override
	public void create () { // inicializa o game

		batch = new SpriteBatch();

		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");

		fundo = new Texture("fundo.png");

		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo  = Gdx.graphics.getHeight();
		posicaoInicialVertical = alturaDispositivo / 2;
	}

	@Override
	public void render () { // loop

		// Incremento
		variacao += Gdx.graphics.getDeltaTime() * 10;
		if(variacao > 2) variacao = 0;

		//Velocidade
		velocidadeQueda ++;

		//queda passaro
		if(posicaoInicialVertical > 0)
			posicaoInicialVertical = posicaoInicialVertical - velocidadeQueda;

		//Abertura looop
		batch.begin();

		// img fundo
		batch.draw( fundo, 0,0, larguraDispositivo, alturaDispositivo );

		// Movimento
		batch.draw(passaros[ ( int )variacao ], 30, posicaoInicialVertical);

		//fechamento
		batch.end();

	}

}
