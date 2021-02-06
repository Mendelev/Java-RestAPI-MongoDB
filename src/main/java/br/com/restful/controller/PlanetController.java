package br.com.restful.controller;

import java.util.ArrayList;


import br.com.restful.dao.PlanetDAO;
import br.com.restful.model.Planet;

public class PlanetController {
	/**
	 * 
	 * Chama o metodo listarTodos da classe PlanetDAO
	 */
	public ArrayList<Planet> listAll() {
		System.out.println("Controller: listarTodos ");
		PlanetDAO dao = new PlanetDAO("test",null);
		return dao.listAll();

	}

	/**
	 * Chama o metodo getById da classe PlanetDAO
	 */
	public Planet searchById(String id) {
		System.out.println("Controller: buscarPorId - " + id);
		PlanetDAO dao = new PlanetDAO("test",null);
		Planet planet = dao.getById(id);
		return planet;
	}
	
	public Planet searchByName(String name) {
		System.out.println("Controller: Searching planet with name - " + name);
		PlanetDAO dao = new PlanetDAO("test",null);
		Planet planet = dao.getByName(name);
		return planet;
	}

	/**
	 * Call method insert on PlanetDAO class
	 */
	public boolean savePlanet(Planet planet) {
		System.out.println("Controller: gravarCliente " + planet.getName());
		PlanetDAO dao = new PlanetDAO("test",null);
		return  dao.insert(planet);
	}

	/**
	 * Chama o metodo update na classe PlanetDAO
	 */
	public boolean updatePlanet(Planet planet) {
		System.out.println("Controller: atualizarCliente " + planet.getName());
		PlanetDAO dao = new PlanetDAO("test",null);
		return dao.update(planet);
	}

	/**
	 * Chama o metodo delete na classe PlanetDAO
	 */
	public boolean deletePlanet(String id) {
		System.out.println("Controller: deletarCliente de id " + id);
		PlanetDAO dao = new PlanetDAO("test",null);
		return dao.delete(id);
	}

}
