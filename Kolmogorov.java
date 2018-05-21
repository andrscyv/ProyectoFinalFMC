import java.io.*;
import java.math.*;

public class Kolmogorov {
	
  // Declaración de variables
  public static String mejorMT;
  public static String maquinaAct;
  public static String mejorCadena;
  public static String cadenaAct;
  public static String cadenaMeta;
  public static String obj;
  public static double fitnessOfBest,fitness;
  static int steps;

  static RandomAccessFile ra;
  static PrintStream ps;
  static BufferedReader br;

  //DEFINICION DE TAMAÑO MAQUINA
  public static int L = 1024;

  //Tamaño cinta
  //public static int tamCinta = 2000;
  public static int tamCinta = 200;

  //Numero de pasos maximo para la mt
  public static int maxPmt = 150;

  //Cinta de la TM
  public static String cinta;

  // Carga la cadena meta
  public static void cargaObjetivo() throws IOException{
	br = new BufferedReader(new InputStreamReader(System.in));
	String car, fileTarget;
	String fileName = "";
	String resp = ""; 
	boolean flag = false;
	int bytesDatos;

	while(!flag){
		System.out.println("Inserte el nombre del archivo que contiene la cadena meta:");
		try{
			fileName = br.readLine();
			ra = new RandomAccessFile(new File(fileName), "r");
			flag=true;
		}catch(Exception e){
			System.out.println("No se encontró el archivo "+fileName+".");
		}
	}
	
	while(true){
		System.out.println("Los datos están en binario-ASCII? (S/N)");
		
		try{
			resp = br.readLine().toUpperCase();
		}catch(Exception e){
			System.out.println("Error en la respuesta");
		}

		if (resp.equals("S") || resp.equals("N")){
			break;
		}
	}

	// Se intenta cargar la cadena 
	if(resp.equals("N")){
		byte X;
		bytesDatos = 0;

		System.out.println("Deme el nombre del archivo de salida de imagen numérica:");
		fileTarget = br.readLine();

		ps = new PrintStream(new FileOutputStream(new File(fileTarget)));
			
		while(true){
			ra.seek(bytesDatos);
				
			try{
				X = ra.readByte();
			}catch(Exception e){
				break;
			}

			bytesDatos++;
		} // end while

		ra.close();

		System.out.println("Se leyeron "+bytesDatos+" datos.\n");

		// Convierte en binario - ASCII
		int Y,T;
		ra = new RandomAccessFile(new File(fileName), "r");
		for (int i = 0; i < bytesDatos; i++) {
			ra.seek(i);
			Y = ra.readByte();
			T = Y;
			car  = "";

			for (int j = 0; j < 8; j++) {
				car = ((Y%2 == 0) ? "0"+car:"1"+car);
				Y/=2;
			}//end for
				
			obj = obj + car;
		} // end for

		ps.printf(obj);
		ps.println();
		ps.close();
	}else{
		BufferedReader brCinta;
		ra.close();

		brCinta = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));
		obj = brCinta.readLine();

		brCinta.close();
	}

	while(tamCinta/2 < obj.length())
		tamCinta = tamCinta*2;

	maxPmt = Math.round(3*tamCinta/4);
  }

  public static void formateaInput(){

  	StringBuilder aux = new StringBuilder(tamCinta);
	for(int i = 0; i < tamCinta; i++)
		aux.append("0");

	cinta = aux.toString();
	StringBuilder zeros = new StringBuilder(tamCinta);

	for(int i = 0; i < tamCinta/2; i++)
		zeros = zeros.append("0");
	zeros.append(obj);

	for(int i = 0; i < (tamCinta/2-obj.length()); i++)
		zeros.append("0");

	cadenaMeta = zeros.toString();
  }

  public static void maquinaInicial(){
	/*
	 * Genera TOP of Hill aleatoriamente
	 */
	maquinaAct = "";

	for (int j=0;j<L;j++){
		if (Math.random()<0.5)
			maquinaAct = maquinaAct + "0";
		else
	  		maquinaAct = maquinaAct + "1";
	  	//endIf
	}//endFor

	mejorMT = maquinaAct;

	
	fitnessOfBest=+1000000000;
  }//endMaquinaInicial

  // Obtiene la cinta generada por ma MT
  public static void getSimulacion(){

	cadenaAct = UTM.NewTape(maquinaAct,cinta,maxPmt,tamCinta/2);
  }//endGetFenotipoDelTOH

  public static void muta() {
	// NÚMERO DE BITS A MUTAR (AL MENOS UNO)
	int M=-1; 

	while (M<1|M>=L) M=(int)(Math.random()*L);

	for (int i=0;i<M;i++){
		// BIT A MUTAR
		int nBit = -1; 
		while (nBit<0|nBit>=L) nBit=(int)(Math.random()*L);

		String mBit = "0";
		String aux = maquinaAct;

		// 1) SI EL BIT ESTÁ EN UN LUGAR INTERMEDIO
		if (nBit!=0 & nBit!=L-1){
	 		if (aux.substring(nBit,nBit+1).equals("0")) mBit="1";
			maquinaAct = aux.substring(0,nBit)+(mBit)+(aux.substring(nBit+1));
			continue;
		}//endif

		// 2) SI EL BIT ES EL PRIMERO
		if (nBit==0){
			if (aux.substring(0,1).equals("0")) mBit="1";
			maquinaAct = mBit+(aux.substring(1));
			continue;
		}//endif

		// 3) SI EL BIT ES EL ÚLTIMO
		if (nBit==L-1){
			if (aux.substring(L-1).equals("0")) mBit="1";
			maquinaAct = aux.substring(0,L-1)+(mBit);
			continue;
		} //endif
	}//endFor
	return;
  }//endMuta

  public static void evalua() throws Exception{
	double F=0;

	// Checa si la cadena se parece bit a bit
	for(int i = 0; i < tamCinta; i++)
		F = ((cadenaAct.charAt(i) == cadenaMeta.charAt(i)) ? F:F+1);
	
	// Checa si las cadenas se parecen por pares
	for(int i = 0; i < tamCinta-1; i++)
		F = ((cadenaAct.substring(i,i+2).equals(cadenaMeta.substring(i,i+2))) ? F:F+2);

	// Checa si las cadenas se parecen por tercias
	for(int i = 0; i < tamCinta-2; i++)
		F = ((cadenaAct.substring(i,i+3).equals(cadenaMeta.substring(i,i+3))) ? F:F+4);


	fitness  = F;
	//endFor
	return;
  }//endEvalua

  public static int distanciaHamming(String approx){
	  int d = 0;

	  for (int i = 0; i < obj.length(); i++) {
		  d = ((approx.charAt(i) == obj.charAt(i)) ? d:d+1);
	  }

	  return d;
  } //endDistanciaHamming

  public static void main(String[] args) throws Exception {
	double sim;
	int n;
	String resp;
	boolean flag  = false;

	//obj = "0100110001110101010101010100100100010101001100000010101111";
	//obj = "1011";
	// obtiene la cadena meta
	cargaObjetivo();
	formateaInput();

	n = obj.length();

	steps = 5000;

	br = new BufferedReader(new InputStreamReader(System.in));
	System.out.println("\nSe realizarán "+steps+" iteraciones, ¿quieres cambiarlo? (S/N)");
	resp = br.readLine().toUpperCase();

	if(resp.equals("S")){
		while(!flag){
			try{
				System.out.println("¿Cuántas iteraciones quieres hacer?");
				resp = br.readLine();

				steps = Integer.parseInt(resp);

				if(steps < 0)
					System.out.println("Ingresa un número mayor a cero.\n");
				else
					flag = true;

			}catch(Exception e){
				System.out.println("Ingresa un número válido.\n");
			}

		}
	}

	maquinaInicial(); // crea la maquina inicial aleatoriamente
	getSimulacion();
	evalua();			//Evalua al individuo
	fitnessOfBest = fitness;

	for (int i=0;i<steps;i++){
		muta();					//Muta
		getSimulacion();				
		evalua();				//Evalua
		
		if(fitness < fitnessOfBest){
			mejorMT = maquinaAct;
			mejorCadena = cadenaAct;
			fitnessOfBest = fitness;
		}

		if(fitnessOfBest == 0)
			break;					// Si ya se encontró la exacta entonces deja de iterar
	}//endFor
	
	sim = distanciaHamming(mejorCadena.substring(tamCinta/2,tamCinta/2 + n));
	sim = (1-sim/n)*100;

	ps = new PrintStream(new FileOutputStream(new File("MejorMT.txt")));
	ps.println(mejorMT);
	ps.close();

	ps = new PrintStream(new FileOutputStream(new File("MejorCinta.txt")));
	ps.println(mejorCadena);
	ps.close();

	System.out.printf("\nMejor valor encontrado: "+fitnessOfBest);
	System.out.println();

	System.out.println("Objetivo:    "+obj);
	System.out.println("Mejor cinta: "+mejorCadena.substring(tamCinta/2,tamCinta/2 + n));

	System.out.printf("\nSimilitud: %.2f %% \n", sim );

	System.out.println("\nLa descripción de la máquina de Turing se encuentra en MejorTM.txt y la cinta se encuentra en MejorCinta.txt\n");
  }//endMain


}
 //endClass
