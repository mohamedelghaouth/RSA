import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.event.CellEditorListener;



public class RSA1 {
	
	private BigInteger n, e,d; // (e,n) clé public, (d,n) clé privé
	private int taillecle, blocsize;
	private static BigInteger s;                               
	

	public RSA1(int taillecle){
		this.taillecle = taillecle; 
		
		BigInteger p, q;
		
		p = BigInteger.probablePrime(taillecle/2, new Random());//p grand premier 
				
		q  = BigInteger.probablePrime(taillecle/2, new Random());	//q grand premier
		
		while(q==p) {
			q  = BigInteger.probablePrime(taillecle/2, new Random());	//q grand premier
		}
		
		this.n = p.multiply(q);	//n = p*q	le nombre de bits de n donne la taille de la clé 
		

		BigInteger p2 = p.subtract(new BigInteger("1"));	//p2 = p-1
		BigInteger q2 = q.subtract(new BigInteger("1"));	//q2 = q-1
		BigInteger w = p2.multiply(q2);						//w = (p-1)*(q-1) 
		
		this.e = new BigInteger(taillecle + 1 , new Random()); // e aleatoire > p, q
		
		while ((!this.e.gcd(w).toString().equals("1")) || (this.e.compareTo(w) != -1 )){ // e premier avec w et e < w
			this.e = new BigInteger(taillecle + 1, new Random());
		}
		
		this.d = this.e.modInverse(w);	// calcul du modulo inverse à e pour avoir la clé privée (cf algorithme d'euclide etendu) 
		
		
		this.blocsize =(( taillecle-1)/8)+1;	//On aura donc des blocs toujours inferieur à n
		System.out.println(" ");
		System.out.println(this.toString());
		System.out.println(" ");


	}

	
	public byte[] convert_to_bited(String messg) {
		
		System.out.println("convertie le message en code ascii :");
		System.out.println(" ");
		
		byte[] bite_liste=new byte[messg.length()];
		bite_liste=messg.getBytes();
		for (int i = 0; i < bite_liste.length; i++) {
			System.out.print(bite_liste[i]+ " ");
		}
		System.out.println(" ");
		System.out.println(" ");

		return bite_liste;
	}
	
	public ArrayList<BigInteger> split_to_blocs(byte[] bite_liste) {
		
		System.out.println("decoupage du message en bloc:");
		System.out.println(" ");
		
		String s ="";
		String s1 ="";

		for (int i = 0; i < bite_liste.length; i++) {
			s+=bite_liste[i]+ "";
		}
				
		BigInteger bite_concat =new BigInteger(s);

		ArrayList<BigInteger> blocs=new ArrayList<BigInteger>();
		
		int j=0;
		
		int nmbfois=(bite_concat.toString().length())/(this.blocsize);
		int rest=(bite_concat.toString().length())%(this.blocsize);
		
		
		for (int i = 1; i <= nmbfois; i++) {
			blocs.add(new BigInteger(bite_concat.toString().substring(j, j+this.blocsize)));
			s1+=bite_concat.toString().substring(j, j+this.blocsize)+ " ";
			j+=this.blocsize;
		}
		
		if (rest!=0) {
			s= bite_concat.toString().substring(j,bite_concat.toString().length());
			s1+=s+ " ";
			blocs.add(new BigInteger(s));
			blocs.add(new BigInteger(s.length()+""));

		}
		else 
		{
			blocs.add(new BigInteger(this.blocsize+""));
		}
		
		
		System.out.println(s1);
		System.out.println(blocs);
		System.out.println(" ");
		return blocs;
	}
	
	
	public ArrayList<BigInteger> cripter(ArrayList<BigInteger> blocs) {
		
		ArrayList<BigInteger> blocs_cripter= new ArrayList<BigInteger>();
		String s="";
		String mssg_cypte="";

		for (int i = 0; i < blocs.size(); i++) 
		{
			
			if (i==(blocs.size()-1)) {
				blocs_cripter.add(blocs.get(i));
				
			} else {
				blocs_cripter.add(blocs.get(i).modPow(e, n));
				s+=blocs.get(i).modPow(e, n).toString()+" ";
			}
				
		}
		
		
		System.out.println("les blocs crypte");
		System.out.println(blocs_cripter);
		System.out.println(s);
		System.out.println("");

		System.out.println("le message crypté est :");

		for (BigInteger bigInteger : blocs_cripter) {
			
			byte tmp = (bigInteger.mod(new BigInteger("255"))).byteValue();
			System.out.print((char) tmp);
			
		}
		
		System.out.println(" ");
		System.out.println(" ");

		return blocs_cripter;
	
	}


	public void decripter (ArrayList<BigInteger> blocs_cripter) {
		
		ArrayList<BigInteger> blocs_decripter=new ArrayList<BigInteger>();
		ArrayList<Byte> list_byte_initial=new ArrayList<Byte>();
				
		String s="";
		String tmp="";
		
		int j=0;
		
		
		for (int i = 0; i < blocs_cripter.size(); i++) 
		{
			
			if (i==(blocs_cripter.size()-1)) {
				blocs_decripter.add(blocs_cripter.get(i));
				
			} else {
				blocs_decripter.add(blocs_cripter.get(i).modPow(d, n));

			}
				
		}
		
		
		for (int i = 0; i < blocs_decripter.size(); i++) {
			
			if (i==(blocs_decripter.size()-1)) { continue;}
			
			if (blocs_decripter.get(i).toString().length()<this.blocsize) 
			{
				
				if (i==(blocs_decripter.size()-2)) {
					tmp="";
					if (blocs_decripter.get(i).toString().length()<blocs_decripter.get(i+1).intValue())
					{
						for (int t = 0; t <(blocs_decripter.get(i+1).intValue()-blocs_decripter.get(i).toString().length()) ; t++)
						{ 
						tmp +="0";
						}
					}
			
					s+=tmp+blocs_decripter.get(i).toString();
										
				} else {
					tmp="";
					for (int t = 0; t <(this.blocsize-blocs_decripter.get(i).toString().length()) ; t++)
					{ 
					tmp +="0";
					}
					s+=tmp+blocs_decripter.get(i).toString();
				}
					
			}
			else 
			{
				s+=blocs_decripter.get(i).toString();
			}
			
		}
		
		System.out.println("decryptage");
		System.out.println(" ");

		
		System.out.println("les blocs initiales :");
		System.out.println(blocs_decripter);
		System.out.println(" ");

		
		System.out.println("chaine code ascii initiale:");
		System.out.println(s);
		System.out.println(" ");

		
		while (j<s.length())
		{
			String tmp1="";			
			if (j+3<=s.length()) 
			{
				tmp1=s.substring(j, j+3);
				
				if ((Integer.parseInt(tmp1)>32)&&(Integer.parseInt(tmp1)<=127)) 
				{
					list_byte_initial.add((new Byte(tmp1)));
					j+=3;
				} 
				else 
				{
					tmp1=s.substring(j, j+2);
					list_byte_initial.add((new Byte(tmp1)));
					j+=2;
				}	
			} 
			else 
			{
				tmp1=s.substring(j, j+2);
				list_byte_initial.add((new Byte(tmp1)));
				j+=2;
			}	
		}
		
		System.out.println("la chaine de cractere initiale");
		
		for (Byte byte1 : list_byte_initial) {System.out.print((char)byte1.byteValue());}
	
	}
	
	
	@Override
	public String toString() {
		return "RSA [n=" + n + ", e=" + e + ", d=" + d + ", taillecle=" + taillecle + ", blocsize=" + blocsize + "]";
	}
	
	
	
	
}
