import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MyClientResult extends JFrame implements MouseListener {
	private Container c;
	private ImageIcon result, end;
	int iconnum = 1;
	String playerMyName;//ユーザの名前
	String playerIpAdress;//IPアドレス
	private JButton startbutton;//スタートボタン
	int iconNum, resultNum;//結果用


	public MyClientResult() {

		//トップページより引継ぎ
		String myName = MyClientTop.playerMyName;
		iconNum = MyClientTop.iconnum;

		//ゲームよりの引継ぎ
		int resultNum = MyClient.resultNum;
		int mynum = MyClient.mynum;
		int enenum = MyClient.enemynum;

		//ウィンドウを作成する
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ウィンドウを閉じるときに，正しく閉じるように設定する
		setTitle("MyClientResult");//ウィンドウのタイトルを設定する
		setSize(880,600);//ウィンドウのサイズを設定する
		c = getContentPane();//フレームのペインを取得する
		c.setBackground(new Color(135, 206, 250));

		//アイコンの設定
		if(resultNum == 1){
			result = new ImageIcon("win.png");
		}else if(resultNum == 2){
			result = new ImageIcon("lose.png");
		}else{
			result = new ImageIcon("draw.png");
		}
		end = new ImageIcon("end.png");

		c.setLayout(null);//自動レイアウトの設定を行わない

		//結果画像
	    JLabel theLabel = new JLabel(result);
	    c.add(theLabel);
	    theLabel.setBounds(220,30,440,200);

		//スコア表示
		String myscore = String.valueOf(mynum);
		JLabel myScore = new JLabel(myscore);
		c.add(myScore);
		myScore.setBounds(220,710,200,50);
		myScore.setFont(new Font( "メイリオ",Font.BOLD,30));
		myScore.setOpaque(true);

		String enemyscore = String.valueOf(enenum);
		JLabel enemyScore = new JLabel(enemyscore);
		c.add(enemyScore);
		enemyScore.setBounds(440,710,200,50);
		enemyScore.setFont(new Font( "メイリオ",Font.BOLD,30));
		enemyScore.setOpaque(true);


		//終了ボタン
		startbutton = new JButton(end);
		c.add(startbutton);
		startbutton.setBounds(250,400,400,100);
		startbutton.addMouseListener(this);

	}

	public static void main(String[] args) {
		MyClientResult net = new MyClientResult();
		net.setVisible(true);
	}

	public void mouseClicked(MouseEvent e) {//ボタンをクリックしたときの処理

		System.out.println("クリック");
		JButton theButton = (JButton)e.getComponent();//クリックしたオブジェクトを得る．型が違うのでキャストする

		Icon theIcon = theButton.getIcon();//theIconには，現在のボタンに設定されたアイコンが入る
		System.out.println(theIcon);//デバッグ（確認用）に，クリックしたアイコンの名前を出力する

		if(theIcon == end){//終了
			System.out.println("終わります");
			c.setVisible(false); //ウィンドウ閉じる
			dispose();
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



}



