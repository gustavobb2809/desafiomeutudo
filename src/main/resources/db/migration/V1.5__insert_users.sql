INSERT INTO users(name, age, email, password) VALUES("Gustavo", 25, "gustavo.test@test.com", "$2a$12$5Dcx9t4OQ5FgPzWAFiq...GtfELRIrsyf3chNsxjTSfRbMPTSpykq");
INSERT INTO users(name, age, email, password) VALUES("João", 23, "joao.test@test.com", "$2a$12$5Dcx9t4OQ5FgPzWAFiq...GtfELRIrsyf3chNsxjTSfRbMPTSpykq");

CALL insert_account();