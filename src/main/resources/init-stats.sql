create schema if not exists scripting;

CREATE TABLE if not exists  scripting.stats_liste_comptes (
    id integer NOT NULL,
    user character varying(255),
    pass character varying(255),
    monde integer,
    active boolean DEFAULT false,
    timezone character varying,
    nb_joueurs integer,
    nb_alliances integer
);

CREATE SEQUENCE if not exists scripting.stats_liste_comptes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE if not exists  scripting.stats_batch_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
    
CREATE TABLE if not exists scripting.stats_batch_log (
    id bigint  DEFAULT scripting.stats_batch_log_id_seq.nextval NOT NULL,
    num_batch integer,
    date_debut timestamp without time zone,
    date_fin timestamp without time zone,
    duree integer,
    nb_worlds integer,
    nb_worlds_fails integer,
    fail_list character varying(255)
);

MERGE INTO scripting.stats_liste_comptes 
  KEY(ID) 
VALUES 
  (2, 'USER', 'PASS', 373, true, 'Europe/Paris',  0,0);
  


CREATE TABLE scripting.stats_list (
    id integer NOT NULL,
    nom character varying(50),
    requete text,
    type smallint
);

MERGE INTO scripting.stats_list 
  KEY(id) 
VALUES (10, 'record_poi_nb', 'SELECT 
			id,
			nom_alliance,
			nb_poi
			from
			stats_alliance
			order by nb_poi desc
			limit 1', 0),
		(12, 'record_base_score', 'SELECT 
			id_joueur,
			pseudo,
			nom_base,
			nom_alliance,
			score_base
			from
			stats_joueur, stats_base, stats_alliance
			where stats_base.id_joueur=stats_joueur.id
			and stats_joueur.id_alliance=stats_alliance.id
			order by score_base desc
			limit 1', 0),
	 	(24, '_record_poi_nb', 'SELECT 
			id,
			nom_alliance,
			nb_poi
			from
			stats_alliance
			where stats_alliance.id= :id_alliance
			order by nb_poi desc
			limit 1', 1);
  
			
CREATE SEQUENCE scripting.stats_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
    
    
CREATE TABLE scripting.stats_log (
    id integer DEFAULT scripting.stats_log_id_seq.nextval NOT NULL,
    id_stat integer,
    duree integer,
    date timestamp without time zone,
    monde integer,
    alliance integer DEFAULT 0
);



create schema if not exists monde373;

CREATE SEQUENCE if not exists monde373.stats_alliance_hist_id_hist_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE if not exists monde373.stats_base_hist_id_hist_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE if not exists monde373.stats_joueur_hist_id_hist_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE if not exists monde373.stats_poi_hist_id_hist_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;





CREATE SEQUENCE if not exists monde373.stats_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE if not exists  monde373.stats_alliance (
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
    score_poi_7 integer NOT NULL
);


CREATE TABLE if not exists  monde373.stats_alliance_hist (
    id_hist integer DEFAULT monde373.stats_alliance_hist_id_hist_seq.nextval NOT NULL,
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


CREATE TABLE if not exists  monde373.stats_base (
    id integer NOT NULL,
    id_joueur integer NOT NULL,
    nom_base character varying(100) NOT NULL,
    score_base integer NOT NULL,
    coord_x integer NOT NULL,
    coord_y integer NOT NULL
);



CREATE TABLE if not exists  monde373.stats_base_hist (
    id integer,
    id_joueur integer,
    nom_base character varying(100),
    score_base integer,
    coord_x integer,
    coord_y integer,
    id_hist integer DEFAULT monde373.stats_base_hist_id_hist_seq.nextval NOT NULL,
    date date
);



CREATE TABLE if not exists  monde373.stats_joueur (
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
    nb_tacitus integer
);


CREATE TABLE if not exists  monde373.stats_joueur_hist (
    id_hist integer DEFAULT monde373.stats_joueur_hist_id_hist_seq.nextval NOT NULL,
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


CREATE TABLE if not exists  monde373.stats_poi (
    id integer NOT NULL,
    id_alliance integer NOT NULL,
    level integer NOT NULL,
    type integer NOT NULL,
    coord_x integer NOT NULL,
    coord_y integer NOT NULL
);



CREATE TABLE if not exists  monde373.stats_poi_hist (
    id integer,
    id_alliance integer,
    level integer,
    type integer,
    coord_x integer,
    coord_y integer,
    id_hist integer DEFAULT monde373.stats_poi_hist_id_hist_seq.nextval NOT NULL,
    date date
);



CREATE TABLE if not exists  monde373.stats_settings (
    name character varying(50) NOT NULL,
    value character varying NOT NULL
);

MERGE INTO monde373.stats_settings 
  KEY(name) 
VALUES 
  ('max_ranking_alliance', '100'),
  ('max_ranking_player', '100'),
  ('date_last_update', '2018-09-25'),
  ('force_stats','0');
  



CREATE TABLE if not exists  monde373.stats (
    id integer DEFAULT monde373.stats_id_seq.nextval NOT NULL,
    nom_stat CLOB NOT NULL,
    donnees_stat CLOB NOT NULL,
    id_alliance integer DEFAULT 0
);


