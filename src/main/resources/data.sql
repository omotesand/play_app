-- challngesテーブル
INSERT INTO challenges(sentiment_type, challenge)
            VALUES(1, "プログラミングを勉強してみたら、"),
                  (1, "初めてやったのがC言語で"),
                  (1, "今日の夜ご飯は"),
                  (1, "最近Twitterを始め"),
                  (1, "今年の夏は例年より暑くなると"),
                  (3, "明日から連休で"),
                  (3, "古くからの友人に会いに"),
                  (3, "海外旅行中に"),
                  (3, "通勤列車に乗っていたら"),
                  (3, "イベントのチケットを買ったら");

-- inputsテーブル
INSERT INTO inputs(your_challenge_id, your_sentiment_type, input, score)
            VALUES(1, 1, "ポジティブサンプル1", 87.975),
                  (2, 1, "ポジティブサンプル2", 70.765),
                  (3, 1, "ポジティブサンプル3", 70.545),
                  (6, 3, "ネガティブサンプル1", 92.211),
                  (6, 3, "ネガティブサンプル2", 90.805),
                  (6, 3, "ネガティブサンプル3", 86.396),
                  (7, 3, "ネガティブサンプル4", 9.001),
                  (7, 3, "ネガティブサンプル5", 30.001),
                  (7, 3, "ネガティブサンプル6", 90.865),
                  (7, 3, "ネガティブサンプル7", 90.865);

-- sentimentテーブル
INSERT INTO sentiment(sentiment_id, sentiment_type)
            VALUES(1, "Postive"),
                  (2, "Neutral"),
                  (3, "Negative");