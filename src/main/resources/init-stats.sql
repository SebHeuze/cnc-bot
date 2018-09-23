create schema if not exists scripting;

CREATE TABLE scripting.liste_comptes (
    id integer NOT NULL,
    "user" character varying(255),
    pass character varying(255),
    monde integer,
    active boolean DEFAULT false,
    timezone character varying,
    nb_joueurs integer,
    nb_alliances integer
);

CREATE SEQUENCE scripting.liste_comptes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ONLY scripting.liste_comptes ALTER COLUMN id SET DEFAULT nextval('scripting.liste_comptes_id_seq'::regclass);

ALTER TABLE ONLY scripting.liste_comptes
    ADD CONSTRAINT id_pk PRIMARY KEY (id);
    
create schema if not exists monde373;



CREATE SEQUENCE monde373.alliance_hist_id_hist_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE monde373.alliance_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE monde373.base_hist_id_hist_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE SEQUENCE monde373.base_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE SEQUENCE monde373.joueur_hist_id_hist_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE monde373.joueur_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE monde373.poi_hist_id_hist_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE monde373.poi_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



CREATE SEQUENCE monde373.stats_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE monde373.alliance (
    id integer DEFAULT nextval('monde373.alliance_id_seq'::regclass) NOT NULL,
    nom_alliance character varying(100) NOT NULL,
    nombre_bases integer NOT NULL,
    nombre_joueurs integer NOT NULL,
    rang integer NOT NULL,
    score bigint NOT NULL,
    top_score bigint NOT NULL,
    average_score integer NOT NULL,
    total_bases_detruites integer NOT NULL,
    bases_oublies_detruites integer NOT NULL,
    bases_joueurs_detruites integer NOT NULL,
    distance_centre integer NOT NULL,
    alliance_description text NOT NULL,
    nb_poi integer NOT NULL,
    rank_poi_1 integer NOT NULL,
    rank_poi_2 integer NOT NULL,
    rank_poi_3 integer NOT NULL,
    rank_poi_4 integer NOT NULL,
    rank_poi_5 integer NOT NULL,
    rank_poi_6 integer NOT NULL,
    rank_poi_7 integer NOT NULL,
    score_poi_1 integer NOT NULL,
    score_poi_2 integer NOT NULL,
    score_poi_3 integer NOT NULL,
    score_poi_4 integer NOT NULL,
    score_poi_5 integer NOT NULL,
    score_poi_6 integer NOT NULL,
    score_poi_7 integer NOT NULL
);


CREATE TABLE monde373.alliance_hist (
    id_hist integer DEFAULT nextval('monde373.alliance_hist_id_hist_seq'::regclass) NOT NULL,
    id integer NOT NULL,
    nom_alliance character varying(100) NOT NULL,
    nombre_bases integer NOT NULL,
    nombre_joueurs integer NOT NULL,
    rang integer NOT NULL,
    score bigint NOT NULL,
    top_score bigint NOT NULL,
    average_score integer NOT NULL,
    total_bases_detruites integer NOT NULL,
    bases_oublies_detruites integer NOT NULL,
    bases_joueurs_detruites integer NOT NULL,
    distance_centre integer NOT NULL,
    alliance_description text NOT NULL,
    nb_poi integer NOT NULL,
    rank_poi_1 integer NOT NULL,
    rank_poi_2 integer NOT NULL,
    rank_poi_3 integer NOT NULL,
    rank_poi_4 integer NOT NULL,
    rank_poi_5 integer NOT NULL,
    rank_poi_6 integer NOT NULL,
    rank_poi_7 integer NOT NULL,
    score_poi_1 integer NOT NULL,
    score_poi_2 integer NOT NULL,
    score_poi_3 integer NOT NULL,
    score_poi_4 integer NOT NULL,
    score_poi_5 integer NOT NULL,
    score_poi_6 integer NOT NULL,
    score_poi_7 integer NOT NULL,
    date date NOT NULL
);


CREATE TABLE monde373.base (
    id integer DEFAULT nextval('monde373.base_id_seq'::regclass) NOT NULL,
    id_joueur integer NOT NULL,
    nom_base character varying(100) NOT NULL,
    score_base integer NOT NULL,
    coord_x integer NOT NULL,
    coord_y integer NOT NULL
);



CREATE TABLE monde373.base_hist (
    id integer,
    id_joueur integer,
    nom_base character varying(100),
    score_base integer,
    coord_x integer,
    coord_y integer,
    id_hist integer DEFAULT nextval('monde373.base_hist_id_hist_seq'::regclass) NOT NULL,
    date date
);



CREATE TABLE monde373.joueur (
    id integer DEFAULT nextval('monde373.joueur_id_seq'::regclass) NOT NULL,
    pseudo character varying(50) NOT NULL,
    faction integer NOT NULL,
    rang integer NOT NULL,
    score bigint NOT NULL,
    id_alliance integer NOT NULL,
    total_bases_detruites integer NOT NULL,
    bases_oublies_detruites integer NOT NULL,
    bases_joueurs_detruites integer NOT NULL,
    distance_centre integer NOT NULL,
    nb_tacitus integer
);


CREATE TABLE monde373.joueur_hist (
    id_hist integer DEFAULT nextval('monde373.joueur_hist_id_hist_seq'::regclass) NOT NULL,
    id integer NOT NULL,
    pseudo character varying(50) NOT NULL,
    faction integer NOT NULL,
    rang integer NOT NULL,
    score bigint NOT NULL,
    id_alliance integer NOT NULL,
    total_bases_detruites integer NOT NULL,
    bases_oublies_detruites integer NOT NULL,
    bases_joueurs_detruites integer NOT NULL,
    distance_centre integer NOT NULL,
    date date NOT NULL,
    nb_tacitus integer
);


CREATE TABLE monde373.poi (
    id integer DEFAULT nextval('monde373.poi_id_seq'::regclass) NOT NULL,
    id_alliance integer NOT NULL,
    level integer NOT NULL,
    type integer NOT NULL,
    coord_x integer NOT NULL,
    coord_y integer NOT NULL
);



CREATE TABLE monde373.poi_hist (
    id integer,
    id_alliance integer,
    level integer,
    type integer,
    coord_x integer,
    coord_y integer,
    id_hist integer DEFAULT nextval('monde373.poi_hist_id_hist_seq'::regclass) NOT NULL,
    date date
);



CREATE TABLE monde373.settings (
    name character varying(50) NOT NULL,
    value character varying NOT NULL
);


CREATE TABLE monde373.stats (
    id integer DEFAULT nextval('monde373.stats_id_seq'::regclass) NOT NULL,
    nom_stat text NOT NULL,
    donnees_stat text NOT NULL,
    id_alliance integer DEFAULT 0
);

ALTER TABLE ONLY monde373.alliance_hist
    ADD CONSTRAINT alliance_hist_pkey PRIMARY KEY (id_hist);



ALTER TABLE ONLY monde373.alliance
    ADD CONSTRAINT alliance_pkey PRIMARY KEY (id);


ALTER TABLE ONLY monde373.base
    ADD CONSTRAINT base_pkey PRIMARY KEY (id);


ALTER TABLE ONLY monde373.joueur_hist
    ADD CONSTRAINT joueur_hist_pkey PRIMARY KEY (id_hist);



ALTER TABLE ONLY monde373.joueur
    ADD CONSTRAINT joueur_pkey PRIMARY KEY (id);


ALTER TABLE ONLY monde373.poi
    ADD CONSTRAINT poi_pkey PRIMARY KEY (id);


ALTER TABLE ONLY monde373.settings
    ADD CONSTRAINT settings_pkey PRIMARY KEY (value, name);


ALTER TABLE ONLY monde373.stats
    ADD CONSTRAINT stats_pkey PRIMARY KEY (id);

