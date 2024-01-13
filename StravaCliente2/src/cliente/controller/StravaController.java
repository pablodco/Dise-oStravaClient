package cliente.controller;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import cliente.remote.ServiceLocator;

import servidor.dto.EntrenamientoDTO;
import servidor.dto.RetoDTO;


public class StravaController {
	private ServiceLocator serviceLocator;
	
	
	public StravaController(ServiceLocator serviceLocator) {
		this.serviceLocator= serviceLocator;
	}
	public List<RetoDTO> obtenerRetosDisponibles(long token) {
		try {
			return this.serviceLocator.getService().obtenerRetosDisponibles(token);
		} catch (RemoteException e) {
			System.out.println("# Error getting all categories: " + e);
			return null;
		}
	}

	public boolean crearEntrenamiento(long token,long duracion, String titulo, String actividad, double distincia, Date fecha_ini) {
		try {
			return this.serviceLocator.getService().crearEntrenamiento(token,duracion,titulo,actividad,distincia, fecha_ini);
		} catch (RemoteException e) {
			System.out.println("# Error getting all categories: " + e);
			return false;
		}
	}
	public List<EntrenamientoDTO> obtenerEntrenamientos(long token) {
		try {
			return this.serviceLocator.getService().obtenerEntrenamientos(token);
		} catch (RemoteException e) {
			System.out.println("# Error getting articles of a category: " + e);
			return null;
		}
	}

	public boolean aceptarReto(long token, String nombre) {
		try {
			return this.serviceLocator.getService().aceptarReto(token,nombre);
		} catch (RemoteException e) {
			System.out.println("# Error making a bid: " + e);
			return false;
		}
	}
	
	public List<RetoDTO> obtenerRetosActivos(long token){
		try {
			return this.serviceLocator.getService().obtenerRetosActivos(token);
		} catch (RemoteException e) {
			System.out.println("# Error making a bid: " + e);
			return null;
		}
	}
	
	public boolean crearReto(long token,int objetivo, String descripcion, String nombre,
			Date fecha_ini, Date fecha_fin,String actividades,String tipo) {
		try {
			return this.serviceLocator.getService().crearReto(token,objetivo,descripcion,nombre,fecha_ini, fecha_fin,actividades,tipo);
		} catch (RemoteException e) {
			System.out.println("# Error making a bid: " + e);
			return false;
		}
	}
	public EntrenamientoDTO obtenerEntrenamientoPorTitulo(long token,String titulo){
		try {
			return this.serviceLocator.getService().obtenerEntrenamientoPorTitulo(token,titulo);
		} catch (RemoteException e) {
			System.out.println("# Error making a bid: " + e);
			return null;
		}
	}
	
	public double obtenerPorcentajeDeReto(RetoDTO reto,long token) {
		try {
			return this.serviceLocator.getService().obtenerPorcentajeDeReto(reto,token);
		}catch (RemoteException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	
	
}
