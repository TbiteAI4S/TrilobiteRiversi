import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MyClient extends JFrame implements MouseListener,MouseMotionListener {
	private JButton buttonArray[][];//ボタン用の配列
	private JButton passbutton;//パスボタン
	private Container c;
	private ImageIcon boardIcon, bigIcon;//場面
	private ImageIcon greenIcon, purpleIcon, mybigIcon, enebigIcon, bigG, bigP;//プレイヤーのコマ
	PrintWriter out;//出力用のライター
	private int myColor;
	private int myTurn;
	private int mypasscount, enepasscount;//パスのカウント
	private int boardnum=60; //以下勝敗判定
	static int mynum=2;
	static int enemynum=2;
	private int game=0;      //以上勝敗判定
	private ImageIcon myIcon, yourIcon, passIcon, faceIcon;
	private int temp;//盤面の配列とその配列から割り出すx,yの値
	int iconNum;
	static int resultNum;//結果をMyClientResultに送る
	private ImageIcon firstIcon, secondIcon;//先攻後攻icon
	private ImageIcon turnicon;//先攻後攻設定icon
	String myNum;//スコア表示


	public MyClient() {
		//トップページより引継ぎ
		String myName = MyClientTop.playerMyName;
		String playerIpAdress = MyClientTop.playerIpAdress;
		iconNum = MyClientTop.iconnum;


		//ウィンドウを作成する
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ウィンドウを閉じるときに，正しく閉じるように設定する
		setTitle("MyClient");//ウィンドウのタイトルを設定する
		setSize(880,600);//ウィンドウのサイズを設定する
		c = getContentPane();//フレームのペインを取得する
		c.setBackground(new Color(135, 206, 250));



		//アイコンの設定
		greenIcon = new ImageIcon("GreenTrilobite.jpg");//緑のコマ
		purpleIcon = new ImageIcon("PurpleTrilobite.jpg");//紫のコマ
		boardIcon = new ImageIcon("SeafllorGame.jpg");//コマを置く場所
		bigIcon = new ImageIcon("EdiacaraFauna.jpg");//特別なコマを置く場所
		bigG = new ImageIcon("GreenTrilobite_S.jpg");//特別な緑のコマ
		bigP = new ImageIcon("PurpleTrilobite_S.jpg");//特別な紫のコマ

		passIcon = new ImageIcon("pass.jpg");
		firstIcon = new ImageIcon("first.png");//先攻
		secondIcon = new ImageIcon("second.png");//後攻

		//自分のキャラ
		if(iconNum == 1){
			faceIcon = new ImageIcon("fishicon1.png");
		}else if(iconNum == 2){
			faceIcon = new ImageIcon("fishicon2.png");
		}

		c.setLayout(null);//自動レイアウトの設定を行わない

		//ボタンの生成
		buttonArray = new JButton[8][8];//ボタンの配列を５個作成する[0]から[7]まで使える
		for(int j=0;j<8;j++){//たて
			for(int i=0;i<8;i++){//よこ
				buttonArray[j][i]= new JButton(boardIcon);//ボタンにアイコンを設定する
				c.add(buttonArray[j][i]);//ペインに貼り付ける
				buttonArray[j][i].setBounds(50+i*45,90+j*45,45,45);//ボタンの大きさと位置を設定する．(x座標，y座標,xの幅,yの幅）
				buttonArray[j][i].addMouseListener(this);//ボタンをマウスでさわったときに反応するようにする
				buttonArray[j][i].addMouseMotionListener(this);//ボタンをマウスで動かそうとしたときに反応するようにする
				buttonArray[j][i].setActionCommand(Integer.toString((j*8)+(i)));//ボタンに配列の情報を付加する（ネットワークを介してオブジェクトを識別するため）
			}
		}

		//パスのボタン
		passbutton = new JButton(passIcon);
		c.add(passbutton);
		passbutton.setBounds(500,250,200,50);
		passbutton.addMouseListener(this);

		//名前を表示
		JLabel nemeLabel = new JLabel(myName);//名前用ラベル
		c.add(nemeLabel);
		nemeLabel.setBounds(500,50,200,50);
		nemeLabel.setForeground(Color.BLACK);
		nemeLabel.setFont(new Font( "メイリオ",Font.BOLD,30));
		nemeLabel.setBackground(new Color(135, 206, 250));
		nemeLabel.setOpaque(true);

		//自分のアイコンを表示
		JLabel myfaceLabel = new JLabel(faceIcon);//アイコン生成
		c.add(myfaceLabel);
		myfaceLabel.setBounds(500,150,100,100);
		myfaceLabel.setOpaque(true);

		//サーバに接続する
		Socket socket = null;
		try {
			//"localhost"は，自分内部への接続．localhostを接続先のIP Addressに設定すると他のPCのサーバと通信できる
			//10000はポート番号．IP Addressで接続するPCを決めて，ポート番号でそのPC上動作するプログラムを特定する
			socket = new Socket(playerIpAdress, 10000);
		} catch (UnknownHostException e) {
			System.err.println("ホストの IP アドレスが判定できません: " + e);
		} catch (IOException e) {
			 System.err.println("エラーが発生しました: " + e);
		}

		MesgRecvThread mrt = new MesgRecvThread(socket, myName);//受信用のスレッドを作成する
		mrt.start();//スレッドを動かす（Runが動く）
	}

	//メッセージ受信のためのスレッド
	public class MesgRecvThread extends Thread {

		Socket socket;
		String myName;

		public MesgRecvThread(Socket s, String n){
			socket = s;
			myName = n;
		}

		//通信状況を監視し，受信データによって動作する
		public void run() {
			try{
				InputStreamReader sisr = new InputStreamReader(socket.getInputStream());
				BufferedReader br = new BufferedReader(sisr);
				out = new PrintWriter(socket.getOutputStream(), true);
				out.println(myName);//接続の最初に名前を送る
				String myNumberStr = br.readLine();
				int myNumber = Integer.parseInt(myNumberStr);
				if(myNumber % 2 == 0){
					myColor = 0;
				}else{
					myColor = 1;
				}

				//先攻後攻やコマの色を決める
				if(myColor == 0){
					myIcon = purpleIcon;
					mybigIcon = bigP;
					enebigIcon = bigG;
					yourIcon = greenIcon;
					myTurn = 0;
					mypasscount = 0;
					enepasscount = 0;
					turnicon = firstIcon;
				}else{
					myIcon = greenIcon;
					yourIcon = purpleIcon;
					mybigIcon = bigG;
					enebigIcon = bigP;
					myTurn = 1;
					mypasscount = 0;
					enepasscount = 0;
					turnicon = secondIcon;
				}

				//先攻後攻
				JLabel turnLabel = new JLabel(turnicon);//アイコン生成
				c.add(turnLabel);
				turnLabel.setBounds(500,100,200,50);
				turnLabel.setOpaque(true);


				//初期コマの配置
				buttonArray[3][3].setIcon(greenIcon);
				buttonArray[3][4].setIcon(purpleIcon);
				buttonArray[4][4].setIcon(greenIcon);
				buttonArray[4][3].setIcon(purpleIcon);

				//ボーナスコマの設置
				buttonArray[2][2].setIcon(bigIcon);
				buttonArray[2][5].setIcon(bigIcon);
				buttonArray[6][5].setIcon(bigIcon);
				buttonArray[6][2].setIcon(bigIcon);

				while(true) {
					String inputLine = br.readLine();//データを一行分だけ読み込んでみる
					if (inputLine != null) {//読み込んだときにデータが読み込まれたかどうかをチェックする
						System.out.println(inputLine);//デバッグ（動作確認用）にコンソールに出力する
						String[] inputTokens = inputLine.split(" ");	//入力データを解析するために、スペースで切り分ける
						String cmd = inputTokens[0];//コマンドの取り出し．１つ目の要素を取り出す
						if(cmd.equals("PLACE")){//cmdの文字と"PLACE"が同じか調べる．同じ時にtrueとなる
							//PLACEの時の処理
							String theBName = inputTokens[1];//ボタンの名前（番号）の取得
							int theBnum = Integer.parseInt(theBName);//ボタンの名前を数値に変換する
							int y = theBnum / 8;
							int x = theBnum % 8;
							int theColor = Integer.parseInt(inputTokens[2]);
							System.out.println("myTurn:"+myTurn);
							System.out.println("myColor:"+myColor);
							if(theColor == myColor){
								//送信元クライアントでの処理
								if(theBnum == 18||theBnum == 21||theBnum == 50||theBnum == 53){
									buttonArray[y][x].setIcon(mybigIcon);
									mynum = mynum + 5;
								}else{
									buttonArray[y][x].setIcon(myIcon);
									mynum = mynum + 1;
								}
								boardnum = boardnum - 1;
								mypasscount = 0;
								System.out.println("passcount:"+mypasscount);
							}else{
								//送信先クライアントでの処理
								if(theBnum == 18||theBnum == 21||theBnum == 50||theBnum == 53){
									buttonArray[y][x].setIcon(enebigIcon);
									enemynum = enemynum + 5;
								}else{
									buttonArray[y][x].setIcon(yourIcon);
									enemynum = enemynum + 1;
								}
								boardnum = boardnum - 1;
								enepasscount = 0;
							}
							myTurn = 1 - myTurn;

							//終了判定
							if(boardnum==0 || mypasscount==2 || enepasscount==2|| mynum==0 || enemynum==0){
								if(mynum > enemynum){
									System.out.println("自分の勝ち");

									//勝利番号
									resultNum = 1;
									resultpage();//結果画面へ
								}else if(mynum < enemynum){
									System.out.println("相手の勝ち");

									//敗北番号
									resultNum = 2;
									resultpage();
								}else if(mynum == enemynum){
									System.out.println("引き分け");

									//引き分け番号
									resultNum = 3;
									resultpage();
								}
								game = game + 1;
							}
						}else if(cmd.equals("FLIP")){//cmdにFLIPが同じか調べる．同じ時にtrueとなる
							//FLIPの時の処理
							String theBName = inputTokens[1];//ボタンの名前（番号）の取得
							int theBnum = Integer.parseInt(theBName);//ボタンの名前を数値に変換する
							int y = theBnum / 8;
							int x = theBnum % 8;
							int theColor = Integer.parseInt(inputTokens[2]);
							if(theColor == myColor){
								//送信元クライアントでの処理
								if(theBnum == 18||theBnum == 21||theBnum == 50||theBnum == 53){
									buttonArray[y][x].setIcon(mybigIcon);
									mynum = mynum + 5;
									enemynum = enemynum - 1;
								}else{
									buttonArray[y][x].setIcon(myIcon);
									mynum = mynum + 1;
									enemynum = enemynum - 1;
								}
							}else{
								//送信先クライアントでの処理
								if(theBnum == 18||theBnum == 21||theBnum == 50||theBnum == 53){
									buttonArray[y][x].setIcon(enebigIcon);
									mynum = mynum - 1;
									enemynum = enemynum + 5;
								}else{
									buttonArray[y][x].setIcon(yourIcon);
									mynum = mynum - 1;
									enemynum = enemynum + 1;
								}
							}
						}else if(cmd.equals("PASS")){
							//PASSの時の処理
							if(myTurn==0){
								mypasscount = mypasscount + 1;
							}else{
								enepasscount = enepasscount + 1;
							}
							myTurn = 1 - myTurn;
							//終了判定
							if(boardnum==0 || mypasscount==2 || enepasscount==2|| mynum==0 || enemynum==0){
								if(mynum > enemynum){
									System.out.println("自分の勝ち");
									resultNum = 1;
									resultpage();
								}else if(mynum < enemynum){
									System.out.println("相手の勝ち");
									resultNum = 2;
									resultpage();
								}else if(mynum == enemynum){
									System.out.println("引き分け");
									resultNum = 3;
									resultpage();
								}
								game = game + 1;
							}
						}else {
							if(myTurn == 0) {
								//自動パスを行う
								autpass();
							}
						}

						repaint();


					}else{
						break;
					}

				}
				socket.close();
			} catch (IOException e) {
				System.err.println("エラーが発生しました: " + e);
			}
		}
	}

	public static void main(String[] args) {
		MyClient net = new MyClient();
		net.setVisible(true);
	}

	public void mouseClicked(MouseEvent e) {//ボタンをクリックしたときの処理
		int x,y;

		System.out.println("クリック");
		JButton theButton = (JButton)e.getComponent();//クリックしたオブジェクトを得る．型が違うのでキャストする
		String theArrayIndex = theButton.getActionCommand();//ボタンの配列の番号を取り出す

		Icon theIcon = theButton.getIcon();//theIconには，現在のボタンに設定されたアイコンが入る
		System.out.println("クリックしたアイコン"+theIcon);//デバッグ（確認用）に，クリックしたアイコンの名前を出力する

		if(game == 0){
			if(theIcon == boardIcon || theIcon == bigIcon){
				temp = Integer.parseInt(theArrayIndex);
					y = temp / 8;
					x = temp % 8;
					System.out.println("x="+x+",y="+y);
				if(myTurn==0){
					//送信情報を作成する（受信時には，この送った順番にデータを取り出す．スペースがデータの区切りとなる）
					String msg = "PLACE"+" "+theArrayIndex+" "+myColor;
					if(judgeButton(y,x)){
						//置ける
						//サーバに情報を送る
						out.println(msg);//送信データをバッファに書き出す
						out.flush();//送信データをフラッシュ（ネットワーク上にはき出す）する
						repaint();//画面のオブジェクトを描画し直す
					}else{
						//置けない
						System.out.println("そこには置けません");
					}
				}

			}else if(theIcon == passIcon){
				if(myTurn==0){
				String msg = "PASS"+" "+1414/*msgをPLACEと同じにするための意味のない数値*/+" "+myColor;
				out.println(msg);
				out.flush();
				System.out.println("パスする");
				}

			}
		}
	}

	public void mouseEntered(MouseEvent e) {//マウスがオブジェクトに入ったときの処理

	}

	public void mouseExited(MouseEvent e) {//マウスがオブジェクトから出たときの処理

	}

	public void mousePressed(MouseEvent e) {//マウスでオブジェクトを押したときの処理（クリックとの違いに注意）

	}

	public void mouseReleased(MouseEvent e) {//マウスで押していたオブジェクトを離したときの処理

	}

	public void mouseDragged(MouseEvent e) {//マウスでオブジェクトとをドラッグしているときの処理

	}

	public void mouseMoved(MouseEvent e) {//マウスがオブジェクト上で移動したときの処理

	}

	public boolean judgeButton(int y, int x){//置けるか置けないかの判断
		boolean flag = false;
		for(int j=-1; j<2; j++){
			for(int i=-1; i<2; i++){
				Icon judge;
				if(flipButtons(y,x,j,i)>=1){
					judge=buttonArray[y+j][x+i].getIcon();
					System.out.println("judge="+judge);
					flag = true;
				}
			}
		}
		return flag;
	}

	public int flipButtons(int y,int x,int j,int i){//ひっくり返せるかの判断
		int flipNum=0;
		for(int dy=j,dx=i; ; dy+=j, dx+=i){
			if(y+dy<0 || y+dy>7 || x+dx<0 || x+dx>7){
				return 0;
			}else{
				Icon flipIcon = buttonArray[y+dy][x+dx].getIcon();
				if(flipIcon == boardIcon){
					return 0;
				}else if(flipIcon == myIcon){
					break;
				}else if(flipIcon == yourIcon){
					flipNum = flipNum + 1;
				}else if(flipIcon == enebigIcon){
					flipNum = flipNum + 1;
				}else if(flipIcon == bigIcon){
					return 0;
				}else if(flipIcon == mybigIcon){
					break;
				}
			}

		}

		for(int dy=j, dx=i, k=0; k<flipNum; k++, dy+=j, dx+=i){
				//ボタンの位置情報を作る
					int msgy = y + dy;
					int msgx = x + dx;
					int theArrayIndex = msgy*8 + msgx;
					System.out.println("theArrayIndex="+theArrayIndex);

					//サーバに情報を送る
					String msg = "FLIP"+" "+theArrayIndex+" "+myColor;
					out.println(msg);
					out.flush();

		}
		return flipNum;
	}


	private void autpass() {//自動パス
		if(myTurn == 0) {
			int autpass = 0;//0ならパスを送る
			for(int j=0;j<8;j++){//たて
				for(int i=0;i<8;i++){//よこ
					if(judgePass(j,i)){
						autpass = 1;//パスを送らない
					}else{

					}
				}
			}//判定終了
			if(autpass == 0) {//パスを送る
				String msg = "PASS"+" "+1414/*msgをPLACEと同じにするための意味のない数値*/+" "+myColor;
				out.println(msg);
				out.flush();
			}
		}

	}

	//autpass()のfor内のj,iをもらい、その場所でコマが置けるか置けないかの判定し置けるならtrueを返す
	public boolean judgePass(int j, int i){
		boolean pass = false;//falseなら置けない
		for(int m=-1; m<2; m++){//たて
			for(int n=-1; n<2; n++){//よこ
				Icon judge;
				if(flipButtons(j,i,m,n)>=1){
					judge=buttonArray[j+m][i+n].getIcon();
					System.out.println("judge="+judge);
					pass = true;
				}
			}
		}
		return pass;
	}

	//autpass()とjudgePassのj,i,m,nをもらい各場所でコマを置いた場合、
	//周りやその先にひっくり返すことのができるコマの有無
	//ひっくり返すコマがあるなら1以上、ないなら0を返す
	public int passcheck(int j,int i,int m,int n){
		int passNum=0;
		for(int dj=m,di=n; ; dj+=m, di+=n){
			if(j+dj<0 || j+dj>7 || i+di<0 || i+di>7){
				return 0;
			}else{
				Icon flipIcon = buttonArray[j+dj][i+di].getIcon();
				if(flipIcon == boardIcon){
					return 0;
				}else if(flipIcon == myIcon){
					break;
				}else if(flipIcon == yourIcon){
					passNum = passNum + 1;
				}else if(flipIcon == enebigIcon){
					passNum = passNum + 1;
				}else if(flipIcon == bigIcon){
					return 0;
				}else if(flipIcon == mybigIcon){
					break;
				}
			}

		}
		return passNum;
	}

	public void resultpage(){
		//結果画面
	MyClientResult re = new MyClientResult();//MyClientResultを起動
	System.out.println("結果画面へ");
	re.setVisible(true);//MyClientResultを見せる
	c.setVisible(false); //ウィンドウ閉じる
	dispose();
	}

}




