select * from disaster_reports;
select * from volunteer_tasks;
INSERT INTO volunteer_tasks (task_id,task_title, task_description, location, assigned_date, status)
VALUES
    (1, 'Distribute Food Supplies', 'Deliver food packages and report back.','Sylhet','2025-01-20', 'Pending'),
    (2, 'Assist in Evacuation', 'Help evacuate families to the nearby shelter.','Dhaka','2025-01-24', 'Pending');
describe volunteer_tasks;
ALTER TABLE volunteer_tasks
ADD disaster_type VARCHAR(255) NOT NULL;
UPDATE volunteer_tasks
SET disaster_type = 'Earthquake'
WHERE task_id = 2;
