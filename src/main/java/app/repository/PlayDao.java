package app.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import app.entity.Play;

@Mapper
public interface PlayDao {
	//使わない
	//Play findChallenge();

	List<Play> findAllForSelectedSentimentType(int analyzedSentimentType);

	Play findById(int analyzedChallengeId);

	void insert(Play play);

	//BigDecimal[] findScore();
	List<Play> findRank(Play play);

	Play findYourRank(Play play);

	Play findTotalCount(Play play);
}