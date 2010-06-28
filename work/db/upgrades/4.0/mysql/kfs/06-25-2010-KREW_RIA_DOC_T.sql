CREATE TABLE KREW_RIA_DOC_T (
	RIA_ID DECIMAL NOT NULL,
    XML_CONTENT VARCHAR(4000),
    RIA_DOC_TYPE_NAME VARCHAR(100),
    VER_NBR DECIMAL(8) DEFAULT 1 NOT NULL,
	OBJ_ID VARCHAR(36) NOT NULL, 
    CONSTRAINT KREW_RIA_DOC_PK PRIMARY KEY (RIA_ID)
)
/
CREATE TABLE KREW_RIA_DOCTYPE_MAP_T (
	ID DECIMAL NOT NULL,
	RIA_DOC_TYPE_NAME VARCHAR(100),
 	UPDATED_AT DATETIME,
 	RIA_URL VARCHAR(255),
 	HELP_URL VARCHAR(255),
 	EDITABLE CHAR,
	INIT_GROUPS VARCHAR(255),
  	VER_NBR DECIMAL(8) DEFAULT 1 NOT NULL,	
  	OBJ_ID VARCHAR(36) NOT NULL, 
    CONSTRAINT KREW_RIA_DOCTYPE_MAP_PK PRIMARY KEY (ID)
)
/
CREATE TABLE KREW_RIA_DOCTYPE_MAP_ID_S
(
	id bigint(19) not null auto_increment, primary key (id) 
)
/
ALTER TABLE KREW_RIA_DOCTYPE_MAP_ID_S auto_increment = 1000;
/
