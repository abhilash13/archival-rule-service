CREATE TABLE archivalpolicy (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(255) NOT NULL UNIQUE,
        databasename VARCHAR(255) NOT NULL,
        databaseuri VARCHAR(255) NOT NULL,
        username VARCHAR(255) NOT NULL,
        password VARCHAR(255) NOT NULL,
        tablename VARCHAR(255) NOT NULL,
        archivedatabeforeindays BIGINT NOT NULL,
        retainarchiveddataforindays BIGINT NOT NULL
);