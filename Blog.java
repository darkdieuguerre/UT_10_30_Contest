import java.io.*;
import java.util.*;
public class Blog {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int cases = Integer.parseInt(br.readLine());
		while(cases-- > 0) {
			System.out.println(br.readLine().equals("Dropbox") ? "YES" : "NO");
		}
	}
}
