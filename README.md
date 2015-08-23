# music-scanner
A, somewhat over-complicated application for scanning a directory for 
MP3 files and allowing a user to see them. Uses bootstrap and angular 
to render the application; mongo to store it; and spring boot to tie 
it all together. I'm using spring data to access mongo, and I've used 
jms to handle the scanning jobs, but that's a little too much: I just 
wanted to play with that stuff a little.

Also, I'm using org.jaudiotagger to do the actual MP3 work.
