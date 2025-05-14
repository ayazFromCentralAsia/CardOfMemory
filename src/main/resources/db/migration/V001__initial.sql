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
    known BOOLEAN DEFAULT FALSE,
    last_reviewed TIMESTAMP
);
