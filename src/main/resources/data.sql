INSERT INTO member (id, github_user_id, crew_nickname, github_profile_image_url)
VALUES
    (1, 100001, '타스', 'https://api.dicebear.com/9.x/shapes/svg?seed=tas'),
    (2, 100002, '모루', 'https://api.dicebear.com/9.x/shapes/svg?seed=moru'),
    (3, 100003, '배찌', 'https://api.dicebear.com/9.x/shapes/svg?seed=baezzi'),
    (4, 100004, '히츠', 'https://api.dicebear.com/9.x/shapes/svg?seed=hits'),
    (5, 100005, '더비', 'https://api.dicebear.com/9.x/shapes/svg?seed=derby'),
    (6, 100006, '도리', 'https://api.dicebear.com/9.x/shapes/svg?seed=dori');

INSERT INTO gathering (id, name, head_count, gathering_datetime, due_date, description, status, member_id)
VALUES
    (1, '잠실 보드게임 번개', 7, '2026-05-23 19:00:00', '2026-05-23 17:00:00', '가볍게 보드게임 하고 저녁도 먹어요.', 'RECRUITING', 1),
    (2, '선릉 점심 메이트', 5, '2026-05-25 12:30:00', '2026-05-24 22:00:00', '월요일 점심 같이 먹을 크루 모집합니다.', 'RECRUITING', 2),
    (3, '한강 러닝 5K', 4, '2026-05-26 20:00:00', '2026-05-25 23:00:00', '천천히 5K 뛰고 음료 한잔해요.', 'RECRUITING', 3),
    (4, '회고 스터디', 3, '2026-05-24 15:00:00', '2026-05-23 23:00:00', '이번 주 미션 회고를 같이 나눠요.', 'MATCHED', 4);

INSERT INTO participant (id, gathering_id, member_id, join_time)
VALUES
    (1, 1, 1, '2026-05-22 10:00:00'),
    (2, 2, 2, '2026-05-22 10:10:00'),
    (3, 2, 3, '2026-05-22 10:20:00'),
    (4, 2, 4, '2026-05-22 10:30:00'),
    (5, 3, 3, '2026-05-22 11:00:00'),
    (6, 3, 5, '2026-05-22 11:15:00'),
    (7, 4, 4, '2026-05-22 12:00:00'),
    (8, 4, 5, '2026-05-22 12:10:00'),
    (9, 4, 6, '2026-05-22 12:20:00');

ALTER TABLE member ALTER COLUMN id RESTART WITH 100;
ALTER TABLE gathering ALTER COLUMN id RESTART WITH 100;
ALTER TABLE participant ALTER COLUMN id RESTART WITH 100;
