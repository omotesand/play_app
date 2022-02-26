package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entity.Play;
import app.repository.PlayDao;

@Service
public class PlayService {

	private final PlayDao dao;

	@Autowired
	public PlayService(PlayDao dao) {
		this.dao = dao;
	}

	public Play getText() {
		return dao.getText();
	}

//	public List<Play> getText() {
//		return dao.findAll();
//	}
}