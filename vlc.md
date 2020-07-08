# VLC

`vlc -I dummy "" :sout='#transcode{aenc=ffmpeg{strict=-2},vcodec=h264,scale=Auto,acodec=mp4a,ab=192,channels=2,samplerate=22050}:std{access=file, mux=mp4,dst=Afareet.Adly.E01.WEB-DL.720P.mp4}' vlc://quit`

`vlc -I dummy "" :sout='#transcode{aenc=ffmpeg{strict=-2},vcodec=h264,scale=Auto,acodec=mp4a,ab=192,channels=2,samplerate=22050}:std{access=file, mux=mp4,dst=Afareet.Adly.E10.WEB-DL.720P.mp4}' vlc://quit`
