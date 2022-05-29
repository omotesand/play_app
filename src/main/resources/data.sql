-- challngesテーブル
INSERT INTO challenges(sentiment_type, challenge)
            VALUES(1, "プログラミングを勉強してみたら"),
                  (1, "明日は友人と遊ぶ約束を"),
                  (1, "今日の晩ご飯は"),
                  (1, "春に入社した新入社員"),
                  (1, "明日から連休"),
                  (2, "年明け早々に"),
                  (2, "小麦粉の値段が上がる"),
                  (2, "楽しみにしていた映画"),
                  (2, "仕事が終わり家に帰る"),
                  (2, "目覚まし時計の電池を交換"),
                  (3, "今年の夏は例年より暑く"),
                  (3, "古くからの友人に会いに"),
                  (3, "海外旅行中に"),
                  (3, "通勤列車に乗っていたら"),
                  (3, "イベントのチケットを買ったら");

-- inputsテーブル
INSERT INTO inputs(your_challenge_id, your_sentiment_type, input, score)
            VALUES(1, 1, "想像以上に面白く、毎日勉強を続けられた。", 99.950);

-- sentimentテーブル
INSERT INTO sentiment(sentiment_id, sentiment_type)
            VALUES(1, "Postive"),
                  (2, "Neutral"),
                  (3, "Negative");