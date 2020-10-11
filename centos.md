# CentOS & Linux Quick Reference

## After minimal installation

On VM use local virtual host (private) & another adapter with NAT to access the internet. Enable both network cards/adapters on installation wizard.

### Adapter connection

`ip address show` or `ip a s` - will show connections with its assigned IP addresses and configs  
`nmcli conn show` - List adapters connections  
`nmcli conn up enps33` - Bringing up the connection called `enps33`  
`sudo sed -i s/ONBOOT=no/ONBOOT=yes/ /etc/sysconfig/network-scripts/ifcfg-ens34` - Make connection up and running on boot. This also should be done with each adapter like `ens34`  
`grep ONBOOT /etc/sysconfig/network-scripts/ifcfg-ens34` - will show that edit is effected  
`systemctl restart network` - Bring up the interfaces with latest configurations  
`nmcli connection edit ens33` - Edit connection instead of manually editing `/etc/sysconfig/network-scripts/ifcfg-ens34`  

`hostnamectl` - Will show hostname info. `cat /etc/hostname` also shows the hostname.  
`hostnamectl --static set-hostname "apollo"` - Set hostname to `apollo`  
`hostnamectl --pretty set-hostname "Apollo"` - Set hostname shows somewhere as info as pretty word to `Apollo`

### Packages

- `yum update -y` - update installed packages. **IMPORTANT**
- `yum install -y redhat-lsb-core net-tools epel-release kernel-headers kernel-devel`
- `yum groupinstall -y "Development Tools"` or `dnf group install gnome-desktop` - Group of packages installation by name (must be exact and case sensitive within double quotation marks) or by id
- `systemctl set-default graphical.target` - After installing desktop, use this to make it default UI for the system
- `systemctl isolate graphical.target` - to take effect immediately
- `sudo dnf install open-vm-tools open-vm-tools-desktop xorg-x11-drv-vmware` - For running CentOS on VMware workstation.
- `dnf group install "Basic Web Server"` - For apache Http web server packages
- `dnf list installed`
- `dnf provides nslookup` - List what provides the tool/command called `nslookup`.
- `dnf remove zip` - Removes zip package from the system.
- `rpm -i nmap.rpm` - Installs package `nmap.rpm`. `rpm -U nmap.rpm` - upgrade the package. `rpm -e nmap` - remove the package.

### Commands & Tools

- `sync` - Sync all cached file data of the current user. Do before removing USB device.
- `ehect /dev/sdd` - Eject USB drive physically
- `fallocate -l10M file1` - Create `file1` with size `10M`
- `man` - Documentation using man page. `man ls` will show document for `ls` command.
- `tty` & `who` - shows which `tty` you're currently on and shows who is logged in on the system.
- `ls -aF` - List files and folders identifying folders, files & links with colors and slashes. `-l` option shows permissions. `-rt` For time data sorted with most recent updated files. `-h` for readable size format.
- `lsb_release -d` - Release information of current OS.
- `which cat` - Shows where binary executer of `cat`
- `rpm -qf /usr/bin/cat` - Shows package that `/usr/bin/cat` belongs to
- `mkdir -p ./x/y` - Make folder and make also use `-p` to parents if not exists
- `rm -rf x` - remove directory recursively and force the deletion
- `ln f1 f2` - Make hard link of file `f1` to `f2`. `f1` is the source and `f2` is the created new file pointing to same physical metadata.
- `ln -s f2 f3` - Make symbolic link from source `f2` to `f3` so `f3` will be created as file of type `symbolic link` pointing to `f2`.
- `less` & `cat` for viewing files content.
- `grep pattern file.ext` - Will echo result of regular expression matching `pattern` in `file.ext`
- `sed` - For regex replacing content in files
- `find` - Searching in folders tool with advanced options
- `touch newline1` or `> newfile1` - Create new empty file called `newfile1`. Second command is actually means writing nothing to file.
- `set -o` - List shell options. `set -o noclobber` will set `noclobber` on. Use `set +o noclobber` to set it off.
- `ifdown ens33` & `ifup ens33` - Brings the network adapter `ens33` down and up.
- `localectl` - Tool related to anything for locale and language handling.
- `systemctl status chronyd` - `chronyd` is related to time handling. Also `timedatectl`
- `lsmod` & `modinfo sr_mod` - List kernel modules and shows info about specific kernel module by specifying its name
- `modprobe -r -v sr_mod` - Removes kernel module named `sr_mod`. `modprobe blutooth` will enable the kernel module in the system. Parament effect needs script in `/etc/sysconfig/modules` to be added to effect the system on reboot for enabling modules. For preventing disabled modules from coming back, editing `/etc/default/grub` to add `rdblacklist=sr_mod` to the key `GRUB_CMDLINE_LINUX` is required then run `grub2-mkconfig -o /boot/grub2/grub.cfg/` and finally `echo "blacklist sr_mod" >> /etc/modprobe.d/blacklist.conf`
- `ls /lib/modules/$(uname -r)/kernel` - List of modules kernel on the system

### Disks

> Unix systems have a single directory tree. All accessible storage must have an associated location in this single directory tree. This is unlike Windows where (in the most common syntax for file paths) there is one directory tree per storage component (drive).
>
> Mounting is the act of associating a storage device to a particular location in the directory tree. For example, when the system boots, a particular storage device (commonly called the root partition) is associated with the root of the directory tree, i.e., that storage device is mounted on `/` (the root directory).
>
> Let's say you now want to access files on a CD-ROM. You must mount the CD-ROM on a location in the directory tree (this may be done automatically when you insert the CD). Let's say the CD-ROM device is `/dev/cdrom` and the chosen mount point is `/media/cdrom`. The corresponding command is
>
> ```bash
> mount /dev/cdrom /media/cdrom
> ```
>
> After that command is run, a file whose location on the CD-ROM is `/dir/file` is now accessible on your system as `/media/cdrom/dir/file`. When you've finished using the CD, you run the command `umount /dev/cdrom` or `umount /media/cdrom` (both will work; typical desktop environments will do this when you click on the “eject” or ”safely remove” button).
>
> Mounting applies to anything that is made accessible as files, not just actual storage devices. For example, all Linux systems have a special filesystem mounted under `/proc`. That filesystem (called `proc`) does not have underlying storage: the files in it give information about running processes and various other system information; the information is provided directly by the kernel from its in-memory data structures.

- `lsblk` - List block devices which are in this case are disks & partitions. Disks are in `/dev/` path
- `fdisk -l` - List disks
- `dd` - For disk image manipulation. `dd if=/dev/sr0 of=netinstall.iso` will create `netinstall.iso` of mounted device `/dev/sr0`.
- `dd if=/dev/zero of=/dev/sda count=1 bs=512` - `bs=512` specifying the block size and only one operation device so `count=1` will delete partition table of `/dev/sda`. `status=progress` could be added to show progress.
- `df` - Disk free tool of local file systems
- `df -hT` - Disk free of local file systems with file system

UNIX system file system

> Traditionally, UNIX systems have had various types of nodes in their filesystems:
>
> - directory
> - file
> - symlink
> - block device
> - character device
> - FIFO
> - UNIX domain socket
>
> While there are now exceptions, generally block devices containing filesystems are mounted on directories.
>
> Since you want to mount a file, you must first create a loop block device that is backed by the file. This can be done using `losetup`, but `mount -o loop` is a shortcut that handles that behind the scenes.

```bash
# Create loop device of a file
losetup -fP /tmp/test-image.dd

# Check loop device
lsblk -i NAME, TYPE, SIZE,MOUNTPOINT,FSTYPE,MODEL

# Make partition table on that loop device using gpt or msdos
parted /dev/loop0 mklable gpt

# Check the partition table
parted /dev/loop0 print

# create device maps to make loop device similar to real hardware partition
kpartx -a /dev/loop0

# Remove loop device but it must be unmounted
unmoun /dev/loop0
losetup -d /dev/loop0
```

### Permissions

- FAT does not support permissions
- ACLs used for additional permissions. XFS fully supports it and build-in with CentOS. EXT however needs `acl` option to be added.
- Use `stat` or `ls` to see file permission
- 4 = READ, 2 = WRITE, 1 = EXECUTE
- `umask` - Sets default permission for new files
- `chmod` - Changes the permission of a file. Examples are `chmod 467 file1`, `chmod u=r,g=rw,o=rwx`, `chmod u=r,go=rw`, `chmod +x`, `chmod o=` means nothing to others, `chmod a+x` means all plus execute, `chmod o-t` means others minus sticky bit.
- Sticky bit is special permission for directories to prevent deletion/renaming/movement. `t` means execute permission is set and `T` means execution permission is not set. To create a directory with sticky bit use `mkdir -m 1777 test` as the `1` here is special permission for sticky bit.
- Permissions in Linux are not cumulative and it goes in the order user, group, and then other. If you are a user you get only the permissions of the user; group permissions and other's permissions do not apply to you.
- The position that the `x` bit takes in the `rwxrwxrwx` for the user octal (1st group of `rwx`) and the group octal (2nd group of `rwx`) can take an additional state where the `x` becomes an `s`. When this occurs this file when executed (if it's a program and not just a shell script) will run with the permissions of the owner or the group of the file. So if the file is owned by root and the SUID bit is turned on, the program will run as root. Even if you execute it as a regular user. The same thing applies to the GUID bit. Executable bit disabled using uppercase `S`.
- set GUID using `chmod 2555 [path_to_file]` and SUID using `chmod 4555 [path_to_file]`
- `id -u` prints user id. `id -un` prints username. `id -gn` prints primary group name. `id -Gn` prints secondary groups names.
- Primary group used when creating file. When accessing file, secondary groups used.
- `chgrp wheel file2` - Change group of `file2`
- `newgrp wheel` - make `wheel` is my primary group creating another shell with it. Use `exit` to get back and verify with `id -gn`.
- `chown` - used to change the ownership of a file. `chown root file2` changes the user owner of `file2` to owner. `chown tux.tux file2` - changes the user owner and group owner to `tux`.
- `cp -a` - Copying a file with permission included.
- `usermod -aG wheel userwhatever` - Will append group `wheel` to user `userwhatever`

### SU

- `su` - Creates new bash as root at same directory and some settings still sees me as original user. Verify with `echo $USER`
- `su -l` - Completely new environment with root access
- `cat /etc/sudoers` - To see users/groups
- `sudo visudo` - Edit to add users or groups. User like `tux ALL=(root) ALL` will add user `tux` from any host connecting as `root` o run all commands.
- `vi /etc/ssh/sshd_config` - Edit to uncomment & modify option to `PermitRootLogin no` then `systemctl restart sshd`
- `ssh-keygen -t ed25519 -C "<comment>"` or `$ ssh-keygen -t rsa -b 4096 -C "your_email@example.com"` - Generates new SSH key with type specified using `-t` option.
- In `~` you can make new `.ssh` folder and inside it add config file `vi config` to create server alias like that

```bash
Host server1
    HostName 192.168.56.105
    User root
    Port 22
```

- `scp file1 server1:/tmp` - Copy from current server to `server1`. Reverse could be done using `scp server1:/tmp file1`. You may specify private key using `-i .ssh/id_rsa`
- `sftp -i .ssh/id_rsa username@10.0.0.1` - Connects to server using `sftp` with specific private key using user called `username` to IP address `10.0.0.1`. Then you may use `put file1` to upload `file1` and `get file1` to download it. End it with `bye` command

### SSH key-pair usage

Either after generating keys pair on the client directly use

`ssh-copy-id -i id_rsa.pub server1` - Copy public key to another server called `server1` which is a alias. Normally it should put the original server IP, port & user  

OR

- Copy key from `id_rsa.pub` on the client to `~/.ssh/authorized_keys` on the server. Could be done directly using

```bash
cat /c/Users/whatever/.ssh/id_rsa.pub | ssh zxc@172.32.0.2 -T "cat > ~/.ssh/authorized_keys"
```

- On server do the following while you're on `~`

```bash
sudo chmod 700 .ssh
sudo chmod 600 .ssh/authorized_keys
```

- On server, make sure to `sudo vi /etc/ssh/sshd_config` and set `PubkeyAuthentication yes` and `PasswordAuthentication no` (optional)
- On server, finally, restart `sshd` service using `sudo systemctl restart sshd`.

### SELinux

- More control over processes
- States are `Enforcing`, `Permissive` and `Disabled`. First state only enforces SELinux polices. Second only logs policy exceptions to `/var/log/audit/audit_log`. Third makes SELinux doesn't participate in system security.
- For first two states, policies are `Strict` and `targeted`.
- `Strict` - All activity is subject to SELinux restrictions. In `Targeted` policy, policy only enforced on specific processes (e.g. `httpd`, `named`, `ntpd`, `snmpd`...etc.)
- `sestatus` - will show info about SELinux. `ls -Z` will show info about files.
- `unconfined_u:object_r:user_home_t:s0` - Maps to `unconfined_u` which is the user context (`unconfined` means out of the scope of SELinux protection when using targeted policy). `object_r` the role context. `user_home_t` type context (`user_home_t` for files and folders in home of user). `s0` is MLS or multi-level security context.
- TE or Type Enforcement is the most common method used to determine the result of security policy. Role-Based Access Control (RBAC) & Multi-Level Security (MLS) are not used in targeted mode.
- `ps axZ` - Processes have a context which could be displayed using `ps axZ`
- `setenforce` and `setenforce 1` - to list and switch mode between `enforcing`, `permissive` and `disabled`. However, this is for current session only. Edit `/etc/sysconfig/selinux` to effect permanently.
- `chcon` - Changes context of files. E.g. `sudo chcon -R -t user_home_t foldername` makes `foldername` can't be used by http service as it belongs to home folder according to its context type. `-R` means recursively.
- `setsebool` - Set the value of Boolean
- Troubleshooting using `setenforce 0` then `aureport --avc` to change to permissive mode then view the AVC events in the audit log.

### FireWall

- `sudo filewall-cmd` With `--get-zones` or `--get-active-zones` or `--zone=public --list-all` to list zones or active zones or all services in specified zones
- `sudo firewall-cmd --reload` - To reload rules and take effect of new rules
- `sudo firewall-cmd --zone=public --permanent` With `--remove-port=80/tcp` to remove that rule or `--add-port=80/tcp` to add the rule or `--add-service=http` to allow access to http service as a rule

### Redirecting & Piping

`>` - Redirecting output to another file
`>>` - Appending instead of overwriting
`date +%F >| file2` - Redirecting output to another file with overwriting switch on. So even if shell option `set -o noclobber` enabled. It would work to overwrite.
`ls | tee file1` - Same as `>|` but showing the output to STDOUT along with writing to the file. Use `tee -a` for appending instead of overwriting.

### VIM

- `set showmode number` - set both options `showmode` and show numbers with `number`
- `vimtutor` - Instructions how to use `vim` editor.
- `vi ~/.vimrc` - To create `.vimrc` and add whatever config in it
- `source $VIMRUNTIME/defaults.vim` - add it to the beginning of `.vimrc` with proper path of default config wanted to extend the default config instead of completely overwriting them.
- `:scriptnames` & `:version` for info about default config and more.

### Info

- `ls -ld /etc` will shows result like `drwxr-xr-x. 126 root root 8192 Sep 28 00:17 /etc`.
  - `d` - means directory
  - `rwxr-xr-x` - permissions
  - `126` - number of hard links pointed to this file/folder
  - `root` - User owner
  - `root` - Group Owner
  - `8192` - Folder size
  - `Sep 28 00:17` - Last modified time of this directory
- File types
  - `-` : regular file.
  - `d` : directory.
  - `c` : character device file.
  - `b` : block device file.
  - `s` : local socket file.
  - `p` : named pipe.
  - `l` : symbolic link.

### Users & Groups

- `useradd anne` - Create user called `anne`
- `passwd anne` - Give user a password
- `passwd -e anne` - Set password in expired state
- `groupadd engineering` - Creates a group. Verify using `cat /etc/group`
- `sudo usermod -aG engineering anne` - Add `anne` to group `engineering`

## Linux FileSystem (File System Hierarchy Standard FHS)

- `/` - Root. Highest level in the hierarchy
- `/bin` - Command binaries. Symbolic link to `/usr/bin`
- `/boot` - Boot loader & kernel
- `/dev` - System devices (disks, USB drive, null (device sending data to nowhere)).
- `/etc` - System configuration files
- `/home` - User home directories
- `/lib` - System libraries
- `/media` - Mount point for removable media
- `/opt` - Often used for third-party packages
- `/proc` - System information
- `/root` - Root's home directory
- `/run` - Process information
- `/sbin` - System binaries
- `/sys` - System information
- `/tmp` - Temporary information
- `/usr` - Some programs live here
- `/var` - Contains log files and others that change over time

## Troubleshooting

- `sudo systemd-analyze blame` - List of startup services and time taken to execute. `sudo systemd-analyze plot > startup.svg` Will create plot graph of time taken to boot
- `/var/log` - To see apps logs. `messages` log is for processes with no specific log file like when connecting USB device you can `tail -f /var/log/messages` to know if the device is recognized by the system and `audit/audit.log`/`sudo aureport`
- `sudo cat secure` - Secure logs including use of `sudo` in the system
- `top` - for processes monitoring
- `system status httpd.service -l` - Troubleshooting a service and see its status and recent logs
- `journalctl -xe` - To see system logs. `journalctl -u sshd.service --since "yesterday"` & `journalctl -p err -b` & `journalctl -p err -b -o verbose` & `journalctl -f` are some variants
- `less /var/log/messages` - See system logs in formatted way

## References

<https://www.server-world.info/en/note?os=CentOS_8&p=initial_conf&f=9>

## Tips & Guides

### Tricks with ordered steps

#### Customize boot loader

```bash

# Create backup just in case
cp /etc/default/grub /etc/default/grub.BAK

# Edit the GRP2 boot loader as you wish then save it
vi /etc/default/grub

# Now we want to build
# the file to get configuration
# from the file we just edited

# Check what system you're on EFI or BIOS
demsg | grep "EFI v"

# If no output from previous command. Means BIOS
grub2-mkconfig -o /boot/grub2/grub.cfg

# If EFI based system is indicated. Means EFI
grub2-mkconfig -o /boot/efi/EFI/centos/grub.cfg
```

#### Rescue mode

```bash
# In rescue mode, you may chroot into
# the installed system root file system
chroot /mnt/sysimage/

# Exit the mode
exit
reboot

# Change root password of the system
passwd

# Plugin new USB device and operate it.
# /dev/sdb1 considered to be the USB drive
mkdir /mnt/whatever-usb-is-for && mount /dev/sdb1 /mnt/whatever-usb-is-for

# Copy config to backup to the USB
cp /mnt/sysimage/etc/postfix/main.cf /mnt/whatever-usb-is-for

# Identify bios or efi
dmesg | grep "EFI v"

# List disks and identify the star partition
fdisk -l

# In Bios based system
# Install grub2 if got corrupted on 
# /dev/sda (without number so it's not sda1)
# assuming that /dev/sda1 was the star partition
grub2-install /dev/sda

# In EFI based system
# Install grub2 if got corrupted on
yum reinstall grub2-efi shim -y
```

#### Recommended groups & packages to install

```bash
dnf group install -y base development rpm-development-tools
dnf install -y vim net-tools screen netcat rsync wget curl -y
```

#### Yum

```bash
# Search for what httpd is depending on
yum deplist httpd

# Figure what package contains the command pstree
yum whatprovides */pstree

# Know what package comes with its own .service definition files
yum whatprovides */*.service

# Keep yum clean and tidy
yum clean packages
yum clean metadata
yum clean dbcache
yum clean all
yum makecache
yum update

###############################

# To use priorities with yum
yum install yum-plugin-priorities

# Check yum priorities if enabled or not
vi /etc/yum/pluginconf.d/priorities.conf

# Add priority=1, priority=2 ...etc to this
# config after repository you like
# No need to plugins for Centos 8 as this is built-in
vi /etc/yum.repos.d/CentOS-Base.repo

###############################

# Install repos and enable PowerTools as EPEL
# repo maybe depending on it
# https://fedoraproject.org/wiki/EPEL
dnf config-manager --set-enabled PowerTools
yum install https://dl.fedoraproject.org/pub/epel/epel-release-latest-8.noarch.rpm

###############################

# Make your repo

# First run apache http server
yum install httpd -y; systemctl enable httpd; systemctrl start httpd

# Add apache web server to accessible list
firewall-cmd --permanent --add-service http
firewall-cmd --reload
firewall-cmd --list-services

# Download everything iso then install createrepo package
yum install creterepo

# Create directory for packages
mkdir -p /var/www/html/reposiory/centos/7.3

# Get the packages
mkdir /mnt/cdrom
mount -t iso9660 -o loop whatever-everything-centos-dvd.iso /mnt/cdrom
cp -r /mnt/cdrom/Packages/* /var/www/html/reposiory/centos/7.3

# Update the packages
vi ~/update-whatever-name-here.sh

# add the following to the sh created script
rsync -avz rsync://mirror.aaaaaaaa/centos/7/os/x86_64/Packages /var/www/html/repository/centos/7.3/
restorecon -v -R /var/www/html

# Make the file executable
chmod +x ~/update-whatever-name-here.sh

# Make script run daily as cron job
vi /etc/cron.daily/update-whatever-name-here.sh

# add the following to the sh to run at 2:30 AM
30 2 * * * /root/update-whatever-name-here.sh

# Finally run the script
~/update-whatever-name-here.sh

# Create the repo
createrepo --database /var/www/html/repository/centos/7.3

# On other machine to access this reposiroy
vi /etc/yum.repos.d/whatevernameofrepo.repo

# Add the following with changing the appropiate
[whatevernameofrepo]
name=my CentOS 7.3 mirror
baseurl=http://master/repository/centos/7.3
gpgcheck=1
gpgkey=https://www.centos.org/keys/RPM-GPG-KEY-CentOS-Official

# Test the repository
yum repolist | grep whatevernameofrepo
yum --disablerepo="*" --enablerepo="whatevernameofrepo" list available
###############################
```

#### RPM package installation

```bash

# Install using rpm package management
rpm -Uvh pv-1.4.6.rpm

# query rpm database of all installed package
rpm qa| grep 'pv-'

# show all files in software package
rpm -qlp pv-1.4.6.rpm

# General info
rpm -qip pv-1.4.6.rpm

# Documentation files
rpm -qdp pv-1.4.6.rpm

# Remove package using its name only
rpm -e pv

# Yum could be used to install rpm and
# it will resolve any dependency needed
yum install pv-1.4.6.rpm
```

#### Install on new system

```bash
dnf config-manager --set-enabled PowerTools
dnf group install xfce-desktop development
systemctl set-default graphical.target
systemctl isolate graphical.target
dnf install open-vm-tools open-vm-tools-desktop xorg-x11-drv-vmware
dnf install epel-release

# Add remi repository
cd /tmp
curl -O https://rpms.remirepo.net/enterprise/remi-release-8.rpm
rpm -Uvh remi-release-8.rpm

# Set priority for base repo to 1 (add priority=1 under base repo)
vi /etc/yum.repos.d/CentOS-Base.repo

# Set enabled to 1 and add priority=10 (lower than main repository)
vi /etc/yum.repos.d/remi.repo

# Same for epel repo
vi /etc/yum.repos.d/epel.repo
```

#### General Tips

```bash
# last argument passed to latest command
!$

# Service management tool that uses SystemD to manage services
# Used with
# `status servicename` for info
# `start|stop servicename` for start/stop
# `enable|disable servicename` for enabling/disabling
# boot the service with the system
# `restart servicename` for restarting the service  
# `reload servicename` same as restarting but without interruption
# and not all services support this option
systemctl

# Services are saved in unit files.
# This command lists all services
systemctl list-unit-files
systemctl -t service -a --state running

# Evaluates to `ls -l /dev/pts/1` because `$()` evaluates
# what's in it first then the result combined to the outer command
ls -l $(tty)

# Cron job details and location to start with
cat /etc/crontab
man 5 crontab
crontab -l
ls -laht /etc/cron.*

# rsync for backup/copy tool
rsync -avzh --progress  --delete /tmp/source /tmp/target

# diff to compare folders
diff -r /tmp/source /tmp/target
```

#### Install Oracle Database

```bash
# as root

# Do some pre-configuration
# http://yum.oracle.com/repo/OracleLinux/OL8/baseos/latest/x86_64/index.html
# https://docs.oracle.com/en/database/oracle/oracle-database/19/ladbi/running-rpm-packages-to-install-oracle-database.html#GUID-BB7C11E3-D385-4A2F-9EAF-75F4F0AACF02
dnf -y install https://yum.oracle.com/repo/OracleLinux/OL8/baseos/latest/x86_64/getPackage/oracle-database-preinstall-19c-1.0-1.el8.x86_64.rpm

# Install RPM downloaded Oracle Database Binary
# https://www.oracle.com/database/technologies/oracle-database-software-downloads.html
dnf -y install oracle-database-ee-19c-1.0-1.x86_64.rpm

# Optional, configure sample database
/etc/init.d/oracledb_ORCLCDB-19c configure

# Set oracle user password
passwd oracle

# Add to the end some variables
# https://docs.oracle.com/en/database/oracle/oracle-database/19/ladbi/using-sql-plus-to-unlock-accounts-and-reset-passwords.html#GUID-1147D2B9-8FFC-4F91-A774-E97066B4E9C5
vi ~/.bash_profile
umask 022
export ORACLE_SID=ORCLCDB
export ORACLE_BASE=/opt/oracle/oradata
export ORACLE_HOME=/opt/oracle/product/19c/dbhome_1
export PATH=$PATH:$ORACLE_HOME/bin

# Reload
source ~/.bash_profile

# Reset password of sys user
sqlplus / as sysdba

CONNECT SYS as SYSDBA;

# Set sys password to connect using netowrk if wanted
alter user sys identified by YOUR_NEW_PASS;
exit;

# Add port to firewall. First git active zone to put the port into it
# https://www.aclnz.com/interests/blogs/solved-how-to-add-port-1521-in-firewall
firewall-cmd --get-active-zones

# Add the port to public zone from previous command
firewall-cmd --zone=public --add-port=1521/tcp --permanent
firewall-cmd --reload
```

#### Static IP address for network adapter

```bash
# Show network adapters
ip address show

# Calculate network addresses
ipcalc -b -m -n 10.0.2.15/24

# Find out the gateway
route

# Edit the config for specific adapter (ens33 as example here)
vi /etc/sysconfig/network-scripts/ifcfg-ens33
```

- Set the following keys to following values then save and close the file
- No DHCP `BOOTPROTO=none`
- Disables network manager taking control on boot for configuration files related to this adapter
  `NM_CONTROLLED="no"`
  `PEERDNS="no"`
  `PEERROUTES="no"`
- Add network details

  ```properties
  IPADDR=192.168.101.100
  NETMASK=255.255.255.0
  BROADCAST=10.0.2.255
  NETWORK=10.0.2.0
  GATEWAY=10.0.2.0
  ```

  ```bash
  # Restart the network daemon
  systemctl restart network
  ```

#### Kickstart file installation method

- Better use everything DVD so the packages could be found directly and installed
- Use kick-start installation file already existing on installed system. It's located in `/root/anaconda-ks.cfg`
- Stick the USB device and run the command `fdisk -l` which will show the USB device
- Assuming the device is `/dev/sdb1`. Mount the device with specific directory using `mkdir /mnt/kickstart-usb && /dev/sdb1 /mnt/kickstart-usb`
- Copy the kick-start file using `cp /root/anaconda-ks.cfg /mnt/kickstart-usb-ks.cfg`
- Go to target machine and boot CentOS installation in rescue mode to determine the correct target volume and attach two USB devices
  - The kick-start USB device that we just used and has the kick-start file.
  - The installation USB with the recommended everything ISO installation ISO
- Boot to `Troubleshooting` then `Rescue a CentOS Linux system` then press `3) Skip to shell` to enter rescue mode
- Use `fdisk -l` and determine the disk to install the system on. Assuming at this point its name would be `/dev/sdb`
- Get back the kick-start USB device on CentOS running system to modify the kick-start file on it
- Modify the file on the drive and make some changes like
  - Comment line `graphical` to `#graphical`
  - Add command to text installer instead of graphical by adding the line `text` after the commented `#graphical` previously done
  - Change the host name in the line `network --hostnam=whatever.com`
  - Change the target disk instead of `=sda` to `=sdb` as this is determined from targeted machine in previous step.
  - Change `clearpart --none --initlabel` to `clearpart -all -drives=sdb` to clear the disk before installation
  - Add packages under the `%packages` to be installed after installation is done
- Validate the kick-start file by `dnf install system-config-kickstart -y` then `ksvalidator /mnt/kickstart-usb/ks.cfg`. No output means no error
- Get the name of the partition that our kick-start file is located on using the command `blkid /dev/sdb1` and copy the UUID which assumed to be `5555-6666`
- Go to target machine and connect back the kick-start USB to it
- On boot menu, press the escape key to enter custom boot screen
- Custom boot `boot:` will show. Start installation using `linux ks=hd:5555-6666:ks.cfg` where
  - `hd` is the protocol to access the file. There are others supported like `http://aaa/ks.cfg`
  - `5555-6666` is the UUID from previous step
  - `ks.cfg` file name on USB drive
- Press `Enter` and finally reboot the machine and verify using login then `hostnamectl` to see the hostname