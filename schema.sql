-- MySQL schema for events table
CREATE TABLE IF NOT EXISTS events (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  junction_name VARCHAR(255),
  camera_name VARCHAR(255),
  event_time DATETIME,
  event_type VARCHAR(100),
  status VARCHAR(100),
  priority VARCHAR(50),
  message TEXT,
  acknowledged_by VARCHAR(100),
  color VARCHAR(50),
  plate_number VARCHAR(20),
  speed INT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY unique_event (camera_name, event_time, plate_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
