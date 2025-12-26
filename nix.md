# Nix

## Commands

- nix-shell

```sh
# Free up some disk space occupied by the different versions of programs you downloaded
nix-collect-garbage

# Install packages in a temporary shell, after done just exit the shell
nix-shell -p cowsay lolcat

# Install and run in one go
nix-shell -p cowsay lolcat --run "cowsay Hello | lolcat"

# Use a specific commit of nixpkgs to run git. Git available only inside the shell (--pure) and outsider git wont be available
nix-shell -p git --run "git --version" --pure -I nixpkgs=https://github.com/NixOS/nixpkgs/tarball/2a601aafdc5605a5133a2ca506a34a3a73377247

# nix-shell alternative is nix shell or nix develop
nix shell nixpkgs#cowsay nixpkgs#lolcat
nix shell nixpkgs#cowsay nixpkgs#lolcat --command bash -c 'cowsay Hello | lolcat'
nix shell nixpkgs#{cowsay,lolcat} --command bash -c 'cowsay Hello | lolcat'

# Enter development shell defined in flake.nix
nix develop

# Run a program from nixpkgs without installing it permanently
nix run nixpkgs#jq -- --version

# List kernels available in nixpkgs
nix repl
:l <nixpkgs>
lib.filter (name: lib.strings.hasPrefix "linuxPackages_" name) (builtins.attrNames pkgs)
:q

# Info about versions
nixos-version
nix --version
nix-instantiate --eval -E '(import <nixpkgs> {}).lib.version'
nix flake metadata ~/nix-config

# Regularly applying
nh clean all --keep 3
nh os switch ~/nix-config --hostname laptop

# Debugging nix
# https://nix.dev/manual/nix/2.32/development/debugging.html
# nix repl and -vvvvv
export mesonBuildType=debugoptimized

# Secrets management
nix shell nixpkgs#sops nixpkgs#age
# For generating keys
sops -i -e secrets/asus-laptop-ssh-keys.yaml
# For decrypting and editing
sops secrets/asus-laptop-ssh-keys.yaml
# Generate age key pair (keep the private key safe)
age-keygen -o ~/.config/sops/age/keys.txt

# Move keys.txt to /var secure and set proper permissions
sudo install -d -m700 /var/lib/sops-nix/age
sudo install -m600 -o root -g root /home/ahmed/.config/sops/age/keys.txt /var/lib/sops-nix/age/keys.txt
```

- script example

```sh
#!/usr/bin/env nix-shell
#! nix-shell -i bash --pure
#! nix-shell -p bash cacert curl jq python3Packages.xmljson
#! nix-shell -I nixpkgs=https://github.com/NixOS/nixpkgs/archive/2a601aafdc5605a5133a2ca506a34a3a73377247.tar.gz

curl https://github.com/NixOS/nixpkgs/releases.atom | xml2json | jq .
```

- With the `-i` option, bash is specified as the interpreter for the rest of the file.
- In this case, the `--pure` option is enabled to prevent the script from implicitly using programs that may already exist on the system on which the script is run.
- The `-p` option lists the packages required for the script to run.
- The command `xml2json` is provided by the package `python3Packages.xmljson`, while `bash`, `jq`, and `curl` are provided by packages of the same name. `cacert` must be present for SSL authentication to work.

- Workflow

```sh
# Initialize a new flake
nix flake init

nix flake update
nix flake show
nix flake check

# Rebuild system + HM. HM included as nixOs module in the flake.
# If used as standalone, use home-manager switch --flake ...
# laptop is the name of the system defined in the flake.
sudo nixos-rebuild switch --flake ~/nix-config#laptop
nh os switch ~/nix-config --hostname laptop
nh os switch ~/nix-config --optimize --hostname laptop

# Regularly apply and clean old generations
nh clean all --keep 3 

# Clean store manually
nix store gc
nix store optimise
nix-collect-garbage

# Search for packages
nix search nixpkgs vscode

# Update only home-manager input
nix flake update home-manager

# build with files not added to vcs
sudo nixos-rebuild switch --flake ~/nix-config#laptop --impure

# Build a specific package from the flake
nix build ~/nix-config#biglybt --impure

# Build a specific package from a specific system configuration in the flake
nix build ~/nix-config#nixosConfigurations.laptop.pkgs.biglybt --impure

# Build with logs
nix build ~/nix-config#nixosConfigurations.laptop.pkgs.biglybt --impure --print-build-logs
nix build ~/nix-config#nixosConfigurations.laptop.pkgs.biglybt --impure -L
```

### Commands mappings to new nix commands

- nix

```sh
# Enter development shell
# develop for dev environments and shell for regular shells as temp ephemeral shells 
# nix-shell is perfect for shell scripts only
nix-shell <=> nix develop or nix shell
nix-build <=> nix build
nix-env -iA pkgs.pkg <=> nix profile install nixpkgs#pkg
nix-env -e pkg <=> nix profile remove pkg
nix-channel <=> nix flake
nix-collect-garbage <=> nix store gc
# Evaluates nix expressions
nix-instantiate --eval <=> nix eval
nix-repl <=> nix repl
```

## For reference

- Disko config check [this video](https://www.youtube.com/watch?v=bKx7V917b2Q)
- [Packages overall configurations and aliases](https://github.com/NixOS/nixpkgs/blob/master/pkgs/top-level/all-packages.nix)

```nix
# hosts/laptop/disko-config.nix
{ ... }:
{
  disko.devices = {
    disk = {
      main = {
        type = "disk";
        device = "/dev/nvme0n1";  # Adjust to your actual device
        content = {
          type = "gpt";
          partitions = {
            ESP = {
              size = "512M";
              type = "EF00";
              content = {
                type = "filesystem";
                format = "vfat";
                mountpoint = "/boot";
                mountOptions = [ "fmask=0077" "dmask=0077" ];
              };
            };
            root = {
              size = "100%";
              content = {
                type = "btrfs";
                extraArgs = [ "-f" ];
                subvolumes = {
                  "@" = {
                    mountpoint = "/";
                    mountOptions = [ "compress=zstd" "noatime" "discard=async" ];
                  };
                  "@home" = {
                    mountpoint = "/home";
                    mountOptions = [ "compress=zstd" "noatime" "discard=async" ];
                  };
                  "@nix" = {
                    mountpoint = "/nix";
                    mountOptions = [ "compress=zstd" "noatime" "discard=async" ];
                  };
                  "@var_log" = {
                    mountpoint = "/var/log";
                    mountOptions = [ "compress=zstd" "noatime" "discard=async" ];
                  };
                  "@swap" = {
                    mountpoint = "/swap";
                    mountOptions = [ "noatime" "discard=async" ];
                    swap.swapfile.size = "8G";
                  };
                };
              };
            };
          };
        };
      };
    };
  };
}
```

## Packaging

- `nixpkgs` is flake input name while `<nixpkgs>` is the path to nixpkgs configured in nix.conf or NIX_PATH.
- Derivation is the low-level, buildable unit in the Nix store. It is the fundamental object that represents how to build something. The recipe Nix uses to produce a package or output. A pure, immutable description of how to build something, stored in the Nix store, usually as a .drv file
- References
  - [Nix Pills: Packaging](https://nixos.org/guides/nix-pills/packaging.html)
  - [Nix.dev: Derivations](https://nix.dev/manual/nix/2.32/language/derivations.html)
  - [Nix.dev: Building](https://nix.dev/manual/nix/2.32/store/building.html)
  - [Nixpkgs manual: Writing Nix expressions](https://nixos.org/manual/nixpkgs/stable/#chap-writing-nix-expressions)
  - [Nixpkgs manual: Packaging basics](https://nixos.org/manual/nixpkgs/stable/#chap-packaging-basics)
  - [NixOS Series 3: Software Packaging 101](https://lantian.pub/en/article/modify-computer/nixos-packaging.lantian/)
  - [NixOS & Flakes Book](https://nixos-and-flakes.thiscute.world/development/packaging-101)
  - [STD Environment](hhttps://ianthehenry.com/posts/how-to-learn-nix/the-standard-environment/) from [How to learn nix series](https://ianthehenry.com/posts/how-to-learn-nix/)
  - [The Standard Environment](https://ryantm.github.io/nixpkgs/stdenv/stdenv/) from [Nixpkgs Community Manual](ryantm.github.io/nixpkgs/stdenv/stdenv/)

```sh
nix build nixpkgs#hello
nix build nixpkgs#hello nixpkgs#cowsay
ls -l result*
nix eval --raw nixpkgs#hello.outPath
nix eval --raw nixpkgs#hello.drvPath
nix eval --raw nixpkgs#cowsay.drvPath 
```

- Sample package definition
  - [imports and callpackage](https://book.divnix.com/ch05-03-imports-and-callpackage.html) used to bind dependencies automatically
  - 

```c
#include <stdio.h>

void main() {
  printf("Hello from Nix!");
}
```

```nix
# simple.nix
let
  pkgs = import <nixpkgs> { };
in
  pkgs.stdenv.mkDerivation {
    name = "hello-nix";

    src = ./.;

    buildPhase = ''
      $CC simple.c -o hello_nix
    '';

    installPhase = ''
      mkdir -p $out/bin
      cp hello_nix  $out/bin/hello_nix
    '';
  }
```

```sh
nix-build simple.nix
./result/bin/hello_nix
```

- another simpler example
  - Builder execution explains where `$out` comes from [here](https://nix.dev/manual/nix/2.32/store/building.html?highlight=NIX_BUILD_TOP#builder-execution) and [here](https://nix.dev/manual/nix/2.18/language/derivations)
  - `makeWrapper` is used to create a wrapper script that sets up the environment for the actual script to run with its dependencies available in PATH
  - `makeWrapper` provided as provided as build time dependency in `buildInputs`
  - `wrapProgram` provided by `makeWrapper` is used to create the wrapper script allowing to set environment variables like PATH
  - `lib.makeBinPath` is used to create a colon-separated list of paths to the binaries of the specified packages. It will keep the paths to the binaries of `cowsay` and `ponysay` in PATH in the runtime keepin the original PATH intact. In other words, `PATH` variable modified only in the runtime environment of the script.

```nix
# hello.nix
{
  cowsay,
  ponysay,
  lib,
  makeWrapper,
  stdenv, # standard env to provide mkDerivation function # https://ryantm.github.io/nixpkgs/stdenv/stdenv
}:

# https://noogle.dev/f/pkgs/stdenv/mkDerivation
# function creates a derivation, and returns it in the form of a package attribute set that refers to the derivation's outputs.
stdenv.mkDerivation { # https://wiki.nixos.org/wiki/Build_Helpers
  pname = "my-package";
  version = "0.1.0";
  src = ./.; # current directory has the source code
  buildInputs = [makeWrapper]; # Dependencies required during build time
  # Bash script that creates the directory $out and calls make install
  installPhase = ''
    mkdir -p $out/bin
    cp myscript.sh $out/bin/myscript.sh
    chmod +x $out/bin/myscript.sh
    wrapProgram $out/bin/myscript.sh \
                --prefix PATH : ${lib.makeBinPath [cowsay ponysay]} '';
}
```

- Build the package

```sh
# nix-build builds a nix expression. -E allows passing expression directly.
# callPackage loads the default.nix file in the current directory
# callPackage automatically provides the dependencies defined in the package
nix-build -E 'with import <nixpkgs> {}; callPackage ./default.nix {}' 
```

- `fetchFromGitea` and alike are used to fetch source code from git repositories.
- `nativeBuildInputs` is like `buildInputs` but the first resolves to the build platform architecture while the latter resolves to the target platform architecture. As a rule of thumb, use `nativeBuildInputs` for build tools and `buildInputs` for libraries and runtime dependencies. When I say libraries and runtime dependencies, I mean those that are needed during the build phase as well as those needed during runtime but wont be available in the runtime environment unless explicitly added using `makeWrapper` as shown above or the host system provides them.

- [Example of home-manager module](https://code.m3ta.dev/m3tam3re/nixcfg/src/branch/video13/modules/home-manager)
  - `options` and `config` are standard arguments for home-manager modules. `options` is kinda of the schema definition for the module while `config` is the actual configuration provided by the user.

```nix
{
  config,
  lib,
  pkgs,
  ...
}:
with lib; let
  cfg = config.programs.zellij-ps;
in {
  options = {
    programs.zellij-ps = {
      enable = mkEnableOption "Zellij Project Selector";
      projectFolders = lib.mkOption {
        type = lib.types.listOf lib.types.path;
        description = "List of project folders for zellij-ps.";
        default = ["${config.home.homeDirectory}/projects"];
      };
      layout = lib.mkOption {
        type = lib.types.str;
        description = "Layout for zellij";
        default = ''
          layout {
                      pane size=1 borderless=true {
                          plugin location="zellij:tab-bar"
                      }
                      pane
                      pane split_direction="vertical" {
                          pane
                          pane command="htop"
                      }
                      pane size=2 borderless=true {
                          plugin location="zellij:status-bar"
                      }
                  }
        '';
      };
    };
  };
  config = mkIf cfg.enable {
    home.packages = [pkgs.zellij-ps];
    home.sessionVariables.PROJECT_FOLDERS = lib.concatStringsSep ":" cfg.projectFolders;
    home.file.".config/zellij/layouts/zellij-ps.kdl".text = cfg.layout;
  };
}
```

## Development

- [Nix.dev: Developing with Nix](https://nix.dev/tutorials/first-steps/declarative-shell#declarative-reproducible-envs)
- [NixOS & Flakes Book: Developing with Nix](https://nixos-and-flakes.thiscute.world/development/intro)
- [How to learn Nix: Developing with Nix](https://ianthehenry.com/posts/how-to-learn-nix/developing-with-nix/)  

- Using flake.nix in project root.

```nix
# flake.nix
{
  description = "Development environment for my project";

  inputs.nixpkgs.url = "github:NixOS/nixpkgs/nixpkgs-unstable";

  outputs = { self, nixpkgs }: {
    devShells.default = nixpkgs.lib.mkShell {
      buildInputs = with nixpkgs.pkgs; [
        python3
        pip
        git
        jq
      ];
      shellHook = ''
        echo "Welcome to the development shell for my project!"
      '';
    };
  };
}
```

- make sure direnv and direnv-nix are installed in your system. i.e. using home-manager

```nix
programs.direnv = {
    enable = true;
    enableZshIntegration = true;
    nix-direnv.enable = true;
  };
```

- in project root. `.envrc` should be committed to version control while `.direnv` should be ignored.

```sh
# One time setup per project
cd ~/projects/nestjs-backend
echo "use flake" > .envrc
direnv allow

# Force reload
direnv reload

# Disable for current session
direnv deny

# Re-enable
direnv allow
```
