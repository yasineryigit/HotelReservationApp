CREATE DATABASE IF NOT EXISTS `hr-accounting-service`;
CREATE DATABASE IF NOT EXISTS `hr-hotel-service`;
CREATE DATABASE IF NOT EXISTS `hr-reservation-service`;
CREATE DATABASE IF NOT EXISTS `hr-user-service`;
CREATE DATABASE IF NOT EXISTS `hr-notification-service`;

GRANT ALL ON `hr-accounting-service`.* TO 'ossovita'@'%';
GRANT ALL ON `hr-hotel-service`.* TO 'ossovita'@'%';
GRANT ALL ON `hr-reservation-service`.* TO 'ossovita'@'%';
GRANT ALL ON `hr-user-service`.* TO 'ossovita'@'%';
GRANT ALL ON `hr-notification-service`.* TO 'ossovita'@'%';



