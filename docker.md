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
```
