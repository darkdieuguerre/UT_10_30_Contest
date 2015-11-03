import java.io.*;
import java.util.*;
 
public class Connections {
  private static InputReader in;
  private static PrintWriter out;
  /***

  The code is a variant of FFT called NTT (or number theoretic transform). 
  https://en.wikipedia.org/wiki/Discrete_Fourier_transform_(general)#Number-theoretic_transform
  The following explanation requires some knowledge of FFT. 
  The main point is that FFT can evaluate a polynomial at "roots of unity" efficiently,
  and the inverse FFT can interpolate a polynomial given the values at the roots of unity.

  Roots of unity are defined as the n complex solutions to the equation z^n = 1.
  This also generalizes to prime moduli (i.e. x^n = 1 mod p, where 0 <= x <= p-1).  

  The reason to use NTT over FFT is this is that we don't have to worry about doubles.
  (On a related note, a doubles implementation should pass this problem as well, but you have to be a bit careful about rounding). 
  Here's a link to a doubles implementaiton of FFT: https://github.com/indy256/codelibrary/blob/master/java/src/FFT.java

  Here's a very quick explanation of FFT:

  We're, we're trying to multiply the polynomials

  A = (sum_i x^(a_i)), B = (sum_j x^(b_j)), where a_i,b_j are the input numbers.
  Let C = A * B.
  Then, the coefficient of x^k in C is nonzero only if there is a a_i+b_j=k

  Note the degree of A and B are at most 1,000,000.

  To multiply these polynomials faster than N^2 time (where N = 1,000,000), we evaluate A,B at sufficiently many points.
  Note that the degree of the product is at most 2,000,000, so evaluating at at least 2,000,000 points is sufficniently large.

  We'll choose the 2^(21)st roots of unity in this case, since 2^21 >= 2,000,000 (also, we want to choose a power of two, because FFT requires this).

  Then, we can use the inverse FFT to interpolate the polynomial from these points (note that C(x) = A(x)*B(x), and given d+1 points of a d degree polynomial, we can find the coefficients uniquely). 


  Onto NTT specifically
  NTT requires that we choose a prime number w*2^s + 1, where w is an odd number, and 2^s is larger than our array size
  In this case, any prime where s >= 21 will work.
  I arbitrarily chose p = 5*(2^25))+1.

  Now, let g be a primitive root of p. Recall a primitive root of p is anumber where g^0,g^1,...g^(p-1) modulo p are all unique.
  In this case, 3 is conveniently a primitive root of 5*(2^25+1). (this is also why I chose this prime). 

  Now, a 2^s-th root of unity is just g^w. In this case, a 2^25th root of unity is 3^5 = 243. You can verify that (3^5)^(2^25) = 1 modulo (5*(2^25)+1)
  Note that given a 2^k-th root of unity, we can get a 2^(k-1)-th root of unity by squaring it. 
  Since we want a 2^21-th root of unity, we just square 243 4 times. 
  */
  public static long mod = (5 << 25) + 1; // is a prime
  public static long gen = 243; // is equal to g^w = 3^5 = 243.
  public static int levels = 21; // make sure 2^levels is bigger than degree of resulting polynomial
  public static int len = 1 << levels;
  
  static {
    // currently have a 2^25th root of unity, but want a 2^level root of unity.
    int ww = 25;
    // square gen until we get the correct root of unity. 
    while (ww-- > levels) {
      gen = gen * gen % mod;
    }
  }
 
  public static void transform(long[] a, long gen) {
    // this is an in place implementation of Cooley-Tukey FFT
    for (int i = 0; i < len; i++) {
      int j = Integer.reverse(i) >>> (32 - levels);
      if (j < i) {
        long t = a[i]; a[i] = a[j]; a[j] = t;
      }
    }
    
    // precompute 2^i-th roots of unity for i between 0 and levels.
    long[] w = new long[levels+1];
    w[levels] = gen;
    for (int i = levels-1; i >= 0; i--) {
      w[i] = w[i+1] * w[i+1] % mod;
    }
    for (int l = 1, hs = 1; (1<<l) <= len; l++, hs <<= 1) {
      long[] fr = new long[hs];
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
      transform(arr1, inv(gen, mod)); // inverse fft is just use (gen)^(-1)
      // note that arr1 is scaled by len. However, since we only care about zero/nonzero, we don't do the normalizing step here. 
      
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
