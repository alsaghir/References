# Windows CLI Commands

## Set system environment variable

`setx JAVA_HOME "D:\Programs\zulu11.37.17-ca-jdk11.0.6-win_x64" /M` Sets `JAVA_HOME` environment variable globally and permenentaly  
`setx JAVA_HOME "D:\Programs\zulu8.44.0.11-ca-jdk8.0.242-win_x64" /M` overrides the previous one

---

## Delete folder quicly

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
