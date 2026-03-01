--- AWS

INSERT INTO public.questions
(answer, is_deleted, option1, option2, option3, option4, question, question_level, category_id)
VALUES
('EC2', false, 'S3', 'EC2', 'RDS', 'Lambda', 'Which AWS service is used to launch virtual servers?', 'Easy', 6),
('S3', false, 'EBS', 'EC2', 'S3', 'IAM', 'Which AWS service provides object storage?', 'Easy', 6),
('Lambda', false, 'EC2', 'Lambda', 'ECS', 'Beanstalk', 'Which AWS service is serverless compute?', 'Easy', 6),
('RDS', false, 'DynamoDB', 'Redshift', 'RDS', 'S3', 'Which AWS service is used for relational databases?', 'Easy', 6),
('DynamoDB', false, 'Aurora', 'DynamoDB', 'RDS', 'EBS', 'Which AWS service provides NoSQL database?', 'Easy', 6),
('Route 53', false, 'CloudFront', 'Route 53', 'ELB', 'IAM', 'Which AWS service manages DNS?', 'Medium', 6),
('ELB', false, 'CloudTrail', 'ELB', 'SNS', 'SQS', 'Which AWS service distributes traffic across instances?', 'Medium', 6),
('CloudWatch', false, 'CloudWatch', 'CloudTrail', 'IAM', 'ECS', 'Which AWS service is used for monitoring resources?', 'Medium', 6),
('CloudTrail', false, 'CloudTrail', 'CloudWatch', 'Inspector', 'Shield', 'Which AWS service tracks API activity?', 'Medium', 6),
('ECS', false, 'ECS', 'EC2', 'S3', 'RDS', 'Which AWS service is used for container orchestration?', 'Medium', 6),
('Shield', false, 'WAF', 'Shield', 'GuardDuty', 'Inspector', 'Which AWS service provides distributed denial-of-service protection?', 'Hard', 6),
('GuardDuty', false, 'GuardDuty', 'Macie', 'WAF', 'IAM', 'Which AWS service analyzes security threats?', 'Hard', 6),
('Redshift', false, 'Redshift', 'Aurora', 'DynamoDB', 'EFS', 'Which AWS service is used for data warehousing?', 'Hard', 6),
('EKS', false, 'ECS', 'EKS', 'EC2', 'Lambda', 'Which AWS service provides managed Kubernetes?', 'Hard', 6),
('CloudFormation', false, 'CloudFormation', 'CodeDeploy', 'CodePipeline', 'OpsWorks', 'Which AWS service is used for infrastructure as code?', 'Hard', 6);