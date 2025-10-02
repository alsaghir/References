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

# Flatpaks
flatpak install --user -y flathub com.biglybt.BiglyBT
flatpak install --user -y flathub com.discordapp.Discord
flatpak install --user -y flathub com.github.IsmaelMartinez.teams_for_linux
flatpak install --user -y flathub com.github.tchx84.Flatseal
flatpak install --user -y flathub com.microsoft.Edge
# https://code.visualstudio.com/docs/configure/settings-sync#_recommended-configure-the-keyring-to-use-with-vs-code
flatpak install --user -y flathub com.visualstudio.code
flatpak install --user -y flathub info.smplayer.SMPlayer
flatpak install --user -y flathub io.github.mpc_qt.mpc-qt
flatpak install --user -y flathub io.github.zaps166.QMPlay2
flatpak install --user -y flathub io.missioncenter.MissionCenter
flatpak install --user -y flathub io.podman_desktop.PodmanDesktop
flatpak install --user -y flathub one.ablaze.floorp
# Env
# GTK_THEME=Breeze:dark
flatpak install --user -y flathub org.gnome.Evolution
flatpak install --user -y flathub org.kde.haruna
flatpak install --user -y flathub org.videolan.VLC

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
export DOCKER_HOST=unix://$XDG_RUNTIME_DIR/podman/podman.sock
systemctl --user enable --now podman.socket
ls $XDG_RUNTIME_DIR/podman/podman.sock
export DOCKER_HOST=unix://$XDG_RUNTIME_DIR/podman/podman.sock
# To make flatpak podman desktop work with the user socket
FLATPAK_ID="com.redhat.PodmanDesktop" # Define the Flatpak application ID.
SOCKET_PATH="/run/user/$UID/podman/podman.sock" # Define the full, dynamic path to the user's Podman socket. OR unix:///run/user/$UID/podman/podman.sock

flatpak override --user $FLATPAK_ID \
    --nofilesystem=host \ 
    --filesystem=home \
    --filesystem=/run/docker.sock \
    --filesystem=xdg-run/podman:create \
    --filesystem=xdg-run/containers:create \
    --share=network \
    --share=ipc \
    --socket=x11 \
    --socket=session-bus \
    --socket=system-bus \
    --device=dri \
    --env=KIND_CONTAINER_RUNTIME=podman \
    --env=DOCKER_HOST=unix:$SOCKET_PATH \
    --env=XCURSOR_PATH=/run/host/user-share/icons:/run/host/share/icons \
    --set-session-bus-policy=org.kde.StatusNotifierWatcher=talk \
    --set-session-bus-policy=org.freedesktop.Notifications=talk \
    --set-session-bus-policy=org.freedesktop.Flatpak=talk \
    --set-session-bus-policy=org.freedesktop.secrets=talk \
    --set-session-bus-policy=org.kde.kwalletd6=talk

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
