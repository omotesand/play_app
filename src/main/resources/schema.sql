-- #timetableがあれば削除
DROP TABLE IF EXISTS playtable;

-- timetableがなければ新しく作成
CREATE TABLE IF NOT EXISTS playtable(
id INT AUTO_INCREMENT,
name VARCHAR(5) NOT NULL,
created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY(id)
);