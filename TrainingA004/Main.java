import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Main {

	private static final int GOAL_NODE_XPOS = 1;

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String conditions = br.readLine().trim();
		String[] conditionArray = conditions.split(" ");

		// �c���̒���
		int totalLength = Integer.parseInt(conditionArray[0]);
		// �����̖{��
		int numOfHrzLine = Integer.parseInt(conditionArray[2]);

		Map<Node, Node> ladderMap = makeLadderMap(br, numOfHrzLine);

		int answer = calcLadderMap(GOAL_NODE_XPOS, totalLength, ladderMap);
		System.out.println(answer);
	}

	public static Map<Node, Node> makeLadderMap(BufferedReader br, int numOfHrzLine) throws IOException {
		Map<Node, Node> ladderMap = new HashMap<Node, Node>();
		for (int i = 0; i < numOfHrzLine; i++) {
			String line = br.readLine().trim();
			String[] lineStrings = line.split(" ");
			// i�Ԗڂ̏c��
			int lineNumber = Integer.parseInt(lineStrings[0]);
			// i�Ԗڂ̏c���̏ォ��̒���
			int distOfLeft = Integer.parseInt(lineStrings[1]);
			// i+1�Ԗڂ̏c���̏ォ��̒���
			int distOfRight = Integer.parseInt(lineStrings[2]);

			Node leftNode = new Node(lineNumber, distOfLeft);
			Node rightNode = new Node(lineNumber + 1, distOfRight);
			ladderMap.put(leftNode, rightNode);
			ladderMap.put(rightNode, leftNode);
		}
		return ladderMap;
	}

	public static int calcLadderMap(int currentXpos, int currentYpos, Map<Node, Node> ladderLeg) {
		for (int i = currentYpos - 1; i > 0; i--) {
			Node currentNode = new Node(currentXpos, i);
			if (ladderLeg.containsKey(currentNode)) {
				Node nextNode = ladderLeg.get(currentNode);
				return calcLadderMap(nextNode.getXpos(), nextNode.getYpos(), ladderLeg);
			}
		}
		return currentXpos;
	}
}

class Node {
	// ���Ԗڂ̏c����
	private int xpos;
	// �ォ��̒���
	private int ypos;

	public int getXpos() {
		return xpos;
	}

	public int getYpos() {
		return ypos;
	}

	public Node(int xpos, int ypos) {
		this.xpos = xpos;
		this.ypos = ypos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + xpos;
		result = prime * result + ypos;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (xpos != other.xpos)
			return false;
		if (ypos != other.ypos)
			return false;
		return true;
	}
}
