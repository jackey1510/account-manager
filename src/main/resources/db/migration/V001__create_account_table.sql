CREATE TABLE "transaction" (
  "id" uuid PRIMARY KEY,
  "idempotency_key" uuid NOT NULL,
  "amount" numeric(13,2) NOT NULL,
  "currency" varchar(3) NOT NULL DEFAULT 'HKD',
  "debtor_account_number" varchar NOT NULL,
  "creditor_account_numebr" varchar NOT NULL,
  "status" ENUM('PENDING', 'SUCCESS', 'FAIL') NOT NULL DEFAULT 'PENDING' ,
  "created_at" timestamp DEFAULT 'NOW()',
  "updated_at" timestamp DEFAULT 'NOW()'
);


CREATE TABLE "account" (
  "account_number" varchar PRIMARY KEY,
  "balance" numeric(13,2) NOT NULL,
  "currency" varchar(3) NOT NULL DEFAULT 'HKD',
  "created_at" timestamp DEFAULT 'NOW()',
  "updated_at" timestamp DEFAULT 'NOW()'
);

CREATE TABLE "transaction_log" (
  "id" uuid PRIMARY KEY,
  "transaction_id" uuid NOT NULL,
  "status" ENUM('PENDING', 'SUCCESS', 'FAIL') NOT NULL DEFAULT 'PENDING' ,
  "timestamp" timestamp NOT NULL
);

ALTER TABLE "transaction" ADD FOREIGN KEY ("debtor_account_number") REFERENCES "account" ("account_number");

ALTER TABLE "transaction" ADD FOREIGN KEY ("creditor_account_numebr") REFERENCES "account" ("account_number");

ALTER TABLE "transaction_log" ADD FOREIGN KEY ("transaction_id") REFERENCES "transaction" ("id");