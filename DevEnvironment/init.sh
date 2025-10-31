#!/bin/zsh

export HOST_USER_HOME="/var/home/ahmed"
export HOST_USER_HOME2="/home/ahmed"
export RUN_HOST_USER_HOME="/run/host/var/home/ahmed"
export ZSH_CUSTOM_PATH="$HOME/.oh-my-zsh/custom"
export ZSH="$HOME/.oh-my-zsh"

export MISE_DATA_DIR="/mise";
export MISE_CONFIG_DIR="/mise";
export MISE_INSTALLS_DIR="/mise/installs";
export MISE_CACHE_DIR="/mise/cache";
export MISE_INSTALL_PATH="/usr/bin/mise";
export MISE_ALWAYS_KEEP_DOWNLOAD=true;

sh -c "$(curl -fsSL https://raw.githubusercontent.com/ohmyzsh/ohmyzsh/master/tools/install.sh)";
git clone https://github.com/zsh-users/zsh-autosuggestions "${ZSH_CUSTOM_PATH:-$HOME/.oh-my-zsh/custom}/plugins/zsh-autosuggestions"
sed -i 's/^plugins=(\([^)]*\))$/plugins=(\1 dnf gradle history kind kubectl mise node podman ssh sudo systemd yarn zsh-autosuggestions)/' $HOME/.zshrc
cp /tmp/robbyrussell.zsh-theme $HOME/.oh-my-zsh/custom/themes/robbyrussell.zsh-theme;

echo 'autoload -U compinit && compinit' >> $HOME/.zshrc;

sudo cp -r /opt/mise-prewarm/. "$MISE_DATA_DIR/";
sudo chown -R $(id -u):$(id -g) $MISE_DATA_DIR;
sudo rm -rf /opt/mise-prewarm;
mise trust --yes /mise/config.toml;

echo 'export MISE_TRUSTED_CONFIG_PATHS=~/.config/mise/config.toml' >> $HOME/.zshrc;
echo 'export MISE_TRUSTED_CONFIG_PATHS=~/.config/mise/config.toml' >> $HOME/.zshrc;
echo 'export MISE_DATA_DIR=/mise'>> $HOME/.zshrc;
echo 'export MISE_CONFIG_DIR=/mise'>> $HOME/.zshrc;
echo 'export MISE_INSTALLS_DIR=/mise/installs'>> $HOME/.zshrc;
echo 'export MISE_CACHE_DIR=/mise/cache'>> $HOME/.zshrc;
echo 'export MISE_INSTALL_PATH=/usr/bin/mise'>> $HOME/.zshrc;
echo 'eval "$(mise activate zsh)"' >> $HOME/.zshrc;
echo 'KIND_CONTAINER_RUNTIME=podman' >> $HOME/.zshrc;

mise trust --ignore $HOST_USER_HOME/.config/mise/config.toml;
mise trust --ignore $HOST_USER_HOME2/.config/mise/config.toml;
mise trust --ignore $RUN_HOST_USER_HOME/.config/mise/config.toml;

mise use -g usage pnpm node java@17 java@21 kubectl kind maven gradle@8 mvnd;



# Will use these from host if enabled, but not recommended to keep the host intact
#distrobox-export --bin /usr/bin/podman-compose;
#distrobox-export --bin /mise/installs/kind/latest/kind;
#distrobox-export --bin /mise/installs/kubectl/latest/kubectl;