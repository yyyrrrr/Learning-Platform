// Neo4j Schema 定义脚本

CREATE CONSTRAINT knowledge_point_neo4j_id_unique IF NOT EXISTS
FOR (kp:KnowledgePoint)
REQUIRE kp.neo4j_id IS UNIQUE;

CREATE INDEX knowledge_point_code IF NOT EXISTS
FOR (kp:KnowledgePoint)
ON (kp.code);

CREATE INDEX knowledge_point_module IF NOT EXISTS
FOR (kp:KnowledgePoint)
ON (kp.module);

CREATE INDEX knowledge_point_level IF NOT EXISTS
FOR (kp:KnowledgePoint)
ON (kp.level);

CREATE INDEX knowledge_point_difficulty IF NOT EXISTS
FOR (kp:KnowledgePoint)
ON (kp.difficulty);

CREATE INDEX knowledge_point_mysql_id IF NOT EXISTS
FOR (kp:KnowledgePoint)
ON (kp.mysql_id);
