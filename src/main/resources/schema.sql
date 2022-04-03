-- #tableがあれば削除
DROP TABLE IF EXISTS challenges;

-- tableがなければ新しく作成
CREATE TABLE IF NOT EXISTS challenges(
challenge_id INT AUTO_INCREMENT,
challenge VARCHAR(120) NOT NULL,
PRIMARY KEY(challenge_id)
);

-- -------------------------------------------------------
DROP TABLE IF EXISTS inputs;

-- tableがなければ新しく作成
CREATE TABLE IF NOT EXISTS inputs(
input_id INT AUTO_INCREMENT,
-- sentiment_type
-- input
score DECIMAL(4, 3) NOT NULL,
-- created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY(input_id)
);