package rand.pack;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

public class Test {

	public static void main(String[] args) {
		BidiMap<Integer, Integer> bijection = new DualHashBidiMap<>();
		bijection.put(3, 1);
		bijection.put(5, 2);
		Integer i = bijection.getKey(10);
		System.out.println(i);
		bijection.forEach((k, v) -> System.out.printf("(%s, %s)%n", k, v));
	}

}
