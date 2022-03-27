package app.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Play {
	private int id;
	private String challenge;
	private LocalDateTime created_at;
}