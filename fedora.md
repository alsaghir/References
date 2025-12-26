# Fedora Usage Ref

```sh
# Enable RPM Fusion
sudo dnf install https://download1.rpmfusion.org/free/fedora/rpmfusion-free-release-$(rpm -E %fedora).noarch.rpm
sudo dnf install https://download1.rpmfusion.org/nonfree/fedora/rpmfusion-nonfree-release-$(rpm -E %fedora).noarch.rpm

# Enable Terra
sudo dnf install --nogpgcheck --repofrompath 'terra,https://repos.fyralabs.com/terra$releasever' terra-release

# Set hostname
sudo hostnamectl set-hostname "New_Custom_Name"

# FFMPEG closed instead of the free one
sudo dnf swap ffmpeg-free ffmpeg --allowerasing
sudo dnf update @multimedia --setopt="install_weak_deps=False" --exclude=PackageKit-gstreamer-plugin

# Amd
sudo dnf swap mesa-va-drivers mesa-va-drivers-freeworld
sudo dnf swap mesa-vdpau-drivers mesa-vdpau-drivers-freeworld

# Battery tools
# https://linrunner.de/tlp/installation/fedora.html
sudo dnf install tlp tlp-rdw
sudo dnf remove tuned tuned-ppd
sudo systemctl enable --now tlp
sudo systemctl mask systemd-rfkill.service systemd-rfkill.socket

# Update Firmware for devices if needed
sudo fwupdmgr get-devices
sudo fwupdmgr refresh --force
sudo fwupdmgr get-updates
sudo fwupdmgr update

# Enable openh264
sudo dnf config-manager setopt fedora-cisco-openh264.enabled=1

# Enable tainted repos
sudo dnf install -y rpmfusion-free-release-tainted
sudo dnf install -y rpmfusion-nonfree-release-tainted

# Kernel
sudo dnf copr enable bieszczaders/kernel-cachyos
sudo setsebool -P domain_kernel_load_modules on
sudo dnf install kernel-cachyos kernel-cachyos-devel-matched
sudo mkdir -p /etc/kernel/postinst.d

# START Make chachy OS default after upgrade
sudo nano /etc/kernel/postinst.d/99-default
---
#!/bin/sh

set -e

grubby --set-default=/boot/$(ls /boot | grep vmlinuz.*cachy | sort -V | tail -1)
---
sudo chmod +x /etc/kernel/postinst.d/99-default
# END

# Queries about kernel
rpm -q kernel
# Current Default
sudo grub2-editenv list
# Currently runnin
uname -r

# START Limit latest installs
sudo nano /etc/dnf/dnf.conf
---
[main]
installonly_limit=3
---
# END

# Boot entries
sudo efibootmgr -v
# to delete the entry, replacing XXXX with the number of the duplicate entry
sudo efibootmgr -b XXXX -B

# Nvidia
sudo dnf install akmod-nvidia
sudo dnf install xorg-x11-drv-nvidia-cuda
sudo dnf mark user akmod-nvidia
sudo dnf install libva-nvidia-driver

# Asus linux
sudo dnf copr enable lukenukem/asus-linux
sudo dnf install asusctl supergfxctl
sudo dnf update --refresh
sudo systemctl enable supergfxd.service
sudo dnf install asusctl-rog-gui

# Monitor GPUs (nvidia and amd)
watch -n 1 nvidia-smi
glxinfo | grep "OpenGL renderer"
prime-run glxinfo | grep "OpenGL renderer"
lsmod | grep amdgpu
lspci -k | grep -A 3 -E "(VGA|3D)"

# Packages and apps
sudo dnf install prime-run -y
sudo dnf copr enable jdxcode/mise
sudo dnf install mise -y
sudo dnf copr enable atim/starship
sudo dnf install starship -y
sudo dnf install jetbrainsmono-nerd-fonts -y
sudo dnf config-manager addrepo --from-repofile=https://download.opensuse.org/repositories/home:luisbocanegra/Fedora_42/home:luisbocanegra.repo
sudo dnf install kde-material-you-colors -y
sudo dnf copr enable ilyaz/LACT
sudo dnf install lact -y
sudo systemctl enable --now lactd
sudo dnf install docker-cli -y
mise use -g kubectl
mise use -g docker-cli
mise use -g podman
mise use -g kind

# Run using nvidia
flatpak run --env=__NV_PRIME_RENDER_OFFLOAD=1 --env=__GLX_VENDOR_LIBRARY_NAME=nvidia org.gnome.Evolution

# Monitors
kde-inotify-survey

# Tips
# Increase inotify watchers limit
# if there is /etc/sysctl.d/50-kde-inotify-survey-max_user_instances.conf then KDE took care of it
echo fs.inotify.max_user_instances=256 | sudo tee -a /etc/sysctl.d/99-inotify-instances.conf
sudo sysctl --system
cat /proc/sys/fs/inotify/max_user_instances
cat /proc/sys/fs/inotify/max_user_watches
# Podman, kubernetes, kind
# https://github.com/containers/podman/blob/main/docs/tutorials/socket_activation.md
export KIND_CONTAINER_RUNTIME=podman
sudo ln -sf /home/ahmed/.local/share/mise/installs/kubectl/latest/kubectl /usr/local/bin/kubectl
sudo ln -sf /home/ahmed/.local/share/mise/installs/kind/latest/kind /usr/local/bin/kind

# Blutooth suspension fix
# Add IdleTimeout=0
sudo vi /etc/bluetooth/input.conf
# Add options bluetooth disable_autosuspend=1
sudo vi /etc/modprobe.d/bluetooth.conf
# Add options btusb enable_autosuspend=0
sudo vi /etc/modprobe.d/btusb-no-autosuspend.conf
# Add usbcore.autosuspend=-1 to GRUB_CMDLINE_LINUX end line like
# GRUB_CMDLINE_LINUX="commands_that_are_already_there_dont_touch usbcore.autosuspend=-1"
sudo nano /etc/default/grub
sudo grub2-mkconfig -o /etc/grub2.cfg && sudo grub2-mkconfig -o /etc/grub2-efi.cfg
```

## Silverblue or Bluefin

- Layers

```sh
# Asus
sudo sh -c 'echo "[copr:copr.fedorainfracloud.org:lukenukem:asus-linux]
name=Copr repo for asus-linux owned by lukenukem
baseurl=https://download.copr.fedorainfracloud.org/results/lukenukem/asus-linux/fedora-\$releasever-\$basearch/
type=rpm-md$fpath
skip_if_unavailable=True
gpgcheck=1
gpgkey=https://download.copr.fedorainfracloud.org/results/lukenukem/asus-linux/pubkey.gpg
repo_gpgcheck=0
enabled=1
enabled_metadata=1" > /etc/yum.repos.d/asus.repo'

# Envycontrol
RELEASEVER=$(grep '^VERSION_ID=' /etc/os-release | cut -d'=' -f2)
sudo wget -O /etc/yum.repos.d/sunwire-envycontrol.repo https://copr.fedorainfracloud.org/coprs/sunwire/envycontrol/repo/fedora-$RELEASEVER/sunwire-envycontrol-fedora-$RELEASEVER.repo

# Terra repo
# Mainly for jetbrainsmono-nerd-fonts. Brew fonts should be used instead
sudo curl -fsSL https://github.com/terrapkg/subatomic-repos/raw/main/terra.repo | pkexec tee > /etc/yum.repos.d/terra.repo

# VScode repo
echo -e "[code]\nname=Visual Studio Code\nbaseurl=https://packages.microsoft.com/yumrepos/vscode\nenabled=1\nautorefresh=1\ntype=rpm-md\ngpgcheck=1\ngpgkey=https://packages.microsoft.com/keys/microsoft.asc" | sudo tee /etc/yum.repos.d/vscode.repo > /dev/null

# Install
# jetbrainsmono-nerd-fonts should be used by brew instead
sudo rpm-ostree install asusctl asusctl-rog-gui code python-envycontrol polkit nemo nemo-fileroller

# Install nix
# https://gist.github.com/queeup/1666bc0a5558464817494037d612f094
# See home.nix example below
sudo tee /etc/ostree/prepare-root.conf <<'EOL'
[composefs]
enabled = yes
[root]
transient = true
EOL

rpm-ostree initramfs-etc --reboot --track=/etc/ostree/prepare-root.conf
curl --proto '=https' --tlsv1.2 -sSf -L https://install.determinate.systems/nix | sh -s -- install ostree --no-confirm
echo "Defaults  secure_path = /nix/var/nix/profiles/default/bin:/nix/var/nix/profiles/default/sbin:$(sudo printenv PATH)" | sudo tee /etc/sudoers.d/nix-sudo-env

# Run regularly for updates
nix flake update --flake /var/home/ahmed/.config/home-manager
home-manager switch --flake ~/.config/home-manager/#ahmed

# Make Nemo the default for directories and related types
xdg-mime default nemo.desktop x-directory/normal inode/directory application/x-gnome-saved-search
# Tell GIO too (helps some apps)
gio mime inode/directory nemo.desktop
gio mime x-directory/normal nemo.desktop
gio mime application/x-gnome-saved-search nemo.desktop
# In GNOME Tweaks > Legacy Applications> Theme > Adwaita-dark
# Verify
xdg-mime query default inode/directory
gio mime inode/directory
xdg-open ~
flatpak-spawn --host xdg-open ~
# Add Nemo Desktop as startup app using gnome tweaks
gsettings set org.gnome.desktop.background show-desktop-icons false
gsettings set org.nemo.desktop show-desktop-icons true

# https://discussion.fedoraproject.org/t/setup-hibernation-on-fedora-atomic-desktops/121534/3
# Enable swapfile
sudo btrfs filesystem mkswapfile --size 64g --uuid clear /var/swap/swapfile
# Add the following line to /etc/fstab
# /var/swap/swapfile                        none                    swap    defaults,pri=5,nofail,x-systemd.requires-mounts-for=/var/swap 0 0
# You might want to check
# sudo btrfs subvolume list /
# and add the followign couple of lines instead, notice the path in the first line from previous command as well as UUID
# using the command sudo findmnt -no UUID -T /var/swap/swapfile
# UUID=c5292113-a5c9-4b96-8eb8-81285e3c3d35 /var/swap btrfs subvol=root/ostree/deploy/fedora/var/swap,compress=zstd:1,nofail 0 0
# /var/swap/swapfile                        none                    swap    defaults,pri=5,nofail 0 0
```

- home.nix example

```nix
{
  config,
  pkgs,
  lib,
  ...
}:

{
  xdg = {
    enable = true;
  };
  xdg.mime.enable = true;
  targets.genericLinux.enable = true;
  fonts.fontconfig.enable = true;

  nixpkgs.config.allowUnfreePredicate =
    pkg:
    builtins.elem ((builtins.parseDrvName pkg.pname).name) [
      "code"
      "vscode"
    ];

  programs.zsh = {
    enable = true;
    enableCompletion = true;
    syntaxHighlighting.enable = true;
    autosuggestion.enable = true;

    sessionVariables = {
      SHELL = "${pkgs.zsh}/bin/zsh";
    };

    # Set Zsh to use a proper theme and manage plugins declaratively.
    oh-my-zsh = {
      enable = true;
      theme = "robbyrussell";

      plugins = [
        "git"
        "brew"
        "cp"
        "gradle"
        "history"
        "kind"
        "kubectl"
        "mise"
        "podman"
        "ssh"
        "ssh-agent"
        "sudo"
        "systemd"
        # NOTE: zsh-autosuggestions is better managed as a separate module
        # { name = "zsh-autosuggestions"; }
      ];
    };

    initContent =
      let
        zshConfigEarlyInit = lib.mkOrder 500 ''
          echo "Early init"
          source /etc/profile
        '';
        zshConfigBeforeCompletionInit = lib.mkOrder 550 ''echo "Before completion init"'';
        zshConfig = lib.mkOrder 1000 ''echo "General Config init"'';
        zshConfigLastToRun = lib.mkOrder 1500 ''
          echo "Last to run init"
          # Starship prompt
          eval "$(starship init zsh)"

          # Mise activation for zsh
          eval "$(mise activate zsh)"

          # Distrobox home prefix
          export DBX_CONTAINER_HOME_PREFIX=$HOME/distrobox

          # Nix auto-completion if you have it
          if command -v determinate-nixd >/dev/null 2>&1; then
            eval "$(determinate-nixd completion zsh)"
          fi
        '';
      in
      lib.mkMerge [
        zshConfigEarlyInit
        zshConfigBeforeCompletionInit
        zshConfig
        zshConfigLastToRun
      ];

  };

  xdg.desktopEntries.code = {
    name = "Visual Studio Code";
    genericName = "Text Editor";
    exec = "env ELECTRON_OZONE_PLATFORM_HINT=wayland ${pkgs.vscode}/bin/code --ozone-platform-hint=auto --enable-features=WaylandWindowDecorations %F";
    terminal = false;
    categories = [
      "Utility"
      "TextEditor"
      "Development"
      "IDE"
    ];
    mimeType = [
      "text/plain"
      "inode/directory"
    ];
    icon = "vscode";
    settings = {
      Keywords = "vscode";
    };
  };


  home.username = "ahmed";
  home.homeDirectory = "/home/ahmed";

  home.stateVersion = "25.05";

  home.packages = with pkgs; [


    terminator
    kdePackages.konsole
    mise
    vscode
    nixfmt-rfc-style

    # zsh
    zsh
    oh-my-zsh
    zsh-autosuggestions
    zsh-syntax-highlighting
    starship
  ];

  home.file = {

  };



  home.sessionVariables = {
  };

  programs.home-manager.enable = true;

}
```

flake.nix example using `github:nixos/nixpkgs/nixpkgs-unstable` for latest packages

```nix
{
  description = "Home Manager configuration of ahmed";

  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs/nixpkgs-unstable";
    home-manager = {
      url = "github:nix-community/home-manager";
      inputs.nixpkgs.follows = "nixpkgs";
    };
  };

  outputs =
    { nixpkgs, home-manager, ... }:
    let
      system = "x86_64-linux";
      pkgs = nixpkgs.legacyPackages.${system};
    in
    {
      homeConfigurations."ahmed" = home-manager.lib.homeManagerConfiguration {
        inherit pkgs;

        modules = [ ./home.nix ];

      };
    };
}

```

- Commands

```sh
# Flatpaks
flatpak install -y flathub com.biglybt.BiglyBT \
    com.discordapp.Discord \
    com.github.IsmaelMartinez.teams_for_linux \
    com.github.tchx84.Flatseal \
    com.microsoft.Edge \
    com.raggesilver.BlackBox \
    com.slack.Slack \
    info.smplayer.SMPlayer \
    io.missioncenter.MissionCenter \
    io.podman_desktop.PodmanDesktop \
    org.fedoraproject.MediaWriter \
    org.kde.konsole \
    org.mozilla.firefox \
    org.remmina.Remmina \
    org.videolan.VLC \
    page.tesk.Refine \
    org.gtk.Gtk3theme.adw-gtk3-dark \
    org.gtk.Gtk3theme.adw-gtk3 \
    org.kde.dolphin

brew install --cask font-jetbrains-mono-nerd-font

mise use -g gradle java@25

# https://github.com/ivan-hc/am?tab=readme-ov-file#installation
wget -q https://raw.githubusercontent.com/ivan-hc/AM/main/AM-INSTALLER && chmod a+x ./AM-INSTALLER && ./AM-INSTALLER && rm ./AM-INSTALLER

am -i appimageupdatetool
am -i appimagetool
am -i sas
am -e https://github.com/valicm/VSCode-AppImage vscode

# Default shell
sudo usermod -s /bin/bash $USERNAME
```

- Distrobox: check [DevEnvironment](DevEnvironment) for automated setup. Use the following commands within the project folder for starting it up.

```sh
# Build base image
podman build -f Containerfile -t dev --build-arg USER_NAME=ahmed

# Start up
gradle devEnvUp

# Start working inside the container shell
# This will reload .zshrc from inside the container rather than the host's own .zshrc
distrobox enter java-dev-box -- /bin/zsh -l

# cleanup container, home folder and volume folder (for directories exposed from the container)
distrobox rm --rm-home -f java-dev-box; rm -rf ~/distrobox/java-dev-box; rm -rf ~/distrobox/java-dev-box-volume
```
