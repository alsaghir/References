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

# Remove multiple containers at once
# Use -f to force removing running containers
docker container rm <container_id> <container_id> <container_id>

# Run container from nginx image
# and mapping port 80 on host to port 80 on guest
docker container run --publish 80:80 nginx

# Run container from nginx image with CMD override
# to start interactively using bash
docker container run -it --publish 80:80 nginx bash

# Start stopped container interactively
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
`-d` or `-detach` Run container in detached mode.  
`--name` Specify a name to a container.  
`-e ENV_VARIABLE=value` specify environment variable for this container when it gets run.  
`-it` Interactive container while `-i` for STDIN attaching and `-t` for terminal output attaching.  
`--network <network_name>` Select network to attach the container to it.  
`--rm` Automatically remove the container when it exits.  
`--net-alias` or `--network-alias` Assign another name used in network besides the container name.  
`-v` Volume applying its configs. See Volumes section below for more info.

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
    - RUN
    - WORKDIR - Like `CD`.
    - USER
    - COPY - From host to container.
    - ADD
    - EXPOSE - Open ports from container to virtual network. Just allowing packets to be received on specified container port(s).
    - CMD
- Keep things to be changed the most at the bottom while things to be changed the least at the top.
- Dockerfile best practices
    
    ```DockerFile
    # Logging directed to stdout and stderr
    RUN ln -sf /dev/stdout /var/log/nginx/access.log \
	&& ln -sf /dev/stderr /var/log/nginx/error.log
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
- Images identified using ID or using `<user>/<repo>:<tag>`. Using `ls` for images shows repository as `<user>/<repo>` pattern.

### Volumes & Bind mounts

- Volume need manual deletion. No auto-cleaning could be done by docker.
- Using `-v` with `docker container run` allows configuration of volume for the container about to be up.
    - `-v /var/lib/mysql` creating unnamed volume mounting to specified path on container.
    - `-v my-sqldb:/var/lib/mysql` creates named volume.
    - `-v /Users/bret/stuff:/path/container` create a bind mount volume.
    - `--mount type=bind,source=/whatever,target=/whatever` New syntax instead of using `-v`.
- Bind mounting only works with `docker container run`. It's not possible in Dockerfile.
- Using `docker volume create whatever` creates a named volume before running the container. It's pretty rare to need to do this step at all.