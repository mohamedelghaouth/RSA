import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class Test {
	
	


	public static void main(String[] args) {
		 
		String s= "ecole nationale des sciences appliquee";	
		RSA1 r1=new RSA1(50);
		System.out.println("Le message à crypter est : "+s);
		System.out.println(" ");
		r1.decripter(r1.cripter(r1.split_to_blocs(r1.convert_to_bited(s))));
		
		
	
	}
	
}
