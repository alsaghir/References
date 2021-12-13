# PowerShell

## References

- https://docs.microsoft.com/en-us/powershell/?view=powershell-7.1
- https://www.improvescripting.com/easy-steps-for-writing-powershell-functions/

---
## Profile on startup

- [Reference](https://docs.microsoft.com/en-us/powershell/module/microsoft.powershell.core/about/about_profiles?view=powershell-7.1)
- Path is `$Home\Documents\PowerShell\Profile.ps1`

### Recommended Startup Script

- [Reference](https://github.com/dahlbyk/posh-git)
- Add `Import-Module posh-git` to startup profile. Any maybe `$GitPromptSettings.EnableFileStatus = $false` for performance.

```powershell
# Git
Import-Module posh-git
$GitPromptSettings.EnableFileStatus = $false

# Autocomplete
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


```

```powershell
PowerShellGet\Install-Module posh-git -Scope CurrentUser -Force
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
```

