CREATE TABLE balance
(
    id      BIGINT AUTO_INCREMENT NOT NULL,
    date    date                  NOT NULL,
    balance DECIMAL               NULL,
    CONSTRAINT pk_balance PRIMARY KEY (id)
);

CREATE TABLE transaction
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    date            date NOT NULL,
    deposit_amount  DECIMAL NULL,
    withdraw_amount DECIMAL NULL,
    balance         DECIMAL NULL,
    CONSTRAINT pk_transaction PRIMARY KEY (id)
);
