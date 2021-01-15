package rand.pack;

import java.util.Objects;
import java.util.function.LongSupplier;

public class Util {
	
	private static final LongSupplier nanoTimeSupplier = System::nanoTime;
	private static final LongSupplier millisTimeSupplier = System::currentTimeMillis;

	private static long measureTime(Runnable r, LongSupplier s) {
		Objects.requireNonNull(r);
		Objects.requireNonNull(s);
		long start = s.getAsLong();
		r.run();
		long stop = s.getAsLong();
		return stop - start;
	}
	
	public static long measureNanoTime(Runnable r) {
		return measureTime(r, nanoTimeSupplier);
	}
	
	public static long measureMillisTime(Runnable r) {
		return measureTime(r, millisTimeSupplier);
	}
	
	public static double measureSecTime(Runnable r) {
		return measureMillisTime(r) / 1000.;
	}
	
	public static void main(String[] args) {
		Runnable r = () -> {
			for (long i=0; i<1_000_000_000_0L; i++);
		};
		double t = measureSecTime(r);
		System.out.println(t);
		//r.run();
	}

}
