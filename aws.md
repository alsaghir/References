# AWS

## References

- [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html)
- [AWS Command Completion](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-completion.html#cli-command-completion-windows)
- [EC2 Instance types](https://aws.amazon.com/ec2/instance-types)
- [AWS Policy Generator](https://awspolicygen.s3.amazonaws.com/policygen.html)
- [AWS Pricing Calculator](https://calculator.aws)
- [IAM Policy Simulator](https://policysim.aws.amazon.com)
- [EC2 Instance Metadata](http://169.254.169.254/latest/meta-data)
- [Providing access to an IAM user in another AWS account that you own](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_common-scenarios_aws-accounts.html)

---

## Services Based on region

### AWS has Global Services:
- Identity and Access Management (IAM)
- Route 53 (DNS service)
- CloudFront (Content Delivery Network)
- WAF (Web Application Firewall)
### Most AWS services are Region-scoped:
- Amazon EC2 (Infrastructure as a Service)
- Elastic Beanstalk (Platform as a Service)
- Lambda (Function as a Service)
- Rekognition (Software as a Service)

Region Table: https://aws.amazon.com/about-aws/global-infrastructure/regional-product-services

1 Region = N Availability zones
1 Availability Zone = M Data centers

### Select region based on
- Compliance
- Latency
- Pricing
- Service availability

---

## Services

### IAM

- You need access key id and secret access key to access the services via API or CLI. Best practice is to leverage IAM Role rather than IAM User and access keys.
- ARN (Amazon Resource Name) format = arn:partition:service:region:account:resource
- *Service control policies (SCPs)* are a type of organization policy that you can use to manage permissions in your organization. SCPs offer central control over the maximum available permissions for all accounts in your organization. SCPs help you to ensure your accounts stay within your organization’s access control guidelines. SCPs are available only in an organization that has all features enabled. SCPs aren't available if your organization has enabled only the consolidated billing features.

#### Policies types

The following policy types, listed in order from most frequently used to less frequently used, are available for use in AWS.

- <u>**Identity-based**</u> policies – Attach managed and inline policies to IAM identities (users, groups to which users belong, or roles). Identity-based policies grant permissions to an identity.

- <u>**Resource-based**</u> policies – Attach inline policies to resources. The most common examples of resource-based policies are Amazon S3 bucket policies and IAM role trust policies. Resource-based policies grant permissions to the principal that is specified in the policy. Principals can be in the same account as the resource or in other accounts.

- <u>**Permissions boundaries**</u> – Use a managed policy as the permissions boundary for an IAM entity (user or role). That policy defines the maximum permissions that the identity-based policies can grant to an entity, but does not grant permissions. Permissions boundaries do not define the maximum permissions that a resource-based policy can grant to an entity.

- <u>**Organizations SCPs**</u> – Use an AWS Organizations service control policy (SCP) to define the maximum permissions for account members of an organization or organizational unit (OU). SCPs limit permissions that identity-based policies or resource-based policies grant to entities (users or roles) within the account, but do not grant permissions.

- <u>**Access control lists (ACLs)**</u> – Use ACLs to control which principals in other accounts can access the resource to which the ACL is attached. ACLs are similar to resource-based policies, although they are the only policy type that does not use the JSON policy document structure. ACLs are cross-account permissions policies that grant permissions to the specified principal. ACLs cannot grant permissions to entities within the same account.

- <u>**Session policies**</u> – Pass advanced session policies when you use the AWS CLI or AWS API to assume a role or a federated user. Session policies limit the permissions that the role or user's identity-based policies grant to the session. Session policies limit permissions for a created session, but do not grant permissions.

- **Identity-based policies vs resource-based policies**
  - Identity-based policies are attached to an IAM user, group, or role. These policies let you specify what that identity can do (its permissions). For example, you can attach the policy to the IAM user named John, stating that he is allowed to perform the Amazon EC2 RunInstances action. The policy could further state that John is allowed to get items from an Amazon DynamoDB table named MyCompany. You can also allow John to manage his own IAM security credentials. Identity-based policies can be managed or inline.
  - Resource-based policies are attached to a resource. For example, you can attach resource-based policies to Amazon S3 buckets, Amazon SQS queues, VPC endpoints, and AWS Key Management Service encryption keys. With resource-based policies, you can specify who has access to the resource and what actions they can perform on it. Resource-based policies are inline only, not managed.

#### Users & Groups

- IAM = Identity and Access Management, **Global** service
- **Root account** created by default, shouldn’t be used or shared
- **Users** are people within your organization, and can be grouped
- **Groups** only contain users, not other groups
- Users don’t have to belong to a group, and user can belong to multiple groups

#### Permissions

- **Users or Groups** can be assigned JSON documents called policies
- These policies define the **permissions** of the users
- In AWS you apply the **least privilege principle**: don’t give more permissions than a user needs

#### IAM Roles for Services

- Some AWS service will need to perform actions on your behalf
- To do so, we will assign permissions to AWS services with IAM Roles. Roles could be assumed by user, ec2 instances, apps...etc.
- When a user assumes a role. temp security credentials are created dynamically and provided to the user.
- Assuming Role is done by calling AWS Security Token Service (STS) AssumeRole APIs. API will return temp credentials used to sign requests to AWS service APIs.
  - AssumeRole
  - AssumeRoleWithWebIdentity
  - AssumeRoleWithSAML
  - GetSessionToken - for MFA from a user or AWS account root user
  - GetFederationToken - Obtain temp credentials for a federated user
  - GetCallerIdentity - return details about the IAM user or role used in the API call
  - DecodeAuthorizationMessage - decode error message when an AWS api is denied
- Common roles:
  - EC2 Instance Roles
  - Lambda Function Roles
  - Roles for CloudFormation

#### IAM Policies Structure

- Managed policy is a standalone policy that is created and administered by AWS. Polices created by the customer are also managed and has more control than ones created by AWS.
- Inline policy is a policy that's embedded in an IAM identity (a user, group, or role). You can create a policy and embed it in an identity, either when you create the identity or later.

- Multiple statements are combined with logical OR.
- Multiple policies are combined with logical OR.

Consists of
- Version: policy language version, always include `2012-10-17`
- Id: an identifier for the policy (optional)
- Statement: one or more individual statements (required)
- Statements consists of
  - Sid: an identifier for the statement (optional)
  - Effect: whether the statement allows or denies access (Allow, Deny)
  - Principal: account/user/role to which this policy applied to
  - Action: list of actions this policy allows or denies
  - Resource: list of resources to which the actions applied to
  - Condition: conditions for when this policy is in effect (optional)

```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "VisualEditor0",
            "Effect": "Allow",
            "Action": [
                "iam:CreatePolicy",
                "iam:GenerateCredentialReport",
                "iam:GetLoginProfile",
                "iam:ChangePassword",
                "iam:ListAccessKeys"
            ],
            "Resource": "*"
        }
    ]
}
```

#### Notes

- IAM polices are attached to user, roles and groups. Best to NOT assign polices to users as this is become hard to manage.
- Policy is DENY by default and unless explicit ALLOW is stated. DENY overrides ALLOW in policies. Priority from high to low is explicit deny > explicit allow > implicit deny.
- Multiple policies together outputs the union of these policies.
  - So S3 bucket policy with NO policy + EC2 Instance that ALLOW writing to that bucket results ALLOW as an output of union.
  - So S3 bucket policy with ALLOW policy + EC2 Instance that ALLOW writing to that bucket results DENY as an output of union because of the explicit deny.
- STS also used to [provide access to an IAM user in another AWS account that you own](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_common-scenarios_aws-accounts.html)
- Passing Role to service
  - Roles could be passed to services that has trust relationship with that Role. This is done on service setup.
  - You need IAM permission `iam:PassRole` which often comes with `iam:GetRole` to view the role being passed. Example for role that allows passing role (specified by arn) to ec2 instances
    ```json
      { "Version": "2012-10-17",
        "Statement": [
        {
          "Effect": "Allow",
          "Action": [
              "iam:PassRole",
              "ec2:*"
          ],
          "Resource": "arn:aws:iam:123456789:ro;e/S3Access"
        }
      ]
    }
    ```
  - Example of trust relationship that only lambda service can assume this role

    ```json
    { "Version": "2012-10-17",
      "Statement": [{
              "Effect": "Allow",
              "Principal": {  "Service": "lambda.amazonaws.com" },
              "Action": "sts:AssumeRole"
          }]
    }
    ```
  
    The role itself
 
    ```json
    { "Version": "2012-10-17",
      "Statement": [
          {
              "Action": [
                  "ec2:*"
              ],
              "Resource": "*",
              "Effect": "Allow"
          },
          {
              "Action": [
                  "logs:*"
              ],
              "Resource": "*",
              "Effect": "Allow"
          }
      ]
    }
    ```

- [AssumeRole vs PassRole](https://stackoverflow.com/a/63148509/7054574)
  - AssumeRole is an API action that allows a principal to get temporary credentials that allows them to then make API calls as the assumed role.
  - PassRole is not an API call itself, but rather a permission required for a principal to pass a role to another AWS service. 
  - PassRole determines who should have privileges to assign that role to a service. If there wasn’t a permission for this anybody could take any role in their account and assign it to a service. For example, someone with limited permissions could get escalated permissions by assigning an account administrator role to a lambda or EC2 instance.
- Access to billing information is handled from root account > Account > IAM User and Role Access to Billing Information.
- Create a cost budget to limit the payment for your account with threshold alert
- For debugging
  - Access advisor (user-level) shows the access by service to revise the user access. It's in the user summery as separate tab.
  - Credentials Report (account-level) generates a report that lists all your account's users and the status of their various credentials. It can be downloaded from IAM service console management page on the lift side at (Credential report).
  - Use [AWS Policy Generator](https://awspolicygen.s3.amazonaws.com/policygen.html) & [IAM Policy Simulator](https://policysim.aws.amazon.com)
  - Use --dry-run with AWS CLI commands that supports it like `aws ec2 run-instances --dry-run --image-id ami-06340c8c12baa6a09 --instance-type t2.micro` and error message could be decoded using `aws stst decode-authorization-message --encoded-message xxxxxxxxx`.
  - Use AWS IAM Access Analyzer which provides the following capabilities:
    - IAM Access Analyzer helps identify resources in your organization and accounts that are shared with an external entity.
    - IAM Access Analyzer validates IAM policies against policy grammar and best practices.
    - IAM Access Analyzer generates IAM policies based on access activity in your AWS CloudTrail logs.
- MFA with CLI could be done using `aws sts get-session-token --serial-number <mfa_device_arn> --token-code <code_from_token> --duration-seconds 3600`
- STS service used to get temp credentials for cli/sdk usage.
- Inline dynamic variables can be used to create dynamic policies like `${aws:username}` and the following example
  ```json
  {
    "Sid": "AllowAllS3ActionInUserFolder",
    "Action":["s3:*"],
    "Effect":"Allow",
    "Resource": ["arn:aws:s3:::my-company/home/${aws:username}/*"]
  }
  ``` 

---

### EC2

It mainly consists in the capability of:
- Renting virtual machines (EC2)
- Storing data on virtual drives (EBS)
- Distributing load across machines (ELB)
- Scaling the services using an auto-scaling group (ASG)

EC2 sizing & configuration options
- Operating System (OS): Linux, Windows or Mac OS
- How much compute power & cores (CPU)
- How much random-access memory (RAM)
- How much storage space:
  - Network-attached (EBS & EFS)
    - EBS volume types
      - Solid state drives (SSD) — Optimized for transactional workloads involving frequent read/write operations with small I/O size, where the dominant performance attribute is IOPS.
        - General Purpose SSD volumes (gp3 & gp2)
        - Provisioned IOPS SSD volumes (io2 & io2 Block Express & io1)
      - Hard disk drives (HDD) — Optimized for large streaming workloads where the dominant performance attribute is throughput.
        - Throughput Optimized HDD - A low-cost HDD designed for frequently accessed, throughput-intensive workloads.
        - Cold HDD - The lowest-cost HDD design for less frequently accessed workloads.
  - hardware (EC2 Instance Store)
- Network card: speed of the card, Public IP address
- Firewall rules: **security group**
- Bootstrap script (configure at first launch): EC2 User Data (run by root user)

Example of bootstrap script

```bash
#!/bin/bash
# Use this for your user data (script from top to bottom)
yum update -y
yum install -y httpd
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk i java 17.0.3.6.1-amzn
sdk i maven
printf 'export JAVA_HOME=$(which javac)' >> ~/.bashrc
```

- AWS supports 2 types of AMIs.
  - HVM (Recommended for performance)
  - Paravirtualization

- Purchasing options
  - On-Demand Instances – Pay, by the second, for the instances that you launch.
  - Savings Plans – Reduce your Amazon EC2 costs by making a commitment to a consistent amount of usage, in USD per hour, for a term of 1 or 3 years.
  - Reserved Instances – Reduce your Amazon EC2 costs by making a commitment to a consistent instance configuration, including instance type and Region, for a term of 1 or 3 years.
  - Spot Instances – Request unused EC2 instances, which can reduce your Amazon EC2 costs significantly.
  - Dedicated - The most expensive
    - Dedicated Hosts – Pay for a physical host that is fully dedicated to running your instances, and bring your existing per-socket, per-core, or per-VM software licenses to reduce costs.
    - Dedicated Instances – Pay, by the hour, for instances that run on single-tenant hardware.
  - Capacity Reservations – Reserve capacity for your EC2 instances in a specific Availability Zone for any duration.

#### Security Groups

- Security Groups are the fundamental of network security in AWS
- They control how traffic is allowed into or out of our EC2 Instances.
- Security groups only contain rules
- Security groups rules can reference by IP or by security group
- Security groups are acting as a “firewall” on EC2 instances
- They regulate:
  - Access to Ports
  - Authorized IP ranges – IPv4 and IPv6
  - Control of inbound network (from other to the instance)
  - Control of outbound network (from the instance to other)
- 0.0.0.0/0 & ::/0 means any
- Can be attached to multiple instances
- Locked down to a region / VPC combination
- Does live “outside” the EC2 – if traffic is blocked the EC2 instance won’t see it
- **It’s good to maintain one separate security group for SSH access**
- If your application is not accessible (time out), then it’s a security group issue
- If your application gives a “connection refused“ error, then it’s an application
error or it’s not launched
- All inbound traffic is **blocked** by default
- All outbound traffic is **authorized** by default

#### ELB (Elastic Load Balancer)

- ALP (Application Load Balancer) - HTTP/HTTPS
- NLB (Network Load Balancer) - TCP & High performance
- CLB (Classic Load Balancer) - HTTP/HTTPS/TCP
- GLP (Gateway Load Balancer)

#### Notes

- Connect using SSH with user `ec2-user`
- [Elastic IP address](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/elastic-ip-addresses-eip.html) is a public IPv4 address, which is reachable from the internet. If your instance does not have a public IPv4 address, you can associate an Elastic IP address with your instance to enable communication with the internet. It's designed for dynamic cloud computing

---

### Route 53

- Amazon DNS Service that allows mapping domain name that you own to
  - EC2 Instance
  - Load Balancers
  - S3 Buckets

### RDS

- Multi-AZ: Used for disaster recovery
- Read Replicas: Used for scaling and performance

### S3

- It's object-based
- File size can be up to 5 TB
- Unlimited storage
- S3 is universal namespace
- Object considered key + value + version id + metadata

#### [Storage classes](https://aws.amazon.com/s3/storage-classes/)

- Standard (Highest cost) - Suitable for most workloads (e.g., websites, content distribution, mobile and gaming applications, and big data analytics).
- Standard-Infrequent Access - Long-term, infrequently accessed critical data (e.g., backups, data store for disaster recovery files, etc.). Minimum storage duration: 30 days.
- One Zone-Infrequent Access - Long-term, infrequently accessed, non-critical data. Minimum storage duration: 30 days.
- Glacier Instant Retrieval - Long-lived data, accessed approximately once per quarter, and needs millisecond retrieval time. Minimum storage duration: 90 days.
- Glacier Flexible Retrieval - Long-term data archiving that occasionally needs to be accessed within a few hours or minutes. Minimum storage duration: 90 days.
- Glacier Deep Archive - Rarely accessed data archiving with a default retrieval time of 12 hours (e.g., financial records for regulatory purposes). Minimum storage duration: 180 days.
- Intelligent-Tiering (Cost optimized for unknown access patterns) - Unknown or unpredictable access patterns. Minimum storage duration: 30 days.

#### ACL

- While bucket policy controls access to buckets, ACLs control access on object level.
- ACL capable of fine-grained control for user/groups/buckets.
- Access logs are not enabled by default.

#### S3 Encryption

- Encryption in transit
  - SSL/TLS
  - HTTPS
- Client-Side Encryption
- Encryption at Rest: SSE (Server-Side Encryption)
  - SSE-S3 (AES 256-bit) (S3 Managed Keys)
  - SSE-KMS (AWS Key Management Service, Managed Keys)
  - SSE-C (Server Side Encryption with Customer Provided Keys)
- Enforcing Encryption with a Bucket Policy by explicitly deny requests that do not include the `x-amz-server-side-encryption` parameter in the request header. Deny requests that do not use `aws:SecureTransport` to enforce the use of HTTPS/SSL. Two options are currently available:
  - x-amz-server-side-encryption: AES256 (SSE-S3 - S3 managed keys)
  - x-amz-server-side-encryption: aws:kms (SSE-KMS - KMS managed keys)

### Elastic Beanstalk

### Overview

- Managed Services

- Three architecture models
 - Singe instance deployment : good for dev
 - LB + ASG (Load balancer + Auto Scaling Group) : good for production
 - ASG only : god for non-web apps in production like workers, messages consumers...etc.

---

ECS vs EKS

- An EC2 instance with the ECS agent installed and configured is called a container instance. In Amazon EKS, it is called a worker node.
- An ECS container is called a task. In Amazon EKS, it is called a pod.
- While Amazon ECS runs on AWS native technology, Amazon EKS runs on top of Kubernetes.

---

### VPC

- VPC is region specific across multiple availability zones
- VPC = region + IP range
- Subnet = VPC + AZ + IP range
- Internet gateway (IGW) attached to a VPC
- VGW  is a VPN access to the VPC
- Route Tables control routing and has enabled local routes by default for each created VPC
- Routing table record specify that when destination is X then route it to target Y.
- To allow routing from internet to IGW add routing table with destination 0.0.0.0/0 and IGW name as destination
- To apply routing table, associate it with wanted subnets
- Network ACL is firewall at subnet level

#### Security groups vs NACL

- Scope: EC2 Instance - Subnet
- Stateful (one rule for in/out) - Stateless
- Rules: allow only (all are denied by default) - allow/deny
- Order: All rules applied - First rule matches from top to bottom applies
- Defense order: SG is second layer of defense - NACL is the first layer of defense
- EC2 can have many SGs - Subnet has only one NACL
- SG allows CIDR (IP with mask), IP, SG as destination - NACL allows only CIDR as destination
- Use public NAT gateway for private subnet to allow EC2 instances to access the internet but the internet cannot initiate a request to the instance

---

### Storage

- EC2 instance store is temp while the instance is up and is directly attached.
- EBS volumes are persistent because they are network attached drives.
- Amazon Elastic File System (EFS) can be mounted onto multiple EC2 instances.

#### S3

- There 6 tiers/classes for storage.
- Transition actions could be define for automated transitions from one storage class to another.

---

## Notes

- Retry should be done on 5xx server errors only. 
- CloudFront used to cache static data in close of users and can get these data from S3.
- Route53 is for domain name management.
- Api Gateway as entry point and can delegate requests to Lambda or any backend.
- STS service used to get temp credentials for cli/sdk usage.
- AWS Limits
  - API Rate Limits
    - DescribeInstances API for EC2 has a limit of 100 calls per seconds
    - GetObject on S3 has a limit of 5500 GET per second per prefix
    - For Intermittent Errors: implement Exponential Backoff 
    - For Consistent Errors: request an API throttling limit increase
  - Service Quotas (Service Limits)
    - Running On-Demand Standard Instances: 1152 vCPU
    - Service limit increase can be requested by opening a ticket
    - Service quota increase can be requested programmatically by using the Service Quotas API

---

## Commands

```sh
# Get AMI details using filter of image-id extracted from aws console
aws ec2 describe-images --owners self amazon --no-include-deprecated --filters "Name=image-id,Values=ami-0a8dc52684ee2fee2"

# Assume role SAML
aws sts assume-role --role-arn "arn:aws:iam::855389350164:role/dxDeveloper" --role-session-name AWSCLI-Session
export AWS_ACCESS_KEY_ID="Key ID Here";
export AWS_SECRET_ACCESS_KEY="Access Key Here";
export AWS_SESSION_TOKEN="Token Here";

# Assume role as one command
export $(printf "AWS_ACCESS_KEY_ID=%s AWS_SECRET_ACCESS_KEY=%s AWS_SESSION_TOKEN=%s" \
$(aws sts assume-role \
--role-arn arn:aws:iam::855389350164:role/dxDeveloper \
--role-session-name AWSCLI-Session \
--query "Credentials.[AccessKeyId,SecretAccessKey,SessionToken]" \
--output text))

# Get credentials to set in environment variables above
aws sts get-caller-identity

# Invoke Lambda function
aws lambda invoke --region=eu-west-1 --function-name arn:aws:lambda:eu-west-1:855389350164:function:dal-ent-til-client-lambda-dev1 --invocation-type RequestResponse --payload fileb://req.json response.json

# saml2aws
brew install saml2aws
saml2aws login --skip-prompt --role=arn:aws:iam::855389350164:role/vfDeveloperReadOnly
saml2aws configure
saml2aws login --username <userName> --password <password> --role arn:aws:iam::855389350164:role/vfDeveloperReadOnly --session-duration 3600
```
