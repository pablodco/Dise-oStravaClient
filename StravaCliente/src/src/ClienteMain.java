package src;

import java.util.List;

import cliente.controller.LoginController;
import cliente.gui.VentanaLogin;
import cliente.gui.VentanaRetosDisponibles;
import cliente.remote.ServiceLocator;


public class ClienteMain {
	public static void main(String[] args) {
		VentanaLogin v= new VentanaLogin();
		v.setVisible(true);
		ServiceLocator serviceLocator = new ServiceLocator();
		
		//args[0] = RMIRegistry IP
		//args[1] = RMIRegistry Port
		//args[2] = Service Name
		serviceLocator.setService(args[0], args[1], args[2]);
		
		LoginController loginController = new LoginController(serviceLocator);
		
		//Login
		VentanaLogin vLogin= new VentanaLogin();
		//VentanaRetosDisponibles vRetosDisponibles= new VentanaRetosDisponibles(loginController.getToken());
	}
}
