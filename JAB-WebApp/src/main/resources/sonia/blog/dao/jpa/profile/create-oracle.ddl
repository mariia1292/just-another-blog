CREATE TABLE JAB_COMMENT (ID NUMBER(19) NOT NULL, AUTHORADDRESS VARCHAR2(255) NULL, AUTHORMAIL VARCHAR2(255) NULL, CONTENT VARCHAR2(2048) NOT NULL, AUTHORURL VARCHAR2(255) NULL, AUTHORNAME VARCHAR2(255), SPAM NUMBER(1) default 0 NULL, CREATIONDATE TIMESTAMP NOT NULL, TYPE NUMBER(10) NOT NULL, ENTRY_ID NUMBER(19) NULL, AUTHOR_ID NUMBER(19) NULL, PRIMARY KEY (ID))
CREATE TABLE JAB_CATEGORY (ID NUMBER(19) NOT NULL, CREATIONDATE TIMESTAMP NOT NULL, DESCRIPTION VARCHAR2(255) NULL, NAME VARCHAR2(255) NOT NULL, BLOG_ID NUMBER(19) NULL, PRIMARY KEY (ID))
CREATE TABLE JAB_BLOGHITCOUNT (ID NUMBER(19) NOT NULL, HITDATE DATE NOT NULL, HITCOUNT NUMBER(19) NULL, BLOG_ID NUMBER(19) NULL, PRIMARY KEY (ID))
CREATE TABLE JAB_USER (ID NUMBER(19) NOT NULL, PASSWORD VARCHAR2(255) NOT NULL, ACTIVATIONCODE VARCHAR2(255) NOT NULL, EMAIL VARCHAR2(255) UNIQUE NOT NULL, REGISTRATIONDATE TIMESTAMP NOT NULL, NAME VARCHAR2(255) UNIQUE NOT NULL, GLOBALADMIN NUMBER(1) default 0 NULL, LASTLOGIN TIMESTAMP NULL, SELFMANAGED NUMBER(1) default 0 NULL, DISPLAYNAME VARCHAR2(255) NOT NULL, HOMEPAGE VARCHAR2(255), AVATAR VARCHAR2(255), ACTIVE NUMBER(1) default 0 NULL, PRIMARY KEY (ID))
CREATE TABLE JAB_TAG (ID NUMBER(19) NOT NULL, NAME VARCHAR2(255) UNIQUE NOT NULL, PRIMARY KEY (ID))
CREATE TABLE JAB_ATTACHMENT (ID NUMBER(19) NOT NULL, FILESIZE NUMBER(19) NOT NULL, CREATIONDATE TIMESTAMP NOT NULL, MIMETYPE VARCHAR2(255) NOT NULL, DESCRIPTION VARCHAR2(255) NULL, NAME VARCHAR2(255) NOT NULL, FILEPATH VARCHAR2(255) NOT NULL, PAGE_ID NUMBER(19) NULL, ENTRY_ID NUMBER(19) NULL, PRIMARY KEY (ID))
CREATE TABLE JAB_ENTRY_JAB_TAG (entries_ID NUMBER(19) NOT NULL, tags_ID NUMBER(19) NOT NULL, PRIMARY KEY (entries_ID, tags_ID))
CREATE TABLE JAB_MEMBER (ID NUMBER(19) NOT NULL, BlogRole NUMBER(10) NOT NULL, REGISTRATIONDATE TIMESTAMP NOT NULL, ENTRYNOTIFY NUMBER(1) default 0 NULL, COMMENTNOTIFY NUMBER(1) default 0 NULL, BLOG_ID NUMBER(19) NULL, USER_ID NUMBER(19) NULL, PRIMARY KEY (ID))
CREATE TABLE JAB_BLOG (ID NUMBER(19) NOT NULL, DESCRIPTION VARCHAR2(255) NULL, EMAIL VARCHAR2(255) NULL, TITLE VARCHAR2(250) NOT NULL, STARTPAGE VARCHAR2(255) NULL, ENTRIESPERPAGE NUMBER(10) NULL, CREATIONDATE TIMESTAMP NOT NULL, IMAGEHEIGHT NUMBER(10) NULL, TEMPLATE VARCHAR2(255) NOT NULL, IMAGEWIDTH NUMBER(10) NULL, ALLOWCACHING NUMBER(1) default 1 NULL, ALLOWCOMMENTS NUMBER(1) default 0 NULL, ALLOWTRACKBACKS NUMBER(1) default 0 NULL, LOCALE VARCHAR2(255) NULL, ALLOWMACROS NUMBER(1) default 0 NULL, THUMBNAILHEIGHT NUMBER(10) NULL, DATEFORMAT VARCHAR2(50) NOT NULL, THUMBNAILWIDTH NUMBER(10) NULL, ACTIVE NUMBER(1) default 0 NULL, TIMEZONE VARCHAR2(255) NULL, ALLOWHTMLCOMMENTS NUMBER(1) default 0 NULL, SENDAUTOPING NUMBER(1) default 0 NULL, IDENTIFIER VARCHAR2(255) UNIQUE NOT NULL, PRIMARY KEY (ID))
CREATE TABLE JAB_BLOGPARAMETER (ID NUMBER(19) NOT NULL, NAME VARCHAR2(255) NOT NULL, VALUE VARCHAR2(255), BLOG_ID NUMBER(19) NULL, PRIMARY KEY (ID))
CREATE TABLE JAB_ENTRY_JAB_CATEGORY (entries_ID NUMBER(19) NOT NULL, categories_ID NUMBER(19) NOT NULL, PRIMARY KEY (entries_ID, categories_ID))
CREATE TABLE JAB_ENTRY (ID NUMBER(19) NOT NULL, PUBLISHED NUMBER(1) default 0 NULL, ALLOWCOMMENTS NUMBER(1) default 0 NULL, TEASER VARCHAR2(4000) NULL, PUBLISHINGDATE TIMESTAMP NULL, CREATIONDATE TIMESTAMP NOT NULL, TITLE VARCHAR2(255) NOT NULL, LASTUPDATE TIMESTAMP NULL, CONTENT CLOB NULL, AUTHOR_ID NUMBER(19) NULL, BLOG_ID NUMBER(19) NULL, PRIMARY KEY (ID))
CREATE TABLE JAB_PAGE (ID NUMBER(19) NOT NULL, PUBLISHINGDATE TIMESTAMP NULL, NAVIGATIONPOSITION NUMBER(10) NULL, NAVIGATIONTITLE VARCHAR2(255) NOT NULL, PUBLISHED NUMBER(1) default 0 NULL, CREATIONDATE TIMESTAMP NOT NULL, CONTENT CLOB NULL, TITLE VARCHAR2(255) NOT NULL, LASTUPDATE TIMESTAMP NULL, AUTHOR_ID NUMBER(19) NULL, PARENT_ID NUMBER(19) NULL, BLOG_ID NUMBER(19) NULL, PRIMARY KEY (ID))

ALTER TABLE JAB_COMMENT ADD CONSTRAINT FK_JAB_COMMENT_ENTRY_ID FOREIGN KEY (ENTRY_ID) REFERENCES JAB_ENTRY (ID)
ALTER TABLE JAB_COMMENT ADD CONSTRAINT FK_JAB_COMMENT_AUTHOR_ID FOREIGN KEY (AUTHOR_ID) REFERENCES JAB_USER (ID)
ALTER TABLE JAB_CATEGORY ADD CONSTRAINT FK_JAB_CATEGORY_BLOG_ID FOREIGN KEY (BLOG_ID) REFERENCES JAB_BLOG (ID)
ALTER TABLE JAB_BLOGHITCOUNT ADD CONSTRAINT FK_JAB_BLOGHITCOUNT_BLOG_ID FOREIGN KEY (BLOG_ID) REFERENCES JAB_BLOG (ID)
ALTER TABLE JAB_ATTACHMENT ADD CONSTRAINT FK_JAB_ATTACHMENT_PAGE_ID FOREIGN KEY (PAGE_ID) REFERENCES JAB_PAGE (ID)
ALTER TABLE JAB_ATTACHMENT ADD CONSTRAINT FK_JAB_ATTACHMENT_ENTRY_ID FOREIGN KEY (ENTRY_ID) REFERENCES JAB_ENTRY (ID)
ALTER TABLE JAB_ENTRY_JAB_TAG ADD CONSTRAINT FK_JAB_ENTRY_JAB_TAG_tags_ID FOREIGN KEY (tags_ID) REFERENCES JAB_TAG (ID)
ALTER TABLE JAB_ENTRY_JAB_TAG ADD CONSTRAINT JAB_ENTRY_JAB_TAG_entries_ID FOREIGN KEY (entries_ID) REFERENCES JAB_ENTRY (ID)
ALTER TABLE JAB_MEMBER ADD CONSTRAINT FK_JAB_MEMBER_BLOG_ID FOREIGN KEY (BLOG_ID) REFERENCES JAB_BLOG (ID)
ALTER TABLE JAB_MEMBER ADD CONSTRAINT FK_JAB_MEMBER_USER_ID FOREIGN KEY (USER_ID) REFERENCES JAB_USER (ID)
ALTER TABLE JAB_ENTRY_JAB_CATEGORY ADD CONSTRAINT JBENTRYJABCATEGORYcategoriesID FOREIGN KEY (categories_ID) REFERENCES JAB_CATEGORY (ID)
ALTER TABLE JAB_ENTRY_JAB_CATEGORY ADD CONSTRAINT JABENTRYJAB_CATEGORYentries_ID FOREIGN KEY (entries_ID) REFERENCES JAB_ENTRY (ID)
ALTER TABLE JAB_ENTRY ADD CONSTRAINT FK_JAB_ENTRY_BLOG_ID FOREIGN KEY (BLOG_ID) REFERENCES JAB_BLOG (ID)
ALTER TABLE JAB_ENTRY ADD CONSTRAINT FK_JAB_ENTRY_AUTHOR_ID FOREIGN KEY (AUTHOR_ID) REFERENCES JAB_USER (ID)
ALTER TABLE JAB_PAGE ADD CONSTRAINT FK_JAB_PAGE_PARENT_ID FOREIGN KEY (PARENT_ID) REFERENCES JAB_PAGE (ID)
ALTER TABLE JAB_PAGE ADD CONSTRAINT FK_JAB_PAGE_BLOG_ID FOREIGN KEY (BLOG_ID) REFERENCES JAB_BLOG (ID)
ALTER TABLE JAB_PAGE ADD CONSTRAINT FK_JAB_PAGE_AUTHOR_ID FOREIGN KEY (AUTHOR_ID) REFERENCES JAB_USER (ID)
ALTER TABLE JAB_BLOGPARAMETER ADD CONSTRAINT FK_JAB_BLOGPARAMETER_BLOG_ID FOREIGN KEY (BLOG_ID) REFERENCES JAB_BLOG (ID)

CREATE UNIQUE INDEX JAB_BLOGHITCOUNT_BLOG_DATE ON JAB_BLOGHITCOUNT (BLOG_ID, HITDATE)
CREATE UNIQUE INDEX JAB_CATEGORY_BLOG_NAME ON JAB_CATEGORY (BLOG_ID, NAME)
CREATE UNIQUE INDEX JAB_MEMBER_BLOG_USER ON JAB_MEMBER (BLOG_ID, USER_ID)
CREATE UNIQUE INDEX JAB_BLOGPARAMETER_BLOG_NAME ON JAB_BLOGPARAMETER (BLOG_ID, NAME)

CREATE SEQUENCE SEQ_GEN_IDENTITY START WITH 1