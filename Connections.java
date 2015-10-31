import java.io.*;
import java.util.*;

public class Connections {
	private static InputReader in;
	private static PrintWriter out;
	public static long mod = (5 << 25) + 1;
	public static long gen = 243;
	public static int levels = 21;
	public static int len = 1 << levels;
	public static long[] fr;

	static {
		int ww = 25;
		while (ww-- > levels) {
			gen = gen * gen % mod;
		}
	}

	public static void transform(long[] a, long gen) {
		for (int i = 0; i < len; i++) {
			int j = Integer.reverse(i) >>> (32 - levels);
	if (j < i) {
		long t = a[i]; a[i] = a[j]; a[j] = t;
	}
		}

		long[] w = new long[levels+1];
		w[levels] = gen;
		for (int i = levels-1; i >= 0; i--) {
			w[i] = w[i+1] * w[i+1] % mod;
		}
		for (int l = 1, hs = 1; (1<<l) <= len; l++, hs <<= 1) {
			fr = new long[hs];
			fr[0] = 1;
			for (int j = 1; j < hs; j++) fr[j] = fr[j-1] * w[l] % mod;
			for (int i = 0; i < len; i += (1<<l)) {
				for (int j = i; j < i + hs; j++) {
					long tre = a[j + hs] * fr[j-i] % mod;
					a[j + hs] = a[j] + mod - tre;
					if (a[j+hs] >= mod) a[j+hs] -= mod;
					a[j] += tre;
					if (a[j] >= mod) a[j] -= mod;
				}
			}
		}
	}

	public static long inv (long N, long M) {
		long x = 0, lastx = 1, y = 1, lasty = 0, q, t, a = N, b = M;
		while (b != 0) {
			q = a / b; t = a % b; a = b; b = t;
			t = x; x = lastx - q * x; lastx = t;
			t = y; y = lasty - q * y; lasty = t;
		}
		return (lastx + M) % M;
	}

	public static void main(String[] args) throws IOException {
		in = new InputReader(System.in);
		out = new PrintWriter(System.out, true);
		int t = in.nextInt();
		while (t-- > 0) {
			int n = in.nextInt(), m = in.nextInt();
			long[] arr1 = new long[len];
			long[] arr2 = new long[len];
			for (int i = 0; i < n; i++) arr1[in.nextInt()] = 1;
			for (int i = 0; i < m; i++) arr2[in.nextInt()] = 1;

			transform(arr1, gen);
			transform(arr2, gen);
			for (int i = 0; i < len; i++)
				arr1[i] = arr1[i] * arr2[i] % mod;
			transform(arr1, inv(gen, mod));

			long res = 0;
			for (int i = 0; i < len; i++)
				res += arr1[i] > 0 ? 1 : 0;
				out.println(res);
		}
		out.close();
		System.exit(0);
	}

	static class InputReader {
		public BufferedReader reader;
		public StringTokenizer tokenizer;

		public InputReader(InputStream stream) {
			reader = new BufferedReader(new InputStreamReader(stream), 32768);
			tokenizer = null;
		}

		public String next() {
			while (tokenizer == null || !tokenizer.hasMoreTokens()) {
				try {
					tokenizer = new StringTokenizer(reader.readLine());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			return tokenizer.nextToken();
		}

		public int nextInt() {
			return Integer.parseInt(next());
		}
	}


}