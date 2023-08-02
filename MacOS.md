# MacOS

## Auth

```shell
kinit && mwinit

# For not using the VPN
mwinit --aea
```

## SSH Config

Example of `~/.ssh/config`

```
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

# https://sdkman.io/install
# Show hiddend files and show path in Finder
defaults write com.apple.finder AppleShowAllFiles YES
defaults write com.apple.finder _FXShowPosixPathInTitle -bool true

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

# Flush DNS
sudo killall -HUP mDNSResponder;sudo killall mDNSResponderHelper;sudo dscacheutil -flushcache

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