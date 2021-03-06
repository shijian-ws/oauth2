-- 用户信息表
-- DROP TABLE IF EXISTS T_OAUTH2_USER;
CREATE TABLE IF NOT EXISTS T_OAUTH2_USER (
  ID            VARCHAR(32),
  MOBILE        VARCHAR(20),
  EMAIL         VARCHAR(99),
  USERNAME      VARCHAR(20),
  PASSWORD      VARCHAR(32),
  STATUS        TINYINT NOT NULL,
  NICKNAME      TEXT,
  HEAD_IMG_URL  TEXT,
  SEX           TINYINT,
  COUNTRY       TEXT,
  PROVINCE      TEXT,
  CITY          TEXT,
  CREATION_TIME BIGINT  NOT NULL,
  PRIMARY KEY (ID),
  UNIQUE (MOBILE),
  UNIQUE (EMAIL),
  UNIQUE (USERNAME)
);

-- 第三方客户信息表
-- DROP TABLE IF EXISTS T_OAUTH2_CLIENT;
CREATE TABLE IF NOT EXISTS T_OAUTH2_CLIENT (
  ID            VARCHAR(32),
  NAME          VARCHAR(30) NOT NULL,
  CLIENT_ID     VARCHAR(32) NOT NULL,
  CLIENT_SECRET VARCHAR(32) NOT NULL,
  CLIENT_URI    TEXT,
  REDIRECT_URI  TEXT,
  DESCRIPTION   TEXT,
  ICON_URI      TEXT,
  EXPIRES_IN    BIGINT,
  CREATION_TIME BIGINT      NOT NULL,
  PRIMARY KEY (ID),
  UNIQUE (CLIENT_ID)
);

-- 客户与本地用户映射表
-- DROP TABLE IF EXISTS T_OAUTH2_OPEN_INFO;
CREATE TABLE IF NOT EXISTS T_OAUTH2_OPEN_INFO (
  ID            VARCHAR(32),
  CLIENT_ID     VARCHAR(32) NOT NULL,
  USER_ID       VARCHAR(32) NOT NULL,
  CREATION_TIME BIGINT      NOT NULL,
  PRIMARY KEY (ID),
  UNIQUE (CLIENT_ID, USER_ID)
);

-- 授权码信息表
-- DROP TABLE IF EXISTS T_OAUTH2_CODE;
CREATE TABLE IF NOT EXISTS T_OAUTH2_CODE (
  ID           VARCHAR(32),
  CODE         VARCHAR(64) NOT NULL,
  OPEN_ID      VARCHAR(32) NOT NULL,
  CLIENT_ID    VARCHAR(32) NOT NULL,
  SCOPE        TEXT,
  REDIRECT_URI TEXT,
  USE_FLAG     TINYINT,
  EXPIRES_IN   BIGINT      NOT NULL,
  ISSUED_AT    BIGINT      NOT NULL,
  PRIMARY KEY (ID),
  UNIQUE (CODE)
);

-- 请求凭证表
-- DROP TABLE IF EXISTS T_OAUTH2_TOKEN;
CREATE TABLE IF NOT EXISTS T_OAUTH2_TOKEN (
  ID            VARCHAR(32),
  TOKEN_TYPE    VARCHAR(64),
  ACCESS_TOKEN  VARCHAR(64) NOT NULL,
  REFRESH_TOKEN VARCHAR(64) NOT NULL,
  CODE          VARCHAR(64) NOT NULL,
  SCOPE         TEXT,
  REDIRECT_URI  TEXT,
  OPEN_ID       VARCHAR(32) NOT NULL,
  CLIENT_ID     VARCHAR(32) NOT NULL,
  EXPIRES_IN    BIGINT      NOT NULL,
  ISSUED_AT     BIGINT      NOT NULL,
  PRIMARY KEY (ID),
  UNIQUE (CODE),
  UNIQUE (ACCESS_TOKEN),
  UNIQUE  (REFRESH_TOKEN)
);
