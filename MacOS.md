# MacOS

## Auth

```shell
kinit && mwinit
```

## Commands

```sh
# Flush DNS
sudo killall -HUP mDNSResponder;sudo killall mDNSResponderHelper;sudo dscacheutil -flushcache

# For each brazil workspace
brazil setup platform-support
```

## Tips

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