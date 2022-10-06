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
echo "HISTFILE=~/.zsh_history" >> ~/.zshrc
echo "HISTSIZE=10000" >> ~/.zshrc
echo "SAVEHIST=10000" >> ~/.zshrc
# Choose one of
echo "setopt APPEND_HISTORY" >> ~/.zshrc
echo "setopt SHARE_HISTORY" >> ~/.zshrc
echo "setopt INC_APPEND_HISTORY_TIME" >> ~/.zshrc

toolbox --version
source ~/.zshrc
toolbox install brazilcli
toolbox install cr
toolbox install brazil-octane

# AWS Developer Account (ADA)
toolbox install ada

# Hub-Create for BuilderHub create usage
toolbox registry add s3://buildertoolbox-registry-hub-create-us-west-2/tools.json
toolbox install hub-create
hub-create prepare -a AccountID

sudo mkdir -p -m 755 /workplace/${USER}
sudo chown -R ${USER}:amazon /workplace/${USER}
ln -s /workplace/${USER} ~/workplace

toolbox install cr

curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

sdk install java 17.0.4-amzn
sdk default java 17.0.4-amzn

echo 'export JAVA_HOME=~/.sdkman/candidates/java/current' >> ~/.zshrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.zshrc
source ~/.zshrc

# For each brazil workspace
brazil setup platform-support

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
```
