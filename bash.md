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
