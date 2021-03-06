-- #tableがあれば削除
DROP TABLE IF EXISTS challenges;

-- tableがなければ新しく作成
CREATE TABLE IF NOT EXISTS challenges(
challenge_id   INT          AUTO_INCREMENT,-- お題ID
sentiment_type INT          NOT NULL,      -- 感情タイプ（1:Positive, 2:Nutral, 3:Negative）
challenge      VARCHAR(100) NOT NULL,      -- お題100文字以内
PRIMARY KEY(challenge_id)
);

-- -------------------------------------------------------
DROP TABLE IF EXISTS inputs;

-- tableがなければ新しく作成
CREATE TABLE IF NOT EXISTS inputs(
input_id            INT           AUTO_INCREMENT,-- 投稿ID
your_challenge_id   INT           NOT NULL,      -- challnegsテーブルのchallenge_idと同等
your_sentiment_type INT           NOT NULL,      -- challnegsテーブルのsentiment_typeと同等
input               VARCHAR(120)  NOT NULL,      -- 投稿内容
score               DECIMAL(6, 3) NOT NULL,      -- 感情分析結果スコア
created_at          DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY(input_id)
);
-- ------------------------------------------------------
DROP TABLE IF EXISTS sentiment;

CREATE TABLE IF NOT EXISTS sentiment(
sentiment_id   INT         NOT NULL,-- inputsテーブルのyour_sentiment_typeと同等
sentiment_type VARCHAR(10) NOT NULL,-- 感情タイプ（Positive or Nutral or Negative）
PRIMARY KEY(sentiment_id)
);
