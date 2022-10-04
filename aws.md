# AWS

## References

- [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html)
- [AWS Command Completion](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-completion.html#cli-command-completion-windows)
- [EC2 Instance types](https://aws.amazon.com/ec2/instance-types/)

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

## IAM

### Users & Groups

- IAM = Identity and Access Management, **Global** service
- **Root account** created by default, shouldn’t be used or shared
- **Users** are people within your organization, and can be grouped
- **Groups** only contain users, not other groups
- Users don’t have to belong to a group, and user can belong to multiple groups

### Permissions

- **Users or Groups** can be assigned JSON documents called policies
- These policies define the **permissions** of the users
- In AWS you apply the **least privilege principle**: don’t give more permissions than a user needs

### IAM Roles for Services

- Some AWS service will need to perform actions on your behalf
- To do so, we will assign permissions to AWS services with IAM Roles
- Common roles:
  - EC2 Instance Roles
  - Lambda Function Roles
  - Roles for CloudFormation 

### IAM Policies Structure

- Managed policy is a standalone policy that is created and administered by AWS
- Inline policy is a policy that's embedded in an IAM identity (a user, group, or role). You can create a policy and embed it in an identity, either when you create the identity or later.


Consists of
- Version: policy language version, always include `2012-10-17`
- Id: an identifier for the policy (optional)
- Statement: one or more individual statements (required)
- Statements consists of
  - Sid: an identifier for the statement (optional)
  - Effect: whether the statement allows or denies access
(Allow, Deny)
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

### Notes

- Access to billing information is handled from root account > Account > IAM User and Role Access to Billing Information.
- Create a cost budget to limit the payment for your account with threshold alert

---

## EC2

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

### Security Groups

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

### Notes

- Connect using SSH with user `ec2-user`
- [Elastic IP address](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/elastic-ip-addresses-eip.html) is a public IPv4 address, which is reachable from the internet. If your instance does not have a public IPv4 address, you can associate an Elastic IP address with your instance to enable communication with the internet. It's designed for dynamic cloud computing

---

## Elastic Beanstalk

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

## VPC

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

### Security groups vs NACL

- Scope: EC2 Instance - Subnet
- Stateful (one rule for in/out) - Stateless
- Rules: allow only (all are denied by default) - allow/deny
- Order: All rules applied - First rule matches from top to bottom applies
- Defense order: SG is second layer of defense - NACL is the first layer of defense
- EC2 can have many SGs - Subnet has only one NACL
- SG allows CIDR (IP with mask), IP, SG as destination - NACL allows only CIDR as destination
- Use public NAT gateway for private subnet to allow EC2 instances to access the internet but the internet cannot initiate a request to the instance

---

## Storage

- EC2 instance store is temp while the instance is up and is directly attached.
- EBS volumes are persistent because they are network attached drives.
- Amazon Elastic File System (EFS) can be mounted onto multiple EC2 instances.

### S3

- There 6 tiers/classes for storage.
- Transition actions could be define for automated transitions from one storage class to another.

---

## Notes

- CloudFront used to cache static data in close of users and can get these data from S3.
- Route53 is for domain name management.
- Api Gateway as entry point and can delegate requests to Lambda or any backend.

---

## Commands

```powershell
# Get AMI details using filter of image-id extracted from aws console
aws ec2 describe-images --owners self amazon --no-include-deprecated --filters "Name=image-id,Values=ami-0a8dc52684ee2fee2"
```