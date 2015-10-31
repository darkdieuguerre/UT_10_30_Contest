import java.io.*;
import java.util.*;
public class Meetings {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int cases = Integer.parseInt(br.readLine());
		while(cases-- > 0) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int n = Integer.parseInt(st.nextToken());
			int m = Integer.parseInt(st.nextToken());
			int[] a = new int[n];
			st = new StringTokenizer(br.readLine());
			for(int i = 0; i < n; i++) {
				a[i] = Integer.parseInt(st.nextToken());
			}
			int[] b = new int[m];
			st = new StringTokenizer(br.readLine());
			for(int i = 0; i < m; i++) {
				b[i] = Integer.parseInt(st.nextToken());
			}
			Arrays.sort(a);
			Arrays.sort(b);
			int ret = 0;
			int last = m-1;
			for(int i = n-1; i >= 0 && last >= 0; i--) {
				if(a[i] <= b[last]) {
					ret++;
					last--;
				}
			}
			System.out.println(ret);
		}
	}
}
