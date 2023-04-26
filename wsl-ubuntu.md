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
- Reference - https://docs.microsoft.com/en-us/windows/wsl/wsl-config#configure-global-options-with-wslconfig. Example for docker:
    ```
    [wsl2]
    memory=8GB
    swap=0
    localhostForwarding=true
    WESTON_RDP_DISABLE_FRACTIONAL_HI_DPI_SCALING=false
    export QT_SCALE_FACTOR=3
    export GDK_SCALE=3
    WESTON_RDP_DISABLE_HI_DPI_SCALING=true
    ```
- [Set default user](https://superuser.com/questions/1566022/how-to-set-default-user-for-manually-installed-wsl-distro/1627461#1627461)
    - Edit `/etc/wsl.conf`
    - add the following
        ```conf
        [user]
        default=username
        ```
- Prevent `PATH` sharing
  - Edit `/etc/wsl.conf`
  - add the following
      ```conf
      [interop]
      appendWindowsPath = false
      ```

### Commands

```sh
# Terminate WSL
taskkill /IM wslservice.exe /F

# Repetitive commands
brew upgrade
brew update
sudo apt-get update
sudo apt-get upgrade
sdk update
sdk selfupdate

# Install ZSH
sudo apt install zsh
sh -c "$(wget https://raw.github.com/robbyrussell/oh-my-zsh/master/tools/install.sh -O -)"
# If needed, change current user shell to ZSH as the default shell manually
chsh -s $(which zsh)

# Configure zsh
cd ~
read -r -d '' zshOptions <<- EOM
# history options
setopt INC_APPEND_HISTORY      # immediately insert history into history file
EOM
sed s'/\(^#THIS MUST BE AT THE END OF THE FILE FOR SDKMAN TO WORK\)/'"${zshOptions}"'\n\n\1/' -i 

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
echo '# Set PATH, MANPATH, etc., for Homebrew.' >> ~/.zprofile
echo 'eval "$(/home/linuxbrew/.linuxbrew/bin/brew shellenv)"' >> ~/.zprofile
eval "$(/home/linuxbrew/.linuxbrew/bin/brew shellenv)"
sudo apt-get install build-essential
brew install gcc

# Dev installations
sudo apt-get install build-essential
brew install awscli
brew install zsh-completions
brew install git
brew install gcc

# Prevent store duplicates
# Bash
echo "" >> ~/.bashrc;
echo "# History config" >> ~/.bashrc;
echo "# - Prevent store duplicates" >> ~/.bashrc;
echo "# - Append to history, don't overwrite it" >> ~/.bashrc;
echo "# - Save and reload the history after each command finishes" >> ~/.bashrc;
echo "HISTCONTROL=ignoredups:erasedups" >> ~/.bashrc;
echo "shopt -s histappend" >> ~/.bashrc;
echo "PROMPT_COMMAND=\"history -n; history -w; history -c; history -r; \$PROMPT_COMMAND\"" >> ~/.bashrc;

# Java installation
# https://sdkman.io/install
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

sdk install java 17.0.5-amzn
sdk default java 17.0.5-amzn

source ~/.bashrc

# Install Syncthing
# Option-1
brew install syncthing
# Option-2
docker run -v /home/zxc/Sync:/var/syncthing -v /home/zxc/code:/code -d --name=syncthing-wsl --network=cnetwork -p 8384:8384 -p 22000:22000/tcp -p 22000:22000/udp -p 21027:21027/udp --hostname=syncthing-wsl syncthing/syncthing:latest
```

```jshelllanguage
import java.nio.charset.*;
// On Windows WSL this should be
// zhrc = "//wsl$/Ubuntu/home/zxc/.zshrc";
var zhrc = System.getProperty("user.home") + "/.zshrc";
var hist = """
        # History Options
        setopt INC_APPEND_HISTORY      # immediately insert history into history file
        
        # Set JAVA_HOME since java executable set by SDKMAN
        export JAVA_HOME=~/.sdkman/candidates/java/current/
        
        """;
var path = FileSystems.getDefault().getPath(zhrc);
var content = Files.readString(path, StandardCharsets.UTF_8);
var replaced = content.replaceFirst("#THIS MUST BE AT THE END OF THE FILE FOR SDKMAN TO WORK", hist + "$0");
Files.writeString(path, replaced, StandardCharsets.UTF_8);
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