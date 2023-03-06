# Cloud Desktop

## Auth

```shell
kinit -f && mwinit -o
```

## Post Creation

```sh
# Enable zsh improvements
echo 'source /apollo/env/envImprovement/var/zshrc' | cat - ~/.zshrc > temp && mv temp ~/.zshrc

# Or creates a ZSH history file
echo "\n" >> ~/.zshrc;
echo "# History config" >> ~/.zshrc;
echo "HISTFILE=~/.zsh_history" >> ~/.zshrc;
echo "HISTSIZE=10000" >> ~/.zshrc;
echo "SAVEHIST=10000" >> ~/.zshrc;
echo "setopt HIST_EXPIRE_DUPS_FIRST" >> ~/.zshrc;
echo "setopt HIST_IGNORE_DUPS" >> ~/.zshrc;
echo "setopt HIST_IGNORE_ALL_DUPS" >> ~/.zshrc;
echo "setopt HIST_IGNORE_SPACE" >> ~/.zshrc;
echo "setopt HIST_FIND_NO_DUPS" >> ~/.zshrc;
echo "setopt HIST_SAVE_NO_DUPS" >> ~/.zshrc;
# Choose one of
echo "setopt APPEND_HISTORY" >> ~/.zshrc
echo "setopt SHARE_HISTORY" >> ~/.zshrc
echo "setopt INC_APPEND_HISTORY_TIME" >> ~/.zshrc

toolbox --version
source ~/.zshrc
toolbox install brazilcli
toolbox install cr
toolbox install brazil-octane

# RDE installation
# https://builderhub.corp.amazon.com/docs/rde/user-guide/setting-up-rde.html
toolbox install rde
rde env validate

# Auto completion
mkdir -p ~/.zsh/completion

echo "\n" >> ~/.zshrc;
echo "# Auto completion scripts" >> ~/.zshrc;
echo 'fpath=(~/.zsh/completion $fpath)' >> ~/.zshrc;
echo 'autoload -Uz compinit && compinit -i' >> ~/.zshrc;
source ~/.zshrc

# Add the cli section of the following
# to the config file under properties section
# version: "1.0"
# properties:
#   # ...
#   cli:
#     completionscriptpaths:
#       zsh: ~/.zsh/completion/_rde
vim ~/.config/rde/config.yml

# Generate the completion file by running
rde env setup completion

# AWS Developer Account (ADA)
toolbox install ada

# Hub-Create for BuilderHub create usage
toolbox registry add s3://buildertoolbox-registry-hub-create-us-west-2/tools.json
toolbox install hub-create
hub-create prepare -a AccountID

sudo mkdir -p -m 755 /workplace/${USER}
sudo chown -R ${USER}:amazon /workplace/${USER}
ln -s /workplace/${USER} ~/workplace

curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

sdk install java 17.0.5-amzn
sdk default java 17.0.5-amzn

echo "\n" >> ~/.zshrc;
echo "# Java settings" >> ~/.zshrc;
echo 'export JAVA_HOME=~/.sdkman/candidates/java/current' >> ~/.zshrc;
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.zshrc;
source ~/.zshrc

# mcurl is a CURL wrapper that allows you to call Midway enabled websites.
mkdir -p ~/bin
curl https://s3.amazonaws.com/com.amazon.aws.midway.software/linux/mcurl.sh \
  -o ~/bin/mcurl && chmod +x ~/bin/mcurl

# After new cloud desktop with same alias, clear all keys
# related to that alias
ssh-keygen -R asolyman.aka.corp.amazon.com

# For each brazil workspace
brazil setup platform-support

# For Java usage
brazil setup --java

# Install NodeJS
# https://docs.aws.amazon.com/sdk-for-javascript/v2/developer-guide/setting-up-node-on-ec2-instance.html
# https://github.com/nvm-sh/nvm#installing-and-updating
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.1/install.sh | bash
. ~/.nvm/nvm.sh
nvm install --lts
node -e "console.log('Running Node.js ' + process.version)"

# Create SSH Key Pair
# Confirm if ids_rsa and id_rsa.pub do not exist
ls -l ~/.ssh/
# Generate ssh private and public keys
ssh-keygen -t RSA -b 2048

# The Midway cookie and SSH certificate are valid for 20 hours since creation. You can check if your certificate is valid by running the following and looking at the Valid value.
ssh-keygen -L -f ~/.ssh/id_rsa-cert.pub

# For code syncing
# https://docs.syncthing.net/users/contrib.html#id1
# Use screen to run in separated process
curl -sS https://webinstall.dev/syncthing | bash
syncthing --gui-address=asolyman.aka.corp.amazon.com:8384

# Sync Version Set metadata
brazil workspace sync --metadata

# To clone a package. 2 steps required
# 1- Create a workspace. 2- Use a package

# Create workspace
brazil workspace create --root <WORKSPACE_NAME>
brazil ws create -n <WORKSPACE_NAME>
# Show info
brazil workspace show
# Use version set
brazil ws use --versionSet BT101AsolymanV1/development
# Use package
brazil ws use --package BT101AsolymanV1

 # Adding package to version set is done through brazil merge. This could be done either by
brazil ws merge
# or after "https://build.amazon.com/merge" then update version set metadata in local environment
brazil ws sync —md
# Or do brazil use package to clone it and build it then it's seen by your workplace
# and to build your package along with other packages in the workplace
bbb

# Sync code from Gitfarm with local, specially after merge
brazil ws sync
# or
git pull

# Compile and clean built artifacts
brazil-build clean
brazil ws clean

# Restart brazil cache daemon
brazil-package-cache stop;
brazil-package-cache start;

# Debug Brazil build in unit tests (use one of the two)
# Run the command and brazil will suspend
# waiting for remote debug connection
brazil-build -Dtests.additional.jvmargs="-Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=1040"
brazil-build -Dtests.additional.jvmargs="-Xdebug -Xnoagent -agentlib:jdwp=transport=dt_socket,server=y,address=localhost:1040"

brazil-recursive-cmd --allPackages --reverse --continue brazil-build clean && brazil ws --sync && brazil-recursive-cmd --allPackages brazil-build test

# Build and output to file
brazil-build 2>&1 | tee build-output.log
```

- Port forwarding could be done adding the following record to `~/.ssh/config`

```sh
cat ~/.ssh/config
host devdsk
  HostName <devdskHostname>
  LocalForward 8385 127.0.0.1:8384
```

- SyncThing ignore pattern

```
// Brazil
*.wasm
/build
/src/*/build
/src/*/bin
/src/*/pkg
/*/src/*/node_modules
/*/node_modules
/**/node_modules
!/src
!/repo
!/packageInfo
!/release-info
/.idea
/src/*/env
/env
/logs
/src/*/var

// Gradle
/.gradle

// Miscellaneous
(?d).DS_Store
(?d)*.iml
(?d).idea
*.class
*.log
*.pyc
*.swp
.DS_Store
.atom/
.buildlog/
.history
.svn/
migrate_working_dir/

// IntelliJ related
*.iml
*.ipr
*.iws
.idea/

// The .vscode folder contains launch configuration and tasks you configure in
// VS Code which you may wish to be included in version control, so this line
// is commented out by default.
//.vscode/

// Flutter/Dart/Pub related
**/doc/api/
**/ios/Flutter/.last_build_id
/**/.dart_tool/
.flutter-plugins
.flutter-plugins-dependencies
.packages
.pub-cache/
.pub/
/build/

// Symbolication related
app.*.symbols

// Obfuscation related
app.*.map.json

// Android Studio will place build artifacts here
/android/app/debug
/android/app/profile
/android/app/release
```