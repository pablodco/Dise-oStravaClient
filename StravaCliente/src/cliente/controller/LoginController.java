package cliente.controller;

import java.rmi.RemoteException;

import cliente.remote.ServiceLocator;
public class LoginController{
	
private ServiceLocator serviceLocator;
private long token = -1; //-1 = login has not been done or fails


public LoginController(ServiceLocator serviceLocator) {
	this.serviceLocator = serviceLocator;
}

public boolean login(String email, String password) {
	try {
		this.token = this.serviceLocator.getService().login(email, password);			
		return true;
	} catch (RemoteException e) {
		System.out.println("# Error during login: " + e);
		this.token = -1;
		return false;
	}
}

public long registro(String email, String password, String nombre, String fecha_nac, int peso_kilo,
int altura, int frecuencia_card, int frecuencia_card_max) {
	try {
		this.token = this.serviceLocator.getService().registro(email, password,nombre,fecha_nac,peso_kilo,altura,frecuencia_card,frecuencia_card_max);			
		return token;
	} catch (RemoteException e) {
		System.out.println("# Error during login: " + e);
		this.token = -1;
		return -1;
	}
}

public void logout() {
	try {
		this.serviceLocator.getService().logout(this.token);
		this.token = -1;
	} catch (RemoteException e) {
		System.out.println("# Error during logout: " + e);
	}
}

public long getToken() {
	return token;
}
}