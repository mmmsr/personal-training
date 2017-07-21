import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

	private static final int SLASH = -1;
	private static final int NO_MIRROR = 0;
	private static final int BACKSLASH = 1;

	private static final int TO_RIGHT = 0;
	private static final int TO_BELOW = 1;
	private static final int TO_LEFT = 2;
	private static final int TO_ABOVE = 3;

	private static final int START_XPOS = 1;
	private static final int START_YPOS = 1;

	private static final boolean IN_THE_RANGE = true;

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line1 = br.readLine().trim();
		String[] line1Strings = line1.split(" ");
		// 箱の高さ
		int height = Integer.parseInt(line1Strings[0]);
		// 箱の幅
		int width = Integer.parseInt(line1Strings[1]);


		//マッピング
		List<BoxRow> rows = new ArrayList<BoxRow>();
		for (int i = 0; i < height; i++) {
			String line = br.readLine().trim();
			List<BoxCell> cells = makeBoxRow(width, i + 1, line);
			BoxRow row = new BoxRow(cells);
			rows.add(row);
		}
		BoxGrid grid = new BoxGrid(rows, width, width);

		//進ませる
		boolean inRange = true;
		Beam beam = new Beam(START_XPOS, START_YPOS, TO_RIGHT, 0, IN_THE_RANGE);
		while(inRange){
			inRange = move(beam, grid);
		}

		System.out.println(beam.getBoxCnt());
	}

	private static List<BoxCell> makeBoxRow(int width, int ypos, String line) {
		List<BoxCell> cells = new ArrayList<BoxCell>();
		char[] charArray = line.toCharArray();
		for (int i = 0; i < width; i++) {
			char ch = charArray[i];
			BoxCell cell = new BoxCell(i + 1, ypos, ch);
			cells.add(cell);
		}
		return cells;
	}

	private static boolean move(Beam beam, BoxGrid grid) {
		int currentXpos = beam.getXpos();
		int currentYpos = beam.getYpos();
		int direction = beam.getDirection();

		//現在の方向に進める
		switch (direction){
			case TO_RIGHT:
				currentXpos++;
				beam.setXpos(currentXpos);
				break;
			case TO_BELOW:
				currentYpos++;
				beam.setYpos(currentYpos);
				break;
			case TO_LEFT:
				currentXpos--;
				beam.setXpos(currentXpos);
				break;
			case TO_ABOVE:
				currentYpos--;
				beam.setYpos(currentYpos);
				break;
		}
		//カウントアップ
		beam.setBoxCnt(beam.getBoxCnt() + 1);

		//次のセルの情報取得
		BoxCell nextCell = grid.getCell(currentXpos, currentYpos);
		if(nextCell == null){
			return false;
		}
		int mirror = nextCell.getMirror();

		//反射有無に応じて向きを変える
		direction = reflect(direction, mirror);
		beam.setDirection(direction);

		return true;
	}

	private static int reflect(int direction, int mirror) {
		if(mirror == NO_MIRROR){
			return direction;
		}else if(mirror == SLASH){
			switch (direction){
				case TO_RIGHT:
					return TO_ABOVE;
				case TO_BELOW:
					return TO_LEFT;
				case TO_LEFT:
					return TO_BELOW;
				case TO_ABOVE:
					return TO_RIGHT;
			}
		}else if(mirror == BACKSLASH){
			switch (direction){
				case TO_RIGHT:
					return TO_BELOW;
				case TO_BELOW:
					return TO_RIGHT;
				case TO_LEFT:
					return TO_ABOVE;
				case TO_ABOVE:
					return TO_LEFT;
			}
		}
		return direction;
	}
}

class BoxGrid {
	private List<BoxRow> rows;
	private int width;
	private int height;

	BoxGrid(List<BoxRow> rows, int width, int height){
		this.rows = rows;
		this.height = height;
		this.width = width;
	}

	public List<BoxRow> getRows() {
		return rows;
	}

	public BoxCell getCell(int xpos, int ypos) {
		if(ypos>rows.size()){
			return null;
		}
		BoxRow row = rows.get(ypos-1);
		List<BoxCell> cells = row.getCells();
		if(xpos<=0||cells.size()<xpos){
			return null;
		}
		BoxCell cell = cells.get(xpos-1);
		return cell;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}

class BoxRow {
	private List<BoxCell> cells;
	private int width;

	BoxRow(List<BoxCell> cells){
		this.cells = cells;
		this.width = cells.size();
	}

	public List<BoxCell> getCells() {
		return cells;
	}

	public int getWidth() {
		return width;
	}
}

class BoxCell {
	// スタート位置から右へX個
	private int xpos;
	// スタート位置から下へY個
	private int ypos;
	// /:-1, _:0, \:1
	private int mirror;

	BoxCell(int xpos, int ypos, char ch){
		this.xpos = xpos;
		this.ypos = ypos;
		if(ch=='/'){
			this.mirror = -1;
		}else if(ch == '_'){
			this.mirror = 0;
		}else if(ch == '\\'){
			this.mirror = 1;
		}
	}

	public int getXpos() {
		return xpos;
	}

	public void setXpos(int xpos) {
		this.xpos = xpos;
	}

	public int getYpos() {
		return ypos;
	}

	public void setYpos(int ypos) {
		this.ypos = ypos;
	}

	public int getMirror() {
		return mirror;
	}

	public void setMirror(int mirror) {
		this.mirror = mirror;
	}
}

class Beam {
	// スタート位置から右へX個
	private int xpos;
	// スタート位置から下へY個
	private int ypos;
	// →:0, ↓:1, ←:2, ↑:3
	private int direction;
	//
	private int boxCnt = 1;
	//領域内にいればTrue 外に出たらFalse
	private boolean inRange;

	Beam(int xpos, int ypos, int direction, int boxCnt, boolean inRange){
		this.xpos = xpos;
		this.ypos = ypos;
		this.direction = direction;
		this.boxCnt = boxCnt;
		this.inRange = inRange;
	}

	public int getXpos() {
		return xpos;
	}

	public void setXpos(int xpos) {
		this.xpos = xpos;
	}

	public int getYpos() {
		return ypos;
	}

	public void setYpos(int ypos) {
		this.ypos = ypos;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getBoxCnt() {
		return boxCnt;
	}

	public void setBoxCnt(int boxCnt) {
		this.boxCnt = boxCnt;
	}

	public boolean isInRange() {
		return inRange;
	}

	public void setInRange(boolean inRange) {
		this.inRange = inRange;
	}
}
