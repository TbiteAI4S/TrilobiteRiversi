# Trilobite Reversi(JavaReversi)
  
 Trilobite Reversihaは，Javaで制作した対戦型リバーシです．

 ***
 Trilobite Reversiha is a competitive Reversi game created in Java.
 ***

 ![タイトル](TrilobiteRiversi/title.png) 


# IMAGES
 <img src="img/top.PNG" width=50%>

 <img src="img/game.PNG" width=50%>


# Features

 Trilobite(三葉虫)がコマのリバーシです．基本的なルールは通常のリバーシと同じですが，4つの特別マスを取ると2倍の得点が入ります．

 ***
 Trilobite is the frame of this game. The basic rules are the same as in regular Reversi, but you can double your score by capturing the four special squares.
 ***

  
# Requirement
 
 Eclipseで実行することを推奨します．
 Eclipseの最新バージョン(2021)での動作を確認しました．

 ***
 It is recommended to run the program in Eclipse.
 We have confirmed that it works with the latest version of Eclipse (2021).
 ***
 
 
# Usage
 
 EclipseのワークスペースにTrilobiteReversiを追加して，起動してください．

 起動後は念のためすべてのjavaファイルをコンパイルしてください．

 コンパイル後，始めに"MyServer2.java"を実行してください．実行するとサーバが起動します．

 ***
 Add TrilobiteReversi to the Eclipse workspace and launch it.
 
 After starting, compile all java files to be sure.

 After compiling, run "MyServer2.java" first. When executed, the server will start.
 ***

 <img src="img/goServer.PNG" width=50%>

 ***
 起動後に，"MyClientTop.java"を実行し，名前とipアドレスを入力してください．(他端末と通信して対戦しない場合はipアドレスは入力しないでください).

 ***
 After startup, run "MyClientTop.java" and enter your name and ip address. (If you do not want to play against other terminals, do not enter the ip address.)
 ***

 ![名前入力](img/inputname.PNG)

 ![ip入力](img/inputip.PNG)

 タイトル画面が表示されますので，STARTを押してプレイしてください．
 ***
 When the title screen appears, press START to start playing.
 ***
 
# Note
 
 ゲームのデータはTrilobiteRiversiという名のフォルダにすべて入っています．

 このゲームは"MyClientTop.java"，"MyClient.java"，"MyClientResult.java"の順に動きます．必ず"MyClientTop.java"をサーバを起動した後に実行してください．

 ウィンドウの生成は左上で固定されているため，2つ以上のMyClientTopを生成した場合，
 ウィンドウを切り替えを行った場合重なってウィンドウの切り替えが行われます．

 同じPC上でプレイする場合は，"MyClientTop.java"を2度実行してください．

 javaファイルはShift-JISで書かれています．文字化けした場合は文字コードShift-JISにして読み込んでください．
***
 The game data is all contained in a folder named TrilobiteRiversi.

 The game runs in the following order: "MyClientTop.java", "MyClient.java", and "MyClientResult.java". Be sure to run "MyClientTop.java" after starting the server.

 Window creation is fixed at the upper left, so if two or more MyClientTops are created, they will overlap when switching windows.
 If more than one MyClientTop is created, they will overlap when switching windows.

 If you want to play the game on the same PC, run "MyClientTop.java" twice.

 The java file is written in Shift-JIS. If you encounter garbled characters, change the character encoding to Shift-JIS and load the file.
 ***
