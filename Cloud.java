import java.io.*;
import java.util.*;
public class Cloud {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int cases = Integer.parseInt(br.readLine());
		while(cases-- > 0) {
			int n = Integer.parseInt(br.readLine());
			double[] ang = new double[n];
			for(int i = 0; i < n; i++) {
				StringTokenizer st = new StringTokenizer(br.readLine());
				int x = Integer.parseInt(st.nextToken());
				int y = Integer.parseInt(st.nextToken());
				int g = gcd(Math.abs(x), Math.abs(y));
				ang[i] = Math.atan2(y/g, x/g);
			}
			System.out.println(isValid(ang) || isValid(rev(ang)) ? "YES" : "NO");
		}
	}
	public static double[] rev(double[] in) {
		double[] ret = new double[in.length];
		for(int i = 0; i < in.length; i++) {
			ret[ret.length-1-i] = in[i];
		}
		return ret;
	}
	public static boolean isValid(double[] ang) {
		int min = 0;
		for(int i = 1; i < ang.length; i++) {
			if(ang[i] < ang[min]) {
				min = i;
			}
		}
		boolean ret = true;
		for(int i = 1; i < ang.length; i++) {
			int next = (min + i)%ang.length;
			int a = (min+i-1)%ang.length;
			if(ang[next] < ang[a]) {
				ret = false;
			}
		}
		return ret;
	}
	public static int gcd(int a, int b) {
		if(b == 0) return a;
		if(a == 0) return b;
		return gcd(b, a%b);
	}
}
