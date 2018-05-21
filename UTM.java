public class UTM {    
    public static String NewTape(String TT, String Cinta, int N, int P) {
        int indiceLlenar = 0, indiceTT=0,estadoActual=0,productividad=0,pasosHALT=-1,leido,cabeza = P;
        int[] bin = {0,1};
        int[][] siguienteEstado = new int[2][TT.length()/16];
        boolean[][] movimientoCabeza = new boolean[2][TT.length()/16];
        boolean[][] escrituraCabeza = new boolean[2][TT.length()/16];
        while(indiceTT < TT.length()){
            for(int i:bin){
                escrituraCabeza[i][indiceLlenar] = ((TT.charAt(indiceTT+(i*8))=='1') ? true:false);
                movimientoCabeza[i][indiceLlenar] = ((TT.charAt(indiceTT+1+(i*8))=='1') ? true:false);
                siguienteEstado[i][indiceLlenar] = Integer.parseInt(TT.substring(indiceTT+2+(i*8), indiceTT+8+(i*8)),2);    
            }
            indiceLlenar++;
            indiceTT+=16;
        }        
        StringBuilder cintaFinal = new StringBuilder(Cinta);
        boolean alcanzoHALT = false;
        try{
            for (int i = 0; i < N; i++) {
                if(estadoActual!=63){
                    leido = cintaFinal.charAt(cabeza)-'0';                
                    //productividad = (!escrituraCabeza[leido][estadoActual] && cintaFinal.charAt(cabeza) != '0') ? productividad-1:productividad;
                    cintaFinal.setCharAt(cabeza, escrituraCabeza[leido][estadoActual] ? '1':'0' );
                    cabeza = movimientoCabeza[leido][estadoActual] ? cabeza-1:cabeza+1;
                    estadoActual = siguienteEstado[leido][estadoActual];
                }else{
                    pasosHALT = i;
                    alcanzoHALT=true;
                    break;
                }
            }

            return cintaFinal.toString();
        }catch(Exception e){ 
            /*
            * Regresa la cinta final.
            * Si la cabeza se salió de la cinta regresa la cinta original
            */
            return Cinta;    
        }
    }
}