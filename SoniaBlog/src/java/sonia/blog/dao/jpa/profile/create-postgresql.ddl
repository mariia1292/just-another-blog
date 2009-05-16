CREATE TABLE JAB_COMMENT (ID  SERIAL NOT NULL, AUTHORADDRESS VARCHAR(255), AUTHORMAIL VARCHAR(255), CONTENT VARCHAR(2048) NOT NULL, AUTHORURL VARCHAR(255), AUTHORNAME VARCHAR(255) NOT NULL, SPAM BOOLEAN, CREATIONDATE TIMESTAMP NOT NULL, ENTRY_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE JAB_CATEGORY (ID  SERIAL NOT NULL, CREATIONDATE TIMESTAMP NOT NULL, DESCRIPTION VARCHAR(255), NAME VARCHAR(255) NOT NULL, BLOG_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE JAB_BLOGHITCOUNT (ID  SERIAL NOT NULL, HITDATE DATE NOT NULL, HITCOUNT BIGINT, BLOG_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE JAB_USER (ID  SERIAL NOT NULL, PASSWORD VARCHAR(255) NOT NULL, ACTIVATIONCODE VARCHAR(255) NOT NULL, EMAIL VARCHAR(255) UNIQUE NOT NULL, REGISTRATIONDATE TIMESTAMP NOT NULL, NAME VARCHAR(255) UNIQUE NOT NULL, GLOBALADMIN BOOLEAN, LASTLOGIN TIMESTAMP, SELFMANAGED BOOLEAN, DISPLAYNAME VARCHAR(255) NOT NULL, ACTIVE BOOLEAN, PRIMARY KEY (ID))
CREATE TABLE JAB_TAG (ID  SERIAL NOT NULL, NAME VARCHAR(255) UNIQUE NOT NULL, PRIMARY KEY (ID))
CREATE TABLE JAB_ATTACHMENT (ID  SERIAL NOT NULL, FILESIZE BIGINT NOT NULL, CREATIONDATE TIMESTAMP NOT NULL, MIMETYPE VARCHAR(255) NOT NULL, DESCRIPTION VARCHAR(255), NAME VARCHAR(255) NOT NULL, FILEPATH VARCHAR(255) NOT NULL, ENTRY_ID BIGINT, PAGE_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE JAB_ENTRY_JAB_TAG (entries_ID BIGINT NOT NULL, tags_ID BIGINT NOT NULL, PRIMARY KEY (entries_ID, tags_ID))
CREATE TABLE JAB_BLOG (ID  SERIAL NOT NULL, DESCRIPTION VARCHAR(255), EMAIL VARCHAR(255), TITLE VARCHAR(250) NOT NULL, ENTRIESPERPAGE INTEGER, CREATIONDATE TIMESTAMP NOT NULL, IMAGEHEIGHT INTEGER, TEMPLATE VARCHAR(255) NOT NULL, IMAGEWIDTH INTEGER, ALLOWCOMMENTS BOOLEAN, LOCALE VARCHAR(255), ALLOWMACROS BOOLEAN, THUMBNAILHEIGHT INTEGER, DATEFORMAT VARCHAR(50) NOT NULL, THUMBNAILWIDTH INTEGER, ACTIVE BOOLEAN, TIMEZONE VARCHAR(255), ALLOWHTMLCOMMENTS BOOLEAN, SENDAUTOPING BOOLEAN, IDENTIFIER VARCHAR(255) UNIQUE NOT NULL, PRIMARY KEY (ID))
CREATE TABLE JAB_MEMBER (ID  SERIAL NOT NULL, BlogRole INTEGER NOT NULL, REGISTRATIONDATE TIMESTAMP NOT NULL, BLOG_ID BIGINT, USER_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE JAB_ENTRY_JAB_CATEGORY (entries_ID BIGINT NOT NULL, categories_ID BIGINT NOT NULL, PRIMARY KEY (entries_ID, categories_ID))
CREATE TABLE JAB_ENTRY (ID  SERIAL NOT NULL, PUBLISHED BOOLEAN, ALLOWCOMMENTS BOOLEAN, TEASER VARCHAR(4000), PUBLISHINGDATE TIMESTAMP, CREATIONDATE TIMESTAMP NOT NULL, TITLE VARCHAR(255) NOT NULL, LASTUPDATE TIMESTAMP, CONTENT TEXT, BLOG_ID BIGINT, AUTHOR_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE JAB_PAGE (ID  SERIAL NOT NULL, PUBLISHINGDATE TIMESTAMP, NAVIGATIONPOSITION INTEGER, NAVIGATIONTITLE VARCHAR(255) NOT NULL, PUBLISHED BOOLEAN, CREATIONDATE TIMESTAMP NOT NULL, CONTENT TEXT, TITLE VARCHAR(255) NOT NULL, LASTUPDATE TIMESTAMP, AUTHOR_ID BIGINT, PARENT_ID BIGINT, BLOG_ID BIGINT, PRIMARY KEY (ID))

ALTER TABLE JAB_COMMENT ADD CONSTRAINT FK_JAB_COMMENT_ENTRY_ID FOREIGN KEY (ENTRY_ID) REFERENCES JAB_ENTRY (ID)
ALTER TABLE JAB_CATEGORY ADD CONSTRAINT FK_JAB_CATEGORY_BLOG_ID FOREIGN KEY (BLOG_ID) REFERENCES JAB_BLOG (ID)
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