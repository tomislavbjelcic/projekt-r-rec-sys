package hr.fer.projektr;

import java.util.LinkedList;
import java.util.List;

public class Test {
	
	public static void main(String[] args) {
		
		List<Integer> list = new LinkedList<>();
		int max = Integer.MAX_VALUE;
		for (int i=0; i<max - 5; i++) {
			try {
				list.add(i);
			} catch (Throwable t) {
				System.out.println(i);
				t.printStackTrace();
				return;
			}
			if (i % 1000000 == 0)
				System.out.println(i);
		}
		
	}
	
}
