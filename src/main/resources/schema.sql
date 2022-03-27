-- #timetableがあれば削除
DROP TABLE IF EXISTS playTable;

-- timetableがなければ新しく作成
CREATE TABLE IF NOT EXISTS playTable(
id INT AUTO_INCREMENT,
challenge VARCHAR(50) NOT NULL,
created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY(id)
);