import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;

import javax.swing.*;

/**
 * 五子棋--棋盘类
 */

public class ChessBoard extends JPanel implements MouseListener,MouseMotionListener {
	/**
	 * 构造方法 ChessBoard()
	 * 绘制方法 paintComponent(Graphics g)
	 * 鼠标按下 mousePressed(MouseEvent e)
	 * 查找棋子 findChess(int x, int y):boolean
	 * 是否胜利 isWin():boolean
	 * 重新开始 restartGame()
	 * 悔棋 goback()
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int MARGIN = 30;// 边距
	public static final int GRID_SPAN = 35;// 网格间距
	public static final int ROWS = 15;// 棋盘行数
	public static final int COLS = 20;// 棋盘列数
	public static final int BLACK_CHESS = 1;
	public static final int WHITE_CHESS = 2;
	public static final int NO_CHESS = 0;
	
	Point[] chessList = new Point[(ROWS + 1) * (COLS + 1)];// 初始每个数组元素为null
	int board[][] = new int[COLS+1][ROWS+1];
	boolean isBlack = true;// 默认开始是黑棋先
	boolean gameOver = false;// 游戏是否结束
	int chessCount;// 当前棋盘棋子的个数

	// 构造
	public ChessBoard() {
		setBackground(Color.white);// 设置背景色为白色
		addMouseListener(this);// 添加指定的鼠标侦听器
		addMouseMotionListener(this);// 添加指定的鼠标移动侦听器
	}

	// 绘制
	public void paintComponent(Graphics g) {
		super.paintComponent(g);// 画棋盘
		Color colortemp;
		for (int i = 0; i <= ROWS; i++) {// 画横线
			g.drawLine(MARGIN, MARGIN + i * GRID_SPAN, MARGIN + COLS * GRID_SPAN, MARGIN + i * GRID_SPAN);
		}
		for (int i = 0; i <= COLS; i++) {// 画竖线
			g.drawLine(MARGIN + i * GRID_SPAN, MARGIN, MARGIN + i * GRID_SPAN, MARGIN + ROWS * GRID_SPAN);
		}

		// 画棋子
		for (int i = 0; i < chessCount; i++) {
			// 网格交叉点x，y坐标
			int xPos = chessList[i].getX() * GRID_SPAN + MARGIN;
			int yPos = chessList[i].getY() * GRID_SPAN + MARGIN;
			colortemp = chessList[i].getColor();
			g.setColor(colortemp);// 设置颜色
			// 使用圆形辐射颜色渐变模式填充某一形状
			if (colortemp == Color.black) {
				RadialGradientPaint paint = new RadialGradientPaint(
						xPos - Point.DIAMETER / 2 + 25, 
						yPos - Point.DIAMETER / 2 + 10,
						20,
						new float[] { 0f, 1f },
						new Color[] { Color.WHITE, Color.BLACK });
				((Graphics2D) g).setPaint(paint);
				((Graphics2D) g).setRenderingHint(
						RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				((Graphics2D) g).setRenderingHint(
						RenderingHints.KEY_ALPHA_INTERPOLATION,
						RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);

			} 
			else if (colortemp == Color.white) {
				RadialGradientPaint paint = new RadialGradientPaint(
						xPos - Point.DIAMETER / 2 + 25,
						yPos - Point.DIAMETER / 2 + 10,
						70,
						new float[] { 0f, 1f },
						new Color[] { Color.WHITE, Color.BLACK });
				((Graphics2D) g).setPaint(paint);
				((Graphics2D) g).setRenderingHint(
						RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				((Graphics2D) g).setRenderingHint(
						RenderingHints.KEY_ALPHA_INTERPOLATION,
						RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);

			}

			Ellipse2D e = new Ellipse2D.Float(
					xPos - Point.DIAMETER / 2,
					yPos - Point.DIAMETER / 2,
					34,
					35);
			((Graphics2D) g).fill(e);
			
			// 标记最后一个棋子的红矩形框
			if (i == chessCount - 1) {
				g.setColor(Color.red);
				g.drawRect(xPos - Point.DIAMETER / 2, yPos - Point.DIAMETER / 2, 34, 35);
			}
		}
	}
	
	// 在棋子数组中查找并返回索引为x，y颜色为color的棋子对象
	// 判断胜利时需要
	private boolean getChess(int xIndex, int yIndex, int color) {
		if (board[xIndex][yIndex] == color) return true;
		return false;
	}
	
	// 判断是否胜利
	private boolean isWin(int xIndex,int yIndex) {
		int continueCount = 1;// 连续棋子的个数
		int c = isBlack ? BLACK_CHESS : WHITE_CHESS;// 棋子颜色
		
		// 向左寻找
		for (int x = xIndex - 1; x >= 0; x--) {
			if (getChess(x, yIndex, c)) continueCount++;
			else break;
		}
		// 向右寻找
		for (int x = xIndex + 1; x <= COLS; x++) {
			if (getChess(x, yIndex, c)) continueCount++;
			else break;
		}
		if (continueCount >= 5) return true;
		else continueCount = 1;

		// 向上寻找
		for (int y = yIndex - 1; y >= 0; y--) {
			if (getChess(xIndex, y, c)) continueCount++;
			else break;
		}
		// 向下寻找
		for (int y = yIndex + 1; y <= ROWS; y++) {
			if (getChess(xIndex, y, c)) continueCount++;
			else break;
		}
		if (continueCount >= 5) return true;
		else continueCount = 1;

		// 右上寻找
		for (int x = xIndex + 1, y = yIndex - 1; y >= 0 && x <= COLS; x++, y--) {
			if (getChess(x, y, c)) continueCount++;
			else break;
		}
		// 左下寻找
		for (int x = xIndex - 1, y = yIndex + 1; x >= 0 && y <= ROWS; x--, y++) {
			if (getChess(x, y, c)) continueCount++;
			else break;
		}
		if (continueCount >= 5) return true;
		else continueCount = 1;

		// 左上寻找
		for (int x = xIndex - 1, y = yIndex - 1; x >= 0 && y >= 0; x--, y--) {
			if (getChess(x, y, c)) continueCount++;
			else break;
		}
		// 右下寻找
		for (int x = xIndex + 1, y = yIndex + 1; x <= COLS && y <= ROWS; x++, y++) {
			if (getChess(x, y, c)) continueCount++;
			else break;
		}
		if (continueCount >= 5) return true;
		else continueCount = 1;

		return false;
	}

	// 清除棋子
	public void restartGame() {	
		for (int i = 0; i < chessList.length; i++) {
			chessList[i] = null;
		}
		for (int i = 0; i <= COLS; i++ ) {
			for (int j = 0; j <= ROWS; j++ ) {
				board[i][j] = NO_CHESS;
			}
		}
		// 恢复游戏相关的变量值
		isBlack = true;
		gameOver = false; // 游戏是否结束
		chessCount = 0; // 当前棋盘棋子个数
		repaint();
	}

	// 悔棋
	public void goback() {
		if (chessCount == 0) return;
		int x=chessList[chessCount - 1].getX();
		int y=chessList[chessCount - 1].getY();
		board[x][y]=NO_CHESS; // 置空棋盘
		chessList[chessCount - 1] = null; // 消除棋子
		chessCount--;
		isBlack = !isBlack;
		repaint();
	}

	// 矩形Dimension
	public Dimension getPreferredSize() {
		return new Dimension(MARGIN * 2 + GRID_SPAN * COLS, MARGIN * 2 + GRID_SPAN * ROWS);
	}

	// 在棋子数组中查找是否有索引为x，y的棋子存在
	// 鼠标按下时需要
	private boolean findChess(int x, int y) {
		for (Point c : chessList) {
			if (c != null && c.getX() == x && c.getY() == y)
				return true;
		}
		return false;
	}
	
	// 能否放置棋子
	private boolean canPush(int x, int y){
		if (x < 0 || x > COLS || y < 0 || y > ROWS) return false;
		if (gameOver) return false;
		if (findChess(x, y)) return false;
		return true; 
	}
	
	// MouseListener
	public void mousePressed(MouseEvent e) {
		// 鼠标在组件上按下时调用	
		
		// 将鼠标点击的坐标位置转换成网格索引
		int xIndex = (e.getX() - MARGIN + GRID_SPAN / 2) / GRID_SPAN;
		int yIndex = (e.getY() - MARGIN + GRID_SPAN / 2) / GRID_SPAN;
		
		// 落在棋盘外不能下、游戏结束时，不再能下、如果x，y位置已经有棋子存在，不能下
		if (!canPush(xIndex, yIndex)) return;

		// 可以进行时的处理
		Point ch = new Point(xIndex, yIndex, isBlack ? Color.black : Color.white);

		//更新棋盘
		chessList[chessCount++] = ch;
		if (isBlack) board[xIndex][yIndex]=BLACK_CHESS;
		else board[xIndex][yIndex]=WHITE_CHESS;
		
		repaint();// 通知系统重新绘制

		// 如果胜出则给出提示信息，不能继续下棋
		if (isWin(xIndex,yIndex)) {
			String colorName = isBlack ? "黑棋" : "白棋";
			String msg = String.format("恭喜，%s赢了！", colorName);
			JOptionPane.showMessageDialog(this, msg);// 标准对话框、告知用户某事已发生
			gameOver = true;
		}
		isBlack = !isBlack;
	}

	public void mouseClicked(MouseEvent e) {
		// 鼠标按键在组件上单击时调用
	}

	public void mouseEntered(MouseEvent e) {
		// 鼠标进入到组件上时调用
	}

	public void mouseExited(MouseEvent e) {
		// 鼠标离开组件时调用
	}

	public void mouseReleased(MouseEvent e) {
		// 鼠标按钮在组件上释放时调用
	}

	//MouseMotionListener
	public void mouseDragged(MouseEvent arg0) {
		// 鼠标按键在组件上按下并拖动时调用
	}

	public void mouseMoved(MouseEvent e) {
		// 鼠标光标移动到组件上但无按键按下时调用
		
		// 将鼠标点击的坐标位置转成网格索引
		int x = (e.getX() - MARGIN + GRID_SPAN / 2) / GRID_SPAN;
		int y = (e.getY() - MARGIN + GRID_SPAN / 2) / GRID_SPAN;
		
		// 游戏已经结束不能下、落在棋盘外不能下、x,y位置已经有棋子存在不能下
		if (canPush(x, y)) setCursor(new Cursor(Cursor.HAND_CURSOR));// 手状光标类型
		else setCursor(new Cursor(Cursor.DEFAULT_CURSOR));// 设置成默认状态		
	}

}
