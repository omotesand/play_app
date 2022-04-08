-- challngesテーブル
INSERT INTO challenges(challenge)
            VALUES("プログラミングを勉強してみたら、"),
                  ("初めてやったのがC言語で、"),
                  ("今日の夜ご飯は"),
                  ("最近Twitterを始め"),
                  ("今年の夏は例年より暑くなると");

-- inputsテーブル
INSERT INTO inputs(sentiment_type, input, score)
            VALUES(1, "想像以上におもしろかった", 0.975);

-- sentimentテーブル
INSERT INTO sentiment(sentiment_id, sentiment_type)
            VALUES(1, "Postive");