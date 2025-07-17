# Windows CLI Commands

## Set system environment variable

`setx JAVA_HOME "D:\Programs\zulu11.37.17-ca-jdk11.0.6-win_x64" /M` Sets `JAVA_HOME` environment variable globally and permanently  
`setx JAVA_HOME "D:\Programs\zulu8.44.0.11-ca-jdk8.0.242-win_x64" /M` overrides the previous one

---
## Delete folder quickly

`DEL /F/Q/S *.* > NUL`

`/F` forces the deletion of read-only files.  
`/Q` enables quiet mode. You are not ask if it is ok to delete files (if you don't use this, you are asked for any file in the folder).  
`/S` runs the command on all files in any folder under the selected structure.  
`*.*` delete all files.  
`> NUL` disables console output. This improves the process further, shaving off about one quarter of the processing time off of the console command.

`RMDIR /Q/S foldername`

`/Q` Quiet mode, won't prompt for confirmation to delete folders.  
`/S` Run the operation on all folders of the selected path.  
`foldername` The absolute path or relative folder name, e.g. o:/backup/test1 or test1

---
## PowerShell

In `CMDer` for example use the following

PowerShell -ExecutionPolicy Bypass -NoLogo -NoProfile -NoExit -Command "Invoke-Expression 'Import-Module ''D:\Programs\PowerShell\profile.ps1'''"

`profile.ps1` content as following

```powershell
Set-PSReadlineKeyHandler -Key Tab -Function Complete
D:
cd D:\Programs
```

---
## CMD Run on Startup

```powershell
# https://stackoverflow.com/questions/17404165/how-to-run-a-command-on-command-prompt-startup-in-windows/17405182
reg add "HKCU\Software\Microsoft\Command Processor" /v AutoRun /t REG_EXPAND_SZ /d "%"USERPROFILE"%\init.cmd" /f

# Then create a file init.cmd in your profile folder and add whatever in it

# To remove these changes, delete the registry key:
reg delete "HKCU\Software\Microsoft\Command Processor" /v AutoRun
```

Example of `init.cmd`

```cmd
@echo off

:: Command aliases
DOSKEY alias=code %USERPROFILE%\Dropbox\alias.cmd
DOSKEY msys=D:\Apps\msys64\msys2_shell.cmd -defterm -here -no-start -msys2

:: Env variables
SET PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%

@echo on
```

## OBS

- Use `Alt` and drag the frames to cut instead of scaling the screen.

## Davinci Resolve

- `Shift` + `J/K/L` for fast playback speeding and slowing. `J/K/L` alone for normal playback and fast speeding without slowing.
- `Ctrl` + `B` Split selected tracks.
- `Backspace` Delete without ripple effect. `Shift` + `Backspace` for ripple delete. Also `Delete` button for ripple delete.
- `Ctrl` + `R` Enable Retime controls to change speed by dragging.
- In Fusion tab, select line between two nodes and press `Shift` + `Space` to add an effect. Right click on the node (usually the shape) and edit splines to control the keyframe markers on the timeline. Add mask (shape) to the effect so only an area is effected. Result nodes would be something like `MediaIn` -> `Blur` (`Rectangle` above the effect here) -> `MediaOut`.
- In Fusion tab, `Merge` node input are foreground and background. For example, `Rectangle` -> `Background` -> `Merge` should be the foreground while `MediaIn` -> `Merge` should be the background.

## Firefox

- After update using scoop, set default profile and delete others

```sh
$defaultProfile = "$env:USERPROFILE\scoop\persist\firefox\profile"
$content = @"
[General]
StartWithLastProfile=1
Version=2

[Profile0]
Name=Scoop
IsRelative=0
Path=$defaultProfile
Default=1

[Install9261CA6489C7D24]
Default=$defaultProfile
Locked=1
"@

$content | Set-Content "$env:APPDATA\Mozilla\Firefox\profiles.ini"

$content = @"
[9261CA6489C7D24]
Default=$defaultProfile
Locked=1
"@

$content | Set-Content "$env:APPDATA\Mozilla\Firefox\installs.ini"

$profilesDir = "$env:APPDATA\Mozilla\Firefox\Profiles"

Get-ChildItem -Path $profilesDir | Remove-Item -Recurse -Force -Confirm:$false
```
