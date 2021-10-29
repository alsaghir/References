# Docker

## References

- Installation : <https://docs.docker.com/engine/install/centos/#install-using-the-repository>

### Installation on CentOS

```bash
sudo dnf config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo

sudo dnf install docker-ce docker-ce-cli containerd.io

sudo systemctl start docker
sudo systemctl enable --now containerd.service
sudo systemctl enable --now docker.service
sudo systemctl status docker.service

# If you would like to use Docker as a non-root
# user, you should now consider adding
# your user to the “docker” group with something like:
sudo usermod -aG docker zxc

# Docker compose
# https://docs.docker.com/compose/install/
sudo curl -L "https://github.com/docker/compose/releases/download/1.28.5/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

sudo chmod +x /usr/local/bin/docker-compose

# testing :D
docker-compose --version

# Auto Completion
# https://docs.docker.com/compose/completion/
sudo dnf install bash-completion
sudo curl -L https://raw.githubusercontent.com/docker/compose/1.28.5/contrib/completion/bash/docker-compose -o /etc/bash_completion.d/docker-compose
```

### Rules

- Always create a network bridge and don't use the default one for your apps. The default bridge network does not have a built-in DNS service. So you'd have to use `--link` to link containers to each other using their names.
- Make `RUN` in Dockerfile merged as much as possible.
- Dockerfile : Keep things to be changed the most at the bottom while things to be changed the least at the top.
- Dockerfile : Logging into container stdout and not local container file.
- Dockerfile : `RUN npm install && npm cache clean` is same as `RUN npm install; npm cache clean`. Difference is that `;` prevent dependency of the second command to run if the first is successful.
- Dockerfile sample

    ```DockerFile
    # Logging directed to stdout and stderr
    RUN ln -sf /dev/stdout /var/log/nginx/access.log \
	&& ln -sf /dev/stderr /var/log/nginx/error.log
    ```

### Commands

```bash
# Execute command in running container
# and also enter interactive mode
docker container exec -it <container_name> bash

# Show details of one container config
docker container inspect <container_name>

# Show the logs of container
# Use --follow to tail the logs
docker container logs <container_name>

# Show running containers
docker container ls

# Show all containers
docker container ls -a

# Show ports used by a container
docker container port <container_name>

# Remove multiple containers at once
# Use -f to force removing running containers
docker container rm <container_id> <container_id> <container_id>

# Run container from nginx image
# and mapping port 80 on host to port 80 on guest
docker container run --publish 80:80 nginx

# Run container from nginx image with CMD override
# to start interactively using bash
docker container run -it --publish 80:80 nginx bash

# Create bind mount from current path to /app
# and create unnamed volume to /app/node_modules
# preventing mapping /app/node_modules in the container
# to anything in the host and leaving it there untouched
# but it's actually mapped to the unnamed volume path on the host
docker container run -v /app/node_modules -v $(pwd):/app <image_name>

# Start stopped container interactively. ai for usage with start.
docker container start -ai --publish 80:80 nginx bash

# Performance stats for all containers
docker container stats

# Stop running container
docker container stop <container_id>

# Show processes inside a container
docker container top <container_name>

# Build an image out of the current directory using
# existing DockerFile passing environment
# variables while doing so
docker image build --build-arg VAR_NAME1=VAR_VALUE1 VAR_NAME2=VAR_VALUE2

# To build image with tag, repo and image name
# The dot in the end for the current location
# Build is done from Dockerfile
docker image build -t <REPOSITORY_NAME>/<IMAGE_NAME>:<TAG> .

# Show the history of an image layers
docker image history <image_id>:<tag>

# Details about an image
docker image inspect <image_id>

# List Images
docker image ls

# Remove unused images
docker image prune

# Pull image from repo without running it
docker image pull nginx

# Push image to docker hub
docker image push <username>/<repo>
docker image push <username>/<repo>:<tag>

# Remove an image
docker image rm webserver:1.0

# To tag an image specify the source image
# and tag then the target image and tag
# For example here the source is ubuntu:16.10
# and the target is webserver:1.0
# Another example is tagging an image you already built
# and this could be done same way but the source
# would be the image id that you built
docker image tag ubuntu:16.10 webserver:1.0
docker image tag 3ee637e54b4f webserver:1.0

# Tag an image you got with your own name and tag.
# The <image_name> is the source image
docker image tag <image_name> <username>/<repo>:<tag>

# Login to docker hub
docker login

# Login to specific server
docker login <server_url>

# Details about specific network
docker network inspect <network_name>

# list networks
docker network ls

# Create network
docker network create <network_name>

# Connect a container to specific network
docker network connect <network_id> <container_id>

# Info about specific volume
docker volume inspect <volume_name>

# List volumes
docker volume ls

# Remove unused volumes
docker volume prune
```

#### Docker container run options

`--publish 8080:80` Maps port 8080 on host to port 80 on containers. It's in `<host>:<container>` format.  
`-publish-all` Publish all exposed ports to random ports.  
`-d` or `-detach` Run container in detached mode.  
`--name` Specify a name to a container.  
`-e ENV_VARIABLE=value` specify environment variable for this container when it gets run.  
`-it` Interactive container while `-i` for STDIN attaching and `-t` for terminal output attaching.  
`--network <network_name>` Select network to attach the container to it.  
`--rm` Automatically remove the container when it exits.  
`--net-alias` or `--network-alias` Assign another name used in network besides the container name.  
`-v` Volume applying its configs. See Volumes section below for more info.  
`-dit` is shorthand for detached mode which run command in the background and interactive to keep STDIN open even if not attached and finally Allocate a pseudo-TTY.

### Networking

Drivers types of containers networks

- Bridge - Default bridge network created for all containers
- None - Isolated containers
- Host - Connected directly to host network

### DockerFile

- Files to copy from host must be in the working directory and not outside.
- Recommended order is
    - FROM - Base image
    - LABEL
    - ARG
    - ENV - Environment variables
    - RUN - run commands on the container
    - WORKDIR - Like `CD`. Preferred over using `RUN cd /some/path`
    - USER
    - COPY - From host to container.
    - ADD
    - EXPOSE - Open ports from container to virtual network. Just allowing packets to be received on specified container port(s).
    - CMD - Run command when launching the container. Use json array format.
    - HEALTHCHECK - To check the app inside the container is running in proper way.

- Examples

    ```Dockerfile
    # Every 5 seconds ping to the API endpoint or exit with status 1 if timeout of 3 seconds is reached
    HEALTHCHECK --interval=5s --timeout=3s CMD curl --fail http://localhost:8091/pools || exit 1
    ```

- Sample
    ```DockerFile
    # Source of the image
    FROM ubuntu

    # Parameter could be passed from command line
    # Should be alway before its equal ENV variable
    ARG JQUERY_VERSION=3.2.0
    ENV JQUERY_VERSION=${JQUERY_VERSION}

    # Environment variable. Good to be at the beginning
    ENV DOC_ROOT /var/www/whatever

    # Specify user:group or uid:guid
    USER uu:gg

    # File in host to file in container
    COPY resources/file.css /tmp/file.css

    # Like copy but supports http source
    # and extracting tar.gz source files
    # on the fly
    ADD http://some-website.com/whatever.css ${DOC_ROOT}/whatever.css
    ```

### Images

- TAG represents a snapshot of the image.
- Images are not necessary named. But they are tagged.
- Image is made of layers. Each layer (say in DockerFile) has a unique SHA. These which are stored and cached.
- Containers layers is read-write while image layer is read only. So changing file in a running container will make docker copies the file to container layer to be saved and written.
- Image layers are read only so they can't be changed. Any changes are made to the top read-write container layer.
- Images identified using ID or using `<user>/<repo>:<tag>`. Using `ls` for images shows repository as `<user>/<repo>` pattern.
- First image to create from zero ground and not from containers or previous images could be done in two ways
    - Create image from an empty special image called `scratch`.
    - Use `docker import` to load tarball (of existing server) into Docker.

### Volumes & Bind mounts

- Docker volumes are configuration option for a container that creates a special location outside of that container's union file system to store unique data. Containers sees it like local file path.
- Bind mounts is simply sharing or mounting a host directory or file into a container.
- Volume need manual deletion. No auto-cleaning could be done by docker.
- Using `-v` with `docker container run` allows configuration of volume for the container about to be up. 
    - `-v /var/lib/mysql` creating unnamed volume mounting to specified path on container. Also used to mark the path `/var/lib/mysql` as 'do not  touch' folder and do not map it to anything.
    - `-v my-sqldb:/var/lib/mysql` creates named volume.
    - `-v /Users/bret/stuff:/path/container` create a bind mount volume.
    - `--mount type=bind,source=/whatever,target=/whatever` New syntax instead of using `-v`.
- Bind mounting only works with `docker container run`. It's not possible in Dockerfile. But possible in docker compose.
- Using `docker volume create whatever` creates a named volume before running the container. It's pretty rare to need to do this step at all.

### Docker Compose

- Controls multiple containers relationships
- Formed of 2 parts
    - Yaml formatted file describe containers, networks and volumes.
    - `docker-compose` CLI tool
- Config file name is `docker-compose.yml`. Use -f to specify another file.
    
    ```yml
    # https://docs.docker.com/compose/compose-file/compose-versioning/
    version: '3.1'  # if no version is specified then v1 is assumed. Recommend v2 minimum

    services:  # containers. same as docker run
    servicename: # a friendly name. this is also DNS name inside network
        image: # Optional if you use build:
        command: # Optional, replace the default CMD specified by the image
        environment: # Optional, same as -e in docker run
        volumes: # Optional, same as -v in docker run. See example
            # - /app/node_modules
            # - .:/app
    servicename2:
        image: # Optional to specify a name for the image to be created from dockerfile.
        build: # for building from Dockerfile
            context: . # Means build here
            dockerfile: # specify docker file name

    volumes: # Optional, same as docker volume create. For named volumes mostly.

    networks: # Optional, same as docker network create
    ```
- Use `docker-compose up` for setup of volumes/networks and start all containers. Use `docker-compose down` to stop all containers and remove containers/volumes/networks.
- Use `docker-compose.override.yml` to automatically picked up by docker compose. Notice it inherits the `docker-compose.yml`.
- `docker compose -f docker-compose.yml -f docker-compose-test.yml up -d` to give a base docker-compose file and its override. It must be in order. You may use `config` to combine files into a single compose file like `docker compose -f docker-compose.yml -f docker-compose-test.yml config`.

### Docker Swarm

- A swarm consists managers and workers nodes. A manager node is a node wit raft database. Manager is actually a worker node with more features/privileges.
- Each node is a linux machine for example.
- `service` command replaces the `docker container run` command. Each service fire up some tasks. Each task run a container.
- Verify if swarm is enabled using `docker info` and enable it using `docker swarm init`.
- `docker node` command converts nodes from/to mangers and workers. It also used for bringing servers in and out of the swarm.
- `docker service` command replaces the docker run.
- By using the `bridge` we aggregated both network in a single unit (on a high level). Bridge can even be used to create connection between networks on same host. This is generally required if the host is running multiple containers. `Overlay` networks are usually used to create a virtual network between two separate hosts. Virtual, since the network is build over an existing network.
- As Example, consider, you have multiple docker host running containers in which each docker host has its own internal private bridge network allowing the containers to communicate with each other however, containers across the host has no way to communicate with each other unless you publish the ports on those containers and set up some kind of routing yourself. This is where overlay network comes into play. With docker swarm you can create an overlay network which will create an internal private network that spans across all the nodes participating in the swarm network we could attach a container or service to this network using the network option while creating a service. So, the containers across the nodes can communicate over this overlay network.
- Services `Stacks` is an abstraction to Swarm mode that allows using compose files for docker swarm. `deploy:` is used since we can't use `build:` in this case.
- Compose ignores `deploy:` while Swarm ignores `build:`. Building images is not the job of Swarm. Images should be ready for Swarm to deploy and use.
- `Stacks` actually is multiple services, volumes & overlay networks. It actually replaces the `docker service` command.
- Secrets could be passed on service creation using `--secret <secret_name>`. Then you might use the secret as environment variable like `POSTGRESS_PASSWORD_FILE=/run/secrets/<secret_name>`. Note that secret must be created first. Remove secret using `docker service update --secret-rm` and this will cause a re-deployment of a container.
- Stack deploy is also considered updating the stack command.

Swarm

```bash
# List nodes
docker node ls

# Set node as specific role for example
# setting node2 as manager
docker node update --role <role_name> <node_name>
docker node update --role manager node2

# Creating overlay network for swarm 
# for containers to connect to each other
docker network create --driver overlay <network_name>

# Start alpine container from alpine
# image with command ping 8.8.8.8
docker service create alpine ping 8.8.8.8

# Start alpine container from alpine image with command ping 8.8.8.8
# connecting to specific network and giving a name to the service
# specifying named volume with source as its name (in case of bind mount then source
# is source path) and target is the target path
docker service create --name <service_name> --network <network_name> --mount type=volume,source=<volume_name>,target=/var/lib/postgresql alpine ping 8.8.8.8

# Change number of replicas of two services (web/api)
docker service scale web=8 api=6

# List services
docker service ls

# List services with nodes details
docker service ps <service_name>

# Update service changing the number
# of replicas
docker service update <service_id> --replicas 3

# Just update the image used to a newer version
docker service update --image nginx:1.13.6 <service_name>

# Adding an environment variable and remove a port
docker service update --env-add NODE_ENV=production --publish-rm 8080

# No change but re-balance the resources on tasks and nodes
docker service update --force <service-name>

# Initialize swarm using specific ip
# for connection of swarm nodes
# Copy the result to other nodes and apply it
# to make every node join the same swarm
docker swarm init --advertise-addr 104.236.114.90

# Get the command token to apply to any node
# that you want to become a manager in the swarm
# and join the swarm at once
docker swarm join-token manager
```

Stacks Sample app yml file and architecture.

```yml
# https://docs.docker.com/compose/compose-file/compose-versioning/
version: "3"
services:

  redis:
    image: redis:alpine
    networks:
      - frontend
    deploy: # New option for stacks only
      replicas: 1
      update_config:
        parallelism: 2
        delay: 10s
      restart_policy:
        condition: on-failure
  db:
    image: postgres:9.4
    volumes:
      - db-data:/var/lib/postgresql/data
    networks:
      - backend
    environment:
      - POSTGRES_HOST_AUTH_METHOD=trust
    deploy:
      placement:
        constraints: [node.role == manager] # To be on specific node
  vote:
    image: bretfisher/examplevotingapp_vote
    ports:
      - 5000:80
    networks:
      - frontend
    depends_on:
      - redis
    deploy:
      replicas: 2
      update_config:
        parallelism: 2
      restart_policy:
        condition: on-failure
  result:
    image: bretfisher/examplevotingapp_result
    ports:
      - 5001:80
    networks:
      - backend
    depends_on:
      - db
    deploy:
      replicas: 1
      update_config:
        parallelism: 2
        delay: 10s
      restart_policy:
        condition: on-failure

  worker:
    image: bretfisher/examplevotingapp_worker:java
    networks:
      - frontend
      - backend
    depends_on:
      - db
      - redis
    deploy:
      mode: replicated
      replicas: 1
      labels: [APP=VOTING]
      restart_policy:
        condition: on-failure
        delay: 10s
        max_attempts: 3
        window: 120s
      placement:
        constraints: [node.role == manager]

  visualizer:
    image: dockersamples/visualizer
    ports:
      - 8080:8080
    stop_grace_period: 1m30s
    networks:
      - frontend
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock
    deploy:
      placement:
        constraints: [node.role == manager]

networks:
  frontend:
  backend:

volumes:
  db-data:
```

Architecture

![Sample app architecture](docker_assets/architecture.png "Sample app architecture")

Commands

```bash
# Create a secret using a file
docker secret create <secret_name> whateverFile.txt

# Create a secret using from stdin
echo "passwordExample" | docker secret create <secret_name> -

# List created secrets
docker secret ls

# To execute the compose file
docker stack deploy -c example-whatever.yml <stack_name>

# List services up and running with their running nodes
docker stack ps <stack_name>

# List services up and running
docker stack services <stack_name>
```

Sample compose file with secrets

```yml
version: "3.1"

services:
  psql:
    image: postgres
    secrets:
      - psql_user
      - psql_password
    environment:
      POSTGRES_PASSWORD_FILE: /run/secrets/psql_password
      POSTGRES_USER_FILE: /run/secrets/psql_user

secrets:
  psql_user:
    file: ./psql_user.txt # File on actual host machine beside this file
  psql_password:
    file: ./psql_password.txt
```

### Health check

Command line

```bash
docker run \ 
 --health-cmd="curl -f localhost:9200/_cluster/health || false" \ # false to return 1 since that's the only thing docker will do anything about. The port here is internal to the container.
 --health-interval=5s \ 
 --health-retries=3 \ 
 --health-timeout=2s \ 
 --health-start-period=15s \ 
 elasticsearch:2 
```

Options for Dockerfile

`--interval=DURATION` (default: 30s)  
`--timeout=DURATION` (default: 30s)  
`--start-period=DURATION` (default: 0s) (17.09+)  
`--retries=N` (default: 3)

Basic command using default options

`HEALTHCHECK curl -f http://localhost/ || false`

Custom options with the command. Note there is `CMD` because we are using options like `--timeout` so CMD is mandatory and after the options

`HEALTHCHECK --timeout=2s --interval=3s --retries=3 \ CMD curl -f http://localhost/ || exit 1`

Example is health check in nginx Dockerfile

```Dockerfile
FROM nginx:1.13

HEALTHCHECK --interval=30s --timeout=3s CMD curl -f http://localhost/ || exit 1 
```

Health check in compose / stack file

```yml
version: "2.1" (minimum for healthchecks) 
services: 
 web: 
 image: nginx 
 healthcheck: 
 test: ["CMD", "curl", "-f", "http://localhost"] 
 interval: 1m30s 
 timeout: 10s 
 retries: 3 
start_period: 1m #version 3.4 minimum
```

### Tips

- Use `registry` image to make local registry like docker-hub and use volume with it mapping to `/var/lib/registry` to save images in it. Use tag `<IP>:<PORT>/<NAME>` for the image to push it directly to the docker local registry.
- Use `USER` stanza in your Dockerfile. If group and user not created by your programming language platform image then create one and use it. Container will run using root by default !

--- 

# Kubernetes

## Components

- API Server
- ETCD - Key-Value store
- Kubelet - Agent that runs on each node in the cluster
- Container runtime - Like docker, rkt or cri-o
- Conteroller - the brain
- Schedular

## Master vs Worker Nodes

Master has `kube-apiserver` while worker node has kubelet interacting with each other. ETCD stores values on the master. The master has also the controller and schedular. The worker node got the container runtime installed on it.

## Kubectl

### Info

- Pod usually is wrapping a container. However, sometimes helper container for an application is required and can be also part of the same pod to be in the same state and life-cycle as the main container. Both containers in that case can communicate via referring to `localhost`. Storage, fate, network & namespace.
- K8s uses YAML to manage its resources like PODs, Replicas, Deployments, Services ...etc.
- YAML always container 4 top level fields
  - apiVersion
    - POD - v1
    - Service - v1
    - ReplicaSet - apps/v1
    - Deployment - apps/v1
  - kind - Pod, ReplicaSet, Deployment or Service
  - metadata - Some other properties like name, labels..etc.
  - spec - Different for each resource type. In Pod type, this section will has container data.
- Bash auto completion - [Documentation](https://kubernetes.io/docs/tasks/tools/included/optional-kubectl-configs-bash-linux/).
- Different between `apply`, `create`, `patch` & `replace` - [Documentation](https://kubernetes.io/docs/concepts/overview/working-with-objects/object-management/).
- Services are responsible of communication between resources. For example `NodePort` service for external access to a resource. Others lik `ClusterIP` & `LoadBalancer` More at [Documentation](https://kubernetes.io/docs/concepts/services-networking/service/#publishing-services-service-types).
- Service is created among all nodes.

### Scripts

#### Commands

```bash
# Deploy apps on the cluster
kubectl run hello

# Make a pod automatically and start container
# in it. Image name is references
# to be pulled from docker hub
kubectl run nginx --image nginx

# Info commands
kubectl cluster-info
kubectl get nodes
kubectl get pods
kubectl get pods -o wide
kubectl describe pod nginx
kubectl get replicasets.apps
kubectl describe replicaset myapp-rs
kubectl get deployments
kubectl get services
kubectl get all
kubectl rollout status deployment/myapp-deployment
kubectl rollout history deployment/myapp-deployment

# Create a  or any resource from yml file
kubectl create -f pod-definition.yml
kubectl apply -f pod-definition.yml

# Add the command as history recorded when creating a resource
kubectl create -f pod-definition.yml --record

# Replace a resource by file name
kubectl replace -f replicaset-def.yml

# Change replica set scale number without changing the configuration file
kubectl scale --replicas=6 -f replicaset-def.yml
kubectl scale --replicas=6 replicaset myapp-rs

# Don't create actual POD and output config
# as yaml
kubectl run nginx --image=nginx --dry-run=client -o yaml

# Edit POD/ReplicaSet config
kubectl edit pod nginx
kubectl edit replicaset myapp-rs

# Revert to previous deployment
kubectl rollout undo deployment/myapp-deployment

# Generate service configuration
# of type NodePort
kubectl expose deployment webapp-deployment --name=webapp-service --target-port=8080 --type=NodePort --port=8080 --dry-run=client -o yaml
```

#### YML

Creating a POD

```yml
apiVersion: v1
kind: Pod
metadata:
  name: myapp-pod
  labels:
    app: myapp
    type: front-end
spec:
  containers:
    - name: nginx-container
    image:nginx

```

- Creating a replica-set. POD config now under `template` key. Also the metadata could be put under `template` section. Selector is mandatory and it allows replication take into its count other resources based on label filtration.
- Deployment has same exact file structure with replacement of `kind` value to be `Deployment` instead of `ReplicaSet`.

```yml
apiVersion: apps/v1
kind: ReplicaSet
metadata:
  name: myapp-rs
  labels:
    app: myapp
    type: front-end
spec:
  replicas: 5
  template:
    metadata:
      name: myapp-pod
      labels:
        app: myapp
        type: front-end # Must match X
    spec:
      containers:
        - name: nginx-container
          image: nginx
  selector:
    matchLabels:
      type: front-end # Must match X
```

Service to forward node port to a pod port

```yml
apiVersion: v1
kind: Service
metadata:
  name: myapp-service
spec:
  type: NodePort
  ports:
   - targetPort: 80
     port: 80
     nodePort: 3008
  selector: # links the service to the pod labeled with these values
    app: myapp
    type: front-end
```

Service to of type `ClusterIP` which enables access to it by creating virtual IP address on the cluster level to communicate through it (or by service name).

```yml
apiVersion: v1
kind: Service
metadata:
  name: myapp-service
spec:
  type: ClusterIP
  ports:
   - targetPort: 80
     port: 80
  selector: # links the service to the pod labeled with these values
    app: myapp
    type: back-end
```

Service to of type `LoadBalancer` is similar to `NodePort` as it provides single access IP/URL and a node port for users/external access. Perfect use-case for it is front-end multiple nodes.

```yml
apiVersion: v1
kind: Service
metadata:
  name: myapp-service
spec:
  type: LoadBalancer
  ports:
   - targetPort: 80
     port: 80
     nodePort: 3008
  selector: # links the service to the pod labeled with these values
    app: myapp
    type: front-end
```