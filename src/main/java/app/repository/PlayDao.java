package app.repository;

import org.apache.ibatis.annotations.Mapper;

import app.entity.Play;

@Mapper
public interface PlayDao {
	//DBからテキストデータを一件取得
	//Play getText();
	Play getText();
}