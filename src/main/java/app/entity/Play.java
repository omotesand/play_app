package app.entity;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class Play {
	private String name;
	private Timestamp created_at;
}