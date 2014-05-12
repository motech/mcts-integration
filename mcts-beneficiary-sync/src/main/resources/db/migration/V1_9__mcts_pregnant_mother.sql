CREATE TABLE report.mcts_pregnant_mother (
  id SERIAL PRIMARY KEY
  ,mcts_id VARCHAR(20) UNIQUE
  ,case_id INTEGER REFERENCES report.mother_case(id) UNIQUE
);