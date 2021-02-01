package br.com.restful.dao;

import java.util.List;

import br.com.restful.model.Planet;

public interface PlanetDAOinterface {

	public Planet save(Planet planet);

	public boolean delete(Planet planet);

	public boolean update(Planet planet);

	public Planet findById(Planet planet);

	public List<Planet> findByName(Planet planet);

	public List<Planet> findAll();
}
