import java.io.*;
import java.math.*;
public class Kolmogorov {

  public static int numVects=10;
  public static double Vects [][] = new double [numVects][2];
  public static double fitnessOfBest;
  public static int Clase [] = new int [numVects];
  static int E,D,V,N,N_2,L_2,FN=1,G,MM,B2M,Nx2,iTmp,Best,n;
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

  //DEFINICION DE TAMAÑO MAQUINA
  public static int L = 1024;

  //DEFINICION CADENA inptu
  public static String input;


  //Definicion cadena obj
  public static String obj;

  //Tamaño cinta DEBE SER NUMERO PAR
  public static int tamCinta = 100;

  //Numero de pasos maximo para la mt
  public static int maxPmt = 50;

  //La lista despues de correr la mt
  public static String fenomaTOH;

  //Mejor output hasta ahora
  public static String mejorCinta;

  //Cinta de la mt
  public static String cinta;

  public static void formateaInput(){

  	StringBuilder aux = new StringBuilder(tamCinta);
	for(int i = 0; i < tamCinta; i++)
		aux.append("0");

	cinta = aux.toString();
	StringBuilder zeros = new StringBuilder(tamCinta);

	for(int i = 0; i < tamCinta/2; i++)
		zeros = zeros.append("0");
	zeros.append(input);

	for(int i = 0; i < (tamCinta/2-input.length()); i++)
		zeros.append("0");
	obj = zeros.toString();
	// System.out.println(obj);
	// System.out.println(obj.length());


  }


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
	//System.out.println(genoma[TOH]);
	fenomaTOH = UTM.NewTape(genoma[TOH],cinta,maxPmt,tamCinta/2);
	//System.out.println(fenomaTOH);
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
	double F=0;
	int Best;

	for(int i = 0; i < tamCinta; i++)
		F = ((fenomaTOH.charAt(i) == obj.charAt(i)) ? F:F+1);

	for(int i = 0; i < tamCinta-1; i++)
		F = ((fenomaTOH.substring(i,i+2).equals(obj.substring(i,i+2))) ? F:F+5);

	for(int i = 0; i < tamCinta-2; i++)
		F = ((fenomaTOH.substring(i,i+3).equals(obj.substring(i,i+3))) ? F:F+8);


	fitness[TOH]=F;
	//System.out.println(fitness[TOH]);
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
  			BestIndiv=genoma[TOH];
  			mejorCinta = fenomaTOH;

  		}//endIf
	}else{						// Maximiza
		if (fitness[TOH]>fitnessOfBest){
  			fitnessOfBest=fitness[TOH];
  			BestIndiv=genoma[TOH];
  		}//endIf
	}//endIf
	return;
  }//endSelecciona


  public static void main(String[] args) throws Exception {
					//Graba en archivo


/*
 *		EMPIEZA EL ESCALADOR

 */	 
	input = "101011101";
	formateaInput();
 	   MM = 0;
	  int Gtemp=G;
 	  IndividuoInicial();					//Genera el individuo inicial
  	  GetFenotipoDelTOH();	
  	  G = 2000;
  	  //System.out.println("Objetivo: "+obj);
  	  //System.out.println("Fenoma:   "+fenomaTOH);				//Decodifica la priemera cadena
	   Evalua(fitness,genoma);			//Evalua al individuo
	  for (int i=0;i<G;i++){
		Muta();								//Muta
        GetFenotipoDelTOH();				//Decodifica la i-ésima cadena
		Evalua(fitness,genoma);				//Evalua
		Selecciona();	
		if(fitnessOfBest == 0)
			break;					//Selecciona al mejor
	  }//endFor
	  System.out.printf("Mejor valor encontrado: "+fitnessOfBest);
	  for (int i=0;i<V;i++){
	  		System.out.println("Variable "+i+": "+Var[TOH][i]);
	  }//endFor
	  System.out.println();
	  System.out.println("Mejor cinta: "+mejorCinta);
	  System.out.println("Objetivo:    "+obj);
	  System.out.println(G);
	  G=Gtemp;
	
  }//endMain


	}
	 //endClass
