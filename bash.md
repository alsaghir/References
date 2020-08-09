# Bash (Mostly Git Bash)

## Ignore / slash

To ignore / in MingW bash we use MSYS2_ARG_CONV_EXCL like this

`MSYS2_ARG_CONV_EXCL="--base-href" ng build --prod --aot --base-href=/arrow-client/`

## Tips for daily usage

### in .bashrc or/and .bash_profile

#### History Config

```bash
HISTCONTROL=ignoredups:erasedups # no duplicate entries
shopt -s histappend # append to history, don't overwrite it
PROMPT_COMMAND="history -n; history -w; history -c; history -r; $PROMPT_COMMAND" # Save and reload the history after each command finishes
```

#### PS1 format for git and bash current path location

```bash
function color_my_prompt {
    local __user_and_host="\[\033[01;32m\]\u@\h"
    local __cur_location="\[\033[01;34m\]\w"
    local __git_branch_color="\[\033[31m\]"
    #local __git_branch="\`ruby -e \"print (%x{git branch 2> /dev/null}.grep(/^\*/).first || '').gsub(/^\* (.+)$/, '(\1) ')\"\`"
    local __git_branch='`git branch 2> /dev/null | grep -e ^* | sed -E  s/^\\\\\*\ \(.+\)$/\(\\\\\1\)\ /`'
    local __prompt_tail="\[\033[35m\]$"
    local __last_color="\[\033[00m\]"
    export PS1="$__user_and_host $__cur_location $__git_branch_color$__git_branch$__prompt_tail$__last_color "
}

color_my_prompt
```

#### Git auto complete

Check the following

https://github.com/git/git/blob/master/contrib/completion/git-completion.bash
https://github.com/git/git/blob/master/contrib/completion/git-prompt.sh

```bash
source /D/Programs/scripts/git-completion.bash
source /D/Programs/scripts/git-prompt.sh
```

### MSYS2 Shell Custom Script

To be used as separated script like in `msys2_shell_custom.cmd` or in **Cmder**

```powershell
set "CHERE_INVOKING=1" & set "MSYSTEM=MINGW64" & set "MSYS2_PATH_TYPE=inherit" & set "PATH=%PATH%" & "bash.exe" --login -i
```

You may replace `bash.exe` with absolute path if not in PATH environment variable

For IDE and multiple apps that has no issues with path like Cmder, this maybe used without `PATH=%PATH%` part.

```powershell
set "CHERE_INVOKING=1" & set "MSYSTEM=MINGW64" & set "MSYS2_PATH_TYPE=inherit" & "bash.exe" --login -i
```

### Know what shel you're using

`echo $0` - `$0` is the name of the running process
`echo $SHELL` - Shell you have on default environment you can check the value of the `SHELL` environment variable
`ps -p "$$"` - where `$$` is PID of the current instance of shell and `ps -p <PID>` to find the process having the PID
