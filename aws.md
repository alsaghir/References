# AWS

## References

- [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html)
- [AWS Command Completion](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-completion.html#cli-command-completion-windows)
- [EC2 Instance types](https://aws.amazon.com/ec2/instance-types)
- [AWS Policy Generator](https://awspolicygen.s3.amazonaws.com/policygen.html)
- [AWS Pricing Calculator](https://calculator.aws)
- [IAM Policy Simulator](https://policysim.aws.amazon.com)
- [EC2 Instance Metadata](http://169.254.169.254/latest/meta-data)

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

- Identity-based policies – Attach managed and inline policies to IAM identities (users, groups to which users belong, or roles). Identity-based policies grant permissions to an identity.

- Resource-based policies – Attach inline policies to resources. The most common examples of resource-based policies are Amazon S3 bucket policies and IAM role trust policies. Resource-based policies grant permissions to the principal that is specified in the policy. Principals can be in the same account as the resource or in other accounts.

- Permissions boundaries – Use a managed policy as the permissions boundary for an IAM entity (user or role). That policy defines the maximum permissions that the identity-based policies can grant to an entity, but does not grant permissions. Permissions boundaries do not define the maximum permissions that a resource-based policy can grant to an entity.

- Organizations SCPs – Use an AWS Organizations service control policy (SCP) to define the maximum permissions for account members of an organization or organizational unit (OU). SCPs limit permissions that identity-based policies or resource-based policies grant to entities (users or roles) within the account, but do not grant permissions.

- Access control lists (ACLs) – Use ACLs to control which principals in other accounts can access the resource to which the ACL is attached. ACLs are similar to resource-based policies, although they are the only policy type that does not use the JSON policy document structure. ACLs are cross-account permissions policies that grant permissions to the specified principal. ACLs cannot grant permissions to entities within the same account.

- Session policies – Pass advanced session policies when you use the AWS CLI or AWS API to assume a role or a federated user. Session policies limit the permissions that the role or user's identity-based policies grant to the session. Session policies limit permissions for a created session, but do not grant permissions.

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
- Assuming Role is done by calling AWS Security Token Service (STS) AssumeRole APIs (AssumeRole, AssumeRoleWithWebIdentity and AssumeRoleWithSAML). API will return temp credentials used to sign requests to AWS service APIs.
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

#### Notes

- Connect using SSH with user `ec2-user`
- [Elastic IP address](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/elastic-ip-addresses-eip.html) is a public IPv4 address, which is reachable from the internet. If your instance does not have a public IPv4 address, you can associate an Elastic IP address with your instance to enable communication with the internet. It's designed for dynamic cloud computing

---

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

- CloudFront used to cache static data in close of users and can get these data from S3.
- Route53 is for domain name management.
- Api Gateway as entry point and can delegate requests to Lambda or any backend.
- STS service used to get temp credentials for cli/sdk usage.

---

## Commands

```powershell
# Get AMI details using filter of image-id extracted from aws console
aws ec2 describe-images --owners self amazon --no-include-deprecated --filters "Name=image-id,Values=ami-0a8dc52684ee2fee2"
```