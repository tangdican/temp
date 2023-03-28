package edu.princeton.cs.algs4;

import java.util.Arrays;

public class MergeList {
    public static void main(String[] args) {
        String[] firsts = {"three","one","nine","four"};
        String[] adds = {"five","one","two","five","o","ffffffffffffffff"};
        String[] removes = {"six","four"};
        String[] mer = new String[firsts.length+adds.length];
        merge(firsts, adds, mer);

        sortByLength(mer);

        String[] res = unique(mer);

        sortByLength(removes);

        remove(res, removes);

        descend(res);
        System.out.println(Arrays.toString(res));
    }

    private static void remove(String[] a, String[] b) {
        int bi = 0;
        int ind = 0;
        for (int loc = 0; loc < a.length; loc++) {
            if (lessByLength(a[loc], b[bi])) {
                ind++;
                continue;
            } else if(a[loc].equals(b[bi])) {
                bi++;
            }
        }
    }

    private static void descend(String[] a) {
        int len = a.length;
        int half = len / 2;
        for (int loc = 0; loc < half; loc++) {
            exch(a, loc, len-1-loc);
        }
    }

    private static String[] unique(String[] a) {
        int ind = 0;
        for (int loc = 1; loc < a.length; loc++) {
            if (a[ind] == a[loc]) {
                continue;
            }
            if(++ind < loc) {
                a[ind] = a[loc];
            }
        }
        String[] b = new String[ind+1];
        for (int loc = 0; loc < ind+1; loc++) {
            b[loc] = a[loc];
        }
        return b;
    }

    public static void merge(String[] a, String[] b, String[] c) {
        int i = 0;
        for (; i < a.length; i++) {
            c[i] = a[i];
        }
        for (int j = 0; j < b.length; j++) {
            c[i+j] = b[j];
        }
    }

    private static void sortByLength(String[] a) {
        int len = a.length;
        // cutoff to insertion sort for small subarrays
        if (len <= CUTOFF) {
            insertionByLength(a);
            return;
        }

        // compute frequency counts
        int[] heads = new int[L+2];
        int[] tails = new int[L+2];
        for (int i = 0; i < len; i++) {
            int l = a[i].length();
            assert l < L;
            heads[l+2]++;
        }

        // transform counts to indices
        for (int r = 0; r < L+1; r++) {
            heads[r+1] += heads[r];
            tails[r] = heads[r+1];
        }

        // sort by length in-place
        for (int r = 0; r < L+1; r++) {
            while (heads[r] < tails[r]) {
                int l = a[heads[r]].length();
                while (l+1 != r) {
                    exch(a, heads[r], heads[l+1]++);
                    l = a[heads[r]].length();
                }
                heads[r]++;
            }
        }

        // recursively sort for each character (excludes sentinel -1)
        for (int r = 0; r < L; r++)
            sort(a, tails[r], tails[r+1] - 1, 0);
    }

    private static void insertionByLength(String[] a) {
        int len = a.length;
        for (int i = 0; i < len; i++)
            for (int j = i; j > 0 && lessByLength(a[j], a[j-1]); j--)
                exch(a, j, j-1);
    }

    private static boolean lessByLength(String v, String w) {
        return v.length() < w.length() || (v.length() == w.length() && less(v, w,0));
    }


    public static void sort(String[] a) {
        int n = a.length;
        sort(a, 0, n-1, 0);
    }

    // return dth character of s, -1 if d = length of string
    private static int charAt(String s, int d) {
        assert d >= 0 && d <= s.length();
        if (d == s.length()) return -1;
        return s.charAt(d);
    }

    // sort from a[lo] to a[hi], starting at the dth character
    private static void sort(String[] a, int lo, int hi, int d) {
        if(lo >= hi) {
            return;
        }
        // cutoff to insertion sort for small subarrays
        if (hi <= lo + CUTOFF) {
            insertion(a, lo, hi, d);
            return;
        }

        // compute frequency counts
        int[] heads = new int[R+2];
        int[] tails = new int[R+1];
        for (int i = lo; i <= hi; i++) {
            int c = charAt(a[i], d);
            heads[c+2]++;
        }

        // transform counts to indices
        heads[0] = lo;
        for (int r = 0; r < R+1; r++) {
            heads[r+1] += heads[r];
            tails[r] = heads[r+1];
        }

        // sort by d-th character in-place
        for (int r = 0; r < R+1; r++) {
            while (heads[r] < tails[r]) {
                int c = charAt(a[heads[r]], d);
                while (c + 1 != r) {
                    exch(a, heads[r], heads[c+1]++);
                    c = charAt(a[heads[r]], d);
                }
                heads[r]++;
            }
        }

        // recursively sort for each character (excludes sentinel -1)
        for (int r = 0; r < R; r++)
            sort(a, tails[r], tails[r+1] - 1, d+1);
    }


    // insertion sort a[lo..hi], starting at dth character
    private static void insertion(String[] a, int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(a[j], a[j-1], d); j--)
                exch(a, j, j-1);
    }

    // exchange a[i] and a[j]
    private static void exch(String[] a, int i, int j) {
        String temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    // is v less than w, starting at character d
    private static boolean less(String v, String w, int d) {
        // assert v.substring(0, d).equals(w.substring(0, d));
        for (int i = d; i < Math.min(v.length(), w.length()); i++) {
            if (v.charAt(i) < w.charAt(i)) return true;
            if (v.charAt(i) > w.charAt(i)) return false;
        }
        return v.length() < w.length();
    }

    private static final int R             = 256;   // extended ASCII alphabet size
    private static final int L             = 25;   // max length of element
    private static final int CUTOFF        =  2;
}
