-- #tableがあれば削除
DROP TABLE IF EXISTS answers;

-- tableがなければ新しく作成
CREATE TABLE IF NOT EXISTS answers(
challenge_id INT AUTO_INCREMENT,
-- sentiment_type
challenge VARCHAR(50) NOT NULL,
created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY(challenge_id)
);