package com.jiangsu.udpChat;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import sun.security.krb5.Config;

public class GUIChat extends Frame {

	private static final long serialVersionUID = 1L;
	private static final Color WHITE = null;
	private TextField tf;
	private Button sendBtn;
	private Button logBtn;
	private Button clearBtn;
	private Button shakeBtn;
	private TextArea viewText;
	private TextArea sendText;
	private DatagramSocket socket;
	private BufferedWriter bw;

	/**
	 * @param args
	 * 	GUI聊天
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		new GUIChat();
	}
	
	public GUIChat() throws IOException {
		init();
		southPanel();
		centerPanel();
		event();
	}
	
	/**
	 * 初始化方法
	 * @throws IOException 
	 */
	private void init() throws IOException {
		this.setLocation(500, 500);
		this.setSize(400,600);
		new Receive().start();
	    socket = new DatagramSocket();
	    bw = new BufferedWriter(new FileWriter("config.txt",true));				//需要在尾部追加而不是清空
		this.setVisible(true);
	}
	
	/**
	 * 点击事件
	 */
	public void event() {
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){ 
			    socket.close();
			    try {
					bw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});
		
		sendBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					sendAction();
				} catch (UnknownHostException | SocketException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		logBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					logFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		});
		
		clearBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				viewText.setText("");
			}
		});
	}
	
	/**
	 * 读取消息记录
	 * @throws IOException 
	 */
	private void logFile() throws IOException {
		bw.flush();							//刷新缓冲区
		 FileInputStream fis = new FileInputStream("config.txt");
		 ByteArrayOutputStream baos = new ByteArrayOutputStream();
		 int len;
		 byte[] arr = new byte[8192];
		 while((len = fis.read(arr) )!= -1){
			 baos.write(arr,0,len);
		 }
		 
		 String str = baos.toString();
		 viewText.setText(str);
		 fis.close();
	}
	
	/**
	 * 发送消息
	 * @throws IOException 
	 */
	private void sendAction() throws IOException{
		 String message = sendText.getText();
		 String ip = tf.getText();
		 DatagramPacket packet = new DatagramPacket(message.getBytes(),message.getBytes().length,InetAddress.getByName(ip),9999);
		 socket.send(packet);
		 
		 String time = getCurrentTime();
		 String str = time+" 我对"+ip+"说："+"\r\n"+message+"\r\n";
		 bw.write(str); 													//将信息写入数据库
		 viewText.append(str);
		 sendText.setText("");
	
	}
	
	private String getCurrentTime() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		return sdf.format(d);
	}

	/**
	 * 中间布局
	 */
	public void centerPanel(){
		Panel center = new Panel();            //创建中间的 Panel
		
		viewText = new TextArea(5,1);
		viewText.setEditable(false);
		viewText.setBackground(WHITE);
		viewText.setFont(new Font("xxx",Font.PLAIN,15));
		
		sendText = new TextArea(5,1);
		sendText.setFont(new Font("xxx",Font.PLAIN,15));
		
		center.setLayout(new BorderLayout());
		center.add(sendText,BorderLayout.SOUTH);
		center.add(viewText,BorderLayout.CENTER);
		
		this.add(center,BorderLayout.CENTER);
		
	}
	
	/**
	 * 底部布局
	 */
	public void southPanel(){
		Panel south = new Panel();            //创建南边的 panel:1个 textField 和4个 Button
		tf = new TextField(15);
		tf.setText("127.0.0.1");
		
		sendBtn = new Button("发送");
		logBtn = new Button("记录");
		clearBtn = new Button("清屏");
		shakeBtn = new Button("震动");
		south.add(tf);
		south.add(sendBtn);
		south.add(logBtn);
		south.add(clearBtn);
		south.add(shakeBtn);
		
		this.add(south,BorderLayout.SOUTH);
	}
	
	/**
	 * 内部接收类
	 * @author jiangSu
	 *
	 */
	private class Receive extends Thread{
		private DatagramSocket socket;

		public void run(){
			try {
				socket = new DatagramSocket(9999);
				while(true){
					DatagramPacket packet = new DatagramPacket(new byte[8192],8192);
					socket.receive(packet);
					byte[] arr = packet.getData();
					int len = packet.getLength();
					String message = new String(arr,0,len);
					String time = getCurrentTime();
					String ip = packet.getAddress().getHostAddress();
					String str = time+" "+ip+"对我说：\r\n"+message+"\r\n";
					bw.write(str); 														//信息写入数据库
					viewText.append(str);
				}	
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}

}
