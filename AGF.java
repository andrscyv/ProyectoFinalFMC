import java.io.*;

class AGF {
  
  public static double F01(double X){
  double Y;
  /*
   * 1)
   *
   *	Resolver la siguiente ecuación cúbica:
   *
   *	Y=X^3+2.5X^2-2X+1
   *
   *	MINIMIZAR CON LAS RESTRICCIONES
   *
   *  X=-3.2180565 --> Y=0.0000000
   */
   Y=X*X*X+2.5*X*X-2*X+1;
   if (Y>0&&Y<=1) return Y;
   if (Y<1)     return 1000d;
   if (Y<10)    return 10000d;
   if (Y<100)   return 100000d;
   if (Y<1000)  return 1000000d;
   if (Y<10000) return 100000000d;
   return 10000000000d;
  }//endF02

  public static double F02(double X,double Y){
/*
 * (2)
 *
 *	Maximizar
 *
 *	X=5
 *	Y=3
 *	F(X,Y)=0
 *
 */
 	return -(X-5)*(X-5)-(Y-3)*(Y-3);
  }//endF03

  public static double F03(double X,double Y){
/*
 * (3)
 *
 *	Minimizar
 *
 *	Z --> 0
 *
 *	(X,Y) = (1.198231,0.706163); Z=0.00000
 *
 *
 */
	double Z;
	Z=Math.pow(X,2)-2*X*Y+Math.pow(Y,3)+X+Y-2; 
	if (Z<0){
		if (Z>-100)
  			return 100000;
  		else
  			if (Z>-1000)
  				return 1000000;
  			else
  				if (Z>-10000)
  					return 100000000;
  				else
  					return 1000000000;
  				//endif
  			//endif
  		//endif
	}//endif
	return Z;
  }//endF04

  public static double F04(double X, double Y){
/*
 * (4)
 *
 */
//  2X+3Y-13=0
//   X-2Y+ 4=0
//  ----------
//  3X+ Y- 9=0
//  X=2; Y=3
/*
 *	USAR:
 *		  2 BITS ENTEROS
 *		 40 BITS DECIMALES
 *		 [0] --> MINIMIZAR
 */
 	double R1,R2;
	int C=0;
	R1=2*X+3*Y-13;
	R2=X-2*Y+4;
	if (R1>=0) C=C+1;
	if (R2>=0) C=C+1;
	if (C==2) return 3*X+Y-9;
	if (C==1) return 5000000d;
	return 10000000d;
  }//endF05

  public static double F05(double X,double Y,double Z){
 /*
 * (5)
 *
 */
 	/*
  	 *	SISTEMA DE ECUACIONES SIMULTÁNEAS NO LINEALES (TRASCENDENTES)
  	 *
  	 *	SIN(X)-2X^2-Y   +3.158529=0
  	 *	COS(Y)+ Y^2-X+2Z-0.583853=0
  	 *	X^2   +Y^2 +Z^2 -6       =0
  	 *
  	 *	MINIMIZA
  	 *	  SIN(X)+COS(Y)-X^2+2Y^2+Z^2-X-Y+2Z-3.425324
  	 *	SUJETO A:
  	 *	SIN(X)-2X^2-Y   +3.158529>=0
  	 *	COS(Y)+ Y^2-X+2Z-0.583853>=0
  	 *	X^2   +Y^2 +Z^2 -6       >=0
  	 *
  	 *
  	 * X=+1; Y=-2; Z=-1
  	 * X=+1; Y=+2; Z=-1.25
  	 * X=+1; Y=+2; Z=-1.015625
  	 * X=+1; Y= 0; Z=-0.984375
  	 * E=4
  	 * D=40
  	 * V=3
  	 * [0] --> MINIMIZAR
  	 */
	double R1,R2,R3;
	int C=0;
  	R1=Math.sin(X)-2*X*X-Y+3.158529;
  	R2=Math.cos(Y)+Y*Y-X+2*Z-0.583853;
  	R3=X*X+Y*Y+Z*Z-6;
  	if (R1>=0) C=C+1;
  	if (R2>=0) C=C+1;
  	if (R3>=0) C=C+1;
  	// Si 3 condiciones satisfechas evalúa la función
  	if (C==3) return Math.sin(X)+Math.cos(Y)-X*X+2*Y*Y+Z*Z-X-Y+2*Z-3.425324;
  	// Si 2 condiciones satisfechas penalty=15M-(15M/3)*2
  	if (C==2) return 5000000d;
  	// Si 1 condición satisfecha    penalty=15M-(15M/3)*1
  	if (C==1) return 10000000d;
  	// Si 0 condiciones satisfechas penalty=15M-(15M/3)*0
  	return 15000000d;
  }//endF06

  public static double F06(double X,double Y,double Z){
/*
 * (6)
 *
	SISTEMA DE ECUACIONES SIMULTÁNEAS NO LINEALES
	ALGEBRÁICAS

  	X*X+3*Y-Z*Z*Z=-10;
  	X*X+Y*Y+Z*Z=6;
  	X*X*X-Y*Y+Z=2;
		
  	 * E=3
  	 * D=25
  	 * V=3
 	// X=+1; Y=-1; Z=+2
	*/
	double R1,R2,R3;
	int C=0;
  	R1=X*X+3*Y-Z*Z*Z+10;
  	R2=X*X+Y*Y+Z*Z-6;
  	R3=X*X*X-Y*Y+Z-2;
  	if (R1>=0) C=C+1;
  	if (R2>=0) C=C+1;
  	if (R3>=0) C=C+1;
  	if (C==3) return 2*X*X+X*X*X+3*Y+Z+Z*Z-Z*Z*Z+2;
  	if (C==2) return 5000000d;
  	if (C==1) return 10000000d;
  	return 15000000d;
  }//endF06

  public static double F07(double X){
  double Y;
/*
 * (7)
 *
*/
 /*
  * ECUACIÓN CUÁRTICA
  *	X^4-X^3+2.5x^2-2X-34.0625=0
   *  X=2.5
   */
  Y=Math.pow(X,4)-X*X*X+2.5*X*X-2*X-34.0625;
  if (Y<0){
  	if (Y>-100)
  		return 100000;
  	else
  		if (Y>-1000)
  			return 1000000;
  		else
  			if (Y>-10000)
  				return 100000000;
  			else
  				return 1000000000;
  			//endif
  		//endif
  	//endif
  }//endif
  return Y; 
  }//endF08

} //endClass
