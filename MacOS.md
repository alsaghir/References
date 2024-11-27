# MacOS

## Auth

```shell
kinit && mwinit

# For not using the VPN
mwinit --aea
```

## SSH Config

Example of `~/.ssh/config`

```config
Host github.com
  AddKeysToAgent yes
  IdentityFile ~/.ssh/id_rsa


# Bastion
Host bastion-non-prod-1
  ServerAliveInterval 240 # keep-alive flag interval for preventing connection getting closed
  AddKeysToAgent yes # a private key that is used during authentication will be added to ssh-agent if it is running. http://www.openssh.com/txt/release-7.2
  UseKeychain yes # UseKeychain option was introduced in macOS Sierra allowing users to specify whether they would like for the passphrase to be stored in the keychain
  ForwardAgent yes # Allowing ssh server to act as you while you’re connected (Through your private/public keys on your machine). This doesn’t send your private keys over the internet, not even while they’re encrypted; it just lets a remote server access your local SSH agent and verify your identity.
  User             ahmed.elsagheer2
  HostName         non-prod-1-bastion.non-prod-1.digital.vodafoneaws.co.uk
  IdentityFile     /Users/elsagheera/.ssh/.ssh/ahmedalsaghir.pem # Private key
  StrictHostKeyChecking no # SSH client connects only to SSH hosts listed in the known host list. It rejects all other SSH hosts. The SSH client connects using SSH host keys stored in the known hosts’ list in strict host key checking mode
  UserKnownHostsFile=/dev/null # By default, the known_hosts file for a given user is located at $HOME/.ssh/known_hosts:
  DynamicForward   8080 # DynamicForward. Specifies that a TCP port on the local machine be forwarded over the secure channel, and the application protocol is then used to determine where to connect to from the remote machine. In command-line, this is equal to -D 8080
  LocalForward 1521 wcsdb-rds.dx-dev.internal.vodafoneaws.co.uk:1521 # Access the wcs-db on the dev environment locally by using localhost:1521
```

## Commands

```sh
# Dev installations
brew analtrics off

# Update to latest versions
brew update --auto-update
brew outdated -g
brew upgrade --greedy
brew upgrade visual-studio-code
brew cleanup

# https://sdkman.io/install
# Show hiddend files and show path in Finder
defaults write com.apple.finder AppleShowAllFiles YES
defaults write com.apple.finder _FXShowPosixPathInTitle -bool true

sh -c "$(curl -fsSL https://raw.githubusercontent.com/ohmyzsh/ohmyzsh/master/tools/install.sh)"
brew install git
brew install zsh-completions
brew install --cask iterm2
brew install --cask firefox
brew install --cask intellij-idea
brew install --cask visual-studio-code
brew install --cask keka
brew install --cask postman
brew install --cask vlc
brew install --cask docker
brew install --cask drawio

brew install --cask biglybt --verbose --debug
brew install --cask caffeine

sdk install java 17.0.4-amzn

brew install pnpm
pnpm install http-proxy-to-socks

brew install pyenv

# https://github.com/pyenv/pyenv#set-up-your-shell-environment-for-pyenv
echo 'export PYENV_ROOT="$HOME/.pyenv"' >> ~/.zshrc
echo 'command -v pyenv >/dev/null || export PATH="$PYENV_ROOT/bin:$PATH"' >> ~/.zshrc
echo 'eval "$(pyenv init -)"' >> ~/.zshrc

pyenv latest -k 3
pyenv latest 3
pyenv install 3

# https://pypa.github.io/pipx/
brew install pipx
pipx ensurepath
pipx completions

# https://virtualenv.pypa.io/en/latest/installation.html
pipx install virtualenv

# VSCode extensions
# Listings
code --list-extensions
# Installing
code --install-extension adpyke.codesnap
code --install-extension asciidoctor.asciidoctor-vscode
code --install-extension blaxou.freezed
code --install-extension codeium.codeium
code --install-extension codium.codium
code --install-extension dart-code.dart-code
code --install-extension dart-code.flutter
code --install-extension davidanson.vscode-markdownlint
code --install-extension dgileadi.java-decompiler
code --install-extension donjayamanne.githistory
code --install-extension donjayamanne.python-environment-manager
code --install-extension dotjoshjohnson.xml
code --install-extension eamodio.gitlens
code --install-extension esbenp.prettier-vscode
code --install-extension geeebe.duplicate
code --install-extension google.arb-editor
code --install-extension hzgood.dart-data-class-generator
code --install-extension k--kato.intellij-idea-keybindings
code --install-extension kevinrose.vsc-python-indent
code --install-extension leodevbro.blockman
code --install-extension marclipovsky.string-manipulation
code --install-extension marufhassan.flutter-snippets
code --install-extension mhutchie.git-graph
code --install-extension ms-azuretools.vscode-docker
code --install-extension ms-python.debugpy
code --install-extension ms-python.python
code --install-extension ms-python.vscode-pylance
code --install-extension ms-vscode.powershell
code --install-extension ms-vsliveshare.vsliveshare
code --install-extension redhat.fabric8-analytics
code --install-extension redhat.java
code --install-extension robert-brunhage.flutter-riverpod-snippets
code --install-extension ryu1kn.partial-diff
code --install-extension streetsidesoftware.code-spell-checker
code --install-extension tabnine.tabnine-vscode
code --install-extension trinm1709.dracula-theme-from-intellij
code --install-extension visualstudioexptteam.intellicode-api-usage-examples
code --install-extension visualstudioexptteam.vscodeintellicode
code --install-extension vmware.vscode-boot-dev-pack
code --install-extension vmware.vscode-spring-boot
code --install-extension vscjava.vscode-java-debug
code --install-extension vscjava.vscode-java-dependency
code --install-extension vscjava.vscode-java-pack
code --install-extension vscjava.vscode-java-test
code --install-extension vscjava.vscode-maven
code --install-extension vscjava.vscode-spring-boot-dashboard
code --install-extension vscjava.vscode-spring-initializr
code --install-extension xyz.local-history

# IDEA extensions
idea64.exe installPlugins org.asciidoctor.intellij.asciidoc
idea64.exe installPlugins codiumai.codiumai
idea64.exe installPlugins com.codeium.intellij
idea64.exe installPlugins google-java-format
idea64.exe installPlugins GrepConsole
idea64.exe installPlugins HighlightBracketPair
idea64.exe installPlugins dev.jbang.intellij.JBangPlugin
idea64.exe installPlugins JFormDesigner
idea64.exe installPlugins com.haulmont.jpab
idea64.exe installPlugins "Key Promoter X"
idea64.exe installPlugins com.yiycf.plugins.mavenDependencyHelper
idea64.exe installPlugins org.kubicz.mavenexecutor.plugin.id
idea64.exe installPlugins MavenRunHelper
idea64.exe installPlugins izhangzhihao.rainbow.brackets
idea64.exe installPlugins org.sonarlint.idea
idea64.exe installPlugins com.github.lipiridi.spotless-applier
idea64.exe installPlugins String Manipulation
idea64.exe installPlugins com.tabnine.TabNine

# Flush DNS
sudo killall -HUP mDNSResponder;sudo killall mDNSResponderHelper;sudo dscacheutil -flushcache

# Port forwarding
# -o: This option is used to specify configuration options for the SSH connection. The option should be followed by the configuration option and its value, separated by an equals sign. For example, ssh -o "Port=2222" user@host specifies that the SSH connection should use port 2222.
# -T: This option disables pseudo-terminal allocation. It is useful when you want to execute a command on the remote host without interacting with a shell, for example when running a script.
# -N: This option tells the SSH client not to execute any command on the remote host. It is useful when you only want to forward ports or establish a tunnel.
# -L: This option is used to set up local port forwarding. It takes three arguments: the local port, the remote host, and the remote port. For example, ssh -L 8080:localhost:80 user@host forwards connections from port 8080 on the local machine to port 80 on the remote host.

# Example of tunnelling with port forwarding in the same command
# Requesting https://localhost:1443 will
# be as requesting https://dal.dx-dev1-blue.internal.vodafoneaws.co.uk
# or https://dal.dx-dev1-blue.internal.vodafoneaws.co.uk:443
ssh -N -M -S /tmp/sshtunnel -D 8080 ahmed.elsagheer2@non-prod-1-bastion.non-prod-1.digital.vodafoneaws.co.uk -p22 -L 1443:dal.dx-dev1-blue.internal.vodafoneaws.co.uk:443 -o ServerAliveInterval=240 -o ProxyCommand=none -i ~/.ssh/ahmedalsaghir.pem

# Port forward to specific destination
ssh -L 8888:server_host_db:3306 -i ~/.ssh/ahmedalsaghir.pem ahmed.elsagheer2@bastion-non-prod-1

# Control keep-alive flag interval for preventing connection getting closed
ssh -o ServerAliveInterval=60 SER@server.domain.com

# Port forward to cloud desktop
ssh -o ProxyCommand=none -T -N -L 1041:localhost:1040 asolyman.aka.corp.amazon.com

# Port forward to connect to db on localhost port 8888 to destination port 3306
ssh -L 8888:db_server_ip:3306 USER@server.domain.com
ssh -L 8888:db_server_ip:3306 -i <path-of-pem-file> USER@server.domain.com

# Detached tunnelling
# -f: This forks the process into the background so you don't have to keep the terminal window open to maintain the SSH tunnel.
# -N: This tells the SSH process to not execute any commands on the remote server (we are only forwarding traffic through the remote server).
# -M: Put the SSH client into master mode. We're doing this so we can easily enter a command later to gracefully end the SSH tunnel without having to kill the connection.
# -S: This is used in conjunction with the -M command. This sets up a special kind of file (called a socket) that will allow us to enter a command later to gracefully end the SSH tunnel without having to kill the connection. /tmp/sshtunnel is the full path to the socket file this command is creating.
# -D: This sets up a dynamic application level forwarding service and 1080 is the port it will listen on. This command creates the SOCKS proxy we'll use later.
# -p: Specify the port on which the remote server is listening for SSH connections.
# Socks http proxy tunnelling to localhost on port 1080
ssh -f -N -M -S /tmp/sshtunnel -D 1080 USER@server.domain.com -p22
# Disable tunnelling
ssh -S /tmp/sshtunnel -O exit server.domain.com -p22
# See if process is active
ps -ax | grep ssh
# See connections
lsof -i -n -P | grep ssh

# List used port process
netstat -vanp tcp | grep 3000
lsof -i tcp:3000
# -9 kills the process immediately, and gives it no chance of cleaning up after itself.
# This may cause problems. Consider using -15 (TERM) or -3 (QUIT) for a softer termination which allows the process to clean up after itself.
kill -9 <PID>

# To map http/https proxy to socks
pnpm exec hpts -s 127.0.0.1:8080 -p 1080

# Kill docker and reopen for starting the daemon
pkill -SIGHUP -f /Applications/Docker.app 'docker serve'
open -a Docker

# List vscode extensions
code --list-extensions
```

## Tips

- Go to location in Finder = Shift + Command + G
- Full screenshot = Shift + Command + 3
- Region screenshot = Shift + Command + 4
- Minimize all windows by `Command + Option + H` then `Command + H`
- Show path in finder `defaults write com.apple.finder _FXShowPosixPathInTitle -bool YES` then ` killall Finder`
- To open app with unverified developer use the command `xattr -d com.apple.quarantine fly` assuming the app is `fly` in this example

### Terminate hanged SSH connection terminal

Press `Enter` then telda `~` followed by dot `.`

### Set up a SSH shortcut (MacOS)

Add the following to `~/.ssh/config`

```
Host clouddesk
    HostName asolyman.aka.corp.amazon.com
    User username
```

then use `ssh clouddesk` for connection