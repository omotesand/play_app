package app.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import app.entity.Play;

@Mapper
public interface PlayDao {
	Play findChallenge();

	void insert(Play play);

	//BigDecimal[] findScore();
	List<Play> findRank();
}