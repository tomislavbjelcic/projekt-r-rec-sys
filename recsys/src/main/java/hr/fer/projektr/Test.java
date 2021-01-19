package hr.fer.projektr;

import java.util.HashMap;
import java.util.Map;

public class Test {
	
	public static void main(String[] args) {
		
		Map<Integer, Integer> m = new HashMap<>();
		int max = Integer.MAX_VALUE - 5;
		for (int i=0; i<max; i++) {
			m.put(i, 420);
			if (i % 1000 == 0)
				System.out.println(i);
		}
		
	}
	
}
