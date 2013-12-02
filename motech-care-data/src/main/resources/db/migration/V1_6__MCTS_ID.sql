ALTER TABLE report.mother_case
ADD COLUMN mcts_id VARCHAR(50) UNIQUE;

ALTER TABLE report.child_case
ADD COLUMN mcts_id VARCHAR(50) UNIQUE;