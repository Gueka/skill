package net.gueka.skill.service;

import org.springframework.stereotype.Service;

import net.gueka.skill.model.Data;

@Service
public class DataService {

	
	public Data getRandomData() { 
		Data data = new Data();
		return data;
	}
}
