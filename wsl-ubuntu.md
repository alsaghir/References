# Ubuntu & Linux Quick Reference

## References

- [Services in linux](https://www.linuxfordevices.com/tutorials/linux/start-stop-restart-services-linux)

## Tips

### Default location

- After installation, run `wsl --export Ubuntu ubuntu.tar`
- Remove installed distribution through `wsl --unregister Ubuntu`
- Import the exported distribution into custom location through `wsl --import Ubuntu D:\Apps\VMs\ubuntu\ .\ubuntu.tar`

### WSL config

- Create  a  `%UserProfile%\.wslconfig` file in Windows and use it to limit memory assigned to WSL2 VM. [Github](https://github.com/microsoft/WSL/issues/4166#issuecomment-526725261) & [Doc](https://docs.microsoft.com/en-us/windows/wsl/release-notes#build-18945).
- Reference - https://docs.microsoft.com/en-us/windows/wsl/wsl-config#configure-global-options-with-wslconfig
- [Set default user](https://superuser.com/questions/1566022/how-to-set-default-user-for-manually-installed-wsl-distro/1627461#1627461)
    - Edit `/etc/wsl.conf`
    - add the following
        ```conf
        [user]
        default=username
        ```

### Commands

```sh
# Repetitive commands
brew upgrade
brew update
sudo apt-get update
sudo apt-get upgrade
sdk update
sdk selfupdate

# IP of wsl instance
wsl -d "Ubuntu" hostname -I
ip addr show eth0 | tr '\n' '\r' | sed -r 's/.*inet\s([0-9]+\.[0-9]+\.[0-9]+\.[0-9]+\/[0-9]+).*/\1\n/'
ip addr show eth0 | grep -oP '(?<=inet\s)\d+(\.\d+){3}'

# Where java is installed
readlink -f $(which java)

# Linux Files inside of File Explorer
# OR in Windows, go \\wsl$\Ubuntu\home
explorer.exe .

# Brew
# https://brew.sh/
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Dev installations
sudo apt-get install build-essential
brew install awscli
brew install zsh-completions
brew install git
brew install gcc

# Java installation
# https://sdkman.io/install
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

sdk install java 17.0.5-amzn
sdk default java 17.0.5-amzn

cd ~
sed s'/\(^#THIS MUST BE AT THE END OF THE FILE FOR SDKMAN TO WORK\)/# Set JAVA_HOME since java executable set by SDKMAN\n\n\1/' -i .bashrc
sed s'/\(^# Set JAVA_HOME since java executable set by SDKMAN\)/\1\nexport JAVA_HOME=~\/.sdkman\/candidates\/java\/current/' -i .bashrc

source ~/.bashrc

# Install Syncthing
# Option-1
brew install syncthing
# Option-2
docker run -d --name=syncthing-wsl --network=cnetwork -p 8384:8384 -p 22000:22000/tcp -p 22000:22000/udp -p 21027:21027/udp -v /home/zxc/Sync:/var/syncthing --hostname=syncthing-wsl syncthing/syncthing:latest
```

### Startup

In .profile or whatever startup fits. Add the following

```bash
# System Variables
export M2_HOME="/usr/bin/mvn"
export JAVA_HOME="/usr/lib/jvm/java-11-openjdk-amd64/bin/"

# GWSL
export LIBGL_ALWAYS_INDIRECT=1
export WSL_ip_line=$(ipconfig.exe | grep "WSL" -n | awk -F ":" '{print $1+4}')
export DISPLAY=$(ipconfig.exe | awk -v a=$WSL_ip_line '{if (NR==a) print $NF":0.0"}' | tr -d "\r")
```

## Installations

https://gitlab.com/mmk2410/intellij-idea-ultimate/

### Intellij

```bash
sudo apt-add-repository ppa:mmk2410/intellij-idea
sudo apt-get update
sudo apt-get install intellij-idea-ultimate
sudo apt-get install openjdk-11-jdk
sudo apt install maven
sudo apt-get install libxss1 libxkbcommon-dev
```