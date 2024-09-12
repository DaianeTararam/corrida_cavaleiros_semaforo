package view;


import controller.CorridaCavaleirosController;
public class CorridaCavaleirosPrincipal {

	public static void main(String[] args) {
			
		for (int i = 1; i <= 4; i++) {
			CorridaCavaleirosController cavaleiro = new CorridaCavaleirosController(i);
			cavaleiro.start();
		}

	}

}
