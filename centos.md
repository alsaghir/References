# CentOS & Linux Quick Reference

## After minimal installation

On VM use local virtual host (private) & another adapter with NAT to access the internet. Enable both network cards/adapters on installation wizard.

### Adapter connection

`ip address show` or `ip a s` - will show connections with its assigned IP addresses and configs  
`nmcli conn show` - List adapters connections  
`nmcli conn up enps33` - Bringing up the connection called `enps33`  
`sudo sed -i s/ONBOOT=no/ONBOOT=yes/ /etc/sysconfig/network-scripts/ifcfg-ens34` - Make connection up and running on boot. This also should be done with each adapter like `ens34`  
`grep ONBOOT /etc/sysconfig/network-scripts/ifcfg-ens34` - will show that edit is effected

### Packages

`yum update` - update installed packages

`yum install -y redhat-lsb-core net-tools epel-release kernel-headers kernel-devel`

`yum groupinstall -y "Development Tools"` or `dnf group install gnome-desktop`- Group of packages installation by name (must be exact and case sensitive within double quotation marks) or by id

`systemctl set-default graphical.target` - After installing desktop, use this to make it default UI for the system

`systemctl isolate graphical.target` - to take effect immediately

`sudo dnf install open-vm-tools open-vm-tools-desktop xorg-x11-drv-vmware` - For running CentOS on VMware workstation.

## Terminal tips

`!$` - is the last argument passed to latest command  
`systemctl` - service management tool  
`ls -l $(tty)` - Evaluates to `ls -l /dev/pts/1` because `$()` evaluates what's in it first then the result combined to the outer command.

### Commands & Tools

- `dnf list installed`
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

### Disks

- `lsblk` - List block devices which are in this case are disks & partitions. Disks are in `/dev/` path
- `fdisk -l` - List disks
- `dd` - For disk image manipulation. `dd if=/dev/sr0 of=netinstall.iso` will create `netinstall.iso` of mounted device `/dev/sr0`.
- `dd if=/dev/zero of=/dev/sda count=1 bs=512` - `bs=512` specifying the block size and only one operation device so `count=1` will delete partition table of `/dev/sda`.
- `df` - Disk free tool of local file systems
- `df -hT` - Disk free of local file systems with file system

### Permissions

- FAT does not support permissions
- ACLs used for additional permissions. XFS fully supports it and build-in with CentOS. EXT however needs `acl` option to be added.
- Use `stat` or `ls` to see file permission
- 4 = READ, 2 = WRITE, 1 = EXECUTE
- `umask` - Sets default permission for new files
- `chmod` - Changes the permission of a file. Examples are `chmod 467 file1`, `chmod u=r,g=rw,o=rwx`, `chmod u=r,go=rw`, `chmod +x`, `chmod o=` means nothing to others, `chmod a+x` means all plus execute, `chmod o-t` means others minus sticky bit.
- Sticky bit is special permission for directories to prevent deletion/renaming/movement. `t` means execute permission is set and `T` means execution permission is not set. To create a directory with sticky bit use `mkdir -m 1777 test` as the `1` here is special permission for sticky bit.
-  Permissions in Linux are not cumulative and it goes in the order user, group, and then other. If you are a user you get only the permissions of the user; group permissions and other's permissions do not apply to you.
- The position that the `x` bit takes in the `rwxrwxrwx` for the user octal (1st group of `rwx`) and the group octal (2nd group of `rwx`) can take an additional state where the `x` becomes an `s`. When this occurs this file when executed (if it's a program and not just a shell script) will run with the permissions of the owner or the group of the file. So if the file is owned by root and the SUID bit is turned on, the program will run as root. Even if you execute it as a regular user. The same thing applies to the GUID bit. Executable bit disabled using uppercase `S`.
- `id -u` prints user id. `id -un` prints username. `id -gn` prints primary group name. `id -Gn` prints secondary groups names.
- Primary group used when creating file. When accessing file, secondary groups used.
- `chgrp wheel file2` - Change group of `file2`
- `newgrp wheel` - make `wheel` is my primary group creating another shell with it. Use `exit` to get back and verify with `id -gn`.
- `chown` - used to change the ownership of a file. `chown root file2` changes the user owner of `file2` to owner. `chown tux.tux file2` - changes the user owner and group owner to `tux`.
- `cp -a` - Copying a file with permission included.

### SU

- `su` - Creates new bash as root at same directory and some settings still sees me as original user. Verify with `echo $USER`
- `su -l` - Completely new environment with root access
- `cat /etc/sudoers` - To see users/groups
- `sudo visudo` - Edit to add users or groups. User like `tux ALL=(root) ALL` will add user `tux` from any host connecting as `root` o run all commands.
- `vi /etc/ssh/sshd_config` - Edit to uncomment & modify option to `PermitRootLogin no` then `systemctl restart sshd`
- `ssh-keygen -t ed25519 -C "<comment>"` or `$ ssh-keygen -t rsa -b 4096 -C "your_email@example.com"` - Generates new SSH key with type specified using `-t` option.
- `ssh-copy-id -i id_rsa.pub server1` - Copy public key to another server called `server1` which is a alias. Normally it should put the original server IP, port & user.
- In `~` you can make new `.ssh` folder and inside it add config file `vi config` to create server alias like that

```bash
Host server1
    HostName 192.168.56.105
    User root
    Port 22
```

- `scp file1 server1:/tmp` - Copy from current server to `server1`. Reverse could be done using `scp server1:/tmp file1`

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

## References

<https://www.server-world.info/en/note?os=CentOS_8&p=initial_conf&f=9>
