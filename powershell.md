# PowerShell

## References

- https://docs.microsoft.com/en-us/powershell/?view=powershell-7.1
- https://www.improvescripting.com/easy-steps-for-writing-powershell-functions/
- [Remove path limitation](https://docs.python.org/3/using/windows.html#removing-the-max-path-limitation)
- [Python Venv For Windows](https://docs.python.org/3/library/venv.html)
- [.NET in PowerShell](https://xkln.net/blog/using-net-with-powershell/#net-syntax-in-powershell)
- [Type Accelerator](https://learn.microsoft.com/en-us/powershell/module/microsoft.powershell.core/about/about_type_accelerators?view=powershell-7.4)

---

## Profile on startup

- [Reference](https://docs.microsoft.com/en-us/powershell/module/microsoft.powershell.core/about/about_profiles?view=powershell-7.1)
- Path is `$Home\Documents\PowerShell\Profile.ps1`

### Installations

```powershell
winget install --id Microsoft.Powershell --source winget

# https://scoop.sh
scoop bucket add nonportable
scoop bucket add extras
scoop bucket add nerd-fonts

# Utilities
scoop install nerd-fonts/JetBrainsMono-NF
scoop install main/starship
scoop install extras/firewallappblocker

# Desktop Apps
scoop install nonportable/biglybt-np
scoop install extras/draw.io
scoop install extras/jetbrains-toolbox
scoop install extras/rufus

scoop install extras/vscode

# Screen share, meetings and screenshots
scoop install extras/sharex
scoop install extras/anydesk
scoop install extras/greenshot

# Multimedia
scoop install extras/mkvtoolnix
scoop install extras/makemkv
scoop install extras/vlc
scoop install extras/qmplay2
scoop install extras/smplayer
scoop install extras/shutter-encoder

# Browsers
scoop install extras/firefox
scoop install extras/postman
scoop install extras/googlechrome

# Docker
scoop install extras/rancher-desktop
```

### Recommended Startup Script

- [Reference](https://github.com/dahlbyk/posh-git)
- Add `Import-Module posh-git` to startup profile. Any maybe `$GitPromptSettings.EnableFileStatus = $false` for performance.
- [Starship](https://github.com/starship/starship#%F0%9F%9A%80-installation) for awesome shell.

```powershell
# Unicode for displaying
# $OutputEncoding = [Console]::InputEncoding = [Console]::OutputEncoding = New-Object System.Text.UTF8Encoding
# https://stackoverflow.com/a/49481797
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8


# Git auto complete [Optional]
# Import-Module posh-git

# Starship
Invoke-Expression (&starship init powershell)

# Docker Autocomplete
# Might need
# Install-Module -Name DockerCompletion -Scope CurrentUser
Import-Module DockerCompletion

# For general auto complete customizations
Import-Module PSReadLine

# Scoop Autocomplete
Import-Module "$($(Get-Item $(Get-Command scoop.ps1).Path).Directory.Parent.FullName)\modules\scoop-completion"

# Shows navigable menu of all options when hitting Tab
Set-PSReadlineKeyHandler -Key Tab -Function MenuComplete
# Autocompletion for arrow keys
Set-PSReadlineKeyHandler -Key UpArrow -Function HistorySearchBackward
Set-PSReadlineKeyHandler -Key DownArrow -Function HistorySearchForward

# https://github.com/microsoft/winget-cli/blob/master/doc/Completion.md
# https://techcommunity.microsoft.com/t5/itops-talk-blog/autocomplete-in-powershell/ba-p/2604524
Register-ArgumentCompleter -Native -CommandName winget -ScriptBlock {
    param($wordToComplete, $commandAst, $cursorPosition)
        [Console]::InputEncoding = [Console]::OutputEncoding = $OutputEncoding = [System.Text.Utf8Encoding]::new()
        $Local:word = $wordToComplete.Replace('"', '""')
        $Local:ast = $commandAst.ToString().Replace('"', '""')
        winget complete --word="$Local:word" --commandline "$Local:ast" --position $cursorPosition | ForEach-Object {
            [System.Management.Automation.CompletionResult]::new($_, $_, 'ParameterValue', $_)
        }
}

# Run specific script on startup using call operator &
# https://learn.microsoft.com/en-us/powershell/module/microsoft.powershell.core/about/about_operators?view=powershell-7.4#call-operator-
& "C:\Users\ahmed\Desktop\data\apps\pyenv\Scripts\Activate.ps1"
```

- [Reference](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-completion.html#cli-command-completion-windows)

```powershell
# https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-completion.html#cli-command-completion-windows
Register-ArgumentCompleter -Native -CommandName aws -ScriptBlock {
    param($commandName, $wordToComplete, $cursorPosition)
        $env:COMP_LINE=$wordToComplete
        $env:COMP_POINT=$cursorPosition
        aws_completer.exe | ForEach-Object {
            [System.Management.Automation.CompletionResult]::new($_, $_, 'ParameterValue', $_)
        }
        Remove-Item Env:\COMP_LINE
        Remove-Item Env:\COMP_POINT
}
```

Install git

```powershell
PowerShellGet\Install-Module posh-git -Scope CurrentUser -Force
```

Install [Scoop](https://github.com/ScoopInstaller/Install#readme)

```powershell
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
irm get.scoop.sh -outfile 'install.ps1'
.\install.ps1 -ScoopDir 'D:\Apps\scoop\dir' -ScoopGlobalDir 'D:\Apps\scoop\globaldir' -ScoopCacheDir 'D:\Apps\scoop\cache'
Remove-Item .\install.ps1
scoop bucket add extras
scoop install scoop-completion
```

Install Python

```powershell
scoop install python
# Make it detectable by other apps
D:\Apps\scoop\dir\apps\python\current\install-pep-514.reg
# Create virtual environment
python -m venv D:\Apps\venv
# Activate it
D:\Apps\venv\Scripts\activate.ps1
```

MacOS Startup File

```powershell
# https://learn.microsoft.com/en-us/powershell/scripting/learn/ps101/09-functions?view=powershell-7.5#advanced-functions
# https://learn.microsoft.com/en-us/powershell/module/microsoft.powershell.core/about/about_functions_cmdletbindingattribute?view=powershell-7.5
[CmdletBinding()] param ()

# Unicode for displaying
# $OutputEncoding = [Console]::InputEncoding = [Console]::OutputEncoding = New-Object System.Text.UTF8Encoding
# https://stackoverflow.com/a/49481797
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

# Curl
$env:PATH = '/opt/homebrew/opt/curl/bin:' + $env:Path     # attach to the beginning
$env:LDFLAGS = "-L/opt/homebrew/opt/curl/lib"
$env:CPPFLAGS = "-I/opt/homebrew/opt/curl/include"

# Rancher desktop. Aut managed in .zshrc
$env:PATH += ":$env:HOME/.rd/bi"

# Binaries
$env:PATH += ':/usr/local/bin'
$env:PATH += ':/usr/bin'

# Brew
$(/opt/homebrew/bin/brew shellenv) | Invoke-Expression

# Mise
mise activate pwsh | Out-String | Invoke-Expression

# Starship
Invoke-Expression (&starship init powershell)

# Brew completions
if ((Get-Command brew) -and (Test-Path ($completions = "$(brew --prefix)/share/pwsh/completions"))) {
  foreach ($f in Get-ChildItem -Path $completions -File) {
    . $f
  }
}

# pnpm
# Set PNPM_HOME environment variable
$env:PNPM_HOME = "$env:HOME/Library/pnpm"

# Check if PNPM_HOME is already in the PATH
if ($env:PATH -notcontains $env:PNPM_HOME) {
  # Add PNPM_HOME to the PATH
  $env:PATH = $env:PNPM_HOME + ":" + $env:PATH
}
# pnpm end

# Docker Autocomplete
Import-Module DockerCompletion

# For general auto complete customizations
Import-Module PSReadLine

# Shows navigable menu of all options when hitting Tab
Set-PSReadlineKeyHandler -Key Tab -Function MenuComplete
# Autocompletion for arrow keys
Set-PSReadlineKeyHandler -Key UpArrow -Function HistorySearchBackward
Set-PSReadlineKeyHandler -Key DownArrow -Function HistorySearchForward

# SAML2AWS
$env:SAML2AWS_BROWSER_TYPE = "firefox"

# Mvnd - Dynamically resolve and add the mvnd binary path to PATH
try {
  # Use Invoke-Expression to capture command output
  # Get the base mvnd path
  $mvndBase = Invoke-Expression "mise where mvnd 2>&1"
  
  if ($mvndBase -and (Test-Path $mvndBase)) {
    # Use a more direct pattern to find the bin directory
    # Look for any maven-mvnd-* directory first, then find the bin directory within it
    $mavenMvndDir = Get-ChildItem -Path $mvndBase -Directory | 
    Where-Object { $_.Name -match "maven-mvnd-" } | 
    Select-Object -First 1 -ExpandProperty FullName
      
    if ($mavenMvndDir) {
      $mvndBinPath = Join-Path $mavenMvndDir "bin"
          
      if (Test-Path $mvndBinPath) {
        $env:PATH = "$mvndBinPath" + [System.IO.Path]::PathSeparator + $env:PATH
      }
    }
  }
}
catch {
  # https://learn.microsoft.com/en-us/powershell/module/microsoft.powershell.core/about/about_try_catch_finally?view=powershell-7.5#using-multiple-catch-statements
  Write-Host "Error setting up mvnd PATH: $_"
}
```

---

## Powershell 101

### Line Breaks

Natural line breaks can occur at commonly used characters including comma `,` and opening brackets `[`, braces `{`, and parenthesis `(`. Others that aren't so common include the semicolon `;`, equals sign `=`, and both opening single and double quotes `'` & `"`.

### Update notification

Controlled by environment variable `POWERSHELL_UPDATECHECK` with values `off`, `Default` or `LTS`

### PowerShell Syntax

- Commands starts with Get, Set, Start, Stop, Out or New.
- Variables created using `$` in the beginning of the name.
- The `$_` automatic variable represents each object that is passed to the `Where-Object` cmdlet.
- `$args[0]` are arguments passed separately (by space for example) when running the command.

---

## CMDlets

### SSH

sshd_config

```config
PasswordAuthentication no
PubkeyAuthentication yes
AuthorizedKeysFile .ssh/authorized_keys
LogLevel DEBUG
```

```powershell
# https://learn.microsoft.com/en-us/windows-server/administration/openssh/openssh_install_firstuse?tabs=powershell
Get-WindowsCapability -Online | ? Name -like 'OpenSSH*'
# Output from previous command, install client or server as needed
Add-WindowsCapability -Online -Name "OpenSSH.Server~~~~0.0.1.0"
Start-Service sshd
# OPTIONAL but recommended:
Set-Service -Name sshd -StartupType 'Automatic'

# Confirm the Firewall rule is configured. It should be created automatically by setup. Run the following to verify
if (!(Get-NetFirewallRule -Name "OpenSSH-Server-In-TCP" -ErrorAction SilentlyContinue | Select-Object Name, Enabled)) {
    Write-Output "Firewall Rule 'OpenSSH-Server-In-TCP' does not exist, creating it..."
    New-NetFirewallRule -Name 'OpenSSH-Server-In-TCP' -DisplayName 'OpenSSH Server (sshd)' -Enabled True -Direction Inbound -Protocol TCP -Action Allow -LocalPort 22
} else {
    Write-Output "Firewall rule 'OpenSSH-Server-In-TCP' has been created and exists."
}

# Restart the service
Restart-Service sshd

# Add public key of local client to authorized_keys file on remote machine
# Then run these permissions commands
# and do the same for $HOME\.ssh\authorized_keys
# Notice that C:\ProgramData\ssh\administrators_authorized_keys
# is used if the user is admin
$acl = Get-Acl C:\ProgramData\ssh\administrators_authorized_keys
$acl.SetAccessRuleProtection($true, $false)
$administratorsRule = New-Object system.security.accesscontrol.filesystemaccessrule("Administrators","FullControl","Allow")
$systemRule = New-Object system.security.accesscontrol.filesystemaccessrule("SYSTEM","FullControl","Allow")
$acl.SetAccessRule($administratorsRule)
$acl.SetAccessRule($systemRule)
$acl | Set-Acl


# Windows remoting tools over ssh
# https://learn.microsoft.com/en-us/powershell/scripting/security/remoting/ssh-remoting-in-powershell?view=powershell-7.2#windows-to-windows
# https://gist.github.com/gildas/6b4ec9b80c3f61e61b01e29dccc87c8c#powershell-core-sessions
Install-Module Microsoft.PowerShell.RemotingTools
Enable-SSHRemoting
help Enable-SSHRemoting

# Connecting
Enter-PSSession -HostName Desktop-server -UserName Echo -KeyFilePath ~/.ssh/id_ed25519
ssh -v -i ~/.ssh/id_ed25519 Echo@Desktop-server
scp -T 'Echo@Desktop-server:"C:\Users\Echo\Documents\Deadpool And Wolverine 2024 1080p 10bit WEBRip 6CH X265 HEVC-PSA.mkv"' C:\Users\ahmed\Desktop

# Misc
Get-Service ssh-agent | Set-Service -StartupType Automatic
```

### Execution Policy

```powershell
# https://docs.microsoft.com/en-us/powershell/scripting/learn/ps101/01-getting-started
# Get currently applied execution policy
Get-ExecutionPolicy
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
```

### Help / Info commands

```powershell
<#
Helps you discover what objects, properties, and methods are available for commands. Any command that produces object-based output can be piped to Get-Member.
#>
Get-Member
Get-Help -Name Get-Help -Full
Get-Help -Name Get-Help -Details
Get-Help -Name Get-Help -Online
Get-Help Get-ChildItem -Parameter *
Get-Help about_*
Get-Help Add-Member - Full | Out-String _Stream | Select-String -Pattern clixml
Get-Help <function_name>
Get-Help .\whatever.ps1

# Example of listing members of output from Get-Service and specifying the type
# of the member to list
Get-Service -Name w32time | Get-Member
Get-Service -Name w32time | Get-Member -MemberType Method

# Using a method
(Get-Service -Name w32time).Stop()

# A better option is to use a cmdlet to perform the action if one exists. Go ahead and start the Windows Time service, except this time use the cmdlet for starting services.
# One of the benefits of using a cmdlet is that many times the cmdlet offers additional functionality that isn't available with a method. Like here, the PassThru parameter was used. This causes a cmdlet that doesn't normally produce output, to produce output.
Get-Service -Name w32time | Start-Service -PassThru
Start-Service -Name w32time -PassThru | Get-Member

# Find commands that accept that type of object as input
Get-Command -ParameterType ServiceController
Get-Command -Verb Add
Get-Command -Noun Computer
Get-Command -Noun Process
Get-Command -CommendType Cmdlet
Get-Command *event*
Get-Command | Group-Object -Property CommandType

# Commands that take -Filter as parameter
Get-Command -ParameterName Filter

# Recommended to run periodically
Update-Help

# Select-Object used to force showing properties not showed by default
Get-Service -Name w32time | Select-Object -Property *
Get-Service -Name w32time | Select-Object -Property Status, Name, DisplayName, ServiceType
Get-Service -Name w32time | Select-Object -Property Status, DisplayName, Can*

# Same as Select-Object & Format-Table but with GUI
Get-Service -Name w32time | Out-Gridview

# Search modules in repositories to find CMDLets, aliases, functions and workflows
# It returns PSGetCommandInfo
Find-Command

# List commands in specific module
Get-Command -module applocker

# Import module
Import-Module -Name PSDiagnostics

# Get-Module alone shows module imported in the current session
# Look for available modules in the .net core
Get-Module -ListAvailable

# Import module prefixing its functions with string
Import-Module PSDiagnostics -Prefix DEMO -PassThru

# Remove module from being imported in the current session
Remove-Module -Name whatever

# Find or search for a module
Find-Module -Tag 'PSEdition_core'

# Install module
Find-Module -Name 'CallPass' | Install-Module
Find-Module -Name 'CallPass' | Uninstall-Module
Find-Module -Name 'CallPass' | Update-Module

# List aliases
Get-Alias

# Set aliases
Set-Alias -Name "" -Value "" -Description ""

# Writes the specified objects to the pipeline or print if it's the last
Write-Output $Host

<# Selects objects that have particular property values from the collection of objects that are passed to it
Script block. You can use a script block to specify the property name, a comparison operator, and a property value. Where-Object returns all objects for which the script block statement is true.
Comparison statement. You can also write a comparison statement, which is much more like natural language.
#>
Get-Process | Where-Object {$_.PriorityClass -eq "Normal"}
Get-Process | Where-Object -Property PriorityClass -eq -Value "Normal"
Get-Process | Where-Object PriorityClass -eq "Normal"
('hi', '', 'there') | Where-Object Length -GT 0
('hi', '', 'there') | Where-Object {$_.Length -gt 0}

# Identical results
Get-Command -Name *workflow*
Get-Command | Where-Object { $_.Name -like '*workflow*'}

# https://docs.microsoft.com/en-us/powershell/module/microsoft.powershell.core/about/about_providers
# Gets the items and child items in one or more specified locations. Locations are exposed to Get-ChildItem by PowerShell providers.
"Env:" | Get-ChildItem
Get-ChildItem Env:

# Without committing the command, see the result to calculate the risk
# Use -whatif to see a simulation
# Use -confirm for confirm action on every result
Get-Service | Stop-Service -whatif
Get-Service | Stop-Service -confirm

# Function Syntax
function <name> {
  param ([type]$parameter1 [,[type]$parameter2])
  <statement list>
}

# Function example to run as Test-ServiceStarted  -Name wuauserv
function Test-ServiceStarted ([string]$name) {
if ((Get-Service -Name $name).Status -eq 'Running') {$true} else {$false}
}

# File Management
Get-ChildItem
New-Item
Copy-Item
Remove-Item
Get-Location
Set-Location
Get-Content
ConvertTo-Json
ConvertFrom-Json
Out-File

# Creating new object
New-Object System.Collections.ArrayList

# Search history using string pattern
Get-Content (Get-PSReadLineOption).HistorySavePath | Select-String -Pattern "mvn"
Get-Content (Get-PSReadLineOption).HistorySavePath | Select-String -Pattern "mvn" | Select-Object -Unique
Get-Content (Get-PSReadLineOption).HistorySavePath | Select-String -Pattern "mvn" | sort | Get-Unique

# https://docs.microsoft.com/en-us/powershell/module/microsoft.powershell.core/about/about_environment_variables
#
# Set Environment Variable
$env:Path                             # shows the actual content
$env:Path = 'C:\foo;' + $env:Path     # attach to the beginning
$env:Path += ';C:\foo'                # attach to the end
$env:Path = "C:\MyPath;$env:Path"

# Modify a system environment variable
[Environment]::SetEnvironmentVariable
     ("Path", $env:Path, [System.EnvironmentVariableTarget]::Machine)

# Modify a user environment variable
[Environment]::SetEnvironmentVariable
     ("INCLUDE", $env:INCLUDE, [System.EnvironmentVariableTarget]::User)

# Add to the system environment variable
[Environment]::SetEnvironmentVariable(
    "Path",
    [Environment]::GetEnvironmentVariable("Path", [EnvironmentVariableTarget]::Machine) + ";C:\bin",
    [EnvironmentVariableTarget]::Machine)

# String based solution is also possible if you don't want to write types
[Environment]::SetEnvironmentVariable("Path", $env:Path + ";C:\bin", "Machine")

# Remove the environment variable
[Environment]::SetEnvironmentVariable('FVM_HOME', [NullString]::Value, 'User')

# Syncthing docker command
docker run -v //d/Apps/syncthing_data:/var/syncthing -v //d/code/code-wsl:/code -d --name=syncthing-windows --network=cnetwork -p 8385:8384 -p 22001:22000/tcp -p 22001:22000/udp -p 21028:21027/udp --hostname=syncthing-windows syncthing/syncthing:latest

# IP of wsl instance
wsl -d "Ubuntu" hostname -I
```
