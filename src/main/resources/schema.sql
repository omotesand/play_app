-- #timetableがあれば削除
DROP TABLE IF EXISTS play_table;

-- timetableがなければ新しく作成
CREATE TABLE IF NOT EXISTS play_table(
id INT AUTO_INCREMENT,
challenge VARCHAR(50) NOT NULL,
created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY(id)
);