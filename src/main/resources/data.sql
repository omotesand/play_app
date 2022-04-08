-- challngesテーブル
INSERT INTO challenges(challenge)
            VALUES("プログラミングを勉強してみたら、"),
                  ("初めてやったのがC言語で、"),
                  ("今日の夜ご飯は"),
                  ("最近Twitterを始め"),
                  ("今年の夏は例年より暑くなると");

-- inputsテーブル
INSERT INTO inputs(sentiment_type, input, score)
            VALUES(1, "想像以上におもしろかった（サンプル）", 0.975),
                  (1, "とても素敵な日を過ごした（サンプル）", 0.765),
                  (1, "忘れられない思い出となった（サンプル）", 0.545);

-- sentimentテーブル
INSERT INTO sentiment(sentiment_id, sentiment_type)
            VALUES(1, "Postive");