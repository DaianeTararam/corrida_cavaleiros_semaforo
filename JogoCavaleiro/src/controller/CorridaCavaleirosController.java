package controller;

import java.util.concurrent.Semaphore;
/*
 * Pontos Relevantes:
 * Há 4 cavaleiros;
 * Há apenas uma tocha;
 * Tocha está em 500m;
 * Pegar a tocha aumenta a velocidade em 2m a cada 50ms;
 * Há uma pedra brilhante a 1500m;
 * Se pegar a pedra, velocidade soma mais 2m;
 * Se já tiver pegado a tocha, não pode pegar a pedra;
 * Há 4 portas no final do caminho;
 * Só uma é a saída, as outras 3 são monstros.
 * 
 */
public class CorridaCavaleirosController extends Thread{
	private int cavaleiro;
	private float velocidade;
	private int distancia;
	private static Semaphore semaforoTocha = new Semaphore(1);
	private static Semaphore semaforoPedra = new Semaphore(1);
	private static Semaphore semaforoPortas = new Semaphore(1);
	private boolean pegouTocha = false;
	private boolean pegouPedra = false;
	private static boolean[] portas = new boolean[4];
	private static boolean ganhador = false;
	
	
	public CorridaCavaleirosController(int cavaleiro) {
		this.cavaleiro = cavaleiro;
		
	}

	@Override
	public void run() {
		corrida();
	}

	private void corrida() {
		velocidade =(int)( Math.random() * 3) + 2;
		distancia = 0;
		try {
			sleep(50);
			while(distancia < 2000) {
				distancia += velocidade;
				if (distancia >= 500 && pegouTocha == false) {
	                if (semaforoTocha.tryAcquire()) {
	                    pegouTocha = true;
	                    velocidade += 2;
	                    System.out.println("O cavaleiro: " + cavaleiro + " pegou a Tocha!!!");
	                }
	            }
	            if (distancia >= 1500 && pegouPedra == false && pegouTocha == true) {
	                if (semaforoPedra.tryAcquire()) {
	                    pegouPedra = true;
	                    velocidade += 2;
	                    System.out.println("O cavaleiro: " + cavaleiro + " pegou a Pedra Brilhante!!!");
	                }
	            }
				if (distancia >= 2000) {
	                System.out.println("O cavaleiro: " + cavaleiro + " chegou ao final e irá escolher a porta.");
	                escolherPorta();
	            }  
            }
			
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
        	if (pegouTocha) semaforoTocha.release();
        	if (pegouPedra) semaforoPedra.release();
        }
    }

	private void escolherPorta() {
		try {
			semaforoPortas.acquire();
			int portaEscolhida;
			do {
				portaEscolhida = (int)(Math.random() * 5);
				
			}while(portas[portaEscolhida]);
			portas[portaEscolhida] = true;
			if (!ganhador && portaEscolhida == 0) {
				System.out.println("O CAVALEIRO: " + cavaleiro + " ENCONTROU A SAÍDA E É O VENCEDOR!!!");
				ganhador = true;
			}else {
				System.out.println("O cavaleiro: " + cavaleiro + " escolheu a porta errada.");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			semaforoPortas.release();
		}
		
	}
}
		
