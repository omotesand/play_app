package app.repository;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Mapper;

import app.entity.Play;

@Mapper
public interface PlayDao {
	Play findChallenge();

	BigDecimal[] findScore();
}