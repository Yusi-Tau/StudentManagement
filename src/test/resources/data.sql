INSERT INTO students (name, furigana, nickname, address, live, age, gender, remark)
VALUES ('佐藤太郎', 'サトウタロウ', 'ユシタウ', 'abcd@example.com', '宮城県仙台市', 44, '男', 'とにかく挑戦的'),
        ('佐々木優子', 'ササキユウコ', 'ササック', 'efgh@example.com', '愛知県名古屋市', 30, '女', ''),
        ('青木大輝', 'アオキダイキ', 'マルモン', 'magu@example.com', '石川県金沢市', 19, 'その他', ''),
        ('竹本大介', 'タケモトダイスケ', 'ブルースケ', 'koko@example.com', '長野県軽井沢市', 29, '男', ''),
        ('石村明美', 'イシムラアケミ', 'ピンナ', 'cccc@example.com', '群馬県前橋市', 21, '女', '');

INSERT INTO students_courses (student_id, course_name, start_date, completion_date)
VALUES (1, 'Javaコース', '20250401', '20251231'),
        (2, 'AWSコース', '20250401', '20251231'),
        (3, 'Web制作コース', '20250401', '20251231'),
        (4, 'デザインコース', '20250401', '20251231'),
        (5, 'Webマーケティングコース', '20250401', '20251231');
