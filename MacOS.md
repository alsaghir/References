# MacOS

## Auth

```shell
kinit && mwinit
```

## Commands

```sh
# Dev installations
# https://sdkman.io/install
brew install awscli
brew install zsh-completions
brew install git
brew install syncthing
sdk install java 17.0.4-amzn

# Flush DNS
sudo killall -HUP mDNSResponder;sudo killall mDNSResponderHelper;sudo dscacheutil -flushcache

# For each brazil workspace
brazil setup platform-support
```

## Tips

- Full screenshot = Shift + Command + 3
- Region screenshot = Shift + Command + 4
- Minimize all windows by `Command + Option + H` then `Command + H`

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