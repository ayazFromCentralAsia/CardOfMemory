DROP TYPE IF EXISTS topic;
DROP TYPE IF EXISTS role;

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TYPE topic AS ENUM (
    'JAVA',
    'SPRING',
    'SQL',
    'ALGORITHMS',
    'DATABASE',
    'OOP',
    'NETWORK',
    'SYSTEM_DESIGN'
);

CREATE TYPE role AS ENUM (
    'ADMIN',
    'USER'
);
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role role NOT NULL,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE cards (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    question TEXT NOT NULL,
    answer TEXT NOT NULL,
    difficulty SMALLINT NOT NULL CHECK (difficulty BETWEEN 1 AND 5),
    topic topic NOT NULL,
    owner_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE card_progress (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    card_id UUID NOT NULL REFERENCES cards(id) ON DELETE CASCADE,
    score SMALLINT NOT NULL CHECK (score BETWEEN 0 AND 10),
    answered VARCHAR(1000),
    known BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP
);
-- Пользователи
INSERT INTO users (username, email, password, role) VALUES
('admin', 'admin@example.com', 'hashed_admin_pass', 'ADMIN'),
('alice', 'alice@example.com', 'hashed_alice_pass', 'USER'),
('bob', 'bob@example.com', 'hashed_bob_pass', 'USER'),
('carol', 'carol@example.com', 'hashed_carol_pass', 'USER'),
('dave', 'dave@example.com', 'hashed_dave_pass', 'USER'),
('eve', 'eve@example.com', 'hashed_eve_pass', 'USER'),
('frank', 'frank@example.com', 'hashed_frank_pass', 'USER'),
('grace', 'grace@example.com', 'hashed_grace_pass', 'USER'),
('heidi', 'heidi@example.com', 'hashed_heidi_pass', 'USER'),
('ivan', 'ivan@example.com', 'hashed_ivan_pass', 'USER');

-- Карточки
INSERT INTO cards (question, answer, difficulty, topic, owner_id) VALUES
('Что такое класс в Java?', 'Шаблон для создания объектов, описывающий их свойства и поведение.', 2, 'JAVA', (SELECT id FROM users WHERE username = 'admin')),
('Что такое интерфейс в Java?', 'Контракт, который класс должен реализовать, описывая методы без тела.', 3, 'JAVA', (SELECT id FROM users WHERE username = 'alice')),
('Что такое Spring Framework?', 'Фреймворк для разработки приложений на Java, поддерживающий инверсию управления и аспектно-ориентированное программирование.', 3, 'SPRING', (SELECT id FROM users WHERE username = 'bob')),
('Как работает Dependency Injection в Spring?', 'Автоматическое предоставление зависимостей объектам, управляемым контейнером Spring.', 4, 'SPRING', (SELECT id FROM users WHERE username = 'carol')),
('Что такое SQL?', 'Язык запросов к реляционным базам данных.', 1, 'SQL', (SELECT id FROM users WHERE username = 'dave')),
('Что такое JOIN в SQL?', 'Операция объединения таблиц по ключевым полям.', 2, 'SQL', (SELECT id FROM users WHERE username = 'eve')),
('Что такое алгоритм сортировки пузырьком?', 'Простой алгоритм сортировки, сравнивающий и меняющий соседние элементы.', 2, 'ALGORITHMS', (SELECT id FROM users WHERE username = 'frank')),
('Что такое сложность алгоритма O(n)?', 'Линейная зависимость времени выполнения от размера входных данных.', 3, 'ALGORITHMS', (SELECT id FROM users WHERE username = 'grace')),
('Что такое нормализация базы данных?', 'Процесс структурирования данных для уменьшения избыточности.', 4, 'DATABASE', (SELECT id FROM users WHERE username = 'heidi')),
('Что такое транзакция в базе данных?', 'Единица работы, которая должна быть выполнена полностью или не выполнена вовсе.', 3, 'DATABASE', (SELECT id FROM users WHERE username = 'ivan')),
('Что такое инкапсуляция в ООП?', 'Сокрытие внутреннего состояния объекта и предоставление доступа через методы.', 2, 'OOP', (SELECT id FROM users WHERE username = 'admin')),
('Что такое наследование в ООП?', 'Механизм, позволяющий создавать новый класс на основе существующего.', 2, 'OOP', (SELECT id FROM users WHERE username = 'alice')),
('Что такое сетевой протокол TCP?', 'Протокол с установлением соединения для надежной передачи данных.', 3, 'NETWORK', (SELECT id FROM users WHERE username = 'bob')),
('Что такое IP-адрес?', 'Уникальный адрес устройства в сети.', 1, 'NETWORK', (SELECT id FROM users WHERE username = 'carol')),
('Что такое микросервисная архитектура?', 'Архитектурный стиль, где приложение состоит из независимых сервисов.', 4, 'SYSTEM_DESIGN', (SELECT id FROM users WHERE username = 'dave')),
('Что такое масштабируемость системы?', 'Способность системы увеличивать производительность при росте нагрузки.', 3, 'SYSTEM_DESIGN', (SELECT id FROM users WHERE username = 'eve')),
('Чем отличается ArrayList от LinkedList в Java?', 'ArrayList хранит данные в массиве, LinkedList — в связном списке.', 3, 'JAVA', (SELECT id FROM users WHERE username = 'frank')),
('Что такое Spring Boot?', 'Проект Spring для быстрого создания автономных приложений.', 2, 'SPRING', (SELECT id FROM users WHERE username = 'grace')),
('Как работает транзакция в Spring?', 'Использование аннотации @Transactional для управления транзакциями.', 4, 'SPRING', (SELECT id FROM users WHERE username = 'heidi')),
('Что такое нормальная форма БД?', 'Правила для структурирования таблиц с целью минимизации дублирования.', 3, 'DATABASE', (SELECT id FROM users WHERE username = 'ivan')),
('Что такое композиция в ООП?', 'Отношение «часть-целое», где объект содержит другой объект.', 3, 'OOP', (SELECT id FROM users WHERE username = 'admin')),
('Что такое DNS?', 'Система доменных имен, переводящая домены в IP-адреса.', 2, 'NETWORK', (SELECT id FROM users WHERE username = 'alice')),
('Что такое CAP теорема?', 'Теорема о невозможности одновременного достижения консистентности, доступности и устойчивости к разделениям.', 4, 'SYSTEM_DESIGN', (SELECT id FROM users WHERE username = 'bob')),
('Что такое хэширование паролей?', 'Процесс преобразования пароля в фиксированного размера строку для безопасности.', 2, 'DATABASE', (SELECT id FROM users WHERE username = 'carol')),
('Что такое абстракция в ООП?', 'Выделение важных характеристик объекта и скрытие деталей реализации.', 2, 'OOP', (SELECT id FROM users WHERE username = 'dave')),
('Что такое HTTP?', 'Протокол передачи гипертекста, основа WWW.', 1, 'NETWORK', (SELECT id FROM users WHERE username = 'eve')),
('Что такое индексация в базе данных?', 'Структура для ускорения поиска данных.', 3, 'DATABASE', (SELECT id FROM users WHERE username = 'frank')),
('Что такое REST?', 'Архитектурный стиль веб-сервисов с использованием HTTP.', 2, 'SPRING', (SELECT id FROM users WHERE username = 'grace')),
('Что такое многопоточность в Java?', 'Выполнение нескольких потоков в рамках одного процесса.', 3, 'JAVA', (SELECT id FROM users WHERE username = 'heidi')),
('Что такое очередь сообщений (Message Queue)?', 'Механизм обмена сообщениями между сервисами.', 3, 'SYSTEM_DESIGN', (SELECT id FROM users WHERE username = 'ivan'));

-- Прогресс по карточкам
INSERT INTO card_progress (user_id, card_id, score, answered, known, created_at) VALUES
-- Alice answers some cards
((SELECT id FROM users WHERE username = 'alice'), (SELECT id FROM cards WHERE question = 'Что такое класс в Java?'), 8, 'Класс — это...', TRUE, NOW()),
((SELECT id FROM users WHERE username = 'alice'), (SELECT id FROM cards WHERE question = 'Что такое интерфейс в Java?'), 7, 'Интерфейс описывает...', TRUE, NOW()),
((SELECT id FROM users WHERE username = 'alice'), (SELECT id FROM cards WHERE question = 'Что такое инкапсуляция в ООП?'), 6, 'Это сокрытие...', TRUE, NOW()),

-- Bob's progress
((SELECT id FROM users WHERE username = 'bob'), (SELECT id FROM cards WHERE question = 'Что такое Spring Framework?'), 9, 'Фреймворк для...', TRUE, NOW()),
((SELECT id FROM users WHERE username = 'bob'), (SELECT id FROM cards WHERE question = 'Что такое сетевой протокол TCP?'), 5, 'TCP — это...', FALSE, NOW()),

-- Carol's progress
((SELECT id FROM users WHERE username = 'carol'), (SELECT id FROM cards WHERE question = 'Как работает Dependency Injection в Spring?'), 7, 'Контейнер внедряет...', TRUE, NOW()),
((SELECT id FROM users WHERE username = 'carol'), (SELECT id FROM cards WHERE question = 'Что такое хэширование паролей?'), 6, 'Пароли преобразуются...', TRUE, NOW()),

-- Dave's progress
((SELECT id FROM users WHERE username = 'dave'), (SELECT id FROM cards WHERE question = 'Что такое SQL?'), 7, 'SQL — язык...', TRUE, NOW()),
((SELECT id FROM users WHERE username = 'dave'), (SELECT id FROM cards WHERE question = 'Что такое абстракция в ООП?'), 5, 'Выделение важных...', FALSE, NOW()),

-- Eve's progress
((SELECT id FROM users WHERE username = 'eve'), (SELECT id FROM cards WHERE question = 'Что такое JOIN в SQL?'), 8, 'JOIN объединяет...', TRUE, NOW()),
((SELECT id FROM users WHERE username = 'eve'), (SELECT id FROM cards WHERE question = 'Что такое HTTP?'), 9, 'Протокол передачи...', TRUE, NOW()),

-- Frank's progress
((SELECT id FROM users WHERE username = 'frank'), (SELECT id FROM cards WHERE question = 'Что такое алгоритм сортировки пузырьком?'), 7, 'Сравнивает и меняет...', TRUE, NOW()),
((SELECT id FROM users WHERE username = 'frank'), (SELECT id FROM cards WHERE question = 'Что такое индексация в базе данных?'), 6, 'Ускоряет поиск...', TRUE, NOW()),

-- Grace's progress
((SELECT id FROM users WHERE username = 'grace'), (SELECT id FROM cards WHERE question = 'Что такое сложность алгоритма O(n)?'), 8, 'Линейная зависимость...', TRUE, NOW()),
((SELECT id FROM users WHERE username = 'grace'), (SELECT id FROM cards WHERE question = 'Что такое REST?'), 7, 'Архитектурный стиль...', TRUE, NOW()),

-- Heidi's progress
((SELECT id FROM users WHERE username = 'heidi'), (SELECT id FROM cards WHERE question = 'Что такое Spring Boot?'), 6, 'Фреймворк для быстрых...', TRUE, NOW()),
((SELECT id FROM users WHERE username = 'heidi'), (SELECT id FROM cards WHERE question = 'Что такое многопоточность в Java?'), 7, 'Выполнение потоков...', TRUE, NOW()),

-- Ivan's progress
((SELECT id FROM users WHERE username = 'ivan'), (SELECT id FROM cards WHERE question = 'Что такое транзакция в базе данных?'), 8, 'Единица работы...', TRUE, NOW()),
((SELECT id FROM users WHERE username = 'ivan'), (SELECT id FROM cards WHERE question = 'Что такое очередь сообщений (Message Queue)?'), 5, 'Обмен сообщениями...', FALSE, NOW());
