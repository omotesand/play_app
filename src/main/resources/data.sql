-- challngesテーブル
INSERT INTO challenges(challenge)
            VALUES("プログラミングを勉強してみたら、"),
                  ("初めてやったのがC言語で、"),
                  ("今日の夜ご飯は、"),
                  ("最近Twitterを始め、"),
                  ("今年の夏は例年より暑くなると、");

-- inputsテーブル
INSERT INTO inputs(current_challenge_id, sentiment_type, input,                         score)
            VALUES(1,                     1,              "想像以上におもしろかった（サンプル）", 0.975),
                  (2,                     1,              "とても素敵な日を過ごした（サンプル）", 0.765),
                  (3,                     1,              "忘れられない思い出となった（サンプル）", 0.545),
                  (5, 3, "聞いてやや悲観的になっている。", 92.211),
                  (5, 3, "サンプル2", 90.805),
                  (5, 3, "サンプル3", 86.396),
                  (5, 3, "サンプル4", 92.279),
                  (5, 3, "サンプル5", 80.765),
                  (5, 3, "サンプル6", 90.865),
                  (5, 3, "サンプル7", 90.865);

-- sentimentテーブル
INSERT INTO sentiment(sentiment_id, sentiment_type)
            VALUES(1, "Postive"),
                  (2, "Neutral"),
                  (3, "Negative");