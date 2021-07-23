# Ubuntu & Linux Quick Reference

## Linux Files inside of File Explorer

```bash
# In WSL Terminal
explorer.exe .
```

Or navigate to `\\wsl$\Ubuntu\home`

## WSL config

- Create  a  `%UserProfile%\.wslconfig` file in Windows and use it to limit memory assigned to WSL2 VM. [Github](https://github.com/microsoft/WSL/issues/4166#issuecomment-526725261) & [Doc](https://docs.microsoft.com/en-us/windows/wsl/release-notes#build-18945).

- Reference - https://docs.microsoft.com/en-us/windows/wsl/wsl-config#configure-global-options-with-wslconfig

## Command line Tips

- `readlink -f $(which java)` - Where java is installed

## Startup

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