package cliente;

import java.util.List;

import cliente.controller.LoginController;
import cliente.controller.StravaController;
import cliente.gui.VentanaLogin;
import cliente.gui.VentanaRetosDisponibles;
import cliente.remote.ServiceLocator;


public class ClienteMain {
	public static void main(String[] args) {

		ServiceLocator serviceLocator = new ServiceLocator();
		System.out.println("aqui /n");
		//args[0] = RMIRegistry IP
		//args[1] = RMIRegistry Port
		//args[2] = Service Name
		serviceLocator.setService(args[0], args[1], args[2]);
		System.out.println("Aqui2 /n");
		
		LoginController loginController = new LoginController(serviceLocator);
		StravaController stravaController= new StravaController(serviceLocator);
		//Login
		VentanaLogin vLogin= new VentanaLogin(loginController,stravaController);
		vLogin.setVisible(true);
		vLogin.setEnabled(true);
		//VentanaRetosDisponibles vRetosDisponibles= new VentanaRetosDisponibles(loginController.getToken());
	}
}
