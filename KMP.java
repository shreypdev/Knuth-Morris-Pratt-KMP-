import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;


public class KMP{

	private final int R;    // The radix
    private int[][] dfa;    // The KMP automoton
    private String pattern; // Pattern string
    
    
    public KMP(String pattern){  
		this.R = 256;
        this.pattern = pattern;

        // build DFA from pattern
        int patternLen = pattern.length();
        dfa = new int[R][patternLen]; 
        dfa[pattern.charAt(0)][0] = 1; 
        for (int i = 0, j = 1; j < patternLen; j++) {
            for (int c = 0; c < R; c++) 
                dfa[c][j] = dfa[c][i];     	   // Copy mismatch cases. 
            dfa[pattern.charAt(j)][j] = j+1;   // Set match case. 
            i = dfa[pattern.charAt(j)][i];     // Update restart state. 
        }
    }
    
    public int search(String txt){  
		// simulate operation of DFA on text
        int patternLen = pattern.length();
        int txtLen = txt.length();
        int i, j;
        for (i = 0, j = 0; i < txtLen && j < patternLen; i++) {
            j = dfa[txt.charAt(i)][j];
        }
        if (j == patternLen) return i - patternLen;    // found
        return txtLen;
    }
    
    
    public static void main(String[] args) throws FileNotFoundException{
		Scanner s;
		if (args.length > 0){
			try{
				s = new Scanner(new File(args[0]));
			}
			catch(java.io.FileNotFoundException e){
				System.out.println("Unable to open "+args[0]+ ".");
				return;
			}
			System.out.println("Opened file "+args[0] + ".");
			String text = "";
			while(s.hasNext()){
				text += s.next();
			}
			
			for(int i = 1; i < args.length; i++){
				KMP k = new KMP(args[i]);
				int index = k.search(text);
				if(index >= text.length())
					System.out.println("The pattern \"" + args[i] + "\" was not found.");
				else
					System.out.println("The string \"" + args[i] + "\" was found at index " + index + ".");
			}
		}
		else{
			System.out.println("usage: java SubstringSearch <filename> <pattern_1> <pattern_2> ... <pattern_n>.");
		}
    }
}
