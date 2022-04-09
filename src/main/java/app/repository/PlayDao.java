package app.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import app.entity.Play;

@Mapper
public interface PlayDao {
	Play findChallenge();

	void insert(Play play);

	//List<Map<String, Object>> findScore();
	//BigDecimal[] findScore();
	List<Play> findScore();
}