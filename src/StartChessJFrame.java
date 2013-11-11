import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

/**
 *五子棋主框架类，程序启动类
 */
public class StartChessJFrame extends JFrame {
	/**
	 * 构造方法StartChessJFrame(),创建整体框架、按钮与菜单。
	 * 监听器MyItemListener(ActionEvent e)，监听按钮
	 * 主方法main(String[] args)
	 */
	private static final long serialVersionUID = 1L;

	private ChessBoard chessBoard;// 五子棋--棋盘类
	private JPanel toolbar;// 一般轻量级容器
	private JButton startButton, backButton, exitButton;// 按钮的实现，通过 Action 可配置按钮，并进行一定程度的控制

	private JMenuBar menuBar;// 菜单栏的实现，将 JMenu 对象添加到菜单栏以构造菜单
	private JMenu sysMenu;// 菜单的该实现是一个包含 JMenuItem 的弹出窗口，用户选择 JMenuBar 上的项时会显示该 JMenuItem
	private JMenuItem startMenuItem, exitMenuItem, backMenuItem;// 菜单中的项的实现，菜单项本质上是位于列表中的按钮

	// 重新开始，退出，和悔棋菜单项
	public StartChessJFrame() {
		setTitle("五子棋");// 设置标题
		chessBoard = new ChessBoard();// 构造一个棋盘
		Container contentPane = getContentPane();// 添加到容器中的组件放在一个列表中
		contentPane.add(chessBoard);// 将棋盘追加到此容器的尾部
		chessBoard.setOpaque(true);// 绘制其边界内的所有像素

		// 创建和添加菜单
		menuBar = new JMenuBar();// 初始化菜单栏
		sysMenu = new JMenu("菜单");// 初始化菜单
		// 初始化菜单项
		startMenuItem = new JMenuItem("重新开始");
		exitMenuItem = new JMenuItem("退出");
		backMenuItem = new JMenuItem("悔棋");
		// 将三个菜单项添加到菜单上
		sysMenu.add(startMenuItem);
		sysMenu.add(exitMenuItem);
		sysMenu.add(backMenuItem);
		// 初始化按钮事件监听器内部类
		MyItemListener lis = new MyItemListener();
		// 将三个菜单注册到事件监听器上
		startMenuItem.addActionListener(lis);
		backMenuItem.addActionListener(lis);
		exitMenuItem.addActionListener(lis);
		menuBar.add(sysMenu);// 将系统菜单添加到菜单栏上
		setJMenuBar(menuBar);// 将menuBar设置为菜单栏

		toolbar = new JPanel();// 工具面板实例化
		// 三个按钮初始化
		startButton = new JButton("重新开始");
		exitButton = new JButton("退出");
		backButton = new JButton("悔棋");
		// 将工具面板按钮用FlowLayout布局
		toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
		// 将三个按钮添加到工具面板
		toolbar.add(startButton);
		toolbar.add(exitButton);
		toolbar.add(backButton);
		// 将三个按钮注册监听事件
		startButton.addActionListener(lis);
		exitButton.addActionListener(lis);
		backButton.addActionListener(lis);
		
		add(toolbar, BorderLayout.SOUTH);// 将工具面板布局到界面”南方“也就是下方
		add(chessBoard);// 将面板对象添加到窗体上
		// 设置界面关闭事件
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();// 自适应大小，布置容器，让它使用显示其内容所需的最小空间。
	}
	
	//菜单与按钮的监听器
	private class MyItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object obj = e.getSource();// 获得事件源
			if (obj == startMenuItem || obj == startButton) {
				// 重新开始
				System.out.println("重新开始");
				chessBoard.restartGame();
			}
			else if (obj == exitMenuItem || obj == exitButton){
				// 退出
				System.exit(0);
			}	
			else if (obj == backMenuItem || obj == backButton) {
				// 悔棋
				System.out.println("悔棋");
				chessBoard.goback();
			}
		}
	}

	//main
	public static void main(String[] args) {
		StartChessJFrame f = new StartChessJFrame();// 创建主框架
		f.setVisible(true);// 显示主框架

	}
}
