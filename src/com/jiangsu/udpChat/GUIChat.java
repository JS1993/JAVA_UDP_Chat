package com.jiangsu.udpChat;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUIChat extends Frame {

	private static final long serialVersionUID = 1L;
	private static final Color WHITE = null;
	private TextField tf;
	private Button sendBtn;
	private Button logBtn;
	private Button clearBtn;
	private Button shakeBtn;

	/**
	 * @param args
	 * 	GUI聊天
	 */
	public static void main(String[] args) {
		new GUIChat();
	}
	
	public GUIChat() {
		init();
		southPanel();
		centerPanel();
		event();
	}
	
	/**
	 * 点击事件
	 */
	public void event() {
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
	}
	
	public void centerPanel(){
		Panel center = new Panel();            //创建中间的 Panel
		
		TextArea viewText = new TextArea(5,1);
		viewText.setEditable(false);
		viewText.setBackground(WHITE);
		viewText.setFont(new Font("xxx",Font.PLAIN,15));
		
		TextArea sendText = new TextArea(5,1);
		sendText.setFont(new Font("xxx",Font.PLAIN,15));
		
		center.setLayout(new BorderLayout());
		center.add(sendText,BorderLayout.SOUTH);
		center.add(viewText,BorderLayout.CENTER);
		
		this.add(center,BorderLayout.CENTER);
		
	}
	
	public void southPanel(){
		Panel south = new Panel();            //创建南边的 panel:1个 textField 和4个 Button
		tf = new TextField(15);
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

	private void init() {
		this.setLocation(500, 100);
		this.setSize(400,600);
		this.setVisible(true);
	}

}
