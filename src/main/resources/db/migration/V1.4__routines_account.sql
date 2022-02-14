DELIMITER $$
CREATE PROCEDURE insert_account()
BEGIN
  DECLARE user1 INT;
  DECLARE user2 INT;

SELECT id INTO user1 FROM users WHERE email = "gustavo.test@test.com";
SELECT id INTO user2 FROM users WHERE email = "joao.test@test.com";

INSERT INTO accounts(agency, account, balance, status, user_id) VALUES("ag-20221", "ac-20221", 300.00, "on", user1);
INSERT INTO accounts(agency, account, balance, status, user_id) VALUES("ag-20222", "ac-20222", 0.00, "on", user2);
END $$
DELIMITER;