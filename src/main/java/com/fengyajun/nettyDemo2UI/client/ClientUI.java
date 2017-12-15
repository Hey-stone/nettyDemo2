package com.fengyajun.nettyDemo2UI.client;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextField;

import javax.swing.JTextArea;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.SwingConstants;

/**
 * @Title: ClientUI.java
 * @Package com.fengyajun.nettyDemo3UI.server
 * @author 冯亚军
 * @date 2017年12月14日下午4:05:59
 * @Description: TODO(用一句话描述该文件做什么)
 * @version V1.0
 */
public class ClientUI implements ClientObserver {
	
	private JFrame frame;
	private JTextField host;
	private JTextField port;
	private JTextField userName;
	private JTextField msg;
	private JTextArea textArea;
	private static JScrollBar scrollBar;

	private NettyClientDemo3 client;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientUI window = new ClientUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientUI() {

		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 539, 354);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(10, 10, 529, 245);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("服务器IP:");
		lblNewLabel.setBounds(10, 10, 66, 22);
		panel.add(lblNewLabel);

		host = new JTextField();
		host.setBounds(86, 11, 111, 21);
		panel.add(host);
		host.setColumns(10);

		JLabel label = new JLabel("端口号:");
		label.setBounds(207, 14, 47, 15);
		panel.add(label);

		port = new JTextField();
		port.setColumns(10);
		port.setBounds(250, 11, 47, 21);
		panel.add(port);

		JLabel label_1 = new JLabel("用户名:");
		label_1.setBounds(308, 14, 47, 15);
		panel.add(label_1);

		userName = new JTextField();
		userName.setBounds(350, 11, 81, 21);
		panel.add(userName);
		userName.setColumns(10);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 68, 486, 177);
		panel.add(scrollPane);

		textArea = new JTextArea();
		textArea.setWrapStyleWord(true); // 换行方式：不分割单词
		textArea.setLineWrap(true); // 自动换行
		scrollPane.setViewportView(textArea);
		scrollBar = scrollPane.getVerticalScrollBar();

		JLabel lblNewLabel_1 = new JLabel("消息列表");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel_1.setFont(new Font("宋体", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(10, 31, 486, 34);
		panel.add(lblNewLabel_1);

		JButton btnNewButton = new JButton("连接");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				detailCon(btnNewButton);

			}
		});

		userName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// 判断按下的键是否是回车键
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					detailCon(btnNewButton);
				}
			}
		});

		btnNewButton.setBounds(437, 10, 66, 23);
		panel.add(btnNewButton);

		msg = new JTextField();
		msg.setBounds(20, 265, 389, 41);
		
//		msg.addFocusListener(new MyFocusListener("Please enter valid text", msg));//添加焦点事件反映

		msg.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// 判断按下的键是否是回车键
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					sendMsg();
				}
			}
		});

		frame.getContentPane().add(msg);
		msg.setColumns(10);

		JButton jbtn = new JButton("发送");
		jbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				sendMsg();

			}
		});
		jbtn.setBounds(419, 265, 93, 41);
		frame.getContentPane().add(jbtn);
	}

	/** 
	* @Title: sendMsg 
	* @Description: <这里用一句话描述这个方法的作用>
	* @return void    返回类型 
	*/
	protected void sendMsg() {
		
		if (msg.getText().trim().equals("")) {
			//msg.setText("Please enter valid text");
			return;
		}
		
		client.sendMsg(msg.getText());
		msg.setText("");
		
	}

	/**
	 * @param btnNewButton
	 * @Title: detailCon
	 * @Description: <这里用一句话描述这个方法的作用>
	 * @return void 返回类型
	 */
	protected void detailCon(JButton btnNewButton) {

		// 点击之后不可点击
		btnNewButton.setEnabled(false);
		btnNewButton.setBackground(Color.gray);

		if (client == null)
			client = new NettyClientDemo3(userName.getText().trim());

		String serverIP = host.getText();
		String serverPort = port.getText();
		Integer port = 8080;
		if (serverIP.isEmpty() || serverPort.isEmpty()) {
			textArea.setText(host.getText());
			textArea.setText("The IP or Port is Empty");
		} else {
			port = Integer.valueOf(serverPort);
		}
		try {
			run();
			client.connServer(serverIP, port);
		} catch (NullPointerException e1) {
			e1.printStackTrace();
			textArea.setText("Connect Server Error");
		} catch (Exception e2) {
			e2.printStackTrace();
			textArea.setText("Invalid Port");
		}

	}

	public void run() {
		SimpleChatClientHandler.register(this);
		try {
			client.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 每次需要在JTextArea增加String时,调用如下方法
	private void refreshTextArea(String text) {
		textArea.setText(text);
		// 在更新logArea后，稍稍延时，否则getMaximum（）获得的数据可能不是最后的最大值，无法滚动到最后一行
		try {
			Thread.sleep(100);
		} catch (InterruptedException ex) {
			// Logger.getLogger(TestServer.class.getName()).log(Level.SEVERE,
			// null, ex);
		}
		scrollBar.setValue(scrollBar.getMaximum());
	}

	@Override
	public void update() {
		// 设置光标到JTextArea中的最后一个字符就行了 不靠谱
		// textArea.setCaretPosition(textArea.getText().length());

		refreshTextArea(SimpleChatClientHandler.getMessages());
	}
}

class MyFocusListener implements FocusListener {
    String info;
    JTextField jtf;
    public MyFocusListener(String info, JTextField jtf) {
        this.info = info;
        this.jtf = jtf;
    }
    @Override
    public void focusGained(FocusEvent e) {//获得焦点的时候,清空提示文字
        String temp = jtf.getText();
        if(temp.equals(info)){
            jtf.setText("");
        }
    }
    @Override
    public void focusLost(FocusEvent e) {//失去焦点的时候,判断如果为空,就显示提示文字
        String temp = jtf.getText();
        if(temp.equals("")){
            jtf.setText(info);
        }
    }
}
