import java.io.*;
import java.math.*;
public class RMH {

  public static int numVects=10;
  public static double Vects [][] = new double [numVects][2];
  public static double fitnessOfBest;
  public static int Clase [] = new int [numVects];
  static int E,D,V,N,N_2,L,L_2,FN=1,G,MM,B2M,Nx2,iTmp,Best,n;
  static int TOH=0;
  static String Resp;
  static double Pc, Pm;
  static double Norm, fTmp;
  static boolean SP;
  public static double data []=new double [317]; 
/*
 *		LA SIGUIENTE VARIABLE REGULA EL NÚMERO DE FUNCIONES DEFINIDAS
 */
  static int maxF=32, minF=1;
  static int maxN=500,minN=1;
  static int maxE=15, minE=0;
  static int maxD=60, minD=0;
  static int maxV=50, minV=1;
  static double maxPc=1f, minPc=.01f;
  static double maxPm=1f, minPm=.001f;
  static int maxG=1000000, minG=1;
  static int maxM=1,  minM=0;
  static int maxStringsInDeceptive=65536;
//
  public static double Var[][]=new double [1][maxV];
  public static String genoma [] = new String [1];
  public static String BestIndiv;
  public static double fitness[]= new double [1];
  public static int DecepFitness[]=new int [maxStringsInDeceptive];			//FUNCIÓN #23
  public static String DecepString[]=new String [maxStringsInDeceptive];	//FUNCIÓN #23
  public static BufferedReader Fbr,Kbr;
  public static boolean firstFromMario=true;

   public static void CreaParams() throws Exception {
	  try {
		Fbr=new BufferedReader(new InputStreamReader(new FileInputStream(new File("AGParams.dat"))));
	  }//endTry
	  catch (Exception e){
	    PrintStream Fps=new PrintStream(new FileOutputStream(new File("AGParams.dat")));
		Fps.println("1");	//1) Funcion
		Fps.println("50");	//2) Individuos
		Fps.println("4");	//3) Bits para Enteros
		Fps.println("25");	//4) Bits para Decimales
		Fps.println("2");	//5) Variables
		Fps.println("0.9");	//6) Pc
		Fps.println("0.01");//7) Pm
		Fps.println("100");	//8) Iteraciones
		Fps.println("0");	//9) Minimiza
	  }//endCatch
  }//endCreaParams

  public static void GetParams() throws Exception {
	  Fbr=new BufferedReader(new InputStreamReader(new FileInputStream(new File("AGParams.dat"))));
	  FN=Integer.parseInt(Fbr.readLine());
	  N =Integer.parseInt(Fbr.readLine());
	  E =Integer.parseInt(Fbr.readLine());
	  D =Integer.parseInt(Fbr.readLine());
	  V =Integer.parseInt(Fbr.readLine());
	  Pc=Double.valueOf(Fbr.readLine()).floatValue();
	  Pm=Double.valueOf(Fbr.readLine()).floatValue();
	  G =Integer.parseInt(Fbr.readLine());
	  MM=Integer.parseInt(Fbr.readLine());
  }//endGetParams

  public static void UpdateParams() throws Exception {
	PrintStream Fps=new PrintStream(new FileOutputStream(new File("AGParams.dat")));
	Fps.println(FN);			//1) Funcion
	Fps.println(N);				//2) Individuos
	Fps.println(E);				//3) Bits para Enteros
	Fps.println(D);				//4) Bits para Decimales
	Fps.println(V);				//5) Variables
	Fps.printf("%8.6f\n",Pc);	//6) Pc
	Fps.printf("%8.6f\n",Pm);	//7) Pm
	Fps.println(G);				//8) Iteraciones
	Fps.println(MM);			//9) Minimiza
  }//endUpdateParams
  
  public static void DispParams() throws Exception {
	System.out.println();
	System.out.println("1) Funcion a optimizar:     "+FN);
//	System.out.println("2) Numero de individuos:    "+ N);
	System.out.println("3) Bits para enteros:       "+ E);
	System.out.println("4) Bits para decimales:     "+ D);
	System.out.println("5) Numero de variables:     "+ V);
	System.out.println("** Long. del genoma:        "+ L);
//	System.out.printf ("6) Prob. de cruzamiento:    %8.6f\n",Pc);
//	System.out.printf ("7) Prob. de mutacion:       %8.6f\n",Pm);
	System.out.println("8) Numero de iteraciones:   "+ G);
	System.out.println("9) Minimiza[0]/Maximiza[1]: "+MM);
  }//endDispParams

  public static boolean CheckParams(int Opcion) {
	switch(Opcion) {
		case 1: {FN=iTmp; if (FN<minF|FN>maxF)   return false; break;}
		case 2: {N =iTmp; if (N<minN|N>maxN)     return false; break;}
		case 3: {E =iTmp; if (E<minE|E>maxE)     return false; break;}
		case 4: {D =iTmp; if (D<minD|D>maxD)     return false; break;}
		case 5: {V =iTmp; if (V<minV|V>maxV)     return false; break;}
		case 6: {Pc=fTmp; if (Pc<minPc|Pc>maxPc) return false; break;}
		case 7: {Pm=fTmp; if (Pm<minPm|Pm>maxPm) return false; break;}
		case 8: {G =iTmp; if (G<minG|G>maxG)     return false; break;}
		case 9: {MM=iTmp; if (MM<minM|MM>maxM)   return false; break;}
	}//endSwitch
	return true;
  }//endCheckParams

  public static void CalcParams() {
	N_2=N/2;
	Nx2=N*2;
	genoma = new String [Nx2];
	fitness= new double [Nx2];
	Norm=Math.pow(2,D);
	L=V*(1+E+D);
	L_2=L/2;
	B2M=(int)((double)L*Pm);				//Bits to Mutate
  }//endCalcParams

  public static void Modify() throws Exception {
    Kbr = new BufferedReader(new InputStreamReader(System.in));
  	String Resp;
	while (true){
		CalcParams();
		DispParams();
		System.out.print("\nModificar (S/N)? ");
		Resp=Kbr.readLine().toUpperCase();
		if (!Resp.equals("S")&!Resp.equals("N")) continue;
		if (Resp.equals("N")) return;
		if (Resp.equals("S")){
			int tFN=FN, tN=N, tE=E, tD=D, tV=V;
			double tPc=Pc, tPm=Pm; int tG=G, tMM=MM;
			while (true){
				System.out.print("Opcion No:       ");
				int Opcion;
				try{
					Opcion=Integer.parseInt(Kbr.readLine());
				}//endTry
				catch (Exception e){
					continue;
				}//endCatch
				if (Opcion<1|Opcion>9)
					continue;
				//endIf
				System.out.print("Nuevo valor:     ");
				iTmp=1;
				fTmp=1;
				try{
					if (Opcion==6|Opcion==7)
						fTmp=Double.parseDouble(Kbr.readLine());
					else
						iTmp=Integer.parseInt(Kbr.readLine());
					//endIf
				}//endTry
				catch (Exception e){
					continue;
				}//endCatch
				boolean OK=CheckParams(Opcion);
				if (!OK){
					FN=tFN; N=tN; E=tE; D=tD; V=tV;
					Pc=tPc; Pm=tPm; G=tG; MM=tMM;
					System.out.println("Error en la opcion # "+Opcion);
					continue;
				}//endIf
			break;
			}//endWhile
		}//endIf
	}//endWhile
  }//endModify

  public static void IndividuoInicial(){
	/*
	 *Genera TOP of Hill aleatoriamente
	 */
	genoma[TOH]="";
	for (int j=0;j<L;j++){
		if (j==0&SP){genoma[TOH]=genoma[TOH]+"0";continue;}
		if (Math.random()<0.5)
			genoma[TOH]=genoma[TOH]+"0";
		else
	  		genoma[TOH]=genoma[TOH]+"1";
	  	//endIf
	}//endFor
	BestIndiv=genoma[TOH];
	if (MM==0)					// Minimize - worst case
		fitnessOfBest=+1000000000;
	else						// Maximize - worst case
		fitnessOfBest=-1000000000;
	//}endif
  }//endIndividuoInicial

  public static void GetFenotipoDelTOH(){
	double Var_k;
	String G=genoma[TOH];
	int j=0;
	for (int k=0;k<V;k++){							// Variable
		String s=G.substring(j,j+1);				// Signo
		j++;
		if (G.substring(j,j+1).equals("0")) Var_k=0; else Var_k=1;
		for (int l=1;l<(E+D);l++){
			Var_k=Var_k*2;
			j++;
			if (G.substring(j,j+1).equals("1"))
				Var_k=Var_k+1;
			//endIf
		}//endFor									** Otro bit
		if (s.equals("1")) Var_k=-Var_k;
		Var[TOH][k]=Var_k/Norm;					//Para los decimales
		j++;
	}//endFor										** Otra variable
	return;
  }//endGetFenotipoDelTOH

  public static void Muta() {
	// NÚMERO DE BITS A MUTAR (AL MENOS UNO)
	int M=-1; while (M<1|M>=L) M=(int)(Math.random()*L);
	for (int i=0;i<M;i++){
		// BIT A MUTAR
		int nBit=-1; while (nBit<0|nBit>=L) nBit=(int)(Math.random()*L);
		if (nBit==0&SP) continue;	// NO MUTAR SI SOLO SON POSITIVOS
		String mBit="0";
		String G=genoma[TOH];
		// 1) SI EL BIT ESTÁ EN UN LUGAR INTERMEDIO
		if (nBit!=0&nBit!=L-1){
	 		if (G.substring(nBit,nBit+1).equals("0")) mBit="1";
			genoma[TOH]=G.substring(0,nBit)+(mBit)+(G.substring(nBit+1));
			continue;
		}//endif
		// 2) SI EL BIT ES EL PRIMERO
		if (nBit==0){
			if (G.substring(0,1).equals("0")) mBit="1";
			genoma[TOH]=mBit+(G.substring(1));
			continue;
		}//endif
		// 3) SI EL BIT ES EL ÚLTIMO
//		if (nBit==L-1){
			if (G.substring(L-1).equals("0")) mBit="1";
			genoma[TOH]=G.substring(0,L-1)+(mBit);
			continue;
		//}endif
	}//endFor
	return;
  }//endMuta

  public static void Evalua(double fitness[],String genoma[]) throws Exception{
	double F=0;int Best;
	switch(FN) {
		case 1: F=AGF.F01(Var[0][0]);					  break;
		case 2: F=AGF.F02(Var[0][0],Var[0][1]);			  break;
		case 3: F=AGF.F03(Var[0][0],Var[0][1]); 		  break;
		case 4: F=AGF.F04(Var[0][0],Var[0][1]); 		  break;
		case 5: F=AGF.F05(Var[0][0],Var[0][1],Var[0][2]); break;
		case 6: F=AGF.F06(Var[0][0],Var[0][1],Var[0][2]); break;
		case 7: F=AGF.F07(Var[0][0]); 				 	  break;
	}//endSwitch
	fitness[TOH]=F;
	//endFor
	return;
  }//endEvalua

/*		Selecciona el TOH
 *
 */
  public static void Selecciona() {
	if (MM==0){					// Minimiza
		if (fitness[TOH]<fitnessOfBest){
  			fitnessOfBest=fitness[TOH];
  		}//endIf
	}else{						// Maximiza
		if (fitness[TOH]>fitnessOfBest){
  			fitnessOfBest=fitness[TOH];
  		}//endIf
	}//endIf
	BestIndiv=genoma[TOH];
	return;
  }//endSelecciona


  public static void main(String[] args) throws Exception {
	BufferedReader Fbr,Kbr;
    Kbr = new BufferedReader(new InputStreamReader(System.in));
    SP=false;
	try {
		Fbr=new BufferedReader(new InputStreamReader(new FileInputStream(new File("SOLO_POS.FLG"))));
	    System.out.println("*** \"SOLO_POS.FLG\" ENCONTRADO. SOLAMENTE SE GENERAN VALORES POSITIVOS ***");
	    SP=true;
	}//endTry
	catch (Exception e){
	    System.out.println("*** SE GENERAN VALORES POSITIVOS Y NEGATIVOS ***");
	    SP=false;
	}//endCatch
//
	CreaParams();							//Crea archivo si no existe
	GetParams();							//Lee parametros de archivo
	for (int loop=1;loop<=10000;loop++){
	  if (loop>1){
		System.out.println("\nOtra funcion (S/N)?");
		Resp=Kbr.readLine().toUpperCase();
		if (!Resp.equals("S")){
			System.out.println("\n*** FIN DE ESCALADOR ***\n");
			return;
		}//endIf		
	    try {
		  Fbr=new BufferedReader(new InputStreamReader(new FileInputStream(new File("SOLO_POS.FLG"))));
	      System.out.println("*** \"SOLO_POS.FLG\" ENCONTRADO. SOLAMENTE SE GENERAN VALORES POSITIVOS ***");
	      SP=true;
	    }//endTry
	    catch (Exception e){
	      System.out.println("*** SE GENERAN VALORES POSITIVOS Y NEGATIVOS ***");
	      SP=false;
	    }//endCatch
	  }//endIf
	  Modify();								//Modifica valores
	  CalcParams();							//Calcula parametros
	  UpdateParams();						//Graba en archivo
/*
 *		EMPIEZA EL ESCALADOR
 */
	  int Gtemp=G;
 	  IndividuoInicial();					//Genera el individuo inicial
  	  GetFenotipoDelTOH();					//Decodifica la priemera cadena
	  Evalua(fitness,genoma);				//Evalua al individuo
	  for (int i=0;i<G;i++){
		Muta();								//Muta
        GetFenotipoDelTOH();				//Decodifica la i-ésima cadena
		Evalua(fitness,genoma);				//Evalua
		Selecciona();						//Selecciona al mejor
	  }//endFor
	  System.out.printf("Mejor valor encontrado:  = %15.7f\n",fitnessOfBest);
	  for (int i=0;i<V;i++){
	  		System.out.println("Variable "+i+": "+Var[TOH][i]);
	  }//endFor
	  G=Gtemp;
	}//endLoop
  }//endMain
	}
	 //endClass
