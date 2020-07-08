# Youtube-Dl Sample of daily used commands

`youtube-dl.exe -r 100K -o "%(title)s.%(ext)s" https://www.youtube.com/playlist?list=PLypxmOPCOkHXbJhUgjRaV2pD9MJkIArhg --restrict-filenames`

`-r 150K` = 150 Kilo Byte = speed  
`-o "%(title)s.%(ext)s"` = Format of name of the file  
`--restrict-filenames` = Keep it for using english litters only  
`-playlist-start 24` = start from track number 24  
`--format "bestvideo[ext=mp4][height=480]+bestaudio[ext=m4a]/best[ext=mp4]/best"` = choose best 480p mp4 video and m4a audio and put them in .mp4 container with ffmpeg.exe

`youtube-dl.exe https://www.youtube.com/playlist?list=PLEywN28C5m6qX1RxdGszULC3wTih78-Ku -o "%(title)s.%(ext)s"  --restrict-filenames --format "bestvideo[ext=mp4][height<=720]+bestaudio[ext=m4a]/best[ext=mp4]/best"`

`youtube-dl.exe --playlist-start 4 --playlist-end 33 -o "%(title)s.%(ext)s" UUrBeMF6dSTMBtSFJMwVoDcw --restrict-filenames`

`youtube-dl.exe -r 100K --playlist-start 3 -o "%(title)s.%(ext)s" https://www.youtube.com/user/CoronaRenderer/videos --format "bestvideo[ext=mp4]+bestaudio[ext=m4a]/best[ext=mp4]/best" --restrict-filenames`

`youtube-dl.exe -r 100K -o "%(title)s.%(ext)s" https://www.youtube.com/watch?v=t4-Xz_Lt2p4 --format "bestvideo[ext=mp4]+bestaudio[ext=m4a]/best[ext=mp4]/best" --restrict-filenames`

`youtube-dl.exe -o "%(title)s.%(ext)s" https://www.youtube.com/watch?v=9JDGmzZaMkE --format "bestvideo[ext=mp4]+bestaudio[ext=m4a]/best[ext=mp4]/best" --restrict-filenames`

`youtube-dl.exe -o "%(title)s.%(ext)s" --list-formats https://www.youtube.com/watch?v=9OhKyUDUe0I`

`youtube-dl.exe -r 100K -o "%(title)s.%(ext)s" https://www.youtube.com/watch?v=t4-Xz_Lt2p4 --format "bestaudio[ext=webm]/best" --restrict-filenames`

`youtube-dl.exe -r 100K -o "%(title)s.%(ext)s" --format "bestvideo[ext=mp4][vcodec=avc1][height<=720]+bestaudio[ext=m4a]/best[ext=mp4]/best" --restrict-filenames https://www.youtube.com/playlist?list=PL5KRlvj6EBRcB2t5uKewn6rerReDrl_sU`

`youtube-dl.exe -o "%(playlist_title)s/%(chapter_number)s - %(chapter)s/%(playlist_index)s-%(title)s.%(ext)s" --format "bestvideo[ext=mp4][vcodec=avc1][height<=720]+bestaudio[ext=m4a]/best[ext=mp4]/best" --restrict-filenames -u ahmed11150@gmail.com -p 11Ot$plur -i -c --no-warnings --no-check-certificate --console-title --batch-file="batch-file.txt" --sleep-interval 120 --add-header Referer:"https://app.pluralsight.com/library/courses/" --playlist-start 1 --all-subs`