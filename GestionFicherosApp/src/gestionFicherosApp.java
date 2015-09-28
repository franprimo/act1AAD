
import gestionficheros.MainGUI;

public class gestionFicherosApp {

	public static void main(String[] args) {
		gestionFicherosImpl getFicherosImpl = new gestionFicherosImpl();
		new MainGUI(getFicherosImpl).setVisible(true);

	}

}
