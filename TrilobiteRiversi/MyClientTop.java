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
import javax.swing.JOptionPane;

public class MyClientTop extends JFrame implements MouseListener {
	private Container c;
	private ImageIcon top, start;
	private ImageIcon icon1, icon2;
	static int iconnum = 1;
	static String playerMyName;//ユーザの名前
	static String playerIpAdress;
	private JButton startbutton;//スタートボタン
	private JButton icon01, icon02;//player


	public MyClientTop() {
		//名前の入力ダイアログを開く
		playerMyName = JOptionPane.showInputDialog(null,"名前を入力してください","名前の入力",JOptionPane.QUESTION_MESSAGE);
		if(playerMyName.equals("")){
			playerMyName = "No name";//名前がないときは，"No name"とする
		}
		//IPアドレスの入力
		playerIpAdress = JOptionPane.showInputDialog(null,"IPアドレスを入力してください","IPアドレスの入力",JOptionPane.QUESTION_MESSAGE);
		if(playerIpAdress.equals("")){
		playerIpAdress = "localhost";
		}



		//ウィンドウを作成する
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ウィンドウを閉じるときに，正しく閉じるように設定する
		setTitle("MyClientTop");//ウィンドウのタイトルを設定する
		setSize(880,600);//ウィンドウのサイズを設定する
		c = getContentPane();//フレームのペインを取得する
		c.setBackground(new Color(135, 206, 250));

		//アイコンの設定
		top = new ImageIcon("title.png");
		start = new ImageIcon("start.png");
		icon1 = new ImageIcon("fishicon1.png");
		icon2 = new ImageIcon("fishicon2.png");

		c.setLayout(null);//自動レイアウトの設定を行わない


		//トップ画像
        JLabel theLabel = new JLabel(top);
        c.add(theLabel);
        theLabel.setBounds(220,30,440,200);

		//名前
		JLabel nameLabel = new JLabel(playerMyName);//名前用ラベル
		c.add(nameLabel);
		nameLabel.setBounds(330,230,200,50);
		nameLabel.setFont(new Font( "メイリオ",Font.BOLD,30));
		nameLabel.setForeground(Color.BLACK);
		nameLabel.setBackground(new Color(135, 206, 250));
		nameLabel.setOpaque(true);

		//プレイヤー選択
		icon01 = new JButton(icon1);
		c.add(icon01);
		icon01.setBounds(330,290,100,100);
		icon01.addMouseListener(this);

		icon02 = new JButton(icon2);
		c.add(icon02);
		icon02.setBounds(450,290,100,100);
		icon02.addMouseListener(this);

		//スタートボタン
		startbutton = new JButton(start);
		c.add(startbutton);
		startbutton.setBounds(250,400,400,100);
		startbutton.addMouseListener(this);

	}

	public static void main(String[] args) {
		MyClientTop net = new MyClientTop();
		net.setVisible(true);
	}

	public void mouseClicked(MouseEvent e) {//ボタンをクリックしたときの処理

	System.out.println("クリック");
	JButton theButton = (JButton)e.getComponent();//クリックしたオブジェクトを得る．型が違うのでキャストする

	Icon theIcon = theButton.getIcon();//theIconには，現在のボタンに設定されたアイコンが入る
	System.out.println(theIcon);//デバッグ（確認用）に，クリックしたアイコンの名前を出力する

	if(theIcon == start){
		MyClient mc = new MyClient();//MyClientを起動
		System.out.println("はじめます");
		mc.setVisible(true);//MyClientを見せる
		c.setVisible(false); //ウィンドウ閉じる
		dispose();
	}else if(theIcon == icon1){//プレイヤーアイコンの設定
		iconnum = 1;
		System.out.println(iconnum);

	}else if(theIcon == icon2){//プレイヤーアイコンの設定
		iconnum = 2;
		System.out.println(iconnum);

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


