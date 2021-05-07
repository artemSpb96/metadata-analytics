CREATE TABLE IF NOT EXISTS filesystem (
    fs_id SERIAL PRIMARY KEY,
    active_ver int NOT NULL,
    name TEXT NOT NULL,
    url TEXT NOT NULL,
    create_time TIMESTAMP NOT NULL,
    update_time TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS node (
    fs_id int REFERENCES filesystem(fs_id),
    path TEXT COLLATE "C" NOT NULL,
    lvl int NOT NULL,
    start_ver int NOT NULL,
    ver int NOT NULL,
    meta json NOT NULL,
    create_time TIMESTAMP NOT NULL,
    is_dir BOOL NOT NULL,
    file_type TEXT,
    PRIMARY KEY (fs_id, path, start_ver)
);

CREATE INDEX children_idx
ON node(fs_id, lvl, path, start_ver);
