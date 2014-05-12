CREATE TABLE report.mcts_pregnant_mother_service_update (
  id SERIAL PRIMARY KEY
  ,mcts_id INTEGER REFERENCES report.mcts_pregnant_mother(id)
  ,service_type SMALLINT
  ,service_delivery_date DATE
  ,service_update_time TIMESTAMP WITH TIME ZONE
);