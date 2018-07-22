create schema if not exists scripting;
create schema if not exists monde373;
CREATE TABLE if not exists monde373.poi (
    x integer,
    y integer,
    t integer,
    l integer,
    a integer
);
CREATE TABLE if not exists monde373.alliance (
    a integer,
    an character varying(255),
    p bigint,
    c integer
);
CREATE TABLE if not exists monde373.base (
    pi integer,
    y integer,
    x integer,
    n character varying(255),
    i integer,
    l integer,
    al boolean,
    pr boolean,
    cb integer,
    cd integer,
    ps bigint
);
CREATE TABLE if not exists monde373.joueur (
    i integer,
    p bigint,
    a integer,
    n character varying(255),
    f integer,
    ps integer,
    pd integer,
    bc integer
);
CREATE TABLE if not exists monde373.endgame (
    type integer,
    x integer NOT NULL,
    y integer NOT NULL,
    step integer
);
CREATE TABLE if not exists monde373.settings (
    name character varying(255),
    value character varying(255)
);

