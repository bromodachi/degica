DROP TABLE IF EXISTS ledger_details;
DROP TABLE IF EXISTS CURRENCY;
DROP TABLE IF EXISTS ledger;
CREATE TABLE ledger(
                       id  BIGSERIAL PRIMARY KEY,
                       name varchar(255) NOT NULL
);


CREATE TABLE currency(
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(4) UNIQUE
);

CREATE TABLE ledger_details(
                               id  BIGSERIAL PRIMARY KEY,
                               ledger_id BIGINT NOT NULL REFERENCES ledger(id) ON DELETE CASCADE,
                               amount DECIMAL NOT NULL,
                               currency_id BIGINT NOT NULL REFERENCES CURRENCY(id) ON DELETE CASCADE,
                               is_credit BOOLEAN NOT NULL,
                               description TEXT,
                               datetime BIGINT NOT NULL,
                               created_at BIGINT NOT NULL,
                               updated_at BIGINT NOT NULL
);
INSERT INTO currency (name) VALUES ('USD');
INSERT INTO currency (name) VALUES ('JPY');
INSERT INTO currency (name) VALUES ('KRW');

INSERT INTO ledger (name) VALUES ('my ledger');
INSERT INTO ledger_details
(ledger_id, amount, currency_id, is_credit, description, datetime, created_at, updated_at)
VALUES (1, 29182.0, 3, false, '전기세', 1643574595000, 1699177816000, 1699177816000);

INSERT INTO ledger_details
(ledger_id, amount, currency_id, is_credit, description, datetime, created_at, updated_at)
VALUES (1, 892.0, 2, true, '払い戻し', 1643574595000, 1699177816000, 1699177816000);

INSERT INTO ledger_details
(ledger_id, amount, currency_id, is_credit, description, datetime, created_at, updated_at)
VALUES (1, 5354.0, 2, false , 'https://docbase.ioからのインボイス"', 1643574595000, 1699177816000, 1699177816000);

INSERT INTO ledger_details
(ledger_id, amount, currency_id, is_credit, description, datetime, created_at, updated_at)
VALUES (1, 4637.0, 2, false , 'ﾔﾏﾀﾞｶｲｼｬ', 1643606995000, 1699177816000, 1699177816000);

INSERT INTO ledger_details
(ledger_id, amount, currency_id, is_credit, description, datetime, created_at, updated_at)
VALUES (1,  66.93, 1, true , 'Refund', 1643621395000, 1699177816000, 1699177816000);

INSERT INTO ledger_details
(ledger_id, amount, currency_id, is_credit, description, datetime, created_at, updated_at)
VALUES (1,  74.6, 1, false , 'Fuel for trip to Steam Offices', 1643621395000, 1699177816000, 1699177816000);

INSERT INTO ledger_details
(ledger_id, amount, currency_id, is_credit, description, datetime, created_at, updated_at)
VALUES (1,  54.18, 1, true , 'REF: #121353abf091285ff727a2649e58ddbae2900918376562abeed49276f', 1643621395000, 1699177816000, 1699177816000);

INSERT INTO ledger_details
(ledger_id, amount, currency_id, is_credit, description, datetime, created_at, updated_at)
VALUES (1,  23.24, 1, false , 'Gas bill', 1643621395000, 1699177816000, 1699177816000);

INSERT INTO ledger_details
(ledger_id, amount, currency_id, is_credit, description, datetime, created_at, updated_at)
VALUES (1,  51.51, 1, true , null, 1643624995000, 1699177816000, 1699177816000);

INSERT INTO ledger_details
(ledger_id, amount, currency_id, is_credit, description, datetime, created_at, updated_at)
VALUES (1,   27.7, 1, false , 'U+1F32D', 1643628595000, 1699177816000, 1699177816000);

CREATE INDEX index_on_ledger_details_ledger_id ON ledger_details(ledger_id, datetime);