import java.io.*;
import java.util.*;
public class Capacity {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int cases = Integer.parseInt(br.readLine());
		while(cases-- > 0) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int n = Integer.parseInt(st.nextToken());
			int k = Integer.parseInt(st.nextToken());
			int[] dp = new int[k+1];
			while(n-- > 0) {
				st = new StringTokenizer(br.readLine());
				int s = Integer.parseInt(st.nextToken());
				int v = Integer.parseInt(st.nextToken());
				for(int i = k; i >= s; i--) {
					dp[i] = Math.max(dp[i], dp[i-s]+v);
				}
			}
			System.out.println(dp[k]);
		}
	}
}
