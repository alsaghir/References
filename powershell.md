# PowerShell

## References

- https://docs.microsoft.com/en-us/powershell/?view=powershell-7.1

---
## Profile on startup

- [Reference](https://docs.microsoft.com/en-us/powershell/module/microsoft.powershell.core/about/about_profiles?view=powershell-7.1)
- Path is `$Home\Documents\PowerShell\Profile.ps1`

### Git

- [Reference](https://github.com/dahlbyk/posh-git)
- Add `Import-Module posh-git` to startup profile

```powershell
PowerShellGet\Install-Module posh-git -Scope CurrentUser -Force
```

---
## Tips

### Help/Info Commands

- `Get-Command`
- `Get-Help`
- `Get-Member` - helps you discover what objects, properties, and methods are available for commands. Any command that produces object-based output can be piped to Get-Member.

```powershell
Get-Help -Name Get-Help -Full
Get-Command -Noun Process
``` 