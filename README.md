
IoT CDK Stack
-------------

This project defines AWS infrastructure for an IoT-based system using the "AWS Cloud Development Kit (CDK)" in "Java". It includes an AWS IoT Thing, a topic rule to listen to MQTT topics, and a Lambda function triggered by the rule.

1. Prerequisites
----------------

Ensure the following tools are installed on your system:

* [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.html)
* [Node.js](https://nodejs.org/)
* [AWS CDK](https://docs.aws.amazon.com/cdk/v2/guide/getting_started.html)
* Java 11 or higher
* [Apache Maven](https://maven.apache.org/)
* [Jenkins](https://www.jenkins.io/) (if using CI/CD)

Install AWS CDK:

npm install -g aws-cdk


2. Local Deployment Steps
---------------------------

Follow these commands to build and deploy the CDK stack locally:

mvn clean compile          # Compile Java source
cdk bootstrap              # One-time setup for AWS environment
cdk synth                  # Synthesize CloudFormation template
cdk deploy                 # Deploy the stack to AWS


3. CI/CD Pipeline (Using Jenkins)
----------------------------------

The Jenkins pipeline automates the build and deployment of the CDK stack.

Jenkins Configuration Requirements:

* AWS credentials (via environment, IAM role, or Jenkins credentials plugin)
* CDK installed: `npm install -g aws-cdk`
* Node.js and Java (v11+) environments configured
* Maven installed

Pipeline Trigger Options:

* Manual trigger from Jenkins UI
* Trigger via Git webhook (e.g., on `git push`)
* Scheduled build (e.g., nightly or daily)
* Change detection on `*.java` files or `Jenkinsfile`

4. Assumptions
---------------

* You have AWS credentials configured using either:

  * `aws configure`
  * An IAM role attached to the machine or Jenkins instance
* You have a Jenkins server up and running
* The CDK stack defined in the project is `IotCdkJavaStack`
* CDK environment is bootstrapped using `cdk bootstrap`


5. Clean Up
-----------
To remove the deployed infrastructure:

cdk destroy
-This will delete the IoT Thing, Lambda, and Topic Rule created by the stack.

 6. Future Improvements
 ------------------------

* Add DynamoDB or S3 integration for telemetry storage
* Define CloudWatch alarms and metrics for IoT rule monitoring
* Support multiple IoT Things or dynamic provisioning
* Add automated tests for CDK stack validation
* Implement parameterization for environment-specific deployments

