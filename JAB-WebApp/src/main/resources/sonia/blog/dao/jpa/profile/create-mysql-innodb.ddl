CREATE TABLE JAB_CATEGORY (ID BIGINT AUTO_INCREMENT NOT NULL, CREATIONDATE DATETIME NOT NULL, DESCRIPTION VARCHAR(255), NAME VARCHAR(255) NOT NULL, BLOG_ID BIGINT, PRIMARY KEY (ID)) ENGINE InnoDB
CREATE TABLE JAB_COMMENT (ID BIGINT AUTO_INCREMENT NOT NULL, AUTHORADDRESS VARCHAR(255), AUTHORMAIL VARCHAR(255), CONTENT VARCHAR(2048) NOT NULL, AUTHORURL VARCHAR(255), AUTHORNAME VARCHAR(255), SPAM TINYINT(1) default 0, CREATIONDATE DATETIME NOT NULL, TYPE INTEGER NOT NULL, ENTRY_ID BIGINT, AUTHOR_ID BIGINT, PRIMARY KEY (ID)) ENGINE InnoDB
CREATE TABLE JAB_BLOGHITCOUNT (ID BIGINT AUTO_INCREMENT NOT NULL, HITDATE DATE NOT NULL, HITCOUNT BIGINT, BLOG_ID BIGINT, PRIMARY KEY (ID)) ENGINE InnoDB
CREATE TABLE JAB_USER (ID BIGINT AUTO_INCREMENT NOT NULL, PASSWORD VARCHAR(255) NOT NULL, ACTIVATIONCODE VARCHAR(255) NOT NULL, EMAIL VARCHAR(255) UNIQUE NOT NULL, REGISTRATIONDATE DATETIME NOT NULL, NAME VARCHAR(255) UNIQUE NOT NULL, GLOBALADMIN TINYINT(1) default 0, LASTLOGIN DATETIME, SELFMANAGED TINYINT(1) default 0, DISPLAYNAME VARCHAR(255) NOT NULL, HOMEPAGE VARCHAR(255), AVATAR VARCHAR(255), ACTIVE TINYINT(1) default 0, PRIMARY KEY (ID)) ENGINE InnoDB
CREATE TABLE JAB_TAG (ID BIGINT AUTO_INCREMENT NOT NULL, NAME VARCHAR(255) UNIQUE NOT NULL, PRIMARY KEY (ID)) ENGINE InnoDB
CREATE TABLE JAB_ATTACHMENT (ID BIGINT AUTO_INCREMENT NOT NULL, FILESIZE BIGINT NOT NULL, CREATIONDATE DATETIME NOT NULL, MIMETYPE VARCHAR(255) NOT NULL, DESCRIPTION VARCHAR(255), NAME VARCHAR(255) NOT NULL, FILEPATH VARCHAR(255) NOT NULL, ENTRY_ID BIGINT, PAGE_ID BIGINT, PRIMARY KEY (ID)) ENGINE InnoDB
CREATE TABLE JAB_ENTRY_JAB_TAG (entries_ID BIGINT NOT NULL, tags_ID BIGINT NOT NULL, PRIMARY KEY (entries_ID, tags_ID)) ENGINE InnoDB
CREATE TABLE JAB_BLOG (ID BIGINT AUTO_INCREMENT NOT NULL, DESCRIPTION VARCHAR(255), EMAIL VARCHAR(255), TITLE VARCHAR(250) NOT NULL, STARTPAGE VARCHAR(255), ENTRIESPERPAGE INTEGER, CREATIONDATE DATETIME NOT NULL, IMAGEHEIGHT INTEGER, TEMPLATE VARCHAR(255) NOT NULL, IMAGEWIDTH INTEGER, ALLOWCACHING TINYINT(1) default 1, ALLOWCOMMENTS TINYINT(1) default 0,  ALLOWTRACKBACKS TINYINT(1) default 0, LOCALE VARCHAR(255), ALLOWMACROS TINYINT(1) default 0, THUMBNAILHEIGHT INTEGER, DATEFORMAT VARCHAR(50) NOT NULL, THUMBNAILWIDTH INTEGER, ACTIVE TINYINT(1) default 0, TIMEZONE VARCHAR(255), ALLOWHTMLCOMMENTS TINYINT(1) default 0, SENDAUTOPING TINYINT(1) default 0, IDENTIFIER VARCHAR(255) UNIQUE NOT NULL, PRIMARY KEY (ID)) ENGINE InnoDB
CREATE TABLE JAB_BLOGPARAMETER (ID BIGINT AUTO_INCREMENT NOT NULL, NAME VARCHAR(255) NOT NULL, VALUE VARCHAR(255) NOT NULL, BLOG_ID BIGINT, PRIMARY KEY (ID)) ENGINE InnoDB
CREATE TABLE JAB_MEMBER (ID BIGINT AUTO_INCREMENT NOT NULL, BlogRole INTEGER NOT NULL, REGISTRATIONDATE DATETIME NOT NULL, ENTRYNOTIFY TINYINT(1) default 0, COMMENTNOTIFY TINYINT(1) default 0, BLOG_ID BIGINT, USER_ID BIGINT, PRIMARY KEY (ID)) ENGINE InnoDB
CREATE TABLE JAB_ENTRY_JAB_CATEGORY (entries_ID BIGINT NOT NULL, categories_ID BIGINT NOT NULL, PRIMARY KEY (entries_ID, categories_ID)) ENGINE InnoDB
CREATE TABLE JAB_ENTRY (ID BIGINT AUTO_INCREMENT NOT NULL, PUBLISHED TINYINT(1) default 0, ALLOWCOMMENTS TINYINT(1) default 0, TEASER VARCHAR(4000), PUBLISHINGDATE DATETIME, CREATIONDATE DATETIME NOT NULL, TITLE VARCHAR(255) NOT NULL, LASTUPDATE DATETIME, CONTENT TEXT(64000), AUTHOR_ID BIGINT, BLOG_ID BIGINT, PRIMARY KEY (ID)) ENGINE InnoDB
CREATE TABLE JAB_PAGE (ID BIGINT AUTO_INCREMENT NOT NULL, PUBLISHINGDATE DATETIME, NAVIGATIONPOSITION INTEGER, NAVIGATIONTITLE VARCHAR(255) NOT NULL, PUBLISHED TINYINT(1) default 0, CREATIONDATE DATETIME NOT NULL, CONTENT TEXT(64000), TITLE VARCHAR(255) NOT NULL, LASTUPDATE DATETIME, AUTHOR_ID BIGINT, PARENT_ID BIGINT, BLOG_ID BIGINT, PRIMARY KEY (ID)) ENGINE InnoDB

ALTER TABLE JAB_CATEGORY ADD CONSTRAINT FK_JAB_CATEGORY_BLOG_ID FOREIGN KEY (BLOG_ID) REFERENCES JAB_BLOG (ID)
ALTER TABLE JAB_COMMENT ADD CONSTRAINT FK_JAB_COMMENT_ENTRY_ID FOREIGN KEY (ENTRY_ID) REFERENCES JAB_ENTRY (ID)
ALTER TABLE JAB_COMMENT ADD CONSTRAINT FK_JAB_COMMENT_AUTHOR_ID FOREIGN KEY (AUTHOR_ID) REFERENCES JAB_USER (ID)
ALTER TABLE JAB_BLOGHITCOUNT ADD CONSTRAINT FK_JAB_BLOGHITCOUNT_BLOG_ID FOREIGN KEY (BLOG_ID) REFERENCES JAB_BLOG (ID)
ALTER TABLE JAB_ATTACHMENT ADD CONSTRAINT FK_JAB_ATTACHMENT_PAGE_ID FOREIGN KEY (PAGE_ID) REFERENCES JAB_PAGE (ID)
ALTER TABLE JAB_ATTACHMENT ADD CONSTRAINT FK_JAB_ATTACHMENT_ENTRY_ID FOREIGN KEY (ENTRY_ID) REFERENCES JAB_ENTRY (ID)
ALTER TABLE JAB_ENTRY_JAB_TAG ADD CONSTRAINT FK_JAB_ENTRY_JAB_TAG_tags_ID FOREIGN KEY (tags_ID) REFERENCES JAB_TAG (ID)
ALTER TABLE JAB_ENTRY_JAB_TAG ADD CONSTRAINT FK_JAB_ENTRY_JAB_TAG_entries_ID FOREIGN KEY (entries_ID) REFERENCES JAB_ENTRY (ID)
ALTER TABLE JAB_MEMBER ADD CONSTRAINT FK_JAB_MEMBER_BLOG_ID FOREIGN KEY (BLOG_ID) REFERENCES JAB_BLOG (ID)
ALTER TABLE JAB_MEMBER ADD CONSTRAINT FK_JAB_MEMBER_USER_ID FOREIGN KEY (USER_ID) REFERENCES JAB_USER (ID)
ALTER TABLE JAB_ENTRY_JAB_CATEGORY ADD CONSTRAINT FK_JAB_ENTRY_JAB_CATEGORY_entries_ID FOREIGN KEY (entries_ID) REFERENCES JAB_ENTRY (ID)
ALTER TABLE JAB_ENTRY_JAB_CATEGORY ADD CONSTRAINT FK_JAB_ENTRY_JAB_CATEGORY_categories_ID FOREIGN KEY (categories_ID) REFERENCES JAB_CATEGORY (ID)
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