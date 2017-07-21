import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class Main {
	private static final char[] ALPHABET = new char[] {'a', 'b', 'c', 'd', 'e',
		'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
		't', 'u', 'v', 'w', 'x', 'y', 'z'};
	private static int idx = 0;
	private static final int DEFAULT_LENGTH_0 = 0;
	private static final int ADDITIONAL_COUNT_1 = 1;
	private static final int TYPE_CHAR_VALUE = 0;
	private static final int TYPE_NUM_VALUE = 1;
	private static final int TYPE_BRACKET_OPEN = 2;
	private static final int TYPE_BRACKET_CLOSE = 3;
	private static final char BRACKET_OPEN = '(';
	private static final char BRACKET_CLOSE = ')';
	private static final char BLANK = ' ';

	private static final long DEFAULT_COUNT_0 = 0;
	private static final long DEFAULT_COUNT_1 = 1;

	public static void main(String args[] ) throws Exception {
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
		HashMap<Character, Long> atozMap = makeHistogram(input, idx);
		printAtozMap(atozMap);
		if(scanner!=null){
			scanner.close();
		}
	}

	private static HashMap<Character, Long> makeAtozMap(){
		HashMap<Character, Long> atozMap = new HashMap<Character,Long>();
		for (char ch: ALPHABET){
			atozMap.put(ch, DEFAULT_COUNT_0);
		}
		return atozMap;
	}

	private static void printAtozMap(HashMap<Character, Long> atozMap){
		StringBuffer result = new StringBuffer();
		for (char ch: ALPHABET){
			result.setLength(DEFAULT_LENGTH_0);
			System.out.println(result.append(ch).append(BLANK).append(atozMap.get(ch)));
		}
	}

	private static HashMap<Character, Long> addResults(HashMap<Character, Long> originalMap,
			HashMap<Character, Long> additionalMap, Long multiplier){
		for(Entry<Character, Long> atozCnt : originalMap.entrySet()) {
			originalMap.put(atozCnt.getKey(), originalMap.get(atozCnt.getKey()) +
					additionalMap.get(atozCnt.getKey()) * multiplier);
		}
		return originalMap;
	}

	private static HashMap<Character, Long> makeHistogram(String input, int tempIdx){
		HashMap<Character, Long> atozMap = makeAtozMap();

		if(tempIdx > input.length()){
			return atozMap;
		}

		idx = tempIdx;
		StringBuffer count = new StringBuffer();

		while(idx < input.length()){
			char ch = input.charAt(idx);
			int charType = checkCharType(ch);
			long multiplier;
			switch (charType) {
			case TYPE_NUM_VALUE:
				count.append(ch);
				break;
			case TYPE_CHAR_VALUE:
				multiplier = getMultiplier(count);
				atozMap.put(ch, atozMap.get(ch) + multiplier);
				count.setLength(DEFAULT_LENGTH_0);
				break;
			case TYPE_BRACKET_OPEN:
				HashMap<Character, Long> subAtozMap = new HashMap<Character, Long>();
				subAtozMap = makeHistogram(input, idx + ADDITIONAL_COUNT_1);
				multiplier = getMultiplier(count);
				atozMap = addResults(atozMap, subAtozMap, multiplier);
				count.setLength(DEFAULT_LENGTH_0);
				break;
			case TYPE_BRACKET_CLOSE:
				return atozMap;
			default:
				break;
			}
			idx++;
		}
		return atozMap;
	}

	private static int checkCharType(char c){
		if (Character.isDigit(c)){
			return TYPE_NUM_VALUE;
		}if (c == BRACKET_OPEN) {
			return TYPE_BRACKET_OPEN;
		} else if (c == BRACKET_CLOSE) {
			return TYPE_BRACKET_CLOSE;
		} else {
			return TYPE_CHAR_VALUE;
		}
	}

	private static long getMultiplier(StringBuffer count){
		if (count.length() == DEFAULT_LENGTH_0) {
			return DEFAULT_COUNT_1;
		} else {
			return Long.valueOf(count.toString());
		}
	}
}