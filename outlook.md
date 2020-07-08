# Outlook

To apply specific data path for emails edit the following according to outlook version

```reg
Outlook 2013: HKEY_CURRENT_USER\Software\Microsoft\Office\15.0\Outlook
Outlook 2010: HKEY_CURRENT_USER\Software\Microsoft\Office\14.0\Outlook
Outlook 2007: HKEY_CURRENT_USER\Software\Microsoft\Office\12.0\Outlook
Outlook 2016: HKEY_CURRENT_USER\Software\Microsoft\Office\16.0\Outlook
```

`Edit > New > Expandable String Value`

```reg
Name: ForcePSTPath
Value: D:\Programs\outlook
```

```reg
Name: ForceOSTPath
Value: D:\Programs\outlook
```
