--CREATE TABLE report.mcts_pregnant_mother (
--  id SERIAL PRIMARY KEY
--  ,case_id INTEGER REFERENCES report.mother_case(id) UNIQUE
--);


-- Table: report.mcts_district

-- DROP TABLE report.mcts_district;

CREATE TABLE report.mcts_district
(
  id serial NOT NULL,
  disctrict_id integer NOT NULL,
  name character varying(255) NOT NULL,
  state_id integer NOT NULL,
  CONSTRAINT mcts_district_pkey PRIMARY KEY (id),
  CONSTRAINT mcts_district_stateid FOREIGN KEY (state_id)
      REFERENCES report.mcts_state (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE report.mcts_district
  OWNER TO postgres;

  
  -- Table: report.mcts_healthblock

-- DROP TABLE report.mcts_healthblock;

CREATE TABLE report.mcts_healthblock
(
  id serial NOT NULL,
  healthblock_id integer NOT NULL,
  name character varying(255) NOT NULL,
  taluk_id integer NOT NULL,
  CONSTRAINT mcts_healthblock_pkey PRIMARY KEY (id),
  CONSTRAINT mcts_healthblock_taluk_fkey FOREIGN KEY (taluk_id)
      REFERENCES report.mcts_taluk (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE report.mcts_healthblock
  OWNER TO postgres;

  
  -- Table: report.mcts_healthworker

-- DROP TABLE report.mcts_healthworker;

CREATE TABLE report.mcts_healthworker
(
  id serial NOT NULL,
  healthworker_id integer NOT NULL,
  name character varying(255) NOT NULL,
  subcenter_id integer NOT NULL,
  village_id integer,
  contact_no integer NOT NULL,
  sex character(1) NOT NULL,
  type character varying(10) NOT NULL,
  husband_name character varying(255),
  aadhar_no character varying(255),
  gf_address character varying(255),
  CONSTRAINT mcts_healthworker_pkey PRIMARY KEY (id),
  CONSTRAINT mcts_healthworker_subcenterid_fkey FOREIGN KEY (subcenter_id)
      REFERENCES report.mcts_subcenter (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT mcts_healthworker_villageid_fkey FOREIGN KEY (village_id)
      REFERENCES report.mcts_village (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE report.mcts_healthworker
  OWNER TO postgres;

  
  -- Table: report.mcts_phc

-- DROP TABLE report.mcts_phc;

CREATE TABLE report.mcts_phc
(
  id serial NOT NULL,
  phc_id integer NOT NULL,
  name character varying(255) NOT NULL,
  healthblock_id integer NOT NULL,
  CONSTRAINT mcts_phc_pkey PRIMARY KEY (id),
  CONSTRAINT mcts_phc_healthblockid_fkey FOREIGN KEY (healthblock_id)
      REFERENCES report.mcts_healthblock (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE report.mcts_phc
  OWNER TO postgres;

  
  -- Table: report.mcts_pregnant_mother

-- DROP TABLE report.mcts_pregnant_mother;

CREATE TABLE report.mcts_pregnant_mother
(
  id serial NOT NULL,
  mcts_id character varying(20),
  case_id integer,
  name character varying(255),
  type character varying(10),
  birth_date date,
  gender character(1),
  village_id character varying(255),
  subcenter_id character varying(255),
  father_husband_name character varying(255),
  email character varying(255),
  mobile_no character varying(20),
  economic_status character varying(255),
  category character varying(255),
  beneficiary_address character varying(255),
  uid_number character varying(255),
  pincode character varying(6),
  lmp_date date,
  eid_number character varying(255),
  anm_id character varying(20),
  ward character varying(255),
  town character varying(255),
  CONSTRAINT mcts_pregnant_mother_pkey PRIMARY KEY (id),
  CONSTRAINT mcts_pregnant_mother_case_id_fkey FOREIGN KEY (case_id)
      REFERENCES report.mother_case (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT mcts_pregnant_mother_case_id_key UNIQUE (case_id),
  CONSTRAINT mcts_pregnant_mother_mcts_id_key UNIQUE (mcts_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE report.mcts_pregnant_mother
  OWNER TO postgres;
  
  
  -- Table: report.mcts_state

-- DROP TABLE report.mcts_state;

CREATE TABLE report.mcts_state
(
  id serial NOT NULL,
  state_id integer NOT NULL,
  name character varying(255) NOT NULL,
  CONSTRAINT mcts_state_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE report.mcts_state
  OWNER TO postgres;

  
  -- Table: report.mcts_subcenter

-- DROP TABLE report.mcts_subcenter;

CREATE TABLE report.mcts_subcenter
(
  id serial NOT NULL,
  subcenter_id integer NOT NULL,
  name character varying(255) NOT NULL,
  phc_id integer NOT NULL,
  CONSTRAINT mcts_subcenter_pkey PRIMARY KEY (id),
  CONSTRAINT mcts_subcenter_phcid_fkey FOREIGN KEY (phc_id)
      REFERENCES report.mcts_phc (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE report.mcts_subcenter
  OWNER TO postgres;

  
  -- Table: report.mcts_taluk

-- DROP TABLE report.mcts_taluk;

CREATE TABLE report.mcts_taluk
(
  id serial NOT NULL,
  taluk_id integer NOT NULL,
  name character varying(255) NOT NULL,
  district_id integer NOT NULL,
  CONSTRAINT mcts_taluk_pkey PRIMARY KEY (id),
  CONSTRAINT mcts_taluk_districtid_fkey FOREIGN KEY (district_id)
      REFERENCES report.mcts_district (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE report.mcts_taluk
  OWNER TO postgres;

  
  -- Table: report.mcts_village

-- DROP TABLE report.mcts_village;

CREATE TABLE report.mcts_village
(
  id serial NOT NULL,
  village_id integer NOT NULL,
  name character varying(255) NOT NULL,
  subcenter_id integer NOT NULL,
  taluk_id integer NOT NULL,
  CONSTRAINT mcts_village_pkey PRIMARY KEY (id),
  CONSTRAINT mcts_village_subcenterid_fkey FOREIGN KEY (subcenter_id)
      REFERENCES report.mcts_subcenter (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT mcts_village_talukid_fkey FOREIGN KEY (taluk_id)
      REFERENCES report.mcts_taluk (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE report.mcts_village
  OWNER TO postgres;

