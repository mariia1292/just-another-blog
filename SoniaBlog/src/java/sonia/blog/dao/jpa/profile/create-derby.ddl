CREATE TABLE JAB_COMMENT (ID BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL, AUTHORADDRESS VARCHAR(255), AUTHORMAIL VARCHAR(255), CONTENT VARCHAR(2048) NOT NULL, AUTHORURL VARCHAR(255), AUTHORNAME VARCHAR(255) NOT NULL, SPAM SMALLINT DEFAULT 0, CREATIONDATE TIMESTAMP NOT NULL, ENTRY_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE JAB_CATEGORY (ID BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL, CREATIONDATE TIMESTAMP NOT NULL, DESCRIPTION VARCHAR(255), NAME VARCHAR(255) NOT NULL, BLOG_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE JAB_BLOGHITCOUNT (ID BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL, HITDATE DATE NOT NULL, HITCOUNT BIGINT, BLOG_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE JAB_USER (ID BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL, PASSWORD VARCHAR(255) NOT NULL, ACTIVATIONCODE VARCHAR(255) NOT NULL, EMAIL VARCHAR(255) UNIQUE NOT NULL, REGISTRATIONDATE TIMESTAMP NOT NULL, NAME VARCHAR(255) UNIQUE NOT NULL, GLOBALADMIN SMALLINT DEFAULT 0, LASTLOGIN TIMESTAMP, SELFMANAGED SMALLINT DEFAULT 0, DISPLAYNAME VARCHAR(255) NOT NULL, ACTIVE SMALLINT DEFAULT 0, PRIMARY KEY (ID))
CREATE TABLE JAB_TAG (ID BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL, NAME VARCHAR(255) UNIQUE NOT NULL, PRIMARY KEY (ID))
CREATE TABLE JAB_ATTACHMENT (ID BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL, FILESIZE BIGINT NOT NULL, CREATIONDATE TIMESTAMP NOT NULL, MIMETYPE VARCHAR(255) NOT NULL, DESCRIPTION VARCHAR(255), NAME VARCHAR(255) NOT NULL, FILEPATH VARCHAR(255) NOT NULL, ENTRY_ID BIGINT, PAGE_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE JAB_ENTRY_JAB_TAG (entries_ID BIGINT NOT NULL, tags_ID BIGINT NOT NULL, PRIMARY KEY (entries_ID, tags_ID))
CREATE TABLE JAB_BLOG (ID BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL, DESCRIPTION VARCHAR(255), EMAIL VARCHAR(255), TITLE VARCHAR(250) NOT NULL, STARTPAGE VARCHAR(255), ENTRIESPERPAGE INTEGER, CREATIONDATE TIMESTAMP NOT NULL, IMAGEHEIGHT INTEGER, TEMPLATE VARCHAR(255) NOT NULL, IMAGEWIDTH INTEGER, ALLOWCOMMENTS SMALLINT DEFAULT 0, LOCALE VARCHAR(255), ALLOWMACROS SMALLINT DEFAULT 0, THUMBNAILHEIGHT INTEGER, DATEFORMAT VARCHAR(50) NOT NULL, THUMBNAILWIDTH INTEGER, ACTIVE SMALLINT DEFAULT 0, TIMEZONE VARCHAR(255), ALLOWHTMLCOMMENTS SMALLINT DEFAULT 0, SENDAUTOPING SMALLINT DEFAULT 0, IDENTIFIER VARCHAR(255) UNIQUE NOT NULL, PRIMARY KEY (ID))
CREATE TABLE JAB_BLOGPARAMETER (ID BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL, NAME VARCHAR(255) NOT NULL, VALUE VARCHAR(255) NOT NULL, BLOG_ID BIGINT, PRIMARY KEY(ID))
CREATE TABLE JAB_MEMBER (ID BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL, BlogRole INTEGER NOT NULL, REGISTRATIONDATE TIMESTAMP NOT NULL, NOTIFY SMALLINT DEFAULT 0, BLOG_ID BIGINT, USER_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE JAB_ENTRY_JAB_CATEGORY (entries_ID BIGINT NOT NULL, categories_ID BIGINT NOT NULL, PRIMARY KEY (entries_ID, categories_ID))
CREATE TABLE JAB_ENTRY (ID BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL, PUBLISHED SMALLINT DEFAULT 0, ALLOWCOMMENTS SMALLINT DEFAULT 0, TEASER VARCHAR(4000), PUBLISHINGDATE TIMESTAMP, CREATIONDATE TIMESTAMP NOT NULL, TITLE VARCHAR(255) NOT NULL, LASTUPDATE TIMESTAMP, CONTENT CLOB(64000), BLOG_ID BIGINT, AUTHOR_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE JAB_PAGE (ID BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL, PUBLISHINGDATE TIMESTAMP, NAVIGATIONPOSITION INTEGER, NAVIGATIONTITLE VARCHAR(255) NOT NULL, PUBLISHED SMALLINT DEFAULT 0, CREATIONDATE TIMESTAMP NOT NULL, CONTENT CLOB(64000), TITLE VARCHAR(255) NOT NULL, LASTUPDATE TIMESTAMP, AUTHOR_ID BIGINT, PARENT_ID BIGINT, BLOG_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE JAB_TRACKBACK (ID BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL, TYPE INTEGER NOT NULL, URL VARCHAR(255) NOT NULL, EXCERPT VARCHAR(255) NOT NULL, TITLE VARCHAR(255) NOT NULL, TRACKBACKDATE TIMESTAMP NOT NULL, BLOGNAME VARCHAR(255), ENTRY_ID BIGINT, PRIMARY KEY (ID))

ALTER TABLE JAB_COMMENT ADD CONSTRAINT JABCOMMENTENTRY_ID FOREIGN KEY (ENTRY_ID) REFERENCES JAB_ENTRY (ID)
ALTER TABLE JAB_CATEGORY ADD CONSTRAINT JABCATEGORYBLOG_ID FOREIGN KEY (BLOG_ID) REFERENCES JAB_BLOG (ID)
ALTER TABLE JAB_BLOGHITCOUNT ADD CONSTRAINT JBBLOGHITCOUNTBLGD FOREIGN KEY (BLOG_ID) REFERENCES JAB_BLOG (ID)
ALTER TABLE JAB_ATTACHMENT ADD CONSTRAINT JBATTACHMENTPAGEID FOREIGN KEY (PAGE_ID) REFERENCES JAB_PAGE (ID)
ALTER TABLE JAB_ATTACHMENT ADD CONSTRAINT JBATTACHMENTNTRYID FOREIGN KEY (ENTRY_ID) REFERENCES JAB_ENTRY (ID)
ALTER TABLE JAB_ENTRY_JAB_TAG ADD CONSTRAINT JBENTRYJABTAGtgsID FOREIGN KEY (tags_ID) REFERENCES JAB_TAG (ID)
ALTER TABLE JAB_ENTRY_JAB_TAG ADD CONSTRAINT JBNTRYJABTAGntrsID FOREIGN KEY (entries_ID) REFERENCES JAB_ENTRY (ID)
ALTER TABLE JAB_MEMBER ADD CONSTRAINT JAB_MEMBER_BLOG_ID FOREIGN KEY (BLOG_ID) REFERENCES JAB_BLOG (ID)
ALTER TABLE JAB_MEMBER ADD CONSTRAINT JAB_MEMBER_USER_ID FOREIGN KEY (USER_ID) REFERENCES JAB_USER (ID)
ALTER TABLE JAB_ENTRY_JAB_CATEGORY ADD CONSTRAINT JBNTRYJBCTGRYntrsD FOREIGN KEY (entries_ID) REFERENCES JAB_ENTRY (ID)
ALTER TABLE JAB_ENTRY_JAB_CATEGORY ADD CONSTRAINT JBNTRYJBCTGRctgrsD FOREIGN KEY (categories_ID) REFERENCES JAB_CATEGORY (ID)
ALTER TABLE JAB_ENTRY ADD CONSTRAINT JAB_ENTRY_BLOG_ID FOREIGN KEY (BLOG_ID) REFERENCES JAB_BLOG (ID)
ALTER TABLE JAB_ENTRY ADD CONSTRAINT JAB_ENTRYAUTHOR_ID FOREIGN KEY (AUTHOR_ID) REFERENCES JAB_USER (ID)
ALTER TABLE JAB_PAGE ADD CONSTRAINT JAB_PAGE_PARENT_ID FOREIGN KEY (PARENT_ID) REFERENCES JAB_PAGE (ID)
ALTER TABLE JAB_PAGE ADD CONSTRAINT JAB_PAGE_BLOG_ID FOREIGN KEY (BLOG_ID) REFERENCES JAB_BLOG (ID)
ALTER TABLE JAB_PAGE ADD CONSTRAINT JAB_PAGE_AUTHOR_ID FOREIGN KEY (AUTHOR_ID) REFERENCES JAB_USER (ID)
ALTER TABLE JAB_TRACKBACK ADD CONSTRAINT JABTRACKBACKNTRYID FOREIGN KEY (ENTRY_ID) REFERENCES JAB_ENTRY (ID)
ALTER TABLE JAB_BLOGPARAMETER ADD CONSTRAINT JABPARAMETERBLOG_ID FOREIGN KEY (BLOG_ID) REFERENCES JAB_BLOG (ID)

CREATE UNIQUE INDEX JAB_BLOGPARAMETER_BLOG_NAME ON JAB_BLOGPARAMETER (BLOG_ID, NAME)
CREATE UNIQUE INDEX JAB_BLOGHITCOUNT_BLOG_DATE ON JAB_BLOGHITCOUNT (BLOG_ID, HITDATE)
CREATE UNIQUE INDEX JAB_CATEGORY_BLOG_NAME ON JAB_CATEGORY (BLOG_ID, NAME)
CREATE UNIQUE INDEX JAB_MEMBER_BLOG_USER ON JAB_MEMBER (BLOG_ID, USER_ID)