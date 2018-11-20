cd "C:/Users/Kevin/repos/conquest-engine/"
dir /b /s *.java>sources.txt
md classes
javac -d classes @sources.txt
del sources.txt
cd classes
java main.RunGame 0 0 0 "java bot.BotStarter" "java bot.BotStarter" 2>err.txt 1>out.txt